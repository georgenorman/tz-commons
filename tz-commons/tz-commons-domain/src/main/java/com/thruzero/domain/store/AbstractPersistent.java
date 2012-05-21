/*
 *   Copyright 2009 George Norman
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
package com.thruzero.domain.store;

import java.io.Serializable;


/**
 * A base class for implementing persistent Domain Objects.
 *
 * @author George Norman
 */
public abstract class AbstractPersistent implements Persistent {
  private static final long serialVersionUID = 1L;

  private Serializable id;

  protected AbstractPersistent() {
  }

  protected AbstractPersistent(final Serializable id) {
    this.id = id;
  }

  @Override
  public void copyFrom(final Persistent source) {
    id = source.getId();
  }

  @Override
  public String getIdAsString() {
    return id == null ? null : id.toString();
  }

  @Override
  public Serializable getId() {
    return id;
  }

  @Override
  public void setId(final Serializable id) {
    this.id = id;
  }

}
