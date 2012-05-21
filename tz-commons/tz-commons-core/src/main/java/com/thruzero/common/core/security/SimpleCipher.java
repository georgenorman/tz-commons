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

import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.thruzero.common.core.bookmarks.ConfigKeysBookmark;
import com.thruzero.common.core.bookmarks.EnvironmentVarKeyBookmark;
import com.thruzero.common.core.config.Config.ConfigKeys;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.support.EnvironmentHelper.EnvironmentVariableKeys;
import com.thruzero.common.core.utils.StringUtilsExt;

/**
 * Provides encryption and decryption using a pass-phrase. The pass-phrase, salt and iteration-count can be read from a
 * config file, environment variable or passed into the constructor (and uses default values if none are given).
 * <p>
 * Useful for encrypting and decrypting a database password stored in a config file (e.g., hibernate config).
 * <p>
 * Note: Never use this to encrypt persisted user passwords. User passwords should never be decryptable. Instead, user
 * passwords should be stored using a one-way hash (see
 * {@link com.thruzero.common.core.security.MessageDigestHelper MessageDigestHelper}).
 *
 * @author George Norman
 * @see http://docs.oracle.com/javase/1.4.2/docs/guide/security/jce/JCERefGuide.html
 */
public class SimpleCipher {
  private static final String PBE_WITH_MD5_AND_DES = "PBEWithMD5AndDES"; // uses a set of parameters, comprising a salt and an iteration count

  private final Base64 base64 = new Base64();
  private Cipher encryptionCipher;
  private Cipher decryptionCipher;

  // ------------------------------------------------
  // SimpleCipherConfigKeys
  // ------------------------------------------------

  /**
   * Defines keys used to configure the cipher from a config file and are defined inside of the config file section
   * named by CONFIG_SECTION: "com.thruzero.common.core.security.SimpleCipher".
   */
  @ConfigKeysBookmark
  public interface SimpleCipherConfigKeys extends ConfigKeys {
    /** The config section to use */
    String CONFIG_SECTION = SimpleCipher.class.getName();

    /** Config key: "SimpleCipherSalt" */
    String SALT = "SimpleCipherSalt";

    /** Config key: "SimpleCipherPassPhrase" */
    String PASS_PHRASE = "SimpleCipherPassPhrase";

    /** Config key: "SimpleCipherIterationCount" */
    String ITERATION_COUNT = "SimpleCipherIterationCount";
  }

  // ------------------------------------------------
  // SimpleCipherEnvironmentVariableKeys
  // ------------------------------------------------

  /** Defines keys used to configure the cipher from environment variables. */
  @EnvironmentVarKeyBookmark
  public interface SimpleCipherEnvironmentVariableKeys extends EnvironmentVariableKeys {
    /** Environment Var key: "com_thruzero_SimpleCipher_SALT" */
    String SALT_ENV_VAR = "com_thruzero_SimpleCipher_SALT";

    /** Environment Var key: "com_thruzero_SimpleCipher_PASS_PHRASE" */
    String PASS_PHRASE_ENV_VAR = "com_thruzero_SimpleCipher_PASS_PHRASE";

    /** Environment Var key: "com_thruzero_SimpleCipher_ITERATION_COUNT" */
    String ITERATION_COUNT_ENV_VAR = "com_thruzero_SimpleCipher_ITERATION_COUNT";
  }

  // ------------------------------------------------
  // SimpleCipherException
  // ------------------------------------------------

  /** A wrapper for the {@code javax.crypto} exceptions (offers hints for the cause). */
  public static class SimpleCipherException extends Exception {
    private static final long serialVersionUID = 100;

    public SimpleCipherException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }

  // ------------------------------------------------
  // SimpleCipherConfiguration
  // ------------------------------------------------

  /**
   * Defines the {@code salt}, {@code passPhrase} and {@code iterationCount} used to construct a {@code SimpleCipher}.
   */
  public static class SimpleCipherConfiguration {
    private byte[] salt;
    private char[] passPhrase;
    private Integer iterationCount;

    private final boolean allowEnvironmentVariableInit;
    private final boolean allowConfigInit;

    /**
     * A configuration used to construct a SimpleCipher using {@code salt}, {@code passPhrase} and
     * {@code iterationCount} using environment variables, a config file or default values, whichever is found first.
     * Each property is read independently, so the {@code salt} can be read from an environment variable and the
     * {@code passPhrase} can be read from a config file and the iteration-count may use the default value.
     */
    public SimpleCipherConfiguration() {
      this(null, null, null, true, true);
    }

    /**
     * A configuration used to construct a SimpleCipher from the given values of {@code salt}, {@code passPhrase} and
     * {@code iterationCount}. For any empty value, a default value will be used.
     */
    public SimpleCipherConfiguration(final byte[] salt, final char[] passPhrase, final Integer iterationCount) {
      this(salt, passPhrase, iterationCount, false, false);
    }

    /**
     * A configuration used to construct a SimpleCipher from the given values for {@code salt}, {@code passPhrase} and
     * {@code iterationCount}. For each empty value, the configuration will first attempt to retrieve the value from an
     * environment variable (if allowed), and if not found or allowed, will look in the configuration file for a value
     * (if allowed), and if still not found, will use a default value.
     */
    public SimpleCipherConfiguration(final byte[] salt, final char[] passPhrase, final Integer iterationCount, final boolean allowEnvironmentVariableInit, final boolean allowConfigInit) {
      this.salt = salt == null ? null : Arrays.copyOf(salt, salt.length);
      this.passPhrase = passPhrase == null ? null : Arrays.copyOf(passPhrase, passPhrase.length);
      this.iterationCount = iterationCount;

      this.allowEnvironmentVariableInit = allowEnvironmentVariableInit;
      this.allowConfigInit = allowConfigInit;
    }

    /**
     * Returns the {@code salt} represented by this instance. The result is determined as follows:
     * <ol>
     * <li>return the value given at construction time, unless null,
     * <li>if environment variables are allowed, then return the environment variable using the
     * {@link SimpleCipherEnvironmentVariableKeys#SALT_ENV_VAR}, unless null,
     * <li>if config variables are allowed, then return the config value using the {@link SimpleCipherConfigKeys#SALT},
     * unless null,
     * <li>otherwise, return the default value using the {@link #getDefaultSalt()} function.
     * </ol>
     */
    public byte[] getSalt() {
      if (salt == null) {
        String saltTokenStream = null;

        if (allowEnvironmentVariableInit) {
          saltTokenStream = System.getenv(SimpleCipherEnvironmentVariableKeys.SALT_ENV_VAR);
        }

        if (StringUtils.isEmpty(saltTokenStream) && allowConfigInit) {
          saltTokenStream = ConfigLocator.locate().getValue(SimpleCipherConfigKeys.CONFIG_SECTION, SimpleCipherConfigKeys.SALT);
        }

        if (StringUtils.isEmpty(saltTokenStream)) {
          salt = getDefaultSalt();
          salt = salt == null ? null : Arrays.copyOf(salt, salt.length); // copy salt (to protect against client modifying given array.
        } else {
          salt = StringUtilsExt.tokensToByteArray(saltTokenStream, ",");
        }
      }

      return salt == null ? null : Arrays.copyOf(salt, salt.length); // copy returned value, to prevent clients from tampering with salt
    }

    /**
     * Returns the {@code passPhrase} represented by this instance. The result is determined as follows:
     * <ol>
     * <li>return the value given at construction time, unless null,
     * <li>if environment variables are allowed, then return the environment variable using the
     * {@link SimpleCipherEnvironmentVariableKeys#PASS_PHRASE}, unless null,
     * <li>if config variables are allowed, then return the config value using the
     * {@link SimpleCipherConfigKeys#PASS_PHRASE}, unless null,
     * <li>otherwise, return the default value using the {@link #getDefaultPassPhrase()} function.
     * </ol>
     */
    public char[] getPassPhrase() {
      if (passPhrase == null) {
        String passPhraseStr = null;

        if (allowEnvironmentVariableInit) {
          passPhraseStr = System.getenv(SimpleCipherEnvironmentVariableKeys.PASS_PHRASE_ENV_VAR);
        }

        if (StringUtils.isEmpty(passPhraseStr) && allowConfigInit) {
          passPhraseStr = ConfigLocator.locate().getValue(SimpleCipherConfigKeys.CONFIG_SECTION, SimpleCipherConfigKeys.PASS_PHRASE);
        }

        if (StringUtils.isEmpty(passPhraseStr)) {
          passPhrase = getDefaultPassPhrase();
          passPhrase = passPhrase == null ? null : Arrays.copyOf(passPhrase, passPhrase.length); // copy passPhrase (to protect against client modifying given array.
        } else {
          passPhrase = passPhraseStr.toCharArray();
        }
      }

      return passPhrase == null ? null : Arrays.copyOf(passPhrase, passPhrase.length); // copy returned value, to prevent clients from tampering with passPhrase
    }

    /**
     * Returns the {@code iterationCount} represented by this instance. The result is determined as follows:
     * <ol>
     * <li>return the value given at construction time, unless null,
     * <li>if environment variables are allowed, then return the environment variable using the
     * {@link SimpleCipherEnvironmentVariableKeys#ITERATION_COUNT}, unless null,
     * <li>if config variables are allowed, then return the config value using the
     * {@link SimpleCipherConfigKeys#ITERATION_COUNT}, unless null,
     * <li>otherwise, return the default value using the {@link #getDefaultIterationCount()} function.
     * </ol>
     */
    public int getIterationCount() {
      if (iterationCount == null) {
        String iterationCountStr = null;

        if (allowEnvironmentVariableInit) {
          iterationCountStr = System.getenv(SimpleCipherEnvironmentVariableKeys.ITERATION_COUNT_ENV_VAR);
        }

        if (StringUtils.isEmpty(iterationCountStr) && allowConfigInit) {
          iterationCountStr = ConfigLocator.locate().getValue(SimpleCipherConfigKeys.CONFIG_SECTION, SimpleCipherConfigKeys.ITERATION_COUNT);
        }

        if (StringUtils.isEmpty(iterationCountStr)) {
          iterationCount = getDefaultIterationCount();
        } else {
          iterationCount = Integer.parseInt(iterationCountStr);
        }
      }

      return iterationCount;
    }

    protected byte[] getDefaultSalt() {
      return new byte[] { 0x19, (byte)0xc1, 0x20, 0x01, 0x31, (byte)0xd2, (byte)0xc3, (byte)0xa7 };
    }

    protected char[] getDefaultPassPhrase() {
      return "S1mpleC1pherDef@ultP@ssPhr@se".toCharArray();
    }

    protected int getDefaultIterationCount() {
      return 20;
    }
  }

  // ============================================================
  // SimpleCipher
  // ============================================================

  /**
   * Construct using the default {@link com.thruzero.common.core.security.SimpleCipher.SimpleCipherConfiguration}.
   *
   * @throws SimpleCipherException
   */
  public SimpleCipher() throws SimpleCipherException {
    this(new SimpleCipherConfiguration());
  }

  /**
   * Construct using the {@code salt}, {@code passPhrase} and {@code iterationCount} defined by the given
   * {@code simpleCipherConfiguration}.
   *
   * @throws SimpleCipherException
   */
  public SimpleCipher(final SimpleCipherConfiguration simpleCipherConfiguration) throws SimpleCipherException {
    try {
      int count = simpleCipherConfiguration.getIterationCount();
      byte[] salt = simpleCipherConfiguration.getSalt();

      KeySpec keySpec = new PBEKeySpec(simpleCipherConfiguration.getPassPhrase(), salt, count);

      AlgorithmParameterSpec parameterSpec = new PBEParameterSpec(salt, count);
      SecretKey key = SecretKeyFactory.getInstance(PBE_WITH_MD5_AND_DES).generateSecret(keySpec);

      encryptionCipher = Cipher.getInstance(key.getAlgorithm());
      encryptionCipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

      decryptionCipher = Cipher.getInstance(key.getAlgorithm());
      decryptionCipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
    } catch (InvalidAlgorithmParameterException e) {
      throw new SimpleCipherException("Couldn't instantiate SimpleCipher because of " + ExceptionUtils.getMessage(e), e);
    } catch (Exception e) {
      throw new SimpleCipherException("Couldn't instantiate SimpleCipher because of " + ExceptionUtils.getMessage(e), e);
    }
  }

  /**
   * @return the given {@code plaintext} as an encrypted string.
   * @throws SimpleCipherException
   */
  public String encrypt(final String plaintext) throws SimpleCipherException {
    String result = null;

    try {
      byte[] plaintextBytes = plaintext.getBytes();
      byte[] enc = encryptionCipher.doFinal(plaintextBytes); // Encrypt the plaintext

      result = Base64.encodeBase64URLSafeString(enc);
    } catch (Exception e) {
      throw new SimpleCipherException("Couldn't encrypt the given plaintext because of " + ExceptionUtils.getMessage(e), e);
    }

    return result;
  }

  /**
   * @return the given {@code encryptedStr} as a decrypted string.
   * @throws SimpleCipherException
   */
  public String decrypt(final String encryptedStr) throws SimpleCipherException {
    byte[] decoded = base64.decode(encryptedStr);
    byte[] decrypted;

    try {
      decrypted = decryptionCipher.doFinal(decoded);
    } catch (IllegalBlockSizeException e) {
      throw new SimpleCipherException("Couldn't decrypt the given ciphertext because of " + ExceptionUtils.getMessage(e), e);
    } catch (BadPaddingException e) {
      throw new SimpleCipherException("Couldn't decrypt the given ciphertext - Ensure you're using the same passphrase to decrypt as was used to encrypt.", e);
    }

    return new String(decrypted);
  }

}
