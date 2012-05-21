/*
 *   Copyright 2005-2011 George Norman
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

import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.thruzero.common.core.bookmarks.ConfigBookmark;
import com.thruzero.common.core.bookmarks.ConfigKeysBookmark;
import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.locator.Initializable;
import com.thruzero.common.core.map.IniMap;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.Singleton;

/**
 * An interface for reading values from a section-based config file (e.g., an <a
 * href="http://en.wikipedia.org/wiki/INI_file">INI</a> file).
 *
 * @author George Norman
 */
@ConfigBookmark(comment = "API")
public interface Config extends Singleton, IniMap, Initializable {

  // ------------------------------------------
  // ConfigInitParamKeys
  // ------------------------------------------

  /**
   * Initialization parameter keys defined for the {@code Config} interface. Parameters can be registered with the
   * {@code ConfigLocator} in a variety of ways. The easiest way is to use the {@link com.thruzero.common.core.locator.ConfigLocator#setup(String, String)}
   * convenience function, as shown below:
   *
   * <pre>
   * <code>
   * // specify the path to the config file, leaving the implementation class null (so the default config class will automatically be loaded).
   * ConfigLocator.setup("config/config.test.xml", null);
   * </code>
   * </pre>
   *
   * If the convenience function is deficient, then clients may manually register a
   * particular config implementation as shown below:
   *
   * <pre>
   * <code>
   * StringMap params = new StringMap();
   * params.put(ConfigInitParamKeys.CONFIG_FILE_PATH_PARAM, configFilePath);
   *
   * InterfaceToClassBinding binding = new InterfaceToClassBinding<Config>(Config.class.getName(), configImplClassName, params);
   * ConfigLocator.getRegistry().registerInterface(binding);
   * </code>
   * </pre>
   */
  @InitializationParameterKeysBookmark
  public interface ConfigInitParamKeys {
    /** Key that specifies the full path to the config file (e.g., "CONFIG_FILE_PATH=/home/foo/bar/config.xml") */
    String CONFIG_FILE_PATH_PARAM = "CONFIG_FILE_PATH";
  }

  // ------------------------------------------------
  // ConfigKeys
  // ------------------------------------------------

  /** An interface that defines keys used to lookup common values in a config file. */
  @ConfigKeysBookmark(comment = "Base interface for config keys.")
  public interface ConfigKeys { // enums are good for type-safety; interfaces are good for API, behavior and String consts
    /**
     * The section named "environment", that contains details related to a particular deployment (e.g., the environment section
     * for DEV will differ from the environment section for PRD).
     */
    String ENVIRONMENT_SECTION = "environment";

    /** The section named "debug" that allows some customization to the debug environment. */
    String DEBUG_SECTION = "debug";
  }

  // =============================================================
  // Config
  // =============================================================

  /**
   * Returns the requested string from the config file. Returns {@code null} if the {@code section} or {@code key} is not
   * found.
   *
   * @param sectionName name of config file section
   * @param key name of key within the named section
   */
  String getValue(String sectionName, String key);

  /**
   * Returns the requested string from the config file. Returns {@code defaultValue} if {@code section} or {@code key}
   * is not found.
   *
   * @param sectionName name of config file section
   * @param key name of key within the named section
   * @param defaultValue value to return if value not found
   */
  String getValue(String sectionName, String key, String defaultValue);

  /**
   * Returns the requested value from the config file, as a boolean. Returns {@code defaultValue} if {@code section} or
   * {@code key} is not found.
   *
   * @param sectionName name of config file section
   * @param key name of key within the named section
   * @param defaultValue value to return if value not found
   */
  boolean getBooleanValue(String sectionName, String key, boolean defaultValue);

  /**
   * Returns the requested value from the config file, as an int. Returns {@code defaultValue} if {@code section} or
   * {@code key} is not found.
   *
   * @param sectionName name of config file section
   * @param key name of key within the named section
   * @param defaultValue value to return if value not found
   */
  int getIntegerValue(String sectionName, String key, int defaultValue);

  /**
   * Returns the requested value from the config file, as a {@code long}. Returns {@code defaultValue} if
   * {@code section} or {@code key} is not found.
   *
   * @param sectionName name of config file section
   * @param key name of key within the named section
   * @param defaultValue value to return if value not found
   */
  long getLongValue(String sectionName, String key, long defaultValue);

  /**
   * Returns the requested value from the config file, as a {@code float}. Returns {@code defaultValue} if
   * {@code section} or {@code key} is not found.
   *
   * @param sectionName name of config file section
   * @param key name of key within the named section
   * @param defaultValue value to return if value not found
   */
  float getFloatValue(String sectionName, String key, float defaultValue);

  /**
   * Returns the requested value from the config file, as a {@code URL}. Returns {@code defaultValue} if {@code section}
   * or {@code key} is not found or the value generates a MalformedURLException.
   *
   * @param sectionName name of config file section
   * @param key name of key within the named section
   * @param defaultValue value to return if value not found
   */
  URL getURLValue(String sectionName, String key, URL defaultValue);

  /**
   * Returns the parsed Date, using the default date format
   * {@link com.thruzero.common.core.utils.DateTimeFormatUtilsExt#MM_DD_YYYY_DATE_FORMAT_STRING}.
   *
   * @throws ParseException
   */
  Date getValueAsDate(String sectionName, String key, Date defaultValue) throws ParseException; // @-@:p0 rename to getDateValue

  /**
   * Returns the parsed Date, using the given {@code parsePatterns}. If the value is null, then null is returned;
   * otherwise, each of the given patterns will be used to parse the value as a Date, until a successful parse or all
   * patterns have been exhausted, in which case a ParseException is thrown.
   *
   * @throws ParseException
   */
  public Date getValueAsDate(String sectionName, String key, Date defaultValue, String... parsePatterns) throws ParseException;

  /**
   * Returns all of the key/value pairs from the named section, as properties.
   */
  Properties getSectionAsProperties(String sectionName);

  /**
   * Returns all of the key/value pairs from the named section.
   */
  @Override
  StringMap getSectionAsStringMap(String sectionName);

  /**
   * Returns all of the key/value pairs from the named section.
   */
  Map<String, String> getSection(String sectionName);

  /**
   * Returns all of the section keys.
   */
  Set<String> getSectionNames();

  /**
   * Returns true if the requested value is found in the config file.
   */
  boolean valueExists(String sectionName, String key);

}
