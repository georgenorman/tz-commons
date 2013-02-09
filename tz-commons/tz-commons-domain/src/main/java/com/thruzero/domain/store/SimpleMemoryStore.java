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
package com.thruzero.domain.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.thruzero.domain.dao.impl.GenericMemoryDAO.MemoryStore;

/**
 * An implementation of MemoryStore that uses a LinkedHashMap for persistence. If this is used with a
 * Singleton (e.g., DAO), then clients of that Singleton will share the same data.
 *
 * @author George Norman
 * @param <T> Type of Domain Object managed by this MemoryStore.
 */
public class SimpleMemoryStore<T extends Persistent> implements MemoryStore<T> {
  private Map<Serializable, T> entityMap;

  public SimpleMemoryStore() {
    this.entityMap = new LinkedHashMap<Serializable, T>();
  }

  @Override
  public void persist(T entity) {
    entityMap.put(entity.getId(), entity);
  }

  @Override
  public T find(Serializable primaryKey) {
    return entityMap.get(primaryKey);
  }

  @Override
  public Collection<T> getResultList() {
    return entityMap.values();
  }

  @Override
  public Set<Serializable> getKeySet() {
    return entityMap.keySet();
  }

  @Override
  public boolean contains(T entity) {
    return entityMap.containsKey(entity.getId());
  }

  @Override
  public void remove(T entity) {
    entityMap.remove(entity.getId());
  }

  @Override
  public void clear() {
    entityMap.clear();
  }
}
