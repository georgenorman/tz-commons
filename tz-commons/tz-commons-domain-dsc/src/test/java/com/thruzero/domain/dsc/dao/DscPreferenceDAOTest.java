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
package com.thruzero.domain.dsc.dao;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.dsc.test.support.AbstractDomainDscTestCase;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.test.support.dao.AbstractPreferenceDAOTestHelper;

/**
 *
 * @author George Norman
 */
public class DscPreferenceDAOTest extends AbstractDomainDscTestCase<DscPreferenceDAO> {
  private static final AbstractPreferenceDAOTestHelper testHelper = new DscPreferenceDAOTestHelper();

  // ------------------------------------------------------
  // AbstractPreferenceDAOTestHelper
  // ------------------------------------------------------

  public static class DscPreferenceDAOTestHelper extends AbstractPreferenceDAOTestHelper {

    @Override
    protected void beginTransaction() {
    }

    @Override
    protected void commitTransaction() {
    }
  }

  // ============================================================================
  // AbstractPreferenceDAOTest
  // ============================================================================

  public DscPreferenceDAOTest() {
    super(DscPreferenceDAO.class);
  }

  @Test
  public void testLocatePreferenceDAO() {
    PreferenceDAO dao = DAOLocator.locate(PreferenceDAO.class);

    assertTrue(dao != null);
    assertTrue(dao.getClass().equals(DscPreferenceDAO.class)); // make sure the locator is configured properly
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

}
