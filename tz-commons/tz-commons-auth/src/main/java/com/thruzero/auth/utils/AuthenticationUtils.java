/*
 *   Copyright 2009-2012 George Norman
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
package com.thruzero.auth.utils;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.auth.model.User;
import com.thruzero.common.core.bookmarks.ConfigKeysBookmark;
import com.thruzero.common.core.config.Config.ConfigKeys;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.security.MessageDigestHelper;
import com.thruzero.common.core.security.PasswordHelper;
import org.apache.log4j.Logger;

/**
 * Authenticates registered users for login.
 *
 * @author George Norman
 */
public class AuthenticationUtils {
  public static final String ADMIN_GROUP_NAME = "admin";

  private static final long DEFAULT_LOCKOUT_TIME_IN_MINUTES = 10;
  private static final long DEFAULT_LOCKOUT_TIME_IN_MS = DEFAULT_LOCKOUT_TIME_IN_MINUTES * 1000 * 60;
  private static final long DEFAULT_INVALID_LOGIN_COUNT_THRESHOLD = 10;

  private static final Logger logger = Logger.getLogger(AuthenticationUtils.class);

  // ------------------------------------------------
  // AuthenticationConfigKeys
  // ------------------------------------------------

  /**
   * Config keys defined for AuthenticationUtils and are defined inside of the config file section named by
   * CONFIG_SECTION: "com.thruzero.auth.utils.AuthenticationUtils".
   */
  @ConfigKeysBookmark
  public interface AuthenticationConfigKeys extends ConfigKeys {
    /** The config section to use */
    String CONFIG_SECTION = AuthenticationUtils.class.getName();

    /**
     * The config key that defines lockout time in minutes for when the bad login count is exceeded:
     * "lockoutTimeInMinutes".
     */
    String LOCKOUT_TIME_THRESHOLD_IN_MINUTES = "lockoutTimeThresholdInMinutes";

    /**
     * The config key that defines how many bad login attempts must be made before account lock occurs:
     * "invalidLoginCountThreshold".
     */
    String INVALID_LOGIN_COUNT_THRESHOLD = "invalidLoginCountThreshold";
  }

  // ============================================================
  // AuthenticationUtils
  // ============================================================

  /** Allow for class extensions; disallow client instantiation */
  protected AuthenticationUtils() {
  }

  /**
   * return true if the given user's encoded one-time password matches the given submittedOneTimePw (presumably
   * submitted from a login page).
   * <p/>
   * Note: The given nonce is a random string that's given for each login. It simply prevents the submitted hashed
   * password from being captured and reused as a login without an extra step (i.e., you don't need to know the password
   * if you have the submitted hashed password - using the nonce prevents using the captured password, as it's only good
   * for a single login).
   */
  public static boolean isValidLogin(final User user, final String submittedLoginId, final char[] submittedOneTimePw, final String nonce) {
    boolean result = false;

    if (user != null && StringUtils.isNotEmpty(submittedLoginId) && submittedOneTimePw != null && StringUtils.isNotEmpty(nonce) && submittedLoginId.equalsIgnoreCase(user.getLoginId())) {
      String noncepw = nonce + user.getPassword();
      String userOneTimePw = MessageDigestHelper.getInstance().encodeAsMd5Hex(noncepw.getBytes());
      result = userOneTimePw != null && userOneTimePw.equals(new String(submittedOneTimePw));
    }

    return result;
  }

  public static String createNonce() {
    return PasswordHelper.getInstance().generatePassword();
  }

  /**
   * Return true if the number of invalid login attempts by the given user, is greater than the configured threshold.
   */
  public static boolean isTooManyBadLoginAttempts(final User user) {
    boolean result = false;

    if (user != null && System.currentTimeMillis() < user.getInvalidLoginLockoutTime()) {
      result = user.getInvalidLoginCount() >= getInvalidLoginCountThreshold();
    }

    return result;
  }

  /**
   * A failed login attempt was made, so update the number of bad login attempts or reset the tracker, if the time
   * threshold has been exceeded.
   */
  public static void handleBadLoginAttempt(final User user) {
    if (user == null) {
      return;
    }

    if (user.getInvalidLoginLockoutTime() == 0) {
      user.setInvalidLoginLockoutTime(System.currentTimeMillis() + AuthenticationUtils.getLockoutTimeThresholdInMinutes() * 1000 * 60);
    }

    if (System.currentTimeMillis() > user.getInvalidLoginLockoutTime()) {
      // current time has exceeded the lock-out time, so start over
      resetBadLoginAttemptTracker(user);
    } else {
      // increment invalid login counter
      user.incrementInvalidLoginCount();
    }
  }

  /** Allow user to attempt logins again. */
  public static void resetBadLoginAttemptTracker(final User user) {
    user.setInvalidLoginCount(0);
    user.setInvalidLoginLockoutTime(0);
  }

  public static long getLockoutTimeThresholdInMinutes() {
    long result;
    try {
      result = ConfigLocator.locate().getLongValue(AuthenticationConfigKeys.CONFIG_SECTION, AuthenticationConfigKeys.LOCKOUT_TIME_THRESHOLD_IN_MINUTES, DEFAULT_LOCKOUT_TIME_IN_MS);
    } catch (RuntimeException e) {
      logger.error(e); // shiro does not log the cause, so log it here
      throw e;
    }

    return result;
  }

  public static long getInvalidLoginCountThreshold() {
    long result;
    try {
      result = ConfigLocator.locate().getLongValue(AuthenticationConfigKeys.CONFIG_SECTION, AuthenticationConfigKeys.INVALID_LOGIN_COUNT_THRESHOLD, DEFAULT_INVALID_LOGIN_COUNT_THRESHOLD);
    } catch (RuntimeException e) {
      logger.error(e); // shiro does not log the cause, so log it here
      throw e;
    }

    return result;
  }

}
