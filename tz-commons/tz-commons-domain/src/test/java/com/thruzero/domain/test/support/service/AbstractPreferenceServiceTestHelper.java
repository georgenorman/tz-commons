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

import com.thruzero.common.core.support.ValueTransformer;
import com.thruzero.domain.dao.GenericDAO;
import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.Preference;
import com.thruzero.domain.service.PreferenceService;
import com.thruzero.domain.service.impl.AbstractCrudService;
import com.thruzero.domain.test.support.dao.PreferenceTestBuilder;
import com.thruzero.domain.test.support.dao.PreferenceTestBuilder.PreferenceTestConst;

/**
 * Shared PreferenceService test code. Contains common constants, set-up functions and tests.
 *
 * @author George Norman
 */
public abstract class AbstractPreferenceServiceTestHelper {
  private PreferenceTestBuilder preferenceTestBuilder = new PreferenceTestBuilder();

  // -----------------------------------------------
  // MockPreferenceService
  // -----------------------------------------------

  /** Exists solely to test the PreferenceDAO implementation. */
  public static class MockPreferenceService extends AbstractCrudService<Preference> implements PreferenceService {
    private PreferenceDAO dao = DAOLocator.locate(PreferenceDAO.class);

    @Override
    public Preference getPreference(String owner, String context, String name) {
      return dao.getPreference(owner, context, name);
    }

    @Override
    public String getPreferenceValue(String owner, String context, String name, String defaultValue) {
      String result = dao.getPreferenceValue(owner, context, name, defaultValue);

      return result;
    }

    @Override
    public String getPreferenceValue(String owner, String context, String name) {
      String result = dao.getPreferenceValue(owner, context, name);

      return result;
    }

    @Override
    public ValueTransformer<String> getValueTransformer(String owner, String context, String name) {
      return new ValueTransformer<String>(getPreferenceValue(owner, context, name));
    }

    @Override
    public void saveOrUpdatePreference(Preference preference) {
      dao.saveOrUpdate(preference);
    }

    @Override
    public void deletePreference(Preference preference) {
      dao.delete(preference);
    }

    @Override
    protected GenericDAO<Preference> getDAO() {
      return dao;
    }
  }

  // =====================================================================
  // AbstractPreferenceServiceTestHelper
  // =====================================================================

  public void doTestGetNonExistantPreference(PreferenceService preferenceService) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference persistedPreference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference == null);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestCreateNewPreference(PreferenceService preferenceService) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference persistedPreference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference == null);
        }
          break;
        case 2: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          preferenceService.saveOrUpdatePreference(newPreference1);
        }
          break;
        case 3: {
          Preference persistedPreference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertEquals(preferenceTestBuilder.createTestPreferenceOneA(), persistedPreference);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestDeletePersistedPreference(PreferenceService preferenceService) {
    Preference persistedPreference = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          preferenceService.saveOrUpdatePreference(newPreference1);
        }
          break;
        case 2: {
          persistedPreference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference != null);
        }
          break;
        case 3: {
          preferenceService.deletePreference(persistedPreference);
        }
          break;
        case 4: {
          persistedPreference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference == null);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestSaveOrUpdatePreference(PreferenceService preferenceService) {
    Preference persistedPreference = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          preferenceService.saveOrUpdatePreference(newPreference1);
        }
          break;
        case 2: {
          persistedPreference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertTrue(persistedPreference != null);
        }
          break;
        case 3: {
          persistedPreference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          persistedPreference.setValue(PreferenceTestConst.TEST_ONE_VALUEB);
          preferenceService.saveOrUpdatePreference(persistedPreference);
        }
          break;
        case 4: {
          persistedPreference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
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

  public void doTestGetValuePreference(PreferenceService preferenceService) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          Preference newPreference1 = preferenceTestBuilder.createTestPreferenceOneA();
          preferenceService.saveOrUpdatePreference(newPreference1);
        }
          break;
        case 2: {
          String value = preferenceService.getPreferenceValue(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME);
          assertEquals(PreferenceTestConst.TEST_ONE_VALUEA, value);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestGetComplexPreference(PreferenceService preferenceService) {
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
          Preference preference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, preferenceTestBuilder.getComplexNameOne(5));

          // test value
          assertNotNull(preference);
          assertEquals(preferenceTestBuilder.getComplexValueOne(5), preference.getValue());
        }
          break;
        case 3: {
          // delete it
          Preference preference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, preferenceTestBuilder.getComplexNameOne(5));
          preferenceService.deletePreference(preference);
        }
          break;
        case 4: {
          // verify
          Preference preference = preferenceService.getPreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, preferenceTestBuilder.getComplexNameOne(5));
          assertNull(preference);
          break;
        }
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  protected abstract void beginTransaction();

  protected abstract void commitTransaction();

}
