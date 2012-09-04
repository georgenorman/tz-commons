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
package com.thruzero.common.core.locator;

import com.thruzero.common.core.bookmarks.LocatorBookmark;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.InterfaceBindingRegistry;
import com.thruzero.common.core.transaction.TransactionMgr;

/**
 * A class used to create the initial instance of the registered {@code TransactionMgr} implementation and return it for
 * all subsequent requests.
 * <p>
 * The concrete {@code TransactionMgr} instance to be created can be specified programmatically by using the
 * {@link #getRegistry()} method or declaratively using the config file.
 * <p>
 * Following is an example that registers the {@code TransactionMgr} programmatically:
 *
 * <pre>
 * {@code
 *   TransactionMgrLocator.getRegistry().registerInterface(TransactionMgr.class, HibernateDatabaseTransactionMgr.class);
 * }
 * </pre>
 *
 * Following is an example that registers the Hibernate {@code TransactionMgr} declaratively, by adding the following
 * line to the main configuration file (when using the default JFig configuration provider):
 *
 * <pre>
 * {@code
 * <include name="config.domain.hibernate.transaction.xml" />
 * }
 * </pre>
 *
 * @author George Norman
 */
@LocatorBookmark(comment = "TransactionMgrLocator")
public class TransactionMgrLocator {
  private static RegistryLocatorStrategy<TransactionMgr> locatorStrategy = new RegistryLocatorStrategy<TransactionMgr>(TransactionMgr.class);

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected TransactionMgrLocator() {
  }

  /**
   * Return the registry used to bind an interface with it's concrete implementation, any time a {@code locate()}
   * request is made.
   */
  public static InterfaceBindingRegistry<TransactionMgr> getRegistry() {
    return locatorStrategy.getRegistry();
  }

  /**
   * Returns the registered concrete implementation of the Service interface, if necessary, creating and initializing
   * the instance.
   */
  @SuppressWarnings("unchecked")
  public static <T extends TransactionMgr> T locate(final Class<T> type) {
    return (T)locatorStrategy.locate(type);
  }

  /** Clears the locator registry and bindings. */
  public static void reset() {
    locatorStrategy.reset();
  }

}
