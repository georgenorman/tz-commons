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
package com.thruzero.auth.dsc.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.auth.dsc.dao.DscUserDAO;
import com.thruzero.auth.dsc.test.support.AbstractAuthDscTestCase;
import com.thruzero.auth.service.UserService;
import com.thruzero.auth.service.impl.GenericUserService;
import com.thruzero.auth.test.support.service.AbstractUserServiceTestHelper;
import com.thruzero.common.core.locator.ServiceLocator;

/**
 * Test the {@code GenericUserService} using a {@code DscUserDAO}.
 *
 * @author George Norman
 */
public class DscUserServiceTest extends AbstractAuthDscTestCase<DscUserDAO> {
  private static final AbstractUserServiceTestHelper testHelper = new DscUserServiceTestHelper();

  // ------------------------------------------------------
  // DscUserServiceTestHelper
  // ------------------------------------------------------

  public static class DscUserServiceTestHelper extends AbstractUserServiceTestHelper {

    @Override
    protected void beginTransaction() {
    }

    @Override
    protected void commitTransaction() {
    }
  }

  // ============================================================================
  // DscUserServiceTest
  // ============================================================================

  public DscUserServiceTest() {
    super(DscUserDAO.class);
  }

  @Test
  public void testLocateUserService() {
    UserService service = ServiceLocator.locate(UserService.class);

    assertTrue(service != null);
    assertTrue(service.getClass().equals(GenericUserService.class)); // make sure the locator is configured properly
    assertTrue(((GenericUserService)service).getUserDAOClassName().equals(DscUserDAO.class.getName())); // make sure the locator is configured properly
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
