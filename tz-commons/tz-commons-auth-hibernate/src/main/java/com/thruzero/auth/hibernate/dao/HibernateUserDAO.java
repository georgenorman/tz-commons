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
package com.thruzero.auth.hibernate.dao;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.Session;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.auth.model.User;
import com.thruzero.auth.model.UserPermission;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.support.StrBuilderExt;
import com.thruzero.domain.hibernate.dao.HibernateGenericDAO;

/**
 * An implementation of UserDAO that uses Hibernate as storage.
 *
 * @author George Norman
 */
public class HibernateUserDAO extends HibernateGenericDAO<User> implements UserDAO {
  private String userClass;

  public HibernateUserDAO() {
    super(User.class);

    userClass = ConfigLocator.locate().getValue(UserDAOConfigKeys.SECTION_NAME, UserDAOConfigKeys.USER_CLASS);

    if (StringUtils.isEmpty(userClass)) {
      throw new DAOException("* ERROR: USER_CLASS for " + HibernateUserDAO.class.getSimpleName() + " is empty.\nThe section named '" + UserDAOConfigKeys.SECTION_NAME +
          " ' did not contain the key: " + UserDAOConfigKeys.USER_CLASS + "' (the fully qualified name of the User subclass representing the user).");
    }
  }

  @Override
  public User getUserByLoginId(String loginId) {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM " + userClass + " a ");
    hql.append("  WHERE a.loginId = :loginId ");

    Query hqlQuery = session.createQuery(hql.toString());
    hqlQuery.setString("loginId", loginId);

    User result = (User)hqlQuery.uniqueResult();

    return result;
  }

  @Override
  public void save(User domainObject) {
    Session session = getCurrentSession();
    session.save(domainObject.getDetails());
    if (domainObject.getPermissions() != null) {
      for (UserPermission permission : domainObject.getPermissions()) {
        session.save(permission);
      }
    }

    super.save(domainObject);
  }

  @Override
  public void saveOrUpdate(User domainObject) {
    Session session = getCurrentSession();
    session.saveOrUpdate(domainObject.getDetails());
    if (domainObject.getPermissions() != null) {
      for (UserPermission permission : domainObject.getPermissions()) {
        session.saveOrUpdate(permission);
      }
    }

    super.save(domainObject);
  }

  @Override
  public void update(User domainObject) {
    Session session = getCurrentSession();
    session.saveOrUpdate(domainObject.getDetails());
    if (domainObject.getPermissions() != null) {
      for (UserPermission permission : domainObject.getPermissions()) {
        session.saveOrUpdate(permission);
      }
    }

    super.save(domainObject);
  }

}
