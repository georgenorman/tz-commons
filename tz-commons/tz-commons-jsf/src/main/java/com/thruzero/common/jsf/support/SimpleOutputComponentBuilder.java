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
package com.thruzero.common.jsf.support;

import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.jsf.utils.FacesUtils;
import com.thruzero.common.web.model.css.Styles;

/**
 * Builds standard JSF output components (e.g., HtmlOutputLink, HtmlOutputText).
 *
 * @author George Norman
 */
public class SimpleOutputComponentBuilder {

  public HtmlOutputLink createOutputLink(String url, String linkText, String onClick, Styles styles) {
    HtmlOutputLink result = createOutputLink(url, linkText, styles);

    result.setOnclick(onClick);

    return result;
  }

  public HtmlOutputLink createOutputLink(String url, String linkText, Styles styles) {
    HtmlOutputLink result = new HtmlOutputLink();

    if (url.startsWith("/")) {
      result.setValue("/" + FacesUtils.getServletContextName() + url);
    } else {
      result.setValue(url);
    }
    result.getChildren().add(createOutputText(linkText, styles));

    return result;
  }

  public HtmlOutputText createOutputText(String text, Styles styles) {
    HtmlOutputText result = new HtmlOutputText();

    result.setValue(text);

    if (StringUtils.isNotEmpty(styles.getInlineStyle())) {
      result.setStyle(styles.getInlineStyle());
    }
    if (StringUtils.isNotEmpty(styles.getStyleClass())) {
      result.setStyleClass(styles.getStyleClass());
    }

    return result;
  }

}
