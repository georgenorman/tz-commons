/*
 *   Copyright 2010-2012 George Norman
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

import org.apache.commons.lang3.tuple.MutablePair;

/**
 * An instance of this class represents a simple key/value pair, where the key is a String and the value is an Object (a
 * convenience function is provided to get the value as a String). An instance can be used to represent label/value pair
 * in the UI, a substitution variable/replacement for substitution strategies, etc.
 *
 * @author George Norman
 */
public class KeyValuePair extends MutablePair<String, Object> {
  private static final long serialVersionUID = 1L;

  public KeyValuePair(final String name) {
    super(String.valueOf(name), null);
  }

  public KeyValuePair(final String name, final int value) {
    super(name, Integer.toString(value));
  }

  public KeyValuePair(final String name, final long value) {
    super(name, Long.toString(value));
  }

  public KeyValuePair(final String name, final Object value) {
    super(String.valueOf(name), value);
  }

  /** Return the value as a String, by calling its toString() method; if value is null, then return EMPTY String. */
  public String getValueAsString() {
    return getValue() == null ? "" : getValue().toString();
  }

  public void setKey(final String key) {
    setLeft(key);
  }

  // utility methods ////////////////////////////////////////////////////////////////

  /**
   * Return the value of this name/value pair. Note: Many clients expect "{@code toString}" to return the value sans
   * name (e.g., JSP pages, etc.), so that's why
   * {@code toString} returns the value and not the name/value combo.
   * <p>
   * Note: The version that takes a String for formatting will return the key and value.
   */
  @Override
  public String toString() {
    return getValueAsString();
  }

}
