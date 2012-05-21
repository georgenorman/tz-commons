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
package com.thruzero.common.core.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.support.KeyValuePair;

/**
 * A strategy that uses a list of key/value pairs to search a source string for the given keys and as they are found,
 * replaces each with the associated value.
 * 
 * @author George Norman
 */
public class KeyValuePairSubstitutionStrategy implements SubstitutionStrategy {
  private final String namePrefix;
  private final String nameSuffix;
  private final List<? extends KeyValuePair> substitutionSpecs;

  /**
   * @param substitutionSpecs the name/value pairs used for search and replace.
   */
  public KeyValuePairSubstitutionStrategy(final List<? extends KeyValuePair> substitutionSpecs) {
    this(substitutionSpecs, "", "");
  }

  /**
   * @param substitutionSpecs the name/value pairs used for search and replace.
   */
  public KeyValuePairSubstitutionStrategy(final KeyValuePair... substitutionSpecs) {
    this(Arrays.asList(substitutionSpecs), "", "");
  }

  /**
   * @param substitutionSpecs the name/value pairs used for search and replace.
   */
  public KeyValuePairSubstitutionStrategy(final List<? extends KeyValuePair> substitutionSpecs, final String namePrefix, final String nameSuffix) {
    this.substitutionSpecs = new ArrayList<KeyValuePair>(substitutionSpecs);
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

    for (KeyValuePair keyValuePair : substitutionSpecs) {
      result = StringUtils.replace(result, namePrefix + keyValuePair.getKey() + nameSuffix, keyValuePair.getValueAsString());
    }

    return result;
  }
}
