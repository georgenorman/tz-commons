/*
 *   Copyright 2005-2011 George Norman
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
package com.thruzero.common.core.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.text.StrBuilder;

import com.thruzero.common.core.support.EnvironmentHelper;
import com.thruzero.common.core.support.EnvironmentHelper.EnvironmentVariableKeys;
import com.thruzero.common.core.support.LogHelper;

/**
 * Extensions to the apache.commons {@code FileUtils} utility class.
 *
 * @author George Norman
 */
public class FileUtilsExt extends FileUtils {
  private static final FileUtilsExtLogHelper logHelper = new FileUtilsExtLogHelper(FileUtilsExt.class);

  public static final String STANDARD_ENCODING = "ISO-8859-1";

  // ---------------------------------------------------------
  // FileUtilsException
  // ---------------------------------------------------------

  /** An exception thrown by FileUtilsExt */
  public static class FileUtilsException extends Exception {
    private static final long serialVersionUID = 100;

    public FileUtilsException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }

  // -----------------------------------------------------------
  // FileUtilsExtLogHelper
  // -----------------------------------------------------------

  public static final class FileUtilsExtLogHelper extends LogHelper {
    public FileUtilsExtLogHelper(final Class<?> clazz) {
      super(clazz);
    }

    public void logBeginUnzip(final File fromFile, final File toDir) {
      if (getLogger().isInfoEnabled()) {
        getLogger().info("## begin unzipArchive: fromFile=" + fromFile.getAbsolutePath() + ", toDir=" + toDir.getAbsolutePath());
        getLogger().info(" # fromFile exists:" + fromFile.exists());
        getLogger().info(" # toDir exists:" + toDir.exists());
      }
    }

    public void logProgressCreatedToDir(final File toDir) {
      if (getLogger().isInfoEnabled()) {
        getLogger().info(" # created toDir, toDir exists: " + toDir.exists());
      }
    }

    public void logProgressToDirIsWritable(final File toDir) {
      if (getLogger().isInfoEnabled()) {
        getLogger().info(" # toDir canWrite: " + toDir.canWrite());
      }
    }

    public void logProgressFilePathCreated(final File pathDir) {
      if (getLogger().isInfoEnabled()) {
        getLogger().info("  - mkdir for path =" + pathDir.getAbsolutePath());
      }
    }

    public void logProgressCopyEntry(final File extractedFile) {
      if (getLogger().isInfoEnabled()) {
        getLogger().info("  - copy entry = " + extractedFile.getAbsolutePath());
      }
    }

    public void logIOException(final String message, final Exception e) {
      getLogger().error(message, e);
    }

    public String logFileWriteError(final File file, final Exception e) {
      StrBuilder msg = new StrBuilder(100);

      msg.appendNewLine();
      msg.append("ERROR: Can't write file or directory.");
      msg.append("\n  - canRead: ").appendln(file.canRead());
      msg.append("\n  - canWrite: ").appendln(file.canWrite());
      msg.append("\n  - exists").appendln(file.exists());
      msg.append("\n  - isFile: ").appendln(file.isFile());
      msg.append("\n  - length").appendln(file.length());
      msg.append("\n  - getPath").appendln(file.getPath());
      msg.append("\n  - isAbsolute").appendln(file.isAbsolute());
      msg.append("\n  - getAbsolutePath: ").appendln(file.getAbsolutePath());

      if (e == null) {
        getLogger().error(msg);
      } else {
        getLogger().error(msg, e);
      }

      return msg.toString();
    }

    public String logFileReadError(final Exception cause) {
      String msg = "Error reading from file.";
      getLogger().error(msg, cause);

      return msg;
    }
  }

  // =====================================================================================
  // FileUtilsExt
  // =====================================================================================

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected FileUtilsExt() {
  }

  /**
   * Return the first existing File found on the classpath, with the given fileName.
   */
  public static File getFileOnClassPath(final String fileName) {
    File result = null;
    String classPath = System.getProperty(EnvironmentVariableKeys.JAVA_CLASS_PATH_ENV_VAR, ".");
    String[] classPathElements = classPath.split(EnvironmentHelper.CLASS_PATH_SEPARATOR);

    for (String parentDirectory : classPathElements) {
      File temp = new File(parentDirectory, fileName);
      System.out.println("- "+temp.getAbsolutePath());
      if (temp.exists()) {
        result = temp;
        break;
      }
    }

    return result;
  }

  public static File urlToFile(final String targetUri) throws MalformedURLException {
    URL url = new URL(targetUri);

    return urlToFile(url);
  }

  public static File urlToFile(final URL url) {
    return new File(url.getPath());
  }

  public static void writeToFile(final String targetUri, final String contentText) throws FileUtilsException, MalformedURLException {
    writeToFile(urlToFile(targetUri), contentText);
  }

  public static void writeToFile(final File file, final String contentText) throws FileUtilsException {
    try {
      writeStringToFile(file, contentText, STANDARD_ENCODING);
    } catch (Exception e) {
      String errorMessage = logHelper.logFileWriteError(file, e);
      throw new IllegalArgumentException(errorMessage);
    }
  }

  /**
   * Append the given {@code data} to the specified {@code file}.
   */
  public static void appendToFile(final File file, final String data) throws IOException {
    FileWriter fw = new FileWriter(file, true);
    fw.write(data);
    fw.close();
  }

  public static String readFromFile(final URL url) throws FileUtilsException {
    return readFromFile(urlToFile(url));
  }

  public static String readFromFile(final File file) throws FileUtilsException {
    try {
      return readFileToString(file, STANDARD_ENCODING);
    } catch (IOException e) {
      String msg = logHelper.logFileReadError(e);
      throw new FileUtilsException(msg, e);
    }
  }

  public static final boolean unzipArchive(final File fromFile, final File toDir) throws IOException {
    boolean result = false; // assumes error

    logHelper.logBeginUnzip(fromFile, toDir);

    ZipFile zipFile = new ZipFile(fromFile);
    Enumeration<? extends ZipEntry> entries = zipFile.entries();

    if (!toDir.exists()) {
      toDir.mkdirs();
      logHelper.logProgressCreatedToDir(toDir);
    }
    logHelper.logProgressToDirIsWritable(toDir);

    if (toDir.canWrite()) {
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();

        if (entry.isDirectory()) {
          File dir = new File(toDir.getAbsolutePath() + EnvironmentHelper.FILE_PATH_SEPARATOR + entry.getName());

          if (!dir.exists()) {
            dir.mkdirs();
            logHelper.logProgressFilePathCreated(dir);
          }
        } else {
          File fosz = new File(toDir.getAbsolutePath() + EnvironmentHelper.FILE_PATH_SEPARATOR + entry.getName());
          logHelper.logProgressCopyEntry(fosz);

          File parent = fosz.getParentFile();
          if (!parent.exists()) {
            parent.mkdirs();
          }

          BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(fosz));
          IOUtils.copy(zipFile.getInputStream(entry), bos);
          bos.flush();
        }
      }
      zipFile.close();
      result = true; // success
    } else {
      logHelper.logFileWriteError(fromFile, null);
      zipFile.close();
    }

    return result;
  }

  /**
   * If {@code reader} not {@code null}, close it and ignore {@code IOException}
   *
   * @param reader
   */
  public static void closeReader(final Reader reader) {
    if (reader != null) {
      try {
        reader.close();
      } catch (IOException e) {
        // ignore IOException
      }
    }
  }

  /**
   * If {@code writer} not {@code null}, close it and ignore {@code IOException}
   *
   * @param reader
   */
  public static void closeWriter(final Writer writer) {
    if (writer != null) {
      try {
        writer.close();
      } catch (IOException e) {
        // ignore IOException
      }
    }
  }
}
