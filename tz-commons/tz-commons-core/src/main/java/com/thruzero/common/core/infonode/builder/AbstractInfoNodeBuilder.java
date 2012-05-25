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

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.SimpleIdGenerator;

/**
 * An abstract base class that can be used to build {@link com.thruzero.common.core.infonode.InfoNodeElement
 * InfoNodeElement} instances. {@code InfoNodeElement} builders are not required to extend this class.
 *
 * @author George Norman
 */
public abstract class AbstractInfoNodeBuilder {
  private final boolean primaryKeyGenerationEnabled;
  private boolean rootNodeGenerationEnabled;

  /**
   * The constructor called by subclasses.
   *
   * @param primaryKeyGenerationEnabled if true, will generate primary keys for each node created by this builder.
   * @param rootNodeGenerationEnabled if true, will create a {@link com.thruzero.common.core.infonode.InfoNodeDocument
   * InfoNodeDocument} and set it as the parent of the root node created by this builder. This is required in order for
   * xpath to work.
   */
  protected AbstractInfoNodeBuilder(final boolean primaryKeyGenerationEnabled, final boolean rootNodeGenerationEnabled) {
    this.primaryKeyGenerationEnabled = primaryKeyGenerationEnabled;
    this.rootNodeGenerationEnabled = rootNodeGenerationEnabled;
  }

  /**
   * Return true if this builder creates a {@link com.thruzero.common.core.infonode.InfoNodeDocument InfoNodeDocument}
   * and sets it as the parent of the root node created by this builder.
   */
  protected boolean isRootNodeGenerationEnabled() {
    return rootNodeGenerationEnabled;
  }

  /**
   * If {@code rootNodeGenerationEnabled} is true, then this builder will create a
   * {@link com.thruzero.common.core.infonode.InfoNodeDocument InfoNodeDocument} and set it as the parent of the root
   * node created by this builder. This is required in order for xpath to work.
   */
  protected void setRootNodeGenerationEnabled(final boolean rootNodeGenerationEnabled) {
    this.rootNodeGenerationEnabled = rootNodeGenerationEnabled;
  }

  /**
   * Return true if primary keys will be auto-generated for each {@code InfoNodeElement} built.
   */
  protected boolean isPrimaryKeyGenerationEnabled() {
    return primaryKeyGenerationEnabled;
  }

  /**
   * If primaryKeyGenerationEnabled is true, the given infoNode will be given an auto-generated primary-key.
   */
  protected void handlePrimaryKey(final InfoNodeElement infoNode) {
    if (primaryKeyGenerationEnabled) {
      infoNode.setEntityPath(new EntityPath(new ContainerPath(), SimpleIdGenerator.getInstance().getNextIdAsString()));
    }
  }

  /**
   * If {@code rootNodeGenerationEnabled} is true, then a {@link com.thruzero.common.core.infonode.InfoNodeDocument
   * InfoNodeDocument} will be created and set as the parent of the given {@code infoNode} (so that full xpath is
   * supported; otherwise, only relative paths are supported). This should only be called once for an InfoNodeElement
   * and only for the top-most element (the root).
   */
  protected void handleRootNode(final InfoNodeElement infoNode) {
    if (rootNodeGenerationEnabled) {
      infoNode.enableRootNode();
    }
  }

}
