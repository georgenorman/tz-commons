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
import java.io.IOException;

import org.junit.Test;

import com.thruzero.common.core.fs.HierarchicalFileWalker.FileWalkerStatus;
import com.thruzero.common.core.fs.walker.visitor.DirectoryDeletingVisitor;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for DirectoryDeletingVisitor.
 *
 * @author George Norman
 */
public class DirectoryDeletingVisitorTest extends AbstractCoreTestCase {
  public static final String DELETING_TEST_DIR_NAME = "deleting";

  /** Delete a directory and verify expected number of files were deleted. */
  @Test
  public void testDeleteOfSimpleDirectory() {
    // clean the temp directory
    deleteTempDirContents();

    // copy test directory to a temp directory, as a delete target
    File deletingTestDir = copyDirToTemp(DELETING_TEST_DIR_NAME);

    // delete the directory
    try {
      // delete the test directory in temp
      HierarchicalFileWalker walker = new HierarchicalFileWalker(deletingTestDir);
      FileWalkerStatus status = walker.accept(new DirectoryDeletingVisitor());
      assertEquals("Wrong number of files were deleted.", 6, status.getNumProcessed());
    } catch (IOException e) {
      fail("DirectoryDeletingVisitor generated exception: " + e);
    }
  }

  /** Attempt to delete a bogus directory and verify expected IllegalArgumentException was thrown. */
  @Test
  public void testDeleteOfNonExistantDirectory() {
    // clean the temp directory
    deleteTempDirContents();

    // attempt to delete a bogus directory
    try {
      // delete the test directory in temp
      HierarchicalFileWalker walker = new HierarchicalFileWalker(getTempTestFile("bogus"));
      walker.accept(new DirectoryDeletingVisitor());
      fail("DirectoryDeletingVisitor didn't generate an IllegalArgumentException (for a bogus directory).");
    } catch (IllegalArgumentException e) {
      //
    } catch (IOException e) {
      fail("DirectoryDeletingVisitor generated IOException: " + e + ". Expected IllegalArgumentException (for a bogus directory).");
    }
  }

}
