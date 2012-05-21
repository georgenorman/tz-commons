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
package com.thruzero.domain.service.impl;

import java.io.Serializable;
import java.util.Collection;

import com.thruzero.common.core.service.Service;
import com.thruzero.domain.dao.GenericDAO;
import com.thruzero.domain.store.Persistent;

/**
 * Simple abstract Service implementation that's tied to a single DAO, exposing its CRUD operations.
 *
 * @author George Norman
 * @param <T> the type of Domain Object to be persisted.
 */
public abstract class AbstractCrudService<T extends Persistent> implements Service {

  /** Save the given Domain Object to the data store represented by the specialized service. */
  public void save(final T domainObject) {
    getDAO().save(domainObject);
  }

  /**
   * Save the given Domain Object, if it has never been persisted, or update the existing record in the data store
   * represented by the specialized service.
   */
  public void saveOrUpdate(final T domainObject) {
    getDAO().saveOrUpdate(domainObject);
  }

  /** Update the given Domain Object to the data store represented by the specialized service. */
  public void update(final T domainObject) {
    getDAO().update(domainObject);
  }

  /** Delete the given Domain Object from the data store represented by the specialized service. */
  public void delete(final T domainObject) {
    getDAO().delete(domainObject);
  }

  public T getByKey(final T domainObject) {
    return getByKey(domainObject.getId());
  }

  public T getByKey(final Serializable id) {
    return getDAO().getByKey(id);
  }

  /** Return all Domain Objects from the data store represented by the specialized service. */
  public Collection<? extends T> getAll() {
    return getDAO().getAll();
  }

  /** Return the DAO used by this service (subclasses implement this method to return the DAO required by the specialized service). */
  protected abstract GenericDAO<T> getDAO();

}
