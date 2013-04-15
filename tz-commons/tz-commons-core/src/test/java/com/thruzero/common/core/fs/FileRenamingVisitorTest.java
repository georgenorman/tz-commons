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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFilter;
import org.junit.Test;

import com.thruzero.common.core.fs.HierarchicalFileWalker.FileWalkerStatus;
import com.thruzero.common.core.fs.walker.visitor.FileListVisitor;
import com.thruzero.common.core.fs.walker.visitor.FileRenamingVisitor;
import com.thruzero.common.core.strategy.KeyValuePairSubstitutionStrategy;
import com.thruzero.common.core.strategy.SubstitutionStrategy;
import com.thruzero.common.core.support.KeyValuePair;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for FileRenamingVisitor.
 * 
 * @author George Norman
 */
public class FileRenamingVisitorTest extends AbstractCoreTestCase {
  public static final String RENAMING_TEST_DIR_NAME = "renaming";
  public static final String SUBSTITUTION_ONE_VALUE = "Bass";

  @Test
  public void testRenameOfSimpleDirectory() {
    // clean the temp directory
    deleteTempDirContents();

    // copy test directory to a temp directory, as a rename target
    File renamingTestDir = copyDirToTemp(RENAMING_TEST_DIR_NAME);

    // walk the temp directory
    try {
      // rename the files and verify
      SubstitutionStrategy strategy = new KeyValuePairSubstitutionStrategy(new KeyValuePair("${sub1}", SUBSTITUTION_ONE_VALUE));
      FileWalkerStatus renamingStatus = new HierarchicalFileWalker(renamingTestDir, null).accept(new FileRenamingVisitor(strategy));
      assertEquals("Wrong number of files/directories were renamed.", 4, renamingStatus.getNumProcessed());

      // verify files were renamed, using FileListFileWalker
      FileAndDirectoryFilter filter = new FileAndDirectoryFilter((FileFilter) DirectoryFileFilter.INSTANCE, new WildcardFilter("*" + SUBSTITUTION_ONE_VALUE + "*"));
      FileWalkerStatus listingStatus = new HierarchicalFileWalker(renamingTestDir, filter).accept(new FileListVisitor());
      assertEquals("Wrong number of files were renamed.", 3, listingStatus.getNumProcessed()); // 3 files should be renamed (plus one directory that's not
                                                                                               // sslisted here)
    } catch (IOException e) {
      fail("FileRenamingFileWalker generated exception: " + e);
    }
  }
}
