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
package com.thruzero.domain.hibernate.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.StrBuilderExt;
import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.model.TextEnvelope;

/**
 * An implementation of TextEnvelopeDAO that uses Hibernate for storage.
 *
 * @author George Norman
 */
public final class HibernateTextEnvelopeDAO extends HibernateGenericDAO<TextEnvelope> implements TextEnvelopeDAO {

  /**
   * Use {@link com.thruzero.domain.locator.DAOLocator DAOLocator} to access a particular DAO.
   */
  private HibernateTextEnvelopeDAO() {
    super(TextEnvelope.class);
  }

  @Override
  public boolean isExistingTextEnvelope(EntityPath entityPath) {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("SELECT COUNT (textEnvelope) FROM TextEnvelope textEnvelope");
    hql.append("  WHERE textEnvelope.entityPath.containerPath = :containerPath ");
    hql.append("    AND textEnvelope.entityPath.entityName = :entityName ");

    Query hqlQuery = session.createQuery(hql.toString());
    hqlQuery.setString("containerPath", entityPath.getContainerPath().getPath());
    hqlQuery.setString("entityName", entityPath.getEntityName());

    Long result = (Long)hqlQuery.uniqueResult();

    return result != null && result > 0;
  }

  @Override
  public List<? extends TextEnvelope> getTextEnvelopes(ContainerPath containerPath, boolean recursive) {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append(" FROM TextEnvelope textEnvelope ");
    hql.append("  WHERE textEnvelope.containerPath = :containerPath ");

    Query hqlQuery = session.createQuery(hql.toString());
    hqlQuery.setString("containerPath", containerPath.getPath());

    @SuppressWarnings("unchecked") // Hibernate isn't generic
    List<? extends TextEnvelope> result = hqlQuery.list();

    return result;
  }

  @Override
  public List<EntityPath> getTextEnvelopePaths(ContainerPath containerPath, boolean recursive) {
    // TODO-p0(george) Auto-generated method stub
    return null;
  }

  @Override
  public TextEnvelope getTextEnvelope(EntityPath entityPath) {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append(" FROM TextEnvelope textEnvelope ");
    hql.append("  WHERE textEnvelope.entityPath.containerPath = :containerPath ");
    hql.append("    AND textEnvelope.entityPath.entityName = :entityName ");

    Query hqlQuery = session.createQuery(hql.toString());
    hqlQuery.setString("containerPath", entityPath.getContainerPath().getPath());
    hqlQuery.setString("entityName", entityPath.getEntityName());

    TextEnvelope result = (TextEnvelope)hqlQuery.uniqueResult();

    return result;
  }
}
