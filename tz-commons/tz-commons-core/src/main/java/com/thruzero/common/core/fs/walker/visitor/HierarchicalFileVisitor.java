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

import com.thruzero.common.core.fs.HierarchicalFileWalker.FileWalkerStatus;

/**
 * An interface roughly based on the <a href="http://en.wikipedia.org/wiki/Hierarchical_visitor_pattern">Hierarchical
 * Visitor</a> pattern and represents an action that can be performed repeatedly on a set of files and/or
 * sub-directories (e.g., add each visited file and sub-directory to a {@code ZipOutputStream} and save the results to a
 * zip file).
 * <p>
 * An instance, implementing this interface, is passed into the
 * {@link com.thruzero.common.core.fs.HierarchicalFileWalker#accept(HierarchicalFileVisitor)
 * HierarchicalFileWalker.accept(HierarchicalFileVisitor)} method, for each file and sub-directory, selected by a given
 * filter and starting at a given root directory.
 *
 * @author George Norman
 */
public interface HierarchicalFileVisitor {

  /**
   * Called before the {@code rootDirectory} is traversed. Override to provide any required initialization before
   * traversal begins (e.g., create {@code ZipOutputStream}).
   *
   * @param rootDirectory the starting directory.
   */
  void open(File rootDirectory) throws IOException;

  /**
   * Called when the given directory is entered. Override to handle any directory-related tasks before any files and
   * sub-directories of the given {@code directory} are processed.
   */
  void visitDirectoryEnter(File directory) throws IOException;

  /**
   * Called when the given directory is exited. Override to handle any directory-related tasks after all files and
   * sub-directories of the given {@code directory} have been processed.
   */
  void visitDirectoryLeave(File directory) throws IOException;

  /** The given {@code file} was encountered during traversal - override to handle file-related tasks. */
  void visitFile(File file) throws IOException;

  /**
   * Called after the {@code rootDirectory} has been traversed. Override to provide any required cleanup after traversal
   * has completed (e.g., flush and close a {@code ZipOutputStream}).
   *
   * @param rootDirectory the starting directory.
   */
  FileWalkerStatus close(File rootDirectory) throws IOException;
}
