/*
 *   Copyright 2011 George Norman
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
package com.thruzero.domain.service;

import java.util.Collection;
import java.util.List;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.service.Service;
import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.SimpleInfoProvider;
import com.thruzero.domain.model.DataStoreInfo;

/**
 * A Service interface to manage persistence-related functionality of InfoNodeElement instances.
 * <p>
 * Note: The particular DAO configured for this service defines where the data is actually stored.
 * 
 * @author George Norman
 */
public interface InfoNodeService extends Service, SimpleInfoProvider {

  /**
   * Return the collection of InfoNodeElement instances at the given containerPath. If recursive is true, then return all of the InfoNodeElement instances for
   * all child container paths.
   * <p/>
   * For example, consider a Service configured for a database with the TEXT_ENVELOPE table populated as follows:
   * 
   * <pre>
   * {@literal
   *    ----------------------------------------------------------------
   *   | PATH                 | DATA                                    |
   *    ----------------------------------------------------------------
   *   | /a/x1.xml            | <x1><y1>D test</y1><y2>A test</y2></x1> |
   *   | /a/b/x2.xml          | <x2><y1>A test</y1><y2>T test</y2></x2> |
   *   | /a/b/x3.xml          | <x3><y1>T test</y1><y2>A test</y2></x3> |
   *   | /c/x4.xml            | <x4><y1>A test</y1><y2>D test</y2></x4> |
   *    ----------------------------------------------------------------
   * }
   * </pre>
   * Then:
   * <ul>
   * <li>getInfoNodes("/a/", false) =&gt; "x1.xml".</li>
   * <li>getInfoNodes("/b/", false) =&gt; "x2.xml" and "x3.xml".</li>
   * <li>getInfoNodes("/a/", true) =&gt; "x1.xml", "x2.xml" and "x3.xml".</li>
   * </ul>
   */
  Collection<? extends InfoNodeElement> getInfoNodes(ContainerPath containerPath, boolean recursive);

  List<EntityPath> getInfoNodePaths(ContainerPath containerPath, boolean recursive);

  InfoNodeElement getInfoNode(EntityPath entityPath);

  InfoNodeElement getInfoNode(EntityPath entityPath, final DataStoreInfo dataStoreInfo);

  /** Return true if resource exists at the given entity path. */
  boolean isExistingEntity(EntityPath entityPath);

  /**
   * Return the first InfoNodeElement found at the given entityPath and xpathExpr. For example, consider a Service configured for a database with the
   * TEXT_ENVELOPE table populated as follows:
   * 
   * <pre>
   * {@literal
   *    ----------------------------------------------------------------
   *   | PATH                 | DATA                                    |
   *    ----------------------------------------------------------------
   *   | /a/x1.xml            | <x1><y1>D test</y1><y2>A test</y2></x1> |
   *    ----------------------------------------------------------------
   * }
   * </pre>
   * 
   * Then:
   * <ul>
   * <li>getFirstInfoNode("/a/x1.xml", "//y1") =&gt; "&lt;y1&gt;D test&lt;/y1&gt;".</li> TODO-p1(george) TEST THIS
   * </ul>
   */
  InfoNodeElement getFirstInfoNode(EntityPath entityPath, String xpathExpr);

  /** Save the new domainObject to the data store */
  void save(InfoNodeElement domainObject);

  /**
   * Tests to see if the object is new or has been previously persisted and if the object is new, it creates a new entity in the data store (e.g., inserts a new
   * row) or if it's already in the data store, it will update the existing entity.
   */
  void saveOrUpdate(InfoNodeElement domainObject);

  /** Update the data store from the given domainObject. */
  void update(InfoNodeElement domainObject);

  /** Delete the domainObject from the data store, if it exists. */
  void delete(InfoNodeElement domainObject);

  String getRawData(EntityPath entityPath);

  void saveOrUpdateRawData(EntityPath entityPath, String data);

}
