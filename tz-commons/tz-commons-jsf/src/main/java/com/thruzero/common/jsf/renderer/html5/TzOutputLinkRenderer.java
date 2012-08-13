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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.FacesRenderer;

import com.sun.faces.renderkit.html_basic.OutputLinkRenderer;
import com.thruzero.common.core.support.SimpleIdGenerator;
import com.thruzero.common.jsf.components.html5.JsfHtml5Component;
import com.thruzero.common.jsf.renderer.html5.helper.TzResponseWriter;

/**
 * Adds configurable pass-through attributes to the TzOutputLink component.
 *
 * @author George Norman
 */
@FacesRenderer(componentFamily = JsfHtml5Component.COMPONENT_FAMILY, rendererType = TzOutputLinkRenderer.RENDERER_TYPE)
public class TzOutputLinkRenderer extends OutputLinkRenderer {
  public static final String RENDERER_TYPE = JsfHtml5Component.COMPONENT_FAMILY + ".TzOutputLinkRenderer";

  @Override
  public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
    context.setResponseWriter(new TzResponseWriter(context.getResponseWriter(), this));

    super.encodeBegin(context, component);
  }

  @Override
  public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
    super.encodeEnd(context, component);

    context.setResponseWriter(((TzResponseWriter)context.getResponseWriter()).getWrapped());
  }

  /**
   * TODO-p1(george) - GROSS hack to FORCE JQuery to refresh the page.
   *
   * Add the refreshHack param to the parameter list if the refresh attribute is set to true. The refreshHack
   * parameter contains a unique number, which will force JQuery to refreshHack the page.
   */
  @Override
  protected Param[] getParamList(UIComponent command) {
    Param[] result;
    Param[] definedParams = super.getParamList(command);
    Object attributeValue = command.getAttributes().get("refreshHack");

    if (attributeValue != null && Boolean.valueOf(attributeValue.toString())) {
      Param refreshParam = new Param("refreshHack", SimpleIdGenerator.getInstance().getNextIdAsString());
      if (definedParams == null) {
        result = new Param[] {refreshParam};
      } else {
        List<Param> parameterList = new ArrayList<Param>(Arrays.asList(definedParams));
        parameterList.add(refreshParam);
        result = parameterList.toArray(new Param[parameterList.size()]);
      }
    } else {
      result = definedParams;
    }

    return result;
  }

}
