/*
 *   Copyright 2011-2012 George Norman
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

import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

import com.thruzero.common.core.support.EnvironmentHelper;

/**
 * Default brand bean - place-holder for allowing brand-based customization.
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name = "brandBean")
@javax.faces.bean.ApplicationScoped
public class BrandBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private String environmentTag;

  public BrandBean() {
    // Environment
    if (EnvironmentHelper.getInstance().isDevEnvironment()) {
      environmentTag = "- DEV -"; // development environment
    } else if (EnvironmentHelper.getInstance().isPreEnvironment()) {
      environmentTag = "- PRE -"; // pre-production environment
    } else {
      environmentTag = ""; // production environment
    }
  }

  public String getEnvironmentTag() {
    return environmentTag;
  }

  public String getAppVersion() {
    String result;
    Properties props = new Properties();

    try {
      InputStream inputStream = BrandBean.class.getClassLoader().getResourceAsStream("resources/version.properties");
      props.load(inputStream);
      Object buildVersion = props.get("build.version");
      result = (buildVersion == null) ? "???" : "v" + buildVersion.toString();
    } catch (Exception e) {
      result = "???";
    }

    return result;
  }

}
