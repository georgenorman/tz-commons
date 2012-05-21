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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.w3c.dom.Document;

import com.thruzero.common.core.infonode.builder.utils.SampleNodeBuilderUtils;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for XmlUtils.
 *
 * @author George Norman
 */
public class XmlUtilsTest extends AbstractCoreTestCase {

  @Test
  public void testCreateDocumentBuilder() {
    DocumentBuilder builder = null;

    try {
      builder = XmlUtils.createDocumentBuilder();
    } catch (ParserConfigurationException e) {
      // ignore
    }

    assertNotNull(builder);
  }

  @Test
  public void testReadNodeElementsToStringMap() {
    Document dom = null;
    InputStream inputStream = StringUtilsExt.stringToInputStream(SampleNodeBuilderUtils.RESULT_AS_STRING_NESTED_INFO_NODE);
    try {
      dom = XmlUtils.createDocument(inputStream, null);
    } catch (Exception e) {
      // ignore
    }

    assertNotNull(dom);
    try {
      //String debugStr = XmlWriterUtils.domToText(dom);
      StringMap map = XmlUtils.readNodeElementsToStringMap(dom, "//" + SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME);
      String eleOneValue = map.get(SampleNodeBuilderUtils.TEST_ELEMENT_ONE_NAME);
      assertNotNull(eleOneValue);
      assertEquals(SampleNodeBuilderUtils.TEST_ELEMENT_ONE_VALUE, eleOneValue);
    } catch (Exception e) {
      fail("readNodeElementsToStringMap generated Exception: " + e);
    }

  }

}
