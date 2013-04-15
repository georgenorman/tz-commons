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

import org.apache.commons.io.FilenameUtils;

/**
 * Matches files, directories or files and directories, using a case-sensitive wildcard, which uses the characters '?' and '*' to represent a single or multiple
 * wildcard characters (note, the apache WildcardFilter matches only files). For more details, see
 * {@link org.apache.commons.io.FilenameUtils#wildcardMatch(java.lang.String, java.lang.String)}
 * 
 * <p>
 * Examples:
 * 
 * <pre>
 *     "*.txt" -> "foo.txt" -> match
 *     "*.???" -> "foo.png" -> match
 * </pre>
 */
public class WildcardFilter implements FileFilter {
  private final String wildcard;
  private final FilterTypes filterTypes;
  private final boolean invert;

  public enum FilterTypes {
    /** matches only files */
    FILES,

    /** matches only directories */
    DIRECTORIES,

    /** matches files and directories */
    FILES_AND_DIRECTORIES
  }

  public WildcardFilter(final String wildcard) {
    this(wildcard, FilterTypes.FILES_AND_DIRECTORIES, false);
  }

  public WildcardFilter(final String wildcard, final FilterTypes filterTypes, final boolean invert) {
    this.wildcard = wildcard;
    this.filterTypes = filterTypes;
    this.invert = invert;
  }

  @Override
  public boolean accept(final File file) {
    switch (filterTypes) {
    case FILES:
      return test(file, file.isFile());
    case DIRECTORIES:
      return test(file, file.isDirectory());
    default:
      return test(file, file.isDirectory() || file.isFile());
    }
  }

  /** if useWildcard is true, then return the result of the wildcard search; otherwise, return false. */
  protected boolean test(final File file, final boolean useWildcard) {
    boolean result = false;

    if (useWildcard) {
      result = FilenameUtils.wildcardMatch(file.getName(), wildcard);
      result = invert ? !result : result;
    }

    return result;
  }
}
