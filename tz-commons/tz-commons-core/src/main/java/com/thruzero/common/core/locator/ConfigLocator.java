/*
 *   Copyright 2005-2011 George Norman
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package com.thruzero.common.core.locator;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.bookmarks.LocatorBookmark;
import com.thruzero.common.core.config.Config;
import com.thruzero.common.core.config.Config.ConfigInitParamKeys;
import com.thruzero.common.core.config.JFigConfig;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.InterfaceBindingRegistry;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.LocatorLogHelper;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.utils.ClassUtils;
import com.thruzero.common.core.utils.ClassUtils.ClassUtilsException;

/**
 * A utility used to create the initial instance of the registered {@code Config} implementation and return it for all
 * subsequent requests.
 * <p>
 * A client can specify the concrete {@code Config} instance to be created, as well the configuration file, by using the
 * {@link #getRegistry()} method or the {@link #setup(String, String)} convenience method. If no {@code Config} binding
 * has been registered by the first {@code locate()} request, then the locator will create and register a binding
 * defined by the environment variables or use the default implementation and file name (which is JFigConfig and
 * "config.xml"). After the {@code Config} instance has been created, the registry and setup methods will have no
 * further effect.
 * <p>
 * The following example registers "config/config.test.xml" as the config file and JFigConfig (the default) as the
 * config implementation. This is typically done by an init Servlet Filter.
 *
 * <pre>
 * <code>ConfigLocator.setup("config/config.test.xml", null);</code>
 * </pre>
 *
 * @author George Norman
 */
@LocatorBookmark(comment = "ConfigLocator")
public class ConfigLocator {
  private static final String DEFAULT_CONFIG_CLASS_NAME = JFigConfig.class.getName();

  private static final RegistryLocatorStrategy<Config> locatorStrategy = new ConfigLocatorStrategy();

  // ------------------------------------------
  // ConfigEnvironmentVarNames
  // ------------------------------------------

  /**
   * An interface that defines the names of environment variables used to optionally initialize the concrete
   * {@code Config} class (e.g., the path to the config file).
   */
  public interface ConfigEnvironmentVarNames {
    /**
     * Override the default config file path by setting this environment variable (e.g., "
     * {@code -Dcom.thruzero.common.core.config.CONFIG_FILE_PATH=/home/foo/bar/config.xml}")
     */
    String CONFIG_FILE_PATH_ENV_VAR = "com.thruzero.common.core.config.CONFIG_FILE_PATH";

    /**
     * Override the default {@code Config} implementation by setting this environment variable (e.g., "
     * {@code -Dcom.thruzero.common.core.config.CLASS_NAME=your.domain.YourConfig}")
     */
    String CONFIG_IMPL_ENV_VAR = "com.thruzero.common.core.config.CLASS_NAME";
  }

  // -----------------------------------------------------------
  // ConfigLocatorLogHelper
  // -----------------------------------------------------------

  public static class ConfigLocatorLogHelper extends LocatorLogHelper {
    public ConfigLocatorLogHelper(final Class<?> clazz) {
      super(clazz);
    }

    public void logSupplemental(final String configImplClassName) {
      if (getLogger().isDebugEnabled() && StringUtils.isEmpty(configImplClassName)) {
        String msg = "    Bound class is null, so will use ";
        String envClassName = System.getenv(ConfigEnvironmentVarNames.CONFIG_IMPL_ENV_VAR);

        if (StringUtils.isEmpty(envClassName)) {
          msg += "DEFAULT: '" + DEFAULT_CONFIG_CLASS_NAME + "'.";
        } else {
          msg += "class defined from Environment variable: '" + envClassName + "'.";
        }

        getLogger().debug(msg);
      }
    }
  }

  // ------------------------------------------
  // ConfigLocatorStrategy
  // ------------------------------------------

  /**
   * A specialization of the {@code RegistryLocatorStrategy} class, used to handle initialization of the {@code Config}
   * instance (e.g., retrieve the config file path from environment variable).
   */
  public static class ConfigLocatorStrategy extends RegistryLocatorStrategy<Config> {

    public ConfigLocatorStrategy() {
      super(Config.class);
    }

    @Override
    protected InterfaceToClassBinding<Config> getRegisteredBinding(final Class<?> interfaceClass) {
      InterfaceToClassBinding<Config> result = super.getRegisteredBinding(interfaceClass);

      if (result != null) {
        prepBinding(result);
      }

      return result;
    }

    @Override
    protected void registerDefaultBindings() {
      // prepBinding method will initialize with values from the system environment or use defaults.
      getRegistry().registerInterface(new InterfaceToClassBinding<Config>(Config.class, null));
    }

    /**
     * Returns a new binding where it's ensured that a concrete {@code Config} class and config file path are specified.
     * For any missing value, it first attempts to lookup a value from the system environment, using
     * {@code ConfigEnvironmentVarNames}. If not found, then it uses default values ("config.xml" and JFigConfig).
     */
    protected void prepBinding(final InterfaceToClassBinding<Config> binding) {
      // ensure configImplClassName
      String configImplClassName = binding.getInstanceClassName();
      if (StringUtils.isEmpty(configImplClassName)) {
        // it's empty, so first try environment variable
        configImplClassName = System.getenv(ConfigEnvironmentVarNames.CONFIG_IMPL_ENV_VAR);

        if (StringUtils.isEmpty(configImplClassName)) {
          // still empty, so use default
          configImplClassName = DEFAULT_CONFIG_CLASS_NAME;
        }

        // update binding
        binding.setInstanceClassName(configImplClassName);
      }

      // ensure init strategy
      InitializationStrategy initStrategy = binding.getInitStrategy();

      if (initStrategy == null) {
        initStrategy = new MapInitializationStrategy(binding.getInstanceClassName(), new StringMap());
        binding.setInitStrategy(initStrategy);
      }

      // get init params
      Class<?> configClass;
      Class<?> configInterface;
      try {
        configClass = ClassUtils.classFrom(binding.getInstanceClassName());
        configInterface = ClassUtils.classFrom(binding.getInterfaceName());
      } catch (ClassUtilsException e) {
        throw new InitializationException("Config class was not found.", e, initStrategy);
      }
      StringMap initParams = LocatorUtils.getInheritedParameters(initStrategy, configClass, configInterface);

      // ensure configFilePath
      String configFilePath = initParams.get(ConfigInitParamKeys.CONFIG_FILE_PATH_PARAM);
      if (StringUtils.isEmpty(configFilePath)) {
        // it's empty, so first try environment variable
        configFilePath = System.getenv(ConfigEnvironmentVarNames.CONFIG_FILE_PATH_ENV_VAR);

        if (StringUtils.isEmpty(configFilePath)) {
          // still empty, so use default
          configFilePath = "config.xml";
        }

        // update binding
        initParams.put(ConfigInitParamKeys.CONFIG_FILE_PATH_PARAM, configFilePath);
      }
    }

    /**
     * Returns a new {@code InterfaceToClassBinding}, with the given config file path and given concrete Config
     * implementation class name.
     */
    public static InterfaceToClassBinding<Config> createBinding(final String configFilePath, final String configImplClassName) {
      StringMap params = new StringMap();
      params.put(ConfigInitParamKeys.CONFIG_FILE_PATH_PARAM, configFilePath);

      return new InterfaceToClassBinding<Config>(Config.class.getName(), configImplClassName, new MapInitializationStrategy(Config.class.getName(), params));
    }
  }

  // ==================================================================
  // ConfigLocator
  // ==================================================================

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected ConfigLocator() {
  }

  /**
   * Convenience function used to register the concrete {@code Config} implementation as well as the path to the config
   * file. Example:
   *
   * <pre>
   * <code>
   * // specify the path to the config file, leaving the implementation class null (so the default config class will automatically be loaded).
   * ConfigLocator.setup("config/config.test.xml", null);
   * </code>
   * </pre>
   *
   * @param configFilePath path to the config file (absolute or relative to the classpath)
   * @param configImplClassName fully qualified class name of the {@code Config} implementation to use. If null, will
   * use environment variable or default.
   */
  public static void setup(final String configFilePath, final String configImplClassName) {
    // this is only called once
    ConfigLocatorLogHelper configLocatorLogHelper = new ConfigLocatorLogHelper(ConfigLocator.class);
    configLocatorLogHelper.logBeginRegisterInterfaces(Config.class.getName(), ConfigLocator.class);

    getRegistry().registerInterface(ConfigLocatorStrategy.createBinding(configFilePath, configImplClassName));
    configLocatorLogHelper.logSupplemental(configImplClassName);
  }

  /**
   * Return the registry used to bind an interface with it's concrete implementation, any time a {@code locate()}
   * request is made.
   */
  public static InterfaceBindingRegistry<Config> getRegistry() {
    return locatorStrategy.getRegistry();
  }

  /**
   * Returns the registered concrete implementation of the Config interface, if necessary, creating and initializing the
   * instance.
   */
  public static Config locate() {
    Config result = locatorStrategy.locate(Config.class);

    return result;
  }

  /** Clears the locator registry and bindings. */
  public static void reset() {
    locatorStrategy.reset();
  }

}
