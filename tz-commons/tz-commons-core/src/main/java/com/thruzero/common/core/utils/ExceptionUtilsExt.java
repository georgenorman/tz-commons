/*
 *   Copyright 2007 George Norman
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

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Extensions to the apache {@code ExceptionUtils} utility class.
 *
 * @author George Norman
 */
public class ExceptionUtilsExt extends ExceptionUtils {

  /** Allow for class extensions; disallow client instantiation */
  protected ExceptionUtilsExt() {
  }

  public static IllegalArgumentException logAndCreateIllegalArgumentException(final Logger logger, final String errorMessage, final Exception cause) {
    String err = errorMessage + cause;

    logger.error(err);

    return new IllegalArgumentException(err);
  }

  public static IllegalArgumentException logAndCreateIllegalArgumentException(final Logger logger, final String errorMessage) {
    logger.error(errorMessage);

    return new IllegalArgumentException(errorMessage);
  }

  public static RuntimeException logAndCreateRuntimeException(final Logger logger, final String errorMessage, final Exception cause) {
    String err = errorMessage + cause;

    logger.error(err);

    return new RuntimeException(err);
  }

  public static RuntimeException logAndCreateRuntimeException(final Logger logger, final String errorMessage) {
    logger.error(errorMessage);

    return new RuntimeException(errorMessage);
  }

  /**
   * Return the single line, from the stack trace, immediately after where the given class is found.
   */
  public static String findTraceAfter(final Class<?> clazz) {
    String result = null;

    try {
      throw new Exception();
    } catch (Exception e) {
      StringWriter s1 = new StringWriter();
      PrintWriter p1 = new PrintWriter(s1);

      e.printStackTrace(p1);

      boolean found = false;
      String line = null;
      BufferedReader br = new BufferedReader(new StringReader(s1.toString()));
      try {
        while ((line = br.readLine()) != null) {
          // loop UNTIL clazz is found
          if (!found) {
            found = line.indexOf(clazz.getName()) >= 0;
            continue;
          }

          // loop WHILE clazz is found
          if (line.indexOf(clazz.getName()) >= 0) {
            continue;
          }

          // after clazz comes the real trace we're looking for
          result = line;
          result = result.trim();

          break;
        }
      } catch (Exception re) {
        result = "can't find line";
      }

    }

    return result;
  }

  /** Return the single line, from the stack trace, where the given class is found. */
  public static String findTraceAt(final Class<?> clazz) {
    String result = null;

    try {
      throw new Exception();
    } catch (Exception e) {
      StringWriter s1 = new StringWriter();
      PrintWriter p1 = new PrintWriter(s1);

      e.printStackTrace(p1);

      boolean found = false;
      String line = null;
      BufferedReader br = new BufferedReader(new StringReader(s1.toString()));
      try {
        while ((line = br.readLine()) != null) {
          // loop UNTIL clazz is found
          found = line.indexOf(clazz.getName()) >= 0;
          if (!found) {
            continue;
          }

          result = line;
          result = result.trim();

          break;
        }
      } catch (Exception re) {
        result = "can't find line";
      }

    }

    return result;
  }

  public static String findMethodAfterClassFromTrace(final Class<?> clazz) {
    String result = findTraceAfter(clazz);
    result = StringUtils.substringBefore(result, "(");
    return StringUtils.substringAfterLast(result, ".");
  }

  public static String findMethodAtClassFromTrace(final Class<?> clazz) {
    String result = findTraceAt(clazz);
    result = StringUtils.substringBefore(result, "(");
    return StringUtils.substringAfterLast(result, ".");
  }

  /**
   * Return the given message surrounded by "*************".
   */
  public static String decorateMessageLevel1(final String message) {
    return "\n*************\n" + message + "\n*************";
  }

  public static String decorateMessageLevel1a(final String message) {
    return "\n*************\n" + message + "\n-------------\n";
  }

  /**
   * Return the given message surrounded by "-------------".
   */
  public static String decorateMessageLevel2(final String message) {
    return "\n-------------\n" + message + "\n-------------";
  }

  public static String decorateMessageLevel2b(final String message) {
    return message + "\n*************";
  }
}
