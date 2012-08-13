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
import javax.faces.component.html.HtmlForm;

import com.thruzero.common.jsf.renderer.html5.TzFormRenderer;

/**
 * A Form component using TzFormRenderer to add configurable pass-through attributes.
 *
 * @author George Norman
 */
@FacesComponent(TzForm.COMPONENT_TYPE)
public class TzForm extends HtmlForm implements JsfHtml5Component {
  public static final String COMPONENT_TYPE = JsfHtml5Component.COMPONENT_FAMILY + ".TzForm";

  @Override
  public String getFamily() {
    return JsfHtml5Component.COMPONENT_FAMILY;
  }

  @Override
  public String getRendererType() {
    return TzFormRenderer.RENDERER_TYPE;
  }

}
