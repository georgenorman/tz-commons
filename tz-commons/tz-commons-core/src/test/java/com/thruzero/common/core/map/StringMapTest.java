/*
 *   Copyright 2011 George Norman
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
package com.thruzero.common.core.map;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.thruzero.common.core.utils.StringUtilsExt;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for StringMap.
 *
 * @author George Norman
 */
public class StringMapTest extends AbstractCoreTestCase {
  private StringMapTestHelper testHelper = new StringMapTestHelper();

  // ---------------------------------------------------------
  // Keys
  // ---------------------------------------------------------

  public interface Keys {
    /** bogus key. */
    String BOGUS_KEY = "bogus";

    // boolean keys.......................
    /** key to boolean true value */
    String BOOLEAN_TRUE_KEY = "booleanTrue";

    /** key to boolean false value */
    String BOOLEAN_FALSE_KEY = "booleanFalse";

    // integer keys.......................
    /** key to first integer value */
    String INTEGER_ONE_KEY = "integerOne";
    /** key to second integer value */
    String INTEGER_TWO_KEY = "integerTwo";
    /** key to third integer value */
    String INTEGER_THREE_KEY = "integerThree";

    // long keys.......................
    String LONG_ONE_KEY = "longOne";

    // float keys.......................
    String FLOAT_12_DOT_34_KEY = "float12dot34";
    String INT_123_KEY = "int123";

    // list keys.......................
    String LIST_ONE_KEY = "listOne";

    // url keys.......................
    String URL_MALFORMED_KEY = "urlMalformed";
    String URL_A_KEY = "urlA";
  }

  // ---------------------------------------------------------
  // Values
  // ---------------------------------------------------------

  public interface Values {
    // integer values.......................
    /** first integer value */
    int INTEGER_ONE_VALUE = 1;
    /** second integer value */
    int INTEGER_TWO_VALUE = 22;
    /** third integer value */
    int INTEGER_THREE_VALUE = 333;

    // long values.......................
    long LONG_ONE_VALUE = 1234567890;

    // float values
    String FLOAT_12_DOT_34_TEST_VALUE_STR = "12.34";
    float FLOAT_12_DOT_34_TEST_VALUE = Float.parseFloat(FLOAT_12_DOT_34_TEST_VALUE_STR);

    // url values.......................
    String URL_A_TEST_VALUE = "http://www.google.com/";
    String URL_DEFAULT_TEST_VALUE = "http://www.yahoo.com/";

    // default values.......................
    String DEFAULT_TEST_VALUE_STR = "DEFAULT";
  }

  // ------------------------------------------------------
  // StringMapTestHelper
  // ------------------------------------------------------

  public static class StringMapTestHelper {
    public String createSimpleTestStream() {
      String btp = Keys.BOOLEAN_TRUE_KEY + "=" + true;
      String bfp = Keys.BOOLEAN_FALSE_KEY + "=" + false;
      String iop = Keys.INTEGER_ONE_KEY + "=" + Values.INTEGER_ONE_VALUE;
      String lop = Keys.LONG_ONE_KEY + "=" + Values.LONG_ONE_VALUE;

      return btp + " ," + bfp + ", " + iop + "," + lop;
    }

    /**
     * create a StringMap populated with test values.
     */
    public StringMap createTestStringMap() {
      StringMap result = new StringMap();

      result.put(Keys.BOOLEAN_TRUE_KEY, String.valueOf(true));
      result.put(Keys.BOOLEAN_FALSE_KEY, String.valueOf(false));
      result.put(Keys.INTEGER_ONE_KEY, String.valueOf(Values.INTEGER_ONE_VALUE));
      result.put(Keys.LONG_ONE_KEY, String.valueOf(Values.LONG_ONE_VALUE));

      return result;
    }

    public void validateTestStringMap(StringMap pd) {
      String value;

      value = pd.get(Keys.BOOLEAN_TRUE_KEY);
      assertEquals(String.valueOf(true), value);

      value = pd.get(Keys.BOOLEAN_FALSE_KEY);
      assertEquals(String.valueOf(false), value);

      value = pd.get(Keys.INTEGER_ONE_KEY);
      assertEquals(String.valueOf(Values.INTEGER_ONE_VALUE), value);

      value = pd.get(Keys.LONG_ONE_KEY);
      assertEquals(String.valueOf(Values.LONG_ONE_VALUE), value);
    }
  }

  // ==============================================================================
  // StringMapTest
  // ==============================================================================

  @Test
  public void testTokenStreamConstructor() {
    String tokenStream = testHelper.createSimpleTestStream();
    StringMap pd = new StringMap(StringUtilsExt.tokensToMap(tokenStream));

    testHelper.validateTestStringMap(pd);
  }

  @Test
  public void testGetValueAsString() {
    StringMap pd = testHelper.createTestStringMap();

    testHelper.validateTestStringMap(pd);
  }

  @Test
  public void testGetDefaultValueAsString() {
    StringMap pd = testHelper.createTestStringMap();
    assertEquals( String.valueOf(true), pd.getValueTransformer(Keys.BOGUS_KEY).getStringValue("true"));
  }

  @Test
  public void testGetValueAsInt() {
    StringMap pd = testHelper.createTestStringMap();
    assertEquals(Values.INTEGER_ONE_VALUE, pd.getValueTransformer(Keys.INTEGER_ONE_KEY).getIntValue(-1));
  }

  @Test
  public void testGetDefaultValueAsInt() {
    StringMap pd = testHelper.createTestStringMap();
    assertEquals(-123, pd.getValueTransformer(Keys.BOGUS_KEY).getIntValue(-123));
  }

  @Test
  public void testGetDefaultValueAsLong() {
    StringMap pd = testHelper.createTestStringMap();
    assertEquals(Values.LONG_ONE_VALUE, pd.getValueTransformer(Keys.LONG_ONE_KEY).getLongValue(-1L));
  }

  @Test
  public void testGetDefaultValueAsBoolean() {
    StringMap pd = testHelper.createTestStringMap();
    assertEquals(-123L, pd.getValueTransformer(Keys.BOGUS_KEY).getLongValue(-123L));
  }

}
