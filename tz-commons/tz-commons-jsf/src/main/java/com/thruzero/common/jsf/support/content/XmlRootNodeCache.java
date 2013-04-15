/*
 *   Copyright 2013 George Norman
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

package com.thruzero.common.jsf.support.content;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.jsf.support.beans.DynamicContentBean.ContentException;
import com.thruzero.common.jsf.support.beans.DynamicContentBean.RootNodeCache;
import com.thruzero.common.web.model.container.PanelSet;
import com.thruzero.common.web.model.container.RowSet;
import com.thruzero.common.web.model.container.builder.PanelSetBuilder;
import com.thruzero.common.web.model.container.builder.RowSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.StandardXmlPanelBuilderTypeRegistry;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.XmlPanelBuilderTypeRegistry;
import com.thruzero.common.web.model.container.builder.xml.XmlRowSetBuilder;
import com.thruzero.common.web.model.nav.MenuBar;
import com.thruzero.common.web.model.nav.builder.xml.XmlMenuBarBuilder;

/**
 * 
 * @author George Norman
 */
public class XmlRootNodeCache extends RootNodeCache {
  private static final XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry = new StandardXmlPanelBuilderTypeRegistry();

  /**
   * Create a root-node cache that can build a variety of component models from XML (e.g., PanelSet and MenuBar models).
   */
  public XmlRootNodeCache(InfoNodeElement rootNode) {
    super(rootNode);
  }

  @Override
  protected RowSet buildRowSet(InfoNodeElement rowSetNode) throws ContentException {
    RowSetBuilder builder = new XmlRowSetBuilder(rowSetNode, panelBuilderTypeRegistry);
    RowSet result;
    try {
      result = builder.build();
    } catch (Exception e) {
      throw new ContentException("ERROR building RowSet.", e);
    }

    return result;
  }

  @Override
  protected PanelSet buildPanelSet(InfoNodeElement panelSetNode) throws ContentException {
    PanelSetBuilder builder = new XmlPanelSetBuilder(panelSetNode, panelBuilderTypeRegistry);
    PanelSet result;
    try {
      result = builder.build();
    } catch (Exception e) {
      throw new ContentException("ERROR building PanelSet.", e);
    }

    return result;
  }

  @Override
  protected MenuBar buildMenuBar(InfoNodeElement menuBarNode) throws ContentException {
    XmlMenuBarBuilder builder = new XmlMenuBarBuilder(menuBarNode);
    MenuBar result = builder.build();

    return result;
  }

}
