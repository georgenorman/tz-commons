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
package com.thruzero.domain.dao;

import java.util.List;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.model.TextEnvelope;

/**
 * A DAO that manages operations specific to the TextEnvelope Domain Object.
 *
 * @author George Norman
 */
public interface TextEnvelopeDAO extends GenericDAO<TextEnvelope> {

  /**
   * Returns true if a node at the given {@code entityPath} exists; otherwise, returns false.
   */
  boolean isExistingTextEnvelope(EntityPath entityPath);

  /**
   * Return all {@code TextEnvelope} instances with the given {@code containerPath} and if {@code recursive} is true, additionally return
   * all "nested" children under the {@code containerPath}. For example, if a store contains the following TextEnvelope instances:
   * <pre>
   *   /a/b/f1.txt
   *   /a/b/f2.xml
   *   /a/b/c/f3.txt
   * </pre>
   * Then, if the given containerPath is "/a/b/" and recursive is false, then the following instances will be returned: "/a/b/f1.txt" and
   * "/a/b/f2.xml". If recursive is true, then "/a/b/f1.txt", "/a/b/f2.xml" and "/a/b/c/f3.txt" are returned.
   */
  List<? extends TextEnvelope> getTextEnvelopes(ContainerPath containerPath, boolean recursive); // TODO-p1(george) what does this return if empty?

  List<EntityPath> getTextEnvelopePaths(ContainerPath containerPath, boolean recursive);

  /**
   * Returns the {@code TextEnvelope} at the given {@code entityPath}, if it exists; otherwise, returns {@code null}.
   */
  TextEnvelope getTextEnvelope(EntityPath entityPath);

}
