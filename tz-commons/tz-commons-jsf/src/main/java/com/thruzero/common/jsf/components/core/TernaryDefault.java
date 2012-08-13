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
package com.thruzero.common.jsf.components.core;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;


/**
 * A JSF tag that represents the ternary operator (see the '?' operator in Java). It provides an easy way to set
 * defaults for page fragments. For example if a client were to use the basicListView tag, but did not set the
 * rendered attribute, then the basicListView facelet code could do the following:
 *
 * <pre>
 *   &lt;tzc:ternaryDefault var=&quot;basicListView_rendered&quot; condition=&quot;#{empty rendered}&quot;
 *      valueIfTrue=&quot;true&quot; valueIfFalse=&quot;#{rendered}&quot; /&gt;
 * </pre>
 *
 * @author George Norman
 */
@FacesComponent(TernaryDefault.COMPONENT_TYPE)
public class TernaryDefault extends UIComponentBase implements JsfCoreComponent {
  public static final String COMPONENT_TYPE = JsfCoreComponent.COMPONENT_FAMILY + ".TernaryDefault";

  @Override
  public String getFamily() {
    return JsfCoreComponent.COMPONENT_FAMILY;
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    String var = (String)getAttributes().get("var");
    ValueExpression condition = getValueExpression("condition");
    Object conditionResult = condition.getValue(getFacesContext().getELContext());
    Object valueIfTrue = getAttributes().get("valueIfTrue");
    Object valueIfFalse = getAttributes().get("valueIfFalse");

    if (Boolean.valueOf(conditionResult.toString())) {
      context.getExternalContext().getRequestMap().put(var, valueIfTrue);
    } else {
      context.getExternalContext().getRequestMap().put(var, valueIfFalse);
    }
  }
}
