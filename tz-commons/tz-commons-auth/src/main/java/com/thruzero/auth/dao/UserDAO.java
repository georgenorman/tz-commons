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
package com.thruzero.auth.dao;

import com.thruzero.auth.model.User;
import com.thruzero.common.core.bookmarks.ConfigKeysBookmark;
import com.thruzero.common.core.config.Config.ConfigKeys;
import com.thruzero.domain.dao.GenericDAO;

/**
 * A DAO that manages operations specific to the User Domain Object.
 *
 * @author George Norman
 */
public interface UserDAO extends GenericDAO<User> {

  // ------------------------------------------------
  // UserDAOConfigKeys
  // ------------------------------------------------

  /**
   * Config keys defined for UserDAO and are defined inside of the config file section named by
   * UserDAO.class.getName(): "com.thruzero.auth.dao.UserDAO".
   */
  @ConfigKeysBookmark(comment = "Config keys for the UserDAO.")
  public interface UserDAOConfigKeys extends ConfigKeys { // Removed static, since it's redundant: see http://stackoverflow.com/questions/71625/
    String SECTION_NAME = UserDAO.class.getName();

    /** The fully qualified concrete class name for the User instance to use. */
    String USER_CLASS = "userClass";
  }

  // ============================================================
  // UserDAO
  // ============================================================

  User getUserByLoginId(String loginId);

}
