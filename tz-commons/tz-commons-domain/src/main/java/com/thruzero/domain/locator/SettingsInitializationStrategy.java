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
package com.thruzero.domain.locator;

import java.util.List;

import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.domain.model.Setting;
import com.thruzero.domain.service.SettingService;

/**
 * Load initialization parameters from the Settings service. This is an alternative to initializing a locatable from the
 * config file.
 * <p>
 * NOTE: DO NOT USE THIS TO INITIALIZE THE SettingService. TODO-p1(george) Throw exception if this is attempted.
 * </p>
 *
 * @author George Norman
 */
public class SettingsInitializationStrategy implements InitializationStrategy {

  /**
   * Load initialization parameters from the settings service, using the context named by the given instanceClassName
   * and return the parameters if found, otherwise, return null.
   */
  @Override
  public StringMap getSectionAsStringMap(String sectionName) {
    StringMap result = null;
    SettingService settingService = ServiceLocator.locate(SettingService.class);
    List<? extends Setting> settings = settingService.getSettings(sectionName);

    if (settings != null && !settings.isEmpty()) {
      result = new StringMap();

      for (Setting setting : settings) {
        result.put(setting.getName(), setting.getValue());
      }
    }

    return result;
  }
}
