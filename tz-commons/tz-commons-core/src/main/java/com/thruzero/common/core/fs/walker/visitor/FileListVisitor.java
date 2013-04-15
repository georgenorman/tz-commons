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
import java.util.ArrayList;
import java.util.List;

import com.thruzero.common.core.fs.HierarchicalFileWalker.FileWalkerStatus;

/**
 * Generates a list of visited files.
 * <p>
 * <b>Example</b>:
 * 
 * <pre>
 *   FileAndDirectoryFilter filter = new FileAndDirectoryFilter((FileFilter)DirectoryFileFilter.INSTANCE, new WildcardFilter("*test*"));
 *   HierarchicalFileWalker walker = new HierarchicalFileWalker("/home/foo", filter);
 *   walker.accept(new <b>FileListVisitor</b>(false));
 * </pre>
 * 
 * @author George Norman
 */
public class FileListVisitor extends AbstractHierarchicalFileVisitor {

  // ----------------------------------------------------------
  // FileListStatus
  // ----------------------------------------------------------

  /**
   * A specialization of {@code FileWalkerStatus} that includes a list of the files that were matched by the FileFilter.
   */
  public static interface FileListStatus extends FileWalkerStatus {
    void addFileResult(File file);

    List<File> getResults();
  }

  // ----------------------------------------------------------
  // MutableFileListStatus
  // ----------------------------------------------------------

  public class MutableFileListStatus extends MutableFileWalkerStatus implements FileListStatus {
    private final List<File> resultList = new ArrayList<File>();

    protected MutableFileListStatus(final File rootDir) {
      super(rootDir);
    }

    @Override
    public void addFileResult(final File file) {
      resultList.add(file);
      incNumProcessed();
    }

    @Override
    public List<File> getResults() {
      return resultList;
    }
  }

  // ========================================================================
  // FileListVisitor
  // ========================================================================

  @Override
  public void visitFile(final File file) throws IOException {
    ((FileListStatus) getStatus()).addFileResult(file);
  }

  @Override
  public void visitDirectoryEnter(final File dir) throws IOException {
  }

  @Override
  public void visitDirectoryLeave(final File dir) throws IOException {

  }

  @Override
  protected MutableFileWalkerStatus createStatus(final File dir) throws IOException {
    return new MutableFileListStatus(dir);
  }
}
