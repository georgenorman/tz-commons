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
package com.thruzero.common.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.thruzero.common.core.bookmarks.ConfigKeysBookmark;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for Config.
 *
 * @author George Norman
 */
public class ConfigTest extends AbstractCoreTestCase {

  // -------------------------------------------------
  // ConfigKeys
  // -------------------------------------------------

  @ConfigKeysBookmark
  public interface ConfigKeys {
    // Section Keys
    String TEST_TYPES_SECTION_KEY = "testTypes";

    // value keys
    String FLOAT_12_DOT_34_VALUE_KEY = "float12dot34";
    String INT_123_VALUE_KEY = "int123";
    String URL_MALFORMED_VALUE_KEY = "urlMalformed";
    String URL_A_VALUE_KEY = "urlA";
    String BOGUS_VALUE_KEY = "bogus"; // this key does not exist in the config file
    String BOOLEAN_TRUE_VALUE_KEY = "booleanTrue"; // key to boolean true value
    String BOOLEAN_FALSE_VALUE_KEY = "booleanFalse"; // key to boolean false value
  }

  // -------------------------------------------------
  // TestResults
  // -------------------------------------------------

  public interface TestResults {
    String FLOAT_12_DOT_34_TEST_VALUE_AS_STR = "12.34";
    float FLOAT_12_DOT_34_TEST_VALUE = Float.parseFloat(FLOAT_12_DOT_34_TEST_VALUE_AS_STR);

    String URL_A_TEST_VALUE = "http://www.google.com/";
    String URL_DEFAULT_TEST_VALUE = "http://www.yahoo.com/";

    String DEFAULT_TEST_VALUE_AS_STR = "DEFAULT";
  }

  // ========================================================================
  // ConfigTest
  // ========================================================================

  @Test
  public void testLocateConfig() {
    Config config = ConfigLocator.locate();

    assertNotNull(config);
  }

  @Test
  public void testSimpleGet() {
    Config config = ConfigLocator.locate();
    String result = config.getValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.FLOAT_12_DOT_34_VALUE_KEY, null);
    assertEquals(TestResults.FLOAT_12_DOT_34_TEST_VALUE_AS_STR, result);
  }

  @Test
  public void testSimpleDefaultGet() {
    Config config = ConfigLocator.locate();

    // since key is bad, must return default value
    String result = config.getValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.BOGUS_VALUE_KEY, TestResults.DEFAULT_TEST_VALUE_AS_STR);
    assertEquals(TestResults.DEFAULT_TEST_VALUE_AS_STR, result);
  }

  @Test
  public void testGetBooleanValue() {
    Config config = ConfigLocator.locate();
    boolean result = config.getBooleanValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.BOOLEAN_TRUE_VALUE_KEY, false);
    assertTrue(result);
  }

  @Test
  public void testGetDefaultBooleanValue() {
    Config config = ConfigLocator.locate();
    boolean result = config.getBooleanValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.BOGUS_VALUE_KEY, false);
    assertFalse(result);
  }

  @Test
  public void testGetDefaultIntegerValue() {
    Config config = ConfigLocator.locate();
    int result = config.getIntegerValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.BOGUS_VALUE_KEY, -100);
    assertEquals(-100, result);
  }

  @Test
  public void testGetFloatValue() {
    Config config = ConfigLocator.locate();
    float result = config.getFloatValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.FLOAT_12_DOT_34_VALUE_KEY, 1.0f);
    assertEquals(TestResults.FLOAT_12_DOT_34_TEST_VALUE, result, 0);
  }

  @Test
  public void testGetDefaultFloatValue() {
    Config config = ConfigLocator.locate();
    float result = config.getFloatValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.BOGUS_VALUE_KEY, 1.0f);
    assertEquals(1.0f, result, 0);
  }

  @Test
  public void testGetUrlValue() {
    Config config = ConfigLocator.locate();
    URL result = config.getURLValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.URL_A_VALUE_KEY, null);
    assertEquals(TestResults.URL_A_TEST_VALUE, result.toExternalForm());
  }

  @Test
  public void testGetDefaultUrlValue() {
    Config config = ConfigLocator.locate();
    // since a bad key is passed in, it must return the default value.
    URL result = config.getURLValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.BOGUS_VALUE_KEY, null);
    assertNull(result);
  }

  @Test
  public void testGetMalformedUrlValue() {
    Config config = ConfigLocator.locate();
    try {
      // since config value is malformed, it must return the default value.
      URL result = config.getURLValue(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.URL_MALFORMED_VALUE_KEY, new URL(TestResults.URL_DEFAULT_TEST_VALUE));
      assertEquals(TestResults.URL_DEFAULT_TEST_VALUE, result.toExternalForm());
    } catch (MalformedURLException e) {
      fail("Deafult URL is not valid.");
    }
  }

  @Test
  public void testGetSectionAsProperties() {
    Config config = ConfigLocator.locate();
    Properties result = config.getSectionAsProperties(ConfigKeys.TEST_TYPES_SECTION_KEY);
    assertEquals(String.valueOf(true), result.getProperty(ConfigKeys.BOOLEAN_TRUE_VALUE_KEY));
    assertEquals(TestResults.FLOAT_12_DOT_34_TEST_VALUE_AS_STR, result.getProperty(ConfigKeys.FLOAT_12_DOT_34_VALUE_KEY));
    assertEquals(TestResults.URL_A_TEST_VALUE, result.getProperty(ConfigKeys.URL_A_VALUE_KEY));
  }

  @Test
  public void testGetSection() {
    Config config = ConfigLocator.locate();
    Map<String, String> result = config.getSection(ConfigKeys.TEST_TYPES_SECTION_KEY);
    assertEquals(String.valueOf(true), result.get(ConfigKeys.BOOLEAN_TRUE_VALUE_KEY));
    assertEquals(TestResults.FLOAT_12_DOT_34_TEST_VALUE_AS_STR, result.get(ConfigKeys.FLOAT_12_DOT_34_VALUE_KEY));
    assertEquals(TestResults.URL_A_TEST_VALUE, result.get(ConfigKeys.URL_A_VALUE_KEY));
  }

  @Test
  public void testValueExists() {
    Config config = ConfigLocator.locate();
    assertTrue(config.valueExists(ConfigKeys.TEST_TYPES_SECTION_KEY, ConfigKeys.URL_A_VALUE_KEY));
  }

}
