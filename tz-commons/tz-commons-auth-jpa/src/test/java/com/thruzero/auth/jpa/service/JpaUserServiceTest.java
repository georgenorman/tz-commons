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
package com.thruzero.auth.jpa.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.auth.jpa.dao.JpaUserDAO;
import com.thruzero.auth.jpa.test.support.AbstractAuthJpaTestCase;
import com.thruzero.auth.service.UserService;
import com.thruzero.auth.service.impl.GenericUserService;
import com.thruzero.auth.test.support.service.AbstractUserServiceTestHelper;
import com.thruzero.common.core.locator.ServiceLocator;

/**
 * Test the {@code GenericUserService} using a {@code JpaUserDAO}.
 *
 * @author George Norman
 */
public class JpaUserServiceTest extends AbstractAuthJpaTestCase {
  private AbstractUserServiceTestHelper testHelper = new DscUserServiceTestHelper();

  // ------------------------------------------------------
  // AbstractUserServiceTestHelper
  // ------------------------------------------------------

  public class DscUserServiceTestHelper extends AbstractUserServiceTestHelper {

    @Override
    protected void beginTransaction() {
      getTransactionService().beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
    }

    @Override
    protected void commitTransaction() {
      getTransactionService().commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  // ============================================================================
  // JpaUserServiceTest
  // ============================================================================

  @Test
  public void testLocateUserService() {
    UserService service = ServiceLocator.locate(UserService.class);

    assertTrue(service != null);
    assertTrue(service.getClass().equals(GenericUserService.class)); // make sure the locator is configured properly
    assertTrue(((GenericUserService)service).getUserDAOClassName().equals(JpaUserDAO.class.getName())); // make sure the locator is configured properly
  }

  @Test
  public void testGetNonExistantUser() {
    testHelper.doTestGetNonExistantUser(ServiceLocator.locate(UserService.class));
  }

  @Test
  public void testCreateNewUser() {
    testHelper.doTestCreateNewUser(ServiceLocator.locate(UserService.class));
  }

  @Test
  public void testDeletePersistedUser() {
    testHelper.doTestDeletePersistedUser(ServiceLocator.locate(UserService.class));
  }

  @Test
  public void testUpdatePersistedUser() {
    testHelper.doTestUpdatePersistedUser(ServiceLocator.locate(UserService.class));
  }
}
