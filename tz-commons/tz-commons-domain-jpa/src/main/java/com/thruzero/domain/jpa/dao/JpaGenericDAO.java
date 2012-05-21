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

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.thruzero.common.core.locator.TransactionMgrLocator;
import com.thruzero.common.core.support.StrBuilderExt;
import com.thruzero.common.core.transaction.DatabaseTransactionMgr;
import com.thruzero.common.core.transaction.TransactionMgr;
import com.thruzero.domain.dao.GenericDAO;
import com.thruzero.domain.jpa.utils.JpaUtils;
import com.thruzero.domain.store.Persistent;

/**
 * Base class for all JPA-based DAOs. It provides basic functionality for CRUD operations.
 *
 * @author George Norman
 * @param <T> Type of Domain Object managed by this DAO.
 */
public class JpaGenericDAO<T extends Persistent> implements GenericDAO<T> {
  private Class<? extends T> clazz;
  private DatabaseTransactionMgr transactionMgr = (DatabaseTransactionMgr)TransactionMgrLocator.locate(TransactionMgr.class);

  protected JpaGenericDAO(Class<? extends T> clazz) {
    this.clazz = clazz;
  }

  protected EntityManager getCurrentPersistenceManager( ) {
    EntityManager result = (EntityManager)transactionMgr.getCurrentPersistenceManager();

    return result;
  }

  public void refresh ( T domainObject ) {
    EntityManager persistenceManager = getCurrentPersistenceManager( );

    persistenceManager.refresh( domainObject );
  }

  @Override
  public T getByKey(Serializable primaryKey) {
    EntityManager persistenceManager = getCurrentPersistenceManager( );

    T result = persistenceManager.find( clazz, primaryKey );

    return result;
  }

  public T queryByKey(Serializable primaryKey) {
    EntityManager entityManager = getCurrentPersistenceManager();

    StrBuilderExt hql = new StrBuilderExt(100);
    hql.append("FROM " + clazz.getSimpleName() +" o ");
    hql.append("    WHERE o.id = :primaryKey ");

    Query hqlQuery = entityManager.createQuery(hql.toString());
    hqlQuery.setParameter("primaryKey", primaryKey);

    // type parameters of <T>T cannot be determined; no unique maximal instance exists for type variable T with upper bounds T,java.lang.Object
    // requires java 7 to compile without this fix: JpaUtils.getSingleResultHack => JpaUtils.<T>getSingleResultHack.
    T result = JpaUtils.<T>getSingleResultHack(hqlQuery); // http://stackoverflow.com/questions/314572/bug-in-eclipse-compiler-or-in-javac

    return result;
  }

  @Override
  public List<? extends T> getAll( ) {
    EntityManager entityManager = getCurrentPersistenceManager();
    String hql = "FROM " + clazz.getSimpleName() + " o ";

    Query hqlQuery = entityManager.createQuery(hql);

    @SuppressWarnings("unchecked")
    List<? extends T> result = hqlQuery.getResultList();

    return result;
  }

  @Override
  public void save(T domainObject) {
    EntityManager persistenceManager = getCurrentPersistenceManager( );

    persistenceManager.persist(domainObject);
    persistenceManager.flush( );
    transactionMgr.setCommitRequested(true);
  }

  @Override
  public void saveOrUpdate(T entity ) {
    EntityManager persistenceManager = getCurrentPersistenceManager( );

    if (persistenceManager.contains(entity)) {
      update( entity );
    } else{
      save( entity );
    }
  }

  @Override
  public void update(T domainObject ) {
    EntityManager persistenceManager = getCurrentPersistenceManager( );

    persistenceManager.flush( );
    transactionMgr.setCommitRequested(true);
  }

  @Override
  public void delete(T entity ) {
    EntityManager persistenceManager = getCurrentPersistenceManager( );

    if ( !persistenceManager.contains(entity) ) {
      entity = persistenceManager.merge(entity);
    }
    persistenceManager.remove(entity);
    persistenceManager.flush( );
    transactionMgr.setCommitRequested(true);
  }

  @Override
  public String getInfo() {
    return getClass().getSimpleName() + ".";
  }
}
