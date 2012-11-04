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
package com.thruzero.common.core.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.EnvironmentHelper;

/**
 * Extensions to the apache {@code StringUtils} utility class.
 *
 * @author George Norman
 */
public class StringUtilsExt extends StringUtils {
  public static final String DEFAULT_SEPARATOR = ",";

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected StringUtilsExt() {
  }

  // string-token functions ///////////////////////////////////////////////////////////

  /**
   * Converts the given token stream of keyValuePairs, using the given separator, into a StringMap. Leading and trailing
   * spaces of the keys and values are trimmed.
   * <p>
   * Example input: "booleanTrue=true ,booleanFalse=false, integerOne=1,longOne=1234567890"
   */
  public static StringMap tokensToMap(final String keyValuePairs, final String separator) {
    StringMap result = new StringMap();

    if (StringUtils.isNotEmpty(keyValuePairs)) {
      StringTokenizer st = new StringTokenizer(keyValuePairs, separator);

      while (st.hasMoreTokens()) {
        String token = st.nextToken();
        StringTokenizer st2 = new StringTokenizer(token, "=");

        result.put(StringUtils.trimToEmpty(st2.nextToken()), st2.hasMoreTokens() ? StringUtils.trimToNull(st2.nextToken()) : null);
      }
    }

    return result;
  }

  /**
   * Converts the given token stream of keyValuePairs, using the default separator (","), into a StringMap. Leading and
   * trailing spaces of the keys and values are trimmed.
   * <p>
   * Example input: "booleanTrue=true ,booleanFalse=false, integerOne=1,longOne=1234567890"
   */
  public static StringMap tokensToMap(final String tokenStream) {
    return tokensToMap(tokenStream, DEFAULT_SEPARATOR);
  }

  /**
   * Create a byte array from the given {@code tokenStream}, using the given token {@code separator}. Leading and
   * trailing spaces of each token are trimmed.
   * <p>
   * Example input: "0xc3, 0x41, 0x55, 0xed, 0xf4, 0x41, 0x3e, 0x61".
   */
  public static byte[] tokensToByteArray(final String tokenStream, final String separator) {
    byte[] result;
    String[] stringArray = StringUtils.split(tokenStream, separator);
    int i = 0;

    result = new byte[stringArray.length];
    for (String string : stringArray) {
      int num = Short.decode(string.trim());
      result[i++] = (byte)num;
    }

    return result;
  }

  /**
   * Converts the given Map of {@code keyValuePairs} into a token stream, using the default separator(",").
   * <p>
   * Example: "integerOne=1,booleanTrue=true,longOne=1234567890,booleanFalse=false"
   */
  public static String toTokenStream(final Map<String, String> keyValuePairs) {
    return toTokenStream(keyValuePairs, DEFAULT_SEPARATOR);
  }

  /**
   * Converts the given Map of {@code keyValuePairs} into a token stream, using the given {@code separator}.
   * <p>
   * Example: "integerOne=1,booleanTrue=true,longOne=1234567890,booleanFalse=false"
   */
  public static String toTokenStream(final Map<String, String> keyValuePairs, final String separator) {
    StringBuilder result = new StringBuilder(keyValuePairs.size()*16); // assume 16-chars for each k/v token.
    String tokenSeparator = "";
    Map<String, String> parametersToStream = keyValuePairs;

    for (Entry<String, String> entry : parametersToStream.entrySet()) {
      result.append(tokenSeparator);
      result.append(entry.getKey());
      result.append("=");
      result.append(entry.getValue());
      tokenSeparator = separator;
    }

    return result.toString();
  }

  // conversion functions ///////////////////////////////////////////////////////////

  public static int stringToInt(final String value, final int defaultValue) {
    return value == null ? defaultValue : Integer.parseInt(value);
  }

  public static long stringToLong(final String value, final long defaultValue) {
    return value == null ? defaultValue : Long.parseLong(value);
  }

  public static float stringToFloat(final String value, final float defaultValue) {
    return value == null ? defaultValue : Float.parseFloat(value);
  }

  public static boolean stringToBoolean(final String value, final boolean defaultValue) {
    return value == null ? defaultValue : Boolean.valueOf(value);
  }

  /**
   * Return the parsed Date, using the default date format
   * {@link com.thruzero.common.core.utils.DateTimeFormatUtilsExt#MM_DD_YYYY_DATE_FORMAT_STRING}.
   *
   * @throws ParseException
   */
  public static Date stringToDate(final String value, final Date defaultValue) throws ParseException {
    return DateTimeUtilsExt.stringToDate(value, defaultValue);
  }

  /**
   * Return the parsed Date, using the given {@code parsePatterns}. If the value is null, then null is returned;
   * otherwise, each of the given patterns will be used to parse the value as a Date, until a successful parse or all
   * patterns have been exhausted, in which case a ParseException is thrown.
   *
   * @throws ParseException
   */
  public static Date stringToDate(final String value, final Date defaultValue, final String... parsePatterns) throws ParseException {
    return DateTimeUtilsExt.stringToDate(value, defaultValue, parsePatterns);
  }

  public static URL stringToUrl(final String value, final URL defaultValue) {
    URL result;

    if (value == null) {
      result = defaultValue;
    } else {
      try {
        result = new URL(value);
      } catch (MalformedURLException mue) {
        result = defaultValue;
      }
    }

    return result;
  }

  public static InputStream stringToInputStream(final String in) {
    return new ByteArrayInputStream(in.getBytes());
  }

  // utility functions /////////////////////////////////////////////////////////////

  /** Compare two strings, where null string is treated as "". */
  public static int compare(final String str1, final String str2) {
    String s1 = (str1 == null ? StringUtils.EMPTY : str1);
    String s2 = (str2 == null ? StringUtils.EMPTY : str2);

    return s1.compareTo(s2);
  }

  /** Return iterator across all lines of a {@code String}. */
  public static Iterator<String> createLineIterator(final String str) {
    String[] array = StringUtils.split(str, EnvironmentHelper.NEWLINE);
    List<String> result = Arrays.asList(array);

    return result.iterator();
  }

  public static String[] splitWithTrim(final String tokenStream, final String separator) {
    // TODO-p1(george) Optimize
    String[] result = StringUtils.split(tokenStream, separator);

    for (int i = 0; i < result.length; i++) {
      result[i] = result[i].trim();
    }

    return result;
  }

}
