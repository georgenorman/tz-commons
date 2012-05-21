/*
 *   Copyright 2009 - 2011 George Norman
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

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.thruzero.common.core.bookmarks.SettingBookmark;
import com.thruzero.common.core.bookmarks.SettingKeysBookmark;
import com.thruzero.common.core.service.Service;
import com.thruzero.common.core.support.ValueTransformer;
import com.thruzero.domain.model.Setting;

/**
 * A Service interface to manage configuration settings saved to a data store (e.g., database), for easy
 * management and centralization of configuration information.
 * <p>
 * Note: The particular DAO configured for this service defines where the data is actually stored.
 *
 * @author George Norman
 */
@SettingBookmark
public interface SettingService extends Service {

  // ------------------------------------------
  // SettingsKeys
  // ------------------------------------------

  @SettingKeysBookmark
  public interface SettingsKeys {

    // contexts /////////////////////////////////////////////////
    String GENERAL_CONTEXT = "General";

  }

  // ============================================================
  // SettingService
  // ============================================================

  Setting getSetting(String context, String name);

  String getStringValue(String context, String name, String defaultValue);

  String getStringValue(String context, String name);

  /** Return the specified value as a ValueTransformer. If the value is null, a ValueTransformer will be returned constructed from the null. */
  ValueTransformer<String> getValueTransformer(String context, String name);

  Set<String> splitStringValueFor(String context, String name, final String separator);

  int getIntValue(String context, String name, int defaultValue);

  long getLongValue(String context, String name, long defaultValue);

  float getFloatValue(String context, String name, float defaultValue);

  boolean getBooleanValue(String context, String name, boolean defaultValue);

  Date getDateValue(String context, String name, Date defaultValue) throws ParseException;

  Date getDateValue(String context, String name, Date defaultValue, final String... parsePatterns) throws ParseException;

  List<? extends Setting> getSettings(String context);

  void saveOrUpdatSetting(Setting setting);

  void deleteSetting(Setting setting);

}
