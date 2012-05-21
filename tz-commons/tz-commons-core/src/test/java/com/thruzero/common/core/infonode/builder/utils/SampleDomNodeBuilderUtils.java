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
package com.thruzero.common.core.infonode.builder.utils;

import static org.junit.Assert.assertEquals;

import org.w3c.dom.Document;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.utils.XmlUtils;
import com.thruzero.common.core.utils.XmlUtils.LoggingErrorHandler;

/**
 * Utils for testing DomInfoNodeBuilder.
 *
 * @author George Norman
 */
public class SampleDomNodeBuilderUtils extends SampleNodeBuilderUtils {

  public static Document createSimpleTokenDomNode() {
    Document result;

    try {
      result = XmlUtils.createDocument("<" + TEST_ELEMENT_ONE_NAME + " " + TEST_ATTRIBUTE_ONE_KEY + "='" + TEST_ATTRIBUTE_ONE_VALUE + "' " + TEST_ATTRIBUTE_TWO_KEY + "='"
          + TEST_ATTRIBUTE_TWO_VALUE + "'>" + TEST_ELEMENT_ONE_VALUE + "</" + TEST_ELEMENT_ONE_NAME + ">", new LoggingErrorHandler("SimpleTokenDomNode"));
    } catch (Exception e) {
      result = null;
    }

    return result;
  }

  public static Document createSimpleTokenDomNode2() {
    Document result;

    try {
      result = XmlUtils.createDocument("<" + TEST_ELEMENT_ONE_NAME + " " + TEST_ATTRIBUTE_ONE_KEY + "='" + TEST_ATTRIBUTE_ONE_VALUE + "' " + TEST_ATTRIBUTE_TWO_KEY + "='"
          + TEST_ATTRIBUTE_TWO_VALUE + "'>" + TEST_ELEMENT_ONE_VALUE + "</" + TEST_ELEMENT_ONE_NAME + ">", new LoggingErrorHandler("SimpleTokenDomNode2"));
    } catch (Exception e) {
      result = null;
    }

    return result;
  }

  /**
   * <pre>
   * {@code
   * <?xml version="1.0" encoding="UTF-8"?>
   * <!DOCTYPE TestParentElement [
   * <!ELEMENT TestParentElement (Test0Element,Test1Element)>
   *  <!ELEMENT Test0Element (#PCDATA)>
   *  <!ELEMENT Test1Element (#PCDATA)>
   * ]>
   * <TestParentElement>
   *   <Test0Element>Test0ElementValue</Test0Element>
   *   <Test1Element>Test1ElementValue</Test1Element>
   * </TestParentElement>
   * }
   * </pre>
   */
  public static String createSimpleDocuemtNoAttributes(final boolean includePreamble) {
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(false).buildInfoNode(TEST_PARENT_ELEMENT_NAME, null, null);
    for (short i = 0; i <= 1; i++) {
      infoNode.addChildNode(createNamedInfoNode("Test" + i, 0, false));
    }

    String result = includePreamble ? TEST_XML_DECLARATION + "\n" + TEST_SIMPLE_DOCUMENT_TYPE + "\n" + infoNode.toStringFormatted() : infoNode.toStringFormatted();

    return result;
  }

  // Validation methods ///////////////////////////////////////////////////////////////////

  public static void verifySimpleDocumentRootElement(final InfoNodeElement rootElement) {
    String xml = createSimpleDocuemtNoAttributes(false);

    assertEquals(rootElement.toStringFormatted(), xml);
  }

}
