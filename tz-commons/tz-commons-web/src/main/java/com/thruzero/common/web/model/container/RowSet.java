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
 * An instance represents an ordered set of rows (typically used to render a row of panels on a page).
 * 
 * @author George Norman
 */
public class RowSet {
  private String id;
  private Map<String, PanelSet> panelSets = new LinkedHashMap<String, PanelSet>();

  public RowSet(String rowId) {
    this.id = rowId;
  }

  public String getId() {
    return id;
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
    return numCols / getNumberOfPanelSets();
  }

}
