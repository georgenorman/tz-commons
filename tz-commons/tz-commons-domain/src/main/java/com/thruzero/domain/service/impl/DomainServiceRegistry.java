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
package com.thruzero.domain.service.impl;

import com.thruzero.common.core.bookmarks.LocatorRegistryBookmark;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.LocatorLogHelper;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.service.Service;
import com.thruzero.common.core.service.impl.ServiceRegistry;
import com.thruzero.domain.service.MailService;
import com.thruzero.domain.service.PreferenceService;
import com.thruzero.domain.service.SettingService;

/**
 * Convenience class for registering all Service-related interfaces provided by this package. Clients may use
 * this class, or alternatively, use the config file or even register the interfaces manually via the {@link
 * com.thruzero.common.core.locator.ServiceLocator#getRegistry() ServiceLocator.getRegistry()} method.
 *
 * @author George Norman
 */
@LocatorRegistryBookmark(comment = "DomainServiceRegistry")
public final class DomainServiceRegistry extends ServiceRegistry {
  private static final DomainServiceRegistry instance = new DomainServiceRegistry();

  /** Use getInstance() to get an instance of this class. */
  private DomainServiceRegistry() {
  }

  public static DomainServiceRegistry getInstance() {
    return instance;
  }

  public void registerAllInterfaces() {
    // this is only called once
    new LocatorLogHelper(DomainServiceRegistry.class).logBeginRegisterInterfaces(Service.class.getName(), DomainServiceRegistry.class);

    ServiceLocator.getRegistry().registerInterface(GenericInfoNodeService.class, GenericInfoNodeService.class);
    ServiceLocator.getRegistry().registerInterface(PreferenceService.class, GenericPreferenceService.class);
    ServiceLocator.getRegistry().registerInterface(SettingService.class, GenericSettingService.class);
    ServiceLocator.getRegistry().registerInterface(MailService.class, SimpleMailService.class);
  }

}
