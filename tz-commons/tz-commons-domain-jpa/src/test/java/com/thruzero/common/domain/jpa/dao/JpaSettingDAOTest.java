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
package com.thruzero.common.domain.jpa.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.jpa.dao.JpaSettingDAO;
import com.thruzero.domain.jpa.test.support.AbstractDomainJpaTestCase;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.test.support.dao.AbstractSettingDAOTestHelper;

/**
 *
 * @author George Norman
 */
public class JpaSettingDAOTest extends AbstractDomainJpaTestCase {
  private AbstractSettingDAOTestHelper testHelper = new JpaSettingDAOTestHelper();

  // -----------------------------------------------
  // JpaSettingDAOTestHelper
  // -----------------------------------------------

  public class JpaSettingDAOTestHelper extends AbstractSettingDAOTestHelper {

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
  // JpaSettingDAOTest
  // =====================================================================

  @Test
  public void testLocateSettingDAO() {
    SettingDAO dao = DAOLocator.locate(SettingDAO.class);

    assertTrue(dao != null);
    assertTrue(dao.getClass().equals(JpaSettingDAO.class)); // make sure the locator is configured properly
  }

  @Test
  public void testGetNonExistantSetting() {
    testHelper.doTestGetNonExistantSetting(DAOLocator.locate(SettingDAO.class));
  }

  @Test
  public void testCreateNewSetting() {
    testHelper.doTestCreateNewSetting(DAOLocator.locate(SettingDAO.class));
  }

  @Test
  public void testDeletePersistedSetting() {
    testHelper.doTestDeletePersistedSetting(DAOLocator.locate(SettingDAO.class));
  }

  @Test
  public void testUpdatePersistedSetting() {
    testHelper.doTestUpdatePersistedSetting(DAOLocator.locate(SettingDAO.class));
  }

  @Test
  public void testSaveOrUpdatePersistedSetting() {
    testHelper.doTestSaveOrUpdatePersistedSetting(DAOLocator.locate(SettingDAO.class));
  }

  @Test
  public void testGetValueSetting() {
    testHelper.doTestGetValueSetting(DAOLocator.locate(SettingDAO.class));
  }

  @Test
  public void testGetComplexSetting() {
    testHelper.doTestGetComplexSetting();
  }

  @Test
  public void testIsExistingSetting() {
    testHelper.doTestIsExistingSetting(DAOLocator.locate(SettingDAO.class));
  }
}
