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
package com.thruzero.test.support;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;

import com.thruzero.common.core.bookmarks.LocatorBookmark;
import com.thruzero.common.core.fs.HierarchicalFileWalker;
import com.thruzero.common.core.fs.HierarchicalFileWalker.FileWalkerStatus;
import com.thruzero.common.core.fs.walker.visitor.DirectoryDeletingVisitor;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.support.EnvironmentHelper;
import com.thruzero.common.core.support.EnvironmentHelper.EnvironmentVariableKeys;
import com.thruzero.common.core.utils.FileUtilsExt;
import com.thruzero.common.core.utils.FileUtilsExt.FileUtilsException;

/**
 *
 * @author George Norman
 */
public abstract class AbstractCoreTestCase {
  /** The default test-classes directory, when using maven to build and run tests */
  public static final String TEST_CLASSES_DIR_NAME = "test-classes";

  /** The name of the temporary test directory: "temp" */
  public static final String DEFAULT_TEMP_DIR_NAME = "temp";

  public enum AssertOption {NONE, ASSERT_NOT_NULL};

  /**
   * Subclasses override this and call super and annotate with {@code Before}.
   * <p>
   * <b>Example</b>:
   *
   * <pre>
   * <code>
   * @Override
   * @Before
   * public void setUp() throws Exception {
   *   super.setUp();
   * }
   * </code>
   * </pre>
   */
  @LocatorBookmark(comment = "ConfigLocator example setup")
  @Before
  public void setUp() throws Exception {
    ConfigLocator.setup("config/config.test.xml", null);
  }

  @LocatorBookmark(comment = "ConfigLocator example tear down")
  @After
  public void tearDown() throws Exception {
    ConfigLocator.reset();
  }

  protected void deleteTempDirContents() {

    if (getTestFile(DEFAULT_TEMP_DIR_NAME).exists()) {
      HierarchicalFileWalker walker = new HierarchicalFileWalker(getTestFile(DEFAULT_TEMP_DIR_NAME));
      try {
        FileWalkerStatus status = walker.accept(new DirectoryDeletingVisitor());
        if (!status.getMessages().isEmpty()) {
          throw new RuntimeException("ERROR: While attempting to clean the temp directory: " + status.getMessages().get(0));
        }
      } catch (IOException e) {
        throw new RuntimeException("ERROR: Caught Exception while attempting to clean the temp directory: " + e);
      }

      getTestFile(DEFAULT_TEMP_DIR_NAME).mkdir();
    }
  }

  /**
   * Copy the given directory to a temporary directory and return it.
   */
  protected File copyDirToTemp(final String sourceDirName) {
    File result;

    result = getTempTestFile(sourceDirName);
    File sourceDir = null;
    try {
      sourceDir = getTestFile(sourceDirName);
      assertTrue("Source directory named '" + sourceDirName + "' does not exist", sourceDir.exists());
      FileUtils.copyDirectory(sourceDir, result);
    } catch (IOException e) {
      System.out.println("sourceDir:" + sourceDir.getAbsolutePath());
      System.out.println("result:" + result.getAbsolutePath());
      fail("could not copy source directory to temp directory: " + e);
    }

    return result;
  }

  /**
   * Copy the given file to a temporary directory, so that a test can be run multiple times without requiring a maven
   * clean.
   */
  protected File copyFileToTemp(final String sourceFileName) {
    File result = null;

    try {
      File templateFile = getTestFile(sourceFileName);
      assertTrue("Source file named '" + sourceFileName + "' does not exist", templateFile.exists());

      File tempDir = new File(templateFile.getParentFile(), DEFAULT_TEMP_DIR_NAME);
      FileUtils.copyFileToDirectory(templateFile, tempDir);
      result = new File(tempDir, sourceFileName);
    } catch (IOException e) {
      fail("could not copy substitution test file to temp directory: " + e);
    }

    return result;
  }

  /**
   * Returns the requested file from the temporary test directory.
   */
  protected File getTempTestFile(final String fileName) {
    File result = null;
    File tempDir = getTestFile(DEFAULT_TEMP_DIR_NAME);

    if (tempDir != null) {
      result = new File(tempDir, fileName);
    }
    assertTrue("Could not find file: " + fileName, result != null);

    return result;
  }

  /**
   * Returns the contents of the given file. Optionally asserts that the result is not empty.
   */
  protected String getFileContents(final File file, final AssertOption assertOption) {
    String result = null;

    try {
      result = FileUtilsExt.readFromFile(file);
    } catch (FileUtilsException e1) {
      // ignore
    }

    if (assertOption == AssertOption.ASSERT_NOT_NULL) {
      assertTrue("Could not read substitution file before walk.", StringUtils.isNotEmpty(result));
    }

    return result;
  }

  /**
   * Returns the specified file by searching the class path for the "<code>test-classes</code>" directory (
   * <code>TEST_CLASSES_DIR_NAME</code>) and appending the "temp" directory plus the requested fileName to create the
   * File instance that represents the requested test file. Returns null if the "<code>test-classes</code>" directory is
   * not found.
   */
  protected File getTestFile(final String fileName) {
    // find the "test-classes" directory
    String classPath = System.getProperty(EnvironmentVariableKeys.JAVA_CLASS_PATH_ENV_VAR, ".");
    String[] classPathElements = classPath.split(EnvironmentHelper.CLASS_PATH_SEPARATOR);

    String testClassesDir = null;
    for (String path : classPathElements) {
      if (StringUtils.endsWith(path, TEST_CLASSES_DIR_NAME) || StringUtils.endsWith(path, TEST_CLASSES_DIR_NAME + EnvironmentHelper.FILE_PATH_SEPARATOR)) {
        testClassesDir = path;
        break;
      }
    }

    if (testClassesDir == null) {
      return null;
    } else {
      return new File(testClassesDir, fileName);
    }
  }

}
