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
package com.thruzero.common.core.support;

import org.apache.commons.lang3.StringUtils;

/**
 * Simple empty string validator.
 * 
 * @author George Norman
 */
public class EmptyValueValidator {

  public static class EmptyValueValidatorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // ----------------------------------------------------
    // EmptyValueValidatorException
    // ----------------------------------------------------

    public EmptyValueValidatorException(final String message) {
      super(message);
    }

  }

  // =====================================================================
  // EmptyValueValidatorException
  // =====================================================================

  /**
   * @throws EmptyValueValidatorException if value is empty. The message will contain the given {@code componentName}.
   */
  public String getRequiredString(final String value, final String componentName) {
    if (StringUtils.isEmpty(value)) {
      throw new EmptyValueValidatorException("ERROR: " + componentName + " can't be empty.");
    }

    return value;
  }

}
