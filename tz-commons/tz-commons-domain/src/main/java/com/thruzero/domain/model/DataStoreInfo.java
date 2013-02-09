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

package com.thruzero.domain.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Information about a particular user's data store. Each user has their own private store.
 * Access to data in any particular user's store is controlled via the Access Control List (ACL).
 * [EXPERIMENTAL]
 *
 * @author George Norman
 */
public class DataStoreInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  private String dataStoreContext;
  private String privateRootDataStorePath;
  private Map<String, AccessControl> accessControlList;

  // ------------------------------------------------------
  // AccessControl
  // ------------------------------------------------------

  /**
   * A primitive API that defines a set of users that can perform a set of actions on a particular dataset.
   */
  public final class AccessControl implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Set<String> actions;
    private Set<String> userNames;

    public AccessControl(String id, Set<String> actions, Set<String> userNames) {
      this.id = id;
      this.actions = new HashSet<String>(actions);
      this.userNames = new HashSet<String>(userNames);
    }

    /** The name of the access control. */
    public final String getId() {
      return id;
    }

    /** The actions that can be performed on any data associated with this particular control. */
    public final Set<String> getActions() {
      return actions;
    }

    /** The user names of the users that are affected by this control instance. */
    public final Set<String> getUserNames() {
      return userNames;
    }
  }

  // ============================================================================
  // DataStoreInfo
  // ============================================================================

  /** Return the ID of the user's personal data store. */
  public final String getDataStoreContext() {
    return dataStoreContext;
  }

  public final void setDataStoreContext(String dataStoreContext) {
    this.dataStoreContext = dataStoreContext;
  }

  /**
   * Return the optional ROOT data-store path for this user. Each Entity is stored within a root data-store.
   * Typically, the root data-store is the same for all entities. However, some users may require a personal
   * ROOT data-store on a separate machine or provided by a separate service (e.g., "http://dl.dropbox.com/u/012345/").
   *
   * @see com.thruzero.common.core.support.EntityPath
   */
  public final String getPrivateRootDataStorePath() {
    return privateRootDataStorePath;
  }

  public final void setPrivateRootDataStorePath(String privateRootDataStorePath) {
    this.privateRootDataStorePath = privateRootDataStorePath;
  }

  /**
   * Return the list of access controls defined by this user. The AccessControl ID
   * can be associated with multiple data-sets, to control access by external users.
   */
  public final Map<String, AccessControl> getAccessControlList() {
    return Collections.unmodifiableMap(accessControlList);
  }

  public final void setAccessControlList(Map<String, AccessControl> accessControlList) {
    this.accessControlList = new HashMap<String, AccessControl>(accessControlList);
  }
}
