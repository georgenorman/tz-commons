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
package com.thruzero.auth.test.support.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.model.User;
import com.thruzero.auth.test.support.SimpleUserTestBuilder;
import com.thruzero.auth.test.support.SimpleUserTestBuilder.UserTestConst;

/**
 * Shared test cases used by the various implementations of the UserDAO (e.g., JPA, Hibernate, DSC).
 *
 * @author George Norman
 */
public abstract class AbstractUserDAOTestHelper {
  private SimpleUserTestBuilder simpleUserTestBuilder = new SimpleUserTestBuilder();

  public void doTestGetNonExistantUser(UserDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User user = dao.getUserByLoginId(UserTestConst.BOGUS_LOGIN_ID);
          assertNull(user);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestCreateNewUser(UserDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser == null); // make sure that user doesn't already exist
        }
          break;
        case 2: {
          User newUser1 = simpleUserTestBuilder.createTestUserOneA();
          dao.save(newUser1);
        }
          break;
        case 3: {
          User persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertEquals(simpleUserTestBuilder.createTestUserOneA(), persistedUser);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestDeletePersistedUser(UserDAO dao) {
    User persistedUser = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User newUser1 = simpleUserTestBuilder.createTestUserOneA();
          dao.save(newUser1);
        }
          break;
        case 2: {
          persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser != null);
        }
          break;
        case 3: {
          dao.delete(persistedUser);
        }
          break;
        case 4: {
          persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser == null);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestUpdatePersistedUser(UserDAO dao) {
    User persistedUser = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User newUser1 = simpleUserTestBuilder.createTestUserOneA();
          dao.save(newUser1);
        }
          break;
        case 2: {
          persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser != null);
        }
          break;
        case 3: {
          persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(StringUtils.isEmpty(persistedUser.getTemporaryPassword()));
          persistedUser.setTemporaryPassword(UserTestConst.TEST_ONE_TEMP_PW);
          dao.update(persistedUser);
        }
          break;
        case 4: {
          persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
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

  public void doTestSaveOrUpdatePersistedUser(UserDAO dao) {
    User persistedUser = null;

    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          User newUser1 = simpleUserTestBuilder.createTestUserOneA();
          dao.saveOrUpdate(newUser1);
        }
          break;
        case 2: {
          persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(persistedUser != null);
        }
          break;
        case 3: {
          persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
          assertTrue(StringUtils.isEmpty(persistedUser.getTemporaryPassword()));
          persistedUser.setTemporaryPassword(UserTestConst.TEST_ONE_TEMP_PW);
          dao.saveOrUpdate(persistedUser);
        }
          break;
        case 4: {
          persistedUser = dao.getUserByLoginId(UserTestConst.TEST_ONE_LOGIN_ID);
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
