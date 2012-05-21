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
import java.util.regex.Matcher;

import com.thruzero.common.core.support.PatternValuePair;

/**
 * A strategy that uses a list of pattern/value pairs to search a source string, using a {@link java.util.regex.Matcher}
 * , for each key and replaces matches with the associated value.
 */
public class RegExSubstitutionStrategy implements SubstitutionStrategy {
  private final List<? extends PatternValuePair> substitutionSpecs;

  /**
   * @param substitutionSpecs the RegEx/value pairs used for search and replace.
   */
  public RegExSubstitutionStrategy(final List<? extends PatternValuePair> substitutionSpecs) {
    this.substitutionSpecs = new ArrayList<PatternValuePair>(substitutionSpecs);
  }

  /**
   * @param substitutionSpecs the RegEx/value pairs used for search and replace.
   */
  public RegExSubstitutionStrategy(final PatternValuePair... substitutionSpecs) {
    this.substitutionSpecs = Arrays.asList(substitutionSpecs);
  }

  /**
   * Using the provided list of RegEx/value pairs, search and replace all matching substitution variables, in the given
   * {@code source} string, with their associated values.
   */
  @Override
  public String replaceAll(final String source) {
    String result = source;

    for (PatternValuePair patternValuePair : substitutionSpecs) {
      Matcher matcher = patternValuePair.getPattern().matcher(result);
      result = matcher.replaceAll(patternValuePair.getValue() == null ? "" : patternValuePair.getValueAsString());
    }

    return result;
  }
}
