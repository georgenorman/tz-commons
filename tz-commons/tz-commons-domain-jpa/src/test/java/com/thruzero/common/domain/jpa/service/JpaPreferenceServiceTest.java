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
package com.thruzero.common.domain.jpa.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.domain.jpa.dao.JpaPreferenceDAO;
import com.thruzero.domain.jpa.test.support.AbstractDomainJpaTestCase;
import com.thruzero.domain.service.PreferenceService;
import com.thruzero.domain.service.impl.GenericPreferenceService;
import com.thruzero.domain.test.support.service.AbstractPreferenceServiceTestHelper;

/**
 * Test the {@code GenericPreferenceService} using a {@code JpaPreferenceDAO}.
 *
 * @author George Norman
 */
public class JpaPreferenceServiceTest extends AbstractDomainJpaTestCase {
  private AbstractPreferenceServiceTestHelper testHelper = new DscPreferenceServiceTestHelper();

  // ------------------------------------------------------
  // AbstractPreferenceServiceTestHelper
  // ------------------------------------------------------

  public class DscPreferenceServiceTestHelper extends AbstractPreferenceServiceTestHelper {

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
  // AbstractPreferenceServiceTest
  // ============================================================================

  @Test
  public void testLocatePreferenceService() {
    PreferenceService service = ServiceLocator.locate(PreferenceService.class);

    assertTrue(service != null);
    assertTrue(service.getClass().equals(GenericPreferenceService.class)); // make sure the locator is configured properly
    assertTrue(((GenericPreferenceService)service).getPreferenceDAOClassName().equals(JpaPreferenceDAO.class.getName())); // make sure the locator is configured properly
  }

  @Test
  public void testGetNonExistantPreference() {
    testHelper.doTestGetNonExistantPreference(ServiceLocator.locate(PreferenceService.class));
  }

  @Test
  public void testCreateNewPreference() {
    testHelper.doTestCreateNewPreference(ServiceLocator.locate(PreferenceService.class));
  }

  @Test
  public void testDeletePersistedPreference() {
    testHelper.doTestDeletePersistedPreference(ServiceLocator.locate(PreferenceService.class));
  }

  @Test
  public void testSaveOrUpdatePreference() {
    testHelper.doTestSaveOrUpdatePreference(ServiceLocator.locate(PreferenceService.class));
  }

  @Test
  public void testGetValuePreference() {
    testHelper.doTestGetValuePreference(ServiceLocator.locate(PreferenceService.class));
  }

  @Test
  public void testGetComplexPreference() {
    testHelper.doTestGetComplexPreference(ServiceLocator.locate(PreferenceService.class));
  }

}
