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
package com.thruzero.domain.dsc.service;

import com.thruzero.domain.dsc.dao.DscTextEnvelopeDAO;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.service.impl.AbstractInfoNodeService;
import com.thruzero.domain.store.BaseStorePath;

/**
 * An InfoNodeService implemented explicitly using the DscTextEnvelopeDAO. This enables mixing multiple different Service types in the same application.
 *
 * <p/>
 * The default configuration for this Service is:
 *
 * <pre>
 * {@code
 * <!-- All Service's are registered using sections named "com.thruzero.common.core.service.Service" - all sections with this name will be merged into one, by the Config -->
 * <section name="com.thruzero.common.core.service.Service">
 *   <entry key="com.thruzero.domain.service.impl.DscInfoNodeService" value="com.thruzero.domain.service.impl.DscInfoNodeService" />
 * </section>
 * }
 * </pre>
 *
 * A different subclass of {@code DscInfoNodeService} may be used for the value, if desired.
 *
 * @author George Norman
 */
public class DscInfoNodeService extends AbstractInfoNodeService {

  public DscInfoNodeService() {
    super(DAOLocator.locate(DscTextEnvelopeDAO.class));
  }

  /**
   * Return the absolute base path for the container managed by this service - used for diagnostics.
   */
  public BaseStorePath getBaseStorePath() {
    DscTextEnvelopeDAO dscTextEnvelopeDAO = (DscTextEnvelopeDAO)getTextEnvelopeDAO();

    return dscTextEnvelopeDAO.getBaseStorePath();
  }

  @Override
  public String getInfo() {
    return getClass().getSimpleName() + " is configured to use " + getTextEnvelopeDAO().getInfo();
  }
}
