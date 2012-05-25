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
package com.thruzero.common.core.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for PasswordHelper.
 */
public class PasswordHelperTest extends AbstractCoreTestCase {

  /**
   * ensure generatePassword generates unique passwords.
   */
  @Test
  public void testGeneratePassword() {
    String pw1 = PasswordHelper.getInstance().generatePassword();
    String pw2 = PasswordHelper.getInstance().generatePassword();

    assertNotNull(pw1);
    assertNotNull(pw2);
    assertFalse(pw1.equals(pw2));
  }
}
