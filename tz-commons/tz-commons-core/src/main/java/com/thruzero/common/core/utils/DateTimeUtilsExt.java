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

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * Extensions to the apache.commons {@code DateUtils} utility class.
 *
 * @author George Norman
 */
public class DateTimeUtilsExt extends DateUtils {

  /** Allow for class extensions; disallow client instantiation */
  protected DateTimeUtilsExt() {
  }

  /**
   * Return the parsed Date, using the default date format
   * {@link com.thruzero.common.core.utils.DateTimeFormatUtilsExt#MM_DD_YYYY_DATE_FORMAT_STRING}.
   *
   * @throws ParseException
   */
  public static Date stringToDate(final String value, final Date defaultValue) {
    return stringToDate(value, defaultValue, DateTimeFormatUtilsExt.MM_DD_YYYY_DATE_FORMAT_STRING);
  }

  /**
   * Return the parsed Date, using the given {@code parsePatterns}. If the value is null, then null is returned;
   * otherwise, each of the given patterns will be used to parse the value as a Date, until a successful parse or all
   * patterns have been exhausted, in which case a ParseException is thrown.
   *
   * @throws ParseException
   */
  public static Date stringToDate(final String value, final Date defaultValue, final String... parsePatterns) {
    Date result = null;

    if (value != null) {
      try {
        result = DateUtils.parseDate(value, parsePatterns);
      } catch (ParseException e) {
        // use the default value if no match was found
      }
    }

    if (result == null) {
      result = defaultValue;
    }

    return result;
  }

}
