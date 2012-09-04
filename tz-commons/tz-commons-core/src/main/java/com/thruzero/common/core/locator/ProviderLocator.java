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
package com.thruzero.common.core.locator;

import com.thruzero.common.core.bookmarks.LocatorBookmark;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.InterfaceBindingRegistry;
import com.thruzero.common.core.provider.Provider;

/**
 * A utility used to create the initial instance of a registered {@code Provider} implementation and return it for all
 * subsequent requests. A {@code Provider} is like a Helper, except that a {@code Provider} is a {@code Singleton} and
 * its implementation is determined at runtime (the concrete {@code Provider} instance can be defined in a config file).
 * <p>
 * The following example locates the instance bound to the registered {@code ResourceProvider} API:
 *
 * <pre>
 * ResourceProvider resourceProvider = ProviderLocator.locate(ResourceProvider.class);
 * </pre>
 *
 * @author George Norman
 */
@LocatorBookmark(comment = "ProviderLocator")
public class ProviderLocator {
  private static RegistryLocatorStrategy<Provider> locatorStrategy = new RegistryLocatorStrategy<Provider>(Provider.class);

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected ProviderLocator() {
  }

  /**
   * Return the registry used to bind an interface with it's concrete implementation, any time a {@code locate()}
   * request is made.
   */
  public static InterfaceBindingRegistry<Provider> getRegistry() {
    return locatorStrategy.getRegistry();
  }

  /**
   * Returns the registered concrete implementation of the {@code Provider} interface, if necessary, creating and initializing
   * the instance.
   */
  @SuppressWarnings("unchecked")
  public static <T extends Provider> T locate(final Class<T> type) {
    return (T)locatorStrategy.locate(type);
  }

  /** Clears the locator registry and bindings. */
  public static void reset() {
    locatorStrategy.reset();
  }

}
