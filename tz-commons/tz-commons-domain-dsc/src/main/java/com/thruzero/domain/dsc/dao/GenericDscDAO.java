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
package com.thruzero.domain.dsc.dao;

import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.io.IOUtils;

import com.thoughtworks.xstream.XStream;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dsc.store.DataStoreContainer.DataStoreEntity;
import com.thruzero.domain.dsc.store.SimpleDataStoreEntity;
import com.thruzero.domain.store.Persistent;

/**
 * An implementation of {@code AbstractDataStoreDAO} that "serializes" a Domain Object using <a
 * href="http://xstream.codehaus.org/">XStream</a> (a serialization component that transparently serializes Java
 * objects to and from XML).
 * <p/>
 * GenericDscDAO uses an EntityPath to represent a unique identifier to a particular entity in a data store (e.g., a file in the file system).
 * The entities may be stored hierarchically and this key can represent the path to a particular entity (from the base
 * store). This key is typically used when a fast lookup of a Domain Object in the data store is desired and the DAO is
 * capable of synthesizing a compound key for the Domain Object. For example, a Preference can be represented uniquely
 * by its owner, context and name. The DscPreferenceDAO uses this knowledge to synthesize a unique key, as shown
 * below:
 *
 * <pre>
 * "/owner1@thruzero.com/context1a/name-one_1.txt".
 * </pre>
 *
 * The base store path is considered the ROOT, so all paths represented by this key are considered absolute data store
 * paths - not to be confused with an absolute file system path. The identifier MUST always begin with a forward slash and
 * never end with one. See {@link com.thruzero.domain.store.EntityPath EntityPath} for examples of valid paths.
 *
 * @author George Norman
 * @param <T> Type of Domain Object managed by this DAO.
 */
public abstract class GenericDscDAO<T extends Persistent> extends AbstractDataStoreDAO<T> { // TODO-p1(george): Should this be GenericDscDAO or DscGenericDAO (it indirectly implements GenericDAO)?

  // ------------------------------------------------------
  // XStreamDomainObjectTransformer
  // ------------------------------------------------------

  /**
   * A {@code DomainObjectTransformer} implemented using {@code XStream} to marshal and unmarshal a given Domain Object to an {@code InputStream}.
   */
  public static class XStreamDomainObjectTransformer<T extends Persistent> implements DomainObjectTransformer<T> {
    private Class<T> domainType;
    private XStream xstream;

    public XStreamDomainObjectTransformer(Class<T> domainType) {
      this.domainType = domainType;

      //xstream = new XStream(new StaxDriver()); // does not require XPP3 library. Note: XStream is thread safe.
      xstream = new XStream();
      xstream.alias(domainType.getSimpleName(), domainType);
      xstream.alias(EntityPath.class.getSimpleName(), EntityPath.class);
    }

    /**
     * Create and return a {@code DataStoreEntity} that contains an {@code InputStream} representing the "marshaled" data of the given the Domain Object.
     * A {@code DataStoreContainer} reads the {@code InputStream} from the {@code DataStoreEntity} and writes it to a store (e.g., the {@code FileDataStoreContainer}
     * uses a {@code FileOutputStream} to write the data to a file).
     *
     * @throws DAOException if can't read the data from the given DataStoreEntity.
     */
    @Override
    public DataStoreEntity flatten(T domainObject) {
      // flatten domain object without the ID (it's redundant, since all DSC entities store the ID in the DataStoreEntity).
      Serializable id = domainObject.getId();
      domainObject.setId(null);
      String serializedData = xstream.toXML(domainObject);
      domainObject.setId(id);

      // get the primaryKey (ID) and save with the DataStoreEntity
      EntityPath primaryKey = null;
      if (domainObject.getId() instanceof EntityPath) {
        primaryKey = (EntityPath)domainObject.getId();
      } else {
        primaryKey = EntityPath.createFromString(domainObject.getIdAsString());
      }

      // the DataStoreEntity is created with a domain object with a null ID and the ID is stored in the DataStoreEntity.
      return new SimpleDataStoreEntity(IOUtils.toInputStream(serializedData), primaryKey);
    }

    /**
     * Read the {@code InputStream}, from the given {@code dataStoreEntity}, and use {@code XStream} to "unmarshal" the Domain Object (e.g.,
     * the {@code FileDataStoreContainer} uses a {@code FileInputStream} to read the XML data from a file).
     *
     * @throws DAOException if can't read the data from the given DataStoreEntity.
     */
    @Override
    public T resurrect(EntityPath primaryKey, DataStoreEntity dataStoreEntity) {
      InputStream is = dataStoreEntity.getData();

      try {
        T result = domainType.cast(xstream.fromXML(is));

        result.setId(primaryKey);

        return result;
      } catch (Exception e) {
        throw new DAOException("ERROR: Could not read the data from the given DataStoreEntity.", e);
      } finally {
        IOUtils.closeQuietly(is);
      }
    }
  }

  // ===========================================================================
  // GenericDscDAO
  // ===========================================================================

  /**
   * DAO constructor for the given type of Domain Object.
   */
  protected GenericDscDAO(final Class<T> domainType) {
    super(new XStreamDomainObjectTransformer<T>(domainType));
  }

}
