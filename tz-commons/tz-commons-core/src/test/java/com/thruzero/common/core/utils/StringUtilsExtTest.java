/*
 *   Copyright 2011-2012 George Norman
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
package com.thruzero.common.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.map.StringMapTest.StringMapTestHelper;
import com.thruzero.test.support.AbstractCoreTestCase;
import com.thruzero.test.support.AbstractLinearGrowthPerformanceTestHelper;

/**
 * Unit test for StringUtilsExt.
 *
 * @author George Norman
 */
public class StringUtilsExtTest extends AbstractCoreTestCase {
  private StringMapTestHelper testHelper = new StringMapTestHelper();

  @Test
  public void testTokensToMap() {
    String tokenStream = testHelper.createSimpleTestStream();

    StringMap pd = StringUtilsExt.tokensToMap(tokenStream);

    testHelper.validateTestStringMap(pd);
  }

  @Test
  public void testToTokenStream() {
    String tokenStream = StringUtilsExt.toTokenStream(testHelper.createTestStringMap());
    StringMap pd = StringUtilsExt.tokensToMap(tokenStream);

    testHelper.validateTestStringMap(pd);
  }

  /** Doubling the number of tokens should approximately double the time. */
  @Test
  public void testToTokenStreamPerformance() throws Exception {
    AbstractLinearGrowthPerformanceTestHelper performanceHelper = new AbstractLinearGrowthPerformanceTestHelper() {
      private Map<String, String> tokens = new HashMap<String, String>();
      private String tokenStream;

      @Override
      protected void doSetup(int size) {
        for (int i=0; i<size; i++) {
          tokens.put("key"+i, "value"+i);
        }
      }

      @Override
      protected void doExecute() {
        tokenStream = StringUtilsExt.toTokenStream(tokens);
      }

      @Override
      protected void doDataValidation(int currentSize) {
        assertTrue(tokenStream.length() > currentSize * 10);
      }
    };

    performanceHelper.execute(100, 8, 0.1f);
  }

}
