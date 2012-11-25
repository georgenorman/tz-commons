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

import com.thruzero.common.web.model.css.StyleClass;

/**
 * A simple panel consisting of raw HTML.
 *
 * TODO-p0(george) DANGER - Source of content must be trusted.
 *
 * @author George Norman
 */
public class HtmlPanel extends AbstractPanel {
  private String content;

  /**
   * @param content raw HTML used to render the panel's contents.
   */
  public HtmlPanel(String id, String title, StyleClass headerStyleClass, String content) {
    super(id, title, headerStyleClass);

    this.content = content;
  }

  public String getContent() {
    return content;
  }
}
