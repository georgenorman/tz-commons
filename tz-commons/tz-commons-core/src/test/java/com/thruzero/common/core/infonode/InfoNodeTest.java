/*
 *   Copyright 2008-2011 George Norman
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
package com.thruzero.common.core.infonode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jdom.JDOMException;
import org.junit.Test;

import com.thruzero.common.core.infonode.builder.AbstractInfoNodeBuilder.RootNodeOption;
import com.thruzero.common.core.infonode.builder.ExpressInfoNodeBuilder;
import com.thruzero.common.core.infonode.builder.utils.SampleInfoNodeBuilderUtils;
import com.thruzero.common.core.infonode.builder.utils.SampleNodeBuilderUtils;
import com.thruzero.common.core.support.EnvironmentHelper;
import com.thruzero.common.core.utils.StringUtilsExt;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for InfoNodeElement.
 *
 * @author George Norman
 */
public class InfoNodeTest extends AbstractCoreTestCase {
  private static final Logger logger = Logger.getLogger(InfoNodeTest.class);

  @Test
  public void testCreateSimpleNestedInfoNode() {
    /*
      <TestParentElement>
        TestParentElementValue
        <TestElementOne TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementOneValue</TestElementOne>
        <TestElementTwo TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementTwoValue</TestElementTwo>
      </TestParentElement>
     */
    InfoNodeElement infoNode = SampleInfoNodeBuilderUtils.createSimpleNestedInfoNode(RootNodeOption.GENERATE_ROOT_NODE);
    String infoNodeAsString = infoNode.toString();
    SampleNodeBuilderUtils.assertEqualNormalizedValues(SampleNodeBuilderUtils.RESULT_AS_STRING_NESTED_INFO_NODE, infoNodeAsString);
  }

  @Test
  public void testCreateComplexNestedInfoNode() {
    /*
      <TestParentElement>
        TestParentElementValue
        <Test0Element Test0AttributeTwo="Test0AttributeTwoValue" Test0AttributeOne="Test0AttributeOneValue">Test0ElementValue</Test0Element>
        <Test1Element Test1AttributeTwo="Test1AttributeTwoValue" Test1AttributeOne="Test1AttributeOneValue">Test1ElementValue</Test1Element>
        <Test2Element Test2AttributeTwo="Test2AttributeTwoValue" Test2AttributeOne="Test2AttributeOneValue">Test2ElementValue</Test2Element>
        <Test3Element Test3AttributeOne="Test3AttributeOneValue" Test3AttributeTwo="Test3AttributeTwoValue">Test3ElementValue</Test3Element>
        <Test4Element Test4AttributeTwo="Test4AttributeTwoValue" Test4AttributeOne="Test4AttributeOneValue">Test4ElementValue</Test4Element>
        <Test5Element Test5AttributeOne="Test5AttributeOneValue" Test5AttributeTwo="Test5AttributeTwoValue">Test5ElementValue</Test5Element>
        <Test6Element Test6AttributeTwo="Test6AttributeTwoValue" Test6AttributeOne="Test6AttributeOneValue">Test6ElementValue</Test6Element>
        <Test7Element Test7AttributeTwo="Test7AttributeTwoValue" Test7AttributeOne="Test7AttributeOneValue">Test7ElementValue</Test7Element>
        <Test8Element Test8AttributeTwo="Test8AttributeTwoValue" Test8AttributeOne="Test8AttributeOneValue">Test8ElementValue</Test8Element>
        <Test9Element Test9AttributeOne="Test9AttributeOneValue" Test9AttributeTwo="Test9AttributeTwoValue">Test9ElementValue</Test9Element>
      </TestParentElement>
     */
    InfoNodeElement infoNode = SampleInfoNodeBuilderUtils.createComplexNestedInfoNodeWithParentValue(RootNodeOption.GENERATE_ROOT_NODE, 10);
    Iterator<? extends InfoNodeElement> iter = infoNode.getChildNodeIterator();
    short i = 0;
    while (iter.hasNext()) {
      InfoNodeElement childNode = iter.next();
      SampleNodeBuilderUtils.verifyNamedInfoNodeData(childNode, "Test" + i++);
    }
  }

  @Test
  public void testNestedNodeNoParentValue() {
    InfoNodeElement complexNode = SampleInfoNodeBuilderUtils.createComplexNestedInfoNodeWithoutParentValue(RootNodeOption.GENERATE_ROOT_NODE, 10);

    // verify that parent to child transition is smooth (i.e., no null printed in result)
    String partialResultStr = StringUtils.substringBetween(complexNode.toString(), "<TestParentElement", "<Test0Element");
    assertTrue(partialResultStr.trim().equals(">"));
  }

  @Test
  public void testToStringFormatted() {
    InfoNodeElement complexNode = SampleInfoNodeBuilderUtils.createFormattableInfoNode(RootNodeOption.GENERATE_ROOT_NODE);

    logger.debug(EnvironmentHelper.NEWLINE + complexNode.toStringFormatted());
  }

  /** test format of node with empty value */
  @Test
  public void testToStringFormatted3() {
    InfoNodeElement resultNode = ExpressInfoNodeBuilder.WITH_ROOT_NODE.buildInfoNode(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, null, null);
    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SampleNodeBuilderUtils.TEST_ATTRIBUTE_ONE_KEY, SampleNodeBuilderUtils.TEST_ATTRIBUTE_ONE_VALUE);
    resultNode.addChildNode(ExpressInfoNodeBuilder.DEFAULT.buildInfoNode("child1", null, attributes));

    logger.debug(EnvironmentHelper.NEWLINE + resultNode.toStringFormatted());
  }

  @Test
  public void testAddChildNodes() {
    InfoNodeElement infoNode = ExpressInfoNodeBuilder.WITH_ROOT_NODE.buildInfoNode(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, null, null);

    for (InfoNodeElement child : SampleInfoNodeBuilderUtils.createListOfChildInfoNode(3)) {
      infoNode.addChildNode(child);
    }

    Iterator<? extends InfoNodeElement> iter = infoNode.getChildNodeIterator();
    for (short i = 1; iter.hasNext(); i++) {
      InfoNodeElement childNode = iter.next();
      String childElementName = childNode.getName();
      String childElementValue = childNode.getText();

      assertTrue(childElementName.equals("Test" + i + "Element"));
      assertTrue(childElementValue.equals("Test" + i + "ElementValue"));
    }
  }

  @Test
  public void testRemoveChildNodes() {
    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(SampleNodeBuilderUtils.TEST_ATTRIBUTE_ONE_KEY, SampleNodeBuilderUtils.TEST_ATTRIBUTE_ONE_VALUE);
    InfoNodeElement rootNode = ExpressInfoNodeBuilder.WITH_ROOT_NODE.buildInfoNode(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, null, null);
    InfoNodeElement topicNode = ExpressInfoNodeBuilder.DEFAULT.buildInfoNode("dataList", null, attributes);
    InfoNodeElement faqtoidNode = ExpressInfoNodeBuilder.DEFAULT.buildInfoNode("faqtoid", null, null);

    for (InfoNodeElement child : SampleInfoNodeBuilderUtils.createListOfChildInfoNode(3)) {
      faqtoidNode.addChildNode(child);
    }

    topicNode.addChildNode(faqtoidNode);
    rootNode.addChildNode(topicNode);

    topicNode.removeContent();

    // all child nodes should be removed.
    assertFalse(topicNode.getChildNodeIterator().hasNext());
  }

  @Test
  public void testFindFullXPath() throws JDOMException {
    InfoNodeElement infoNode = SampleInfoNodeBuilderUtils.createDeepNestedInfoNode(RootNodeOption.GENERATE_ROOT_NODE);
    InfoNodeElement resultNode = (InfoNodeElement)infoNode.find("//TestTwoElement[@TestTwoAttributeOne='attrtwo2']");

    assertNotNull(resultNode);
  }

  @Test
  public void testFindRelativeXPath() throws JDOMException {
    /*
      <TestParentElement>
        <ChildElement1>
          <TestOneElement TestOneAttributeOne="attrone1">
            valueone1
            <TestTwoElement TestTwoAttributeOne="attrtwo1">valuetwo1</TestTwoElement>
          </TestOneElement>
          <TestOneElement TestOneAttributeOne="attrone2">
            valueone2
            <TestTwoElement TestTwoAttributeOne="attrtwo2">valuetwo2</TestTwoElement>
          </TestOneElement>
          <TestOneElement TestOneAttributeOne="attrone3">
            valueone3
            <TestTwoElement TestTwoAttributeOne="attrtwo3">valuetwo3</TestTwoElement>
          </TestOneElement>
        </ChildElement1>
      </TestParentElement>
     */
    InfoNodeElement infoNode = SampleInfoNodeBuilderUtils.createDeepNestedInfoNode(RootNodeOption.NO_ROOT_NODE);
    InfoNodeElement resultNode = (InfoNodeElement)infoNode.find("ChildElement1/TestOneElement[@TestOneAttributeOne='attrone2']");

    assertNotNull(resultNode);
  }

  @Test
  public void testGetFirstSimpleResultFullXPath() throws JDOMException {
    InfoNodeElement nestedNode = SampleInfoNodeBuilderUtils.createSimpleNestedInfoNode(RootNodeOption.GENERATE_ROOT_NODE);
    InfoNodeElement simpleNode = SampleInfoNodeBuilderUtils.createSimpleChildOneInfoNode(RootNodeOption.NO_ROOT_NODE);
    InfoNodeElement foundNode = (InfoNodeElement)nestedNode.find("//" + SampleNodeBuilderUtils.TEST_ELEMENT_ONE_NAME);

    assertNotNull(foundNode);
    assertTrue(simpleNode.equivalent(foundNode));
  }

  @Test
  public void testFindMiddleResultFullXPath() throws JDOMException {
    InfoNodeElement complexNode = SampleInfoNodeBuilderUtils.createComplexNestedInfoNodeWithParentValue(RootNodeOption.GENERATE_ROOT_NODE, 10);
    InfoNodeElement simpleNode = SampleNodeBuilderUtils.createNamedInfoNode("Test5", 2, RootNodeOption.NO_ROOT_NODE);
    InfoNodeElement foundNode = (InfoNodeElement)complexNode.find("//Test5Element");

    assertNotNull(foundNode);
    assertTrue(simpleNode.equivalent(foundNode));
  }

  @Test
  public void testExample1() {
    // create parent (rootNode)
    InfoNodeElement rootNode = ExpressInfoNodeBuilder.WITH_ROOT_NODE.buildInfoNode("parent", null, null);

    // create the data-list, with one attribute and two faq entries
    InfoNodeElement dataListNode = ExpressInfoNodeBuilder.DEFAULT.buildInfoNode("dataList", null, StringUtilsExt.tokensToMap("key1=value1"));
    dataListNode.addChildNode(ExpressInfoNodeBuilder.DEFAULT.buildInfoNode("faq", "one", null));
    dataListNode.addChildNode(ExpressInfoNodeBuilder.DEFAULT.buildInfoNode("faq", "two", null));

    // add data-list to root node.
    rootNode.addChildNode(dataListNode);
  }
}
