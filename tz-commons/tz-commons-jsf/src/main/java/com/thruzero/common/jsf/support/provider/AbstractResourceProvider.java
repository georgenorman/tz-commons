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
package com.thruzero.common.jsf.support.provider;

import java.util.Locale;
import java.util.ResourceBundle;

import com.thruzero.common.core.locator.Initializable;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.locator.LocatorUtils;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.provider.ResourceProvider;

/**
 * An abstract base class and basic implementation of the ResourceProvider interface.
 *
 * @author George Norman
 */
public abstract class AbstractResourceProvider implements ResourceProvider, Initializable {
  public final String RESOURCE_BASE_NAME = "resourceBaseName";

  private String resourceBaseName;

  @Override
  public Locale calculateLocale() {
    Locale result = getPreferredLocale();

    return result;
  }

  protected abstract Locale getPreferredLocale();

  @Override
  public String getResource(String key) {
    try {
      Locale locale = calculateLocale();
      ResourceBundle bundle = ResourceBundle.getBundle(resourceBaseName, locale);
      String result = bundle.getString(key);

      return result;
    } catch (Exception e) {
      return "??"+key+"??";
    }
  }

  @Override
  public void init(InitializationStrategy initStrategy) {
    StringMap initParams = LocatorUtils.getInheritedParameters(initStrategy, this.getClass(), ResourceProvider.class);

    resourceBaseName = initParams.getValueTransformer(RESOURCE_BASE_NAME).getStringValue("resources.bundle.resources"); // returned value must match <resource-bundle><base-name> in faces-config.xml
  }

  @Override
  public void reset() {
  }

}
