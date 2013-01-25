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

package com.thruzero.common.jsf.support.content;

import java.io.Serializable;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.jsf.support.beans.DynamicContentBean.ContentException;
import com.thruzero.common.jsf.support.beans.DynamicContentBean.RootNodeCache;
import com.thruzero.common.jsf.support.beans.DynamicContentBean.RootNodeCacheBuilder;
import com.thruzero.common.web.model.container.PanelSet;
import com.thruzero.common.web.model.container.builder.PanelSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.StandardXmlPanelBuilderTypeRegistry;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.XmlPanelBuilderTypeRegistry;
import com.thruzero.common.web.model.nav.MenuBar;
import com.thruzero.common.web.model.nav.builder.xml.XmlMenuBarBuilder;
import com.thruzero.domain.model.DataStoreInfo;
import com.thruzero.domain.utils.DomainUtils;

/**
 * Supports reading and caching dynamic content from an XML-based data store.
 *
 * @author George Norman
 */
public class XmlRootNodeCacheBuilder implements RootNodeCacheBuilder, Serializable {
  private static final long serialVersionUID = 1L;

  private static final XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry = new StandardXmlPanelBuilderTypeRegistry();

  // ------------------------------------------------------
  // XmlRootNodeCache
  // ------------------------------------------------------

  public static class XmlRootNodeCache extends RootNodeCache {

    /**
     * Create a root-node cache that can build a variety of component models from XML (e.g., PanelSet and MenuBar models).
     */
    public XmlRootNodeCache(InfoNodeElement rootNode) {
      super(rootNode);
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

  // ============================================================================
  // XmlRootNodeCacheBuilder
  // ============================================================================

  @Override
  public RootNodeCache createRootNodeCache(EntityPath entityPath, DataStoreInfo dataStoreInfo) {
    RootNodeCache result = null;
    InfoNodeElement rootNode = DomainUtils.getRootNode(entityPath, dataStoreInfo);

    if (rootNode != null) {
      result = new XmlRootNodeCache(rootNode);
    }

    return result;
  }

}
