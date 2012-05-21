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
package com.thruzero.common.core.locator;

import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;

/**
 * Marker interface for {@code Singletons} that support initialization by the locator framework - parameters are read
 * from the config file and passed into the {@code init(StringMap)} function.
 *
 * @author George Norman
 */
public interface Initializable {

  // --------------------------------------------------------
  // InitializableParameterKeys
  // --------------------------------------------------------

  /** An interface that defines keys used to lookup initialization values from a StringMap. */
  @InitializationParameterKeysBookmark(comment = "Base interface for initialization parameter keys.")
  public interface InitializableParameterKeys {
  }

  // ===============================================================================
  // Initializable
  // ===============================================================================

  /**
   * This function is called by a {@code Locator} to initialize a located instance.
   */
  void init(InitializationStrategy initParams);

  /**
   * This function is called by a {@code Locator} to remove a located instance from the instance pool. The instance
   * should deallocate any storage it created during the {@code init} phase.
   */
  void reset();

}
