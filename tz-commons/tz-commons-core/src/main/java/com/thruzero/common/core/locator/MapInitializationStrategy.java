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

import java.util.HashMap;
import java.util.Map;

import com.thruzero.common.core.map.StringMap;

/**
 * An InitializationStrategy implemented using a Map.
 *
 * @author George Norman
 */
public class MapInitializationStrategy implements InitializationStrategy {
  private final Map<String,StringMap> iniMap;

  public MapInitializationStrategy(String sectionName, StringMap initializationParameters) {
    this.iniMap = new HashMap<String,StringMap>();

    this.iniMap.put(sectionName, initializationParameters);
  }

  @Override
  public StringMap getSectionAsStringMap(String sectionName) {
    return iniMap.get(sectionName);
  }

}
