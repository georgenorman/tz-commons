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
package com.thruzero.domain.jpa.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dsc.store.DataStoreContainer;
import com.thruzero.domain.dsc.store.DataStoreException;
import com.thruzero.domain.dsc.store.SimpleDataStoreEntity;
import com.thruzero.domain.jpa.dao.JpaTextEnvelopeDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.TextEnvelope;
import org.apache.log4j.Logger;

/**
 * A {@code DataStoreContainer} that manages entities for a single ContainerPath, accessed via {@code JpaTextEnvelopeDAO}.
 * It doesn't manage sub-containers or parent containers. Each parent container and sub-container is managed by a
 * separate instance of JpaDataStoreContainer.
 * <p>
 * All DataStoreContainer objects are managed by {@code GenericDscDAO}, which will flatten and resurrect the
 * Domain Object instances automatically (passed in as instances of DataStoreEntity).
 *
 * @author George Norman
 */
public class JpaDataStoreContainer implements DataStoreContainer {
  private final JpaTextEnvelopeDAO jpaTextEnvelopeDAO = DAOLocator.locate(JpaTextEnvelopeDAO.class);
  private static final Logger logger = Logger.getLogger(JpaDataStoreContainer.class);

  private ContainerPath containerPath;

  public JpaDataStoreContainer(ContainerPath containerPath) {
    this.containerPath = containerPath;
  }

  @Override
  public DataStoreEntity readEntity(String entityName) {
    EntityPath primaryKey = new EntityPath(containerPath, entityName);
    TextEnvelope inputsEnvelope = jpaTextEnvelopeDAO.getTextEnvelope(primaryKey);
    DataStoreEntity data = new SimpleDataStoreEntity(IOUtils.toInputStream(inputsEnvelope.getData()), primaryKey);

    return data;
  }

  @Override
  public List<? extends DataStoreEntity> getAllEntities(boolean recursive) {
    List<DataStoreEntity> result = new ArrayList<DataStoreEntity>();
    List<? extends TextEnvelope> textEnvelopes = jpaTextEnvelopeDAO.getTextEnvelopes(containerPath, false);

    for (TextEnvelope dataEnvelope : textEnvelopes) {
      DataStoreEntity data = new SimpleDataStoreEntity(IOUtils.toInputStream(dataEnvelope.getData()), dataEnvelope.getEntityPath());

      result.add(data);
    }

    return result;
  }

  @Override
  public void saveOrUpdateEntity(String entityName, DataStoreEntity dataStoreEntity) {
    updateEntity(entityName, dataStoreEntity);
  }

  @Override
  public void updateEntity(String entityName, DataStoreEntity dataStoreEntity) {
    TextEnvelope inputsEnvelope = jpaTextEnvelopeDAO.getTextEnvelope(getPrimaryKey(entityName));

    try {
      if (inputsEnvelope == null) {
        inputsEnvelope = new TextEnvelope(getPrimaryKey(entityName), IOUtils.toString(dataStoreEntity.getData()));
        jpaTextEnvelopeDAO.save(inputsEnvelope);
      } else {
        inputsEnvelope.setData(IOUtils.toString(dataStoreEntity.getData()));
        jpaTextEnvelopeDAO.update(inputsEnvelope);
      }
    } catch (IOException e) {
      String msg = "ERROR: Couldn't update entity named: '" + entityName + "'.";
      logger.error(msg, e);
      throw new DataStoreException(msg);
    }
  }

  @Override
  public void createNewEntity(String entityName) {
    // TODO-p1(george): Revisit
  }

  @Override
  public void deleteEntity(String entityName) {
    TextEnvelope entity = jpaTextEnvelopeDAO.getTextEnvelope(getPrimaryKey(entityName));

    jpaTextEnvelopeDAO.delete(entity);
  }

  @Override
  public boolean isExistingEntity(String entityName) {
    EntityPath primaryKey = new EntityPath(containerPath, entityName);
    boolean result = jpaTextEnvelopeDAO.isExistingTextEnvelope(primaryKey);

    return result;
  }

  @Override
  public void validate() {

  }

  @Override
  public String getDebugPathInfo(String entityName) {
    return getPrimaryKey(entityName).toString();
  }

  private EntityPath getPrimaryKey(String entityName) {
    EntityPath primaryKey = new EntityPath(containerPath, entityName);

    return primaryKey;
  }

}
