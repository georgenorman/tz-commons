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
package com.thruzero.auth.jpa.dao;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.model.User;
import com.thruzero.auth.model.UserPermission;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.support.StrBuilderExt;
import com.thruzero.domain.jpa.dao.JpaGenericDAO;
import com.thruzero.domain.jpa.utils.JpaUtils;

/**
 * An implementation of UserDAO that uses JPA for storage.
 *
 * @author George Norman
 */
public class JpaUserDAO extends JpaGenericDAO<User> implements UserDAO {
  private String userClass;

  protected JpaUserDAO() {
    super(User.class);

    userClass = ConfigLocator.locate().getValue(UserDAOConfigKeys.SECTION_NAME, UserDAOConfigKeys.USER_CLASS);

    if (StringUtils.isEmpty(userClass)) {
      throw new DAOException("* ERROR: USER_CLASS for " + JpaUserDAO.class.getSimpleName() + " is empty.\nThe section named '" + UserDAOConfigKeys.SECTION_NAME +
          " ' did not contain the key: " + UserDAOConfigKeys.USER_CLASS + "' (the fully qualified name of the User subclass representing the user).");
    }
  }

  @Override
  public User getUserByLoginId(String loginId) {
    EntityManager entityManager = getCurrentPersistenceManager();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM " + userClass + " a ");
    hql.append("  WHERE a.loginId = :loginId ");

    Query hqlQuery = entityManager.createQuery(hql.toString());
    hqlQuery.setParameter("loginId", loginId);

    User result = JpaUtils.getSingleResultHack(hqlQuery);

    return result;
  }

  @Override
  public void save(User domainObject) {
    EntityManager persistenceManager = getCurrentPersistenceManager( );

    persistenceManager.persist(domainObject.getDetails());
    if (domainObject.getPermissions() != null) {
      for (UserPermission permission : domainObject.getPermissions()) {
        persistenceManager.persist(permission);
      }
    }

    super.save(domainObject);
  }
}
