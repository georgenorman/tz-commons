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

import static org.junit.Assert.assertEquals;

import java.util.regex.Matcher;

import org.junit.Test;

import com.thruzero.common.core.fs.SubstitutionVisitorTest;
import com.thruzero.common.core.support.utils.PairTestUtils;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Test PatternValuePair.
 *
 * @author George Norman
 */
public class PatternValuePairTest extends AbstractCoreTestCase {

  @Test
  public void test1() {
    String source = PairTestUtils.getSourceString();
    PatternValuePair patternValuePair = new PatternValuePair(SubstitutionVisitorTest.REGEX_VAR_1, SubstitutionVisitorTest.SUBSTITUTION_VALUE_1);
    Matcher matcher = patternValuePair.getPattern().matcher(source);
    String result = matcher.replaceAll(patternValuePair.getValueAsString());

    PairTestUtils.testExpectedResult(result);
  }

  @Test
  public void test2() {
    PatternValuePair patternValuePair = new PatternValuePair("\\w+@\\w+\\.\\w+", "xxxxxxx");
    Matcher matcher = patternValuePair.getPattern().matcher("The email address, foo@bar.com, should be hidden.");
    String result = matcher.replaceAll(patternValuePair.getValueAsString());

    assertEquals("RegEx error.", "The email address, xxxxxxx, should be hidden.", result);
  }

}
