/*
 *   Copyright 2011 George Norman
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
package com.thruzero.domain.hibernate.dao;

import com.thruzero.common.core.bookmarks.LocatorRegistryBookmark;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.LocatorLogHelper;
import com.thruzero.domain.dao.DAO;
import com.thruzero.domain.dao.PreferenceDAO;
import com.thruzero.domain.dao.SettingDAO;
import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.dao.impl.DAORegistry;
import com.thruzero.domain.locator.DAOLocator;

/**
 * Convenience class for registering all of the core DAO-related interfaces provided by this package. Clients may use
 * this class, or alternatively, use the config file or even register the interfaces manually via the {@link
 * com.thruzero.domain.locator.DAOLocator#getRegistry() DAOLocator.getRegistry()} method.
 *
 * @author George Norman
 */
@LocatorRegistryBookmark(comment = "HibernateDAORegistry")
public class HibernateDAORegistry extends DAORegistry {
  private static final HibernateDAORegistry instance = new HibernateDAORegistry();

  /** Allow for class extensions; disallow client instantiation */
  protected HibernateDAORegistry() {
  }

  public static HibernateDAORegistry getInstance() {
    return instance;
  }

  public void registerAllInterfaces() {
    // this is only called once
    new LocatorLogHelper(HibernateDAORegistry.class).logBeginRegisterInterfaces(DAO.class.getName(), HibernateDAORegistry.class);

    DAOLocator.getRegistry().registerInterface(TextEnvelopeDAO.class, HibernateTextEnvelopeDAO.class);
    DAOLocator.getRegistry().registerInterface(PreferenceDAO.class, HibernatePreferenceDAO.class);
    DAOLocator.getRegistry().registerInterface(SettingDAO.class, HibernateSettingDAO.class);
  }

}
