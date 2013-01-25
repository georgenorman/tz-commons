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

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.thruzero.common.core.utils.UiUtils;
import com.thruzero.common.web.model.nav.MenuNode.MenuNodePath;

/**
 * A model representing a series of menus (e.g., menu bar or tab bar) and maintains the path to the active menu.
 *
 * @author George Norman
 */
public class MenuBar implements MenuStateHolder {
  /** Optional menubar title. */
  private String title;

  /** Path to the active MenuNode. */
  private MenuNodePath activeMenuPath;

  /** set of menus associated with this menubar. */
  private Map<String, MenuNode> menus = new LinkedHashMap<String, MenuNode>();

  /** Return the optional MenuBar title. */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /** Return the set of menus associated with this menubar. */
  public Collection<MenuNode> getMenus() {
    return UiUtils.unmodifiableListHack(menus.values());
  }

  /** Adds the given menu to the end of the menu list and associates it with the given id. */
  public void addMenu(String id, MenuNode menu) {
    menus.put(id, menu);
  }

  /**
   * Return the currently active, top-level Menu (from which sub items and sub menus can be retrieved).
   */
  public MenuNode getActiveMenu() {
    MenuNode result = null;

    if (getActivePath() != null) {
      result = menus.get(getActivePath().getSegmentId(0));
    }

    return result;
  }

  public MenuNode getActiveNode() {
    MenuNode result = null;

    if (getActivePath() != null) {
      Iterator<String> iter = getActivePath().getSegmentIterator();
      result = menus.get(iter.next());

      while (result != null && iter.hasNext()) {
        result = result.getChild(iter.next());
      }
    }

    return result;
  }

  /**
   * Return the first Menu (does not change or set the active menu).
   */
  public MenuNode getDefaultMenu() {
    if (menus == null || menus.isEmpty()) {
      return null;
    } else {
      return menus.values().iterator().next();
    }
  }

  @Override
  public MenuNodePath getActivePath() {
    return activeMenuPath;
  }

  public void setActiveMenuPath(MenuNodePath activeMenuPath) {
    this.activeMenuPath = activeMenuPath;
  }

  /**
   * Return null, as the menu bar has no parent.
   */
  @Override
  public MenuNodePath getPath() {
    return null;
  }

}
