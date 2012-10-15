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
package com.thruzero.common.core.infonode.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.thruzero.common.core.infonode.InfoNodeDocument;
import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.AbstractInfoNodeBuilder.RootNodeOption;
import com.thruzero.common.core.infonode.builder.utils.SampleDomNodeBuilderUtils;
import com.thruzero.common.core.infonode.builder.utils.SampleDomNodeBuilderUtils.PreambleOption;
import com.thruzero.common.core.infonode.builder.utils.SampleInfoNodeBuilderUtils;
import com.thruzero.common.core.infonode.builder.utils.SampleNodeBuilderUtils;
import com.thruzero.common.core.utils.XmlUtils;
import com.thruzero.common.core.utils.XmlUtils.LoggingErrorHandler;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for DomInfoNodeBuilder.
 *
 * @author George Norman
 */
public class DomInfoNodeBuilderTest extends AbstractCoreTestCase {

  @Test
  public void testCreateSimpleInfoNode() throws IOException, SAXException {
    InfoNodeElement sampleInfoNode = SampleInfoNodeBuilderUtils.createSimpleInfoNodeWithAttributes();
    Document w3cDocument = XmlUtils.createDocument(sampleInfoNode.toStringFormatted(), null);

    // <TestParentElement TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestParentElementValue</TestParentElement>
    InfoNodeDocument infoNodeDocument = DomInfoNodeBuilder.DEFAULT.buildInfoNode(w3cDocument);
    InfoNodeElement rootInfoNodeElement = infoNodeDocument.getRootInfoNodeElement();

    SampleNodeBuilderUtils.verifySimpleInfoNodeData(rootInfoNodeElement);
    SampleNodeBuilderUtils.verifySimpleInfoNodeAsString(rootInfoNodeElement);
  }

  @Test
  public void testPrimaryKeyInfoNode() throws IOException, SAXException {
    InfoNodeElement sampleInfoNode = SampleInfoNodeBuilderUtils.createEntityPathInfoNode();
    Document w3cDocument = XmlUtils.createDocument(sampleInfoNode.toStringFormatted(), null);

    // <TestParentElement TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue" primaryKey="primaryValueOne">TestParentElementValue</TestParentElement>
    InfoNodeDocument infoNodeDocument = DomInfoNodeBuilder.DEFAULT.buildInfoNode(w3cDocument);
    InfoNodeElement rootInfoNodeElement = infoNodeDocument.getRootInfoNodeElement();

    assertEquals(SampleNodeBuilderUtils.ENTITY_PATH_VALUE_ONE, rootInfoNodeElement.getEntityPath().toString());
    SampleNodeBuilderUtils.verifySimpleInfoNodeData(rootInfoNodeElement);
  }

  @Test
  public void testCreateSimpleNestedInfoNode() throws IOException, SAXException {
    InfoNodeElement sampleInfoNode = SampleInfoNodeBuilderUtils.createSimpleNestedInfoNode(RootNodeOption.GENERATE_ROOT_NODE);
    Document w3cDocument = XmlUtils.createDocument(sampleInfoNode.toStringFormatted(), null);

    //  <TestParentElement>
    //    TestParentElementValue
    //    <TestElementOne TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementOneValue</TestElementOne>
    //    <TestElementTwo TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementTwoValue</TestElementTwo>
    //  </TestParentElement>    String infoNodeAsString = rootInfoNodeElement.toString();
    InfoNodeDocument infoNodeDocument = DomInfoNodeBuilder.DEFAULT.buildInfoNode(w3cDocument);
    InfoNodeElement rootInfoNodeElement = infoNodeDocument.getRootInfoNodeElement();

    String infoNodeAsString = rootInfoNodeElement.toString();
    SampleNodeBuilderUtils.assertEqualNormalizedValues(SampleNodeBuilderUtils.RESULT_AS_STRING_NESTED_INFO_NODE, infoNodeAsString);
  }

  @Test
  public void testCreateComplexNestedInfoNode() throws IOException, SAXException {
    InfoNodeElement sampleInfoNode = SampleInfoNodeBuilderUtils.createComplexNestedInfoNodeWithParentValue(RootNodeOption.GENERATE_ROOT_NODE, 10);
    Document w3cDocument = XmlUtils.createDocument(sampleInfoNode.toStringFormatted(), null);
    InfoNodeDocument infoNodeDocument = DomInfoNodeBuilder.DEFAULT.buildInfoNode(w3cDocument);
    InfoNodeElement rootInfoNodeElement = infoNodeDocument.getRootInfoNodeElement();

    Iterator<? extends InfoNodeElement> iter = rootInfoNodeElement.getChildNodeIterator();
    short i = 0;
    while (iter.hasNext()) {
      InfoNodeElement childNode = iter.next();
      SampleNodeBuilderUtils.verifyNamedInfoNodeData(childNode, "Test" + i++);
    }
  }

  @Test
  public void testNestedNodeNoParentValue() throws IOException, SAXException {
    InfoNodeElement sampleInfoNode = SampleInfoNodeBuilderUtils.createComplexNestedInfoNodeWithoutParentValue(RootNodeOption.GENERATE_ROOT_NODE, 10);
    Document w3cDocument = XmlUtils.createDocument(sampleInfoNode.toStringFormatted(), null);
    InfoNodeDocument infoNodeDocument = DomInfoNodeBuilder.DEFAULT.buildInfoNode(w3cDocument);
    InfoNodeElement rootInfoNodeElement = infoNodeDocument.getRootInfoNodeElement();

    // verify that parent to child transition is smooth (i.e., no null printed in result)
    String partialResultStr = StringUtils.substringBetween(rootInfoNodeElement.toString(), "<TestParentElement", "<Test0Element");
    assertTrue(partialResultStr.trim().equals(">"));
  }

  @Test
  public void testCreateSimpleDocumentInfoNode() throws IOException, SAXException {
    String xmlDoc = SampleDomNodeBuilderUtils.createSimpleDocuemtNoAttributes(PreambleOption.EXCLUDED);
    Document w3cDocument = XmlUtils.createDocument(xmlDoc, new LoggingErrorHandler("ComplexNestedInfoNode"));
    InfoNodeDocument infoNodeDocument = DomInfoNodeBuilder.DEFAULT.buildInfoNode(w3cDocument);
    InfoNodeElement rootInfoNodeElement = infoNodeDocument.getRootInfoNodeElement();

    SampleDomNodeBuilderUtils.verifySimpleDocumentRootElement(rootInfoNodeElement);
  }

}
