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
import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.jpa.utils.JpaUtils;
import com.thruzero.domain.model.Preference;

/**
 * An implementation of PreferenceDAO that uses JPA for storage.
 *
 * @author George Norman
 */
public class JpaPreferenceDAO extends JpaGenericDAO<Preference> implements PreferenceDAO {

  protected JpaPreferenceDAO() {
    super(Preference.class);
  }

  @Override
  public String getPreferenceValue( String owner, String context, String name ) {
    return getPreferenceValue(owner, context, name, null);
  }

  @Override
  public String getPreferenceValue(String owner, String context, String name, String defaultValue) {
    Preference result = getPreference(owner, context, name);

    return result == null ? defaultValue : result.getValue();
  }

  @Override
  public Preference getPreference( String owner, String context, String name ) {
    EntityManager entityManager = getCurrentPersistenceManager();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM Preference preference ");
    hql.append("  WHERE preference.context = :context ");
    hql.append("    AND preference.name = :name ");
    hql.append("    AND preference.owner = :owner ");

    Query hqlQuery = entityManager.createQuery(hql.toString());
    hqlQuery.setParameter("context", context);
    hqlQuery.setParameter("name", name);
    hqlQuery.setParameter("owner", owner);

    Preference result = JpaUtils.getSingleResultHack(hqlQuery);

    return result;
  }

  @Override
  public List<? extends Preference> getPreferences(String owner, String context) {
    EntityManager entityManager = getCurrentPersistenceManager();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM Preference preference ");
    hql.append("  WHERE preference.context = :context ");
    hql.append("    AND preference.owner = :owner ");

    Query hqlQuery = entityManager.createQuery(hql.toString());
    hqlQuery.setParameter("context", context);
    hqlQuery.setParameter("owner", owner);

    @SuppressWarnings("unchecked")
    List<? extends Preference> result = hqlQuery.getResultList();

    return result;
  }

}
