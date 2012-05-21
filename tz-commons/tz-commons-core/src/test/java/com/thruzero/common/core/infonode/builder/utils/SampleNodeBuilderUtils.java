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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.ExpressInfoNodeBuilder;
import com.thruzero.common.core.support.EnvironmentHelper;
import com.thruzero.common.core.support.KeyValuePair;

/**
 * Utils for testing InfoNode builders.
 *
 * @author George Norman
 */
public class SampleNodeBuilderUtils {
  public static final String TEST_XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
  public static final String TEST_SIMPLE_DOCUMENT_TYPE = "<!DOCTYPE TestParentElement [\n<!ELEMENT TestParentElement (Test0Element,Test1Element)>\n <!ELEMENT Test0Element (#PCDATA)>\n <!ELEMENT Test1Element (#PCDATA)>\n ]>";
  public static final String TEST_PARENT_ELEMENT_NAME = "TestParentElement";
  public static final String TEST_PARENT_ELEMENT_VALUE = "TestParentElementValue";
  public static final String TEST_ATTRIBUTE_ONE_KEY = "TestAttributeOne";
  public static final String TEST_ATTRIBUTE_ONE_VALUE = "TestAttributeOneValue";
  public static final String TEST_ATTRIBUTE_TWO_KEY = "TestAttributeTwo";
  public static final String TEST_ATTRIBUTE_TWO_VALUE = "TestAttributeTwoValue";
  public static final String TEST_ELEMENT_ONE_NAME = "TestElementOne";
  public static final String TEST_ELEMENT_ONE_VALUE = "TestElementOneValue";
  public static final String TEST_ELEMENT_TWO_NAME = "TestElementTwo";
  public static final String TEST_ELEMENT_TWO_VALUE = "TestElementTwoValue";
  public static final String PRIMARY_VALUE_ONE = "primaryValueOne";

  public static final String RESULT_AS_STRING_PARENT_ELEMENT_START_TAG_NO_ATTRIBUTES = "<"+TEST_PARENT_ELEMENT_NAME+">";

  public static final String RESULT_AS_STRING_PARENT_ELEMENT_START_TAG_WITH_ATTRIBUTES =
      "<"+TEST_PARENT_ELEMENT_NAME+" "+TEST_ATTRIBUTE_ONE_KEY+"=\""+TEST_ATTRIBUTE_ONE_VALUE+"\" "+
      TEST_ATTRIBUTE_TWO_KEY+"=\""+TEST_ATTRIBUTE_TWO_VALUE+"\">";

  public static final String RESULT_AS_STRING_PARENT_ELEMENT_END_TAG = "</"+TEST_PARENT_ELEMENT_NAME+">";

  public static final String RESULT_AS_STRING_SIMPLE_INLINE_ELEMENT_NO_ATTRIBUTES =
      RESULT_AS_STRING_PARENT_ELEMENT_START_TAG_NO_ATTRIBUTES+TEST_PARENT_ELEMENT_VALUE+RESULT_AS_STRING_PARENT_ELEMENT_END_TAG;

  public static final String RESULT_AS_STRING_SIMPLE_INLINE_ELEMENT_WITH_ATTRIBUTES =
      RESULT_AS_STRING_PARENT_ELEMENT_START_TAG_WITH_ATTRIBUTES+TEST_PARENT_ELEMENT_VALUE+RESULT_AS_STRING_PARENT_ELEMENT_END_TAG;

  public static final String RESULT_AS_STRING_SIMPLE_CHILD_ELEMENT_ONE_WITH_ATTRIBUTES =
      "<"+TEST_ELEMENT_ONE_NAME+" "+TEST_ATTRIBUTE_ONE_KEY+"=\""+TEST_ATTRIBUTE_ONE_VALUE+"\" "+
      TEST_ATTRIBUTE_TWO_KEY+"=\""+TEST_ATTRIBUTE_TWO_VALUE+"\">" + TEST_ELEMENT_ONE_VALUE + "</" + TEST_ELEMENT_ONE_NAME + ">";

  public static final String RESULT_AS_STRING_SIMPLE_CHILD_ELEMENT_TWO_WITH_ATTRIBUTES =
      "<"+TEST_ELEMENT_TWO_NAME+" "+TEST_ATTRIBUTE_ONE_KEY+"=\""+TEST_ATTRIBUTE_ONE_VALUE+"\" "+
      TEST_ATTRIBUTE_TWO_KEY+"=\""+TEST_ATTRIBUTE_TWO_VALUE+"\">" + TEST_ELEMENT_TWO_VALUE + "</" + TEST_ELEMENT_TWO_NAME + ">";

  public static final String RESULT_AS_STRING_NESTED_INFO_NODE =
      RESULT_AS_STRING_PARENT_ELEMENT_START_TAG_NO_ATTRIBUTES+TEST_PARENT_ELEMENT_VALUE + EnvironmentHelper.NEWLINE
      +"  "+RESULT_AS_STRING_SIMPLE_CHILD_ELEMENT_ONE_WITH_ATTRIBUTES+EnvironmentHelper.NEWLINE
      +"  "+RESULT_AS_STRING_SIMPLE_CHILD_ELEMENT_TWO_WITH_ATTRIBUTES+EnvironmentHelper.NEWLINE+
      RESULT_AS_STRING_PARENT_ELEMENT_END_TAG;

  public static final String RESULT_AS_STRING_NESTED_ELEMENTS =
      "<person>"+ EnvironmentHelper.NEWLINE
      +"  <fname>George</fname>"+ EnvironmentHelper.NEWLINE
      +"  <lname>Norman</lname>"+ EnvironmentHelper.NEWLINE
      +"  <phone type=\"work\">555-1234</phone>"+ EnvironmentHelper.NEWLINE
      +"  <phone type=\"home\">555-5678</phone>"+ EnvironmentHelper.NEWLINE
      +"  <phone type=\"cell\">555-9012</phone>"+ EnvironmentHelper.NEWLINE
      +"  <email type=\"home\">foo@bar-home.com</email>"+ EnvironmentHelper.NEWLINE
      +"  <email type=\"work\">foo@bar-work.com</email>"+ EnvironmentHelper.NEWLINE
      +"  <address type=\"work\">1234 Yyyy Ave.</address>"+ EnvironmentHelper.NEWLINE
      +"  <address type=\"home\">1234 Zzzz Dr.</address>"+ EnvironmentHelper.NEWLINE
      +"</person>";

  public static Map<String,String> createSimpleAttributeMap() {
    Map<String,String> attributes = new HashMap<String,String>();

    attributes.put(TEST_ATTRIBUTE_ONE_KEY,TEST_ATTRIBUTE_ONE_VALUE);
    attributes.put(TEST_ATTRIBUTE_TWO_KEY,TEST_ATTRIBUTE_TWO_VALUE);

    return attributes;
  }

  public static List<KeyValuePair> createSimpleAttributeList() {
    List<KeyValuePair> attributes = new ArrayList<KeyValuePair>();

    attributes.add(new KeyValuePair(TEST_ATTRIBUTE_ONE_KEY,TEST_ATTRIBUTE_ONE_VALUE));
    attributes.add(new KeyValuePair(TEST_ATTRIBUTE_TWO_KEY,TEST_ATTRIBUTE_TWO_VALUE));

    return attributes;
  }

  /**
   * <pre>{@code
   * <Test0Element Test0AttributeOne="Test0AttributeOneValue" Test0AttributeTwo="Test0AttributeTwoValue">Test0ElementValue</Test0Element>
   * }</pre>
   */
  public static InfoNodeElement createNamedInfoNode(final String name, final int numAttributes, final boolean enableRootNode) {
    Map<String, String> attributes = new HashMap<String, String>();
    if (numAttributes >= 1) {
      attributes.put(name + "AttributeOne", name + "AttributeOneValue");
    }

    if (numAttributes == 2) {
      attributes.put(name + "AttributeTwo", name + "AttributeTwoValue");
    }

    if (numAttributes > 2) {
      throw new IllegalArgumentException("Max num attributes is 2");
    }
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(name + "Element", name + "ElementValue", attributes);

    return infoNode;
  }

  /**
   * <pre>{@code
   * <Test1Element Test1AttributeOne="attrone1">valueone1</Test1Element>
   * }</pre>
   */
  public static InfoNodeElement createNamedInfoNode(final String name, final String value, final boolean enableRootNode) {
    Map<String, String> attributes = new HashMap<String, String>();
    attributes.put(name + "AttributeOne", "attr" + value);
    InfoNodeElement infoNode = getExpressInfoNodeBuilder(enableRootNode).buildInfoNode(name + "Element", "value" + value, attributes);

    return infoNode;
  }

  protected static ExpressInfoNodeBuilder getExpressInfoNodeBuilder(final boolean enableRootNode) {
    return enableRootNode ? ExpressInfoNodeBuilder.WITH_ROOT_NODE : ExpressInfoNodeBuilder.DEFAULT;
  }

  // Validation methods ///////////////////////////////////////////////////////////////////

  public static void verifySimpleInfoNodeData(final InfoNodeElement infoNode) {
    assertEqualNormalizedValues(SampleInfoNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, infoNode.getName());
    assertEqualNormalizedValues(SampleInfoNodeBuilderUtils.TEST_PARENT_ELEMENT_VALUE, infoNode.getText());
    assertEqualNormalizedValues(SampleInfoNodeBuilderUtils.TEST_ATTRIBUTE_ONE_VALUE, infoNode.getAttribute(SampleInfoNodeBuilderUtils.TEST_ATTRIBUTE_ONE_KEY).getValue());
    assertEqualNormalizedValues(SampleInfoNodeBuilderUtils.TEST_ATTRIBUTE_TWO_VALUE, infoNode.getAttribute(SampleInfoNodeBuilderUtils.TEST_ATTRIBUTE_TWO_KEY).getValue());
  }

  public static void verifyNamedInfoNodeData(final InfoNodeElement infoNode, final String name) {
    assertEqualNormalizedValues(name + "Element", infoNode.getName());
    assertEqualNormalizedValues(name + "ElementValue", infoNode.getText());
    assertEqualNormalizedValues(name + "AttributeOneValue", infoNode.getAttribute(name + "AttributeOne").getValue());
    assertEqualNormalizedValues(name + "AttributeTwoValue", infoNode.getAttribute(name + "AttributeTwo").getValue());
  }

  public static void verifySimpleInfoNodeAsString(final InfoNodeElement infoNode) {
    assertEqualNormalizedValues(SampleInfoNodeBuilderUtils.RESULT_AS_STRING_SIMPLE_INLINE_ELEMENT_WITH_ATTRIBUTES, infoNode.toString());
  }

  public static void assertEqualNormalizedValues(final String v1, final String v2) {
    String vn1 = normalize(v1);
    String vn2 = normalize(v2);

    assertEquals(vn1, vn2);
  }

  public static void assertEqualNormalizedChildValues(InfoNodeElement parentNode, String childElementName, String childElementValue) {
    InfoNodeElement child = (InfoNodeElement)parentNode.getChild(childElementName);

    SampleNodeBuilderUtils.assertEqualNormalizedValues(childElementValue, child.getText());
  }

  public static void assertEqualNormalizedChildAttributeValues(InfoNodeElement parentNode, String childElementName, String childAttributeName, String childAttributeValue) {
    InfoNodeElement child = (InfoNodeElement)parentNode.getChild(childElementName);

    SampleNodeBuilderUtils.assertEqualNormalizedValues(childAttributeValue, child.getAttributeValue(childAttributeName));
  }

  public static String normalize(String elements) {
    StringBuilder result = new StringBuilder();
    elements = StringUtils.replace(elements, "\r", "");
    elements = StringUtils.replace(elements, "\n", "");
    StringTokenizer st = new StringTokenizer(elements, " ");

    while (st.hasMoreTokens()) {
      String token = st.nextToken();

      result.append(StringUtils.trimToEmpty(token));
    }

    return result.toString();
  }

}
