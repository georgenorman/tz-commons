/*
 *   Copyright 2011-2012 George Norman
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
package com.thruzero.common.core.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.domain.jpa.test.support.AbstractDomainJpaTestCase;

/**
 * A single, simple test to ensure the config system is set up properly.
 *
 * @author George Norman
 */
public class ConfigTest extends AbstractDomainJpaTestCase {

  @Test
  public void testLocateConfig() {
    Config config = ConfigLocator.locate();

    assertNotNull(config);
  }

  @Test
  public void testSimpleGet() {
    Config config = ConfigLocator.locate();
    String result = config.getValue("testTypes", "float12dot34", null);
    assertEquals("12.34", result);
  }

}
