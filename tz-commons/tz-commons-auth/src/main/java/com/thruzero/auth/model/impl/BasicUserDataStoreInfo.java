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

import com.thruzero.auth.model.UserDataStoreInfo;
import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.SaxInfoNodeBuilder;

/**
 * Basic implementation of UserDataStoreInfo that constructs itself from a single XML field.
 * Following is an example:
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
public class BasicUserDataStoreInfo implements UserDataStoreInfo {
  private String rawDataStoreInfo;

  private String dataStoreContext;
  private String privateRootDataStorePath;
  private Map<String, AccessControl> accessControlList;

  // ------------------------------------------------------
  // BasicAccessControl
  // ------------------------------------------------------

  public class BasicAccessControl implements AccessControl {
    private String id;
    private Set<String> actions;
    private Set<String> userName;

    public BasicAccessControl(String id, Set<String> actions, Set<String> userName) {
      this.id = id;
      this.actions = actions;
      this.userName = userName;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public Set<String> getActions() {
      return actions;
    }

    @Override
    public Set<String> getUserNames() {
      return userName;
    }
  }

  // ============================================================================
  // BasicUserDataStoreInfo
  // ============================================================================

  /**
   * Default constructor, used when data is injected (e.g., Hibernate)
   */
  public BasicUserDataStoreInfo() {
  }

  public BasicUserDataStoreInfo(String rawDataStoreInfo) {
    this.rawDataStoreInfo = rawDataStoreInfo;
  }

  @Override
  public String getDataStoreContext() {
    ensureAllInfo();

    return dataStoreContext;
  }

  @Override
  public String getPrivateRootDataStorePath() {
    ensureAllInfo();

    return privateRootDataStorePath;
  }

  @Override
  public Map<String, AccessControl> getAccessControlList() {
    ensureAllInfo();

    return accessControlList;
  }

  private void ensureAllInfo() {
    if (rawDataStoreInfo != null) {
      InfoNodeElement rootNode = SaxInfoNodeBuilder.WITH_ROOT_NODE.buildInfoNode(rawDataStoreInfo, null);

      dataStoreContext = rootNode.getChildText("context");

      privateRootDataStorePath = rootNode.getChildText("private-root-data-store-path");

      accessControlList = new HashMap<String, AccessControl>();
      Element aclElement = rootNode.getChild("acl");

      if (aclElement != null) {
        @SuppressWarnings("unchecked")
        List<InfoNodeElement> aclNode = aclElement.getChildren();
        for (InfoNodeElement accessNode : aclNode) {
          String key = accessNode.getAttributeValue("id");
          Set<String> actions = new HashSet<String>(accessNode.getAttributeTransformer("actions").getStringListValue());
          Set<String> userNames = new HashSet<String>(accessNode.getValueTransformer().getStringListValue());
          AccessControl ac = new BasicAccessControl(accessNode.getAttributeValue("id"), actions, userNames);

          accessControlList.put(key, ac);
        }
      }
    }

    rawDataStoreInfo = null;
  }
}
