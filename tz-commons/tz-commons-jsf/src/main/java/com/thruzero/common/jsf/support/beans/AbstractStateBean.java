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
package com.thruzero.common.jsf.support.beans;

import java.io.Serializable;
import java.util.Locale;

/**
 * A basic state bean for common application properties.
 *
 * @author George Norman
 */
public class AbstractStateBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private String theme;
  private Locale locale;

  public AbstractStateBean() {
  }

  public AbstractStateBean(String theme, Locale locale) {
    this.theme = theme;
    this.locale = locale;
  }

  public String getTheme() {
    return theme;
  }

  public void setTheme(String theme) {
    this.theme = theme;
  }

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

}
