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
package com.thruzero.common.core.fs;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import com.thruzero.common.core.fs.walker.visitor.HierarchicalFileVisitor;
import com.thruzero.common.core.support.AbstractSortComparator.SortDirection;

/**
 * Given a starting directory and filter, walks the filtered tree, visiting each file and sub-directory along the way.
 * The implementation is roughly based on the <a
 * href="http://en.wikipedia.org/wiki/Hierarchical_visitor_pattern">Hierarchical Visitor</a> pattern.
 *
 * <p>
 * Clients create an instance of {@code HierarchicalFileWalker} and call its {@code accept} method, passing in an
 * instance of {@link com.thruzero.common.core.fs.walker.visitor.HierarchicalFileVisitor}. A variety of visitor
 * instances can be passed in, depending on the desired result. For example, a FileRenamingVisitor can be used to rename
 * a series of files based on a filename pattern. Likewise, a ZipCompressorVisitor can be used to zip-compress a
 * particular directory.
 *
 * <p>
 * The code below demonstrates how to use a {@link com.thruzero.common.core.fs.walker.visitor.SubstitutionVisitor} to
 * perform a search and replace on the contents of a series of files. This example will replace all of the "${foo}"
 * strings with "Foo" and all of the "${bar}" strings with "Bar" for all files visited by the
 * {@code HierarchicalFileWalker}.
 *
 * <pre>
 * <code>
 *   // create the strategy
 *   SubstitutionStrategy strategy = new KeyValuePairSubstitutionStrategy(new KeyValuePair("${foo"}, "Foo"), new KeyValuePair("${bar"}, "Bar"));
 *
 *   // next, perform the search and replace
 *   FileWalkerStatus status = new HierarchicalFileWalker(someDirectory).accept(new SubstitutionVisitor(strategy));
 * </code>
 * </pre>
 *
 * @author George Norman
 */
public class HierarchicalFileWalker {
  private File rootDirectory;
  private FileAndDirectoryFilter filter;
  private FileSortComparator fileSortComparator;

  // ----------------------------------------------------------
  // FileWalkerStatus
  // ----------------------------------------------------------

  /**
   * The interface, used by clients of {@code HierarchicalFileWalker}, to view status information after a full traversal
   * has completed (e.g., how many files and directories were successfully processed).
   */
  public static interface FileWalkerStatus {

    /** @return the start directory (where the traversal begins). */
    File getStartDirectory();

    /** @return the number of files and directories processed. */
    int getNumProcessed();

    /** @return optional status/error messages that may have occurred. */
    public List<String> getMessages();
  }

  // ========================================================================
  // HierarchicalFileWalker
  // ========================================================================

  /**
   * Constructs a deep-traversal walker for the given {@code rootDirectory}, with no sorting. This will process every
   * file and sub-directory in {@code rootDirectory}.
   */
  public HierarchicalFileWalker(final File rootDirectory) {
    this(rootDirectory, TrueFileFilter.INSTANCE);
  }

  /**
   * Constructs a walker for the given {@code rootDirectory} using the given {@code filter} with no sorting.
   *
   * @param rootDirectory starting point.
   * @param filter the @{link java.io.FileFilter} used to determine which files to process.
   */
  public HierarchicalFileWalker(final File rootDirectory, final FileFilter filter) {
    this(rootDirectory, filter, null);
  }

  /**
   * Constructs a walker for the given {@code rootDirectory} using the given {@code filter} and {@code sortDirection}.
   * If the {@code filter} is an instance of {@code FileAndDirectoryFilter}, then the {@code fileFilter} component will
   * be used to filter files and the {@code directoryFilter} component will be used to filter directories. Otherwise,
   * directories are implicitly accepted and files are filtered using the given {@code filter}. If the given
   * {@code filter} is null, then all files and directories will be processed.
   *
   * @param rootDirectory starting point.
   * @param filter use {@link org.apache.commons.io.filefilter.TrueFileFilter#INSTANCE} to process all files.
   * @param sortDirection if present, files will be sorted in the direction indicated; otherwise, no sorting will take
   * place.
   */
  public HierarchicalFileWalker(final File rootDirectory, final FileFilter filter, final SortDirection sortDirection) {
    this.rootDirectory = rootDirectory;
    if (filter == null) {
      // default filter returns everything
      this.filter = new FileAndDirectoryFilter((FileFilter)DirectoryFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
    } else if (filter instanceof FileAndDirectoryFilter) {
      this.filter = (FileAndDirectoryFilter)filter;
    } else {
      this.filter = new FileAndDirectoryFilter(DirectoryFileFilter.INSTANCE, filter);
    }
    this.fileSortComparator = new FileSortComparator(sortDirection);
  }

  /**
   * For the {@code rootDirectory} given at construction time, traverse all sub-directories and files allowed by the
   * filter(s) given at construction time. On the given {@code visitor}, for each sub-directory, the
   * {@code visitDirectoryEnter} and {@code visitDirectoryLeave} methods will be called and for each file, the
   * {@code visitFile} method will be called.
   *
   * @see {@link com.thruzero.common.core.fs.HierarchicalFileWalker(File, FileFilter, SortDirection)} for details on filters.
   * @throws IllegalArgumentException if rootDirectory (given at construction time) is not a valid directory.
   */
  public FileWalkerStatus accept(final HierarchicalFileVisitor visitor) throws IOException {
    assertIsDirectory(rootDirectory);

    visitor.open(rootDirectory);

    doAccept(visitor, rootDirectory);

    return visitor.close(rootDirectory);
  }

  /**
   * For the given {@code directory}, recursively traverse all sub-directories and files allowed by the filter(s) given
   * at construction time. On the given visitor, for each sub-directory, the {@code visitDirectoryEnter} and
   * {@code visitDirectoryLeave} methods are called and for each file, the {@code visitFile} method is called.
   *
   * @see {@link com.thruzero.common.core.fs.HierarchicalFileWalker(File, FileFilter, SortDirection)} for details on
   * filters.
   */
  protected void doAccept(final HierarchicalFileVisitor visitor, final File directory) throws IOException {
    // if directory doesn't exist, then nothing to do
    if (!directory.isDirectory() || !directory.exists()) {
      return;
    }

    // begin directory processing.
    visitor.visitDirectoryEnter(directory);

    // get all files and sub-directories
    File[] files = directory.listFiles();

    // next, sort files in the directory, if requested
    if (getFileSortComparator().getSortDirection() != null) {
      Arrays.sort(files, getFileSortComparator());
    }

    // process each file and directory in the given directory
    for (File file : files) {
      if (file.isDirectory()) {
        if (getFilter().accept(file)) { // uses directoryFilter, since this is a directory
          doAccept(visitor, file);
        }
      } else {
        if (getFilter().accept(file)) { // uses fileFilter, since this is a file
          visitor.visitFile(file);
        }
      }
    }

    // complete directory processing
    visitor.visitDirectoryLeave(directory);
  }

  /** Return the root directory given at construction time. */
  public File getRootDirectory() {
    return rootDirectory;
  }

  public void setRootDirectory(final File rootDirectory) {
    this.rootDirectory = rootDirectory;
  }

  /** Return the filter given at construction time. */
  public FileFilter getFilter() {
    return filter;
  }

  public void setFilter(final FileAndDirectoryFilter filter) {
    this.filter = filter;
  }

  public FileSortComparator getFileSortComparator() {
    return fileSortComparator;
  }

  public void setFileSortComparator(final FileSortComparator fileSortComparator) {
    this.fileSortComparator = fileSortComparator;
  }

  /**
   * Throws an IllegalArgumentException if the given {@code srcDir} is not a directory.
   *
   * @throws IllegalArgumentException
   */
  protected final void assertIsDirectory(final File srcDir) {
    if (!srcDir.isDirectory()) {
      throw new IllegalArgumentException("ERROR: Source directory is not a Directory (" + srcDir + ").");
    }
  }

}
