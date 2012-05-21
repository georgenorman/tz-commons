/*
 *   Copyright 2009-2012 George Norman
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
 * An interface for objects that are to be persisted (e.g., to a database), with a unique identifier (primary key).
 *
 * @author George Norman
 */
public interface Persistent extends Serializable {

  Serializable getId();

  void setId(Serializable id);

  String getIdAsString();

  /**
   * Perform a shallow copy of the data from the given source object (copyFrom) into this object (self).
   * Only non-transient properties directly owned by this object should be copied. For example,
   * BasicUser owns loginId, password, lastLoginDate, etc, but the id property is owned by AbstractPersistent.
   * Hence, BasicUser copies loginId, password, lastLoginDate, but not the id (super.copyFrom is called instead).
   * Furthermore, the permissions and details properties are separate entities that are persisted externally
   * to BasicUser; these properties will likely be persisted separately by the particular type of UserDAO.
   *
   * @param source the source object that this object uses to read data from and copy into self.
   */
  void copyFrom(Persistent source);

}
