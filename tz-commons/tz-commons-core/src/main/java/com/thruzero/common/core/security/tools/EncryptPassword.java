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
package com.thruzero.common.core.security.tools;

import com.thruzero.common.core.security.SimpleCipher;
import com.thruzero.common.core.security.SimpleCipher.SimpleCipherConfiguration;
import com.thruzero.common.core.security.SimpleCipher.SimpleCipherException;

/**
 * Tool used to encrypt a password using SimpleCipher.
 *
 * @author George Norman
 */
public class EncryptPassword {

  /**
   * {@literal java -cp tz-commons-core-1.0.0.jar com.thruzero.common.core.security.tools.EncryptPassword <pass-phrase> <plaintext-password>}
   */
  public static void main(final String[] args) {
    if (args == null || args.length < 2) {
      throw new RuntimeException("*** USAGE: java -cp <jars> com.thruzero.common.core.security.tools.EncryptPassword <pass-phrase> <plaintext-password>");
    }

    System.out.println("EncryptPassword");

    try {
      byte[] salt = null; // use default salt
      char[] passPhrase = args[0].toCharArray();
      Integer iterationCount = null;

      SimpleCipher cipher = new SimpleCipher(new SimpleCipherConfiguration(salt, passPhrase, iterationCount));

      System.out.println("Encrypted as: " + cipher.encrypt(args[1]));
    } catch (SimpleCipherException e) {
      e.printStackTrace();
    }

  }

}
