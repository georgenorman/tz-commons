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
package com.thruzero.common.core.fs;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.thruzero.common.core.fs.walker.visitor.SubstitutionVisitor;
import com.thruzero.common.core.strategy.KeyValuePairSubstitutionStrategy;
import com.thruzero.common.core.strategy.RegExSubstitutionStrategy;
import com.thruzero.common.core.strategy.SubstitutionStrategy;
import com.thruzero.common.core.support.KeyValuePair;
import com.thruzero.common.core.support.PatternValuePair;
import com.thruzero.common.core.utils.FileUtilsExt;
import com.thruzero.common.core.utils.FileUtilsExt.FileUtilsException;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for SubstitutionVisitor.
 *
 * @author George Norman
 */
public class SubstitutionVisitorTest extends AbstractCoreTestCase {
  public static final String SUBSTITUTION_TEST_FILE_NAME = "substitution-test.txt";
  public static final String SUBSTITUTION_VAR_1_NAME = "substitution-var-1";
  public static final String SUBSTITUTION_VAR_1 = "${"+SUBSTITUTION_VAR_1_NAME+"}";
  public static final String SUBSTITUTION_VAR_2_NAME = "substitution-var-2";
  public static final String SUBSTITUTION_VAR_2 = "${"+SUBSTITUTION_VAR_2_NAME+"}";
  public static final String SUBSTITUTION_VALUE_1 = "SubstitutionOne";
  public static final String SUBSTITUTION_VALUE_2 = "SubstitutionTwo";
  public static final String REGEX_VAR_1 = "\\$\\{substitution-var-1\\}";
  public static final String REGEX_VAR_2 = "\\$\\{substitution-var-2\\}";

  /** Simple key/value pair substitution (e.g., "${foo}" substituted using key="${foo}"). */
  @Test
  public void testSimpleKeyValuePairSubstitution() {
    // copy test directory to a temp directory, as a substitution target
    File substitutionTestFile = copyFileToTemp(SUBSTITUTION_TEST_FILE_NAME);

    // read contents
    String originalContents = getFileContents(substitutionTestFile, AssertOption.ASSERT_NOT_NULL);

    // walk the temp directory (substitutions only on copied test file)
    try {
      KeyValuePair[] substitutionSpecs = new KeyValuePair[] { new KeyValuePair(SUBSTITUTION_VAR_1, SUBSTITUTION_VALUE_1), new KeyValuePair(SUBSTITUTION_VAR_2, SUBSTITUTION_VALUE_2) };
      SubstitutionStrategy strategy = new KeyValuePairSubstitutionStrategy(substitutionSpecs);
      new HierarchicalFileWalker(substitutionTestFile.getParentFile()).accept(new SubstitutionVisitor(strategy));

      validateTest(substitutionTestFile, originalContents);
    } catch (IOException e) {
      fail("SubstitutionFileWalker generated exception: " + e);
    }
  }

  @Test
  public void testSimpleKeyValuePairSubstitutionUsingNullValue() {
    // copy file to temp directory, so test can be run multiple times without m2 clean
    copyFileToTemp(SUBSTITUTION_TEST_FILE_NAME);

    // read contents
    File tempTestFile = getTempTestFile(SUBSTITUTION_TEST_FILE_NAME);
    String originalContents = getFileContents(tempTestFile, AssertOption.ASSERT_NOT_NULL);

    // walk the temp directory (substitutions only on copied test file)
    try {
      SubstitutionStrategy strategy = new KeyValuePairSubstitutionStrategy(new KeyValuePair(SUBSTITUTION_VAR_1), new KeyValuePair(SUBSTITUTION_VAR_2, SUBSTITUTION_VALUE_2));

      new HierarchicalFileWalker(tempTestFile.getParentFile()).accept(new SubstitutionVisitor(strategy));

      validateTest(tempTestFile, originalContents);
    } catch (IOException e) {
      fail("SubstitutionFileWalker generated exception: " + e);
    }
  }

  private void validateTest(final File tempTestFile, final String originalContents) {
    String substitutedContents = null;
    try {
      substitutedContents = FileUtilsExt.readFromFile(tempTestFile);
    } catch (FileUtilsException e1) {
      // ignore
    }
    assertNotNull("Could not read substitution file after walk.", substitutedContents);
    assertFalse("Substitution failed - files are same before and after substitution.", StringUtils.equals(originalContents, substitutedContents));
    assertNull("Substitution failed - files contain substitution variables.", StringUtils.substringBetween(substitutedContents, "${", "}"));
    assertFalse("Substitution failed - files contain remnants of substitution variables.", StringUtils.contains(substitutedContents, "${"));
    assertFalse("Substitution failed - files contain remnants of substitution variables.", StringUtils.contains(substitutedContents, "$"));
    assertFalse("Substitution failed - files contain remnants of substitution variables.", StringUtils.contains(substitutedContents, "}"));
  }

  /** Simple key/value pair substitution (e.g., "${foo}" substituted using regex="\$\{foo\}"). */
  @Test
  public void testSimpleRegExSubstitution() {
    // copy file to temp directory, so test can be run multiple times without m2 clean
    copyFileToTemp(SUBSTITUTION_TEST_FILE_NAME);

    // read contents
    File tempTestFile = getTempTestFile(SUBSTITUTION_TEST_FILE_NAME);
    String originalContents = getFileContents(tempTestFile, AssertOption.ASSERT_NOT_NULL);

    // walk the temp directory (substitutions only on copied test file)
    try {
      PatternValuePair[] substitutionSpecs = new PatternValuePair[] { new PatternValuePair(REGEX_VAR_1, SUBSTITUTION_VALUE_1), new PatternValuePair(REGEX_VAR_2, SUBSTITUTION_VALUE_2) };

      new HierarchicalFileWalker(tempTestFile.getParentFile()).accept(new SubstitutionVisitor(new RegExSubstitutionStrategy(substitutionSpecs)));

      validateTest(tempTestFile, originalContents);
    } catch (IOException e) {
      fail("SubstitutionFileWalker generated exception: " + e);
    }
  }

}
