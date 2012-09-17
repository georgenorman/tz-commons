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

import com.thruzero.common.core.config.Config;
import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;

/**
 * Builds a {@code InfoNodeElement} from a config file, where the keys are used as element names and the values are used
 * as element values.
 * <p>
 * An example using {@code JFigConfig}:
 *
 * <pre>
 * {@code
 * <section name="test">
 *   <entry key="foo1" value="FOO-ONE" />
 *   <entry key="foo2" value="FOO-TWO" />
 * </section>
 * }
 * </pre>
 *
 * Results in an InfoNodeElement representing (and capable of generating) the following XML:
 *
 * <pre>
 * {@code
 * <test>
 *   <foo1>FOO-ONE</foo1>
 *   <foo2>FOO-TWO</foo2>
 * </test>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class ConfigInfoNodeBuilder extends AbstractInfoNodeBuilder {
  private final Config config = ConfigLocator.locate();

  /** Basic builder; does not provide parent document (only relative xpath supported) and no primary key. */
  public static final ConfigInfoNodeBuilder DEFAULT = new ConfigInfoNodeBuilder(PrimaryKeyOption.NO_PRIMARY_KEY, RootNodeOption.NO_ROOT_NODE);

  /** Builder providing parent document (full xpath support) and no primary key. */
  public static final ConfigInfoNodeBuilder WITH_ROOT_NODE = new ConfigInfoNodeBuilder(PrimaryKeyOption.NO_PRIMARY_KEY, RootNodeOption.GENERATE_ROOT_NODE);

  /** Builder without parent document (only relative xpath supported) and provides a primary key. */
  public static final ConfigInfoNodeBuilder WITH_PRIMARY_KEY = new ConfigInfoNodeBuilder(PrimaryKeyOption.GENERATE_PRIMARY_KEY, RootNodeOption.NO_ROOT_NODE);

  /** Builder providing parent document (full xpath support) and a primary key. */
  public static final ConfigInfoNodeBuilder WITH_PRIMARY_KEY_AND_ROOT_NODE = new ConfigInfoNodeBuilder(PrimaryKeyOption.GENERATE_PRIMARY_KEY, RootNodeOption.GENERATE_ROOT_NODE);

  /**
   * Use one of the pre-configured builders (e.g., DEFAULT)
   */
  private ConfigInfoNodeBuilder(final PrimaryKeyOption primaryKeyOption, final RootNodeOption rootNodeOption) {
    super(primaryKeyOption, rootNodeOption);
  }

  /**
   * Construct an {@code InfoNodeElement} from the given {@link com.thruzero.common.core.config.Config Config}
   * {@code sectionName}, where {@code sectionName} is the parent element name and all key/value pairs in the section
   * are child nodes.
   */
  public InfoNodeElement buildInfoNode(final String sectionName) {
    return buildInfoNode(sectionName, sectionName, null);
  }

  /**
   * Construct an {@code InfoNodeElement} from the given {@link com.thruzero.common.core.config.Config Config}
   * {@code sectionName}, using {@code parentElementName} as the parent element name.
   */
  public InfoNodeElement buildInfoNode(final String sectionName, final String parentElementName) {
    return buildInfoNode(sectionName, parentElementName, null);
  }

  /**
   * Construct an {@code InfoNodeElement} from the given {@link com.thruzero.common.core.config.Config Config}
   * {@code sectionName}, using {@code parentElementName} as the parent element name and the given {@code attributes}.
   */
  public InfoNodeElement buildInfoNode(final String sectionName, final String parentElementName, final Map<String, String> attributes) {
    InfoNodeElement result = new InfoNodeElement();

    result.setName(parentElementName);
    handlePrimaryKey(result);

    // select the proper child builder
    ExpressInfoNodeBuilder childBuilder = ExpressInfoNodeBuilder.DEFAULT;
    if (isPrimaryKeyGenerationEnabled()) {
      childBuilder = ExpressInfoNodeBuilder.WITH_PRIMARY_KEY;
    }

    // add the children
    Map<String, String> section = config.getSection(sectionName);
    for (Entry<String, String> entrySet : section.entrySet()) {
      result.addChildNode(childBuilder.buildInfoNode(entrySet.getKey(), entrySet.getValue(), attributes));
    }
    handleRootNode(result);

    return result;
  }

}
