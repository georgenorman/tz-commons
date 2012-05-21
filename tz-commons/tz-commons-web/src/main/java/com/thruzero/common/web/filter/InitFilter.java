/*
 *   Copyright 2009 George Norman
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
package com.thruzero.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.thruzero.common.core.bookmarks.ConfigBookmark;
import com.thruzero.common.core.bookmarks.ConfigKeysBookmark;
import com.thruzero.common.core.locator.ConfigLocator;

/**
 * Init filter that prepares the Config implementation to read from the config file.
 *
 * @author George Norman
 */
public class InitFilter implements Filter {
  private static Logger logger = Logger.getLogger(InitFilter.class);

  // ------------------------------------------------
  // InitFilterInitParameterWebXmlKeys
  // ------------------------------------------------

  @ConfigKeysBookmark(comment = "Servlet FilterConfig keys for the init filter.")
  public interface InitFilterInitParameterWebXmlKeys {
    /**
     * the name of the environment variable that contains the path to the config file (e.g., "pftest18_config_dir_path_env").
     * An environment variable can be used to specify information about where the config file resides.
     * If this init parameter is provided, then it will be used to look up an environment variable.
     * If a value is found for the given environment variable, then the value will be used to generate
     * the path to the config file. Example:
     * <xmp>
     *   <init-param>
     *     <param-name>config-file-path-env-var</param-name>
     *     <param-value>pftest18_config_dir_path_env</param-value>
     *   </init-param>
     *   <init-param>
     *     <param-name>config-file-path</param-name>
     *     <!-- environment var is optional - enables config files to be maintained outside of the container -->
     *     <param-value>${pftest18_config_dir_path_env}/config.xml</param-value>
     *   </init-param>
     * </xmp>
     */
    String CONFIG_FILE_PATH_ENV_VAR_NAME_INIT_PARAM = "config-file-path-env-var";

    /** path to the config file (e.g., "/home/foo/bar/config.xml", or "${pftest18_config_dir_path_env}", or "${pftest18_config_dir_path_env}/config.xml") */
    String CONFIG_FILE_PATH_INIT_PARAM = "config-file-path";
  }

  // =============================================================
  // InitFilter
  // =============================================================

  @Override
  @ConfigBookmark( comment = "initialize config" )
  public void init(FilterConfig config) throws ServletException {
    String configFilePath = config.getInitParameter(InitFilterInitParameterWebXmlKeys.CONFIG_FILE_PATH_INIT_PARAM); // get config file path from web.xml init-param
    String configFileEnvironmentVar = config.getInitParameter(InitFilterInitParameterWebXmlKeys.CONFIG_FILE_PATH_ENV_VAR_NAME_INIT_PARAM); // get config file environment var name from web.xml init-param

    if (StringUtils.isNotEmpty(configFileEnvironmentVar)) {
      String configDirPath = System.getenv(configFileEnvironmentVar);

      // if env var set for config home, then attempt to substitute
      if (StringUtils.isEmpty(configDirPath)) {
        logger.error("*** ERROR *** ENVIRONMENT VAR NAME '" + configFileEnvironmentVar + "' WAS PROVIDED, but no value was found.");
      } else {
        configFilePath = StringUtils.replace(configFilePath, "${" + configFileEnvironmentVar + "}", configDirPath);
      }
    }

    // init config
    ConfigLocator.setup(configFilePath, null); // TODO-p1(george) add config implementation name as an option to web.xml
  }

  @Override
  public void destroy() {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    chain.doFilter(request, response);
  }

}
