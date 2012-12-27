/*
 *   Copyright 2012 George Norman
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

package com.thruzero.common.core.infonode.builder.filter;

import java.util.ArrayList;
import java.util.List;

import com.thruzero.common.core.infonode.InfoNodeElement;

/**
 * Simple implementation of InfoNodeFilterChain.
 *
 * @author George Norman
 */
public class SimpleInfoNodeFilterChain implements InfoNodeFilterChain {
  private final List<InfoNodeFilter> filterList;

  private int filterIndex;

  public SimpleInfoNodeFilterChain(List<InfoNodeFilter> filterList) {
    this.filterList = new ArrayList<InfoNodeFilter>(filterList); // shallow-copy the list
  }

  @Override
  public void reset() {
    filterIndex = 0;
  }

  @Override
  public InfoNodeElement applyFilter(InfoNodeElement infoNode) {
    InfoNodeElement result;

    if (filterIndex < filterList.size()) {
      // apply next filter
      result = filterList.get(filterIndex++).applyFilter(infoNode, this);
    } else {
      result = infoNode;
    }

    return result;
  }
}
