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
package com.thruzero.common.web.model.container;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.thruzero.common.core.utils.UiUtils;

/**
 * An instance represents a row of PanelSet instances. The layout of the panels in the PanelSet 
 * can be horizontal or vertical and depends on the renderer.
 * Here's an example of a PanelGrid with two columns, and the PanelSet layout is vertical (so each PanelSet represents a column):
 * <pre>
 *  ---------------------   ---------------------
 * | PanelSet-1 panel-1  | | PanelSet-2 panel-1  |
 *  ---------------------   ---------------------
 * | PanelSet-1 panel-2  | | PanelSet-2 panel-2  |
 *  ---------------------   ---------------------
 * | PanelSet-1 panel-3  |
 *  ---------------------
 * </pre>
 * 
 * Here's the same set, but the PanelSet layout is horizontal (so each panel in the PanelSet represents a column):
 * <pre>
 *  ---------------------   ---------------------   ---------------------
 * | PanelSet-1 panel-1  | | PanelSet-1 panel-2  | | PanelSet-1 panel-3  |
 *  ---------------------   ---------------------   ---------------------
 * | PanelSet-2 panel-1  | | PanelSet-2 panel-2  | 
 *  ---------------------   ---------------------  
 * </pre>
 * 
 * @author George Norman
 */
public class PanelGrid {
  private final String id;
  private final String layout; // horizontal or vertical
  
  private final Map<String, PanelSet> panelSets = new LinkedHashMap<String, PanelSet>();
  
  public interface Layout {
    String HORIZONTAL = "horizontal";
    String VERTICAL = "vertical";
  }

  public PanelGrid(String rowId) {
    this.id = rowId;
    this.layout = Layout.VERTICAL;
  }

  public PanelGrid(String rowId, String layout) {
    this.id = rowId;
    this.layout = layout;
  }

  public String getId() {
    return id;
  }

  public String getLayout() {
    return layout;
  }

  public void addPanelSet(PanelSet panelSet) {
    panelSets.put(panelSet.getId(), panelSet);
  }

  public PanelSet getPanelSet(String panelSetId) {
    return panelSets.get(panelSetId);
  }

  public Collection<PanelSet> getPanelSets() {
    return UiUtils.unmodifiableListHack(panelSets.values());
  }

  public int getNumberOfPanelSets() {
    return panelSets.size();
  }

  public int getColumnSpan(int numCols) {
    if (Layout.VERTICAL.equals(layout)) {
      return numCols / getNumberOfPanelSets();
    } else {
      return numCols / panelSets.values().iterator().next().getNumberOfPanels();
    }
  }

}
