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
package com.thruzero.domain.dao.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.dao.mock.MockPreferenceDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.test.support.AbstractDomainTestCase;
import com.thruzero.domain.test.support.dao.AbstractPreferenceDAOTestHelper;

/**
 * Test the MockPreferenceDAO, used solely for testing, but which the GenericPreferenceService will use during its test. The DAO is tested
 * independently, to ensure a valid GenericPreferenceService test.
 *
 * @author George Norman
 */
public class MockPreferenceDAOTest extends AbstractDomainTestCase {
  private static final AbstractPreferenceDAOTestHelper testHelper = new MockPreferenceDAOTestHelper();

  // -----------------------------------------------
  // MockPreferenceDAOTestHelper
  // -----------------------------------------------

  public static class MockPreferenceDAOTestHelper extends AbstractPreferenceDAOTestHelper {

    @Override
    protected void beginTransaction() {
    }

    @Override
    protected void commitTransaction() {
    }
  }

  // =====================================================================
  // MockPreferenceDAOTest
  // =====================================================================

  @Test
  public void testLocatePreferenceDAO() {
    PreferenceDAO dao = DAOLocator.locate(PreferenceDAO.class);

    assertTrue(dao != null);
    assertTrue(dao.getClass().equals(MockPreferenceDAO.class)); // make sure the locator is configured properly
  }

  @Test
  public void testGetNonExistantPreference() {
    testHelper.doTestGetNonExistantPreference(DAOLocator.locate(PreferenceDAO.class));
  }

  @Test
  public void testCreateNewPreference() {
    testHelper.doTestCreateNewPreference(DAOLocator.locate(PreferenceDAO.class));
  }

  @Test
  public void testDeletePersistedPreference() {
    testHelper.doTestDeletePersistedPreference(DAOLocator.locate(PreferenceDAO.class));
  }

  @Test
  public void testUpdatePersistedPreference() {
    testHelper.doTestUpdatePersistedPreference(DAOLocator.locate(PreferenceDAO.class));
  }

  @Test
  public void testSaveOrUpdatePersistedPreference() {
    testHelper.doTestSaveOrUpdatePersistedPreference(DAOLocator.locate(PreferenceDAO.class));
  }

  @Test
  public void testGetValuePreference() {
    testHelper.doTestGetValuePreference(DAOLocator.locate(PreferenceDAO.class));
  }

  @Test
  public void testGetComplexPreference() {
    testHelper.doTestGetComplexPreference(DAOLocator.locate(PreferenceDAO.class));
  }

  @Test
  public void testIsExistingPreference() {
    testHelper.doTestIsExistingPreference(DAOLocator.locate(PreferenceDAO.class));
  }

}
