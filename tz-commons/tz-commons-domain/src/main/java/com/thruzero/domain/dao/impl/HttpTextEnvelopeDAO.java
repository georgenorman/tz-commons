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

package com.thruzero.domain.dao.impl;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.KeyGen;
import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.model.TextEnvelope;

/**
 * An implementation of TextEnvelopeDAO that retrieves data via an HTTP connection.
 * This enables each individual user to have a personal remote data-store (e.g., DropBox).
 * This DAO is used for reading the data-store, while managing the contents is handled by the tools
 * provided by the remote service (e.g., create, update and delete files via the DropBox desktop drive).
 *
 * @author George Norman
 */
public final class HttpTextEnvelopeDAO extends GenericMemoryDAO<TextEnvelope> implements TextEnvelopeDAO {

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

  // ------------------------------------------------------
  // HttpTextEnvelopeMemoryStore
  // ------------------------------------------------------

  public static class HttpTextEnvelopeMemoryStore implements MemoryStore<TextEnvelope> {

    @Override
    public void persist(TextEnvelope entity) {
      // TODO-p2(george) Auto-generated method stub
    }

    /**
     * Return a {@code TextEnvelope} using an HTTP connection, defined by the given primaryKey. In this case, the
     * primaryKey is expected to be an {@code EntityPath} that resolves to a well-formed URL to the text resource.
     */
    @Override
    public TextEnvelope find(Serializable primaryKey) {
      TextEnvelope result;
      HttpURLConnection connection = null;

      try {
        URL remoteUrl = new URL(primaryKey.toString());
        connection = (HttpURLConnection)remoteUrl.openConnection();
        connection.setRequestMethod("GET");
        connection.setAllowUserInteraction(false);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();

        String rawData = IOUtils.toString(connection.getInputStream());
        result = new TextEnvelope((EntityPath)primaryKey, rawData);
      } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("ERROR reading remote data store: " + e.toString());
      } finally {
        if (connection != null) {
          connection.disconnect();
        }
      }

      return result;
    }

    @Override
    public Collection<TextEnvelope> getResultList() {
      // TODO-p2(george) Auto-generated method stub
      return null;
    }

    @Override
    public Set<Serializable> getKeySet() {
      // TODO-p2(george) Auto-generated method stub
      return null;
    }

    @Override
    public boolean contains(TextEnvelope entity) {
      return find(entity.getId()) != null; // TODO-p1(george). This may cause a double-fetch.
    }

    @Override
    public void remove(TextEnvelope entity) {
      // TODO-p2(george) Auto-generated method stub
    }

    @Override
    public void clear() {
      // TODO-p2(george) Auto-generated method stub
    }
  }

  // ============================================================================
  // RemoteTextEnvelopeDAO
  // ============================================================================

  protected HttpTextEnvelopeDAO() {
    super(new HttpTextEnvelopeMemoryStore());
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
    // TODO-p2(george) Auto-generated method stub
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
