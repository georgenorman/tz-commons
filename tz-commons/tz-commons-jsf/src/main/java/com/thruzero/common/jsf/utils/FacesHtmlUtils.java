/*
 *   Copyright 2012 George Norman
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

package com.thruzero.common.jsf.utils;

import javax.faces.model.SelectItem;

/**
 *
 * @author George Norman
 */
public class FacesHtmlUtils {

  /**
   * create a sequence of SelectItem's starting from the given start param, incrementing by the given increment, until
   * the maxItems number of items has been completed.
   *
   * @param start first item value.
   * @param maxItems total number of items (plus one if provideEmptyValue is true)
   * @param increment
   * @param provideEmptyValue if true, then an empty SelectItem will be added as the first item.
   * @return
   */
  public static SelectItem[] createSelectSequence(int start, int maxItems, int increment, boolean provideEmptyValue) {
    maxItems += provideEmptyValue ? 1 : 0;
    SelectItem[] result = new SelectItem[maxItems];
    int item = start;

    if (provideEmptyValue) {
      result[0] = new SelectItem("");
    }

    for (int i = provideEmptyValue ? 1 : 0; i < maxItems; i++) {
      result[i] = new SelectItem(item + "");
      item += increment;
    }

    return result;
  }

}
