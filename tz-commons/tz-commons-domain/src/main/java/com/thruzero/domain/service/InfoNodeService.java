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

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.service.Service;
import com.thruzero.domain.store.ContainerPath;
import com.thruzero.domain.store.EntityPath;

/**
 * A Service interface to manage persistence-related functionality of InfoNodeElement instances.
 * <p>
 * Note: The particular DAO configured for this service defines where the data is actually stored.
 *
 * @author George Norman
 */
public interface InfoNodeService extends Service {

  /**
   * Return the collection of InfoNodeElement instances at the given containerPath.
   * If recursive is true, then return all of the InfoNodeElement instances for all child container paths.
   * <p/>
   * For example, consider a Service configured for a database with the TEXT_ENVELOPE table populated as follows:
   * <pre>
   *    ----------------------------------------------------------------
   *   | PATH                 | DATA                                    |
   *    ----------------------------------------------------------------
   *   | /a/x1.xml            | &lt;x1&gt;&lt;y1&gt;D test&lt;/y1&gt;&lt;y2&gt;A test&lt;/y2&gt;&lt;/x1&gt; |
   *   | /a/b/x2.xml          | &lt;x2&gt;&lt;y1&gt;A test&lt;/y1&gt;&lt;y2&gt;T test&lt;/y2&gt;&lt;/x2&gt; |
   *   | /a/b/x3.xml          | &lt;x3&gt;&lt;y1&gt;T test&lt;/y1&gt;&lt;y2&gt;A test&lt;/y2&gt;&lt;/x3&gt; |
   *   | /c/x4.xml            | &lt;x4&gt;&lt;y1&gt;A test&lt;/y1&gt;&lt;y2&gt;D test&lt;/y2&gt;&lt;/x4&gt; |
   *    ----------------------------------------------------------------
   * </pre>
   * Then:
   * <ul>
   *  <li>getInfoNodes("/a/", false) =&gt; "x1.xml".</li>
   *  <li>getInfoNodes("/b/", false) =&gt; "x2.xml" and "x3.xml".</li>
   *  <li>getInfoNodes("/a/", true)  =&gt; "x1.xml", "x2.xml" and "x3.xml".</li>
   * </ul>
   */
  Collection<? extends InfoNodeElement> getInfoNodes(ContainerPath containerPath, boolean recursive);

  InfoNodeElement getInfoNode(EntityPath entityPath);

  /**
   * Return the first InfoNodeElement found at the given entityPath and xpathExpr.
   * For example, consider a Service configured for a database with the TEXT_ENVELOPE table populated as follows:
   * <pre>
   *    ----------------------------------------------------------------
   *   | PATH                 | DATA                                    |
   *    ----------------------------------------------------------------
   *   | /a/x1.xml            | &lt;x1&gt;&lt;y1&gt;D test&lt;/y1&gt;&lt;y2&gt;A test&lt;/y2&gt;&lt;/x1&gt; |
   *    ----------------------------------------------------------------
   * </pre>
   * Then:
   * <ul>
   *  <li>getFirstInfoNode("/a/x1.xml", "//y1") =&gt; "&lt;y1&gt;D test&lt;/y1&gt;".</li> TODO-p1(george) TEST THIS
   * </ul>
   */
  InfoNodeElement getFirstInfoNode(EntityPath entityPath, String xpathExpr);

  /** Return a simple description of the service configuration (e.g., "GenericInfoNodeService configured using JpaTextEnvelopeDAO"). */
  String getInfo();

  // TODO-p1(george) add CRUD functions.

}
