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
package com.thruzero.common.core.locator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.map.StringMap;

/**
 * Static utility methods pertaining to location and initialization of <code>Singleton</code> objects.
 *
 * @author George Norman
 */
public class LocatorUtils {

  /**
   * Return the required initialization parameter and if empty, throw an InitializationException.
   *
   * @param params the parameters read from the InitializationStrategy.
   * @param sectionName the section the params were read from (used only for error reporting).
   * @param key the parameter key used to read the value.
   * @param initStrategy the initialization strategy that was the source of the params (used only for error reporting).
   */
  public static String getRequiredParam(final StringMap params, final String sectionName, final String key, final InitializationStrategy initStrategy) {
    String result = params.get(key);

    if (StringUtils.isEmpty(result)) {
      throw InitializationException.createMissingKeyInitializationException(sectionName, key, initStrategy);
    }

    return result;
  }

  /**
   * Test the given initParameter and if empty, throw an InitializationException.
   *
   * @param initParameter the parameter value read from the InitializationStrategy.
   * @param sectionName the section the params were read from (used only for error reporting).
   * @param key the parameter key used to read the value (used only for error reporting).
   * @param initStrategy the initialization strategy that was the source of the params (used only for error reporting).
   */
  public static void assertInitParameterExists(final String initParameter, final String sectionName, final String key, final InitializationStrategy initStrategy) {
    if (StringUtils.isEmpty(initParameter)) {
      throw InitializationException.createMissingKeyInitializationException(sectionName, key, initStrategy);
    }
  }

  /**
   * For a given childClass, start at the root base class and read the sections of each sub-class, walking down the class hierarchy
   * to the given childClass. The result is that all parameters from all inherited sections, including the given childClass,
   * are read with each subclass overwriting the common keys of any parent section. If given, optionalBaseInterface will be used as the
   * root base and placed at the top of the inheritance stack.
   *
   * @param sectionName
   * @param initStrategy
   * @param childClass
   */
  public static StringMap getInheritedParameters(final InitializationStrategy initStrategy, final Class<?> childClass, final Class<?> optionalBaseInterface) {
    StringMap result = new StringMap();
    List<Class<?>> inheritanceList = new  ArrayList<Class<?>>();

    Class<?> nextClass = childClass;
    while (nextClass != null) {
      inheritanceList.add(nextClass);
      nextClass = nextClass.getSuperclass();
    }
    inheritanceList.remove(inheritanceList.size()-1); // remove java.lang.Object

    if (optionalBaseInterface != null) {
      inheritanceList.add(optionalBaseInterface);
    }

    ListIterator<Class<?>> iter = inheritanceList.listIterator(inheritanceList.size());
    StringMap params;
    while (iter.hasPrevious()) {
      params = initStrategy.getSectionAsStringMap(iter.previous().getName());
      if (params != null) {
        result.putAll(params);
      }
    }

    return result;
  }
}
