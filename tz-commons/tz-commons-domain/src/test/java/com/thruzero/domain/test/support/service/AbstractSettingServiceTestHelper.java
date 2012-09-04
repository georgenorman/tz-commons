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
package com.thruzero.domain.test.support.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import com.thruzero.common.core.support.ValueTransformer;
import com.thruzero.domain.dao.GenericDAO;
import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.Setting;
import com.thruzero.domain.service.SettingService;
import com.thruzero.domain.service.impl.AbstractCrudService;
import com.thruzero.domain.test.support.dao.SettingTestBuilder;
import com.thruzero.domain.test.support.dao.SettingTestBuilder.SettingTestConst;


/**
 * Shared SettingService test code. Contains common constants, set-up functions and tests.
 *
 * @author George Norman
 */
public abstract class AbstractSettingServiceTestHelper {
  private SettingTestBuilder settingTestBuilder = new SettingTestBuilder();

  // -----------------------------------------------
  // MockSettingService
  // -----------------------------------------------

  /** Exists solely to test the SettingDAO implementation. */
  public static final class MockSettingService extends AbstractCrudService<Setting> implements SettingService {
    private SettingDAO dao = DAOLocator.locate(SettingDAO.class);

    /**
     * Use {@link com.thruzero.common.core.locator.ServiceLocator ServiceLocator} to access a particular Service.
     */
    private MockSettingService() {
    }

    @Override
    public Setting getSetting(String context, String name) {
      Setting result = dao.getSetting(context, name);

      return result;
    }

    @Override
    public List<? extends Setting> getSettings(String context) {
      List<? extends Setting> result = dao.getSettings(context);

      return result;
    }

    @Override
    public void deleteSetting(Setting setting) {
      dao.delete(setting);
    }

    @Override
    public String getStringValue(String context, String name, String defaultValue) {
      String result = dao.getSettingValue(context, name, defaultValue);

      return result;
    }

    @Override
    public String getStringValue(String context, String name) {
      String result = dao.getSettingValue(context, name);

      return result;
    }

    @Override
    public ValueTransformer<String> getValueTransformer(String context, String name) {
      String result = dao.getSettingValue(context, name);

      return new ValueTransformer<String>(result);
    }

    @Override
    public Set<String> splitStringValueFor(String context, String name, String separator) {
      return null;
    }

    @Override
    public void saveOrUpdatSetting(Setting setting) {
      dao.saveOrUpdate(setting);
    }

    @Override
    protected GenericDAO<Setting> getDAO() {
      return dao;
    }
  }

  // ====================================================================================
  // AbstractSettingServiceTestHelper
  // ====================================================================================

  public void doTestGetNonExistantSetting(SettingService preferenceService) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting persistedSetting = preferenceService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertTrue(persistedSetting == null);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestCreateNewSetting(SettingService settingService) {
    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting persistedSetting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertNull(persistedSetting);
        }
          break;
        case 2: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          settingService.saveOrUpdatSetting(newSetting1);
        }
          break;
        case 3: {
          String persistedValue = settingService.getStringValue(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME, null);
          assertEquals(SettingTestConst.TEST_ONE_VALUEA, persistedValue);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestDeletePersistedSetting(SettingService settingService) {
    Setting persistedSetting = null;

    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          settingService.saveOrUpdatSetting(newSetting1);
        }
          break;
        case 2: {
          persistedSetting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertTrue(persistedSetting != null);
        }
          break;
        case 3: {
          settingService.deleteSetting(persistedSetting);
        }
          break;
        case 4: {
          persistedSetting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertTrue(persistedSetting == null);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestSaveOrUpdatePersistedSetting(SettingService settingService) {
    Setting persistedSetting = null;

    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          settingService.saveOrUpdatSetting(newSetting1);
        }
          break;
        case 2: {
          persistedSetting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertTrue(persistedSetting != null);
        }
          break;
        case 3: {
          persistedSetting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          persistedSetting.setValue(SettingTestConst.TEST_ONE_VALUEB);
          settingService.saveOrUpdatSetting(persistedSetting);
        }
          break;
        case 4: {
          persistedSetting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertNotNull(persistedSetting);
          assertEquals(SettingTestConst.TEST_ONE_VALUEB, persistedSetting.getValue());
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestGetValueSetting(SettingService settingService) {
    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          settingService.saveOrUpdatSetting(newSetting1);
        }
          break;
        case 2: {
          String value = settingService.getStringValue(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME, null);
          assertEquals(SettingTestConst.TEST_ONE_VALUEA, value);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestGetComplexSetting(SettingService settingService) {
    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          // setup multiple settings
          settingTestBuilder.setupComplexSetting();
        }
          break;
        case 2: {
          // get it
          Setting setting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, settingTestBuilder.getComplexNameOne(5));

          // test value
          assertNotNull(setting);
          assertEquals(settingTestBuilder.getComplexValueOne(5), setting.getValue());
        }
          break;
        case 3: {
          // delete it
          Setting setting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, settingTestBuilder.getComplexNameOne(5));
          settingService.deleteSetting(setting);
        }
          break;
        case 4: {
          // verify
          Setting setting = settingService.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, settingTestBuilder.getComplexNameOne(5));
          assertNull(setting);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  protected abstract void beginTransaction();

  protected abstract void commitTransaction();
}
