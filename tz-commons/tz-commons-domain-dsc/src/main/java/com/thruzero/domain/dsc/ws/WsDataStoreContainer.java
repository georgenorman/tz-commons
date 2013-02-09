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
package com.thruzero.domain.dsc.ws;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.SaxInfoNodeBuilder;
import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dsc.store.DataStoreContainer;
import com.thruzero.domain.dsc.store.SimpleDataStoreEntity;
import com.thruzero.domain.store.BaseStorePath;

/**
 * Web Service based data store container
 * ### This is Work-In-Progress #### Only the readEntity function is implemented.
 * TODO-p1(george) Finish this implementation
 * <p>
 * All DataStoreContainer objects are managed by {@code GenericDscDAO}, which will flatten and resurrect the
 * Domain Object instances automatically (and passed in as instances of DataStoreEntity).
 *
 * @author George Norman
 */
public class WsDataStoreContainer implements DataStoreContainer {
  /** Saved ContainerPath instance. */
  private ContainerPath resourceContainerPath;

  private Client client;
  private WebResource resource;

  // ============================================================
  // WsDataStoreContainer
  // ============================================================

  public WsDataStoreContainer(BaseStorePath baseStorePath, ContainerPath containerPath, String rootServiceUri) {
    this.resourceContainerPath = new ContainerPath(containerPath.getPath());

    this.client = Client.create();
    this.resource = client.resource(rootServiceUri);
  }

  @Override
  public DataStoreEntity readEntity(String entityName) {
    MultivaluedMap<String, String> params = new MultivaluedMapImpl();
    params.add("containerPath", resourceContainerPath.getPath());
    params.add("entityName", entityName);

    String response = resource.path("readEntity").queryParams(params).get(String.class);

    SimpleDataStoreEntity result = new SimpleDataStoreEntity(IOUtils.toInputStream(response), new EntityPath(new ContainerPath(), entityName));

    return result;
  }

  @Override
  public List<? extends DataStoreEntity> getAllEntities(boolean recursive) {
    // TODO-p2(george) Auto-generated method stub
    return null;
  }

  @Override
  public List<EntityPath> getAllEntityPaths(boolean recursive) {
    // TODO-p2(george) Auto-generated method stub
    return null;
  }

  @Override
  public void saveOrUpdateEntity(String entityName, DataStoreEntity dataStoreEntity) {
    // TODO-p2(george) Auto-generated method stub

  }

  @Override
  public void updateEntity(String entityName, DataStoreEntity dataStoreEntity) {
    // TODO-p2(george) Auto-generated method stub

  }

  @Override
  public void createNewEntity(String entityName) {
    // TODO-p2(george) Auto-generated method stub

  }

  @Override
  public void deleteEntity(String entityName) {
    // TODO-p2(george) Auto-generated method stub

  }

  @Override
  public boolean isExistingEntity(String entityName) {
    MultivaluedMap<String, String> params = new MultivaluedMapImpl();
    params.add("containerPath", resourceContainerPath.getPath());
    params.add("entityName", entityName);

    String response = resource.path("isExistingEntity").queryParams(params).get(String.class);

    InfoNodeElement responseAsNode;
    try {
      responseAsNode = SaxInfoNodeBuilder.DEFAULT.buildInfoNode(response, null);
    } catch (Exception e) {
      throw new RuntimeException("Invalid response node.", e);
    }

    return responseAsNode.getAttributeTransformer("value").getBooleanValue();
  }

  @Override
  public void validate() {
    // TODO-p2(george) Auto-generated method stub

  }

  @Override
  public String getDebugPathInfo(String entityName) {
    // TODO-p2(george) Auto-generated method stub
    return null;
  }

}
