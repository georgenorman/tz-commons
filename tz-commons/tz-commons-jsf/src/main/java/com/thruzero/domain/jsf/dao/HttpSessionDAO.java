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
package com.thruzero.domain.jsf.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.thruzero.common.jsf.utils.FacesUtils;
import com.thruzero.domain.dao.impl.GenericMemoryDAO;
import com.thruzero.domain.store.Persistent;

/**
 * A generic DAO that provides CRUD services for any Domain Object, and implemented using an instance of HttpMemoryStore,
 * which uses the current HttpSession to service each request. Hence, from a single HttpMemoryStore, each client appears to
 * have their own private data store.
 * <p/>
 * This DAO does not support transactions. It does not copy data from the given Domain Object to the memory
 * map (TODO-p2(george) although, it probably should). It's main purpose is for testing and demos.
 *
 * @author George Norman
 * @param <T> Type of Domain Object managed by this DAO.
 */
public abstract class HttpSessionDAO<T extends Persistent> extends GenericMemoryDAO<T> {

  // ------------------------------------------------------
  // HttpMemoryStore
  // ------------------------------------------------------

  public static class HttpMemoryStore<T extends Persistent> implements MemoryStore<T> {
    private Class<T> domainType;

    public HttpMemoryStore(Class<T> domainType) {
      this.domainType = domainType;
    }

    @Override
    public void persist(T entity) {
      getEntityMap().put(entity.getId(), entity);
    }

    @Override
    public T find(Serializable primaryKey) {
      return getEntityMap().get(primaryKey);
    }

    @Override
    public Collection<T> getResultList() {
      return getEntityMap().values();
    }

    @Override
    public boolean contains(T entity) {
      return getEntityMap().containsKey(entity.getId());
    }

    @Override
    public void remove(T entity) {
      getEntityMap().remove(entity.getId());
    }

    @Override
    public void clear() {
      getEntityMap().clear();
    }

    protected Map<Serializable, T> getEntityMap() {
      HttpSession session = FacesUtils.getSession(false);
      @SuppressWarnings("unchecked")
      Map<Serializable, T> result = (Map<Serializable, T>)session.getAttribute(domainType.getName());

      if (result == null) {
        result = createNewEntityMap();
        session.setAttribute(domainType.getName(), result);
      }

      return result;
    }

    protected Map<Serializable, T> createNewEntityMap() {
      Map<Serializable, T> result = new LinkedHashMap<Serializable, T>();

      return result;
    }
  }

  // ============================================================================
  // HttpSessionDAO
  // ============================================================================

  protected HttpSessionDAO(MemoryStore<T> memoryStore) {
    super(memoryStore);
  }

}
