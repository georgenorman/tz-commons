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
package com.thruzero.domain.jpa.transaction;

import com.thruzero.common.core.bookmarks.LocatorRegistryBookmark;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.LocatorLogHelper;
import com.thruzero.common.core.locator.TransactionMgrLocator;
import com.thruzero.common.core.transaction.TransactionMgr;
import com.thruzero.common.core.transaction.TransactionMgrRegistry;

/**
 * Convenience class for registering all of the JPA transaction-related interfaces provided by this package.
 * Clients may use this class, or alternatively, use the config file or even register the interfaces manually via the
 * {@link com.thruzero.common.core.locator.TransactionMgrLocator#getRegistry() ServiceLocator.getRegistry()} method.
 *
 * @author George Norman
 */
@LocatorRegistryBookmark(comment = "JpaTransactionRegistry")
public final class JpaTransactionRegistry extends TransactionMgrRegistry {
  private static final JpaTransactionRegistry instance = new JpaTransactionRegistry();

  /** Use getInstance() to get an instance of this class. */
  private JpaTransactionRegistry() {
  }

  public static JpaTransactionRegistry getInstance() {
    return instance;
  }

  /** Register all JPA transaction-related interfaces provided by this package. */
  public void registerAllInterfaces() {
    // this is only called once
    new LocatorLogHelper(JpaTransactionRegistry.class).logBeginRegisterInterfaces(TransactionMgr.class.getName(), JpaTransactionRegistry.class);

    TransactionMgrLocator.getRegistry().registerInterface(TransactionMgr.class, JpaDatabaseTransactionMgr.class);
  }
}
