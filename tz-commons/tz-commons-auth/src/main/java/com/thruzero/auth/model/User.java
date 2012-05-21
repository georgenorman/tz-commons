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
package com.thruzero.auth.model;

import java.util.Date;
import java.util.Set;

import com.thruzero.domain.store.Persistent;

/**
 * A User is an entity that can be identified by a unique name (e.g., email address) and password.
 * Info about the last successful login, as well as a count of invalid login attempts, is stored with the User object.
 * <p/>
 * TzAuthorizingRealm loads a User instance from a store (e.g., database, file-system, web service).
 * If the user is not found, then access is not granted. If the user is found, then it tests to see if too many
 * invalid login attempts have been made, tests the password hash for match, etc.
 * Associated with a User is a set of permissions (UserPermission) which may grant access to restricted areas of the application.
 *
 * @author George Norman
 */
public interface User extends Persistent {

  // ------------------------------------------------------
  // UserStatus
  // ------------------------------------------------------

  public static interface UserStatus {
    String ACTIVE = "A";
    String IN_ACTIVE = "I";
  }

  // ============================================================================
  // User
  // ============================================================================

  public void clear();

  /** Return the ID this user uses to login (e.g., email address). Must be unique for all users within the store. */
  public String getLoginId();

  public void setLoginId(final String loginId);

  /** Return the hashed password for this user. */
  public String getPassword();

  public void setPassword(final String password);

  /** Return the hashed temporary password for this user. */
  public String getTemporaryPassword();

  public void setTemporaryPassword(final String temporaryPassword);

  /** Return how many times this user has attempted to login, since the previous timeout period. */
  public int getInvalidLoginCount();

  public void setInvalidLoginCount(final int invalidLoginCount);

  public void incrementInvalidLoginCount();

  /**
   * Return the time in milliseconds at which login attempts may resume. For example, if the getInvalidLoginCount()
   * threshold is set to 10 and the lockout time is set to five-minutes, then if 11 bad logins occur, the InvalidLoginLockoutTime
   * will be set to the current time plus five-minutes. After the five-minute interval has passed, the user may resume login attempts and
   * the InvalidLoginLockoutTime may be reset to zero, or remain as is (either way, System.currentTimeMillis() will now
   * be greater than the InvalidLoginLockoutTime).
   */
  public long getInvalidLoginLockoutTime();

  public void setInvalidLoginLockoutTime(final long invalidLoginLockoutTime);

  /** Return the date of the last successful login. */
  public Date getLastLoginDate();

  public void setLastLoginDate(final Date lastLoginDate);

  /** Return the current standing of the user's account (e.g., ACTIVE, INACTIVE, TERMINATED, etc). */
  public String getStatus();

  public void setStatus(final String status);

  public Set<UserPermission> getPermissions();

  public void setPermissions(Set<UserPermission> permissions);

  /** Return personal user info (e.g., first and last name, home and work phone number, etc). */
  public UserDetails getDetails();

  public void setDetails(UserDetails details);
}
