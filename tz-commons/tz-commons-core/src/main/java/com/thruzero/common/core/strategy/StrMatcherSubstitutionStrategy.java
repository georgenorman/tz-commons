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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.text.StrSubstitutor;

import com.thruzero.common.core.support.KeyValuePair;

/**
 * A strategy that uses a StrSubstitutor to search a source string for the given keys and as they are found, replaces
 * each with an associated value.
 *
 * @author George Norman
 */
public class StrMatcherSubstitutionStrategy implements SubstitutionStrategy {
  private final StrSubstitutor strSubstitutor;

  public StrMatcherSubstitutionStrategy(final StrSubstitutor strSubstitutor) {
    this.strSubstitutor = strSubstitutor;
  }

  public <V> StrMatcherSubstitutionStrategy(final Map<String, V> valueMap, final String prefix, final String suffix, final char escape) {
    this.strSubstitutor = new StrSubstitutor(valueMap, prefix, suffix, escape);
  }

  /**
   * @param substitutionSpecs the key/value pairs used for search and replace.
   */
  public StrMatcherSubstitutionStrategy(final KeyValuePair... substitutionSpecs) {
    this("${", "}", '$', substitutionSpecs);
  }

  /**
   * @param substitutionSpecs the key/value pairs used for search and replace.
   */
  public StrMatcherSubstitutionStrategy(final String prefix, final String suffix, final char escape, final KeyValuePair... substitutionSpecs) {
    Map<String, Object> valueMap = new HashMap<String, Object>();

    for (KeyValuePair keyValuePair : substitutionSpecs) {
      valueMap.put(keyValuePair.getKey(), keyValuePair.getValue());
    }

    // no prefix or suffix is used with this version
    this.strSubstitutor = new StrSubstitutor(valueMap, prefix, suffix, escape);
  }

  @Override
  public String replaceAll(final String source) {
    return strSubstitutor.replace(source);
  }

}
