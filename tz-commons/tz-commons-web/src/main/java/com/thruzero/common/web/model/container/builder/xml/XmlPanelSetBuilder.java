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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.utils.ClassUtils;
import com.thruzero.common.core.utils.ClassUtils.ClassUtilsException;
import com.thruzero.common.web.model.container.HtmlPanel;
import com.thruzero.common.web.model.container.PanelSet;
import com.thruzero.common.web.model.container.builder.PanelBuilder;
import com.thruzero.common.web.model.container.builder.PanelSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.AbstractXmlPanelBuilder.XmlPanelBuilderAnnotation;

/**
 * A builder of PanelSet instances, using XML as the definition. Below is a sample &lt;panelSet&gt; node containing the
 * set of panel nodes (in presentation order). The XmlPanelSetBuilder will parse the XML and build each panel and add it
 * to the result.
 *
 * <pre>
 * {@code
 * <index>
 *  <panelSet id="col1">
 *   <listPanel title="Panel-1" id="col1_panel1">
 *     <dataList>
 *       <a href="item11.html" title="Item-11"/>
 *       <a href="item12.html" title="Item-12"/>
 *       <a href="item13.html" title="Item-13"/>
 *     </dataList>
 *   </listPanel>
 *
 *   <listPanel title="Panel-2" id="col1_panel2">
 *     <dataList>
 *       <a href="item21.html" title="Item-21"/>
 *       <a href="item22.html" title="Item-22"/>
 *     </dataList>
 *   </listPanel>
 *  </panelSet>
 *
 *   ...
 *
 * </index>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class XmlPanelSetBuilder implements PanelSetBuilder {
  private static final String ID = ConfigLocator.locate().getValue(PanelSet.class.getName(), "id", "id");
  //private static final String PANEL_PATH_QUERY = ConfigLocator.locate().getValue(PanelSet.class.getName(), "panelPathQuery", "/index/panelSet/*[ends-with(name(),'Panel')]");

  private InfoNodeElement panelSetNode;
  private XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry;

  // ------------------------------------------------------
  // XmlPanelBuilderTypeRegistry
  // ------------------------------------------------------

  /**
   * A registry of XML-based panel builders, that maps panel names (e.g., 'listPanel') to panel builder types (e.g.,
   * 'XmlListPanelBuilder').
   * <p/>
   * Clients must manually register builder types with the xregistry. Below is an example of the FAQ page bean's
   * registry initialization:
   *
   * <pre>
   * {
   *   &#064;code
   *   // Enable FAQ page to build FAQ and HTML panels
   *   private static XmlPanelBuilderTypeRegistry panelBuilderRegistry = new XmlPanelBuilderTypeRegistry(XmlFaqPanelBuilder.class, XmlHtmlPanelBuilder.class);
   * }
   * </pre>
   *
   * @author George Norman
   */
  public static class XmlPanelBuilderTypeRegistry {
    private Map<String, Class<? extends AbstractXmlPanelBuilder>> builderRegistry = new HashMap<String, Class<? extends AbstractXmlPanelBuilder>>();

    /**
     * Builds a registry from the given <code>builderTypes</code>, using the <code>XmlPanelBuilderAnnotation</code> of
     * each <code>builderType</code> as the panel type name.
     *
     * @param builderTypes
     */
    public XmlPanelBuilderTypeRegistry(Class<? extends AbstractXmlPanelBuilder>... builderTypes) {
      for (Class<? extends AbstractXmlPanelBuilder> builderType : builderTypes) {
        registerBuilderType(builderType);
      }
    }

    /**
     * Registers the given <code>builderTypes</code>, using the <code>XmlPanelBuilderAnnotation</code> from the
     * <code>builderType</code> as the panel type name.
     *
     * @param builderTypes
     */
    public final Class<? extends AbstractXmlPanelBuilder> registerBuilderType(Class<? extends AbstractXmlPanelBuilder> builderType) {
      XmlPanelBuilderAnnotation builderAnnotation = builderType.getAnnotation(XmlPanelBuilderAnnotation.class);

      return builderRegistry.put(builderAnnotation.panelTypeName(), builderType);
    }

    /**
     * Registers the given <code>builderTypes</code>, using the given <code>panelTypeName</code> as the name (ignores
     * the <code>XmlPanelBuilderAnnotation</code> from the <code>builderType</code>).
     *
     * @param builderTypes
     */
    public final Class<? extends AbstractXmlPanelBuilder> registerBuilderType(String panelTypeName, Class<? extends AbstractXmlPanelBuilder> builderType) {
      return builderRegistry.put(panelTypeName, builderType);
    }

    /**
     * Create a panel builder of the type identified by panelTypeName, using the given panelNode as the XML source.
     *
     * @throws ClassUtilsException
     */
    public AbstractXmlPanelBuilder createBuilder(String panelTypeName, InfoNodeElement panelNode) throws ClassUtilsException {
      Class<? extends AbstractXmlPanelBuilder> builderType = builderRegistry.get(panelTypeName);
      AbstractXmlPanelBuilder result = ClassUtils.instanceFrom(builderType, new Class[] { InfoNodeElement.class }, new InfoNodeElement[] { panelNode });

      return result;
    }
  }

  // ============================================================================
  // XmlPanelSetBuilder
  // ============================================================================

  /**
   * Builds a <code>PanelSet</code> from the given <code>panelSetNode</code>.
   *
   * @param panelSetNode node containing the set of panels (each child of this node will be a panel node).
   * @param panelBuilderTypeRegistry registry that specifies which builder to use for a given panel definition (based on
   * panel name - e.g., 'listPanel').
   */
  public XmlPanelSetBuilder(InfoNodeElement panelSetNode, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this.panelSetNode = panelSetNode;
    this.panelBuilderTypeRegistry = panelBuilderTypeRegistry;
  }

  @Override
  public PanelSet build() throws Exception {
    PanelSet result = null;

    result = new PanelSet(panelSetNode.getAttributeValue(ID));
    @SuppressWarnings("unchecked")
    List<InfoNodeElement> children = panelSetNode.getChildren();

    for (InfoNodeElement panelNode : children) {
      PanelBuilder panelBuilder = panelBuilderTypeRegistry.createBuilder(panelNode.getName(), panelNode);

      if (panelBuilder == null) {
        result.addPanel(new HtmlPanel("error", "Panel ERROR", null, "PanelBuilder not found for panel type " + panelNode.getName()));
      } else {
        result.addPanel(panelBuilder.build());
      }
    }

    return result;
  }

}
