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
package com.thruzero.domain.dao;

import java.io.Serializable;
import java.util.Collection;

import com.thruzero.common.core.support.SimpleInfoProvider;
import com.thruzero.common.core.utils.ExceptionUtilsExt;

/**
 * An interface that defines basic CRUD operations used by persistent Services. It essentially manages the translation
 * of an instance of a Domain Object into a record in the data store and back again. A {@code DAO} typically references
 * a single data store entity (e.g., the SETTINGS table).
 * <p>
 * {@code GenericDAO} is parameterized to a single type of Domain Object. A side effect being that it can only be used
 * to operate on the parameterized type (e.g., you won't be able to use the same DAO instance to load a Setting and a
 * Preference).
 *
 * @author George Norman
 * @param <T> The type of object managed by the DAO.
 */
public interface GenericDAO<T> extends DAO, SimpleInfoProvider {

  // ------------------------------------------------
  // DAOException
  // ------------------------------------------------

  /**
   * An exception which can be thrown by any implementation of {@code DAO}.
   */
  public static class DAOException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DAOException(final String message) {
      super(ExceptionUtilsExt.decorateMessageLevel1(message));
    }

    public DAOException(final String message, final Throwable cause) {
      super(ExceptionUtilsExt.decorateMessageLevel1(message), cause);
    }
  }

  // ============================================================
  // GenericDAO
  // ============================================================

  /** Save the new domainObject to the data store */
  void save(T domainObject);

  /**
   * Tests to see if the object is new or has been previously persisted and if the object is new, it creates a new
   * entity in the data store (e.g., inserts a new row) or if it's already in the data store, it will update the
   * existing entity.
   */
  void saveOrUpdate(T domainObject);

  /** Update the data store from the given domainObject. */
  void update(T domainObject);

  /** Delete the domainObject from the data store, if it exists. */
  void delete(T domainObject);

  /** Load and return the Domain Object from the data store, for the given primaryKey; returning null if not found. */
  T getByKey(Serializable primaryKey);

  /** Return true if resource exists for the given primaryKey. */
  boolean isExistingEntity(Serializable primaryKey);

  /** Return all of the domainObject's, of type T, from the data store */
  Collection<? extends T> getAll();

}
