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
import java.util.Map;

import com.thruzero.common.core.locator.ConfigLocator;

/**
 * Reads in a set of supported themes, from the config file, and provides them to a JSF page.
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="themeSwitcherBean")
@javax.faces.bean.RequestScoped
public class ThemeSwitcherBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private static Map<String, String> themes = ConfigLocator.locate().getSection(ThemeSwitcherBean.class.getName());

  private String theme;

  public Map<String, String> getThemes() {
    return themes;
  }

  public String getTheme() {
    return theme;
  }
  public void setTheme(String theme) {
    this.theme = theme;
  }
}
