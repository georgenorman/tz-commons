/*
 *   Copyright 2010 - 2012 George Norman
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

import com.thruzero.domain.model.Preference;

/**
 * A DAO that manages operations specific to the Preference Domain Object.
 *
 * @author George Norman
 */
public interface PreferenceDAO extends GenericDAO<Preference> {

  Preference getPreference(String owner, String context, String name);

  String getPreferenceValue(String owner, String context, String name);

  String getPreferenceValue(String owner, String context, String name, String defaultValue);

  List<? extends Preference> getPreferences(String owner, String context);

}
