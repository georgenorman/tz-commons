/*
 *   Copyright 2010 George Norman
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
package com.thruzero.common.core.support;

import java.io.File;
import java.util.Map;

import com.thruzero.common.core.bookmarks.EnvironmentVarKeyBookmark;
import com.thruzero.common.core.config.Config.ConfigKeys;
import com.thruzero.common.core.locator.ConfigLocator;

/**
 * Helper functions and constants related to the java runtime environment (e.g., total memory, java version, etc).
 *
 * @author George Norman
 */
public final class EnvironmentHelper implements Singleton {
  private static final EnvironmentHelper instance = new EnvironmentHelper();

  /** The system-dependent new-line character. */
  public static final String NEWLINE = System.getProperty(EnvironmentVariableKeys.LINE_SEPARATOR_ENV_VAR);

  /** The system-dependent class-path separator used to separate filenames (e.g., ':' on UNIX and ';' for Windows). */
  public static final String CLASS_PATH_SEPARATOR = File.pathSeparator;

  /** The system-dependent file name separator used in a file path (e.g., '/' on UNIX, '\\' for Windows). */
  public static final String FILE_PATH_SEPARATOR = File.separator;

  // ------------------------------------------------
  // EnvironmentVariableKeys
  // ------------------------------------------------

  /** Keys used to read useful environment variables via the {@link System#getProperty(String)} method. */
  @EnvironmentVarKeyBookmark()
  public interface EnvironmentVariableKeys {
    // http://stackoverflow.com/questions/2821043/allowed-characters-in-linux-environment-variable-names
    /** Class paths to the .jar, .zip or .class files */
    String JAVA_CLASS_PATH_ENV_VAR = "java.class.path";

    /** Java Runtime Environment version */
    String JAVA_VERSION_ENV_VAR = "java.version";

    /** Java class format version number */
    String JAVA_CLASS_VERSION_ENV_VAR = "java.class.version";

    /** line separator key (retrieves "\n" for linux, "\r\n" for windows, etc). */
    String LINE_SEPARATOR_ENV_VAR = "line.separator";

    /** Operating system name */
    String OS_NAME_ENV_VAR = "os.name";

    /** Operating system version */
    String OS_VERSION_ENV_VAR = "os.version";

    /** Operating system architecture (e.g., "Windows Vista - 6.1 - x86", "Linux - 2.6.32.42-grsec - i386"). */
    String OS_ARCHITECTURE_ENV_VAR = "os.arch";
  }

  // --------------------------------------------------
  // EnvironmentNames
  // --------------------------------------------------

  public interface EnvironmentNames {
    String DEV = "DEV";
    String PRE = "PRE";
    String PRD = "PRD";
  }

  // ============================================================
  // EnvironmentHelper
  // ============================================================

  private EnvironmentHelper() {
  }

  /**
   * Returns the {@code EnvironmentHelper} singleton.
   */
  public static EnvironmentHelper getInstance() {
    return instance;
  }

  public String getJavaClassPath() {
    return System.getProperty(EnvironmentVariableKeys.JAVA_CLASS_PATH_ENV_VAR);
  }

  public String getJavaVersion() {
    return System.getProperty(EnvironmentVariableKeys.JAVA_VERSION_ENV_VAR) + " - Class Version " + System.getProperty(EnvironmentVariableKeys.JAVA_CLASS_VERSION_ENV_VAR);
  }

  public String getJavaTotalMemory() {
    return Runtime.getRuntime().totalMemory() / (1024000) + "M";
  }

  public String getJavaFreeMemory() {
    return Runtime.getRuntime().freeMemory() / (1024000) + "M";
  }

  public String getJavaMaxMemory() {
    return Runtime.getRuntime().maxMemory() / (1024000) + "M";
  }

  public String getOs() {
    return System.getProperty(EnvironmentVariableKeys.OS_NAME_ENV_VAR) + " - " + System.getProperty(EnvironmentVariableKeys.OS_VERSION_ENV_VAR) + " - "
        + System.getProperty(EnvironmentVariableKeys.OS_ARCHITECTURE_ENV_VAR);
  }

  public String getEnvVars() {
    StringBuilder result = new StringBuilder("");

    Map<String, String> env = System.getenv();
    for (Map.Entry<String, String> entry : env.entrySet()) {
      result.append(entry.getKey());
      result.append(" = ");
      result.append(entry.getValue());
      result.append("<br/>");
    }

    return result.toString();
  }

  public String getEnvVar(final String key) {
    return System.getenv().get(key);
  }

  public String getHostUrl() {
    String result = ConfigLocator.locate().getValue(ConfigKeys.ENVIRONMENT_SECTION, "hostUrl");

    return result;
  }

  /**
   * Return the name of the environment the application is currently executing in (e.g., DEV, PRE, PRD). The environment
   * name is set via the "com_thruzero_env" environment variable.
   */
  public String getEnvironmentName() {
    String deployEnvironment = System.getenv("com_thruzero_env");

    return deployEnvironment;
  }

  public boolean isDevEnvironment() {
    return EnvironmentNames.DEV.equals(getEnvironmentName());
  }

  public boolean isPreEnvironment() {
    return EnvironmentNames.PRE.equals(getEnvironmentName());
  }

  public boolean isPrdEnvironment() {
    return EnvironmentNames.PRD.equals(getEnvironmentName());
  }

}
