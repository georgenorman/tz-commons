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
package com.thruzero.domain.dsc.store;

import java.io.InputStream;
import java.util.List;

import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dao.GenericDAO.DAOException;

/**
 * An interface that defined basic CRUD operations within a single container; it doesn't manage sub-containers or parent
 * containers. For example, the FileDataStoreContainer can create a new file only within the single directory it
 * manages. Each container has its own DataStoreContainer (i.e., each parent container and sub-container is managed by a
 * separate instance of DataStoreContainer).
 *
 * @author George Norman
 */
public interface DataStoreContainer {

  // ------------------------------------------------
  // DataStoreEntity
  // ------------------------------------------------

  /** An instance represents the data to be read from or written to a particular DataStoreContainer. */
  public static interface DataStoreEntity {
    /**
     * Return the EntityPath of this entity instance.
     */
    EntityPath getEntityPath();

    /**
     * Return an InputStream representing the "flattened" data for this entity. After reading the data from the
     * InputStream, you must close it. A new InputStream will be created each time this method is called.
     */
    InputStream getData();
  }

  // ============================================================
  // DataStoreContainer
  // ============================================================

  /**
   * Retrieves the DataStoreEntity with the given {@code entityName}, from the container managed by this
   * {@code DataStoreContainer}. Returns null if the entity does not exist. The DataStoreEntity contains an InputStream
   * to the actual data, so in most cases, the data will not actually be fetched until the stream is requested and read
   * (e.g., the FileDataStoreEntity doesn't create the InputStream until getData() is called).
   */
  DataStoreEntity readEntity(String entityName);

  /**
   * Return the list of {@code DataStoreEntity} instances managed by this {@code DataStoreContainer}. If recursive is
   * true, then return all of the {@code DataStoreEntity} instances managed by this {@code DataStoreContainer} as well
   * as all of the child {@code DataStoreContainer}'s.
   */
  List<? extends DataStoreEntity> getAllEntities(boolean recursive);

  List<EntityPath> getAllEntityPaths(boolean recursive);

  /**
   * If an entity with the given {@code entityName} does not exist, in the container managed by this
   * {@code DataStoreContainer}, then create it and write the given {@code dataEntity} to it; if it already exists, then
   * overwrite the existing data with the given {@code dataEntity}.
   */
  void saveOrUpdateEntity(String entityName, DataStoreEntity dataStoreEntity);

  /**
   * Overwrite the existing entity, with the given {@code entityName} and in the container managed by this
   * {@code DataStoreContainer}, with the the given {@code dataEntity}.
   *
   * @throws DAOException if nonexistent.
   */
  void updateEntity(String entityName, DataStoreEntity dataStoreEntity);

  /**
   * Create a new node with the given {@code entityName}, in the container managed by this {@code DataStoreContainer},
   * and write the given {@code dataEntity} to it.
   *
   * @throws DAOException if file already exists or could not be created.
   */
  void createNewEntity(String entityName);

  /**
   * Deletes the node with the given {@code entityName}, from the container managed by this {@code DataStoreContainer}.
   *
   * @throws DAOException if data node is a container or could not be deleted.
   */
  void deleteEntity(String entityName);

  /**
   * Return true if the named node exists in the container managed by this {@code DataStoreContainer}.
   */
  boolean isExistingEntity(String entityName);

  // validation and debug support functions /////////////////////////////////////////////////////////////

  /**
   * Validate the DataStoreContainer instance - used after initialization, to ensure the base store path exists, etc.
   */
  void validate();

  /**
   * Return information about the path for the given entityName, for use in tracking down where the NodeManager was
   * expecting a particular node (e.g., absolute path to the node).
   */
  String getDebugPathInfo(String entityName);

}
