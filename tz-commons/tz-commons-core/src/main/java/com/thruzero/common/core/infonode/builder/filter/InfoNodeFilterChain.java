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

import com.thruzero.common.core.infonode.InfoNodeElement;

/**
 * An API that represents a series of InfoNodeElement filters. Each filter is given access to the chain, when their
 * applyFilter function is called. After the filter has completed its job, it may call applyFilter on the chain, to
 * to advance to the next filter, or it may just return immediately, preventing any further filtering to occur.
 * Any filter that returns null as the result, should cause the InfoNodeElement to be removed from the DOM.
 *
 * @author George Norman
 */
public interface InfoNodeFilterChain {

  /** Apply the next filter in the chain. */
  InfoNodeElement applyFilter(InfoNodeElement infoNode);

  /** Reset the filter back to the first node (useful for clients that want to reuse the chain for multiple calls). */
  void reset();

}
