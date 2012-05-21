///*
// *   Copyright 2011 George Norman
// *
// *   Licensed under the Apache License, Version 2.0 (the "License");
// *   you may not use this file except in compliance with the License.
// *   You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *   Unless required by applicable law or agreed to in writing, software
// *   distributed under the License is distributed on an "AS IS" BASIS,
// *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *   See the License for the specific language governing permissions and
// *   limitations under the License.
// */
//package com.thruzero.common.core.utils;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//
//import com.thruzero.common.core.infonode.builder.utils.SampleNodeBuilderUtils;
//import com.thruzero.common.core.support.KeyValuePair;
//import com.thruzero.test.support.AbstractCoreTestCase;
//
///**
// * Unit test for XmlWriterUtils.
// *
// * @author George Norman
// */
//public class XmlWriterUtilsTest extends AbstractCoreTestCase {
//
//  @Test
//  public void testGenerateStartElementNoAttributes() {
//    String result = XmlWriterUtils.generateStartElement(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME);
//
//    assertEquals("Generated Start Element should equal test value", SampleNodeBuilderUtils.RESULT_AS_STRING_PARENT_ELEMENT_START_TAG_NO_ATTRIBUTES, result);
//  }
//
//  @Test
//  public void testGenerateStartElementWithAttributeMap() {
//    Map<String, String> attributes = SampleNodeBuilderUtils.createSimpleAttributeMap();
//    String result = XmlWriterUtils.generateStartElement(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, attributes);
//
//    assertEquals("Generated Start Element should equal test value", SampleNodeBuilderUtils.RESULT_AS_STRING_PARENT_ELEMENT_START_TAG_WITH_ATTRIBUTES, result);
//  }
//
//  @Test
//  public void testGenerateStartElementWithAttributeList() {
//    List<KeyValuePair> attributes = SampleNodeBuilderUtils.createSimpleAttributeList();
//    String result = XmlWriterUtils.generateStartElement(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, attributes);
//
//    assertEquals("Generated Start Element should equal test value", SampleNodeBuilderUtils.RESULT_AS_STRING_PARENT_ELEMENT_START_TAG_WITH_ATTRIBUTES, result);
//  }
//
//  @Test
//  public void testGenerateEndElement() {
//    String result = XmlWriterUtils.generateEndElement(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME);
//
//    assertEquals("Generated End Element should equal test value", SampleNodeBuilderUtils.RESULT_AS_STRING_PARENT_ELEMENT_END_TAG, result);
//  }
//
//  @Test
//  public void testGenerateInlineElementNoAttributes() {
//    String result = XmlWriterUtils.generateInlineElement(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_VALUE, false);
//
//    assertEquals("Generated Inline Element should equal test value", SampleNodeBuilderUtils.RESULT_AS_STRING_SIMPLE_INLINE_ELEMENT_NO_ATTRIBUTES, result);
//  }
//
//  @Test
//  public void testGenerateInlineElementWithAttributes() {
//    Map<String, String> attributes = SampleNodeBuilderUtils.createSimpleAttributeMap();
//    String result = XmlWriterUtils.generateInlineElement(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_VALUE, attributes, false);
//
//    assertEquals("Generated Inline Element should equal test value", SampleNodeBuilderUtils.RESULT_AS_STRING_SIMPLE_INLINE_ELEMENT_WITH_ATTRIBUTES, result);
//  }
//
//  @Test
//  public void testGenerateNestedElement() {
//    String newDoc = XmlWriterUtils.generateNestedElements("person", "fname=George|lname=Norman|phone[@type='work']=555-1234|phone[@type='home']=555-5678|"
//        + "phone[@type='cell']=555-9012|email[@type='home']=foo@bar-home.com|"
//        + "email[@type='work']=foo@bar-work.com|address[@type='work']=1234 Yyyy Ave.|address[@type='home']=1234 Zzzz Dr.", "|");
//
//    assertEquals("Generated Inline Element should equal test value", SampleNodeBuilderUtils.RESULT_AS_STRING_NESTED_ELEMENTS, newDoc);
//  }
//
//}
