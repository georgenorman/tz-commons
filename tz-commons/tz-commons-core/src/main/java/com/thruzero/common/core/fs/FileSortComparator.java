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

import com.thruzero.common.core.support.AbstractSortComparator;

/**
 * Used to sort a {@code Collection} of files with a given sort-direction.
 * 
 * @author George Norman
 */
public class FileSortComparator extends AbstractSortComparator<File> {

  /** Single-column table sort comparator. */
  public FileSortComparator(final SortDirection sortDirection) {
    super(sortDirection);
  }

  @Override
  public int compare(final File f1, final File f2) {
    return applySortDirection(f1.compareTo(f2));
  }

}
