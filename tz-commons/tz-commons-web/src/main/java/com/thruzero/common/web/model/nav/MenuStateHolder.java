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

package com.thruzero.common.web.model.nav;

import com.thruzero.common.web.model.nav.MenuNode.MenuNodePath;

/**
 * An interface for keeping track of the current menu selection.
 *
 * @author George Norman
 */
public interface MenuStateHolder {
  /**
   * Return the path of the "active" MenuNode. A typical strategy is to let the root hold the state of the path of
   * the active menu (hence, all child nodes just call getActivePath() on their direct parent).
   */
  MenuNodePath getActivePath();

  /** Return the path of this node or null if it's the root (e.g., a MenuBar is the root of any top-level MenuNode it contains). */
  MenuNodePath getPath();
}
