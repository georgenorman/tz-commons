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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.thruzero.common.core.strategy.SubstitutionStrategy;

/**
 * Renames all visited files.
 * <p>
 * <b>Example</b>:
 *
 * <pre>
 * <code>
 *   SubstitutionStrategy strategy = new KeyValuePairSubstitutionStrategy(new KeyValuePair("${sub1}", "Value"));
 *   HierarchicalFileWalker walker = new HierarchicalFileWalker("/home/foo");
 *   walker.accept(new FileRenamingVisitor(strategy));
 * </code>
 * </pre>
 *
 * @author George Norman
 */
public class FileRenamingVisitor extends AbstractHierarchicalFileVisitor {
  private static final Logger logger = Logger.getLogger(FileRenamingVisitor.class);

  private final SubstitutionStrategy substitutionStrategy;

  /**
   * Substitutes the names of all files and directories whose name contains the given substitutionVariable with the
   * given substitutionValue.
   */
  public FileRenamingVisitor(final SubstitutionStrategy substitutionStrategy) {
    this.substitutionStrategy = substitutionStrategy;
  }

  @Override
  public void visitFile(final File file) throws IOException {
    doRenameFileOrDirectory(file);
  }

  @Override
  public void visitDirectoryEnter(final File dir) throws IOException {

  }

  /**
   * Renames each directory that matches the selection criteria. The selection criteria and value are provided by the
   * substitution strategy.
   */
  @Override
  public void visitDirectoryLeave(final File dir) throws IOException {
    doRenameFileOrDirectory(dir);
  }

  protected void doRenameFileOrDirectory(final File fileOrDir) throws IOException {
    String newName = substitutionStrategy.replaceAll(fileOrDir.getName());

    if (StringUtils.isNotEmpty(newName) && !fileOrDir.getName().equals(newName)) {
      if (logger.isDebugEnabled()) {
        logger.debug("  - renaming file = " + fileOrDir.getName() + " to " + newName);
      }
      fileOrDir.renameTo(new File(fileOrDir.getParent(), newName));
      getStatus().incNumProcessed();
    }
  }
}
