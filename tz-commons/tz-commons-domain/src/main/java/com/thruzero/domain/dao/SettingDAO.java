/*
 *   Copyright 2009-2012 George Norman
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
package com.thruzero.domain.dao;

import java.util.List;

import com.thruzero.domain.model.Setting;

/**
 * A DAO that manages operations specific to the Setting Domain Object.
 *
 * @author George Norman
 */
public interface SettingDAO extends GenericDAO<Setting> {

  boolean isExistingSetting(String context, String name);

  Setting getSetting(String context, String name);

  String getSettingValue(String context, String name);

  String getSettingValue( String context, String name, String defaultValue );

  List<? extends Setting> getSettings(String context);

}
