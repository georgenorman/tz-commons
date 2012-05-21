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

import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.Preference;

/**
 *
 * @author George Norman
 */
public class PreferenceTestBuilder {

  // -----------------------------------------------------
  // PreferenceTestConst
  // -----------------------------------------------------

  public static interface PreferenceTestConst {
    String TEST_ONE_OWNER = "owner1@thruzero.com";
    String TEST_ONE_CONTEXTA = "context1a";
    String TEST_ONE_CONTEXTB = "context1b";
    String TEST_ONE_NAME = "name-one";
    String TEST_ONE_VALUEA = "value-one-a";
    String TEST_ONE_VALUEB = "value-one-b";

    String TEST_TWO_OWNER = "owner2@thruzero.com";
    String TEST_TWO_CONTEXT = "context2";
    String TEST_TWO_NAME = "name-two";
    String TEST_TWO_VALUE = "value-two";

    String BOGUS_NAME = "bogus"; // this preference does not exist
  }

  // =======================================================================================
  // PreferenceTestBuilder
  // =======================================================================================

  public Preference createTestPreferenceOneA() {
    return new Preference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, PreferenceTestConst.TEST_ONE_NAME, PreferenceTestConst.TEST_ONE_VALUEA);
  }

  public Preference createTestPreferenceTwo() {
    return new Preference(PreferenceTestConst.TEST_TWO_OWNER, PreferenceTestConst.TEST_TWO_CONTEXT, PreferenceTestConst.TEST_TWO_NAME, PreferenceTestConst.TEST_TWO_VALUE);
  }

  public void setupComplexPreference() {
    // setup multiple preferences
    for (int i = 1; i <= 10; i++) {
      saveOrUpdatePreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTA, getComplexNameOne(i), getComplexValueOne(i));
    }

    for (int i = 1; i <= 10; i++) {
      saveOrUpdatePreference(PreferenceTestConst.TEST_ONE_OWNER, PreferenceTestConst.TEST_ONE_CONTEXTB, getComplexNameOne(i), getComplexValueOne(i));
    }

    for (int i = 1; i <= 10; i++) {
      saveOrUpdatePreference(PreferenceTestConst.TEST_TWO_OWNER, PreferenceTestConst.TEST_TWO_CONTEXT, getComplexNameTwo(i), getComplexValueTwo(i));
    }
  }

  public String getComplexNameOne(int i) {
    return PreferenceTestConst.TEST_ONE_NAME + "_" + i;
  }

  public String getComplexValueOne(int i) {
    return PreferenceTestConst.TEST_ONE_VALUEA + "_" + i;
  }

  public String getComplexNameTwo(int i) {
    return PreferenceTestConst.TEST_TWO_NAME + "_" + i;
  }

  public String getComplexValueTwo(int i) {
    return PreferenceTestConst.TEST_TWO_VALUE + "_" + i;
  }

  public Preference saveOrUpdatePreference(final String owner, final String context, final String name, final String value) {
    Preference testPreference = new Preference(owner, context, name, value);

    return saveOrUpdatePreference(testPreference);
  }

  public Preference saveOrUpdatePreference(Preference preference) {
    PreferenceDAO dao = DAOLocator.locate(PreferenceDAO.class);

    // create it
    dao.saveOrUpdate(preference);

    return preference;
  }

}
