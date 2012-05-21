/*
 *   Copyright 2006-2011 George Norman
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

import com.thruzero.common.core.support.LogHelper;

/**
 * Deletes all visited files and directories.
 * <p>
 * <b>Example</b>:
 *
 * <pre>
 *   HierarchicalFileWalker walker = new HierarchicalFileWalker("/home/foo");
 *   walker.accept(new <b>DirectoryDeletingVisitor</b>());
 * </pre>
 *
 * @author George Norman
 */
public class DirectoryDeletingVisitor extends AbstractHierarchicalFileVisitor {
  private static final DirectoryDeletingVisitorLogHelper directoryDeletingVisitorLogHelper = new DirectoryDeletingVisitorLogHelper(DirectoryDeletingVisitor.class);

  // -----------------------------------------------------------
  // DirectoryDeletingVisitorLogHelper
  // -----------------------------------------------------------

  public static class DirectoryDeletingVisitorLogHelper extends LogHelper {
    public DirectoryDeletingVisitorLogHelper(final Class<?> clazz) {
      super(clazz);
    }

    public void logDeleteSubDirectory(final File directory) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("Deleting: " + directory.getAbsolutePath());
      }
    }

    public String logDeleteSubDirectoryError(final File directory) {
      String msg = "*** ERROR: Couldn't delete directory:'" + directory.getAbsolutePath() + "', canWrite=" + directory.canWrite();

      return msg;
    }
  }

  // =================================================================================
  // DirectoryDeletingVisitor
  // =================================================================================

  /**
   * @throws IllegalArgumentException if attempting to delete a file system root directory (e.g., "/home/")
   */
  @Override
  public void open(final File startDir) throws IOException {
    super.open(startDir);

    for (File file : File.listRoots()) {
      if (startDir.equals(file)) {
        throw new IllegalArgumentException("*** ERROR: Can't delete roots.");
      }
    }
  }

  @Override
  public void visitFile(final File file) throws IOException {
  }

  @Override
  public void visitDirectoryEnter(final File directory) throws IOException {
    // deletes directories on exit (see below)
  }

  @Override
  public void visitDirectoryLeave(final File directory) throws IOException {
    // delete the directory
    directoryDeletingVisitorLogHelper.logDeleteSubDirectory(directory);
    deepDeleteDir(directory);
    getStatus().incNumProcessed();
  }

  protected void deepDeleteDir(final File directory) throws IOException {
    // first delete all of the files from directory
    for (File file : directory.listFiles()) {
      if (file.isDirectory()) {
        deepDeleteDir(file);
      } else {
        file.delete();
      }
    }

    // now, delete the directory
    directoryDeletingVisitorLogHelper.logDeleteSubDirectory(directory);
    if (!directory.delete()) {
      String msg = directoryDeletingVisitorLogHelper.logDeleteSubDirectoryError(directory);
      getStatus().addMessage(msg);
    }
  }
}
