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
package com.thruzero.common.jsf.support.components;

import java.io.IOException;

import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;

/**
 * A simple conditional tag that evaluates a condition and if true, sets the given var to valueIfTrue, otherwise sets it
 * to valueIfFalse. Useful for setting default values in facelets, if an optional parameter is empty.
 * <p>
 * The example below sets the "standardFooter_spacerHeight" var to "0" if the given spacerHeight is empty,
 * otherwise, sets it to #{spacerHeight}.
 * 
 * <pre>
 * &lt;tzi:ternaryDefault var="standardFooter_spacerHeight" condition="#{empty spacerHeight}" valueIfTrue="0" valueIfFalse="#{spacerHeight}" /&gt;
 * </pre>
 * 
 * @author George Norman
 */
@FacesComponent("components.TernaryDefault")
public class TernaryDefault extends UIComponentBase {

  @Override
  public String getFamily() {
    return TzComponent.COMPONENT_FAMILY;
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    String var = (String)getAttributes().get("var");
    ValueExpression condition = getValueExpression("condition");
    Object conditionResult = condition.getValue(getFacesContext().getELContext());
    String valueIfTrue = (String)getAttributes().get("valueIfTrue");
    String valueIfFalse = (String)getAttributes().get("valueIfFalse");

    if (Boolean.valueOf(conditionResult.toString())) {
      context.getExternalContext().getRequestMap().put(var, valueIfTrue);
    } else {
      context.getExternalContext().getRequestMap().put(var, valueIfFalse);
    }
  }
}
