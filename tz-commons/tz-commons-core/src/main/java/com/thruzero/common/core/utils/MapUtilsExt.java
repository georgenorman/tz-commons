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
package com.thruzero.common.core.utils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.infonode.InfoNodeElement;

/**
 * Extensions to the apache {@code MapUtils} utility class.
 *
 * @author George Norman
 */
public class MapUtilsExt extends MapUtils {

  /** Allow for class extensions; disallow client instantiation */
  protected MapUtilsExt() {
  }

  /**
   * Takes the comma separated series of keys and returns an array of {@code String} values for each key in the series
   * (trimming each key segment before lookup).
   *
   * @param commaSeparatedKeys series of key names (e.g., "foo, bar").
   * @return value for each key in the series (e.g., ["value-of-foo", "value-of-bar"]).
   */
  public static <K, V> String[] getValueAsStringSeries(final K key, final Map<K, V> map) {
    Object mapValue = map.get(key);

    if (mapValue == null) {
      return null;
    } else {
      return getValueAsStringSeries(mapValue.toString());
    }
  }

  public static String[] getValueAsStringSeries(final String series) {
    return StringUtilsExt.splitWithTrim(series, ",");
  }

  /**
   * Takes the comma separated series of keys and returns an array of int values for each key in the series (trimming
   * each key segment before lookup).
   *
   * @param key to a value in the map, that's a comma separated series of strings that are in-turn, each used to look up
   * a value in the map (e.g., "foo, bar").
   * @return value for each key in the series (e.g., int[] {123, 456}).
   */
  public static <K, V> int[] getValueAsIntSeries(final K key, final Map<K, V> map) {
    Object mapValue = map.get(key);

    if (mapValue == null) {
      return null;
    } else {
      String[] series = StringUtils.split(mapValue.toString(), ',');

      return getValueAsIntSeries(series);
    }
  }

  /**
   * Takes the comma separated series of keys and returns an array of int values for each key in the series (trimming
   * each key segment before lookup).
   */
  public static <K, V> int[] getValueAsIntSeries(final String[] series) {
    int[] result = null;

    try {
      result = new int[series.length];

      for (int i = 0; i < series.length; i++) {
        result[i] = Integer.parseInt(series[i].trim());
      }
    } catch (Exception e) {
      throw new RuntimeException("ERROR: getValueAsIntSeries failed to convert to int[]. Exception: " + e.getMessage(), e);
    }

    return result;
  }

  /**
   * Takes the comma separated series of keys and returns a {@code Map} of key/value pairs (trimming each key segment
   * before lookup).
   *
   * @param commaSeparatedKeys series of key names (e.g., "foo, bar").
   * @return key/value pair for each key in the series.
   */
  public static <K, V> Map<String, String> getValueAsStringMap(final K key, final Map<K, V> map) {
    Object mapValue = map.get(key);

    if (mapValue == null) {
      return null;
    } else {
      Map<String, String> result = new LinkedHashMap<String, String>();
      String[] pairs = StringUtils.split(mapValue.toString(), ']');

      for (String string : pairs) {
        String[] values = StringUtils.split(StringUtils.substring(string, 1, string.length() - 1), ',');
        result.put(values[0], values[1].trim());
      }

      return result;
    }
  }

  /**
   * Takes the comma separated series of keys and returns a {@code Map} of key/Integer-value pairs (trimming each key
   * segment before lookup).
   *
   * @param commaSeparatedKeys series of key names (e.g., "foo, bar").
   * @return key/Integer-value pair for each key in the series.
   */
  public static <K, V> Map<String, Integer> getValueAsIntegerMap(final K key, final List<InfoNodeElement> keyValueNodes) {
    Map<String, Integer> result = new LinkedHashMap<String, Integer>();

    for (InfoNodeElement kvPair : keyValueNodes) {
      result.put(kvPair.getAttributeValue("key"), Integer.valueOf(kvPair.getText().trim()));
    }

    return result;
  }

}
