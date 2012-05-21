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
import com.thruzero.common.core.fs.walker.visitor.ZipCompressingVisitor;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for ZipCompressingVisitor.
 *
 * @author George Norman
 */
public class ZipCompressingVisitorTest extends AbstractCoreTestCase {
  public static final String ZIP_TEST_DIR_NAME = "nested";

  @Test
  public void test() {
    // clean the temp directory
    deleteTempDirContents();

    // copy test directory to a temp directory, as a zip target
    File zipTestDir = copyDirToTemp(ZIP_TEST_DIR_NAME);

    // delete the directory
    try {
      // zip the test directory in temp
      File targetArchive = new File(zipTestDir.getParent(), "testArchive.zip");
      HierarchicalFileWalker walker = new HierarchicalFileWalker(zipTestDir);
      FileWalkerStatus status = walker.accept(new ZipCompressingVisitor(targetArchive));
      assertEquals("Wrong number of files were compressed.", 16, status.getNumProcessed());
    } catch (IOException e) {
      fail("FileRenamingFileWalker generated exception: " + e);
    }
  }

}
