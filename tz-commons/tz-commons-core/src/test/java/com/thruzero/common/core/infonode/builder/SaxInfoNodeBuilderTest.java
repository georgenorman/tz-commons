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

import java.io.IOException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.AbstractInfoNodeBuilder.RootNodeOption;
import com.thruzero.common.core.infonode.builder.utils.SampleInfoNodeBuilderUtils;
import com.thruzero.common.core.infonode.builder.utils.SampleNodeBuilderUtils;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for SaxInfoNodeBuilder.
 *
 * @author George Norman
 */
public class SaxInfoNodeBuilderTest extends AbstractCoreTestCase {

  @Test
  public void testSaxInfoNodeBuilder() {
    String xmlContent = "<test a=\"A\" b=\"B\">T1V1" + "  <ele1 c=\"C\">" + "  <!-- this is a comment -->" + "  Element1 Value" + "  </ele1>T1V2" + "</test>";

    //  <test a="A" b="B">
    //    <ele1 c="C">Element1 Value</ele1>
    //    T1V1 T1V2
    //  </test>
    InfoNodeElement test = SaxInfoNodeBuilder.WITH_ROOT_NODE.buildInfoNode(xmlContent, null);

    SampleNodeBuilderUtils.assertEqualNormalizedValues("test", test.getName());
    SampleNodeBuilderUtils.assertEqualNormalizedValues("A", test.getAttributeValue("a"));
    SampleNodeBuilderUtils.assertEqualNormalizedValues("B", test.getAttributeValue("b"));
    SampleNodeBuilderUtils.assertEqualNormalizedChildAttributeValues(test, "ele1", "c", "C");
  }

  @Test
  public void testCreateSimpleInfoNode() throws IOException, SAXException {
    InfoNodeElement sampleInfoNode = SampleInfoNodeBuilderUtils.createSimpleInfoNodeWithAttributes();

    // <TestParentElement TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestParentElementValue</TestParentElement>
    InfoNodeElement rootInfoNodeElement = SaxInfoNodeBuilder.DEFAULT.buildInfoNode(sampleInfoNode.toStringFormatted(), null);

    SampleNodeBuilderUtils.verifySimpleInfoNodeData(rootInfoNodeElement);
    SampleNodeBuilderUtils.verifySimpleInfoNodeAsString(rootInfoNodeElement);
  }

  @Test
  public void testPrimaryKeyInfoNode() throws IOException, SAXException {
    InfoNodeElement sampleInfoNode = SampleInfoNodeBuilderUtils.createEntityPathInfoNode();

    // <TestParentElement TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue" primaryKey="primaryValueOne">TestParentElementValue</TestParentElement>
    InfoNodeElement rootInfoNodeElement = SaxInfoNodeBuilder.DEFAULT.buildInfoNode(sampleInfoNode.toStringFormatted(), null);

    assertEquals(SampleNodeBuilderUtils.ENTITY_PATH_VALUE_ONE, rootInfoNodeElement.getEntityPath().toString());
    SampleNodeBuilderUtils.verifySimpleInfoNodeData(rootInfoNodeElement);
  }

  @Test
  public void testCreateSimpleNestedInfoNode() throws IOException, SAXException {
    InfoNodeElement sampleInfoNode = SampleInfoNodeBuilderUtils.createSimpleNestedInfoNode(RootNodeOption.GENERATE_ROOT_NODE);

    //  <TestParentElement>
    //    TestParentElementValue
    //    <TestElementOne TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementOneValue</TestElementOne>
    //    <TestElementTwo TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementTwoValue</TestElementTwo>
    //  </TestParentElement>    String infoNodeAsString = rootInfoNodeElement.toString();
    InfoNodeElement rootInfoNodeElement = SaxInfoNodeBuilder.DEFAULT.buildInfoNode(sampleInfoNode.toStringFormatted(), null);

    SampleNodeBuilderUtils.assertEqualNormalizedValues("TestParentElement", rootInfoNodeElement.getName());
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(rootInfoNodeElement, "TestElementOne", "TestElementOneValue");
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(rootInfoNodeElement, "TestElementTwo", "TestElementTwoValue");
  }

}
