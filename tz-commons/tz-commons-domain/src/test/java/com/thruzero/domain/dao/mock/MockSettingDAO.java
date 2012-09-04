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
package com.thruzero.domain.dao.mock;

import java.io.Serializable;
import java.util.List;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.KeyGen;
import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.dao.impl.GenericMemoryDAO;
import com.thruzero.domain.model.Setting;
import com.thruzero.domain.store.SimpleMemoryStore;

/**
 *
 * @author George Norman
 */
public final class MockSettingDAO extends GenericMemoryDAO<Setting> implements SettingDAO {

  // ------------------------------------------------------
  // SettingKeyGen
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying an instance of Setting in the store. */
  public static class SettingKeyGen extends KeyGen<Setting> {
    @Override
    public Serializable createKey(Setting domainObject) {
      return createKey(domainObject.getContext(), domainObject.getName());
    }

    /** Synthesize a primary key from the owner, context and Setting name. */
    public EntityPath createKey(String context, String name) {
      EntityPath result = new EntityPath(ContainerPath.CONTAINER_PATH_SEPARATOR + context + ContainerPath.CONTAINER_PATH_SEPARATOR, name);

      return result;
    }
  }

  // ============================================================================
  // MockSettingDAO
  // ============================================================================

  private MockSettingDAO() {
    super(new SimpleMemoryStore<Setting>());
  }

  @Override
  public boolean isExistingSetting(String context, String name) {
    Serializable key = ((SettingKeyGen)getKeyGen()).createKey(context, name);

    return isExistingEntity(key);
  }

  @Override
  public Setting getSetting(String context, String name) {
    Serializable key = ((SettingKeyGen)getKeyGen()).createKey(context, name);

    return getByKey(key);
  }

  @Override
  public String getSettingValue(String context, String name) {
    return getSettingValue(context, name, null);
  }

  @Override
  public String getSettingValue(String context, String name, String defaultValue) {
    Serializable key = ((SettingKeyGen)getKeyGen()).createKey(context, name);
    Setting result = getByKey(key);

    return result == null ? defaultValue : result.getValue();
  }

  @Override
  public List<? extends Setting> getSettings(String context) {
    return null;
  }

  @Override
  protected KeyGen<Setting> createKeyGen() {
    return new SettingKeyGen();
  }

}
