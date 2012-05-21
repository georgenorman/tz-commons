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
package com.thruzero.common.core.support;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.utils.StringUtilsExt;

/**
 * Transform a value from it's native type to boolean, String, int, long, etc.
 *
 * @author George Norman
 * @param <T> Type of value object to be transformed.
 */
public class ValueTransformer<T> {
  private T value;

  public ValueTransformer(T value) {
    this.value = value;
  }

  /** Return true if the value given at construction time is null or its String value is "". */
  public boolean isEmptyValue() {
    return StringUtils.isEmpty(getStringValue());
  }

  /** Returns the value as a String, if present; otherwise, returns null. */
  public String getStringValue() {
    return value == null ? null : value.toString();
  }

  public String getStringValue(final String defaultValue) {
    String result = getStringValue();

    return StringUtils.isEmpty(result) ? defaultValue : result;
  }

  public int getIntValue(final int defaultValue) {
    return StringUtilsExt.stringToInt(getStringValue(), defaultValue);
  }

  public long getLongValue(final long defaultValue) {
    return StringUtilsExt.stringToLong(getStringValue(), defaultValue);
  }

  public float getFloatValue(final float defaultValue) {
    return StringUtilsExt.stringToFloat(getStringValue(), defaultValue);
  }

  public boolean getBooleanValue() {
    return getBooleanValue(false);
  }

  public boolean getBooleanValue(final boolean defaultValue) {
    return StringUtilsExt.stringToBoolean(getStringValue(), defaultValue);
  }

  /**
   * Return the parsed Date, using the default date format
   * {@link com.thruzero.common.core.utils.DateTimeFormatUtilsExt#MM_DD_YYYY_DATE_FORMAT_STRING}.
   *
   * @throws ParseException
   */
  public Date getDateValue() throws ParseException {
    return StringUtilsExt.stringToDate(getStringValue(), null);
  }

  /**
   * Return the parsed Date, using the given {@code parsePatterns}. If the value is null, then null is returned;
   * otherwise, each of the given patterns will be used to parse the value as a Date, until a successful parse or all
   * patterns have been exhausted, in which case a ParseException is thrown.
   *
   * @throws ParseException
   */
  public Date getDateValue(final String... parsePatterns) throws ParseException {
    return StringUtilsExt.stringToDate(getStringValue(), null, parsePatterns);
  }

  public String getAsRichText() {
    // TODO-p1(george) quick and dirty hack. Expand on this using RegExSubstitutionStrategy (or find a better way - e.g., xml escape)
    return StringUtils.replace(getStringValue(), "${break}", "<br/>");
  }

}
