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

import com.thruzero.common.jsf.utils.FacesUtils;
import com.thruzero.common.web.util.HtmlUtils;

/**
 * Helper bean that optionally adds the faces redirect query param (faces-redirect) and the servlet context (useful for raw anchors).
 *
 * @author George Norman
 */
public class UrlBean {
  private String url;

  /**
   * Creates an updated URL with the context name prepended to it if the given action begins with a "/" and prependContext is true,
   * plus appends the redirect query parameter, "faces-redirect=true", if appendRedirect is true.
   * <p>
   * NOTE: The appendRedirect parameter is critical for iPhone Home Screen apps - must be set to true. Otherwise, it
   * corrupts the Home Screen browser (must stop and restart to fix).
   * <p>
   * Scenario:
   * <ol>
   *   <li>Click the Inputs link
   *   <li>Click the Save button
   *   <li>Click the OK button (when redirect is set to false)
   * </ol>
   * To see the corruption, continue as follows
   * <ol>
   *   <li>Click the Basic Panels link
   *   <li>Click the Home button and notice the jittery slide back to the dashboard.
   * </ol>
   * TODO-p1(george) Research why this happens.
   */
  public UrlBean(String url, boolean appendRedirect) {
    this.url = url;

    if (appendRedirect) {
      this.url = HtmlUtils.appendQueryParam(url, "faces-redirect", "true"); // TODO-p1(george) Assumes that redirect is not already present
    }
  }

  public String getUrl() {
    return url;
  }

  public String getUrlWithContext() {
    String result = url;

    if (url.startsWith("/")) {
      result = FacesUtils.getUrlWithContextName(url); // TODO-p1(george) Assumes that context is not already present
    }

    return result;
  }

}
