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
package com.thruzero.auth.hibernate.dao;

import com.thruzero.auth.dao.UserDAO;
import com.thruzero.common.core.bookmarks.LocatorRegistryBookmark;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.LocatorLogHelper;
import com.thruzero.domain.dao.DAO;
import com.thruzero.domain.dao.impl.DAORegistry;
import com.thruzero.domain.locator.DAOLocator;

/**
 * Convenience class for registering all of the core DAO-related interfaces provided by this package. Clients may use
 * this class, or alternatively, use the config file or even register the interfaces manually via the {@link
 * com.thruzero.domain.locator.DAOLocator#getRegistry() DAOLocator.getRegistry()} method.
 *
 * @author George Norman
 */
@LocatorRegistryBookmark(comment = "HibernateAuthDAORegistry")
public final class HibernateAuthDAORegistry extends DAORegistry {
  private static final HibernateAuthDAORegistry instance = new HibernateAuthDAORegistry();

  /** Use getInstance() to get an instance of this class. */
  private HibernateAuthDAORegistry() {
  }

  public static HibernateAuthDAORegistry getInstance() {
    return instance;
  }

  public void registerAllInterfaces() {
    // this is only called once
    new LocatorLogHelper(HibernateAuthDAORegistry.class).logBeginRegisterInterfaces(DAO.class.getName(), HibernateAuthDAORegistry.class);

    DAOLocator.getRegistry().registerInterface(UserDAO.class, HibernateUserDAO.class);
  }

}
