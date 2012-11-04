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
package com.thruzero.common.web.util;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author George Norman
 */
public class HtmlUtils {

  // ---------------------------------------------
  // TableUtils
  // ---------------------------------------------

  public static class TableUtils {

    public static String createSimpleFormRow(final String label, final String value) {
      String result = "<tr><td>" + label + "</td><td>" + value + "</td></tr>";

      return result;
    }

    public static String createSimpleFormRow(final String label, final String href, final String value) {
      String result = "<tr><td><a href=\"" + href + "\">" + label + "</a></td><td>" + value + "</td></tr>";

      return result;
    }
  }

  // =============================================================
  // HtmlUtils
  // =============================================================

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected HtmlUtils() {
  }

  /**
   * @param text
   * @return given text with <br>
   * after each new line.
   */
  public static String formatLinefeedWrap(String text) {
    if (text == null) {
      text = "";
    }
    return StringUtils.replace(text, "\n", "<br>\n");
  }

  public static String appendQueryParam(final String url, final String key, final String value) {
    StringBuffer result = new StringBuffer(url);

    if (url.indexOf('?') > 0) {
      result.append("&");
    } else {
      result.append("?");
    }
    result.append(key + "=" + value);

    return result.toString();
  }
}
