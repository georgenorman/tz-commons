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

package com.thruzero.common.web.model.nav.builder;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.web.model.nav.MenuBar;
import com.thruzero.common.web.model.nav.MenuNode;

/**
 * Creates a menu bar and then defers to subclasses to populate it with menus.
 *
 * @author George Norman
 */
public abstract class AbstractMenuBarBuilder {

  public MenuBar build() {
    MenuBar result = createMenuBar();

    loadTitle(result);
    loadMenus(result);

    return result;
  }

  protected MenuBar createMenuBar() {
    return new MenuBar();
  }

  protected abstract void loadTitle(MenuBar menuBar);

  protected abstract void loadMenus(MenuBar menuBar);

  /**
   * Each main menu in the menu bar is a root menu (e.g., Design is a root menu with Software and UI as sub-menus, each
   * of which may have their own sub-menus).
   */
  protected void addRootMenu(MenuBar menuBar, String id, MenuNode menu) {
    menuBar.addMenu(id, menu);
  }

  protected InfoNodeElement createSimplePayload(String content) {
    InfoNodeElement result = new InfoNodeElement("payload");

    result.setText(content);

    return result;
  }
}
