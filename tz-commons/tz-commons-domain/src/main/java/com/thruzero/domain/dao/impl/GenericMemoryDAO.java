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
package com.thruzero.domain.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import com.thruzero.common.core.support.KeyGen;
import com.thruzero.common.core.support.SimpleInfo;
import com.thruzero.domain.dao.GenericDAO;
import com.thruzero.domain.store.Persistent;

/**
 * A generic DAO that provides CRUD services for any Domain Object, and implemented using a MemoryStore, which
 * can be an in-memory Map, an HttpSession, etc.
 * <p/>
 * This DAO does not support transactions. It does not copy data from the given Domain Object to the memory
 * map (TODO-p3(george) although, it probably should). It's main purpose is for testing and demos.
 *
 * @author George Norman
 * @param <T> Type of Domain Object managed by this DAO.
 */
public class GenericMemoryDAO<T extends Persistent> implements GenericDAO<T> {
  private KeyGen<T> keyGen;
  private MemoryStore<T> memoryStore;

  // ------------------------------------------------------
  // MemoryStore
  // ------------------------------------------------------

  public static interface MemoryStore<T> {

    void persist(T entity);

    T find(Serializable primaryKey);

    Collection<T> getResultList();

    boolean contains(T entity);

    void remove(T entity);

    /** Clear the Map used to store the Domain Objects. */
    void clear();
  }

  // ============================================================================
  // GenericMemoryDAO
  // ============================================================================

  public GenericMemoryDAO(MemoryStore<T> memoryStore) {
    this.memoryStore = memoryStore;
  }

  @Override
  public void save(T domainObject) {
    if (domainObject.getId() != null) {
      throw new DAOException("ERROR: Can't save a Domain Object that already has an ID. Use update function instead. ID is: '" + domainObject.getId().toString() + "'.");
    }

    Serializable primaryKey = getKeyGen().createKey(domainObject);

    domainObject.setId(primaryKey);
    memoryStore.persist(domainObject);
  }

  @Override
  public void saveOrUpdate(T domainObject) {
    if (domainObject != null) {
      if (domainObject.getId() == null) {
        save(domainObject);
      } else {
        update(domainObject);
      }
    }
  }

  @Override
  public void update(T domainObject) {
    if (domainObject != null) {
      if (domainObject.getId() == null) {
        throw new DAOException("ERROR: Can't update a Domain Object that doesn't have an ID.");
      }

      T existingDomainObject = memoryStore.find(domainObject.getId());

      if (existingDomainObject == null) {
        throw new DAOException("ERROR: Domain Object not found. ID is: " + domainObject.getId());
      }

      memoryStore.persist(domainObject);
    }
  }

  @Override
  public void delete(T domainObject) {
    if (domainObject != null) {
      if (domainObject.getId() == null) {
        throw new DAOException("ERROR: Can't delete a Domain Object with an empty ID.");
      }

      memoryStore.remove(domainObject);
    }
  }

  @Override
  public T getByKey(Serializable primaryKey) {
    T result = null;

    if (primaryKey != null) {
      result = memoryStore.find(primaryKey);
    }

    return result;
  }

  @Override
  public boolean isExistingEntity(Serializable primaryKey) {
    boolean result = false;

    if (primaryKey != null) {
      result = memoryStore.find(primaryKey) != null;
    }

    return result;
  }

  @Override
  public Collection<? extends T> getAll() {
    return new ArrayList<T>(memoryStore.getResultList()); // TODO-p1(george) JSF EL (using h:dataTable tag) can't use Collection returned by Map.
  }

  // Support ////////////////////////////////////////////////

  /**
   * Return the MemoryStore used to store the Domain Objects.
   */
  protected MemoryStore<T> getMemoryStore() {
    return memoryStore;
  }

  /** Clear the Map used to store the Domain Objects. */
  public void resetStore() {
    memoryStore.clear();
  }

  /** Return the key generator used to create primary keys for all Domain Objects saved by this DAO. */
  protected KeyGen<T> getKeyGen() {
    if (keyGen == null) {
      keyGen = createKeyGen();
    }
    return keyGen;
  }

  /** Create and return the default KeyGen. Override this to provide an alternate type. */
  protected KeyGen<T> createKeyGen() {
    return new KeyGen<T>();
  }

  @Override
  public SimpleInfo getSimpleInfo() {
    return SimpleInfo.createSimpleInfo(this);
  }

}
