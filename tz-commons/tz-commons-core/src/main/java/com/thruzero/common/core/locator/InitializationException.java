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
package com.thruzero.common.core.locator;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.utils.ExceptionUtilsExt;

/**
 * An Exception that can be thrown by any implementation of Initializable. It retrieves the INITIALIZATION_SOURCE from
 * the StringMap used during initialization.
 *
 * @author George Norman
 */
public class InitializationException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public InitializationException(String message, Throwable cause, InitializationStrategy initStrategy) {
    super(ExceptionUtilsExt.decorateMessageLevel1a(message + getInitializationSourceMessage(initStrategy.getClass().getName())), cause);
  }

  public InitializationException(String message, InitializationStrategy initStrategy) {
    super(ExceptionUtilsExt.decorateMessageLevel1(message + getInitializationSourceMessage(initStrategy.getClass().getName())));
  }

  // Factory methods /////////////////////////////////////////////////////////////////////

  public static InitializationException createMissingKeyInitializationException(String sectionName, String key, InitializationStrategy initStrategy) {
    return new InitializationException("ERROR: The key named '" + key + "' is missing from the section named: '" + sectionName + "'.\n"
        + "Make sure that the named section in your Initialization Source (e.g., config file) contains the requested key.", initStrategy);
  }

  public static InitializationException createMissingSectionInitializationException(String sectionName, InitializationStrategy initStrategy) {
    return new InitializationException("ERROR: Could not find the section named: '" + sectionName + "'.\n"
        + "Make sure that the named section in your Initialization Source (e.g., config file) contains the requested key.", initStrategy);
  }

  // Support methods /////////////////////////////////////////////////////////////////////

  private static String getInitializationSourceMessage(String initilizationSource) {
    if (StringUtils.isEmpty(initilizationSource)) {
      initilizationSource = "SOURCE NOT DEFINED";
    }

    return "\n*** INITIALIZATION_SOURCE: '" + initilizationSource + "'.";
  }

}
