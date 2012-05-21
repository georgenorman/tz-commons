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
import com.thruzero.domain.jpa.dao.JpaSettingDAO;
import com.thruzero.domain.jpa.test.support.AbstractDomainJpaTestCase;
import com.thruzero.domain.service.SettingService;
import com.thruzero.domain.service.impl.GenericSettingService;
import com.thruzero.domain.test.support.service.AbstractSettingServiceTestHelper;

/**
 * Test the {@code GenericSettingService} using a {@code JpaSettingDAO}.
 *
 * @author George Norman
 */
public class JpaSettingServiceTest extends AbstractDomainJpaTestCase {
  private AbstractSettingServiceTestHelper testHelper = new DscSettingServiceTestHelper();

  // ------------------------------------------------------
  // AbstractSettingServiceTestHelper
  // ------------------------------------------------------

  public class DscSettingServiceTestHelper extends AbstractSettingServiceTestHelper {

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
  // JpaSettingServiceTest
  // ============================================================================

  @Test
  public void testLocateSettingService() {
    SettingService service = ServiceLocator.locate(SettingService.class);

    assertTrue(service != null);
    assertTrue(service.getClass().equals(GenericSettingService.class)); // make sure the locator is configured properly
    assertTrue(((GenericSettingService)service).getSettingDAOClassName().equals(JpaSettingDAO.class.getName())); // make sure the locator is configured properly
  }

  @Test
  public void testGetNonExistantSetting() {
    testHelper.doTestGetNonExistantSetting(ServiceLocator.locate(SettingService.class));
  }

  @Test
  public void testCreateNewSetting() {
    testHelper.doTestCreateNewSetting(ServiceLocator.locate(SettingService.class));
  }

  @Test
  public void testDeletePersistedSetting() {
    testHelper.doTestDeletePersistedSetting(ServiceLocator.locate(SettingService.class));
  }

  @Test
  public void testSaveOrUpdatePersistedSetting() {
    testHelper.doTestSaveOrUpdatePersistedSetting(ServiceLocator.locate(SettingService.class));
  }

  @Test
  public void testGetValueSetting() {
    testHelper.doTestGetValueSetting(ServiceLocator.locate(SettingService.class));
  }

  @Test
  public void testGetComplexSetting() {
    testHelper.doTestGetComplexSetting(ServiceLocator.locate(SettingService.class));
  }

}
