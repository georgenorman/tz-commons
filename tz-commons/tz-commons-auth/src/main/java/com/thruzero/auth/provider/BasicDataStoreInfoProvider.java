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

package com.thruzero.auth.provider;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.auth.model.User;
import com.thruzero.auth.service.UserService;
import com.thruzero.common.core.locator.Initializable;
import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.locator.LocatorUtils;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.domain.model.DataStoreInfo;
import com.thruzero.domain.provider.DataStoreInfoProvider;

/**
 * Basic implementation of DataStoreInfoProvider that retrieves the user's DataStoreInfo from the User object (which
 * is retrieved from the UserService)
 *
 * @author George Norman
 */
public class BasicDataStoreInfoProvider implements DataStoreInfoProvider, Initializable {
  private String defaultUserName;

  @Override
  public DataStoreInfo getDataStoreInfo() {
    DataStoreInfo result;

    UserService userService = ServiceLocator.locate(UserService.class);
    User user = userService.getCurrentAuthenticatedUser();
    if (user == null) {
      result = handleDefaultUser();
    } else {
      result = user.getUserDataStoreInfo();
    }

    return result;
  }

  /**
   * Return the default DataStoreInfo for the default user (used when no user is logged in). The default
   * user is specified when this service is initialized (see {@link #init(InitializationStrategy)}).
   */
  protected DataStoreInfo handleDefaultUser() {
    DataStoreInfo result = getDataStoreInfo(defaultUserName);

    return result;
  }

  @Override
  public DataStoreInfo getDataStoreInfo(String userName) {
    DataStoreInfo result = null;

    UserService userService = ServiceLocator.locate(UserService.class);
    User user = userService.getUserByLoginId(userName);

    if (user != null) {
      result = user.getUserDataStoreInfo();
    }

    return result;
  }

  /**
   * Initialize the service from the given InitializationStrategy - DataStoreInfoProvider.DEFAULT_USER_NAME_KEY
   * is used to fetch the default user name to use when no user is logged in.
   */
  @Override
  public void init(InitializationStrategy initStrategy) {
    StringMap initParams = LocatorUtils.getInheritedParameters(initStrategy, this.getClass(), DataStoreInfoProvider.class);

    defaultUserName = initParams.getValueTransformer(DEFAULT_USER_NAME_KEY).getStringValue();

    if (StringUtils.isEmpty(defaultUserName)) {
      throw InitializationException.createMissingKeyInitializationException(DataStoreInfoProvider.class.getName(), DEFAULT_USER_NAME_KEY, initStrategy);
    }
  }

  @Override
  public void reset() {
  }
}
