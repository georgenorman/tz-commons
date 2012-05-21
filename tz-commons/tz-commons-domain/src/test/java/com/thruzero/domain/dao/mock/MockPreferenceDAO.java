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

import com.thruzero.common.core.support.KeyGen;
import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.dao.impl.GenericMemoryDAO;
import com.thruzero.domain.model.Preference;
import com.thruzero.domain.store.ContainerPath;
import com.thruzero.domain.store.EntityPath;
import com.thruzero.domain.store.SimpleMemoryStore;

/**
 *
 * @author George Norman
 */
public class MockPreferenceDAO extends GenericMemoryDAO<Preference> implements PreferenceDAO {

  // ------------------------------------------------------
  // PreferenceKeyGen
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying an instance of Preference in the store. */
  public static class PreferenceKeyGen extends KeyGen<Preference> {
    @Override
    public Serializable createKey(Preference domainObject) {
      return createKey(domainObject.getOwner(), domainObject.getContext(), domainObject.getName());
    }

    /** Synthesize a primary key from the owner, context and preference name. */
    public EntityPath createKey(String owner, String context, String name) {
      EntityPath result = new EntityPath(ContainerPath.CONTAINER_PATH_SEPARATOR + owner + ContainerPath.CONTAINER_PATH_SEPARATOR + context + ContainerPath.CONTAINER_PATH_SEPARATOR, name);

      return result;
    }
  }

  // ============================================================================
  // MockPreferenceDAO
  // ============================================================================

  public MockPreferenceDAO() {
    super(new SimpleMemoryStore<Preference>());
  }

  @Override
  public Preference getPreference(String owner, String context, String name) {
    Serializable key = ((PreferenceKeyGen)getKeyGen()).createKey(owner, context, name);

    return getByKey(key);
  }

  @Override
  public String getPreferenceValue(String owner, String context, String name) {
    return getPreferenceValue(owner, context, name, null);
  }

  @Override
  public String getPreferenceValue(String owner, String context, String name, String defaultValue) {
    Serializable key = ((PreferenceKeyGen)getKeyGen()).createKey(owner, context, name);
    Preference result = getByKey(key);

    return result == null ? defaultValue : result.getValue();
  }

  @Override
  public List<? extends Preference> getPreferences(String owner, String context) {
    // TODO-p2(george) Auto-generated method stub
    return null;
  }

  @Override
  protected KeyGen<Preference> createKeyGen() {
    return new PreferenceKeyGen();
  }

}
