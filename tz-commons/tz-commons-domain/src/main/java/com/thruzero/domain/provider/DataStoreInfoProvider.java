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

package com.thruzero.domain.provider;

import com.thruzero.common.core.provider.Provider;
import com.thruzero.domain.model.DataStoreInfo;

/**
 * A Provider interface that returns the {@code DataStoreInfo} for a requested user. For example, the {@code DataStoreInfo} for
 * the currently logged in user (or default user) is supplied by this {@code Provider} as well as the {@code DataStoreInfo} for a
 * requested user.
 *
 * @author George Norman
 */
public interface DataStoreInfoProvider extends Provider {
  String DEFAULT_USER_NAME_KEY = "defaultUserName";

  /**
   * Return the {@code DataStoreInfo} for the currently logged in user, or if no user is logged in, return the DataStoreInfo for the default user
   * (specified at initialization-time via {@code DEFAULT_USER_NAME_KEY}).
   */
  DataStoreInfo getDataStoreInfo();

  /**
   * Return the {@code DataStoreInfo} for the user defined by the given {@code userName} or null if user not found.
   */
  DataStoreInfo getDataStoreInfo(String userName);
}
