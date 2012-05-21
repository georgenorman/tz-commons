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
package com.thruzero.domain.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.domain.dao.mock.MockSettingDAO;
import com.thruzero.domain.service.SettingService;
import com.thruzero.domain.test.support.AbstractDomainTestCase;
import com.thruzero.domain.test.support.service.AbstractSettingServiceTestHelper;

/**
 *
 * @author George Norman
 */
public class GenericSettingServiceTest extends AbstractDomainTestCase {
  private static final AbstractSettingServiceTestHelper testHelper = new GenericSettingServiceTestHelper();

  // --------------------------------------------------------------
  // GenericSettingServiceTestHelper
  // --------------------------------------------------------------

  public static class GenericSettingServiceTestHelper extends AbstractSettingServiceTestHelper {

    @Override
    protected void beginTransaction() {
    }

    @Override
    protected void commitTransaction() {
    }
  }

  // ========================================================================================
  // GenericSettingServiceTest
  // ========================================================================================

  @Test
  public void testLocateService() {
    SettingService service = ServiceLocator.locate(SettingService.class);

    assertNotNull(service);
    assertTrue(service.getClass().equals(GenericSettingService.class)); // make sure the locator is configured properly
    assertTrue(((GenericSettingService)service).getSettingDAOClassName().equals(MockSettingDAO.class.getName())); // make sure the locator is configured properly
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
