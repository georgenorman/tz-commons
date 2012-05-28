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
package com.thruzero.auth.test.support.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.model.User;
import com.thruzero.auth.service.UserService;
import com.thruzero.auth.test.support.SimpleUserTestBuilder;
import com.thruzero.auth.test.support.SimpleUserTestBuilder.UserTestConst;
import com.thruzero.common.core.support.SimpleInfo;
import com.thruzero.domain.dao.GenericDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.service.impl.AbstractCrudService;

/**
 * Shared test cases used by the various implementations of the UserService (e.g., JPA, Hibernate, DSC).
 *
 * @author George Norman
 */
public abstract class AbstractUserServiceTestHelper {
  SimpleUserTestBuilder simpleUserTestBuilder = new SimpleUserTestBuilder();

  // -----------------------------------------------
  // MockUserService
  // -----------------------------------------------

  /** Exists solely to test the UserDAO implementation. */
  public static class MockUserService extends AbstractCrudService<User> implements UserService { // TODO-p1(george) not implemented
    private UserDAO userDAO = DAOLocator.locate(UserDAO.class);

    @Override
    public User getUserByLoginId(String loginId) {
      // TODO-p2(george) Auto-generated method stub
      return null;
    }

    @Override
    public void handleLastLoginDate(User user) {
      // TODO-p2(george) Auto-generated method stub

    }

    @Override
    public void handleBadLoginAttempt(User user) {
      // TODO-p2(george) Auto-generated method stub

    }

    @Override
    public void saveUser(User user) {
      userDAO.save(user);
    }

    @Override
    public void updateUser(User user) {
      userDAO.update(user);

    }

    @Override
    public void deleteUser(User user) {
      // TODO-p2(george) Auto-generated method stub

    }

    @Override
    protected GenericDAO<User> getDAO() {
      return userDAO;
    }

    @Override
    public SimpleInfo getSimpleInfo() {
      return SimpleInfo.createSimpleInfo(this, userDAO);
    }
  }

  // =====================================================================
  // AbstractUserServiceTestHelper
  // =====================================================================

  public void doTestGetNonExistantUser(UserService userService) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User user = userService.getUserByLoginId(UserTestConst.BOGUS_LOGIN_ID);
          assertNull(user);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestCreateNewUser(UserService userService) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User persistedUser = userService.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser == null); // make sure that user doesn't already exist
        }
          break;
        case 2: {
          User newUser1 = simpleUserTestBuilder.createTestUserOneA();
          userService.saveUser(newUser1);
        }
          break;
        case 3: {
          User persistedUser = userService.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertEquals(simpleUserTestBuilder.createTestUserOneA(), persistedUser);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestDeletePersistedUser(UserService userService) {
    User persistedUser = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User newUser1 = simpleUserTestBuilder.createTestUserOneA();
          userService.saveUser(newUser1);
        }
          break;
        case 2: {
          persistedUser = userService.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser != null);
        }
          break;
        case 3: {
          userService.deleteUser(persistedUser);
        }
          break;
        case 4: {
          persistedUser = userService.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser == null);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestUpdatePersistedUser(UserService userService) {
    User persistedUser = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User newUser1 = simpleUserTestBuilder.createTestUserOneA();
          userService.saveUser(newUser1);
        }
          break;
        case 2: {
          persistedUser = userService.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser != null);
        }
          break;
        case 3: {
          persistedUser = userService.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(StringUtils.isEmpty(persistedUser.getTemporaryPassword()));
          persistedUser.setTemporaryPassword(UserTestConst.TEST_ONE_TEMP_PW);
          userService.updateUser(persistedUser);
        }
          break;
        case 4: {
          persistedUser = userService.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser != null);
          assertEquals(UserTestConst.TEST_ONE_TEMP_PW, persistedUser.getTemporaryPassword());
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
