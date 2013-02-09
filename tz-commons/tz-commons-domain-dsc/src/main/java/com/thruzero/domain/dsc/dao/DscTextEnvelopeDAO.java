/*
 *   Copyright 2011 - 2012 George Norman
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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.dsc.store.DataStoreContainer;
import com.thruzero.domain.dsc.store.DataStoreContainer.DataStoreEntity;
import com.thruzero.domain.dsc.store.SimpleDataStoreEntity;
import com.thruzero.domain.model.TextEnvelope;

/**
 * An implementation of TextEnvelopeDAO that uses a DataStoreContainer (DSC) as storage.
 * <p>
 * DscTextEnvelopeDAO requires initialization (see {@link AbstractDataStoreDAO.DataStoreDAOInitParamKeys} for
 * details). Following is an example initialization using the config file using the FileDataStoreContainer:
 *
 * <xmp>
 *   <!-- Define the generic file-system DAO settings (see "config.fs.domain.dao.xml") -->
 *   <section name="com.thruzero.domain.dsc.dao.DscTextEnvelopeDAO">
 *     <entry key="baseStorePath" value="[com.thruzero.domain.dsc.dao.AbstractDataStoreDAO]{storePath}/DscTextEnvelopeDAO" />
 *     <entry key="com.thruzero.domain.dsc.store.DataStoreContainerFactory" value="[com.thruzero.domain.dsc.dao.AbstractDataStoreDAO]{com.thruzero.domain.dsc.store.DataStoreContainerFactory}" />
 *   </section>
 *
 *   <!-- Define application-specific file-system DAO settings (see "config.xml" from the  "pf-test18-jpa-war" project) -->
 *   <section name="com.thruzero.domain.dsc.dao.AbstractDataStoreDAO">
 *     <!-- The type of DataStoreContainerFactory to use -->
 *     <entry key="com.thruzero.domain.dsc.store.DataStoreContainerFactory" value="com.thruzero.domain.dsc.fs.FileDataStoreContainerFactory" />
 *   </section>
 * </xmp>
 *
 * As can be seen above, 'com.thruzero.domain.dsc.dao.DscTextEnvelopeDAO' references the [com.thruzero.domain.dsc.dao.AbstractDataStoreDAO] section
 * to get the base store path (using {com.thruzero.domain.dsc.store.DataStoreContainerFactory}) and then appends
 * "DscTextEnvelopeDAO", creating a unique path to the DscTextEnvelopeDAO storage.
 *
 * @author George Norman
 */
public final class DscTextEnvelopeDAO extends AbstractDataStoreDAO<TextEnvelope> implements TextEnvelopeDAO {

  // ------------------------------------------------------
  // DscTextEnvelopeKeyGen
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying a TextEnvelope instance in the store. */
  public static class DscTextEnvelopeKeyGen extends DataStoreKeyGen<TextEnvelope> {
    @Override
    public EntityPath createKey(TextEnvelope domainObject) {
      return domainObject.getEntityPath();
    }
  }

  // ============================================================================
  // DscTextEnvelopeDAO
  // ============================================================================

  /**
   * Use {@link com.thruzero.domain.locator.DAOLocator DAOLocator} to access a particular DAO.
   */
  private DscTextEnvelopeDAO() {
    super(new DomainObjectTransformer<TextEnvelope>() {
      @Override
      public TextEnvelope resurrect(EntityPath primaryKey, DataStoreEntity data) {
        byte[] dataAsBytes;
        InputStream is = data.getData();

        try {
          dataAsBytes = IOUtils.toByteArray(is);
        } catch (IOException e) {
          throw new DAOException("ERROR: Can't read input stream (to byte array).");
        } finally {
          IOUtils.closeQuietly(is);
        }

        TextEnvelope result = new TextEnvelope(primaryKey, new String(dataAsBytes));

        return result;
      }

      @Override
      public DataStoreEntity flatten(TextEnvelope domainObject) {
        String data = domainObject.getData();

        return new SimpleDataStoreEntity(IOUtils.toInputStream(data), domainObject.getEntityPath());
      }
    });
  }

  @Override
  public boolean isExistingTextEnvelope(EntityPath entityPath) {
    return isExistingEntity(entityPath);
  }

  @Override
  public List<? extends TextEnvelope> getTextEnvelopes(ContainerPath containerPath, boolean recursive) {
    List<TextEnvelope> result = new ArrayList<TextEnvelope>();
    DataStoreContainer dsc = createDataStoreContainer(containerPath, true);
    List<? extends DataStoreEntity> entities = dsc.getAllEntities(recursive);

    for (DataStoreEntity entity : entities) {
      EntityPath id = new EntityPath(containerPath, entity.getEntityPath());

      result.add(getByKey(id));
    }

    return result;
  }

  @Override
  public List<EntityPath> getTextEnvelopePaths(ContainerPath containerPath, boolean recursive) {
    DataStoreContainer dsc = createDataStoreContainer(containerPath, true);
    List<EntityPath> result = dsc.getAllEntityPaths(recursive);

    return result;
  }

  @Override
  public TextEnvelope getTextEnvelope(EntityPath entityPath) {
    return getByKey(entityPath);
  }

  @Override
  protected DataStoreKeyGen<TextEnvelope> createKeyGen() {
    return new DscTextEnvelopeKeyGen();
  }
}
