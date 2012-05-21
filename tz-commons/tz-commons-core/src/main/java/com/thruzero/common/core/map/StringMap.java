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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.thruzero.common.core.support.ValueTransformer;
import com.thruzero.common.core.utils.DateTimeFormatUtilsExt;

/**
 * A {@code Map} of {@code String} objects with convenience functions to get and put values as {@code String},
 * {@code int}, {@code boolean}, {@code Date}, etc.
 *
 * @author George Norman
 */
public class StringMap extends HashMap<String, String> implements Iterable<Entry<String, String>> {
  private static final long serialVersionUID = 1L;

  public StringMap() {
  }

  /**
   * Initialized from a series of pipe-separated key value pairs.
   * <p>
   * Example:
   *
   * <pre>
   * <code>
   * StringMap map = new StringMap("key1|val1", "key2|val2");
   * </code>
   * </pre>
   */
  public StringMap(final String... keyValuePairs) {
    if (keyValuePairs != null) {
      for (String string : keyValuePairs) {
        String[] keyValue = StringUtils.split(string, '|');
        if (keyValue.length != 2) {
          throw new RuntimeException("* ERROR: Each key value pair must be separated by the pipe character. This key value pair was not: " + string);
        }

        put(keyValue[0], keyValue[1]);
      }
    }
  }

  /**
   * Constructs a {@code StringMap}, using the {@link #putAllWithConversionToString(Map)} method to convert the values
   * to strings.
   */
  @SuppressWarnings("unchecked")
  public StringMap(final Map<String, ?> keyValues) {
    putAll((Map<? extends String, ? extends String>)keyValues);
  }

  /**
   * Return the specified value as a ValueTransformer. If the value is null, a ValueTransformer will be returned constructed from the null.
   */
  public ValueTransformer<String> getValueTransformer(String key) {
    return new ValueTransformer<String>(get(key));
  }

  public void putBoolean(final String key, final boolean value) {
    put(key, Boolean.toString(value));
  }

  /**
   * Put the value, as a String, using the default date format
   * {@link com.thruzero.common.core.utils.DateTimeFormatUtilsExt#MM_DD_YYYY_DATE_FORMAT_STRING}.
   */
  public void putDate(final String key, final Date value) {
    SimpleDateFormat sdf = new SimpleDateFormat(DateTimeFormatUtilsExt.MM_DD_YYYY_DATE_FORMAT_STRING);

    if (value == null) {
      put(key, null);
    } else {
      put(key, sdf.format(value));
    }
  }

  /**
   * Put the value, as a String, using the given {@code dateFormat}
   */
  public void putDate(final String key, final Date value, final DateFormat dateFormat) {
    if (value == null) {
      put(key, null);
    } else {
      put(key, dateFormat.format(value));
    }
  }

  /**
   * Associates all values, from the given {@code Map}, with this {@code StringMap}, using the conversion functions of
   * {@code StringMap}, to convert each {@code Object} to a {@code String}. If {@code pm} contains a value of
   * {@code String[]}, then only the first value from the array will be used.
   */
  public void putAllWithConversionToString(final Map<String, ?> pm) {
    SimpleDateFormat sdf = null;

    for (Entry<String, ?> entry : pm.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      if (value instanceof String[]) {
        put(key, ((String[])value)[0]);
      } else if (value instanceof Date) {
        if (sdf == null) {
          sdf = new SimpleDateFormat(DateTimeFormatUtilsExt.MM_DD_YYYY_DATE_FORMAT_STRING);
        }
        put(key, sdf.format(value));
      } else {
        put(key, value.toString());
      }
    }
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @Override
  public Iterator<java.util.Map.Entry<String, String>> iterator() {
    return entrySet().iterator();
  }
}
