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
package com.thruzero.domain.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.support.ValueTransformer;
import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.Setting;
import com.thruzero.domain.service.SettingService;

/**
 * Basic implementation of the SettingService, using {@link com.thruzero.domain.dao.SettingDAO SettingDAO} as the
 * persistence provider.
 * <p>
 * Note: The particular DAO configured for this service defines where the data is actually stored (e.g., file system, relational table, etc).
 *
 * @author George Norman
 *
 * TODO-p1(george) cache the settings. Add ability to force a refresh.
 */
public class GenericSettingService implements SettingService {
  private final SettingDAO settingDAO = DAOLocator.locate(SettingDAO.class);

  @Override
  public Setting getSetting(String context, String name) {
    return settingDAO.getSetting(context, name);
  }

  @Override
  public String getStringValue(final String context, final String name, final String defaultValue) {
    String result = getStringValue(context, name);

    if (StringUtils.isEmpty(result)) {
      result = defaultValue;
    }

    return result;
  }

  @Override
  public String getStringValue(final String context, final String name) {
    String result = settingDAO.getSettingValue(context, name);

    return result;
  }

  @Override
  public ValueTransformer<String> getValueTransformer(String context, String name) {
    return new ValueTransformer<String>(getStringValue(context, name));
  }

  @Override
  public Set<String> splitStringValueFor(final String context, final String name, final String separator) {
    Set<String> result = new HashSet<String>();
    String flattenedResult = getStringValue(context, name);
    String[] tokens = StringUtils.split(flattenedResult, separator);

    if (tokens != null) {
      for (String value : tokens) {
        value = StringUtils.trim(value);

        if (StringUtils.isNotEmpty(value)) {
          result.add(value);
        }
      }
    }

    return result;
  }

  @Override
  public List<? extends Setting> getSettings(String context) {
    List<? extends Setting> result = settingDAO.getSettings(context);

    return result;
  }

  @Override
  public void saveOrUpdatSetting(final Setting setting) {
    settingDAO.saveOrUpdate(setting);
  }

  @Override
  public void deleteSetting(Setting setting) {
    settingDAO.delete(setting);
  }

  /** return the DAO class name used by this service (used in unit tests to assert proper configuration setup). */
  public String getSettingDAOClassName() {
    return settingDAO.getClass().getName();
  }

}
