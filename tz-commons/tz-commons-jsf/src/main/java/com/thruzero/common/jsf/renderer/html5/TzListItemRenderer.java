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
package com.thruzero.common.jsf.renderer.html5;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;

import org.apache.commons.lang3.StringUtils;

import com.sun.faces.renderkit.html_basic.HtmlBasicRenderer;
import com.sun.faces.util.Util;
import com.thruzero.common.jsf.components.html5.JsfHtml5Component;
import com.thruzero.common.jsf.renderer.html5.helper.TzResponseWriter;

/**
 * Adds configurable pass-through attributes to the TzListItem component.
 *
 * @author George Norman
 */
@FacesRenderer(componentFamily = JsfHtml5Component.COMPONENT_FAMILY, rendererType = TzListItemRenderer.RENDERER_TYPE)
public class TzListItemRenderer extends HtmlBasicRenderer {
  public static final String RENDERER_TYPE = JsfHtml5Component.COMPONENT_FAMILY + ".TzListItemRenderer";

  @Override
  public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    context.setResponseWriter(new TzResponseWriter(context.getResponseWriter(), this));

    super.encodeBegin(context, component);

    ResponseWriter writer = context.getResponseWriter();

    String itemType = (String)component.getAttributes().get("itemType");
    writer.startElement(StringUtils.isEmpty(itemType) ? "li" : itemType, component);

    String style = (String)component.getAttributes().get("style");
    if (StringUtils.isNotEmpty(style)) {
      writer.writeAttribute("style", style, "style");
    }

    String styleClass = (String)component.getAttributes().get("styleClass");
    if (StringUtils.isNotEmpty(styleClass)) {
      writer.writeAttribute("class", styleClass, "styleClass");
    }

    writer.flush();
  }

  @Override
  public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
    rendererParamsNotNull(context, component);

    if (!shouldEncodeChildren(component)) {
      return;
    }

    if (component.getChildCount() > 0) {
      for (UIComponent kid : component.getChildren()) {
        encodeRecursive(context, kid);
      }
    }
  }

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    super.encodeEnd(context, component);

    context.setResponseWriter(((TzResponseWriter)context.getResponseWriter()).getWrapped());
  }

  @Override
  protected void getEndTextToRender(FacesContext context, UIComponent component, String currentValue) throws IOException {
    String itemType = (String)component.getAttributes().get("itemType");
    ResponseWriter writer = context.getResponseWriter();
    writer.endElement(StringUtils.isEmpty(itemType) ? "li" : itemType);
  }

  @Override
  protected Object getValue(UIComponent component) {
    if (Util.componentIsDisabled(component)) {
      return null;
    } else {
      return ((UIOutput)component).getValue();
    }
  }

}
