/*
 *   Copyright 2013 George Norman
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Static UI utility methods.
 *
 * @author George Norman
 */
public class UiUtils {

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected UiUtils() {
  }

  /**
   * TODO-p1(george) REVIST.
   * Using LinkedHashMap.values() or Collections.unmodifiableCollection with {@code <ui:repeat>},
   * causes exception.
   * <p/>
   * Example:
   * <pre>
   *   &lt;ui:repeat value=&quot;#{panelSet.panels}&quot; var=&quot;panel&quot;&gt;
   *      &lt;tbsb:rssPanel title=&quot;#{panel.title}&quot; feed=&quot;#{panel.rssFeed}&quot; /&gt;
   * </pre>
   * The {@code <ui:repeat>} tag doesn't work with collections that aren't instances of List.
   * When a non-List collection is used, a ScalarDataModel is returned, which causes the following error
   * (see UIRepeat.getDataModel()):
   * <pre>
   *     The class 'java.util.HashMap$Values' does not have the property 'title'.
   * </pre>
   *
   * This hack returns an UnmodifiableList of the given collection if it is a List; otherwise, it
   * copies the given collection into a List and returns an UnmodifiableList on that.
   */
  public static <T> Collection<T> unmodifiableListHack(Collection<T> srcCollection) {
    if (srcCollection instanceof List) {
      return Collections.unmodifiableList((List<T>)srcCollection);
    } else {
      return Collections.unmodifiableList(new ArrayList<T>(srcCollection));
    }
  }
}
