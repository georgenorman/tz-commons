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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import javax.crypto.BadPaddingException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.thruzero.common.core.security.SimpleCipher.SimpleCipherConfiguration;
import com.thruzero.common.core.security.SimpleCipher.SimpleCipherException;
import com.thruzero.common.core.security.SimpleCipher.SimpleCipherConfiguration.ConfigInitOption;
import com.thruzero.common.core.security.SimpleCipher.SimpleCipherConfiguration.EnvironmentVarInitOption;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for SimpleCipher.
 *
 * @author George Norman
 */
public class SimpleCipherTest extends AbstractCoreTestCase {
  private static final String PLAIN_TEXT_1 = "pl@1nText0ne";

  // differs from the default salt used by SimpleCipher
  private static final byte[] UNIQUE_SALT = new byte[] { (byte)0xc3, 0x41, 0x55, (byte)0xed, (byte)0xf4, 0x41, 0x3e, 0x61 };

  // differs from the default test phrase used by SimpleCipher
  private static final String UNIQUE_PASS_PHRASE = "SimpleCipherTest Pass Phrase Test 1234";

  // differs from the default iteration count used by SimpleCipher
  private static final int UNIQUE_ITERATION_COUNT = 23;

  @Test
  public void testEncryptSimpleString() throws SimpleCipherException {
    SimpleCipher cipher = new SimpleCipher();

    String encryptedPw = cipher.encrypt(PLAIN_TEXT_1);
    assertFalse("Encrypt must not result in empty string.", StringUtils.isEmpty(encryptedPw));
    assertFalse("Encrypt must not equal original string.", PLAIN_TEXT_1.equals(encryptedPw));
  }

  @Test
  public void testDecryptSimpleString() throws SimpleCipherException {
    SimpleCipher cipher = new SimpleCipher();
    String encryptedPw = cipher.encrypt(PLAIN_TEXT_1);

    String decryptedPw = cipher.decrypt(encryptedPw);
    assertFalse("Decrypt must not result in empty string.", StringUtils.isEmpty(decryptedPw));
    assertEquals("Encrypt must equal original string.", PLAIN_TEXT_1, decryptedPw);
  }

  @Test
  public void testDecryptWithDifferentCipher() throws SimpleCipherException {
    SimpleCipher cipher1 = new SimpleCipher();
    String encryptedPw = cipher1.encrypt(PLAIN_TEXT_1);
    SimpleCipher cipher2 = new SimpleCipher(new SimpleCipherConfiguration(null, null, null, EnvironmentVarInitOption.DISABLED, ConfigInitOption.DISABLED));

    try {
      cipher2.decrypt(encryptedPw);
      fail("Exception should have been thrown - encrypt and decrypt with different ciphers");
    } catch (SimpleCipherException e) {
      assertEquals("Decrypt using different string - was expecting BadPaddingException.", BadPaddingException.class, e.getCause().getClass());
    }
  }

  @Test
  public void testSimpleCipherDirectConfiguration() {
    byte[] salt = UNIQUE_SALT;
    String passPhrase = UNIQUE_PASS_PHRASE;
    int iterationCount = UNIQUE_ITERATION_COUNT;

    // First, assert that the test values do not match the default values
    assertNotDefaultCipherValues(salt, passPhrase, iterationCount);

    // test configuration constructed using direct arguments
    SimpleCipherConfiguration simpleCipherConfiguration = new SimpleCipherConfiguration(salt, passPhrase.toCharArray(), iterationCount);
    assertTrue("Configuration salt must equal given salt", Arrays.equals(salt, simpleCipherConfiguration.getSalt()));
    assertTrue("Configuration pass-phrase must equal given pass-phrase", Arrays.equals(passPhrase.toCharArray(), simpleCipherConfiguration.getPassPhrase()));
    assertEquals("Configuration iteration count must equal given iteration count", UNIQUE_ITERATION_COUNT, simpleCipherConfiguration.getIterationCount());
  }

  @Test
  public void testSimpleCipherConfigurationViaConfigFile() {
    // MUST match config file values, but not default values
    byte[] salt = UNIQUE_SALT;
    String passPhrase = UNIQUE_PASS_PHRASE;
    int iterationCount = UNIQUE_ITERATION_COUNT;

    // First, assert that the test values do not match the default values
    assertNotDefaultCipherValues(salt, passPhrase, iterationCount);

    // test configuration constructed using config file
    SimpleCipherConfiguration simpleCipherConfiguration = new SimpleCipherConfiguration(null, null, null, EnvironmentVarInitOption.DISABLED, ConfigInitOption.ENABLED);
    assertTrue("Configuration salt must equal given salt", Arrays.equals(salt, simpleCipherConfiguration.getSalt()));
    assertTrue("Configuration pass-phrase must equal given pass-phrase", Arrays.equals(passPhrase.toCharArray(), simpleCipherConfiguration.getPassPhrase()));
    assertEquals("Configuration iteration count must equal given iteration count", UNIQUE_ITERATION_COUNT, simpleCipherConfiguration.getIterationCount());
  }

  private void assertNotDefaultCipherValues(final byte[] salt, final String passPhrase, final int iterationCount) {
    SimpleCipherConfiguration defaultConfiguration = new SimpleCipherConfiguration(null, null, null, EnvironmentVarInitOption.DISABLED, ConfigInitOption.DISABLED);
    assertFalse("Test salt must NOT equal default salt", Arrays.equals(salt, defaultConfiguration.getSalt()));
    assertFalse("Test pass-phrase must NOT equal default pass-phrase", Arrays.equals(passPhrase.toCharArray(), defaultConfiguration.getPassPhrase()));
    assertFalse("Test iteration count must NOT equal default iteration count", iterationCount == defaultConfiguration.getIterationCount());
  }
}
