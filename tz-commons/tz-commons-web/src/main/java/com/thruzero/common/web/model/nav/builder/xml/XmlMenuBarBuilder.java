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

package com.thruzero.common.web.model.nav.builder.xml;

import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.web.model.nav.MenuBar;
import com.thruzero.common.web.model.nav.MenuNode;
import com.thruzero.common.web.model.nav.MenuStateHolder;
import com.thruzero.common.web.model.nav.builder.AbstractMenuBarBuilder;

/**
 * Builds a menu bar from XML (typically retrieved from dynamic content).
 * <p/>
 * The following sample XML would build a menu bar with two menus - "Design" (with two items) and "Languages" (with one
 * item). The UI representation can be a menu bar with drop-down menus or tabs with sub-menus, etc.
 *
 * <pre>
 * {@code
 *  <menu-bar>
 *    <menu>
 *      <id>design</id>
 *      <title>Design</title>
 *      <dataList>
 *        <item>
 *          <id>software</id>
 *          <title>Software</title>
 *          <payload>design.software</payload>
 *        </item>
 *        <item>
 *          <id>ui</id>
 *          <title>UI</title>
 *          <payload>design.ui</payload>
 *        </item>
 *      </dataList>
 *    </menu>
 *    <menu>
 *      <id>languages</id>
 *      <title>Languages</title>
 *      <dataList>
 *        <item>
 *          <id>java</id>
 *          <title>Java</title>
 *          <payload>languages.java</payload>
 *        </item>
 *      </dataList>
 *    </menu>
 *  </menu-bar>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class XmlMenuBarBuilder extends AbstractMenuBarBuilder {
  private static final Logger logger = Logger.getLogger(XmlMenuBarBuilder.class);

  /**
   * Name of the child element representing the ID of a particular menu. The default name is "id" and can be changed via
   * config.
   */
  private static final String MENU_ID = ConfigLocator.locate().getValue(MenuNode.class.getName(), "id", "id");

  /**
   * Name of the child element representing the title of a particular menu. The default name is "title" and can be
   * changed via config.
   */
  private static final String TITLE_ID = ConfigLocator.locate().getValue(MenuNode.class.getName(), "title", "title");

  /**
   * Name of the child element representing optional data associated with a menu (e.g., resource ID of a record in a
   * database). The default name is "payload" and can be changed via config.
   */
  private static final String PAYLOAD_ID = ConfigLocator.locate().getValue(MenuNode.class.getName(), "payload", "payload");

  /** Name of the child element representing menu items. The default name is "dataList" and can be changed via config. */
  private static final String DATALIST_ID = ConfigLocator.locate().getValue(MenuNode.class.getName(), "dataList", "dataList");

  private InfoNodeElement menusNode;

  public XmlMenuBarBuilder(InfoNodeElement menusNode) {
    this.menusNode = menusNode;
  }

  @Override
  protected void loadMenus(MenuBar menuBar) {
    @SuppressWarnings("unchecked")
    List<InfoNodeElement> menuNodes = menusNode.getChildren();

    for (InfoNodeElement menuNodeElement : menuNodes) {
      MenuNode rootMenu = loadMenu(menuBar, menuNodeElement);

      if (rootMenu != null) {
        addRootMenu(menuBar, rootMenu.getId(), rootMenu);
      }
    }
  }

  protected MenuNode loadMenu(MenuStateHolder parent, InfoNodeElement menuNodeElement) {
    MenuNode result = null;
    String id = menuNodeElement.getChildText(MENU_ID);

    try {
      result = new MenuNode(parent, id, menuNodeElement.getChildText(TITLE_ID), (InfoNodeElement)menuNodeElement.getChild(PAYLOAD_ID));
      InfoNodeElement dataListNode = menuNodeElement.findElement(DATALIST_ID);

      if (dataListNode != null) {
        @SuppressWarnings("unchecked")
        List<InfoNodeElement> menuItemNodes = dataListNode.getChildren();
        for (InfoNodeElement menuItemNode : menuItemNodes) {
          MenuNode childMenu = loadMenu(result, menuItemNode);
          if (childMenu != null) {
            result.addChild(childMenu);
          }
        }
      }
    } catch (JDOMException e1) {
      // log error, but don't die because of a menu issue.
      logger.error("ERROR: Menu not loaded: " + id, e1);
    }

    return result;
  }
}
