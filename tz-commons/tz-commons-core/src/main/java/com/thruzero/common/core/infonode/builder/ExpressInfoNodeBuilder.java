/*
 *   Copyright 2006-2011 George Norman
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
import java.util.Map.Entry;

import com.thruzero.common.core.infonode.InfoNodeElement;

/**
 * Manually build a {@code InfoNodeElement} directly from the element name, value and attributes. The following example
 * shows how to build a {@code InfoNodeElement} that represents a list of FAQs:
 *
 * <pre>
 * <code>
 *    // create parent (rootNode)
 *    InfoNodeElement rootNode = ExpressInfoNodeBuilder.WITH_ROOT_NODE.buildInfoNode("parent", null, null);
 *
 *    // create the data-list, with one attribute and two faq entries
 *    ExpressInfoNodeBuilder builder = ExpressInfoNodeBuilder.DEFAULT;
 *    InfoNodeElement dataListNode = builder.buildInfoNode("dataList", null, StringUtilsExt.tokensToMap("key1=value1"));
 *    dataListNode.addChildNode(builder.buildInfoNode("faq", "one", null));
 *    dataListNode.addChildNode(builder.buildInfoNode("faq", "two", null));
 *
 *    // add data-list to root node.
 *    rootNode.addChildNode(dataListNode);
 * </code>
 * </pre>
 *
 * Generates the following:
 *
 * <pre>
 * {@code
 * <parent>
 *   <dataList key1="value1">
 *     <faq>one</faq>
 *     <faq>two</faq>
 *   </dataList>
 * </parent>
 * }
 * </pre>
 *
 * @author George Norman
 */
public final class ExpressInfoNodeBuilder extends AbstractInfoNodeBuilder {
  /** Basic builder; does not provide parent document (only relative xpath supported) and no primary key. */
  public static final ExpressInfoNodeBuilder DEFAULT = new ExpressInfoNodeBuilder(PrimaryKeyOption.NO_PRIMARY_KEY, RootNodeOption.NO_ROOT_NODE);

  /** Builder providing parent document (full xpath support) and no primary key. */
  public static final ExpressInfoNodeBuilder WITH_ROOT_NODE = new ExpressInfoNodeBuilder(PrimaryKeyOption.NO_PRIMARY_KEY, RootNodeOption.GENERATE_ROOT_NODE);

  /** Builder without parent document (only relative xpath supported) and provides a primary key. */
  public static final ExpressInfoNodeBuilder WITH_PRIMARY_KEY = new ExpressInfoNodeBuilder(PrimaryKeyOption.GENERATE_PRIMARY_KEY, RootNodeOption.NO_ROOT_NODE);

  /** Builder providing parent document (full xpath support) and a primary key. */
  public static final ExpressInfoNodeBuilder WITH_PRIMARY_KEY_AND_ROOT_NODE = new ExpressInfoNodeBuilder(PrimaryKeyOption.GENERATE_PRIMARY_KEY, RootNodeOption.GENERATE_ROOT_NODE);

  /**
   * Instead of creating a new builder, use one of the pre-configured builders (e.g., DEFAULT)
   */
  private ExpressInfoNodeBuilder(final PrimaryKeyOption primaryKeyOption, final RootNodeOption rootNodeOption) {
    super(primaryKeyOption, rootNodeOption);
  }

  /**
   * Construct parent or child {@code InfoNodeElement}.
   */
  public InfoNodeElement buildInfoNode(final String elementName, final String elementValue, final Map<String, String> attributes) {
    return initInfoNode(new InfoNodeElement(), elementName, elementValue, attributes);
  }

  /**
   * Set element name, value and attributes on the given {@code InfoNodeElement}.
   */
  public InfoNodeElement initInfoNode(final InfoNodeElement infoNode, final String elementName, final String elementValue, final Map<String, String> attributes) {
    infoNode.setName(elementName);
    infoNode.setText(elementValue);
    handlePrimaryKey(infoNode);

    if (attributes != null) {
      for (Entry<String, String> entry : attributes.entrySet()) {
        infoNode.setAttribute(entry.getKey(), entry.getValue());
      }
    }
    handleRootNode(infoNode);

    return infoNode;
  }

}
