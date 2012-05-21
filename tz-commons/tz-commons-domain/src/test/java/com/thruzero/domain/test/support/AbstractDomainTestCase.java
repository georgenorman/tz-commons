/*
 *   Copyright 2011 George Norman
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
package com.thruzero.domain.test.support;

import org.junit.After;
import org.junit.Before;

import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.dao.mock.MockDomainDAORegistry;
import com.thruzero.domain.dao.mock.MockPreferenceDAO;
import com.thruzero.domain.dao.mock.MockSettingDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.service.impl.DomainServiceRegistry;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Abstract base class used by all domain test classes. Sets up domain services to be tested as well as mock DAOs.
 *
 * @author George Norman
 */
public abstract class AbstractDomainTestCase extends AbstractCoreTestCase {
  public static final String DEFAULT_TEMP_DIR_NAME = "temp";

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    DomainServiceRegistry.getInstance().registerAllInterfaces(); // register default domain services. The default persistent services use what ever DAOs are registered (see below).
    MockDomainDAORegistry.getInstance().registerAllInterfaces(); // register the mock DAOs (which will be used by the persistent services)
  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    // delete the contents of the data-store directory
    MockPreferenceDAO mockPreferenceDAO = (MockPreferenceDAO)DAOLocator.locate(PreferenceDAO.class);
    mockPreferenceDAO.resetStore();
    MockSettingDAO mockSettingDAO = (MockSettingDAO)DAOLocator.locate(SettingDAO.class);
    mockSettingDAO.resetStore();

    // reset registries
    DomainServiceRegistry.getInstance().reset();
    MockDomainDAORegistry.getInstance().reset();
  }

}
