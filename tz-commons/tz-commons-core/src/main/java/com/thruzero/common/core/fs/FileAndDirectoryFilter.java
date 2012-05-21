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

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import org.apache.commons.io.filefilter.DelegateFileFilter;

/**
 * A filter consisting of two nested filters, where one is used to filter directories and the other is used to filter
 * files. This filter can be used to wrap existing filters that would prevent traversal of sub-directories. For example,
 * the apache.commons {@code WildcardFilter} rejects all directories, preventing sub-directories from being traversed.
 * However, by passing a {@code DirectoryFileFilter.INSTANCE}, followed by the {@code WildcardFilter}, all
 * sub-directories will be traversed.
 */
public class FileAndDirectoryFilter implements FileFilter {
  private final DelegateFileFilter directoryFilter;
  private final DelegateFileFilter fileFilter;

  /**
   * @param directoryFilter used to filter directories - if null, then no directories will be processed. Use the
   * apache.commons {@code DirectoryFileFilter} to process all directories.
   * @param fileFilter used to filter files - if null, then no files will be processed. Use the apache.commons
   * {@code TrueFileFilter} to process all files.
   */
  public FileAndDirectoryFilter(final FileFilter directoryFilter, final FileFilter fileFilter) {
    this.directoryFilter = directoryFilter == null ? null : new DelegateFileFilter(directoryFilter);
    this.fileFilter = fileFilter == null ? null : new DelegateFileFilter(fileFilter);
  }

  /**
   * @param directorynameFilter used to filter directories - if null, then no directories will be processed. Use the
   * apache.commons {@code DirectoryFileFilter} to process all directories.
   * @param filenameFilter used to filter files - if null, then no files will be processed. Use the apache.commons
   * {@code TrueFileFilter} to process all files.
   */
  public FileAndDirectoryFilter(final FilenameFilter directorynameFilter, final FilenameFilter filenameFilter) {
    this.directoryFilter = directorynameFilter == null ? null : new DelegateFileFilter(directorynameFilter);
    this.fileFilter = filenameFilter == null ? null : new DelegateFileFilter(filenameFilter);
  }

  /**
   * if the given {@code file} is a directory, then uses the {@code directoryFilter}; otherwise, uses the
   * {@code fileFilter}. A null {@code fileFilter} will accept all files and a null {@code directoryFilter} will accept
   * all directories.
   */
  @Override
  public boolean accept(final File file) {
    if (file.isDirectory()) {
      return directoryFilter != null && directoryFilter.accept(file);
    } else {
      return fileFilter != null && fileFilter.accept(file);
    }
  }
}
