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
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

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
 * A builder of PanelSet instances, using XML as the definition. Below is a sample with an optional table of contents
 * (used for panel filtering and ordering), followed by the panel definitions (in any order). The XmlPanelSetBuilder
 * will parse the XML, looking for specific panel definitions and then build each panel and add it to the result.
 *
 * <pre>
 * {@code
 * <index>
 *   <toc>
 *     <filterOrder>
 *       <col>col1_panel1|col1_panel2</col>
 *       <col>col2_panel1|col2_panel2|col2_panel3</col>
 *     </filterOrder>
 *   </toc>
 *
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
  private static final String PANEL_ID = ConfigLocator.locate().getValue(PanelSet.class.getName(), "id", "id");
  private static final String PANEL_PATH_ID = ConfigLocator.locate().getValue(PanelSet.class.getName(), "panelId", "/index/*[ends-with(name(),'Panel')]");

  private String[] panelIds;
  private InfoNodeElement contentNode;
  private XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry;

  // ------------------------------------------------------
  // XmlPanelBuilderTypeRegistry
  // ------------------------------------------------------

  /**
   * A registry of XML-based panel builders, that maps panel names (e.g., 'listPanel') to panel builder types (e.g.,
   * 'XmlListPanelBuilder').
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
     * Registers the given <code>builderTypes</code>, using the given <code>panelTypeName</code> as the name (ignores the
     * <code>XmlPanelBuilderAnnotation</code> from the <code>builderType</code>).
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
   * Builds a <code>PanelSet</code> from the given <code>contentNode</code>, using the given <code>tocColumnNode</code>
   * to determine which panels from the <code>contentNode</code> to include in the result.
   *
   * @param tocColumnNode column node from the table of contents (toc) node that determines the set of panels to build.
   * @param contentNode node containing all of the panel definitions.
   * @param panelBuilderTypeRegistry registry that specifies which builder to use for a given panel definition (based on
   * panel name - e.g., 'listPanel').
   */
  public XmlPanelSetBuilder(InfoNodeElement tocColumnNode, InfoNodeElement contentNode, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this(StringUtils.isEmpty(tocColumnNode.getValue()) ? null : tocColumnNode.getValue().split("\\|"), contentNode, panelBuilderTypeRegistry);
  }

  /**
   * Builds a <code>PanelSet</code> from the given <code>contentNode</code>, using the given <code>panelIds</code> to
   * determine which panels from the <code>contentNode</code> to include in the result.
   *
   * @param panelIds determines the set of panels to build.
   * @param contentNode node containing all of the panel definitions.
   * @param panelBuilderTypeRegistry registry that specifies which builder to use for a given panel definition (based on
   * panel name - e.g., 'listPanel').
   */
  public XmlPanelSetBuilder(String[] panelIds, InfoNodeElement contentNode, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this.panelIds = panelIds;
    this.contentNode = contentNode;
    this.panelBuilderTypeRegistry = panelBuilderTypeRegistry;
  }

  @Override
  public PanelSet build() throws Exception {
    PanelSet result = null;

    if (panelIds != null) {
      result = new PanelSet(stringArrayToId(panelIds));
      for (String panelId : panelIds) {
        InfoNodeElement panelNode = null;
        try {
          panelNode = contentNode.findElement(PANEL_PATH_ID + "[@" + PANEL_ID + "='" + panelId + "']");
        } catch (Exception e) {
          // ignore (node will be null and is handled below).
        }

        if (panelNode == null) {
          result.addPanel(new HtmlPanel("error", "Panel ERROR - " + panelId, null, "Content not found for panel named: " + panelId));
        } else {
          PanelBuilder panelBuilder = panelBuilderTypeRegistry.createBuilder(panelNode.getName(), panelNode);
          result.addPanel(panelBuilder.build());
        }
      }
    }

    return result;
  }

  /**
   * Convert the given array into a panel Id.
   */
  protected String stringArrayToId(String[] sa) {
    StringBuilder sb = new StringBuilder();
    String separator = "";

    for (String string : sa) {
      sb.append(separator).append(string);
      separator = ".";
    }

    return sb.toString();
  }

}
