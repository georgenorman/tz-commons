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
 * An abstract base class that provides a skeletal implementation of the {@code HierarchicalFileVisitor} interface. Most
 * subclasses will only need to implement one or two of the following methods:
 * <ul>
 * <li>
 * {@link com.thruzero.common.core.fs.walker.visitor.HierarchicalFileVisitor#visitDirectoryEnter(File)}
 * <li>
 * {@link com.thruzero.common.core.fs.walker.visitor.HierarchicalFileVisitor#visitDirectoryLeave(File)}
 * <li>{@link com.thruzero.common.core.fs.walker.visitor.HierarchicalFileVisitor#visitFile(File)}
 * </ul>
 *
 * @author George Norman
 */
public abstract class AbstractHierarchicalFileVisitor implements HierarchicalFileVisitor {
  private MutableFileWalkerStatus fileWalkerStatus;

  // ----------------------------------------------------------
  // MutableFileWalkerStatus
  // ----------------------------------------------------------

  /**
   * A default implementation of the {@code FileWalkerStatus} interface, suitable for implementors of
   * {@code HierarchicalFileVisitor}.
   */
  public static class MutableFileWalkerStatus implements FileWalkerStatus {
    private final File startDirectory;
    private int numProcessed;
    private final List<String> messages = new ArrayList<String>();

    protected MutableFileWalkerStatus(final File startDirectory) {
      this.startDirectory = startDirectory;
    }

    @Override
    public File getStartDirectory() {
      return startDirectory;
    }

    @Override
    public int getNumProcessed() {
      return numProcessed;
    }

    public void incNumProcessed() {
      this.numProcessed += 1;
    }

    @Override
    public List<String> getMessages() {
      return messages;
    }

    public void addMessage(final String message) {
      this.messages.add(message);
    }
  }

  // ========================================================================
  // AbstractHierarchicalFileVisitor
  // ========================================================================

  /**
   * Called before any files or sub-directories have been processed (the first task before processing begins). Calls
   * {@link com.thruzero.common.core.fs.walker.visitor.AbstractHierarchicalFileVisitor#createStatus(File)}, to create
   * and save the status object. When overriding, subclasses should call {@code super.open(File)}.
   */
  @Override
  public void open(final File startDir) throws IOException {
    fileWalkerStatus = createStatus(startDir);
  }

  /**
   * Called after all files and sub-directories have been processed (the final task before exit). By default, the status
   * is detached and returned. When overriding, to perform optional cleanup, subclasses should call
   * {@code super.close(File)}.
   */
  @Override
  public MutableFileWalkerStatus close(final File startDir) throws IOException {
    MutableFileWalkerStatus result = fileWalkerStatus;

    fileWalkerStatus = null;

    return result;
  }

  /** Returns the active status object, used to update progress, statistics, or any other data useful to clients. */
  protected MutableFileWalkerStatus getStatus() {
    return fileWalkerStatus;
  }

  /**
   * Creates the default status object. Subclasses override to create a specialized status object. Subclasses should not
   * call {@code super.createStatus(File)}.
   */
  protected MutableFileWalkerStatus createStatus(final File startDir) throws IOException {
    return new MutableFileWalkerStatus(startDir);
  }
}
