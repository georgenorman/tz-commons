/*
 *   Copyright 2006 George Norman
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

/**
 * Helper class pertaining to MD5 encoding.
 *
 * @author George Norman
 */
public final class MessageDigestHelper {
  private static final MessageDigestHelper instance = new MessageDigestHelper();

  private MessageDigestHelper() {
  }

  public static MessageDigestHelper getInstance() {
    return instance;
  }

  /** MD5 encode the given plaintext char array. */
  public String encodeAsMd5Hex(final char[] plaintext) {
    return encodeAsMd5Hex(new String(plaintext).getBytes());
  }

  /** MD5 encode the given plaintext byte array. */
  public String encodeAsMd5Hex(final byte[] plaintext) {
    String result = null;
    MessageDigest md5 = getMd5();

    if (md5 != null) {
      result = new String(Hex.encodeHex(md5.digest(plaintext)));
    }

    return result;
  }

  /**
   * MD5 encode the given plaintext string and then compare it to the given encodedAsMd5Hex string, returning true if
   * they match or false if they don't.
   */
  public boolean testAsMd5Hex(final char[] plaintext, final String encodedAsMd5Hex) {
    if (plaintext == null || plaintext.length == 0 || StringUtils.isEmpty(encodedAsMd5Hex)) {
      return false;
    }

    return encodedAsMd5Hex.equals(encodeAsMd5Hex(plaintext));
  }

  /**
   * Return an MD5 MessageDigest.
   *
   * @throws RuntimeException if MD5 algorithm was not found.
   */
  protected MessageDigest getMd5() {
    MessageDigest result = null;

    try {
      result = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException nsae) {
      throw new RuntimeException("MD5 algorithm was not found", nsae);
    }

    return result;
  }
}
