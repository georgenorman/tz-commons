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

import com.thruzero.common.web.util.WebUtils;

/**
 * An instance represents an ordered set of panels (typically used to render a column of panels on a page).
 *
 * @author George Norman
 */
public class PanelSet {
  private String id;
  private Map<String, AbstractPanel> panels = new LinkedHashMap<String, AbstractPanel>();

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
    return WebUtils.uiRepeatHack(panels.values());
  }
}
