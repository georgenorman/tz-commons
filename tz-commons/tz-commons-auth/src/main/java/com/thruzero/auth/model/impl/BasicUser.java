/*
 *   Copyright 2005-2012 George Norman
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

import java.util.Collections;
import java.util.Date;
import java.util.Set;

import com.thruzero.auth.model.User;
import com.thruzero.auth.model.UserPermission;
import com.thruzero.auth.model.UserDetails;
import com.thruzero.domain.store.AbstractPersistent;
import com.thruzero.domain.store.Persistent;

/**
 * Simple implementation of User.
 *
 * @author George Norman
 */
public class BasicUser extends AbstractPersistent implements User {
  private static final long serialVersionUID = 1L;

  private String loginId;

  private String password;
  private String temporaryPassword;
  private int invalidLoginCount;
  private long invalidLoginLockoutTime;
  private Date lastLoginDate;

  private String status;

  private Set<UserPermission> permissions;
  private UserDetails details = new BasicUserDetails();

  @Override
  public void clear() {
    loginId = "";
    password = "";
    temporaryPassword = "";
    invalidLoginCount = 0;
    invalidLoginLockoutTime = 0;
    lastLoginDate = null;
    status = "";

    if (permissions != null) {
      permissions.clear();
    }
    permissions = null;
  }

  @Override
  public void copyFrom(final Persistent source) {
    super.copyFrom(source);

    BasicUser basicUserSource = (BasicUser)source;
    loginId = basicUserSource.getLoginId();
    password = basicUserSource.getPassword();
    temporaryPassword = basicUserSource.getTemporaryPassword();
    invalidLoginCount = basicUserSource.getInvalidLoginCount();
    invalidLoginLockoutTime = basicUserSource.getInvalidLoginLockoutTime();
    lastLoginDate = basicUserSource.getLastLoginDate();
    status = basicUserSource.getStatus();
  }

  /** Return the ID this user uses to login (e.g., email address). Must be unique for all users within the store. */
  @Override
  public String getLoginId() {
    return loginId;
  }

  @Override
  public void setLoginId(final String loginId) {
    this.loginId = loginId;
  }

  /** Return the encrypted password for this user. */
  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public void setPassword(final String password) {
    this.password = password;
  }

  /** Return the encrypted temporary password for this user. */
  @Override
  public String getTemporaryPassword() {
    return temporaryPassword;
  }

  @Override
  public void setTemporaryPassword(final String temporaryPassword) {
    this.temporaryPassword = temporaryPassword;
  }

  /** Return how many times this user has attempted to login, since the previous timeout period. */
  @Override
  public int getInvalidLoginCount() {
    return invalidLoginCount;
  }

  @Override
  public void setInvalidLoginCount(final int invalidLoginCount) {
    this.invalidLoginCount = invalidLoginCount;
  }

  @Override
  public void incrementInvalidLoginCount() {
    invalidLoginCount++;
  }

  /**
   * Return the time in milliseconds at which login attempts may resume. For example, if the getInvalidLoginCount()
   * threshold is set to 10 and the lockout time is set to five-minutes, then if 11 bad logins occur, the InvalidLoginLockoutTime
   * will be set to the current time plus five-minutes. After the five-minute interval has passed, the user may resume login attempts and
   * the InvalidLoginLockoutTime may be reset to zero, or remain as is (either way, System.currentTimeMillis() will now
   * be greater than the InvalidLoginLockoutTime).
   */
  @Override
  public long getInvalidLoginLockoutTime() {
    return invalidLoginLockoutTime;
  }

  @Override
  public void setInvalidLoginLockoutTime(final long invalidLoginLockoutTime) {
    this.invalidLoginLockoutTime = invalidLoginLockoutTime;
  }

  /** Return the date of the last successful login. */
  @Override
  public Date getLastLoginDate() {
    return lastLoginDate;
  }

  @Override
  public void setLastLoginDate(final Date lastLoginDate) {
    this.lastLoginDate = lastLoginDate;
  }

  /** Return the current standing of the user's account (e.g., ACTIVE, INACTIVE, TERMINATED, etc). */
  @Override
  public String getStatus() {
    return status;
  }

  @Override
  public void setStatus(final String status) {
    this.status = status;
  }

  @Override
  public Set<UserPermission> getPermissions() {
    return Collections.unmodifiableSet(permissions);
  }

  @Override
  public void setPermissions(Set<UserPermission> permissions) {
    this.permissions = permissions;
  }

  @Override
  public UserDetails getDetails() {
    return details;
  }

  @Override
  public void setDetails(UserDetails details) {
    this.details = details;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((loginId == null) ? 0 : loginId.hashCode());
    return result;
  }

  /**
   * {@code User}s are equal if their {@code loginId}s are equal.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    User other = (User)obj;
    if (loginId == null) {
      if (other.getLoginId() != null)
        return false;
    } else if (!loginId.equals(other.getLoginId()))
      return false;
    return true;
  }

}
