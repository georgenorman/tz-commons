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
package com.thruzero.auth.test.support;

import java.util.HashSet;
import java.util.Set;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.model.User;
import com.thruzero.auth.model.User.UserStatus;
import com.thruzero.auth.model.UserPermission;
import com.thruzero.auth.model.impl.BasicUser;
import com.thruzero.auth.model.impl.BasicUserPermission;
import com.thruzero.domain.locator.DAOLocator;

/**
 * Builds test User instances for testing.
 *
 * @author George Norman
 */
public class SimpleUserTestBuilder {

  // -----------------------------------------------------
  // UserTestConst
  // -----------------------------------------------------

  public static interface UserTestConst {
    String TEST_ONE_LOGIN_ID = "user1@thruzero.com";
    String TEST_ONE_PASSWORD = "p@ssw0rd0ne";
    String TEST_ONE_FIRST_NAME = "FirstOne";
    String TEST_ONE_LAST_NAME = "LastOne";
    String TEST_ONE_PRIMARY_EMAIL = "primary1@email.com";
    String TEST_ONE_STATUS = UserStatus.ACTIVE;

    String TEST_TWO_LOGIN_ID = "user2@thruzero.com";
    String TEST_TWO_PASSWORD = "p@ssw0rdTw0";
    String TEST_TWO_FIRST_NAME = "FirstTwo";
    String TEST_TWO_LAST_NAME = "LastTwo";
    String TEST_TWO_PRIMARY_EMAIL = "primar2y@email.com";
    String TEST_TWO_STATUS = UserStatus.IN_ACTIVE;

    String BOGUS_LOGIN_ID = "bogus"; // this user does not exist

    String TEST_ONE_TEMP_PW = "temp0r@rypw1";
  }

  // =======================================================================================
  // UserTestBuilder
  // =======================================================================================

  public User createTestUserOneA() {
    User result = new BasicUser();

    result.setLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
    result.setPassword(UserTestConst.TEST_ONE_PASSWORD);
    result.setStatus(UserTestConst.TEST_ONE_STATUS);
    result.getDetails().setFirstName(UserTestConst.TEST_ONE_FIRST_NAME);
    result.getDetails().setLastName(UserTestConst.TEST_ONE_LAST_NAME);
    result.getDetails().setPrimaryEmail(UserTestConst.TEST_ONE_PRIMARY_EMAIL);

    result.setPermissions(createPermissionsUserOneA());

    return result;
  }

  public Set<UserPermission> createPermissionsUserOneA() {
    Set<UserPermission> result = new HashSet<UserPermission>();

    result.add(new BasicUserPermission("demoSecure2", "view,edit,create", "User can view, edit and create items on the '/apps/demo/secure/secure2.jsf' page."));

    return result;
  }

  public User createTestUserTwo() {
    User result = new BasicUser();

    result.setLoginId(UserTestConst.TEST_TWO_LOGIN_ID);
    result.setPassword(UserTestConst.TEST_TWO_PASSWORD);
    result.setStatus(UserTestConst.TEST_TWO_STATUS);
    result.getDetails().setFirstName(UserTestConst.TEST_TWO_FIRST_NAME);
    result.getDetails().setLastName(UserTestConst.TEST_TWO_LAST_NAME);
    result.getDetails().setPrimaryEmail(UserTestConst.TEST_TWO_PRIMARY_EMAIL);

    return result;
  }

  public User saveOrUpdateUser(final String loginId) {
    User testUser = createTestUserOneA();

    testUser.setLoginId(loginId);

    return saveOrUpdateUser(testUser);
  }

  public User saveOrUpdateUser(User testUser) {
    UserDAO dao = DAOLocator.locate(UserDAO.class);

    // create it
    dao.saveOrUpdate(testUser);

    return testUser;
  }

}
