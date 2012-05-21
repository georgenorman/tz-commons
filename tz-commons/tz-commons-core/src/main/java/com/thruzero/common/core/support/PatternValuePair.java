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
package com.thruzero.common.core.support;

import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.MutablePair;

/**
 * An instance represents a pattern/value pair, consisting of a RegEx {@code Pattern} and a corresponding {@code String}
 * replacement value, which can be passed into a function to perform substitutions.
 * <p>
 * The example below shows three PatternValuePair instances used by the HierarchicalFileWalker to perform a search and
 * replace on the contents of files in a given directory:
 * 
 * <pre>
 * <code>
 *   // Replace all email addresses with "***", all occurrences of "${substitution-var-1}" with "foo" and "substitution-var-2" with "bar"
 *   PatternValuePair hideEmailAddress = new PatternValuePair("\\w+@\\w+\\.\\w+", "***");
 *   PatternValuePair replaceSubVar1 = new PatternValuePair("\\$\\{substitution-var-1\\}", "foo");
 *   PatternValuePair replaceSubVar2 = new PatternValuePair("\\$\\{substitution-var-2\\}", "bar");
 * 
 *   new HierarchicalFileWalker(testDir, filter).accept(new SubstitutionVisitor(new RegExSubstitutionStrategy(hideEmailAddress, replaceSubVar1, replaceSubVar2)));
 * </code>
 * </pre>
 * 
 * @author George Norman
 */
public class PatternValuePair extends MutablePair<Pattern, Object> {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a {@code PatternValuePair} where all {@code regex} matches are to be replaced with the given integer
   * {@code value}.
   */
  public PatternValuePair(final String regex, final int value) {
    this(Pattern.compile(regex), Integer.toString(value));
  }

  /**
   * Constructs a {@code PatternValuePair} where all {@code pattern} matches are to be replaced with the given integer
   * {@code value}.
   */
  public PatternValuePair(final Pattern pattern, final int value) {
    this(pattern, Integer.toString(value));
  }

  /**
   * Constructs a {@code PatternValuePair} where all {@code regex} matches are to be replaced with the given long
   * {@code value}.
   */
  public PatternValuePair(final String regex, final long value) {
    this(Pattern.compile(regex), Long.toString(value));
  }

  /**
   * Constructs a {@code PatternValuePair} where all {@code pattern} matches are to be replaced with the given integer
   * {@code value}.
   */
  public PatternValuePair(final Pattern pattern, final long value) {
    this(pattern, Long.toString(value));
  }

  /**
   * Constructs a {@code PatternValuePair} where all {@code regex} matches are to be replaced with the given String
   * {@code value}.
   */
  public PatternValuePair(final String regex, final Object value) {
    this(Pattern.compile(regex), value);
  }

  /**
   * Constructs a {@code PatternValuePair} where all {@code pattern} matches are to be replaced with the given String
   * {@code value}.
   */
  public PatternValuePair(final Pattern pattern, final Object value) {
    super(pattern, value);
  }

  public Pattern getPattern() {
    return getKey();
  }

  public void setPattern(final Pattern pattern) {
    setLeft(pattern);
  }

  /** Return the value as a String, by calling its toString() method; if value is null, then return EMPTY String. */
  public String getValueAsString() {
    return getValue() == null ? "" : getValue().toString();
  }
}
