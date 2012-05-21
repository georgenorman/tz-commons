/*
 *   Copyright 2011-2012 George Norman
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
package com.thruzero.auth.jpa.service;

import com.thruzero.domain.jpa.dao.JpaTextEnvelopeDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.service.impl.AbstractInfoNodeService;
import com.thruzero.domain.store.BaseStorePath;

/**
 * An InfoNodeService implemented explicitly using the JpaTextEnvelopeDAO. This enables mixing multiple different Service types in the same application.
 *
 * @author George Norman
 */
public class JpaInfoNodeService extends AbstractInfoNodeService {

  // ------------------------------------------------------
  // JpaBaseStorePath
  // ------------------------------------------------------

  public static class JpaBaseStorePath implements BaseStorePath {

  }

  // ============================================================================
  // JpaInfoNodeService
  // ============================================================================

  public JpaInfoNodeService() {
    super(DAOLocator.locate(JpaTextEnvelopeDAO.class));
  }

  /**
   * Return the absolute base path for the container managed by this service - used for diagnostics.
   */
  public BaseStorePath getBaseStorePath() {
    return new JpaBaseStorePath();
  }

  @Override
  public String getInfo() {
    return getClass().getSimpleName() + " is configured to use " + getTextEnvelopeDAO().getInfo();
  }

}
