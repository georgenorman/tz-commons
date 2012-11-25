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

import java.util.Iterator;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.web.model.container.AbstractPanel;
import com.thruzero.common.web.model.container.ListPanel;
import com.thruzero.common.web.model.container.builder.xml.AbstractXmlPanelBuilder.XmlPanelBuilderAnnotation;

/**
 * An instance represents a builder of ListPanel objects (a panel containing a list of items).
 * <p/>
 * The following XML, if used by an XmlListPanelBuilder, would build a hierarchical bullet list panel:
 *
 * <pre>
 * {@code
 * <listPanel id="articles" title="Java Articles">
 *   <dataList>
 *     <a href="http://www.ibm.com/developerworks/views/java/libraryview.jsp?search_by=practice:" title="DeveloperWorks - Java theory and practice">
 *       <icon>iconStar</icon>
 *       <dataList viewType="ul">
 *         <a href="http://www.ibm.com/developerworks/java/library/j-jtp04223/index.html" title="Urban performance legends">
 *           <fta>Synchronization is really slow...</fta>
 *         </a>
 *         <a href="http://www.ibm.com/developerworks/java/library/j-jtp06197/index.html" title="Managing volatility" />
 *       </dataList>
 *     </a>
 *   </dataList>
 * </listPanel>
 * }
 * </pre>
 *
 * @author George Norman
 */
@XmlPanelBuilderAnnotation(panelTypeName = "listPanel")
public class XmlListPanelBuilder extends AbstractXmlPanelBuilder {
  private static final String CHILD_DATALIST_ID = ConfigLocator.locate().getValue(XmlListPanelBuilder.class.getName(), "dataList", "dataList");

  public XmlListPanelBuilder(InfoNodeElement panelNode) {
    super(panelNode);
  }

  @Override
  public AbstractPanel build() throws Exception {
    ListPanel result = new ListPanel(getPanelId(), getPanelTitle(), getPanelHeaderStyleClass());
    InfoNodeElement dataListNode = getPanelNode().findElement(CHILD_DATALIST_ID);

    if (dataListNode != null) {
      for (Iterator<? extends InfoNodeElement> iter = dataListNode.getChildNodeIterator(); iter.hasNext(); ) {
        result.addItem(iter.next());
      }
    }

    return result;
  }
}
