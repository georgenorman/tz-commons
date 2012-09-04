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
package com.thruzero.auth.service.impl;

import com.thruzero.auth.service.UserService;
import com.thruzero.common.core.bookmarks.LocatorRegistryBookmark;
import com.thruzero.common.core.locator.RegistryLocatorStrategy.LocatorLogHelper;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.service.Service;
import com.thruzero.common.core.service.impl.ServiceRegistry;

/**
 * Convenience class for registering all of the core Service-related interfaces provided by this package. Clients may use
 * this class, or alternatively, use the config file or even register the interfaces manually via the {@link
 * com.thruzero.common.core.locator.ServiceLocator#getRegistry() ServiceLocator.getRegistry()} method.
 *
 * @author George Norman
 */
@LocatorRegistryBookmark(comment = "AuthServiceRegistry")
public final class AuthServiceRegistry extends ServiceRegistry {
  private static final AuthServiceRegistry instance = new AuthServiceRegistry();

  /** Use getInstance() to get an instance of this class. */
  private AuthServiceRegistry() {
  }

  public static AuthServiceRegistry getInstance() {
    return instance;
  }

  public void registerAllInterfaces() {
    // this is only called once
    new LocatorLogHelper(AuthServiceRegistry.class).logBeginRegisterInterfaces(Service.class.getName(), AuthServiceRegistry.class);

    ServiceLocator.getRegistry().registerInterface(UserService.class, GenericUserService.class);
  }

}
