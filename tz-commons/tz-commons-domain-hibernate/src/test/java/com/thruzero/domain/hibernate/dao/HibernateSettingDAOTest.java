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
package com.thruzero.domain.hibernate.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.hibernate.test.support.AbstractDomainHibernateTestCase;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.test.support.dao.AbstractSettingDAOTestHelper;

/**
 *
 * @author George Norman
 */
public class HibernateSettingDAOTest extends AbstractDomainHibernateTestCase {
  private AbstractSettingDAOTestHelper testHelper = new HibernateSettingDAOTestHelper();

  // -----------------------------------------------
  // HibernateSettingDAOTestHelper
  // -----------------------------------------------

  public class HibernateSettingDAOTestHelper extends AbstractSettingDAOTestHelper {

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
  // HibernateSettingDAOTest
  // =====================================================================

  @Test
  public void testLocateSettingDAO() {
    SettingDAO dao = DAOLocator.locate(SettingDAO.class);

    assertTrue(dao != null);
    assertTrue(dao.getClass().equals(HibernateSettingDAO.class)); // make sure the locator is configured properly
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

}
