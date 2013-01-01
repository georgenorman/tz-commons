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

package com.thruzero.auth.model.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jdom.Element;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.SaxInfoNodeBuilder;
import com.thruzero.domain.model.DataStoreInfo;

/**
 * Basic implementation of UserDataStoreInfo that constructs itself from a single XML field. Following is an example:
 *
 * <pre>
 * {@code
 * <db-info>
 *   <context>dilbert</context>
 *   <private-root-data-store-path>http://dl.dropbox.com/u/012345/DscTextEnvelopeDAO</private-root-data-store-path>
 *   <acl>
 *     <user id="public" actions="View">dilbert</user>
 *   </acl>
 * </db-info>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class BasicUserDataStoreInfo extends DataStoreInfo {
  private String rawDataStoreInfo;

  public BasicUserDataStoreInfo() {
  }

  public BasicUserDataStoreInfo(String rawDataStoreInfo) {
    this.rawDataStoreInfo = rawDataStoreInfo;
    initFromRawData();
  }

  public String getRawDataStoreInfo() {
    // TODO-p0(george) Reconstruct XML from model (so that any modifications will be persisted)
    return rawDataStoreInfo;
  }

  public void setRawDataStoreInfo(String rawDataStoreInfo) {
    this.rawDataStoreInfo = rawDataStoreInfo;
    initFromRawData();
  }

  private void initFromRawData() {
    InfoNodeElement rootNode = SaxInfoNodeBuilder.WITH_ROOT_NODE.buildInfoNode(rawDataStoreInfo, null);

    setDataStoreContext(rootNode.getChildText("context"));
    setPrivateRootDataStorePath(rootNode.getChildText("private-root-data-store-path"));

    Map<String, AccessControl> acl = new HashMap<String, AccessControl>();
    Element aclElement = rootNode.getChild("acl");
    if (aclElement != null) {
      @SuppressWarnings("unchecked")
      List<InfoNodeElement> aclNode = aclElement.getChildren();
      for (InfoNodeElement accessNode : aclNode) {
        String key = accessNode.getAttributeValue("id");
        Set<String> actions = new HashSet<String>(accessNode.getAttributeTransformer("actions").getStringListValue());
        Set<String> userNames = new HashSet<String>(accessNode.getValueTransformer().getStringListValue());
        AccessControl ac = new AccessControl(accessNode.getAttributeValue("id"), actions, userNames);

        acl.put(key, ac);
      }
    }
    setAccessControlList(acl);
  }
}
