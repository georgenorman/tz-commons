/*
 *   Copyright 2011-2012 George Norman
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.bookmarks.ConfigBookmark;
import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.config.Config;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.LogHelper;
import com.thruzero.common.core.support.Singleton;
import com.thruzero.common.core.utils.ClassUtils;
import com.thruzero.common.core.utils.ClassUtils.ClassUtilsException;
import com.thruzero.common.core.utils.ExceptionUtilsExt;

/**
 * A locator strategy, based on a registry, used to locate an implementation for a given interface (e.g., find a
 * specific {@code DAO} or {@code Service} singleton). Concrete locators can use this strategy in their implementation.
 * For example, the {@link com.thruzero.common.core.locator.ServiceLocator ServiceLocator} is implemented as follows:
 *
 * <pre>
 * public class ServiceLocator {
 *   private static RegistryLocatorStrategy<Service> locatorStrategy = new RegistryLocatorStrategy<Service>();
 *
 *   public static <T extends Service> T locate( Class<T> type ) {
 *     return type.cast(locatorStrategy.locate(type));
 *   }
 * </pre>
 *
 * And clients use the ServiceLocator as follows:
 *
 * <pre>
 * SettingService settingService = ServiceLocator.locate(SettingService.class);
 * </pre>
 *
 * Note: This locator strategy is very simple - it doesn't provide any injection, so Singletons must manage their own
 * dependencies. Fortunately, these dependencies can be managed by yet another locator, thus achieving 80% of the
 * functionality and 0% of the elegance of injection-based dependency mechanisms (e.g., Spring or Guice). Below is an
 * example of how the default, SettingService implementation uses yet another locator to find the SettingDAO configured
 * for the current application:
 *
 * <pre>
 * public class GenericSettingService implements SettingService {
 *   private final SettingDAO SettingDAO = DAOLocator.locate(SettingDAO.class);
 *   ...
 * }
 * </pre>
 * <p>
 * Implementations may be registered directly via the {@code InterfaceBindingRegistry} or declaratively, via a config
 * file. Pre-configured config files are provided by each implementation package. For example, the
 * {@code com.thruzero.domain.service.impl} package provides the {@code config.domain.service.impl.xml} config file that maps
 * all of the {@link com.thruzero.common.core.service.Service Service} interfaces from the its package to all of its
 * implementations. Here's a sample using JFig as the config provider:
 *
 * <pre>
 * {@code
 *   <section name="com.thruzero.common.core.service.Service">
 *     <entry key="com.thruzero.domain.service.InfoNodeService" value="com.thruzero.domain.service.impl.GenericInfoNodeService" />
 *     <entry key="com.thruzero.domain.service.SettingService" value="com.thruzero.domain.service.impl.GenericSettingService" />
 *     <entry key="com.thruzero.domain.service.MailService" value="com.thruzero.domain.service.impl.SimpleMailService" />
 *     ...
 *   </section>
 * }
 * </pre>
 *
 * When creating an application, you can choose to use the {@code config.domain.service.impl.xml} config file or create your
 * own custom configuration, perhaps mapping the interfaces to different implementations.
 * <p>
 * Note: TODO-p1(george) Investigate replacing this with Guice: http://code.google.com/p/google-guice/
 *
 * @author George Norman
 * @param <T> Type of Object located by this strategy (e.g., ServiceLocator only finds instances of type Service).
 */
public class RegistryLocatorStrategy<T extends Singleton> implements LocatorStrategy<T> {
  private static final LocatorLogHelper logHelper = new LocatorLogHelper(RegistryLocatorStrategy.class);

  private final String targetInterfaceTypeName;
  private final InstanceCache<T> instanceCache = new InstanceCache<T>();
  private final ReadWriteLock rwl = new ReentrantReadWriteLock();
  private final InterfaceBindingRegistry<T> interfaceBindingRegistry = new InterfaceBindingRegistry<T>(rwl);

  // -----------------------------------------------------------
  // LocatorException
  // -----------------------------------------------------------

  public static class LocatorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LocatorException(final String message, final Throwable cause) {
      super(ExceptionUtilsExt.decorateMessageLevel1(message), cause);
    }

    public LocatorException(final String message) {
      super(ExceptionUtilsExt.decorateMessageLevel1(message));
    }
  }

  // -----------------------------------------------------------
  // InterfaceBindingRegistryNotInitializedException
  // -----------------------------------------------------------

  public static class InterfaceBindingRegistryNotInitializedException extends LocatorException {
    private static final long serialVersionUID = 1L;

    public InterfaceBindingRegistryNotInitializedException() {
      super(InterfaceBindingRegistry.class.getSimpleName() + " is not initialized.");
    }
  }

  // --------------------------------------------------------
  // RegistryLocatorStrategyParameterKeys
  // --------------------------------------------------------

  /**
   * An interface that defines Keys for RegistryLocatorStrategy (INITIALIZATION_STRATEGY key to look up an optional
   * LocatorStrategy implementation.
   */
  @InitializationParameterKeysBookmark(comment = "Keys used by RegistryLocatorStrategyParameterKeys, and subclasses.")
  public interface RegistryLocatorStrategyParameterKeys {
    /**
     * An optional parameter that can be used to define the fully qualified class name of the InitializationStrategy to use
     * when loading initialization parameters. The default InitializationStrategy is the ConfigInitializationStrategy,
     * which will load the initialization parameters from a config file. The SettingsInitializationStrategy will load
     * the initialization parameters from the SettingService.
     * <p/>
     * Note: The strategy parameter is read from the config file, then if present, the defined InitializationStrategy instance
     * is created and used to load the parameters used to initialize the class. So, if the located class is to be initialized
     * from the SettingService, the config file must at least contain the key used to define the InitializationStrategy instance.
     */
    String INITIALIZATION_STRATEGY = InitializationStrategy.class.getSimpleName();
  }

  // -----------------------------------------------------------
  // LocatorLogHelper
  // -----------------------------------------------------------

  public static class LocatorLogHelper extends LogHelper {
    public LocatorLogHelper(final Class<?> clazz) {
      super(clazz);
    }

    public void logNullBindingFor(final Class<?> interfaceClass, final String targetInterfaceTypeName) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("Binding for interface '" + interfaceClass.getName() + "' was null, so now registering all default interfaces of type: '" + targetInterfaceTypeName + "'.");
      }
    }

    public void logBindingsDiscovered(final String targetInterfaceTypeName) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("Interface section '" + targetInterfaceTypeName + "' was discovered in config, so will use it to register the default bindings.");
      }
    }

    public void logBindingsNotDiscovered(final String targetInterfaceTypeName) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("* Interface section '" + targetInterfaceTypeName + "' was NOT found in config, so can't use it to register the default bindings.");
      }
    }

    public void logBeginRegisterInterfaces(final String targetInterfaceTypeName, final Class<?> source) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("Registering Interfaces of type: '" + targetInterfaceTypeName + "' via " + source.getSimpleName() + ".");
      }
    }

    public void logBeginRegisterDefaultInterfaces(final String targetInterfaceTypeName, final Class<?> source) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("Registering DEFAULT Interfaces of type: '" + targetInterfaceTypeName + "' via " + source.getSimpleName() + ".");
      }
    }

    public void logRegisteredInterface(final InterfaceToClassBinding<?> binding) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("  - Interface '" + binding.getInterfaceName() + "' is bound to '" + binding.getInstanceClassName() + "'.");
      }
    }

    public void logTeardownRegistry(final String targetInterfaceTypeName) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("! Locator is being reset for interface types: '" + targetInterfaceTypeName + "'.");
      }
    }

    public void logInitializationStrategyCreationError(final String targetInterfaceTypeName, final String initializationStrategyClassName) {
      getLogger().error("!!!! Locator for interface types: '" + targetInterfaceTypeName + "' could not create InitializationStrategyClass: " + initializationStrategyClassName + "'.");
    }

    public void logConfigInitConfigError() {
      getLogger().error(
          "!!!! Attempted to initialize the Config instance using a parameters loaded from the Config instance. Config initialization paremeters can't be loaded from the config file.");
    }

    public void warnInstanceTypeAlreadyRegistered(final String interfaceName, final Object instance) {
      getLogger().warn("InterfaceBindingRegistry WARNING - Instance already exists for this type: interface name = " + interfaceName + ", instance class name = "
          + instance.getClass().getName());
    }
  }

  // -----------------------------------------------------------
  // InstanceCache
  // -----------------------------------------------------------

  public static class InstanceCache<T> {
    private final Map<String, T> instances = new HashMap<String, T>();

    public void cacheInstance(final String interfaceName, final T instance) {
      // first, verify that there isn't an instance of this type already registered (these are Singletons)
      for (T cachedInstance : instances.values()) {
        if (cachedInstance.getClass().equals(instance.getClass())) {
          logHelper.warnInstanceTypeAlreadyRegistered(interfaceName, instance);
        }
//          throw new LocatorException("InterfaceBindingRegistry ERROR - Instance already exists for this type: interface name = " + interfaceName + ", instance class name = "
//              + instance.getClass().getName());
      }

      // okay to cache
      instances.put(interfaceName, instance);
    }

    public T getInstance(final String interfaceName) {
      T result = instances.get(interfaceName);

      return result;
    }
  }

  // -----------------------------------------------------------
  // InterfaceBindingRegistry
  // -----------------------------------------------------------

  /**
   * The collection of registered interface-to-implementation bindings.
   */
  public static class InterfaceBindingRegistry<T> {
    private final ReadWriteLock rwl; // TODO-p1(george) revisit need to pass this in; should be able to easily make this a non-static class and access rwl from outer.
    private Map<String, InterfaceToClassBinding<T>> bindingMap;

    public InterfaceBindingRegistry(ReadWriteLock rwl) {
      this.rwl = rwl;
    }

    public void registerInterfaces(final Collection<InterfaceToClassBinding<T>> bindings) {
      rwl.writeLock().lock();
      try {
        for (InterfaceToClassBinding<T> interfaceToClass : bindings) {
          registerInterface(interfaceToClass);
        }
      } finally {
        rwl.writeLock().unlock();
      }
    }

    public void registerInterface(final Class<? extends T> interfaceType, final Class<? extends T> instanceType) {
      registerInterface(new InterfaceToClassBinding<T>(interfaceType, instanceType));
    }

    public void registerInterface(final Class<? extends T> interfaceType, final Class<? extends T> instanceType, final InitializationStrategy initStrategy) {
      registerInterface(new InterfaceToClassBinding<T>(interfaceType, instanceType, initStrategy));
    }

    public void registerInterface(final InterfaceToClassBinding<T> binding) {
      rwl.writeLock().lock();
      try {
        if (bindingMap == null) {
          bindingMap = new HashMap<String, InterfaceToClassBinding<T>>();
        }

        // error if attempting to register the same binding twice.
        if (bindingMap.containsKey(binding.getInterfaceName())) {
          throw ExceptionUtilsExt.logAndCreateLocatorException(logHelper.getLogger(),
                "InterfaceBindingRegistry ERROR - Binding already exists for given interface: " + binding.getInterfaceName());
        }

        bindingMap.put(binding.getInterfaceName(), binding);
        logHelper.logRegisteredInterface(binding);
      } finally {
        rwl.writeLock().unlock();
      }
    }

    public InterfaceToClassBinding<T> getBindingFor(String interfaceName) {
      InterfaceToClassBinding<T> result = null;

      rwl.readLock().lock();
      try {
        if (bindingMap != null) {
          result = bindingMap.get(interfaceName);
        }
      } finally {
        rwl.readLock().unlock();
      }

      return result;
    }

    public void reset() {
      rwl.writeLock().lock();
      try {
        if (bindingMap != null) {
          bindingMap.clear();
          bindingMap = null;
        }
      } finally {
        rwl.writeLock().unlock();
      }
    }
  }

  // ============================================================================
  // RegistryLocatorStrategy
  // ============================================================================

  /**
   * Create a registry strategy for a locator that manages classes implementing the given type of interface.
   */
  public RegistryLocatorStrategy(final Class<T> targetInterfaceType) {
    this.targetInterfaceTypeName = targetInterfaceType.getName(); // preserve the erased type name.
  }

  public InterfaceBindingRegistry<T> getRegistry() {
    return interfaceBindingRegistry;
  }

  /** reset the registry and bindings back to empty state. */
  public void reset() {
    logHelper.logTeardownRegistry(targetInterfaceTypeName);

    rwl.writeLock().lock();
    try {
      for (T instance : instanceCache.instances.values()) {
        if (instance instanceof Initializable) {
          ((Initializable)instance).reset();
        }
      }
      instanceCache.instances.clear();
      interfaceBindingRegistry.reset();
    } finally {
      rwl.writeLock().unlock();
    }
  }

  /**
   * Return the cached instance for the given {@code type} and if absent, create and optionally initialize the instance
   * specified by the registry and cache it for future requests. See
   * {@link #doCreateTargetInstance(InterfaceToClassBinding)} for details.
   */
  @Override
  public T locate(final Class<? extends T> type) {
    T result;
    String typeName = type.getName();

    rwl.readLock().lock();
    try {
      result = instanceCache.getInstance(typeName);
    } finally {
      rwl.readLock().unlock();
    }

    // lazy-init the cache for each interface/instance as they are requested.
    // TODO-p1(george) As an alternative, could create all instances and cache at the time the registry is created (see registerDefaultBindings)
    if (result == null) {
      // write protect entire initialization process, plus check that it's still not initialized (TODO-p1(george) probably not a safe solution 'Double-checked locking')
      rwl.writeLock().lock();
      result = instanceCache.getInstance(typeName);
      if (result == null) {
        try {
          // instance was not cached, so create one from the registered binding and cache it
          InterfaceToClassBinding<T> binding = getRegisteredBinding(type); // this will acquire the lock again for read and possibly write

          if (binding == null) {
            throw ExceptionUtilsExt.logAndCreateLocatorException(logHelper.getLogger(), "Error, interface is not registered: " + type);
          } else {
            // create the target instance
            result = doCreateTargetInstance(binding);

            instanceCache.cacheInstance(typeName, result);
          }
        } finally {
          rwl.writeLock().unlock();
        }
      }
    }

    return result;
  }

  /**
   * Create a new implementation defined by the given interface to implementation binding. If the implementation
   * implements the {@link com.thruzero.common.core.locator.Initializable Initializable} interface, then call its
   * init(StringMap) method. The StringMap may be specified in the given binding. However, if the binding returns null
   * for the init parameters, then an attempt to load them from the config file is made, using the class name of the
   * implementation. For example, the following config section defines the init parameters for the
   * {@code DscTextEnvelopeDAO} (using {@code JFig} as the config provider):
   *
   * <pre>
   * {@code
   *  <section name="com.thruzero.domain.dao.impl.DscTextEnvelopeDAO">
   *    <entry key="rootStorePath" value="/home/george/pf-test-desktop/demo-data-store" />
   *  </section>
   * }
   * </pre>
   *
   * Note: By convention, the class name of the implementation is used as the section name. The parameters used in the
   * section are defined by the implementation class (refer to the implementation class for details).
   */
  protected T doCreateTargetInstance(final InterfaceToClassBinding<T> binding) {
    T result;

    try {
      // assert that the concrete-class is a Singleton
      Class<T> cls = ClassUtils.classFrom(binding.getInstanceClassName());
      if (!Singleton.class.isAssignableFrom(cls)) {
        throw ExceptionUtilsExt.logAndCreateLocatorException(logHelper.getLogger(), "Error attempting to locate class: " + binding.getInstanceClassName()
            + ". The class must be a Singleton (since only one instance will ever be created by the locator).");
      }

      // assert that the concrete-class implements the registered interface
      Class<?> inf = ClassUtils.classFrom(binding.getInterfaceName());
      if (!inf.isAssignableFrom(cls)) {
        throw ExceptionUtilsExt.logAndCreateLocatorException(logHelper.getLogger(), "Error attempting to locate class: " + binding.getInstanceClassName() +
            ". The class must implement the registered interface: " + binding.getInterfaceName());
      }

      // create Singleton instance.
      result = ClassUtils.<T> instanceFrom(cls); // workaround for javac bug 6302954 (added <T>)

      if (result instanceof Initializable) {
        // The SecurityManager does not allow construction with a non-default constructor (if the constructor is private), so must use the init method
        // instead of having a constructor that takes params.
        ((Initializable)result).init(getInitializationStrategy(binding));
      }
    } catch (ClassUtilsException e) {
      throw ExceptionUtilsExt.logAndCreateLocatorException(logHelper.getLogger(),
          "Error attempting to instantiate class: " + binding.getInstanceClassName() + " where interface is: " + binding.getInterfaceName(), e);
    }

    return result;
  }

  /**
   * Return the initialization parameters for the given binding. If the binding has no initialization parameters
   * defined, then attempt to load them from the config file. See
   * {@link #doCreateTargetInstance(InterfaceToClassBinding)} for details.
   */
  @ConfigBookmark(comment = "Get the initialization strategy passed into the init method.")
  protected InitializationStrategy getInitializationStrategy(final InterfaceToClassBinding<T> binding) {
    InitializationStrategy result = binding.getInitStrategy();

    // if init params weren't provided, attempt to init via config
    if (result == null) {
      // config can't init itself via config
      if (Config.class.getName().equals(binding.getInterfaceName())) {
        logHelper.logConfigInitConfigError();
      } else {
        // use config to look in section based on the target type to find its initialization strategy
        Config config = ConfigLocator.locate();
        String initializationStrategyClassName = config.getValue(binding.getInstanceClassName(), RegistryLocatorStrategyParameterKeys.INITIALIZATION_STRATEGY);

        if (StringUtils.isEmpty(initializationStrategyClassName)) {
          result = new ConfigInitializationStrategy();
        } else {
          try {
            Class<InitializationStrategy> clazz = ClassUtils.classFrom(initializationStrategyClassName);
            result = ClassUtils.instanceFrom(clazz);
          } catch (ClassUtilsException e) {
            logHelper.logInitializationStrategyCreationError(targetInterfaceTypeName, initializationStrategyClassName);
            throw new LocatorException("Error attempting to instantiate class: " + binding.getInstanceClassName() + " where interface is: " + binding.getInterfaceName(), e);
          }
        }

        // if strategy is missing, then provide an empty map (the init function will test for valid init data and fail if any args are missing).
        if (result == null && StringUtils.isNotEmpty(binding.getInstanceClassName())) {
          result = new MapInitializationStrategy(binding.getInstanceClassName(), new StringMap());
        }
      }
    }

    return result;
  }

  /**
   * Return the registered binding for the given {@code interfaceClass}. If bindings have not been set, then attempt to
   * load them from the config file.
   */
  protected InterfaceToClassBinding<T> getRegisteredBinding(final Class<?> interfaceClass) {
    InterfaceToClassBinding<T> result = interfaceBindingRegistry.getBindingFor(interfaceClass.getName());

    if (result == null) {
      // lazy init the registry
      logHelper.logNullBindingFor(interfaceClass, targetInterfaceTypeName);
      registerDefaultBindings();

      // attempt to get the binding again
      result = interfaceBindingRegistry.getBindingFor(interfaceClass.getName());

      // if still null, then fail.
      if (result == null) {
        throw ExceptionUtilsExt.logAndCreateLocatorException(logHelper.getLogger(), "ERROR: No interface-to-class binding was found for the requested interface '" + interfaceClass.getSimpleName() + "'.\n"
              + "If using Config-based bindings, make sure the config file contains a section named '" + targetInterfaceTypeName + "' for registering the bindings.\n"
              + "Also, make sure that interface '" + interfaceClass.getName() + "' is mapped to an implementation within that section.\n"
              + "If not using Config-based bindings, make sure you have called the registerAllInterfaces() method of the provider package (e.g., DomainServiceRegistry).");
      }
    }

    return result;
  }

  /**
   * Attempt to load the bindings from the config file, if the client didn't register any bindings programmatically. The
   * bindings are defined by the type of class the locator locates. For example, the ServiceLocator locates classes of
   * type Service, hence the section would be named "com.thruzero.common.core.service.Service":
   *
   * <pre>
   * {@code
   *   <section name="com.thruzero.common.core.service.Service">
   *     <entry key="com.thruzero.domain.service.InfoNodeService" value="com.thruzero.domain.service.impl.GenericInfoNodeService" />
   *     <entry key="com.thruzero.domain.service.SettingService" value="com.thruzero.domain.service.impl.GenericSettingService" />
   *     ...
   *   </section>
   * }
   * </pre>
   *
   * Note: The interfaces and implementation classes are not loaded at this time (their names are saved as strings until
   * an attempt to locate a particular instance is made.
   */
  @ConfigBookmark(comment = "Load the interface to implementation bindings from the config file (if the bindings were not set programmatically).")
  protected void registerDefaultBindings() {
    Config config = ConfigLocator.locate();
    Map<String, String> bindings = config.getSection(targetInterfaceTypeName);

    if (bindings == null) {
      logHelper.logBindingsNotDiscovered(targetInterfaceTypeName);
    } else {
      logHelper.logBindingsDiscovered(targetInterfaceTypeName);
      logHelper.logBeginRegisterInterfaces(targetInterfaceTypeName, config.getClass());

      // write protect entire initialization process (caller should have already write locked this), plus check that it's still not initialized (TODO-p1(george) probably not a safe solution 'Double-checked locking')
      rwl.writeLock().lock();
      if (interfaceBindingRegistry.bindingMap == null) {
        try {
          for (Entry<String, String> entry : bindings.entrySet()) {
            InterfaceToClassBinding<T> binding = new InterfaceToClassBinding<T>(entry.getKey(), entry.getValue());
            interfaceBindingRegistry.registerInterface(binding);
          }
        } finally {
          rwl.writeLock().unlock();
        }
      }
    }
  }
}
