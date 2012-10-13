/*
 *   Copyright 2012 George Norman
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

import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.config.Config;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.LogHelper;

/**
 * Loads initialization parameters from the config file. This is the default InitializationStrategy and is automatically
 * created by the locator framework, if one isn't specified.
 *
 * @author George Norman
 */
public class ConfigInitializationStrategy implements InitializationStrategy {
  private static final ConfigInitializationStrategyLogHelper logHelper = new ConfigInitializationStrategyLogHelper(ConfigInitializationStrategy.class);

  // -----------------------------------------------------------
  // ConfigInitializationStrategyLogHelper
  // -----------------------------------------------------------

  public static final class ConfigInitializationStrategyLogHelper extends LogHelper {
    public ConfigInitializationStrategyLogHelper(final Class<?> clazz) {
      super(clazz);
    }

    public void logBeginLoading(final String instanceClassName) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("Loading INIT parameters from config file at section: " + instanceClassName);
      }
    }

    public void dumpParams(final StringMap params) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("Params:");
        for (Entry<String, String> entry : params.entrySet()) {
          String value = entry.getValue();
          if (StringUtils.contains(entry.getKey(), "password")) {
            value = "****";
          }
          getLogger().debug("  -" + entry.getKey() + " = " + value);
        }
      }
    }
  }

  // =========================================================================
  // ConfigInitializationStrategy
  // =========================================================================

  /**
   * Return the initialization parameters from the config file, from the given sectionName.
   */
  @Override
  public StringMap getSectionAsStringMap(final String sectionName) {
    logHelper.logBeginLoading(sectionName);

    // get the config instance and retrieve the section named by the instance class.
    Config config = ConfigLocator.locate();
    StringMap result = config.getSectionAsStringMap(sectionName);

    if (result != null) {
      // dump all parameters to the debug log
      logHelper.dumpParams(result);
    }

    return result;
  }

}
