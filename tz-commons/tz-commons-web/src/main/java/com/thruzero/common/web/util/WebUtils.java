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

package com.thruzero.common.web.util;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Static general-web utility methods.
 *
 * @author George Norman
 */
public class WebUtils {

  /**
   * HACK HACK HACK. TODO-p1(george) REVIST.
   * Using LinkedHashMap.values() or Collections.unmodifiableCollection with {@code <ui:repeat>},
   * causes exception:
   *
   * <pre>
   *   &lt;ui:repeat value=&quot;#{column.panels}&quot; var=&quot;panel&quot;&gt;
   *      &lt;h:panelGroup rendered=&quot;#{panel.panelClass eq &apos;ListPanel&apos;}&quot;&gt;
   * </pre>
   *
   * Causes this error: The class 'java.util.HashMap$Values' does not have the property 'panelClass'.
   * <p/>
   * This hack wraps the given collection in an ArrayList.
   */
  public static <T> Collection<T> uiRepeatHack(Collection<T> srcCollection) {
    return new ArrayList<T>(srcCollection);
  }

}
