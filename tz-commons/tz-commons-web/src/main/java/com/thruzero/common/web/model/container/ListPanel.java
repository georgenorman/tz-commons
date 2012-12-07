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

package com.thruzero.common.web.model.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.web.model.css.StyleClass;
import com.thruzero.common.web.util.WebUtils;

/**
 * An instance represents a panel containing a list of items. Each item in the list is an InfoNodeElement, which can
 * represent the model of a simple property sheet item, a hierarchical list item, etc.
 * <p/>
 * The panel may be built manually or using an
 * {@link com.thruzero.common.web.model.container.builder.xml.XmlListPanelBuilder XmlListPanelBuilder} which creates the
 * panel from a source InfoNodeElement. As an example, the following XML would build a hierarchical bullet list panel:
 *
 * <pre>
 * {@code
 * <listPanel title="Java Articles">
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
public class ListPanel extends AbstractPanel {
  private List<InfoNodeElement> items = new ArrayList<InfoNodeElement>();

  public ListPanel(String id, String title, StyleClass headerStyleClass) {
    super(id, title, headerStyleClass);
  }

  /**
   * Return the list of items ({@link com.thruzero.common.core.infonode.InfoNodeElement InfoNodeElement}), each of which
   * defines one top-level item as well as zero or more sub-items.
   *
   * @return
   */
  public Collection<InfoNodeElement> getItems() {
    return WebUtils.uiRepeatHack(Collections.unmodifiableCollection(items));
  }

  public void addItem(InfoNodeElement panelItem) {
    items.add(panelItem);
  }
}
