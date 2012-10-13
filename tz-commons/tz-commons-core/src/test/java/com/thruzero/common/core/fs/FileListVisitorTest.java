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

import com.thruzero.common.core.fs.walker.visitor.FileListVisitor;
import com.thruzero.common.core.fs.walker.visitor.FileListVisitor.DirectoryOption;
import com.thruzero.common.core.fs.walker.visitor.FileListVisitor.FileListStatus;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for FileListVisitor.
 *
 * @author George Norman
 */
public class FileListVisitorTest extends AbstractCoreTestCase {
  public static final String NESTED_TEST_DIR_NAME = "nested";

  @Test
  public void testListOfSimpleDirectory() {
    // get test file in temp directory
    File nestedTestDir = getTestFile(NESTED_TEST_DIR_NAME);

    // walk the temp directory
    try {
      // find files named "*test*", using FileListVisitor
      FileAndDirectoryFilter filter = new FileAndDirectoryFilter((FileFilter)DirectoryFileFilter.INSTANCE, new WildcardFilter("*test*"));
      FileListStatus listingStatus = (FileListStatus)new HierarchicalFileWalker(nestedTestDir, filter).accept(new FileListVisitor(DirectoryOption.IGNORE_DIRECTORIES));
      assertEquals("Wrong number of files were renamed.", 5, listingStatus.getNumProcessed());
      assertEquals("Wrong number of files were renamed.", 5, listingStatus.getResults().size());
    } catch (IOException e) {
      fail("FileListFileWalker generated exception: " + e);
    }
  }

}
