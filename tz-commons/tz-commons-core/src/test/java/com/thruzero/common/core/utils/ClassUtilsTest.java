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
package com.thruzero.common.core.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.utils.ClassUtils.ClassUtilsException;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for ClassUtils.
 *
 * @author George Norman
 */
public class ClassUtilsTest extends AbstractCoreTestCase {

  @Test
  public void testIsValidClassName() throws ClassUtilsException {
    assertTrue(ClassUtils.isValidClassName(StringMap.class.getName()));
    assertFalse(ClassUtils.isValidClassName("ThisIsABogusClassName"));
  }

  @Test
  public void testClassFrom() throws ClassUtilsException {
    Class<StringMap> result = ClassUtils.classFrom(StringMap.class.getName());

    assertNotNull("ClassUtils.classFrom(String) failed to get the Class.", result);
    assertEquals("The resulting class does not match the request class.", StringMap.class.getName(), result.getName());
  }

  @Test
  public void testInstanceFrom() throws ClassUtilsException {
    Class<StringMap> clazz = ClassUtils.classFrom(StringMap.class.getName());
    StringMap result = ClassUtils.instanceFrom(clazz);

    assertNotNull("ClassUtils.instanceFrom(String) failed to construct an instance.", result);
  }

}
