/*
 *   Copyright 2010 George Norman
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
package com.thruzero.common.core.security;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Helper class pertaining to generating temporary passwords.
 *
 * @author George Norman
 */
public class PasswordHelper {
  private static final PasswordHelper instance = new PasswordHelper();

  private PasswordHelper() {
  }

  // Do not use 'o', 'O' or '0' and do not use '1' or 'l' (will be difficult for users to tell the difference between these characters).
  private static final char[] pwChars = { 'q', 'a', 'z', 'r', 't', 'y', 'u', 'i', 'p', 'w', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'e', 'x', 'c', 'v', 'b', 'n', 'm', '2', '3', '4', '5', '6',
      '7', '8', '9', 'Q', 'S', 'E', 'D', 'T', 'H', 'U', 'I', 'P', 'A', 'W', 'R', 'F', 'G', 'Y', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M' };

  private final Random random = new SecureRandom();

  public static PasswordHelper getInstance() {
    return instance;
  }

  /**
   * Generates a password, which can be used for user-login (e.g., a temporary password or as a first-time password).
   * The characters 'o', 'O', '0', '1' and 'l' are excluded, to reduce potential for misreading.
   */
  public String generatePassword() {
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < 8; i++) {
      result.append(pwChars[random.nextInt(pwChars.length)]);
    }

    return result.toString();
  }

}
