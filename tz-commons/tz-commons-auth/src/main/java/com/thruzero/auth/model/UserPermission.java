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
package com.thruzero.auth.model;

import com.thruzero.domain.store.Persistent;

/**
 * An instance represents a set of actions that are granted to a particular domain (e.g., View and Edit the RSS feed page page).
 * It is used to create a Shiro WildcardPermission by TzAuthorizingRealm.
 *
 * @author George Norman
 */
public interface UserPermission extends Persistent {
  /** The context in which the permission is valid (e.g., "userAccountPage", "usePreferencesPage"). */
  public String getDomain();

  public void setDomain(String domain);

  /** Actions that can be performed within the Domain of this permission (e.g., Create, Update, Delete, View). */
  public String getActions();

  public void setActions(String actions);

  /** Brief explanation about this permission. */
  public String getDescription();

  public void setDescription(String description);
}
