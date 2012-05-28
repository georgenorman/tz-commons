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

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.SimpleInfo;
import com.thruzero.domain.dsc.dao.AbstractDataStoreDAO.DataStoreDAOInitParamKeys;
import com.thruzero.domain.store.BaseStorePath;

/**
 *
 * @author George Norman
 */
public abstract class AbstractDataStoreContainerFactory implements DataStoreContainerFactory {
  /** The BASE store path for containers created by this factory (e.g., for file factories it's baseStorePath => "${root-store-path}/${base-name}". */
  private BaseStorePath baseStorePath;

  @Override
  public void init(InitializationStrategy initStrategy, String sourceSectionName) {
    // get the section for this DSC factory
    StringMap dscFactoryParams = initStrategy.getSectionAsStringMap(this.getClass().getName());
    if (dscFactoryParams == null) {
      throw InitializationException.createMissingSectionInitializationException(this.getClass().getName(), initStrategy);
    }

    // get the base name for the container
    StringMap daoParams = initStrategy.getSectionAsStringMap(sourceSectionName);
    String baseName = daoParams.get(DataStoreDAOInitParamKeys.BASE);
    if (StringUtils.isEmpty(baseName)) {
      throw InitializationException.createMissingKeyInitializationException(sourceSectionName, DataStoreDAOInitParamKeys.BASE, initStrategy);
    }

    // subclass init
    this.baseStorePath = doInit(initStrategy, sourceSectionName, dscFactoryParams, baseName);
  }

  /**
   * Subclass initialization hook - returns the baseStorePath for the container factory.
   * @param daoInitStrategy
   * @param sourceSectionName
   * @param dscFactoryParams
   * @param baseName
   * @return
   */
  protected abstract BaseStorePath doInit(InitializationStrategy daoInitStrategy, String sourceSectionName, StringMap dscFactoryParams, String baseName);

  @Override
  public BaseStorePath getBaseStorePath() {
    return baseStorePath;
  }

  @Override
  public SimpleInfo getSimpleInfo() {
    return SimpleInfo.createSimpleInfo(this);
  }

}
