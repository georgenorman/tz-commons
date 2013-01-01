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
   *   &lt;ui:repeat value=&quot;#{column.panels}&quot; var=&quot;panel&quot;&gt;
   *      &lt;h:panelGroup rendered=&quot;#{panel.panelClass eq &apos;ListPanel&apos;}&quot;&gt;
   * </pre>
   * Causes the following error:
   * <pre>
   *     The class 'java.util.HashMap$Values' does not have the property 'panelClass'.
   *
   * Here's a partial stack trace:
   *
   *     javax.faces.model.ListDataModel.<init>(ListDataModel.java:79)
   *     com.sun.faces.facelets.component.UIRepeat.getDataModel(UIRepeat.java:257)
   * </pre>
   *
   * This hack wraps the given collection in an ArrayList, if not already a List.
   */
  public static <T> Collection<T> unmodifiableListHack(Collection<T> srcCollection) {
    if (srcCollection instanceof List) {
      return Collections.unmodifiableList((List<T>)srcCollection);
    } else {
      return Collections.unmodifiableList(new ArrayList<T>(srcCollection));
    }
  }

}
