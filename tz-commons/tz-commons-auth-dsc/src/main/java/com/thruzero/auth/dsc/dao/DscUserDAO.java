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
package com.thruzero.auth.dsc.dao;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.model.User;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dsc.dao.AbstractDataStoreDAO;
import com.thruzero.domain.dsc.dao.GenericDscDAO;

/**
 * An implementation of UserDAO that uses a DataStoreContainer (DSC) as storage.
 * <p>
 * This DAO requires initialization (see {@link AbstractDataStoreDAO.DataStoreDAOInitParamKeys} for
 * details). Following is an example initialization using the config file using the FileDataStoreContainer:
 *
 * <xmp>
 *   <!-- Define the generic file-system DAO settings (see "config.fs.domain.dao.xml") -->
 *   <section name="com.thruzero.domain.fs.dao.DscUserDAO">
 *     <entry key="storePath" value="[com.thruzero.domain.dsc.dao.AbstractDataStoreDAO]{storePath}/DscUserDAO" />
 *     <entry key="com.thruzero.domain.dsc.store.DataStoreContainerFactory" value="[com.thruzero.domain.dsc.dao.AbstractDataStoreDAO]{com.thruzero.domain.dsc.store.DataStoreContainerFactory}" />
 *   </section>
 *
 *   <!-- Define application-specific file-system DAO settings (see "config.xml" from the  "pf-test18-jpa-war" project) -->
 *   <section name="com.thruzero.domain.dsc.dao.AbstractDataStoreDAO">
 *     <!-- full path to the data store directory (can be anywhere on the file system) -->
 *     <entry key="storePath" value="/home/george/pf-test-desktop/demo-data-store" />
 *
 *     <!-- The type of DataStoreContainerFactory to use -->
 *     <entry key="com.thruzero.domain.dsc.store.DataStoreContainerFactory" value="com.thruzero.domain.fs.support.FileDataStoreContainerFactory" />
 *   </section>
 * </xmp>
 *
 * As shown above, the "com.thruzero.domain.dsc.dao.AbstractDataStoreDAO" section is referenced to retrieve the base
 * storePath (and "DscUserDAO" is appended to that path).
 *
 * @author George Norman
 */
public class DscUserDAO extends GenericDscDAO<User> implements UserDAO {

  // ------------------------------------------------------
  // DscUserKeyGen
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying a User instance in the store. */
  public static class DscUserKeyGen extends DataStoreKeyGen<User> {
    @Override
    public EntityPath createKey(User domainObject) {
      return createKey(domainObject.getLoginId());
    }

    /** Synthesize a primary key from the user's loginId. */
    public EntityPath createKey(String loginId) {
      EntityPath result = new EntityPath(createParentPath(User.class), loginId);

      return result;
    }
  }

  // ============================================================================
  // DscUserDAO
  // ============================================================================

  public DscUserDAO() {
    super(User.class);
  }

  @Override
  public User getUserByLoginId(String loginId) {
    EntityPath key = ((DscUserKeyGen)getKeyGen()).createKey(loginId);
    User result = getByKey(key);

    return result;
  }

  @Override
  protected DataStoreKeyGen<User> createKeyGen() {
    return new DscUserKeyGen();
  }

}
