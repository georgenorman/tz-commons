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
package com.thruzero.domain.dsc.fs;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EnvironmentHelper;
import com.thruzero.domain.dsc.dao.AbstractDataStoreDAO.DataStoreDAOInitParamKeys;
import com.thruzero.domain.dsc.store.AbstractDataStoreContainerFactory;
import com.thruzero.domain.dsc.store.DataStoreContainer;
import com.thruzero.domain.store.BaseStorePath;

/**
 * A factory for creating instances of {@link com.thruzero.domain.dsc.fs.FileDataStoreContainer FileDataStoreContainer} to manage CRUD
 * operations within a single directory.
 *
 * @author George Norman
 */
public class FileDataStoreContainerFactory extends AbstractDataStoreContainerFactory {

  // ------------------------------------------------
  // FileDataStoreContainerFactoryInitParamKeys
  // ------------------------------------------------

  /**
   * Initialization parameter keys defined for {@code FileDataStoreContainerFactory}.
   */
  @InitializationParameterKeysBookmark
  public interface FileDataStoreContainerFactoryInitParamKeys extends DataStoreDAOInitParamKeys {

    /**
     * The parameter key that defines the ROOT data store for all file-based DSC DAOs.
     * This is an absolute file path to the root store. Each file-based data store container will use this
     * root to form a base path, which is the ROOT path prepended to the BASE (see DataStoreDAOInitParamKeys.BASE).
     * For example, if the rootStorePath is defined to be "/users/george/data-store" and the base
     * is defined to be "DscTextEnvelopeDAO", then the baseStorePath will be "/users/george/data-store/DscTextEnvelopeDAO".
     * In this case, the DscTextEnvelopeDAO will manage the DscTextEnvelopeDAO store using a FileDataStoreContainerFactory
     * instance for each sub-directory (e.g., "/users/george/data-store/DscTextEnvelopeDAO/foo" and "/users/george/data-store/DscTextEnvelopeDAO/foo/bar"
     * will each be managed by a separate instance of FileDataStoreContainerFactory).
     */
    String ROOT_STORE_PATH = "rootStorePath";
  }

  // ------------------------------------------------
  // FileBaseStorePath
  // ------------------------------------------------

  public static class FileBaseStorePath implements BaseStorePath {
    private String baseStorePath;

    public FileBaseStorePath(String rootStorePath, String base, InitializationStrategy daoInitStrategy) {
      if (StringUtils.isEmpty(rootStorePath)) {
        throw new InitializationException("ERROR: rootStorePath is empty.", daoInitStrategy);
      }

      if (StringUtils.isEmpty(base)) {
        throw new InitializationException("ERROR: base is empty.", daoInitStrategy);
      }

      if (rootStorePath.endsWith(ContainerPath.CONTAINER_PATH_SEPARATOR) || rootStorePath.endsWith(EnvironmentHelper.FILE_PATH_SEPARATOR)) {
        this.baseStorePath = rootStorePath + base;
      } else {
        this.baseStorePath = rootStorePath + EnvironmentHelper.FILE_PATH_SEPARATOR + base;
      }

      this.baseStorePath = FilenameUtils.normalize(this.baseStorePath);
    }

    @Override
    public String toString() {
      return baseStorePath;
    }
  }

  // ============================================================
  // FileDataStoreContainerFactory
  // ============================================================

  @Override
  public BaseStorePath doInit(InitializationStrategy daoInitStrategy, String sourceSectionName, StringMap dscFactoryParams, String baseName) {
    // get the absolute path to the data store directory from the DSC factory section.
    String rootStorePath = dscFactoryParams.get(FileDataStoreContainerFactoryInitParamKeys.ROOT_STORE_PATH);
    if (StringUtils.isEmpty(rootStorePath)) {
      throw InitializationException.createMissingKeyInitializationException(this.getClass().getName(), FileDataStoreContainerFactoryInitParamKeys.ROOT_STORE_PATH, daoInitStrategy);
    }

    BaseStorePath baseStorePath = new FileBaseStorePath(rootStorePath, baseName, daoInitStrategy);

    return baseStorePath;
  }

  @Override
  public DataStoreContainer createDataStoreContainer(ContainerPath containerPath, boolean createParentContainersIfNonExistent) {
    return new FileDataStoreContainer(getBaseStorePath(), containerPath, createParentContainersIfNonExistent);
  }

}
