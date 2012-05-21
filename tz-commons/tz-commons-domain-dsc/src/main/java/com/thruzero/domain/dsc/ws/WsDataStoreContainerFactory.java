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

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.domain.dsc.dao.AbstractDataStoreDAO.DataStoreDAOInitParamKeys;
import com.thruzero.domain.dsc.store.AbstractDataStoreContainerFactory;
import com.thruzero.domain.dsc.store.DataStoreContainer;
import com.thruzero.domain.store.BaseStorePath;
import com.thruzero.domain.store.ContainerPath;

/**
 * A factory for creating instances of {@link com.thruzero.domain.dsc.ws.WsDataStoreContainer WsDataStoreContainer} to manage CRUD
 * operations within a single node.
 *
 * @author George Norman
 */
public class WsDataStoreContainerFactory extends AbstractDataStoreContainerFactory {
  private String rootServiceUri;

  // ------------------------------------------------
  // WsDataStoreContainerFactoryInitParamKeys
  // ------------------------------------------------

  /**
   * Initialization parameter keys defined for {@code WsDataStoreContainerFactory}.
   */
  @InitializationParameterKeysBookmark
  public interface WsDataStoreContainerFactoryInitParamKeys extends DataStoreDAOInitParamKeys {

    /** The parameter key that defines the Service End Point for the web service. */
    String ROOT_SERVICE_URI = "rootServiceUri";
  }

  // ------------------------------------------------
  // WsBaseStorePath
  // ------------------------------------------------

  public class WsBaseStorePath implements BaseStorePath {
    private String base;

    public WsBaseStorePath(String base, InitializationStrategy initStrategy) {
      if (StringUtils.isEmpty(base)) {
        throw new InitializationException("ERROR: base is empty.", initStrategy);
      }

      if (base.startsWith(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
        this.base = base;
      } else {
        this.base = ContainerPath.CONTAINER_PATH_SEPARATOR + base;
      }
    }

    @Override
    public String toString() {
      return base.toString();
    }
  }

  // ============================================================
  // WsDataStoreContainerFactory
  // ============================================================

  @Override
  public BaseStorePath doInit(InitializationStrategy initStrategy, String sourceSectionName, StringMap dscFactoryParams, String baseName) {
    // get the service end point
    rootServiceUri = dscFactoryParams.get(WsDataStoreContainerFactoryInitParamKeys.ROOT_SERVICE_URI);
    if (StringUtils.isEmpty(rootServiceUri)) {
      throw InitializationException.createMissingKeyInitializationException(this.getClass().getName(), WsDataStoreContainerFactoryInitParamKeys.ROOT_SERVICE_URI, initStrategy);
    }

    return new WsBaseStorePath(baseName, initStrategy);
  }

  @Override
  public DataStoreContainer createDataStoreContainer(ContainerPath containerPath, boolean createParentContainersIfNonExistent) {
    return new WsDataStoreContainer(getBaseStorePath(), containerPath, rootServiceUri);
  }

}
