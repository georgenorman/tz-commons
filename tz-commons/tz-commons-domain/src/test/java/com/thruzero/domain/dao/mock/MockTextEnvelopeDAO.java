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
package com.thruzero.domain.dao.mock;

import java.io.Serializable;
import java.util.List;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.KeyGen;
import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.dao.impl.GenericMemoryDAO;
import com.thruzero.domain.model.TextEnvelope;
import com.thruzero.domain.store.SimpleMemoryStore;

/**
 *
 * @author George Norman
 */
public final class MockTextEnvelopeDAO extends GenericMemoryDAO<TextEnvelope> implements TextEnvelopeDAO {

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
  // MockTextEnvelopeDAO
  // ============================================================================

  /**
   * Use {@link com.thruzero.domain.locator.DAOLocator DAOLocator} to access a particular DAO.
   */
  private MockTextEnvelopeDAO() {
    super(new SimpleMemoryStore<TextEnvelope>());
  }

  @Override
  public boolean isExistingTextEnvelope(EntityPath entityPath) {
    return isExistingEntity(entityPath);
  }

  @Override
  public List<? extends TextEnvelope> getTextEnvelopes(ContainerPath containerPath, boolean recursive) {
    // TODO-p1(george) Auto-generated method stub
    return null;
  }

  @Override
  public List<EntityPath> getTextEnvelopePaths(ContainerPath containerPath, boolean recursive) {
    // TODO-p0(george) Auto-generated method stub
    return null;
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
