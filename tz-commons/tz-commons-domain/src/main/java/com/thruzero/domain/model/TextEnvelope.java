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
package com.thruzero.domain.model;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.store.AbstractPersistent;
import com.thruzero.domain.store.Persistent;

/**
 * An instance represents a container of text data, persisted with a unique EntityPath. A TextEnvelope can be saved to a
 * database table or to a file (where the EntityPath could be used as the file path within a data store).
 *
 * @author George Norman
 */
public class TextEnvelope extends AbstractPersistent {
  private static final long serialVersionUID = 1L;

  private EntityPath entityPath;
  private String data;

  public TextEnvelope() {
    entityPath = new EntityPath();
  }

  /**
   * Construct an instance with the given text {@code data} and {@code entityPath}.
   */
  public TextEnvelope(final EntityPath entityPath, final String data) {
    this.entityPath = entityPath;
    this.data = data;
  }

  /**
   * Construct an instance with the given text {@code data} and {@code entityPath}.
   */
  public TextEnvelope(final InfoNodeElement infoNode) {
    this.entityPath = infoNode.getEntityPath();
    this.data = infoNode.toStringFormatted();
  }

  @Override
  public void copyFrom(final Persistent source) {
    super.copyFrom(source);

    TextEnvelope textEnvelopeSource = (TextEnvelope)source;
    entityPath = textEnvelopeSource.getEntityPath();
    data = textEnvelopeSource.getData();
  }

  public EntityPath getEntityPath() {
    return entityPath;
  }

  public void setEntityPath(final EntityPath entityPath) {
    this.entityPath = entityPath;
  }

  public String getData() {
    return data;
  }

  public void setData(final String data) {
    this.data = data;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((data == null) ? 0 : data.hashCode());
    result = prime * result + ((entityPath == null) ? 0 : entityPath.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    TextEnvelope other = (TextEnvelope)obj;
    if (data == null) {
      if (other.data != null)
        return false;
    } else if (!data.equals(other.data))
      return false;
    if (entityPath == null) {
      if (other.entityPath != null)
        return false;
    } else if (!entityPath.equals(other.entityPath))
      return false;
    return true;
  }
}
