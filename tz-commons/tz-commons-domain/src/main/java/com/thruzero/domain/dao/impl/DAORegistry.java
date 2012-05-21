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
package com.thruzero.domain.dao.impl;

import com.thruzero.common.core.bookmarks.LocatorRegistryBookmark;
import com.thruzero.domain.locator.DAOLocator;

/**
 * Abstract base class for all DAO-related locator registries (e.g., DscDAORegistry, HibernateDAORegistry).
 *
 * @author George Norman
 */
@LocatorRegistryBookmark(comment = "DAORegistry")
public abstract class DAORegistry {

  /** Allow for class extensions; disallow client instantiation */
  protected DAORegistry() {
  }

  /** Clears the locator registry and bindings. */
  public void reset() {
    DAOLocator.reset();
  }

}
