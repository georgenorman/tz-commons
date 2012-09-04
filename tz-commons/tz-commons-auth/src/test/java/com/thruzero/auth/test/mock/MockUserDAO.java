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
package com.thruzero.auth.test.mock;

import java.io.Serializable;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.model.User;
import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.KeyGen;
import com.thruzero.domain.dao.impl.GenericMemoryDAO;
import com.thruzero.domain.store.SimpleMemoryStore;

/**
 * Implementation of UserDAO used for testing.
 *
 * @author George Norman
 */
public final class MockUserDAO extends GenericMemoryDAO<User> implements UserDAO {

  // ------------------------------------------------------
  // UserKeyGen
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying an instance of User in the store. */
  public static class UserKeyGen extends KeyGen<User> {
    @Override
    public Serializable createKey(User domainObject) {
      return createKey(domainObject.getLoginId());
    }

    /** Synthesize a primary key from the user's loginId. */
    public EntityPath createKey(String loginId) {
      EntityPath result = new EntityPath(ContainerPath.CONTAINER_PATH_SEPARATOR, loginId);

      return result;
    }
  }

  // ============================================================================
  // MockUserDAO
  // ============================================================================

  /**
   * Use {@link com.thruzero.domain.locator.DAOLocator DAOLocator} to access a particular DAO.
   */
  private MockUserDAO() {
    super(new SimpleMemoryStore<User>());
  }

  @Override
  public User getUserByLoginId(String loginId) {
    EntityPath id = ((UserKeyGen)getKeyGen()).createKey(loginId);
    User result = getByKey(id);

    return result;
  }

  @Override
  protected KeyGen<User> createKeyGen() {
    return new UserKeyGen();
  }
}
