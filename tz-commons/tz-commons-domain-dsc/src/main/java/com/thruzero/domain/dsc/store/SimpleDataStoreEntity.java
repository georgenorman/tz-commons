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

import java.io.InputStream;

import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dsc.store.DataStoreContainer.DataStoreEntity;


/**
 * A simple implementation of DataStoreEntity that represents the data to be read from or written to a particular
 * DataStoreContainer.
 *
 * @author George Norman
 */
public class SimpleDataStoreEntity implements DataStoreEntity {
  private EntityPath entityPath;
  private InputStream inputStream;

  public SimpleDataStoreEntity(InputStream inputStream, EntityPath entityPath) {
    this.inputStream = inputStream;
    this.entityPath = entityPath;
  }

  @Override
  public EntityPath getEntityPath() {
    return entityPath;
  }

  @Override
  public InputStream getData() {
    return inputStream;
  }
}
