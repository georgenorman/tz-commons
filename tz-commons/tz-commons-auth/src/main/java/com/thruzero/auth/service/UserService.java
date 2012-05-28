/*
 *   Copyright 2010-2012 George Norman
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
package com.thruzero.auth.service;

import com.thruzero.auth.model.User;
import com.thruzero.common.core.service.Service;
import com.thruzero.common.core.support.SimpleInfoProvider;

/**
 * Manages registered Users and is used for authentication and authorization.
 *
 * @author George Norman
 */
public interface UserService extends Service, SimpleInfoProvider {

  /** Return the User with the given loginId, or null if not found. */
  User getUserByLoginId(String loginId);

  /** Set the last login Date for the given user, as now, and persist. */
  void handleLastLoginDate(User user);

  /** increment the invalid login count for the given user and persist. */
  void handleBadLoginAttempt(User user);

  void saveUser(User user);

  void updateUser(User user);

  void deleteUser(User user); // TODO-p0(george) hmmm, may want to retire the user, so history is preserved.

}
