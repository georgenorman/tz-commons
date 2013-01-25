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

package com.thruzero.domain.utils;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.model.DataStoreInfo;
import com.thruzero.domain.service.InfoNodeService;
import com.thruzero.domain.service.impl.HttpInfoNodeService;

/**
 * Static utility methods related to the domain layer.
 *
 * @author George Norman
 */
public class DomainUtils {

  /**
   * Return the root InfoNodeElement for the given entityPath, taking the user's private RootDataStorePath into account.
   * TODO-p1(george). May need an abstraction for this to bundle various compound services related to the same type.
   *
   * @param entityPath path to the root node, from the RootDataStorePath.
   * @param dataStoreInfo determines if the user has a private RootDataStorePath and if so, modifies the given entityPath to reflect the
   */
  public static InfoNodeElement getRootNode(EntityPath entityPath, final DataStoreInfo dataStoreInfo) {
    InfoNodeElement result;
    InfoNodeService infoNodeService;

    if (StringUtils.isNotEmpty(dataStoreInfo.getPrivateRootDataStorePath())) {
      infoNodeService = ServiceLocator.locate(HttpInfoNodeService.class);
      entityPath = new EntityPath(dataStoreInfo.getPrivateRootDataStorePath(), entityPath.getContainerPath(), entityPath.getEntityName());
    } else {
      infoNodeService = ServiceLocator.locate(InfoNodeService.class);
    }
    result = infoNodeService.getInfoNode(entityPath);

    if (result != null) {
      result.enableRootNode();
    }

    return result;
  }


}
