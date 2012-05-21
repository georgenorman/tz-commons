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
package com.thruzero.common.core.fs.walker.visitor;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.thruzero.common.core.strategy.SubstitutionStrategy;
import com.thruzero.common.core.utils.FileUtilsExt;
import com.thruzero.common.core.utils.FileUtilsExt.FileUtilsException;

/**
 * Performs a global search and replace on the contents of every visited file. The <a
 * href="http://en.wikipedia.org/wiki/Strategy_pattern">strategy</a> pattern is used to provide the actual substitution
 * functionality, enabling a variety of search and replace options. Currently, there are two {@code Strategy}
 * implementations available (and new ones can be added by implementing the
 * {@link com.thruzero.common.core.strategy.SubstitutionStrategy SubstitutionStrategy} interface):
 * <ul>
 * <li>{@link com.thruzero.common.core.strategy.KeyValuePairSubstitutionStrategy KeyValuePairSubstitutionStrategy} -
 * uses a list of name/value pairs and searches a source string for each key and when found, replaces it with the
 * associated value.
 * <li>{@link com.thruzero.common.core.strategy.RegExSubstitutionStrategy RegExSubstitutionStrategy} - uses a list of
 * RegEx/value pairs and searches a source string, using a {@link java.util.regex.Matcher}, for each key and replaces
 * matches with the associated value.
 * </ul>
 *
 * <b>Example</b>:
 *
 * <pre>
 *   PatternValuePair patternValue = new PatternValuePair("\\$\\{substitution-var-1\\}", "SubstitutionValue");
 *   HierarchicalFileWalker walker = new HierarchicalFileWalker("/home/foo");
 *   walker.accept(new <b>SubstitutionVisitor</b>(new RegExSubstitutionStrategy(patternValue)));
 * </pre>
 *
 * @author George Norman
 */
public class SubstitutionVisitor extends AbstractHierarchicalFileVisitor {
  private final SubstitutionStrategy substitutionStrategy;

  public SubstitutionVisitor(final SubstitutionStrategy substitutionStrategy) {
    this.substitutionStrategy = substitutionStrategy;
  }

  /**
   * For given {@code file}, reads its contents to a String and replaces each substitution variable that matches the
   * selection criteria and writes the results back out to the file, if the contents were modified. The selection
   * criteria and value are provided by the substitution strategy.
   */
  @Override
  public void visitFile(final File file) throws IOException {
    String contents = FileUtils.readFileToString(file, FileUtilsExt.STANDARD_ENCODING);
    String newContents = substitutionStrategy.replaceAll(contents);

    if (!contents.equals(newContents)) {
      try {
        FileUtilsExt.writeToFile(file, newContents);
        getStatus().incNumProcessed();
      } catch (FileUtilsException e) {
        throw new IOException("ERROR: Could not write updated file.", e);
      }
    }
  }

  @Override
  public void visitDirectoryEnter(final File dir) throws IOException {

  }

  @Override
  public void visitDirectoryLeave(final File dir) throws IOException {

  }
}
