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
package com.thruzero.common.jsf.renderer.html5.helper;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.apache.commons.lang3.StringUtils;

/**
 * Some JQuery components (e.g., HTML5 input types such as password, number, email, tel)
 * will not get rendered unless the text type attribute is set to the proper type.
 *
 * @author George Norman
 */
public class TzInputTypeResponseWriter extends TzResponseWriter {
  private String type;

  public TzInputTypeResponseWriter(ResponseWriter delegate, UIComponent component, Renderer renderer) {
    this(delegate, component, renderer, null);
  }

  public TzInputTypeResponseWriter(ResponseWriter delegate, UIComponent component, Renderer renderer, String defaultType) {
    super(delegate, renderer);

    type = (String)component.getAttributes().get("type");

    if (StringUtils.isEmpty(type) && StringUtils.isNotEmpty(defaultType)) {
      type = defaultType;
    }
  }

  @Override
  public void writeAttribute(String name, Object value, String property) throws IOException {
    if ("type".equals(name)) {
      super.writeAttribute(name, type, property);
    } else {
      super.writeAttribute(name, value, property);
    }
  }

}
