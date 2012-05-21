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
package com.thruzero.common.core.strategy;

import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.map.StringMap;

/**
 * A strategy that uses a StringMap to search a source string for the given keys and as they are found, replaces each
 * with the associated value.
 * 
 * @author George Norman
 */
public class MapSubstitutionStrategy implements SubstitutionStrategy {
  private final String namePrefix;
  private final String nameSuffix;
  private final StringMap substitutionSpecs;

  /**
   * @param substitutionSpecs the name/value pairs used for search and replace, using "${" as the namePrefix and "}" as
   * the nameSuffix.
   */
  public MapSubstitutionStrategy(final StringMap substitutionSpecs) {
    this(substitutionSpecs, "${", "}");
  }

  /**
   * @param substitutionSpecs the name/value pairs used for search and replace.
   */
  public MapSubstitutionStrategy(final StringMap substitutionSpecs, final String namePrefix, final String nameSuffix) {
    this.substitutionSpecs = (StringMap)substitutionSpecs.clone();
    this.namePrefix = namePrefix;
    this.nameSuffix = nameSuffix;
  }

  /**
   * Using the provided list of name/value pairs, search and replace all matching substitution variables, in the given
   * {@code source} string, with their associated values.
   */
  @Override
  public String replaceAll(final String source) {
    String result = source;

    for (Entry<String, String> entry : substitutionSpecs.entrySet()) {
      result = StringUtils.replace(result, namePrefix + entry.getKey() + nameSuffix, entry.getValue());
    }

    return result;
  }

}
