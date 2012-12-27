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
import com.thruzero.common.core.infonode.builder.filter.InfoNodeFilterChain;
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
  private final PrimaryKeyOption primaryKeyOption;
  private RootNodeOption rootNodeOption;

  public enum PrimaryKeyOption {GENERATE_PRIMARY_KEY, NO_PRIMARY_KEY};
  public enum RootNodeOption {GENERATE_ROOT_NODE, NO_ROOT_NODE};

  /**
   * The constructor called by subclasses.
   *
   * @param primaryKeyGenerationEnabled if true, will generate primary keys for each node created by this builder.
   * @param rootNodeGenerationEnabled if true, will create a {@link com.thruzero.common.core.infonode.InfoNodeDocument
   * InfoNodeDocument} and set it as the parent of the root node created by this builder. This is required in order for
   * xpath to work.
   */
  protected AbstractInfoNodeBuilder(final PrimaryKeyOption primaryKeyOption, final RootNodeOption rootNodeOption) {
    this.primaryKeyOption = primaryKeyOption;
    this.rootNodeOption = rootNodeOption;
  }

  /**
   * Return true if this builder creates a {@link com.thruzero.common.core.infonode.InfoNodeDocument InfoNodeDocument}
   * and sets it as the parent of the root node created by this builder.
   */
  protected boolean isRootNodeGenerationEnabled() {
    return rootNodeOption == RootNodeOption.GENERATE_ROOT_NODE;
  }

  /**
   * If {@code rootNodeOption} equals GENERATE_ROOT_NODE, then this builder will create a
   * {@link com.thruzero.common.core.infonode.InfoNodeDocument InfoNodeDocument} and set it as the parent of the root
   * node created by this builder. This is required in order for xpath to work.
   */
  protected void setRootNodeOption(final RootNodeOption rootNodeOption) {
    this.rootNodeOption = rootNodeOption;
  }

  /**
   * Return true if primary keys will be auto-generated for each {@code InfoNodeElement} built.
   */
  protected boolean isPrimaryKeyGenerationEnabled() {
    return primaryKeyOption == PrimaryKeyOption.GENERATE_PRIMARY_KEY;
  }

  /**
   * If primary key generation is enabled (see isPrimaryKeyGenerationEnabled), the given infoNode will be given a unique auto-generated primary-key.
   */
  protected void handlePrimaryKey(final InfoNodeElement infoNode) {
    if (isPrimaryKeyGenerationEnabled()) {
      infoNode.setEntityPath(new EntityPath(new ContainerPath(), SimpleIdGenerator.getInstance().getNextIdAsString()));
    }
  }

  /**
   * Use the given infoNodeFilterChain to apply a series of filters to the given infoNode and return the resulting node. If infoNodeFilterChain
   * is null, then return the given infoNode unchanged.
   */
  protected InfoNodeElement handleFilters(final InfoNodeElement infoNode, final InfoNodeFilterChain infoNodeFilterChain) {
    InfoNodeElement result;

    if (infoNodeFilterChain == null) {
      result = infoNode;
    } else {
      result = infoNodeFilterChain.applyFilter(infoNode);
    }

    return result;
  }

  /**
   * If {@code rootNodeGenerationEnabled} is true, then a {@link com.thruzero.common.core.infonode.InfoNodeDocument
   * InfoNodeDocument} will be created and set as the parent of the given {@code infoNode} (so that full xpath is
   * supported; otherwise, only relative paths are supported). This should only be called once for an InfoNodeElement
   * and only for the top-most element (the root).
   */
  protected void handleRootNode(final InfoNodeElement infoNode) {
    if (isRootNodeGenerationEnabled()) {
      infoNode.enableRootNode();
    }
  }

}
