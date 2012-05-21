/*
 *   Copyright 2011 George Norman
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
package com.thruzero.common.core.locator;

import com.thruzero.common.core.support.Singleton;

/**
 * An interface for {@code Locator} strategies.
 *
 * @author George Norman
 * @param <T> Type of Object located by this strategy.
 */
public interface LocatorStrategy<T extends Singleton> {

  /**
   * Locate the specified Singleton {@code type} and initialize it the first time, using the params specified by the
   * registered binding, if present. If the located instance has an init method, with the following signature, it will
   * be called immediately after construction:
   *
   * <pre>
   * void init(com.thruzero.common.core.map.StringMap);
   * </pre>
   *
   * Note: An init method is used because the SecurityManager does not allow construction with a non-default constructor
   * (if the constructor is private).
   */
  T locate(Class<? extends T> type);

}
