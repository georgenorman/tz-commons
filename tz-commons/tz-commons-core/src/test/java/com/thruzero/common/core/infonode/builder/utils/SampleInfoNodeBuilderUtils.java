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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.InfoNodeElement.AttributeKeys;
import com.thruzero.common.core.infonode.builder.ExpressInfoNodeBuilder;
import com.thruzero.common.core.infonode.builder.TokenStreamInfoNodeBuilder;

/**
 * Utils for testing InfoNode builders.
 *
 * @author George Norman
 */
public class SampleInfoNodeBuilderUtils extends SampleNodeBuilderUtils {

  /**
   * <pre>{@code
   * <TestParentElement>TestParentElementValue</TestParentElement>
   * }</pre>
   */
  public static InfoNodeElement createSimpleInfoNodeNoAttributes(final boolean enableRootNode) {
    InfoNodeElement result = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(TEST_PARENT_ELEMENT_NAME, TEST_PARENT_ELEMENT_VALUE, null);

    return result;
  }

  /**
   * <pre>{@code
   * <TestParentElement TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestParentElementValue</TestParentElement>
   * }</pre>
   */
  public static InfoNodeElement createSimpleInfoNodeWithAttributes() {
    Map<String, String> attributes = createSimpleAttributeMap();
    InfoNodeElement result = ExpressInfoNodeBuilder.DEFAULT.buildInfoNode(TEST_PARENT_ELEMENT_NAME, TEST_PARENT_ELEMENT_VALUE, attributes);

    return result;
  }

  /**
   * <pre>{@code
   * <TestParentElement primaryKey="primaryValueOne" TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestParentElementValue</TestParentElement>
   * }</pre>
   */
  public static InfoNodeElement createPrimaryKeyInfoNode() {
    InfoNodeElement result = TokenStreamInfoNodeBuilder.DEFAULT.buildInfoNode(
        TEST_PARENT_ELEMENT_NAME + "[@" + AttributeKeys.PRIMARY_KEY_ATTR_KEY + "='" + PRIMARY_VALUE_ONE + "',@" + TEST_ATTRIBUTE_ONE_KEY + "='" + TEST_ATTRIBUTE_ONE_VALUE + "',@"
            + TEST_ATTRIBUTE_TWO_KEY + "='" + TEST_ATTRIBUTE_TWO_VALUE + "']=" + TEST_PARENT_ELEMENT_VALUE);

    return result;
  }

  /**
   * <pre>{@code
   * <TestParentElement>
   *   TestParentElementValue
   *   <TestElementOne TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementOneValue</TestElementOne>
   *   <TestElementTwo TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementTwoValue</TestElementTwo>
   *  </TestParentElement>
   * }</pre>
   */
  public static InfoNodeElement createSimpleNestedInfoNode(final boolean enableRootNode) {
    InfoNodeElement infoNode = createSimpleInfoNodeNoAttributes(enableRootNode);
    infoNode.addChildNode(createSimpleChildOneInfoNode(false));
    infoNode.addChildNode(createSimpleChildTwoInfoNode(false));

    return infoNode;
  }

  /**
   * <pre>{@code
   * <TestElementOne TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementOneValue</TestElementOne>
   * }</pre>
   */
  public static InfoNodeElement createSimpleChildOneInfoNode(final boolean enableRootNode) {
    Map<String, String> attributes = createSimpleAttributeMap();
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(TEST_ELEMENT_ONE_NAME, TEST_ELEMENT_ONE_VALUE, attributes);

    return infoNode;
  }

  /**
   * <pre>{@code
   * <TestElementTwo TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestElementTwoValue</TestElementTwo>
   * }</pre>
   */
  public static InfoNodeElement createSimpleChildTwoInfoNode(final boolean enableRootNode) {
    Map<String, String> attributes = createSimpleAttributeMap();
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(TEST_ELEMENT_TWO_NAME, TEST_ELEMENT_TWO_VALUE, attributes);

    return infoNode;
  }

  /**
   * <pre>{@code
   *   <TestParentElement>
   *     TestParentElementValue
   *     <Test0Element Test0AttributeOne="Test0AttributeOneValue" Test0AttributeTwo="Test0AttributeTwoValue">Test0ElementValue</Test0Element>
   *     <Test1Element Test1AttributeOne="Test1AttributeOneValue" Test1AttributeTwo="Test1AttributeTwoValue">Test1ElementValue</Test1Element>
   *     <Test2Element Test2AttributeOne="Test2AttributeOneValue" Test2AttributeTwo="Test2AttributeTwoValue">Test2ElementValue</Test2Element>
   *     ...
   *     <Test9Element Test9AttributeOne="Test9AttributeOneValue" Test9AttributeTwo="Test9AttributeTwoValue">Test9ElementValue</Test9Element>
   *   </TestParentElement>
   * }</pre>
   */
  public static InfoNodeElement createComplexNestedInfoNodeWithParentValue(final boolean enableRootNode) {
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(TEST_PARENT_ELEMENT_NAME, TEST_PARENT_ELEMENT_VALUE, null);
    for (short i = 0; i <= 9; i++) {
      infoNode.addChildNode(createNamedInfoNode("Test" + i, 2, false));
    }

    return infoNode;
  }

  /**
   * <pre>{@code
   * <TestParentElement>
   *   <Test0Element Test0AttributeOne="Test0AttributeOneValue" Test0AttributeTwo="Test0AttributeTwoValue">Test0ElementValue</Test0Element>
   *   <Test1Element Test1AttributeOne="Test1AttributeOneValue" Test1AttributeTwo="Test1AttributeTwoValue">Test1ElementValue</Test1Element>
   *   <Test2Element Test2AttributeTwo="Test2AttributeTwoValue" Test2AttributeOne="Test2AttributeOneValue">Test2ElementValue</Test2Element>
   *   ...
   *   <Test9Element Test9AttributeTwo="Test9AttributeTwoValue" Test9AttributeOne="Test9AttributeOneValue">Test9ElementValue</Test9Element>
   * </TestParentElement>
   * }</pre>
   */
  public static InfoNodeElement createComplexNestedInfoNodeWithoutParentValue(final boolean enableRootNode) {
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(TEST_PARENT_ELEMENT_NAME, null, null);
    for (short i = 0; i <= 9; i++) {
      infoNode.addChildNode(createNamedInfoNode("Test" + i, 2, false));
    }

    return infoNode;
  }

  /**
   * <pre>{@code
   * <TestParentElement>
   *  <ChildElement>
   *    <Test1Element>Test1ElementValue</Test1Element>
   *    <Test2Element>Test2ElementValue</Test2Element>
   *    <Test3Element>Test3ElementValue</Test3Element>
   *  </ChildElement>
   * </TestParentElement>
   * }</pre>
   */
  public static InfoNodeElement createFormattableInfoNode(final boolean enableRootNode) {
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(TEST_PARENT_ELEMENT_NAME, null, null);
    InfoNodeElement childNode = ExpressInfoNodeBuilder.DEFAULT.buildInfoNode("ChildElement", null, null);
    infoNode.addChildNode(childNode);

    for (short i = 1; i <= 3; i++) {
      childNode.addChildNode(createNamedInfoNode("Test" + i, 0, false));
    }

    return infoNode;
  }

  /**
   * <pre>{@code
   *  <TestParentElement>
   *    <ChildElement1>
   *      <TestOneElement TestOneAttributeOne="attrone1">
   *        valueone1
   *        <TestTwoElement TestTwoAttributeOne="attrtwo1">valuetwo1</TestTwoElement>
   *      </TestOneElement>
   *      <TestOneElement TestOneAttributeOne="attrone2">
   *        valueone2
   *        <TestTwoElement TestTwoAttributeOne="attrtwo2">valuetwo2</TestTwoElement>
   *      </TestOneElement>
   *      <TestOneElement TestOneAttributeOne="attrone3">
   *        valueone3
   *        <TestTwoElement TestTwoAttributeOne="attrtwo3">valuetwo3</TestTwoElement>
   *      </TestOneElement>
   *    </ChildElement1>
   *  </TestParentElement>
   * }</pre>
   */
  public static InfoNodeElement createDeepNestedInfoNode(final boolean enableRootNode) {
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(TEST_PARENT_ELEMENT_NAME, null, null);
    InfoNodeElement childNode = ExpressInfoNodeBuilder.DEFAULT.buildInfoNode("ChildElement1", null, null);
    infoNode.addChildNode(childNode);

    for (short i = 1; i <= 3; i++) {
      InfoNodeElement childNode2 = createNamedInfoNode("TestOne", "one" + i, false);
      childNode2.addChildNode(createNamedInfoNode("TestTwo", "two" + i, false));
      childNode.addChildNode(childNode2);
    }

    return infoNode;
  }

  /**
   * <pre>{@code
   * [<Test1Element>Test1ElementValue</Test1Element>, <Test2Element>Test2ElementValue</Test2Element>, <Test3Element>Test3ElementValue</Test3Element>]
   * }</pre>
   */
  public static List<InfoNodeElement> createListOfChildInfoNode(final int numChildren) {
    List<InfoNodeElement> result = new ArrayList<InfoNodeElement>();

    for (short i = 1; i <= numChildren; i++) {
      result.add(createNamedInfoNode("Test" + i, 0, false));
    }

    return result;
  }

}
