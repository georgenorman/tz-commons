/*
 *   Copyright 2010-2011 George Norman
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

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.support.ValueTransformer;
import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.Preference;
import com.thruzero.domain.service.PreferenceService;

/**
 * A simple service for storing and retrieving preferences for a particular owner and context. This service is typically
 * subclassed (or wrapped) to provide an API to access the individual preferences (instead of forcing clients to pass in
 * keys). The implementation uses {@link com.thruzero.domain.dao.PreferenceDAO PreferenceDAO} as the persistence provider.
 * <p>
 * Note: The particular DAO configured for this service defines where the data is actually stored (e.g., file system, relational table, etc).
 */
public final class GenericPreferenceService implements PreferenceService {
  private final PreferenceDAO preferenceDAO;

  /**
   * Use {@link com.thruzero.common.core.locator.ServiceLocator ServiceLocator} to access a particular Service.
   */
  private GenericPreferenceService() {
    this.preferenceDAO = DAOLocator.locate(PreferenceDAO.class);
  }

  @Override
  public String getPreferenceValue(final String owner, final String context, final String name, final String defaultValue) {
    String result = getPreferenceValue(owner, context, name);

    if (StringUtils.isEmpty(result)) {
      result = defaultValue;
    }

    return result;
  }

  @Override
  public String getPreferenceValue(final String owner, final String context, final String name) {
    String result = preferenceDAO.getPreferenceValue(owner, context, name);

    return result;
  }

  @Override
  public ValueTransformer<String> getValueTransformer(String owner, String context, String name) {
    return new ValueTransformer<String>(getPreferenceValue(owner, context, name));
  }

  @Override
  public Preference getPreference(final String owner, final String context, final String name) {
    Preference result = preferenceDAO.getPreference(owner, context, name);

    return result;
  }

  @Override
  public void saveOrUpdatePreference(final Preference preference) {
    preferenceDAO.saveOrUpdate(preference);
  }

  @Override
  public void deletePreference(Preference preference) {
    preferenceDAO.delete(preference);
  }

  /** return the DAO class name used by this service (used in unit tests to assert proper configuration setup). */
  public String getPreferenceDAOClassName() {
    return preferenceDAO.getClass().getName();
  }

}
