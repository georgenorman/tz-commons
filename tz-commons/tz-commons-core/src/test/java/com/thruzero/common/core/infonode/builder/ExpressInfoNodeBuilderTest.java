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

import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.AbstractInfoNodeBuilder.RootNodeOption;
import com.thruzero.common.core.infonode.builder.utils.SampleInfoNodeBuilderUtils;
import com.thruzero.common.core.infonode.builder.utils.SampleNodeBuilderUtils;
import com.thruzero.test.support.AbstractCoreTestCase;
import com.thruzero.test.support.AbstractLinearGrowthPerformanceTestHelper;

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

  /** Doubling the number of children should approximately double the time to build the InfoNode. */
  @Test
  public void testCreateComplexNestedInfoNodePerformance() throws Exception {
    AbstractLinearGrowthPerformanceTestHelper performanceHelper = new AbstractLinearGrowthPerformanceTestHelper() {
      private InfoNodeElement infoNode;
      private int currentSize;

      @Override
      protected void doSetup(int currentSize) {
        this.currentSize = currentSize;
      }

      @Override
      protected void doExecute() {
        // assumes that createComplexNestedInfoNodeWithParentValue uses ExpressInfoNodeBuilder
        // build an InfoNode of the current size.
        infoNode = SampleInfoNodeBuilderUtils.createComplexNestedInfoNodeWithParentValue(RootNodeOption.GENERATE_ROOT_NODE, currentSize);
      }

      @Override
      protected void doDataValidation(int size) {
        assertTrue(infoNode.getChildren().size() == size);
      }
    };

    performanceHelper.execute(10, 8, 0.1f);
  }

  /** Doubling the number of children should approximately double the time to convert the InfoNode to an unformatted string. */
  @Test
  public void testToStringUnformattedComplexNestedInfoNodePerformance() throws Exception {
    AbstractLinearGrowthPerformanceTestHelper performanceHelper = new AbstractLinearGrowthPerformanceTestHelper() {
      private InfoNodeElement infoNode;

      @Override
      protected void doSetup(int size) {
        // assumes that createComplexNestedInfoNodeWithParentValue uses ExpressInfoNodeBuilder
        infoNode = SampleInfoNodeBuilderUtils.createComplexNestedInfoNodeWithParentValue(RootNodeOption.GENERATE_ROOT_NODE, size);
      }

      @Override
      protected void doExecute() {
        infoNode.toStringUnformatted();
      }

      @Override
      protected void doDataValidation(int size) {
        assertTrue(infoNode.getChildren().size() == size);
      }
    };

    performanceHelper.execute(10, 8, 0.1f);
  }

  /** Doubling the number of children should approximately double the time to convert the InfoNode to a formatted string. */
  @Test
  public void testToStringFormattedComplexNestedInfoNodePerformance() throws Exception {
    AbstractLinearGrowthPerformanceTestHelper performanceHelper = new AbstractLinearGrowthPerformanceTestHelper() {
      private InfoNodeElement infoNode;

      @Override
      protected void doSetup(int size) {
        // assumes that createComplexNestedInfoNodeWithParentValue uses ExpressInfoNodeBuilder
        infoNode = SampleInfoNodeBuilderUtils.createComplexNestedInfoNodeWithParentValue(RootNodeOption.GENERATE_ROOT_NODE, size);
      }

      @Override
      protected void doExecute() {
        infoNode.toStringFormatted();
      }

      @Override
      protected void doDataValidation(int size) {
        assertTrue(infoNode.getChildren().size() == size);
      }
    };

    performanceHelper.execute(10, 8, 0.1f);
  }

}
