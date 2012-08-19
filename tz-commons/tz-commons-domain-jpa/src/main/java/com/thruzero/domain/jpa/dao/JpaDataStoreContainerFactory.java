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

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.domain.dsc.store.AbstractDataStoreContainerFactory;
import com.thruzero.domain.dsc.store.DataStoreContainer;
import com.thruzero.domain.store.BaseStorePath;

/**
 * A factory for creating instances of {@link com.thruzero.domain.jpa.dao.JpaDataStoreContainer JpaDataStoreContainer}.
 *
 * @author George Norman
 */
public class JpaDataStoreContainerFactory extends AbstractDataStoreContainerFactory {

  // ------------------------------------------------
  // JpaBaseStorePath
  // ------------------------------------------------

  public static class JpaBaseStorePath implements BaseStorePath {
    private String baseStorePath;

    public JpaBaseStorePath(String rootStorePath, String base, InitializationStrategy daoInitStrategy) {
      if (StringUtils.isEmpty(rootStorePath)) {
        throw new InitializationException("ERROR: rootStorePath is empty.", daoInitStrategy);
      }

      if (StringUtils.isEmpty(base)) {
        throw new InitializationException("ERROR: base is empty.", daoInitStrategy);
      }

      if (rootStorePath.endsWith(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
        this.baseStorePath = rootStorePath + base;
      } else {
        this.baseStorePath = rootStorePath + ContainerPath.CONTAINER_PATH_SEPARATOR + base;
      }
    }

    @Override
    public String toString() {
      return baseStorePath;
    }
  }

  // ============================================================
  // JpaDataStoreContainerFactory
  // ============================================================

  @Override
  protected BaseStorePath doInit(InitializationStrategy daoInitStrategy, String sourceSectionName, StringMap dscFactoryParams, String baseName) {
    BaseStorePath baseStorePath = new JpaBaseStorePath(ContainerPath.CONTAINER_PATH_SEPARATOR, baseName, daoInitStrategy);

    return baseStorePath;
  }

  @Override
  public DataStoreContainer createDataStoreContainer(ContainerPath containerPath, boolean createParentContainersIfNonExistent) {
    return new JpaDataStoreContainer(containerPath);
  }

}
