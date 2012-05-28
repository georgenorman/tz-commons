/*
 *   Copyright 2009-2012 George Norman
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
package com.thruzero.auth.service.impl;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.model.User;
import com.thruzero.auth.service.UserService;
import com.thruzero.auth.utils.AuthenticationUtils;
import com.thruzero.common.core.support.SimpleInfo;
import com.thruzero.domain.locator.DAOLocator;

/**
 * An Implementation of the UserService interface, that uses the currently configured
 * instance of UserDAO (JPA, Hibernate or DSC based DAO).
 *
 * @author George Norman
 */
public class GenericUserService implements UserService {
  private final UserDAO userDAO = DAOLocator.locate(UserDAO.class);

  @Override
  public User getUserByLoginId(final String loginId) {
    User result = userDAO.getUserByLoginId(loginId);

    return result;
  }

  @Override
  public void handleLastLoginDate(final User user) {
//    try {
//      user.setLastLoginDate(new Date());
//
//      updateUser(user);
//    } catch (Exception e) {
//      AuditTrailLoggerUtils.error(AuditContext.AUTHENTICATION, "Unable to set the last login date for User (" + user.getPrimaryEmail() + "): " + e.getMessage());
//    }
  }

  @Override
  public void handleBadLoginAttempt(final User user) {
    try {
      AuthenticationUtils.handleBadLoginAttempt(user);

      updateUser(user);
    } catch (Exception e) {
      //AuditTrailLoggerUtils.error(AuditContext.AUTHENTICATION, "Unable to handle Bad Login Attempt for User (" + user.getPrimaryEmail() + "): " + e.getMessage());
    }
  }

  @Override
  public void saveUser(final User user) {
    userDAO.saveOrUpdate(user);
  }

  @Override
  public void updateUser(final User user) {
    userDAO.update(user);
  }

  @Override
  public void deleteUser(User user) {
    userDAO.delete(user);
  }

  /** return the DAO used by this service (used in unit tests to assert proper configuration setup). */
  public String getUserDAOClassName() {
    return userDAO.getClass().getName();
  }

  @Override
  public SimpleInfo getSimpleInfo() {
    return SimpleInfo.createSimpleInfo(this, userDAO);
  }

}
