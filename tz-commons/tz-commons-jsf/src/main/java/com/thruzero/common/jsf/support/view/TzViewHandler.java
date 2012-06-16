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
package com.thruzero.common.jsf.support.view;

import java.util.Locale;

import javax.faces.context.FacesContext;

import com.sun.faces.application.view.MultiViewHandler;
import com.thruzero.common.core.locator.ProviderLocator;
import com.thruzero.common.core.provider.ResourceProvider;

/**
 * A basic ViewHandler that calculates the locale from a ResourceProvider, which can then retrieve the
 * preferred locale from a menu selection, user preference, etc.
 *
 * @author George Norman
 */
public class TzViewHandler extends MultiViewHandler {

  @Override
  public Locale calculateLocale(FacesContext context) {
    ResourceProvider resourceProvider = ProviderLocator.locate(ResourceProvider.class);
    Locale result = resourceProvider.calculateLocale();

    if (result == null) {
      result = super.calculateLocale(context);
    }

    return result;
  }

}
