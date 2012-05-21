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

import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.Setting;
import com.thruzero.domain.model.Setting.Types;

/**
 *
 * @author George Norman
 */
public class SettingTestBuilder {

  // -----------------------------------------------------
  // SettingTestConst
  // -----------------------------------------------------

  public static interface SettingTestConst {
    String TEST_ONE_CONTEXTA = "context1a";
    String TEST_ONE_CONTEXTB = "context1b";
    String TEST_ONE_NAME = "name-one";
    String TEST_ONE_VALUEA = "value-onea";
    String TEST_ONE_VALUEB = "value-oneb";

    String TEST_TWO_CONTEXT = "context2";
    String TEST_TWO_NAME = "name-two";
    String TEST_TWO_VALUE = "value-two";

    String BOGUS_NAME = "bogus"; // this setting does not exist
  }

  // =======================================================================================
  // SettingTestBuilder
  // =======================================================================================

  public Setting createTestSettingOneA() {
    Setting result = new Setting();

    result.setContext(SettingTestConst.TEST_ONE_CONTEXTA);
    result.setDescription("This is a test description");
    result.setEditable(true);
    result.setName(SettingTestConst.TEST_ONE_NAME);
    result.setType(Types.INPUT_TEXT);
    result.setValue(SettingTestConst.TEST_ONE_VALUEA);

    return result;
  }

  public Setting createTestSettingTwo() {
    Setting result = new Setting();

    result.setContext(SettingTestConst.TEST_TWO_CONTEXT);
    result.setDescription("This is a test description too");
    result.setEditable(true);
    result.setName(SettingTestConst.TEST_TWO_NAME);
    result.setType(Types.INPUT_TEXT_AREA);
    result.setValue(SettingTestConst.TEST_TWO_VALUE);

    return result;
  }

  public void setupComplexSetting() {
    // setup multiple settings
    for (int i = 1; i <= 10; i++) {
      saveOrUpdateSetting(SettingTestConst.TEST_ONE_CONTEXTA, getComplexNameOne(i), getComplexValueOne(i));
    }

    for (int i = 1; i <= 10; i++) {
      saveOrUpdateSetting(SettingTestConst.TEST_ONE_CONTEXTB, getComplexNameOne(i), getComplexValueOne(i));
    }

    for (int i = 1; i <= 10; i++) {
      saveOrUpdateSetting(SettingTestConst.TEST_TWO_CONTEXT, getComplexNameTwo(i), getComplexValueTwo(i));
    }
  }

  public String getComplexNameOne(int i) {
    return SettingTestConst.TEST_ONE_NAME + "_" + i;
  }

  public String getComplexValueOne(int i) {
    return SettingTestConst.TEST_ONE_VALUEA + "_" + i;
  }

  public String getComplexNameTwo(int i) {
    return SettingTestConst.TEST_TWO_NAME + "_" + i;
  }

  public String getComplexValueTwo(int i) {
    return SettingTestConst.TEST_TWO_VALUE + "_" + i;
  }

  public Setting saveOrUpdateSetting(final String context, final String name, final String value) {
    Setting testSetting = new Setting();

    testSetting.setContext(context);
    testSetting.setName(name);
    testSetting.setValue(value);

    return saveOrUpdateSetting(testSetting);
  }

  public Setting saveOrUpdateSetting(Setting setting) {
    SettingDAO dao = DAOLocator.locate(SettingDAO.class);

    // create it
    dao.saveOrUpdate(setting);

    return setting;
  }

}
