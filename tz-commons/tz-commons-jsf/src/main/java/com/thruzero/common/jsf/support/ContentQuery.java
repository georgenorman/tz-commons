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

package com.thruzero.common.jsf.support;

import java.io.Serializable;

import com.thruzero.common.core.support.EntityPath;

/**
 * A simple XML query, representing an entity (via an EntityPath) and a node within the
 * entity (via an X-Path). Used to retrieve specific content from within an entity.
 *
 * @author George Norman
 */
public class ContentQuery implements Serializable {
  private static final long serialVersionUID = 1L;

  private final EntityPath entityPath;
  private final String xPath;

  public ContentQuery(EntityPath entityPath, String xPath) {
    this.entityPath = entityPath;
    this.xPath = xPath;
  }

  public EntityPath getEntityPath() {
    return entityPath;
  }

  public String getEntityPathAsString() {
    return entityPath.toString();
  }

  public String getXPath() {
    return xPath;
  }

}
