/*
 *   Copyright 2010 George Norman
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * Extensions to the apache {@code DateFormatUtils} utility class that adds additional Date and time formatting
 * functions.
 * 
 * @author George Norman
 */
public class DateTimeFormatUtilsExt extends DateFormatUtils {

  /** Default date format */
  public static final String MM_DD_YYYY_DATE_FORMAT_STRING = "MM/dd/yyyy";

  /** Default time format */
  public static final String HH_MM_AMPM_TIME_FORMAT_STRING = "hh:mm a";

  /** Allow for class extensions; disallow client instantiation */
  protected DateTimeFormatUtilsExt() {
  }

  /** format the given date as: "MM/dd/yyyy". */
  public static String formatMmDdYyyy(final Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY_DATE_FORMAT_STRING);

    return sdf.format(date);
  }

  /** format the given date as: "MM/dd/yyyy at hh:mm a". */
  public static String formatMmDdYyyyHhMm(final Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat(MM_DD_YYYY_DATE_FORMAT_STRING + " 'at' " + HH_MM_AMPM_TIME_FORMAT_STRING);

    return sdf.format(date);
  }

  /** format the given date in MySQL format: "yyyy-MM-dd". */
  public static String formatMySql(final Date date) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    return sdf.format(date);
  }

  /** Return the {@code Calendar.DATE} portion of the given {@code dateTime}. */
  public static Date truncateTime(final Date dateTime) {
    return DateUtils.truncate(dateTime, Calendar.DATE);
  }

  public static String formatElapsedTime(final long elapsedTime) {
    String result = formatElapsedTime(0, elapsedTime);

    return result;
  }

  public static String formatElapsedTime(final long startMillis, final long endMillis) {
    String result = DurationFormatUtils.formatPeriod(startMillis, endMillis, "s.SSS");

    return result;
  }

}
