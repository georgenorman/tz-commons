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
package com.thruzero.common.core.support;

import java.io.Serializable;

/**
 * A primary-key generator.
 *
 * @author George Norman
 * @param <T> Type of Object keys will be generated for.
 */
public class KeyGen<T> {
  /**
   * Create a unique key for the given Object. By default, the key will simply be the next value from a
   * sequence. However, subclasses of KeyGen may generate a compound key based on a set of fields from the given
   * sourceObject (e.g., Setting compound key may be generated from its context and name).
   */
  public Serializable createKey(T sourceObject) {
    String nextVal = SimpleIdGenerator.getInstance().getNextIdAsString();

    return nextVal;
  }
}
