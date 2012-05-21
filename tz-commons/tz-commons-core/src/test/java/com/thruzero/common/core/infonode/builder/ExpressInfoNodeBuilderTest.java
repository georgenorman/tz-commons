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

import java.util.Map;

import org.junit.Test;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.utils.SampleNodeBuilderUtils;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Unit test for ExpressInfoNodeBuilder.
 *
 * @author George Norman
 */
public class ExpressInfoNodeBuilderTest extends AbstractCoreTestCase {

  @Test
  public void testCreateSimpleInfoNode() {
    // <TestParentElement TestAttributeOne="TestAttributeOneValue" TestAttributeTwo="TestAttributeTwoValue">TestParentElementValue</TestParentElement>
    Map<String, String> attributes = SampleNodeBuilderUtils.createSimpleAttributeMap();
    InfoNodeElement infoNode = ExpressInfoNodeBuilder.DEFAULT.buildInfoNode(SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_NAME, SampleNodeBuilderUtils.TEST_PARENT_ELEMENT_VALUE, attributes);

    SampleNodeBuilderUtils.verifySimpleInfoNodeData(infoNode);
    SampleNodeBuilderUtils.verifySimpleInfoNodeAsString(infoNode);
  }

}
