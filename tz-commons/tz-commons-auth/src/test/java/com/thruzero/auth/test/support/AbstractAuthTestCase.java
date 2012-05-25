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
package com.thruzero.auth.test.support;

import org.junit.After;
import org.junit.Before;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.service.impl.AuthServiceRegistry;
import com.thruzero.auth.test.mock.MockAuthDAORegistry;
import com.thruzero.auth.test.mock.MockUserDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Base test class that provides setup and tear-down for the auth tests.
 *
 * @author George Norman
 */
public abstract class AbstractAuthTestCase extends AbstractCoreTestCase {
  public static final String DEFAULT_TEMP_DIR_NAME = "temp";

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    AuthServiceRegistry.getInstance().registerAllInterfaces(); // register default auth services. The default persistent services use what ever DAOs are registered (see below).
    MockAuthDAORegistry.getInstance().registerAllInterfaces(); // register the mock DAOs (which will be used by the persistent services)
  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    // delete the contents of the data-store
    MockUserDAO mockUserDAO = (MockUserDAO)DAOLocator.locate(UserDAO.class);
    mockUserDAO.resetStore();

    AuthServiceRegistry.getInstance().reset();
    MockAuthDAORegistry.getInstance().reset();
  }

}
