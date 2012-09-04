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

import java.io.Serializable;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.thruzero.common.core.locator.TransactionMgrLocator;
import com.thruzero.common.core.support.SimpleInfo;
import com.thruzero.common.core.support.StrBuilderExt;
import com.thruzero.common.core.transaction.DatabaseTransactionMgr;
import com.thruzero.common.core.transaction.TransactionMgr;
import com.thruzero.domain.dao.GenericDAO;
import com.thruzero.domain.store.Persistent;

/**
 * Base class for all Hibernate-based DAOs. It provides basic functionality for CRUD operations.
 * <p/>
 * Note: The table name is the simple name of the concrete Persistent type.
 *
 * @author George Norman
 * @param <T>
 */
public abstract class HibernateGenericDAO<T extends Persistent> implements GenericDAO<T> {
  private Class<? extends T> type;
  private String tableName;

  private DatabaseTransactionMgr transactionMgr = (DatabaseTransactionMgr)TransactionMgrLocator.locate(TransactionMgr.class);

  protected HibernateGenericDAO(Class<? extends T> type) {
    this.type = type;
    this.tableName = type.getSimpleName();
  }

  protected Session getCurrentSession() {
    // Use getCurrentSession(), so it's automatically bound to the current thread.
    Session session = (Session)transactionMgr.getCurrentPersistenceManager();

    return session;
  }

  @Override
  public T getByKey(Serializable primaryKey) {
    Session session = getCurrentSession();

    @SuppressWarnings("unchecked")
    T result = (T)session.load(type, primaryKey);

    return result;
  }

  public T queryByKey(Serializable primaryKey) {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM ", tableName, " o ");
    hql.append("  WHERE o.id = :primaryKey ");

    Query hqlQuery = session.createQuery(hql.toString());
    hqlQuery.setParameter("primaryKey", primaryKey);

    @SuppressWarnings("unchecked")
    T result = (T)hqlQuery.uniqueResult();

    return result;
  }

  @Override
  public List<? extends T> getAll() {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM ", tableName, " o ");

    Query hqlQuery = session.createQuery(hql.toString());

    @SuppressWarnings("unchecked")
    List<? extends T> result = hqlQuery.list();

    return result;
  }

  @Override
  public boolean isExistingEntity(Serializable primaryKey) {
    Session session = getCurrentSession();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("SELECT COUNT (entity) FROM ", tableName, " entity");
    hql.append("  WHERE entity.id = :primaryKey ");

    Query hqlQuery = session.createQuery(hql.toString());
    hqlQuery.setParameter("primaryKey", primaryKey);

    Long result = (Long) hqlQuery.uniqueResult();

    return result != null && result > 0;
  }

  /**
   * make transient objects persistent. save() does guarantee to return an identifier. If an INSERT has to be executed
   * to get the identifier ( e.g. "identity" generator, not "sequence"), this INSERT happens immediately, no matter if
   * you are inside or outside of a transaction.
   */
  @Override
  public void save(T domainObject) {
    Session session = getCurrentSession();

    doSave(session, domainObject);

    session.save(domainObject);
    transactionMgr.setCommitRequested(true);
  }

  protected void doSave(Session session, T domainObject) {

  }

  /**
   * Consider using Transitive persistence instead of calling this (see hibernate.cfg.xml for details).
   *
   * either saves a transient instance by generating a new identifier or updates/reattaches the detached instances
   * associated with its current identifier.
   *
   * Note: as long as you are not trying to use instances from one session in another new session, you should not need
   * to use update(), saveOrUpdate(), or merge().
   *
   * @param domainObject
   */
  @Override
  public void saveOrUpdate(T domainObject) {
    Session session = getCurrentSession();

    doSaveOrUpdate(session, domainObject);

    session.saveOrUpdate(domainObject);
  }

  protected void doSaveOrUpdate(Session session, T domainObject) {

  }

  /**
   * Consider using Transitive persistence instead of calling this (see hibernate.cfg.xml for details).
   *
   * If the Persistent object has already been loaded by the current Session, then an Exception will be thrown.
   *
   * Use update() if you are sure that the session does not contain an already persistent instance with the same
   * identifier, and merge() if you want to merge your modifications at any time without consideration of the state of
   * the session.
   *
   * Note: as long as you are not trying to use instances from one session in another new session, you should not need
   * to use update(), saveOrUpdate(), or merge().
   *
   * @param domainObject
   */
  @Override
  public void update(T domainObject) {
    Session session = getCurrentSession();

    doUpdate(session, domainObject);

    session.update(domainObject);
    transactionMgr.setCommitRequested(true);
  }

  protected void doUpdate(Session session, T domainObject) {

  }

  @Override
  public void delete(T domainObject) {
    Session session = getCurrentSession();

    session.delete(domainObject);
    transactionMgr.setCommitRequested(true);
  }

  public void refresh(T domainObject) {
    Session session = getCurrentSession();

    doRefresh(session, domainObject);

    session.refresh(domainObject);
  }

  protected void doRefresh(Session session, T domainObject) {

  }

  @Override
  public SimpleInfo getSimpleInfo() {
    return SimpleInfo.createSimpleInfo(this);
  }

}
