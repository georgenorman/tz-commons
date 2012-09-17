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
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.Renderer;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.utils.StringUtilsExt;

/**
 * A custom ResponseWriterWrapper that supports configurable pass-through attributes. Each custom component
 * must define their pass-through attributes in a section named "com.thruzero.common.jsf.renderer.html5.helper.TzResponseWriter".
 * Below is an example of a few JQuery mobile components:
 * <pre>
 * {@literal
 *   <section name="com.thruzero.common.jsf.renderer.html5.helper.TzResponseWriter">
 *     <entry key="com.thruzero.common.jsf.renderer.html5.TzCheckboxRenderer" value="data-mini, data-role, data-theme" />
 *     <entry key="com.thruzero.common.jsf.renderer.html5.TzFormRenderer" value="data-ajax" />
 *     <entry key="com.thruzero.common.jsf.renderer.html5.TzTextRenderer" value="data-mini, data-role, data-theme, data-highlight, placeholder, ..." />
 *     ...
 *   </section>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class TzResponseWriter extends ResponseWriterWrapper {
  ResponseWriter delegate;
  Renderer renderer;

  public TzResponseWriter(ResponseWriter delegate, Renderer renderer) {
    this.delegate = delegate;
    this.renderer = renderer;
  }

  @Override
  public ResponseWriter getWrapped() {
    return delegate;
  }

  @Override
  public void startElement(String name, UIComponent component) throws IOException {
    super.startElement(name, component);

    if (component != null) {
      writeCustomAttributes(component);
    }
  }

  /**
   * Read the value for the given jsfAttribute and if the value is not null, then write the value as the given htmlAttribute.
   * Example, assume the value of the "styleClass" property is "myCssClass". then:
   *
   * <pre>
   * {@code
   *   writeAttribute(component, "class", "styleClass")
   * }
   * </pre>
   *
   * Renders:
   *
   * <pre>
   * {@code
   *   class="myCssClass"
   * }
   * </pre>
   */
  public void writeAttributeValueNotNull(String htmlAttribute, String jsfAttribute, UIComponent component) throws IOException {
    String attributeValue = (String)component.getAttributes().get(jsfAttribute);

    writeAttributeValueNotNull(htmlAttribute, attributeValue, jsfAttribute);
  }

  public void writeAttributeValueNotNull(String attribute, UIComponent component) throws IOException {
    String attributeValue = (String)component.getAttributes().get(attribute);

    writeAttributeValueNotNull(attribute, attributeValue, attribute);
  }

  /**
   * Write the htmlAttribute, for the given jsfAttribute, if the given attributeValue is not null.
   */
  public void writeAttributeValueNotNull(String htmlAttribute, Object attributeValue, String jsfAttribute) throws IOException {
    if (attributeValue != null) {
      writeAttribute(htmlAttribute, attributeValue, jsfAttribute);
    }
  }

  // Convenience functions //////////////////////////////////////////////////////

  public void writeCustomAttributes(UIComponent component) throws IOException {
    String customAttributesStream = ConfigLocator.locate().getValue(TzResponseWriter.class.getName(), renderer.getClass().getName());

    if (StringUtils.isNotEmpty(customAttributesStream)) {
      String[] customAttributes = StringUtilsExt.splitWithTrim(customAttributesStream, ",");

      for (String attribute : customAttributes) {
        writeAttributeValueNotNull(attribute, component);
      }
    }
  }

}
