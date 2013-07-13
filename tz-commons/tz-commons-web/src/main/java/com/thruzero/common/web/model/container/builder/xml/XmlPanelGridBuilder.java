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
package com.thruzero.common.web.model.container.builder.xml;

import java.util.List;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.web.model.container.PanelGrid;
import com.thruzero.common.web.model.container.builder.PanelGridBuilder;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.XmlPanelBuilderTypeRegistry;

/**
 * A builder of PanelSet instances, using XML as the definition. Below is a sample &lt;panelSet&gt; node containing the set of panel nodes (in presentation
 * order). The XmlPanelSetBuilder will parse the XML and build each panel and add it to the result.
 * 
 * <pre>
 * {@code
 * <index>
 *  <panelGrid id="row1">
 *    <panelSet id="col1">
 *     <listPanel title="Panel-1" id="col1_panel1">
 *       <dataList>
 *         <a href="item1-p1c1.html" title="Item-1-p1c1"/>
 *         <a href="item2-p1c1.html" title="Item-2-p1c1"/>
 *         <a href="item3-p1c1.html" title="Item-3-p1c1"/>
 *       </dataList>
 *     </listPanel>
 *
 *     <listPanel title="Panel-2" id="col1_panel2">
 *       <dataList>
 *         <a href="item1-p2c1.html" title="Item-1-p2c1"/>
 *         <a href="item2-p2c1.html" title="Item-2-p2c1"/>
 *       </dataList>
 *     </listPanel>
 *    </panelSet>
 *    
 *    <panelSet id="col2">
 *     <listPanel title="Panel-1" id="col2_panel1">
 *       <dataList>
 *         <a href="item1-p1c2.html" title="Item-1-p1c2"/>
 *       </dataList>
 *     </listPanel>
 *
 *  </panelGrid>
 *   ...
 *
 * </index>
 * }
 * </pre>
 * 
 * @author George Norman
 */
public class XmlPanelGridBuilder implements PanelGridBuilder {
  private static final String ID = ConfigLocator.locate().getValue(XmlPanelGridBuilder.class.getName(), "id", "id");

  private final String rowId;
  private final List<InfoNodeElement> panelSetNodes;
  private final XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry;

  /**
   * Builds a <code></code> from the given <code>panelGridNode</code>.
   * 
   * @param panelGridNode
   *          node containing the list of PanelSet nodes, each of which represents a column in the row.
   * @param panelBuilderTypeRegistry
   *          registry that specifies which builder to use for a given panel definition (based on panel name - e.g., 'listPanel').
   */
  @SuppressWarnings("unchecked")
  public XmlPanelGridBuilder(InfoNodeElement panelGridNode, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this(panelGridNode.getAttributeValue(ID), panelGridNode.getChildren(), panelBuilderTypeRegistry);
  }

  public XmlPanelGridBuilder(String rowId, List<InfoNodeElement> panelSetNodes, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this.rowId = rowId;
    this.panelSetNodes = panelSetNodes;
    this.panelBuilderTypeRegistry = panelBuilderTypeRegistry;
  }

  @Override
  public PanelGrid build() throws Exception {
    PanelGrid result = new PanelGrid(rowId);

    for (InfoNodeElement panelSetNode : panelSetNodes) {
      XmlPanelSetBuilder panelSetBuilder = new XmlPanelSetBuilder(panelSetNode, panelBuilderTypeRegistry);

      result.addPanelSet(panelSetBuilder.build());
    }

    return result;
  }

}
