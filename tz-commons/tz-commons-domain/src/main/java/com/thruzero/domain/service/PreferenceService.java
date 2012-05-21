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
package com.thruzero.domain.service;

import com.thruzero.common.core.service.Service;
import com.thruzero.common.core.support.ValueTransformer;
import com.thruzero.domain.model.Preference;

/**
 * A Service interface to manage system and user-level preferences. Each preference is represented by an owner
 * (e.g., "system", "user1@thruzero.com"), a context (e.g., "Volunteer") and a name (e.g., "hideUnavailableResponseEvents").
 * <p>
 * Note: The particular DAO configured for this service defines where the data is actually stored.
 *
 * @author George Norman
 */
public interface PreferenceService extends Service {

  String getPreferenceValue(String owner, String context, String name, String defaultValue);

  String getPreferenceValue(String owner, String context, String name);

  ValueTransformer<String> getValueTransformer(String owner, String context, String name);

  Preference getPreference(String owner, String context, String name);

  void saveOrUpdatePreference(Preference preference);

  void deletePreference(Preference preference);

}
