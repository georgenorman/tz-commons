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

import com.thruzero.common.core.map.IniMap;
import com.thruzero.common.core.map.StringMap;

/**
 * A strategy for initializing locatable components (e.g., DAO's and Services). Initialization parameters can be
 * read from a variety of sources (e.g., config file, settings table in a database).
 *
 * @author George Norman
 */
public interface InitializationStrategy extends IniMap {

  /**
   * Return a StringMap of key/value pairs that are to be used to initialize a locatable instance at the one and only
   * time it is constructed (locatables are Singletons).
   */
  @Override
  StringMap getSectionAsStringMap(String sectionName);

}
