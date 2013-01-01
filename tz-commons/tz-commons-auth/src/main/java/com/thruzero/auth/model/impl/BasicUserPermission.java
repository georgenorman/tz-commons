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
package com.thruzero.auth.model.impl;

import com.thruzero.auth.model.UserPermission;
import com.thruzero.domain.store.AbstractPersistent;
import com.thruzero.domain.store.Persistent;

/**
 * Simple implementation of UserPermission.
 * It's used to create a Shiro WildcardPermission by TzAuthorizingRealm.
 *
 * @author George Norman
 */
public class BasicUserPermission extends AbstractPersistent implements UserPermission {
  private static final long serialVersionUID = 1L;

  private String domain;
  private String actions; // TODO-p0(george) This should be a Set<String> Of Values (read from a single source).
  private String description;

  public BasicUserPermission() {
  }

  public BasicUserPermission(String domain, String actions, String description) {
    this.domain = domain;
    this.actions = actions;
    this.description = description;
  }

  @Override
  public void copyFrom(final Persistent source) {
    super.copyFrom(source);

    BasicUserPermission basicUserPermissionSource = (BasicUserPermission)source;
    domain = basicUserPermissionSource.getDomain();
    actions = basicUserPermissionSource.getActions();
    description = basicUserPermissionSource.getDescription();
  }

  @Override
  public String getDomain() {
    return domain;
  }

  @Override
  public void setDomain(String domain) {
    this.domain = domain;
  }

  @Override
  public String getActions() {
    return actions;
  }

  @Override
  public void setActions(String actions) {
    this.actions = actions;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

}
