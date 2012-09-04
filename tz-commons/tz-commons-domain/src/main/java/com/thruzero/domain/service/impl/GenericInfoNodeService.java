/*
 *   Copyright 2011 - 2012 George Norman
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
package com.thruzero.domain.service.impl;

import com.thruzero.common.core.support.SimpleInfo;
import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.locator.DAOLocator;

/**
 * An implementation of the InfoNodeService, using {@link com.thruzero.domain.dao.TextEnvelopeDAO TextEnvelopeDAO}
 * as its persistence provider. The default configuration for this Service is:
 *
 * <pre>
 * {@code
 * <!-- All Service's are registered using sections named "com.thruzero.common.core.service.Service" - all sections with this name will be merged into one by the Config -->
 * <section name="com.thruzero.common.core.service.Service">
 *   <entry key="com.thruzero.domain.service.InfoNodeService" value="com.thruzero.domain.service.impl.GenericInfoNodeService" />
 * </section>
 * }
 * </pre>
 * <p>
 * Note: The particular DAO configured for this service defines where the data is actually stored (e.g., file system, relational table, etc).
 *
 * @author George Norman
 */
public final class GenericInfoNodeService extends AbstractInfoNodeService {

  /**
   * Use {@link com.thruzero.common.core.locator.ServiceLocator ServiceLocator} to access a particular Service.
   */
  private GenericInfoNodeService() {
    super(DAOLocator.locate(TextEnvelopeDAO.class));
  }

  @Override
  public SimpleInfo getSimpleInfo() {
    return SimpleInfo.createSimpleInfo(this, getTextEnvelopeDAO());
  }
}
