/*
 *   Copyright 2006 George Norman
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Static utility methods pertaining to {@code Class} objects.
 *
 * <p>
 * <b>Examples:</b>
 *
 * <pre>
 * <code>
 *  // Example: Create an export command
 *  Command exportCommand = (Command)ClassUtils.instanceFrom("com.thruzero.jcat.apps.admin.actions.commands.ExportCommand");
 *
 *  // Example: Get the class from a specified name
 *  Class<StringMap> api = ClassUtils.classFrom("com.thruzero.common.core.map.StringMap");
 * </code>
 * </pre>
 *
 * @author George Norman
 */
public class ClassUtils {

  // ------------------------------------------------
  // ClassUtilsException
  // ------------------------------------------------

  /** An exception thrown by {@code ClassUtils}. */
  public static class ClassUtilsException extends Exception {
    private static final long serialVersionUID = 100;

    public ClassUtilsException(final String message, final Throwable cause) {
      super(ExceptionUtilsExt.decorateMessageLevel1(message), cause);
    }

    public ClassUtilsException(final String message) {
      super(ExceptionUtilsExt.decorateMessageLevel1(message));
    }
  }

  // ==================================================================
  // ClassUtils
  // ==================================================================

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected ClassUtils() {
  }

  /**
   * Return {@code true} if the named class can be loaded.
   */
  public static boolean isValidClassName(final String fullClassName) {
    boolean result = false;

    try {
      Class.forName(fullClassName);
      result = true;
    } catch (Exception e) {
      // ignore - returns false on invalid class name
    }

    return result;
  }

  /**
   * Load named class. Throws {@code ClassError} if class is not found.
   *
   * @throws ClassUtilsException
   */
  @SuppressWarnings("unchecked")
  public static <T> Class<T> classFrom(final String fullClassName) throws ClassUtilsException {
    try {
      return (Class<T>)Class.forName(fullClassName);
    } catch (Exception e) {
      throw new ClassUtilsException("ERROR: Class not found (" + fullClassName + ").", e);
    }
  }

  /**
   * Create an instance of the named class, using the default constructor.
   *
   * @throws ClassUtilsException
   */
  public static <T> T instanceFrom(final Class<?> clazz) throws ClassUtilsException {
    T result = null;

    if (clazz != null) {
      Constructor<?>[] constructorArray = clazz.getDeclaredConstructors();

      try {
        // find default constructor TODO-p1(george) SHOULD be able to use "getConstructor(null)" to find default.
        Constructor<?> constructor = null;
        for (Constructor<?> element : constructorArray) {
          if (element.getParameterTypes().length == 0) {
            element.setAccessible(true);

            constructor = element;
            break;
          }
        }

        // assert constructor was found
        if (constructor == null) {
          throw new ClassUtilsException("ERROR: Default constructor was not found for: '" + clazz.getName() + "'.");
        }

        // create new instance
        @SuppressWarnings("unchecked")
        T newInstance = (T)constructor.newInstance((Object[])null);
        result = newInstance;
      } catch (SecurityException e) {
        throw new ClassUtilsException("ERROR: SecurityException for (" + clazz.getName() + ").", e);
      } catch (IllegalArgumentException e) {
        throw new ClassUtilsException("ERROR: IllegalArgumentException for (" + clazz.getName() + ").", e);
      } catch (InstantiationException e) {
        throw new ClassUtilsException("ERROR: InstantiationException for (" + clazz.getName() + ").", e);
      } catch (IllegalAccessException e) {
        throw new ClassUtilsException("ERROR: IllegalAccessException for (" + clazz.getName() + ").", e);
      } catch (InvocationTargetException e) {
        throw new ClassUtilsException("ERROR: InvocationTargetException for (" + clazz.getName() + ").", e);
      }
    }

    return result;
  }

  public static <T> void invokeMethod(final Object o, final Class<?> clazz, final String methodName, final Class<?>[] paramTypes, final Object[] arguments) throws ClassUtilsException {
    try {
      Method m = clazz.getMethod("init", paramTypes);

      m.setAccessible(true);
      m.invoke(o, arguments);
    } catch (IllegalArgumentException e) {
      throw new ClassUtilsException("ERROR: IllegalArgumentException for (" + clazz.getName() + ").", e);
    } catch (IllegalAccessException e) {
      throw new ClassUtilsException("ERROR: IllegalAccessException for (" + clazz.getName() + ").", e);
    } catch (InvocationTargetException e) {
      throw new ClassUtilsException("ERROR: InvocationTargetException for (" + clazz.getName() + ").", e);
    } catch (SecurityException e) {
      throw new ClassUtilsException("ERROR: SecurityException for (" + clazz.getName() + ").", e);
    } catch (NoSuchMethodException e) {
      throw new ClassUtilsException("ERROR: NoSuchMethodException for (" + clazz.getName() + ").", e);
    }

  }

  public static <T> void invokeDeclaredMethod(final Object o, final Class<?> clazz, final String methodName, final Class<?>[] paramTypes, final Object[] arguments)
      throws ClassUtilsException {
    try {
      Method m = clazz.getDeclaredMethod("init", paramTypes);

      m.setAccessible(true);
      m.invoke(o, arguments);
    } catch (IllegalArgumentException e) {
      throw new ClassUtilsException("ERROR: IllegalArgumentException for (" + clazz.getName() + ").", e);
    } catch (IllegalAccessException e) {
      throw new ClassUtilsException("ERROR: IllegalAccessException for (" + clazz.getName() + ").", e);
    } catch (InvocationTargetException e) {
      throw new ClassUtilsException("ERROR: InvocationTargetException for (" + clazz.getName() + ").", e);
    } catch (SecurityException e) {
      throw new ClassUtilsException("ERROR: SecurityException for (" + clazz.getName() + ").", e);
    } catch (NoSuchMethodException e) {
      throw new ClassUtilsException("ERROR: NoSuchMethodException for (" + clazz.getName() + ").", e);
    }

  }

  /**
   * Call the specified static method on the given class, using the given arguments.
   */
  @SuppressWarnings("unchecked")
  public static <T> T invokeStaticMethod(final Class<T> clazz, final String methodName, final Class<?>[] parameterTypes, final Object[] arglist) throws NoSuchMethodException,
      IllegalAccessException, InvocationTargetException {

    Method method = clazz.getMethod(methodName, parameterTypes);

    return (T)method.invoke(null, arglist);
  }

}
