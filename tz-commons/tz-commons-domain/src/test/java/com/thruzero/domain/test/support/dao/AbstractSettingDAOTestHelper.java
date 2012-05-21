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
package com.thruzero.domain.test.support.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.Setting;
import com.thruzero.domain.test.support.dao.SettingTestBuilder.SettingTestConst;


/**
 * Shared SettingService test code. Contains common constants and set-up functions.
 *
 * @author George Norman
 */
public abstract class AbstractSettingDAOTestHelper {
  private SettingTestBuilder settingTestBuilder = new SettingTestBuilder();

  public void doTestGetNonExistantSetting(SettingDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting setting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.BOGUS_NAME);
          assertNull(setting);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestCreateNewSetting(SettingDAO dao) {
    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertNull(persistedSetting);
        }
          break;
        case 2: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          dao.save(newSetting1);
        }
          break;
        case 3: {
          String persistedValue = dao.getSettingValue(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME, null);
          assertEquals(SettingTestConst.TEST_ONE_VALUEA, persistedValue);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestDeletePersistedSetting(SettingDAO dao) {
    Setting persistedSetting = null;

    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          dao.save(newSetting1);
        }
          break;
        case 2: {
          persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertTrue(persistedSetting != null);
        }
          break;
        case 3: {
          dao.delete(persistedSetting);
        }
          break;
        case 4: {
          persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertTrue(persistedSetting == null);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestUpdatePersistedSetting(SettingDAO dao) {
    Setting persistedSetting = null;

    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          dao.save(newSetting1);
        }
          break;
        case 2: {
          persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertTrue(persistedSetting != null);
        }
          break;
        case 3: {
          persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          persistedSetting.setValue(SettingTestConst.TEST_ONE_VALUEB);
          dao.update(persistedSetting);
        }
          break;
        case 4: {
          persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
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

  public void doTestSaveOrUpdatePersistedSetting(SettingDAO dao) {
    Setting persistedSetting = null;

    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          dao.saveOrUpdate(newSetting1);
        }
          break;
        case 2: {
          persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          assertTrue(persistedSetting != null);
        }
          break;
        case 3: {
          persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
          persistedSetting.setValue(SettingTestConst.TEST_ONE_VALUEB);
          dao.saveOrUpdate(persistedSetting);
        }
          break;
        case 4: {
          persistedSetting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME);
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

  public void doTestGetValueSetting(SettingDAO dao) {
    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Setting newSetting1 = settingTestBuilder.createTestSettingOneA();
          dao.save(newSetting1);
        }
          break;
        case 2: {
          String value = dao.getSettingValue(SettingTestConst.TEST_ONE_CONTEXTA, SettingTestConst.TEST_ONE_NAME, null);
          assertEquals(SettingTestConst.TEST_ONE_VALUEA, value);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestGetComplexSetting() {
    SettingDAO dao = DAOLocator.locate(SettingDAO.class);

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
          Setting setting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, settingTestBuilder.getComplexNameOne(5));

          // test value
          assertNotNull(setting);
          assertEquals(settingTestBuilder.getComplexValueOne(5), setting.getValue());
        }
          break;
        case 3: {
          // delete it
          Setting setting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, settingTestBuilder.getComplexNameOne(5));
          dao.delete(setting);
        }
          break;
        case 4: {
          // verify
          Setting setting = dao.getSetting(SettingTestConst.TEST_ONE_CONTEXTA, settingTestBuilder.getComplexNameOne(5));
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
