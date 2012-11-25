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
 * Abstract base class for a UI panel.
 *
 * @author George Norman
 */
public abstract class AbstractPanel {
  private String id;
  private String title;
  private StyleClass headerStyleClass;

  public AbstractPanel(String id, String title, StyleClass headerStyleClass) {
    this.id = id;
    this.title = title;
    this.headerStyleClass = headerStyleClass;
  }

  /**
   * Returns the ID of this panel.
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the text used to render the panel's title in the header.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the optional style class used to render the panel's header.
   */
  public String getHeaderStyleClass() {
    return headerStyleClass == null ? "" : headerStyleClass.toString();
  }

  /**
   * Returns the simple class name of this instance (used to determine its renderer).
   */
  public String getPanelClass() {
    return getClass().getSimpleName();
  }
}
