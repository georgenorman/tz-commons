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
package com.thruzero.auth.jpa.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.jpa.dao.JpaUserDAO;
import com.thruzero.auth.jpa.test.support.AbstractAuthJpaTestCase;
import com.thruzero.auth.test.support.dao.AbstractUserDAOTestHelper;
import com.thruzero.domain.locator.DAOLocator;

/**
 * Unit test for JpaUserDAO.
 */
public class JpaUserDAOTest extends AbstractAuthJpaTestCase {
  private AbstractUserDAOTestHelper testHelper = new JpaUserDAOTestHelper();

  // -----------------------------------------------
  // JpaUserDAOTestHelper
  // -----------------------------------------------

  public class JpaUserDAOTestHelper extends AbstractUserDAOTestHelper {

    @Override
    protected void beginTransaction() {
      getTransactionService().beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
    }

    @Override
    protected void commitTransaction() {
      getTransactionService().commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  // =====================================================================
  // JpaUserDAOTest
  // =====================================================================

  @Test
  public void testLocateUserDAO() {
    UserDAO dao = DAOLocator.locate(UserDAO.class);

    assertTrue(dao != null);
    assertTrue(dao.getClass().equals(JpaUserDAO.class)); // make sure the locator is configured properly
  }

  @Test
  public void testGetNonExistantUser() {
    testHelper.doTestGetNonExistantUser(DAOLocator.locate(UserDAO.class));
  }

  @Test
  public void testCreateNewUser() {
    testHelper.doTestCreateNewUser(DAOLocator.locate(UserDAO.class));
  }

  @Test
  public void testDeletePersistedUser() {
    testHelper.doTestDeletePersistedUser(DAOLocator.locate(UserDAO.class));
  }

  @Test
  public void testUpdatePersistedUser() {
    testHelper.doTestUpdatePersistedUser(DAOLocator.locate(UserDAO.class));
  }

  @Test
  public void testSaveOrUpdatePersistedUser() {
    testHelper.doTestSaveOrUpdatePersistedUser(DAOLocator.locate(UserDAO.class));
  }
}
