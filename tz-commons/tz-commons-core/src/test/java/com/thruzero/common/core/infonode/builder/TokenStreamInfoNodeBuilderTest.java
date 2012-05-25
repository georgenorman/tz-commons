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

import org.jdom.JDOMException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.utils.SampleInfoNodeBuilderUtils;
import com.thruzero.common.core.infonode.builder.utils.SampleNodeBuilderUtils;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for TokenStreamInfoNodeBuilder.
 *
 * @author George Norman
 */
public class TokenStreamInfoNodeBuilderTest extends AbstractCoreTestCase {

  /**
   * <pre>
   * TestParentElement[@TestAttributeOne='TestAttributeOneValue', @TestAttributeTwo='TestAttributeTwoValue']=TestParentElementValue
   * </pre>
   */
  public static final String SIMPLE_TOKEN_STREAM = SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME + "[@" + SampleNodeBuilderUtils.TEST_ATTRIBUTE_ONE_KEY + "='"
      + SampleNodeBuilderUtils.TEST_ATTRIBUTE_ONE_VALUE + "', @" + SampleNodeBuilderUtils.TEST_ATTRIBUTE_TWO_KEY + "='" + SampleNodeBuilderUtils.TEST_ATTRIBUTE_TWO_VALUE + "']="
      + SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_VALUE;

  @Test
  public void testCreateSimpleInfoNode() throws IOException, SAXException {
    TokenStreamInfoNodeBuilder builder = TokenStreamInfoNodeBuilder.DEFAULT;

    // <TestParentElement TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestParentElementValue</TestParentElement>
    InfoNodeElement infoNode = builder.buildInfoNode(SIMPLE_TOKEN_STREAM);

    SampleNodeBuilderUtils.verifySimpleInfoNodeData(infoNode);
    SampleNodeBuilderUtils.verifySimpleInfoNodeAsString(infoNode);
  }

  @Test
  public void testPrimaryKeyInfoNode() {
    // <TestParentElement primaryKey="primaryValueOne" TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestParentElementValue</TestParentElement>
    InfoNodeElement infoNode = SampleInfoNodeBuilderUtils.createEntityPathInfoNode();
    assertEquals(SampleNodeBuilderUtils.ENTITY_PATH_VALUE_ONE, infoNode.getEntityPath().toString());
    SampleNodeBuilderUtils.verifySimpleInfoNodeData(infoNode);
  }

  @Test
  public void testCreateSimpleParentChild() throws IOException, SAXException {
    TokenStreamInfoNodeBuilder builder = TokenStreamInfoNodeBuilder.DEFAULT;

    String parentTokenStream = "anchor[@id='a1']";
    String childTokenStream = "label=LABEL|href=HREF";

    // <anchor id="a1">
    //   <label>LABEL</label>
    //   <href>HREF</href>
    // </anchor>
    InfoNodeElement infoNode = builder.buildInfoNode(parentTokenStream, childTokenStream);

    SampleNodeBuilderUtils.assertEqualNormalizedValues("anchor", infoNode.getName());
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(infoNode, "label", "LABEL");
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(infoNode, "href", "HREF");
  }

  @Test
  public void testCreateExample1() throws IOException, SAXException {
    TokenStreamInfoNodeBuilder builder = TokenStreamInfoNodeBuilder.DEFAULT;
    //  <contentDescription>
    //    <title>FAQ TITLE</title>
    //    <contentBanner>BANNER</contentBanner>
    //    <toc>
    //      <item primaryKey="topic" title="TOPIC TITLE">Note 123</item>
    //    </toc>
    //  </contentDescription>
    InfoNodeElement newFaqDescNode = builder.buildInfoNode("contentDescription");

    builder.buildInfoNode(newFaqDescNode, "title=FAQ TITLE|contentBanner=BANNER");
    newFaqDescNode.addChildNode(builder.buildInfoNode("toc", "item[@primaryKey='topic', @title='TOPIC TITLE']=Note 123"));

    SampleNodeBuilderUtils.assertEqualNormalizedValues("contentDescription", newFaqDescNode.getName());
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(newFaqDescNode, "title", "FAQ TITLE");
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(newFaqDescNode, "contentBanner", "BANNER");
    InfoNodeElement toc = (InfoNodeElement)newFaqDescNode.getChild("toc");
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(toc, "item", "Note 123");
    SampleNodeBuilderUtils.assertEqualNormalizedChildAttributeValues(toc, "item", "primaryKey", "topic");
    SampleNodeBuilderUtils.assertEqualNormalizedChildAttributeValues(toc, "item", "title", "TOPIC TITLE");
  }

  @Test
  public void testCreateComplexInfoNode() throws IOException, SAXException, JDOMException {
    TokenStreamInfoNodeBuilder builder = TokenStreamInfoNodeBuilder.DEFAULT;

    String parentTokenStream = "contact[@primaryKey='c1', @type='family']";
    String childTokenStream = "fname=George|lname=Norman|phone[@type='work']=555-1234|phone[@type='home']=555-5678|phone[@type='cell', @preferred='true']=555-9012|email[@type='home']=foo@barh.com|email[@type='work']=foo@barw.com|address[@type='work']=yy|address[@type='home']=zz";

    //  <contact primaryKey="c1" type="family">
    //    <fname>George</fname>
    //    <lname>Norman</lname>
    //    <phone type="work">555-1234</phone>
    //    <phone type="home">555-5678</phone>
    //    <phone type="cell" preferred="true">555-9012</phone>
    //    <email type="home">foo@barh.com</email>
    //    <email type="work">foo@barw.com</email>
    //    <address type="work">yy</address>
    //    <address type="home">zz</address>
    //  </contact>
    InfoNodeElement contact = builder.buildInfoNode(parentTokenStream, childTokenStream);

    SampleNodeBuilderUtils.assertEqualNormalizedValues("contact", contact.getName());
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(contact, "fname", "George");
    SampleNodeBuilderUtils.assertEqualNormalizedChildValues(contact, "lname", "Norman");
    SampleNodeBuilderUtils.assertEqualNormalizedValues(((InfoNodeElement)contact.find("phone[@type='work']")).getText(), "555-1234");
    SampleNodeBuilderUtils.assertEqualNormalizedValues(((InfoNodeElement)contact.find("phone[@type='home']")).getText(), "555-5678");
    SampleNodeBuilderUtils.assertEqualNormalizedValues(((InfoNodeElement)contact.find("phone[@type='cell']")).getText(), "555-9012");
  }

}
