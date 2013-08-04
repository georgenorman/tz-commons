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

package com.thruzero.common.web.model.container.builder.xml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.web.model.container.builder.PanelBuilder;
import com.thruzero.common.web.model.css.StyleClass;

/**
 * An abstract base class for XML-based panel builders.
 *
 * @author George Norman
 */
public abstract class AbstractXmlPanelBuilder implements PanelBuilder {
  /**
   * Name of the attribute representing the ID of a particular panel. The default name is "id" and can be changed via
   * config.
   */
  public static final String PANEL_ID = ConfigLocator.locate().getValue(AbstractXmlPanelBuilder.class.getName(), "id", "id");

  /**
   * Name of the attribute representing the title of a particular panel. The default name is "title" and can be
   * changed via config.
   */
  public static final String TITLE_ID = ConfigLocator.locate().getValue(AbstractXmlPanelBuilder.class.getName(), "title", "title");

  /**
   * Name of the attribute representing an optional flag to render the domain for anchor list items. The default name is "renderDomain" and can be changed via config.
   */
  public static final String RENDER_DOMAIN_ID = ConfigLocator.locate().getValue(AbstractXmlPanelBuilder.class.getName(), "renderDomain", "renderDomain");

  /**
   * Name of the attribute representing an optional flag for the collapse direction (in or out). The default name is "collapseDirection" and can be changed via config.
   */
  public static final String COLLAPSE_DIRECTION_ID = ConfigLocator.locate().getValue(AbstractXmlPanelBuilder.class.getName(), "collapseDirection", "collapseDirection");

  /**
   * Name of the attribute representing an optional CSS class for the panel header. The default name is "headerStyleClass" and can be changed via config.
   */
  public static final String HEADER_STYLE_CLASS_ID = ConfigLocator.locate().getValue(AbstractXmlPanelBuilder.class.getName(), "headerStyleClass", "headerStyleClass");

  /**
   * Name of the attribute representing an optional toolbar for the panel header. The default name is "toolbar" and can be changed via config.
   */
  public static final String TOOLBAR_ID = ConfigLocator.locate().getValue(AbstractXmlPanelBuilder.class.getName(), "toolbar", "toolbar");

  private final InfoNodeElement panelNode;

  // ------------------------------------------------------
  // XmlPanelBuilderAnnotation
  // ------------------------------------------------------

  /**
   * Maps a panel node name to a panel type (e.g., maps a "listPanel" element to a ListPanel).
   *
   * @param panelTypeName type of panel (typically the node name - e.g., &lt;listPanel&gt;, &lt;htmlPanel&gt;).
   * @author George Norman
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Target({ElementType.TYPE})
  public static @interface XmlPanelBuilderAnnotation {
    String panelTypeName();
  }

  // ============================================================================
  // AbstractXmlPanelBuilder
  // ============================================================================

  public AbstractXmlPanelBuilder(InfoNodeElement panelNode) {
    this.panelNode = panelNode;
  }

  protected InfoNodeElement getPanelNode() {
    return panelNode;
  }

  protected String getPanelId() {
    return panelNode.getAttributeValue(PANEL_ID);
  }

  protected String getPanelTitle() {
    return panelNode.getAttributeValue(TITLE_ID);
  }

  protected boolean getRenderDomain() {
    return panelNode.getAttributeTransformer(RENDER_DOMAIN_ID).getBooleanValue(false);
  }

  protected String getCollapseDirection() {
    return panelNode.getAttributeValue(COLLAPSE_DIRECTION_ID);
  }

  protected StyleClass getPanelHeaderStyleClass() {
    return new StyleClass(panelNode.getAttributeValue(HEADER_STYLE_CLASS_ID));
  }

  protected List<InfoNodeElement> getToolbar() throws Exception {
    List<InfoNodeElement> result = new ArrayList<InfoNodeElement>();
    InfoNodeElement dataListNode = getPanelNode().findElement(TOOLBAR_ID);

    if (dataListNode != null) {
      for (Iterator<? extends InfoNodeElement> iter = dataListNode.getChildNodeIterator(); iter.hasNext(); ) {
        result.add(iter.next());
      }
    }

    return result;
  }
}
