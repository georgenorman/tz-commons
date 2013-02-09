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
package com.thruzero.domain.jsf.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.KeyGen;
import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.model.TextEnvelope;

/**
 *
 * @author George Norman
 */
public final class HttpSessionTextEnvelopeDAO extends HttpSessionDAO<TextEnvelope> implements TextEnvelopeDAO {

  // ------------------------------------------------------
  // TextEnvelopeKeyGen
  // ------------------------------------------------------

  /** A primary-key generator for uniquely identifying an instance of TextEnvelope in the store. */
  public static class TextEnvelopeKeyGen extends KeyGen<TextEnvelope> {
    @Override
    public Serializable createKey(TextEnvelope domainObject) {
      return domainObject.getEntityPath();
    }
  }

  // ============================================================================
  // HttpSessionTextEnvelopeDAO
  // ============================================================================

  /**
   * Use {@link com.thruzero.domain.locator.DAOLocator DAOLocator} to access a particular DAO.
   */
  private HttpSessionTextEnvelopeDAO() {
    super(new HttpMemoryStore<TextEnvelope>(TextEnvelope.class));
  }

  @Override
  public boolean isExistingTextEnvelope(EntityPath entityPath) {
    return isExistingEntity(entityPath);
  }

  @Override
  public List<? extends TextEnvelope> getTextEnvelopes(ContainerPath containerPath, boolean recursive) {
    List<TextEnvelope> result = new ArrayList<TextEnvelope>();
    Collection<? extends TextEnvelope> allEntities = getMemoryStore().getResultList();

    for (TextEnvelope entity : allEntities) {
      if (entity.getEntityPath().getContainerPath().equals(containerPath)) {
        result.add(entity);
      }
    }

    return result;
  }

  @Override
  public List<EntityPath> getTextEnvelopePaths(ContainerPath containerPath, boolean recursive) {
    List<EntityPath> result = new ArrayList<EntityPath>();
    Set<Serializable> allPaths = getMemoryStore().getKeySet();

    for (Serializable path : allPaths) {
      if (path.toString().startsWith(containerPath.toString())) {
        result.add((EntityPath)path);
      }
    }

    return result;
  }

  @Override
  public TextEnvelope getTextEnvelope(EntityPath entityPath) {
    return getByKey(entityPath);
  }

  @Override
  protected KeyGen<TextEnvelope> createKeyGen() {
    return new TextEnvelopeKeyGen();
  }

}
