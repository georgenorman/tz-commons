/*
 *   Copyright 2005-2011 George Norman
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
import com.thruzero.common.core.service.Service;

/**
 * A utility used to create and initialize an instance bound to a registered {@code Service} API and then return it for
 * all subsequent requests. The following example locates the instance bound to the registered SettingService API:
 *
 * <pre>
 * SettingService settingService = ServiceLocator.locate(SettingService.class);
 * </pre>
 *
 * Services may be registered directly, via the registry, or declaratively, via a config file. Each package of services
 * provides two convenient ways to register the packaged services. For example, the "domain.service" package provides
 * the {@link com.thruzero.domain.service.impl#DomainServiceRegistry DomainServiceRegistry}, which can be used to
 * register all of its provided services, as follows:
 *
 * <pre>
 * {@code
 *   DomainServiceRegistry.registerAllInterfaces();
 * }
 * </pre>
 *
 * A sample implementation of the {@code registerAllInterfaces} method is shown below:
 *
 * <pre>
 * {@code
 *   public static void registerAllInterfaces() {
 *     ...
 *     ServiceLocator.getRegistry().registerInterface(SettingService.class, GenericSettingService.class);
 *     ServiceLocator.getRegistry().registerInterface(MailService.class, SimpleMailService.class);
 *   }
 *
 * }
 * </pre>
 *
 * Second, each service package provides a config file to register its services. For example, the "domain.service"
 * package provides the config file named "config.domain.service.impl.xml" to register the same services as the
 * DomainServiceRegistry. All that's required is to add the following line to the application's config file (when using
 * the default JFig configuration provider)
 *
 * <pre>
 * {@code
 *   <include name="config.domain.service.impl.xml" />
 * }
 * </pre>
 *
 * Below is an example of the contents of the "config.domain.service.impl.xml" config file:
 *
 * <pre>
 * {@code
 *   <section name="com.thruzero.common.core.service.Service">
 *     ...
 *     <entry key="com.thruzero.domain.service.SettingService" value="com.thruzero.domain.service.impl.GenericSettingService" />
 *     <entry key="com.thruzero.domain.service.MailService" value="com.thruzero.domain.service.impl.SimpleMailService" />
 *  </section>
 * }
 * </pre>
 *
 * Alternatively, each {@code Service} may be registered individually, via the {@link #getRegistry()} method. Following
 * is an example that registers an individual interface programmatically:
 *
 * <pre>
 * {@code
 *   ServiceLocator.getRegistry().registerInterface(SettingService.class, GenericSettingService.class);
 * }
 * </pre>
 *
 * A Service API may only be registered once. For example, if the SettingService API is bound to the
 * GenericSettingService, it can't be registered again for the MockSettingService. You must choose only one. However, if
 * you need to use both the GenericSettingService and the MockSettingService, you could register the MockSettingService
 * as the API and implementation.
 *
 * @author George Norman
 */
@LocatorBookmark(comment = "ServiceLocator")
public class ServiceLocator {
  private static final RegistryLocatorStrategy<Service> locatorStrategy = new RegistryLocatorStrategy<Service>(Service.class);

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected ServiceLocator() {
  }

  /**
   * Return the registry used to bind an interface with it's concrete implementation, any time a {@code locate()}
   * request is made.
   */
  public static InterfaceBindingRegistry<Service> getRegistry() {
    return locatorStrategy.getRegistry();
  }

  /**
   * Returns the registered concrete implementation of the Service interface, if necessary, creating and initializing
   * the instance.
   */
  public static <T extends Service> T locate(final Class<T> type) {
    return type.cast(locatorStrategy.locate(type)); // use dynamic cast to avoid @SuppressWarnings("unchecked")
  }

  /** Clears the locator registry and bindings. */
  public static void reset() {
    locatorStrategy.reset();
  }

}
