/*
 *   Copyright 2009-2012 George Norman
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
package com.thruzero.domain.locator;

import com.thruzero.common.core.locator.RegistryLocatorStrategy;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.InterfaceBindingRegistry;
import com.thruzero.domain.dao.DAO;

/**
 * A class used to create and initialize an instance of any registered {@code DAO} implementation and return it for all
 * subsequent requests. The interface-to-Implementation binding can be registered programmatically or declaratively (via
 * a config file).
 * <p>
 * Using the {@link #getRegistry()} method, a client can register a concrete {@code DAO} implementation for any DAO
 * interface. Following is an example of how to register an interface programmatically:
 *
 * <pre>
 * {@code
 *   DAOLocator.getRegistry().registerInterface(TextEnvelopeDAO.class, HibernateTextEnvelopeDAO.class);
 * }
 * </pre>
 *
 * Alternatively, interfaces can be defined declaratively using a config file. These interfaces will be registered
 * automatically, if at the time of the first locate request is made, no interfaces have been registered. The
 * RegistryLocatorStrategy automatically looks in the config file and registers all DAO interfaces found within the
 * "com.thruzero.domain.dao.DAO" section. Following is an example config section:
 *
 * <pre>
 * {@code
 *   <section name="com.thruzero.domain.dao.DAO">
 *    <entry key="com.thruzero.domain.dao.TextEnvelopeDAO" value="com.thruzero.domain.hibernate.dao.HibernateTextEnvelopeDAO" />
 *    <entry key="com.thruzero.domain.dao.PreferenceDAO" value="com.thruzero.domain.hibernate.dao.HibernatePreferenceDAO" />
 *    <entry key="com.thruzero.domain.dao.SettingDAO" value="com.thruzero.domain.hibernate.dao.HibernateSettingDAO" />
 *   </section>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class DAOLocator {
  private static RegistryLocatorStrategy<DAO> locatorStrategy = new RegistryLocatorStrategy<DAO>(DAO.class);

  /** Allow for class extensions; disallow client instantiation */
  protected DAOLocator() {
  }

  public static InterfaceBindingRegistry<DAO> getRegistry() {
    return locatorStrategy.getRegistry();
  }

  @SuppressWarnings("unchecked")
  public static <T extends DAO> T locate(final Class<T> type) {
    return (T)locatorStrategy.locate(type);
  }

  /** Clears the locator registry and bindings. */
  public static void reset() {
    locatorStrategy.reset();
  }

//  @SuppressWarnings("unchecked")
//  public static <T> Map<String, ? extends DAO> locateAll(final Class<? extends DAO> type) {
//    return (Map<String, ? extends DAO>)locatorStrategy.locateAll(type);
//  }

}
