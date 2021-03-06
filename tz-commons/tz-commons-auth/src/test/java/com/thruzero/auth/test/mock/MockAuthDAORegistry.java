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

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.LocatorLogHelper;
import com.thruzero.domain.dao.DAO;
import com.thruzero.domain.dao.impl.DAORegistry;
import com.thruzero.domain.locator.DAOLocator;

/**
 * Class used to register the DAO implementations provided by this package.
 *
 * @author George Norman
 */
public final class MockAuthDAORegistry extends DAORegistry {
  private static final MockAuthDAORegistry instance = new MockAuthDAORegistry();

  /** Use getInstance() to get an instance of this class. */
  private MockAuthDAORegistry() {
  }

  public static MockAuthDAORegistry getInstance() {
    return instance;
  }

  public void registerAllInterfaces() {
    // this is only called once
    new LocatorLogHelper(MockAuthDAORegistry.class).logBeginRegisterInterfaces(DAO.class.getName(), MockAuthDAORegistry.class);

    DAOLocator.getRegistry().registerInterface(UserDAO.class, MockUserDAO.class);
  }

}
