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

import com.thruzero.common.core.infonode.InfoNodeElement;

/**
 * Simple empty string validator (useful for asserting required values and attributes from InfoNodeElement's).
 *
 * @author George Norman
 */
public class InfoNodeEmptyValueValidator extends EmptyValueValidator {

  /**
   * Return the value for the attribute identified by {@code id}, from the given {@code infoNode} and if no value is
   * found, throw EmptyValueValidatorException.
   *
   * @throws EmptyValueValidatorException if the attribute value is empty. The message will contain the given
   * {@code componentName}.
   */
  public String getRequiredAttributeString(final InfoNodeElement infoNode, final String id, final String componentName) {
    assertValidArguments(infoNode, id);

    return getRequiredString(infoNode.getAttributeValue(id), componentName);
  }

  /**
   * Return the value for the attribute identified by {@code id}, from the given {@code infoNode} and if no value is
   * found, return the given defaultValue.
   */
  public String getOptionalAttributeString(final InfoNodeElement infoNode, final String id, final String defaultValue) {
    assertValidArguments(infoNode, id);

    String result = infoNode.getAttributeValue(id);

    if (StringUtils.isEmpty(result)) {
      result = defaultValue;
    }

    return result;
  }

  protected void assertValidArguments(final InfoNodeElement infoNode, final String id) {
    if (infoNode == null) {
      throw new EmptyValueValidatorException("ERROR: The given InfoNodeElement to be validated, can't be null.");
    } else if (StringUtils.isEmpty(id)) {
      throw new EmptyValueValidatorException("ERROR: The given 'id' for the InfoNodeElement to be validated, can't be empty.");
    }
  }

}
