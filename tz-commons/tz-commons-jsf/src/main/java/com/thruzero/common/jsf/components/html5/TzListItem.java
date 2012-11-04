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
package com.thruzero.common.jsf.components.html5;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;

import com.thruzero.common.jsf.renderer.html5.TzListItemRenderer;

/**
 * Simple list item component (e.g., &lt;li&gt;, &lt;dt&gt;, &lt;dd&gt;, etc).
 *
 * @author George Norman
 */
@FacesComponent(TzListItem.COMPONENT_TYPE)
public class TzListItem extends UIOutput implements JsfHtml5Component {
  public static final String COMPONENT_TYPE = JsfHtml5Component.COMPONENT_FAMILY + ".TzListItem";

  protected enum PropertyKeys {
    itemType, style, styleClass
  }

  public TzListItem() {
    setRendererType(TzListItemRenderer.RENDERER_TYPE);
  }

  @Override
  public String getFamily() {
    return JsfHtml5Component.COMPONENT_FAMILY;
  }

  @Override
  public String getRendererType() {
    return TzListItemRenderer.RENDERER_TYPE;
  }

  public String getItemType() {
    return (String)getStateHelper().eval(PropertyKeys.itemType);
  }

  public void setItemType(String itemType) {
    getStateHelper().put(PropertyKeys.itemType, itemType);
  }

  public String getStyle() {
    return (String)getStateHelper().eval(PropertyKeys.style);
  }

  public void setStyle(String style) {
    getStateHelper().put(PropertyKeys.style, style);
  }

  public String getStyleClass() {
    return (String)getStateHelper().eval(PropertyKeys.styleClass);
  }

  public void setStyleClass(String styleClass) {
    getStateHelper().put(PropertyKeys.styleClass, styleClass);
  }

}
