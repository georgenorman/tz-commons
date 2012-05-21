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
package com.thruzero.common.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;

import com.thruzero.common.core.support.EnvironmentHelper;

/**
 * Static utility methods pertaining to logging and tracking elapsed time.
 *
 * @author George Norman
 */
public class PerformanceTimerUtils {
  private static final Logger logger = Logger.getLogger(PerformanceTimerUtils.class);

  private static ThreadLocal<PerformanceLogger> threadLocal = new ThreadLocal<PerformanceLogger>();

  // ----------------------------------------------
  // PerformanceLoggerHelper
  // ----------------------------------------------

  public static class PerformanceLoggerHelper {
    public static final String ELAPSED_TIME_PARAM = "${elapsedTime}";

    private long startTime;

    public PerformanceLoggerHelper() {
      try {
        PerformanceLogger performanceLogger = PerformanceTimerUtils.get();

        startTime = performanceLogger.getRunningMillis();
      } catch (Exception e) {
        startTime = -1;
      }
    }

    public String getFormattedElapsedTime() {
      String result;

      try {
        if (startTime < 0) {
          result = "N/A";
        } else {
          PerformanceLogger performanceLogger = PerformanceTimerUtils.get();

          result = DateTimeFormatUtilsExt.formatElapsedTime(startTime, performanceLogger.getRunningElapsedMillis());
        }
      } catch (Exception e) {
        result = "N/A";
      }

      return result;
    }

    public void debug(final String message) {
      try {
        PerformanceLogger performanceLogger = PerformanceTimerUtils.get();
        String elapsedTime = getFormattedElapsedTime();

        performanceLogger.debug(StringUtils.replace(message, ELAPSED_TIME_PARAM, elapsedTime));
      } catch (Exception e) {
        logger.error("PerformanceLoggerHelper.debug(String) generated and Exception: ", e);
      }
    }
  }

  // ----------------------------------------------
  // PerformanceLogger
  // ----------------------------------------------

  public static class PerformanceLogger {
    private final StopWatch stopWatch = new StopWatch();

    private final List<String> logEntries = new ArrayList<String>();
    private long startMillis;
    private long endMillis;

    public void resetAndStart() {
      stopWatch.reset();
      stopWatch.start();
      startMillis = stopWatch.getTime();
    }

    public void start() {
      stopWatch.start();
      startMillis = stopWatch.getTime();
    }

    public void stop() {
      stopWatch.stop();
      endMillis = stopWatch.getTime();
    }

    public long getStartMillis() {
      return startMillis;
    }

    public long getEndMillis() {
      return endMillis;
    }

    public long getRunningMillis() {
      return stopWatch.getTime();
    }

    public long getRunningElapsedMillis() {
      return getRunningMillis() - startMillis;
    }

    public void debug(final String message) {
      logEntries.add(message);
    }

    public String getLogEntries() {
      StringBuilder result = new StringBuilder(EnvironmentHelper.NEWLINE);

      for (String line : logEntries) {
        result.append(line + EnvironmentHelper.NEWLINE);
      }

      return result.toString();
    }
  }

  // ==============================================================
  // PerformanceTimerUtils
  // ==============================================================

  /** Allow for class extensions; disallow client instantiation */
  protected PerformanceTimerUtils() {
  }

  public static PerformanceLogger get() {
    return threadLocal.get();
  }

  public static void set(final PerformanceLogger performanceLogger) {
    threadLocal.set(performanceLogger);
  }

}
