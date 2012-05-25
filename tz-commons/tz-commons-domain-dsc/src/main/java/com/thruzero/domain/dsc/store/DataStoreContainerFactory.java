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
package com.thruzero.domain.dsc.store;

import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.domain.store.BaseStorePath;

/**
 * A factory for creating instances of {@link com.thruzero.domain.store.DataStoreContainer DataStoreContainer}, which
 * are used to handle CRUD operations within a single container. For example, the {@code FileDataStoreContainerFactory}
 * creates instances of {@code FileDataStoreContainer} to manage CRUD operations in a single file system directory.
 *
 * @author George Norman
 */
public interface DataStoreContainerFactory {

  /**
   * @param daoParams param map used to init the DAO.
   * @param sourceSectionName the name of the section the param map was read from.
   */
  void init(InitializationStrategy daoInitStrategy, String sourceSectionName);

  /** Return the absolute BASE store path for containers created by this factory (baseStorePath => "${root-store-path}/${base-name}". */
  BaseStorePath getBaseStorePath();

  DataStoreContainer createDataStoreContainer(ContainerPath containerPath, boolean createParentContainersIfNonExistent);

}
