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

import java.util.ArrayList;
import java.util.List;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.support.SimpleIdGenerator;
import com.thruzero.common.web.model.container.PanelSet;
import com.thruzero.common.web.model.container.builder.CarouselPanelSetBuilder;
import com.thruzero.common.web.model.container.builder.PanelSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.XmlPanelBuilderTypeRegistry;

/**
 * A builder of a list of PanelSet instances, using XML as the definition. Below is a sample &lt;carouselPanelSet&gt; node containing a
 * set of panel nodes (in presentation order). The XmlCarouselPanelSetBuilder will parse the XML and build each panel and then add it
 * to the result list.
 *
 * <pre>
 * {@code
 * <index>
 *   <carouselPanelSet id="rssFeedSet" paginate="3">
 *     <rssFeedPanel id="main010" title="Slashdot" titleLink="http://slashdot.org/" size="4" quoteTooltipsCount="1" refreshRate="1">
 *       http://rss.slashdot.org/Slashdot/slashdot/to
 *     </rssFeedPanel>
 *     <rssFeedPanel id="main011" title="TheServerSide" titleLink="http://www.theserverside.com/" size="8" quoteTooltipsCount="1" refreshRate="4">
 *       http://feeds.pheedo.com/techtarget/tsscom/home
 *     </rssFeedPanel>
 *
 *   ...
 *
 *   </carouselPanelSet>
 * </index>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class XmlCarouselPanelSetBuilder implements CarouselPanelSetBuilder {
  private static final String PAGINATE_ATTRIBUTE_NAME = ConfigLocator.locate().getValue(XmlCarouselPanelSetBuilder.class.getName(), "paginate", "paginate");

  private InfoNodeElement carouselPanelSetNode;
  private XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry;

  /**
   * Builds a list of <code>PanelSet</code> instances from the given <code>carouselPanelSet</code>. The <code>paginate</code>
   * attribute of the <code>carouselPanelSet</code> node, specifies how many panels will be placed in each {@code PanelSet} instance.
   *
   * @param carouselPanelSetNode node containing the set of panels (each child of this node will be a panel node).
   * @param panelBuilderTypeRegistry registry that specifies which builder to use for a given panel definition (based on
   * panel name - e.g., 'listPanel').
   */
  public XmlCarouselPanelSetBuilder(InfoNodeElement carouselPanelSetNode, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this.carouselPanelSetNode = carouselPanelSetNode;
    this.panelBuilderTypeRegistry = panelBuilderTypeRegistry;
  }

  @Override
  public List<PanelSet> build() throws Exception {
    List<PanelSet> result = new ArrayList<PanelSet>();

    // setup
    @SuppressWarnings("unchecked")
    List<InfoNodeElement> children = carouselPanelSetNode.getChildren();
    int paginate = carouselPanelSetNode.getAttributeTransformer(PAGINATE_ATTRIBUTE_NAME).getIntValue(0);
    int panelCount = 0;
    List<InfoNodeElement> panelNodes = new ArrayList<InfoNodeElement>();

    // handle pagination
    for (InfoNodeElement panelNode : children) {
      panelNodes.add(panelNode);

      if (++panelCount % paginate == 0 || panelCount == children.size()) {
        PanelSetBuilder panelSetBuilder = new XmlPanelSetBuilder(SimpleIdGenerator.getInstance().getNextIdAsString(), panelNodes, panelBuilderTypeRegistry);
        result.add(panelSetBuilder.build());

        panelNodes.clear();
      }
    }

    return result;
  }
}
