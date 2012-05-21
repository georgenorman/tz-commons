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
package com.thruzero.domain.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.thruzero.common.core.support.StrBuilderExt;
import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.model.Setting;

/**
 * An implementation of SettingDAO that uses Hibernate for storage.
 *
 * @author George Norman
 */
public class HibernateSettingDAO extends HibernateGenericDAO<Setting> implements SettingDAO {

  protected HibernateSettingDAO() {
    super(Setting.class);
  }

  @Override
  public String getSettingValue( String context, String name ) {
    return getSettingValue(context, name, null);
  }

  @Override
  public String getSettingValue( String context, String name, String defaultValue ) {
    Setting result = getSetting(context, name);

    return result == null ? defaultValue : result.getValue();
  }

  @Override
  public Setting getSetting( String context, String name ) {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(200);
    hql.append("FROM Setting setting ");
    hql.append("  WHERE setting.context = :context ");
    hql.append("    AND setting.name = :name ");

    Query hqlQuery = session.createQuery(hql.toString());
    hqlQuery.setString("context", context);
    hqlQuery.setString("name", name);

    Setting result = (Setting)hqlQuery.uniqueResult();

    return result;
  }

  @Override
  public List<? extends Setting> getSettings( String context ) {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(200);
    hql.append("FROM Setting setting ");
    hql.append("  WHERE setting.context = :context ");

    Query hqlQuery = session.createQuery(hql.toString());
    hqlQuery.setString("context", context);

    @SuppressWarnings("unchecked")
    List<? extends Setting> result = hqlQuery.list();

    return result;
  }

}
