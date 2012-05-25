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

import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.model.Setting;
import com.thruzero.domain.store.ContainerPath;
import com.thruzero.domain.store.EntityPath;

/**
 * An implementation of SettingDAO that uses a DataStoreContainer (DSC) as storage.
 * <p>
 * DscSettingDAO requires initialization (see {@link DataStoreDAOInitParamKeys.DataStoreDAOInitParamKeys} for details).
 * Following is an example initialization using the config file using the FileDataStoreContainer:
 *
 * <xmp>
 *   <!-- Define the generic file-system DAO settings (see "config.fs.domain.dao.xml") -->
 *   <section name="com.thruzero.domain.dsc.dao.DscSettingDAO">
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
 * As can be seen, it references the AbstractDataStoreDAO to get the base store path and then appends
 * "DscSettingDAO", creating a unique path to the DscSettingDAO storage.
 *
 * @author George Norman
 */
public class DscSettingDAO extends GenericDscDAO<Setting> implements SettingDAO {

  // ------------------------------------------------------
  // DscSettingDAO
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying a Setting instance in the store. */
  public static class DscSettingKeyGen extends DataStoreKeyGen<Setting> {
    @Override
    public EntityPath createKey(Setting domainObject) {
      return createKey(domainObject.getContext(), domainObject.getName());
    }

    /** Synthesize a primary key from the user's loginId. */
    public EntityPath createKey(String context, String name) {
      // TODO-p1(george) need to escape context and name, because they can include '/'
      EntityPath result = new EntityPath(createParentPath(Setting.class) + context + ContainerPath.CONTAINER_PATH_SEPARATOR, name);

      return result;
    }
  }

  // ============================================================================
  // DscSettingDAO
  // ============================================================================

  public DscSettingDAO() {
    super(Setting.class);
  }

  @Override
  public boolean isExistingSetting(String context, String name) {
    EntityPath id = ((DscSettingKeyGen)getKeyGen()).createKey(context, name);
    boolean result = isExistingEntity(id);

    return result;
  }

  @Override
  public String getSettingValue(String context, String name) {
    return getSettingValue(context, name, null);
  }

  @Override
  public String getSettingValue(String context, String name, String defaultValue) {
    String result = null;
    Setting setting = getSetting(context, name);

    if (setting != null) {
      result = setting.getValue();
    }

    return result;
  }

  @Override
  public Setting getSetting(String context, String name) {
    EntityPath key = ((DscSettingKeyGen)getKeyGen()).createKey(context, name);
    Setting result = getByKey(key);

    return result;
  }

  @Override
  public List<? extends Setting> getSettings(String context) {
    // TODO-p2(george) Auto-generated method stub
    return null;
  }

  @Override
  protected DataStoreKeyGen<Setting> createKeyGen() {
    return new DscSettingKeyGen();
  }

}
