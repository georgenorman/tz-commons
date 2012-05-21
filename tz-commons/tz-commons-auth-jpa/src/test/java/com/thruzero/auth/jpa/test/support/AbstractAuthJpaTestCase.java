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
package com.thruzero.auth.jpa.test.support;

import com.thruzero.auth.jpa.dao.JpaAuthDAORegistry;
import com.thruzero.auth.service.impl.AuthServiceRegistry;
import com.thruzero.domain.jpa.test.support.AbstractDomainJpaTestCase;

/**
 * Base test class that provides setup and tear-down for the auth-jpa tests.
 *
 * @author George Norman
 */
public abstract class AbstractAuthJpaTestCase extends AbstractDomainJpaTestCase {

  @Override
  public void setUp() throws Exception {
    super.setUp();

    AuthServiceRegistry.getInstance().registerAllInterfaces(); // register default auth services. The default persistent services use what ever DAOs are registered (see below).
    JpaAuthDAORegistry.getInstance().registerAllInterfaces();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();

    AuthServiceRegistry.getInstance().reset();
    JpaAuthDAORegistry.getInstance().reset();
  }

}
