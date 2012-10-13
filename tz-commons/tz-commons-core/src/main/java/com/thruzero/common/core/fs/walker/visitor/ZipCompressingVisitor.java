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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.support.EnvironmentHelper;
import com.thruzero.common.core.support.LogHelper;

/**
 * Compresses all visited files and sub-directories to a single target zip file.
 * <p>
 * <b>Example</b>:
 *
 * <pre>
 *   File zipArchive = new File("/home/archives","zipArchive.zip");
 *   HierarchicalFileWalker walker = new HierarchicalFileWalker("/home/foo");
 *   FileWalkerStatus status = walker.accept(new <b>ZipCompressingVisitor</b>(zipArchive));
 * </pre>
 *
 * @author George Norman
 */
public class ZipCompressingVisitor extends AbstractHierarchicalFileVisitor {
  private final File targetArchive;

  private File rootDirectory;
  private ZipOutputStream zipOut;

  private final ZipCompressingVisitorLogHelper logHelper = new ZipCompressingVisitorLogHelper(ZipCompressingVisitor.class);

  // -----------------------------------------------------------
  // ZipCompressingVisitorLogHelper
  // -----------------------------------------------------------

  public static final class ZipCompressingVisitorLogHelper extends LogHelper {
    public ZipCompressingVisitorLogHelper(final Class<?> clazz) {
      super(clazz);
    }

    public void logAddDirectory(final String relativePath) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("  - adding directory = " + relativePath);
      }
    }

    public void logZippingFile(final File file) {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("  - zipping file = " + file);
      }
    }

    public void logError(final Exception error) {
      getLogger().error(error);
    }
  }

  // ====================================================================================
  // ZipCompressingVisitor
  // ====================================================================================

  /**
   * Create a visitor that will compress files and directories to the given {@code targetArchive} file.
   */
  public ZipCompressingVisitor(final File targetArchive) {
    if (targetArchive.isDirectory()) {
      throw new IllegalArgumentException("ERROR: target must be a file (for zip), but is a directory: " + targetArchive);
    }

    this.targetArchive = targetArchive;
  }

  /**
   * Add the given directory path to the target zip archive (no files are copied, just the relative directory path is
   * added).
   */
  @Override
  public void visitDirectoryEnter(final File directory) throws IOException {
    String relativePath = getRelativePath(directory);

    if (StringUtils.isNotEmpty(relativePath)) {
      if (!relativePath.endsWith(EnvironmentHelper.FILE_PATH_SEPARATOR)) {
        relativePath += EnvironmentHelper.FILE_PATH_SEPARATOR;
      }

      logHelper.logAddDirectory(relativePath);

      ZipEntry zipEntry = new ZipEntry(relativePath);
      zipOut.putNextEntry(zipEntry);
      zipOut.flush();
      zipOut.closeEntry();

      getStatus().incNumProcessed();
    }
  }

  @Override
  public void visitDirectoryLeave(final File directory) throws IOException {

  }

  /**
   * Compress the given file and add it to the archive.
   */
  @Override
  public void visitFile(final File file) throws IOException {
    logHelper.logZippingFile(file);

    // zip the file
    String relativePath = getRelativePath(file);
    ZipEntry zipEntry = new ZipEntry(relativePath);
    zipOut.putNextEntry(zipEntry);
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
    IOUtils.copy(bis, zipOut);
    zipOut.flush();
    zipOut.closeEntry();
    bis.close();

    getStatus().incNumProcessed();
  }

  /**
   * create the zip archive file and prepare it to accept files and directory paths.
   *
   * @throws IllegalArgumentException if target file already exists, but can't be deleted, or when the target archive
   * file can't be created.
   */
  @Override
  public void open(final File rootDirectory) throws IOException {
    super.open(rootDirectory);

    // cache rootDirectory, for determining the relative path during traversals
    this.rootDirectory = rootDirectory;

    try {
      if (targetArchive.exists()) {
        boolean deleteSuccess = targetArchive.delete();
        if (!deleteSuccess) {
          IllegalArgumentException iae = new IllegalArgumentException("ERROR: Could not delete target file: " + targetArchive);
          logHelper.logError(iae);
          throw iae;
        }
      }
      targetArchive.createNewFile();
      zipOut = new ZipOutputStream(new FileOutputStream(targetArchive));
    } catch (IOException e) {
      IllegalArgumentException iae = new IllegalArgumentException("ERROR: Could not create target file: " + targetArchive + ": " + e);
      logHelper.logError(iae);
      throw iae;
    }
  }

  /**
   * flush and close the zip archive file, persisting it to the file system.
   */
  @Override
  public MutableFileWalkerStatus close(final File rootDir) throws IOException {
    zipOut.flush();
    zipOut.close();

    return super.close(rootDir);
  }

  protected String getRelativePath(final File file) {
    // make relative path
    String relativePath = StringUtils.remove(file.getAbsolutePath(), rootDirectory.getAbsolutePath());

    if (StringUtils.isEmpty(relativePath)) {
      return StringUtils.EMPTY;
    }

    return StringUtils.stripStart(relativePath, String.valueOf(File.separatorChar));
  }

}
