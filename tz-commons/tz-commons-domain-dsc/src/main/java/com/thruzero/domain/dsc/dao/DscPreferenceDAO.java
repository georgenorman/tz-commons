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
package com.thruzero.domain.dsc.dao;

import java.util.List;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.model.Preference;

/**
 * An implementation of PreferenceDAO that uses a DataStoreContainer (DSC) as storage.
 * <p>
 * This DAO requires initialization (see {@link AbstractDataStoreDAO.DataStoreDAOInitParamKeys} for
 * details). Following is an example initialization using the config file using the FileDataStoreContainer:
 *
 * <xmp>
 *   <!-- Define the generic file-system DAO settings (see "config.fs.domain.dao.xml") -->
 *   <section name="com.thruzero.domain.dsc.dao.DscPreferenceDAO">
 *     <entry key="com.thruzero.domain.dsc.store.DataStoreContainerFactory" value="[com.thruzero.domain.dsc.dao.AbstractDataStoreDAO]{com.thruzero.domain.dsc.store.DataStoreContainerFactory}" />
 *   </section>
 *
 *   <!-- Define application-specific file-system DAO settings (see "config.xml" from the  "pf-test18-jpa-war" project) -->
 *   <section name="com.thruzero.domain.dsc.dao.AbstractDataStoreDAO">
 *     <!-- The type of DataStoreContainerFactory to use -->
 *     <entry key="com.thruzero.domain.dsc.store.DataStoreContainerFactory" value="com.thruzero.domain.dsc.fs.FileDataStoreContainerFactory" />
 *   </section>
 * </xmp>
 *
 * As shown above, the "com.thruzero.domain.dsc.dao.AbstractDataStoreDAO" section is referenced to retrieve the base
 * storePath (and "DscPreferenceDAO" is appended to that path).
 *
 * @author George Norman
 */
public final class DscPreferenceDAO extends GenericDscDAO<Preference> implements PreferenceDAO {

  // ------------------------------------------------------
  // DscPreferenceKeyGen
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying a Preference instance in the store. */
  public static class DscPreferenceKeyGen extends DataStoreKeyGen<Preference> {
    @Override
    public EntityPath createKey(Preference domainObject) {
      return createKey(domainObject.getOwner(), domainObject.getContext(), domainObject.getName());
    }

    /** Synthesize a primary key from the Preference owner, context and name. */
    public EntityPath createKey(String owner, String context, String name) {
      // TODO-p1(george) need to escape context, owner and name, because they can include '/'
      ContainerPath parentPath = new ContainerPath(createParentPath(Preference.class), owner + ContainerPath.CONTAINER_PATH_SEPARATOR + context + ContainerPath.CONTAINER_PATH_SEPARATOR);
      EntityPath result = new EntityPath(parentPath, name);

      return result;
    }
  }

  // ============================================================================
  // DscPreferenceDAO
  // ============================================================================

  private DscPreferenceDAO() {
    super(Preference.class);
  }

  @Override
  public boolean isExistingPreference(String owner, String context, String name) {
    EntityPath id = ((DscPreferenceKeyGen)getKeyGen()).createKey(owner, context, name);
    boolean result = isExistingEntity(id);

    return result;
  }

  @Override
  public String getPreferenceValue(String owner, String context, String name) {
    return getPreferenceValue(owner, context, name, null);
  }

  @Override
  public String getPreferenceValue(String owner, String context, String name, String defaultValue) {
    String result = defaultValue;
    Preference preference = getPreference(owner, context, name);

    if (preference != null) {
      result = preference.getValue();
    }

    return result;
  }

  @Override
  public Preference getPreference(String owner, String context, String name) {
    EntityPath id = ((DscPreferenceKeyGen)getKeyGen()).createKey(owner, context, name);
    Preference result = getByKey(id);

    return result;
  }

  @Override
  public List<? extends Preference> getPreferences(String owner, String context) {
    // TODO-p2(george) Auto-generated method stub
    return null;
  }

  @Override
  protected DataStoreKeyGen<Preference> createKeyGen() {
    return new DscPreferenceKeyGen();
  }

}
