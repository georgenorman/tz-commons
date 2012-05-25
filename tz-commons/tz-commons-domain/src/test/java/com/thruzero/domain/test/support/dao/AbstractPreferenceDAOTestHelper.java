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

import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.model.Preference;
import com.thruzero.domain.test.support.dao.PreferenceTestBuilder.PreferenceTestConst;

/**
 * Shared PreferenceDAO test code. Contains common constants, set-up functions and tests.
 *
 * @author George Norman
 */
public abstract class AbstractPreferenceDAOTestHelper {
  private PreferenceTestBuilder preferenceTestBuilder = new PreferenceTestBuilder();

  public void doTestGetNonExistantPreference(PreferenceDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference preference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.BOGUS_NAME);
          assertNull(preference);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestCreateNewPreference(PreferenceDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference == null);
        }
          break;
        case 2: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          dao.save(newPreference1);
        }
          break;
        case 3: {
          Preference persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertEquals(preferenceTestBuilder.createTestPreferenceOneA(), persistedPreference);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestDeletePersistedPreference(PreferenceDAO dao) {
    Preference persistedPreference = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          dao.save(newPreference1);
        }
          break;
        case 2: {
          persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference != null);
        }
          break;
        case 3: {
          dao.delete(persistedPreference);
        }
          break;
        case 4: {
          persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference == null);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestUpdatePersistedPreference(PreferenceDAO dao) {
    Preference persistedPreference = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          dao.save(newPreference1);
        }
          break;
        case 2: {
          persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference != null);
        }
          break;
        case 3: {
          persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          persistedPreference.setValue(PreferenceTestConst.TEST_ONE_VALUEB);
          dao.update(persistedPreference);
        }
          break;
        case 4: {
          persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference != null);
          assertEquals(PreferenceTestConst.TEST_ONE_VALUEB, persistedPreference.getValue());
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestSaveOrUpdatePersistedPreference(PreferenceDAO dao) {
    Preference persistedPreference = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          dao.saveOrUpdate(newPreference1);
        }
          break;
        case 2: {
          persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference != null);
        }
          break;
        case 3: {
          persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          persistedPreference.setValue(PreferenceTestConst.TEST_ONE_VALUEB);
          dao.saveOrUpdate(persistedPreference);
        }
          break;
        case 4: {
          persistedPreference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference != null);
          assertEquals(PreferenceTestConst.TEST_ONE_VALUEB, persistedPreference.getValue());
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestGetValuePreference(PreferenceDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          dao.save(newPreference1);
        }
          break;
        case 2: {
          String value = dao.getPreferenceValue(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertEquals(PreferenceTestConst.TEST_ONE_VALUEA, value);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestGetComplexPreference(PreferenceDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          // setup multiple preferences
          preferenceTestBuilder.setupComplexPreference();
        }
          break;
        case 2: {
          // get it
          Preference preference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, preferenceTestBuilder.getComplexNameOne(5));

          // test value
          assertNotNull(preference);
          assertEquals(preferenceTestBuilder.getComplexValueOne(5), preference.getValue());
        }
          break;
        case 3: {
          // delete it
          Preference preference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, preferenceTestBuilder.getComplexNameOne(5));
          dao.delete(preference);
        }
          break;
        case 4: {
          // verify
          Preference preference = dao.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, preferenceTestBuilder.getComplexNameOne(5));
          assertNull(preference);
          break;
        }
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestIsExistingPreference(PreferenceDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          dao.save(newPreference1);
        }
          break;
        case 2: {
          boolean value = dao.isExistingPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(value);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  protected abstract void beginTransaction();

  protected abstract void commitTransaction();

}
