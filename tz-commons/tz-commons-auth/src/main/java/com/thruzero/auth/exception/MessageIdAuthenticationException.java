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
package com.thruzero.auth.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * An AuthenticationException that takes a message ID as the message argument. Clients
 * Can then pull the ID out and use it to look up a localized message based on the user's
 * chosen locale (e.g., via preferences).
 *
 * @author George Norman
 */
public class MessageIdAuthenticationException extends AuthenticationException {
  private static final long serialVersionUID = 1L;

  /**
   * Message-ID exception thrown due to an error during the Authentication process.
   *
   * @param messageId id of message in a resource file (e.g., "resources.bundle.resources").
   * @param cause cause of the exception
   */
  public MessageIdAuthenticationException(String messageId, Throwable cause) {
    super(messageId, cause);
  }

  /**
   * Message-ID exception thrown due to an error during the Authentication process.
   *
   * @param messageId id of message in a resource file (e.g., "resources.bundle.resources").
   */
  public MessageIdAuthenticationException(String messageId) {
    super(messageId);
  }

}
