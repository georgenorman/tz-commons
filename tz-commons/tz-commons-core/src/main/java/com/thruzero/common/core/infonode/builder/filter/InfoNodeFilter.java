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
 * An API used to modify InfoNodeElement instances as they are built. For example, a filter may remove
 * elements from the DOM, if the external user doesn't meet the constraints defined by the owner's AccessControl.
 * <p>
 * A simple example of an experimental filter is shown below (see AccessInfoNodeFilter).
 * 
 * <pre>
 * {@code
 * <db-info>
 *   <context>jcat3</context>
 *   <acl>
 *     <user id="private" actions="Create, Update, Delete, View">moe, larry, curly</user>
 *   </acl>
 * </db-info>
 * }
 * </pre>
 * 
 * The presence of the 'private' access ID in a data node will require that the logged
 * in user be present in the access control list (acl); otherwise, the data node won't be inserted into the DOM.
 * Furthermore, the privileges granted to the logged in user for the data node will be
 * controlled by the actions list.
 * 
 * <pre>
 * {@code
 * <faqtoid title="For ACL User Eyes Only" accessId="private">
 *   ...
 * </faqtoid>
 * }
 * </pre>
 * 
 * @author George Norman
 */
public interface InfoNodeFilter {

  InfoNodeElement applyFilter(InfoNodeElement infoNode, InfoNodeFilterChain chain);

  void init();

  void destroy();
}
