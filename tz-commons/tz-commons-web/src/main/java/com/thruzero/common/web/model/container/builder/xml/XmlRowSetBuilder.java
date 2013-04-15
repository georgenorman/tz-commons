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
import com.thruzero.common.web.model.container.RowSet;
import com.thruzero.common.web.model.container.builder.RowSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.XmlPanelBuilderTypeRegistry;

/**
 * A builder of PanelSet instances, using XML as the definition. Below is a sample &lt;panelSet&gt; node containing the set of panel nodes (in presentation
 * order). The XmlPanelSetBuilder will parse the XML and build each panel and add it to the result.
 * 
 * <pre>
 * {@code
 * <index>
 *  <rowSet id="row1">
 *    <panelSet id="col1">
 *     <listPanel title="Panel-1" id="col1_panel1">
 *       <dataList>
 *         <a href="item11.html" title="Item-11"/>
 *         <a href="item12.html" title="Item-12"/>
 *         <a href="item13.html" title="Item-13"/>
 *       </dataList>
 *     </listPanel>
 *
 *     <listPanel title="Panel-2" id="col1_panel2">
 *       <dataList>
 *         <a href="item21.html" title="Item-21"/>
 *         <a href="item22.html" title="Item-22"/>
 *       </dataList>
 *     </listPanel>
 *    </panelSet>
 *  </rowSet>
 *   ...
 *
 * </index>
 * }
 * </pre>
 * 
 * @author George Norman
 */
public class XmlRowSetBuilder implements RowSetBuilder {
  private static final String ID = ConfigLocator.locate().getValue(XmlRowSetBuilder.class.getName(), "id", "id");

  private String rowSetId;
  private List<InfoNodeElement> panelSetNodes;
  private XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry;

  /**
   * Builds a <code>PanelSet</code> from the given <code>panelSetNode</code>.
   * 
   * @param panelSetNode
   *          node containing the set of panels (each child of this node will be a panel node).
   * @param panelBuilderTypeRegistry
   *          registry that specifies which builder to use for a given panel definition (based on panel name - e.g., 'listPanel').
   */
  @SuppressWarnings("unchecked")
  public XmlRowSetBuilder(InfoNodeElement rowSetNode, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this(rowSetNode.getAttributeValue(ID), rowSetNode.getChildren(), panelBuilderTypeRegistry);
  }

  public XmlRowSetBuilder(String rowSetId, List<InfoNodeElement> panelSetNodes, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this.rowSetId = rowSetId;
    this.panelSetNodes = panelSetNodes;
    this.panelBuilderTypeRegistry = panelBuilderTypeRegistry;
  }

  @Override
  public RowSet build() throws Exception {
    RowSet result = new RowSet(rowSetId);

    for (InfoNodeElement panelSetNode : panelSetNodes) {
      XmlPanelSetBuilder panelSetBuilder = new XmlPanelSetBuilder(panelSetNode, panelBuilderTypeRegistry);

      result.addPanelSet(panelSetBuilder.build());
    }

    return result;
  }

}
