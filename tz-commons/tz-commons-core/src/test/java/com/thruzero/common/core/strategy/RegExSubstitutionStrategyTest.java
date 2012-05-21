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
package com.thruzero.common.core.strategy;

import org.junit.Test;

import com.thruzero.common.core.fs.SubstitutionVisitorTest;
import com.thruzero.common.core.support.PatternValuePair;
import com.thruzero.common.core.support.utils.PairTestUtils;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Test RegExSubstitutionStrategy.
 *
 * @author George Norman
 */
public class RegExSubstitutionStrategyTest extends AbstractCoreTestCase {

  @Test
  public void testReplaceAll() {
    String source = PairTestUtils.getSourceString();
    PatternValuePair[] substitutionSpecs = new PatternValuePair[] { new PatternValuePair(SubstitutionVisitorTest.REGEX_VAR_1, SubstitutionVisitorTest.SUBSTITUTION_VALUE_1), new PatternValuePair(SubstitutionVisitorTest.REGEX_VAR_2, SubstitutionVisitorTest.SUBSTITUTION_VALUE_2) };
    SubstitutionStrategy strategy = new RegExSubstitutionStrategy(substitutionSpecs);

    String result = strategy.replaceAll(source);

    PairTestUtils.testExpectedResult(result);
  }

}
