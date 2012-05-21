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
package com.thruzero.common.core.support.utils;

import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.fs.SubstitutionVisitorTest;

/**
 * Utils for testing Pairs.
 *
 * @author George Norman
 */
public class PairTestUtils {

  public static String getSourceString() {
    String source = "This is a test.\nThis is a test too.\nHere's a substitution var: " + SubstitutionVisitorTest.SUBSTITUTION_VAR_1 + "\nThis is another test.\n";

    return source;
  }

  public static void testExpectedResult(String result) {
    String expectedResult = "This is a test.\nThis is a test too.\nHere's a substitution var: " + SubstitutionVisitorTest.SUBSTITUTION_VALUE_1 + "\nThis is another test.\n";

    assertTrue("Substitution failed - does not match expected result.", StringUtils.equals(result, expectedResult));
  }
}
