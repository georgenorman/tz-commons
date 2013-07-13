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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.thruzero.common.core.utils.UiUtils;

/**
 * An instance represents an ordered set of panels. The layout of the panels in the PanelSet 
 * can be horizontal or vertical and depends on the renderer. Examples:
 * 
 * <pre>
 * Horizontal
 *   [panel-1] [panel-2] [panel-3]
 *   
 * Vertical
 *   [panel-1]
 *   [panel-2]
 *   [panel-3]
 * </pre>
 * 
 * @author George Norman
 */
public class PanelSet {
  private final String id;
  private final Map<String, AbstractPanel> panels = new LinkedHashMap<String, AbstractPanel>();

  public PanelSet(String columnId) {
    this.id = columnId;
  }

  public String getId() {
    return id;
  }

  public void addPanel(AbstractPanel panel) {
    panels.put(panel.getId(), panel);
  }

  public AbstractPanel getPanel(String panelId) {
    return panels.get(panelId);
  }

  public Collection<? extends AbstractPanel> getPanels() {
    return UiUtils.unmodifiableListHack(panels.values());
  }

  public int getNumberOfPanels() {
    return panels.size();
  }
}
