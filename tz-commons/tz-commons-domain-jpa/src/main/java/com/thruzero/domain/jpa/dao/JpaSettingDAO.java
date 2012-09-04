/*
 *   Copyright 2011-2012 George Norman
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
package com.thruzero.domain.jpa.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.thruzero.common.core.support.StrBuilderExt;
import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.jpa.utils.JpaUtils;
import com.thruzero.domain.model.Setting;

/**
 * An implementation of SettingDAO that uses JPA for storage.
 *
 * @author George Norman
 */
public final class JpaSettingDAO extends JpaGenericDAO<Setting> implements SettingDAO {

  /**
   * Use {@link com.thruzero.domain.locator.DAOLocator DAOLocator} to access a particular DAO.
   */
  private JpaSettingDAO() {
    super(Setting.class);
  }

  @Override
  public boolean isExistingSetting(String context, String name) {
    EntityManager entityManager = getCurrentPersistenceManager();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("SELECT COUNT (setting) FROM Setting setting");
    hql.append("  WHERE setting.context = :context ");
    hql.append("    AND setting.name = :name ");

    Query hqlQuery = entityManager.createQuery(hql.toString());
    hqlQuery.setParameter("context", context);
    hqlQuery.setParameter("name", name);

    Long result = (Long)hqlQuery.getSingleResult();

    return result != null && result > 0;
  }

  @Override
  public String getSettingValue( String context, String name ) {
    return getSettingValue(context, name, null);
  }

  @Override
  public String getSettingValue(String context, String name, String defaultValue) {
    Setting result = getSetting(context, name);

    return result == null ? defaultValue : result.getValue();
  }

  @Override
  public Setting getSetting( String context, String name ) {
    EntityManager entityManager = getCurrentPersistenceManager();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM Setting setting ");
    hql.append("  WHERE setting.context = :context ");
    hql.append("    AND setting.name = :name ");

    Query hqlQuery = entityManager.createQuery(hql.toString());
    hqlQuery.setParameter("context", context);
    hqlQuery.setParameter("name", name);

    Setting result = JpaUtils.getSingleResultHack(hqlQuery);

    return result;
  }

  @Override
  public List<? extends Setting> getSettings( String context ) {
    EntityManager entityManager = getCurrentPersistenceManager();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM Setting setting ");
    hql.append("  WHERE setting.context = :context ");

    Query hqlQuery = entityManager.createQuery(hql.toString());
    hqlQuery.setParameter("context", context);

    @SuppressWarnings("unchecked")
    List<? extends Setting> result = hqlQuery.getResultList();

    return result;
  }

}
