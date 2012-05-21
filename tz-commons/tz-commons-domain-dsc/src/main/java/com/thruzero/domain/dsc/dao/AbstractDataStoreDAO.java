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
package com.thruzero.domain.dsc.dao;

import java.io.Serializable;
import java.util.List;

import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.locator.Initializable;
import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.locator.LocatorUtils;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.KeyGen;
import com.thruzero.common.core.support.SimpleIdGenerator;
import com.thruzero.common.core.utils.ClassUtils;
import com.thruzero.common.core.utils.ClassUtils.ClassUtilsException;
import com.thruzero.domain.dao.GenericDAO;
import com.thruzero.domain.dsc.store.DataStoreContainer;
import com.thruzero.domain.dsc.store.DataStoreContainer.DataStoreEntity;
import com.thruzero.domain.dsc.store.DataStoreContainerFactory;
import com.thruzero.domain.store.BaseStorePath;
import com.thruzero.domain.store.ContainerPath;
import com.thruzero.domain.store.EntityPath;
import com.thruzero.domain.store.Persistent;

/**
 * An abstract base class for a DAO that handles CRUD operations for a transactionless data store.
 *
 * <p>
 * A {@link com.thruzero.domain.dsc.store.DataStoreContainerFactory DataStoreContainerFactory} is used to create
 * instances of {@code DataStoreContainer}. Each {@code DataStoreContainer} handles the basic CRUD operations for a
 * single container. For example, {@link com.thruzero.domain.fs.support.FileDataStoreContainer FileDataStoreContainer}
 * handles persistence for a single directory in the file system (the persistence of child directories are managed by
 * separate instances of {@code FileDataStoreContainer}).
 * <p>
 * A {@code DomainObjectTransformer} is used to handle the translation from a Domain Object to a data node and back
 * again. For example, the {@code XStreamDomainObjectTransformer} uses <a
 * href="http://xstream.codehaus.org/">XStream</a> to handle these translations.
 * <p>
 * The <i>baseStorePath</i> property is passed into the DAO by the locator, at initialization-time, and
 * represents the path to the BASE data store for a particular DAO. Each DAO has its own BASE data store. For
 * example, {@code DscSettingDAO} manages persistence in a directory named {@code DscSettingDAO}, while
 * {@code DscTextEnvelopeDAO} manages persistence in the directory named {@code DscTextEnvelopeDAO}. The BASE data store
 * is configurable for each DAO. Below is an example configuration for the {@code DscSettingDAO} (Using JFig as the
 * config provider using substitution: [config-section]{property-key}).
 *
 * <pre>
 *   &lt;section name=&quot;com.thruzero.domain.fs.dao.DscSettingDAO&quot;&gt;
 *     &lt;entry key=&quot;baseStorePath&quot; value=&quot;[com.thruzero.domain.dsc.dao.AbstractDataStoreDAO]{rootStorePath}/DscSettingDAO&quot; /&gt;
 *   &lt;/section&gt;
 * </pre>
 * <p>
 * An instance of {@link com.thruzero.domain.store.EntityPath EntityPath} represents a path to a
 * particular node in the ROOT data store. Hence, all paths represented by {@code EntityPath} are
 * <b>absolute</b>, from the perspective of the ROOT data store. For example, a particular {@code Preference} is
 * referenced by the absolute <i>data store</i> path of "/owner1@thruzero.com/context1a/name123.txt", but in the file
 * system, it's represented by the absolute <i>file system</i> path of
 * "/home/foo/bar/data-store/DscPreferenceDAO/owner1@thruzero.com/context1a/name123.txt".
 *
 * @author George Norman
 * @param <T> the type of Domain Object to be persisted.
 */
public abstract class AbstractDataStoreDAO<T extends Persistent> implements GenericDAO<T>, Initializable {
  private DataStoreContainerFactory dscFactory;

  private DomainObjectTransformer<T> domainObjectTransformer;
  private DataStoreKeyGen<T> keyGen;

  // ------------------------------------------------
  // DataStoreDAOInitParamKeys
  // ------------------------------------------------

  /**
   * Initialization parameter keys defined for {@code AbstractDataStoreDAO}.
   */
  @InitializationParameterKeysBookmark
  public interface DataStoreDAOInitParamKeys extends InitializableParameterKeys {
    /** The section to use when loading parameters (e.g., config file section, settings context, etc) */
    String SOURCE_SECTION = AbstractDataStoreDAO.class.getName();

    /** The parameter key that defines the BASE data store for a particular DAO: "base" (appended to the rooStorePath). */
    String BASE = "base";

    /**
     * The parameter key that defines the fully qualified class name of the DataStoreContainerFactory for this
     * particular DAO: "DataStoreContainerFactory" (e.g., FileDataStoreContainerFactory,
     * MockDscDataStoreContainerFactory).
     */
    String STORE_CONTAINER_FACTORY = DataStoreContainerFactory.class.getName();
  }

  // ------------------------------------------------
  // DomainObjectTransformer
  // ------------------------------------------------

  /** An interface that provides and API to flatten and resurrect Domain Objects on a stream. */
  public static interface DomainObjectTransformer<T> {
    /** Create a DataStoreEntity from the given domainObject (marshal). */
    DataStoreEntity flatten(T domainObject);

    /** Create a new Domain Object from the given DataStoreEntity (unmarshal). */
    T resurrect(EntityPath primaryKey, DataStoreEntity data);
  }

  // ------------------------------------------------------
  // DataStoreKeyGen
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying an instance of &lt;T&gt; in the store. */
  public static class DataStoreKeyGen<T> extends KeyGen<T> {
    /**
     * Create a default DscEntityPath using the class name of the Domain Object plus System.currentTimeMillis()
     * and a simple ID sequence as the node name (A CHEAP and dirty key generator, likely to work just fine for light
     * prototyping). Most hierarchical data store DAO implementations override this to enable more efficient look-ups of
     * nodes based on a synthetic key (e.g., Preference can be represented uniquely by its owner, context and name).
     */
    @Override
    public EntityPath createKey(T domainObject) {
      @SuppressWarnings("unchecked")
      ContainerPath parentPath = createParentPath((Class<T>)domainObject.getClass());
      EntityPath result = new EntityPath(parentPath, Long.toString(System.currentTimeMillis()) + "_" + SimpleIdGenerator.getInstance().getNextId());

      return result;
    }

    protected ContainerPath createParentPath(Class<T> clazz) {
      StringBuilder parentPath = new StringBuilder();

      parentPath.append(ContainerPath.CONTAINER_PATH_SEPARATOR);
      parentPath.append(clazz.getName());
      parentPath.append(ContainerPath.CONTAINER_PATH_SEPARATOR);

      return new ContainerPath(parentPath.toString());
    }
  }

  // ============================================================
  // AbstractDataStoreDAO
  // ============================================================

  protected AbstractDataStoreDAO(DomainObjectTransformer<T> domainObjectTransformer) {
    this.domainObjectTransformer = domainObjectTransformer;
  }

  /**
   * The following parameters will be read from the given StringMap:
   * <ul>
   * <li>storePath - The ROOT data store for this particular DAO.
   * </ul>
   *
   * @throws InitializationException if a problem is encountered with the given initParams.
   */
  @Override
  public void init(InitializationStrategy daoInitStrategy) {
    // get the base class parameters, because common DAO related keys can be specified here (e.g., base name), and
    // add the concrete class parameters to the base daoParams, overwriting base params if duplicated by the concrete class.
    StringMap daoParams = LocatorUtils.getInheritedParameters(daoInitStrategy, this.getClass(), GenericDAO.class);

    // get the required DataStoreContainerFactory class name
    String dataStoreContainerFactoryClassName = LocatorUtils.getRequiredParam(daoParams, this.getClass().getName(), DataStoreDAOInitParamKeys.STORE_CONTAINER_FACTORY, daoInitStrategy);

    // create the DSC factory
    try {
      Class<DataStoreContainerFactory> dscfClazz = ClassUtils.classFrom(dataStoreContainerFactoryClassName);
      dscFactory = ClassUtils.instanceFrom(dscfClazz);

      dscFactory.init(daoInitStrategy, this.getClass().getName());
    } catch (ClassUtilsException e) {
      throw new InitializationException("ERROR: Could not create the DataStoreContainerFactory of type: '" + dataStoreContainerFactoryClassName + "' for DAO of type: " + this.getClass().getName() + ".", e, daoInitStrategy);
    }

    // validate the container
    DataStoreContainer dataStoreContainer = createDataStoreContainer(new ContainerPath(), false);
    dataStoreContainer.validate();
  }

  @Override
  public void reset() {

  }

  /**
   * Save the given domainObject in the data store.
   *
   * @throws DAOException if id is not null or Node already exists in the data store.
   */
  @Override
  public synchronized void save(T domainObject) {
    if (domainObject != null) {
      if (domainObject.getId() != null) {
        throw new DAOException("ERROR: Can't save a Domain Object that already has an ID. Use update function instead. ID is: '" + domainObject.getId().toString() + "'.");
      }

      // create a primary key, using the given Domain Object, and assert that it's unique within the data store
      EntityPath primaryKey = getKeyGen().createKey(domainObject);
      domainObject.setId(primaryKey);
      DataStoreContainer dataStoreContainer = createDataStoreContainer(domainObject, true);

      if (dataStoreContainer.isExistingEntity(primaryKey.getEntityName())) {
        throw new DAOException("ERROR: ID already exists in the data store. Path is: '" + dataStoreContainer.getDebugPathInfo(primaryKey.getEntityName()) + "'.");
      }

      // flatten the given object to an InputStream, so it can be persisted to the data store
      DataStoreEntity dataStoreEntity = domainObjectTransformer.flatten(domainObject);

      dataStoreContainer.saveOrUpdateEntity(primaryKey.getEntityName(), dataStoreEntity);
    }
  }

  /**
   * if the given domainObject has an ID, then calls update; otherwise, calls save.
   */
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

  /**
   * Update the data store from the given domainObject.
   *
   * @throws DAOException if id is null or Node does not exist in the data store.
   */
  @Override
  public synchronized void update(T domainObject) {
    if (domainObject != null) {
      if (domainObject.getId() == null) {
        throw new DAOException("ERROR: Can't update a Domain Object that doesn't have an ID.");
      }

      // get the primary key from the given Domain Object, and assert that it currently exists within the data store
      EntityPath primaryKey = (EntityPath)domainObject.getId();
      DataStoreContainer dataStoreContainer = createDataStoreContainer(domainObject, false); // don't create the parent dir if it doesn't exist (can't update non-existent file)

      if (!dataStoreContainer.isExistingEntity(primaryKey.getEntityName())) {
        throw new DAOException("ERROR: Node does not exist in the data store. Path is: : '" + dataStoreContainer.getDebugPathInfo(primaryKey.getEntityName()) + "'.");
      }

      // flatten the given object to an InputStream, so it can be persisted to the data store
      DataStoreEntity dataStoreEntity = domainObjectTransformer.flatten(domainObject);

      dataStoreContainer.saveOrUpdateEntity(primaryKey.getEntityName(), dataStoreEntity);
    }
  }

  /**
   * Delete the given domainObject from the data store.
   *
   * @throws DAOException if id is null.
   */
  @Override
  public synchronized void delete(T domainObject) {
    if (domainObject != null) {
      if (domainObject.getId() == null) {
        throw new DAOException("ERROR: Can't delete a Domain Object with an empty ID.");
      }

      DataStoreContainer dataStoreContainer = createDataStoreContainer(domainObject, false); // don't create the parent directory if it doesn't exist
      EntityPath entityPath = (EntityPath)domainObject.getId();

      dataStoreContainer.deleteEntity(entityPath.getEntityName());
    }
  }

  @Override
  public synchronized T getByKey(Serializable id) {
    T result = null;

    if (id != null) {
      EntityPath primaryKey = (EntityPath)id;

      DataStoreContainer dataStoreContainer = createDataStoreContainer(primaryKey.getContainerPath(), true); // create the parent directory if it doesn't exist
      DataStoreEntity nodeData = null;

      if (dataStoreContainer.isExistingEntity(primaryKey.getEntityName())) {
        nodeData = dataStoreContainer.readEntity(primaryKey.getEntityName());
      }

      if (nodeData != null) {
        result = domainObjectTransformer.resurrect(primaryKey, nodeData);

        result.setId(getKeyGen().createKey(result));
      }
    }

    return result;
  }

  @Override
  public synchronized List<? extends T> getAll() {
    return null; // TODO-p1(george) IMPLEMENT
  }

  // Support functions ////////////////////////////////////////////////////////////////////

  /**
   * Using the EntityPath, from the given Domain Object ID, create and return a new DataStoreContainer.
   */
  protected DataStoreContainer createDataStoreContainer(T domainObject, boolean createParentContainersIfNonExistent) {
    EntityPath entityPath = (EntityPath)domainObject.getId();

    return createDataStoreContainer(entityPath.getContainerPath(), createParentContainersIfNonExistent);
  }

  /**
   * Create a {@code DataStoreContainer}, using a DataStoreContainerFactory, for the given {@code containerPath}. If
   * {@code createParentContainersIfNonExistent} is true, then create any necessary but nonexistent parent directories.
   */
  protected DataStoreContainer createDataStoreContainer(ContainerPath containerPath, boolean createParentContainersIfNonExistent) {
    return dscFactory.createDataStoreContainer(containerPath, createParentContainersIfNonExistent);
  }

  /** Return the key generator used to create primary keys for all Domain Objects saved by this DAO. */
  protected DataStoreKeyGen<T> getKeyGen() {
    if (keyGen == null) {
      keyGen = createKeyGen();
    }
    return keyGen;
  }

  /** Create and return the default KeyGen (default is NanoKeyGen). Override this to provide an alternate type. */
  protected DataStoreKeyGen<T> createKeyGen() {
    return new DataStoreKeyGen<T>();
  }

  /**
   * Return the absolute base path used to store the containers managed by this DAO - used for diagnostics.
   */
  public BaseStorePath getBaseStorePath() {
    return dscFactory.getBaseStorePath();
  }

  @Override
  public String getInfo() {
    return getClass().getSimpleName() + " using " + dscFactory.getClass().getSimpleName() + " at " + getBaseStorePath();
  }

}
