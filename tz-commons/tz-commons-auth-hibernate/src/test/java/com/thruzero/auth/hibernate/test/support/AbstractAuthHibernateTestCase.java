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
package com.thruzero.auth.hibernate.test.support;

import org.junit.After;
import org.junit.Before;

import com.thruzero.auth.hibernate.dao.HibernateAuthDAORegistry;
import com.thruzero.auth.service.impl.AuthServiceRegistry;
import com.thruzero.domain.hibernate.test.support.AbstractDomainHibernateTestCase;

/**
 * Base test class that provides setup and tear-down for the auth-hibernate tests.
 *
 * @author George Norman
 */
public abstract class AbstractAuthHibernateTestCase extends AbstractDomainHibernateTestCase {
  public static final String DEFAULT_TEMP_DIR_NAME = "temp";

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    AuthServiceRegistry.getInstance().registerAllInterfaces(); // register default auth services. The default persistent services use what ever DAOs are registered (see below).
    HibernateAuthDAORegistry.getInstance().registerAllInterfaces();
  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    AuthServiceRegistry.getInstance().reset();
    HibernateAuthDAORegistry.getInstance().reset();
  }

}
