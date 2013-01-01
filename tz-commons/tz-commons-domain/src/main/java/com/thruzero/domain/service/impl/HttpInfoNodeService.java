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

package com.thruzero.domain.service.impl;

import com.thruzero.common.core.support.SimpleInfo;
import com.thruzero.domain.dao.impl.HttpTextEnvelopeDAO;
import com.thruzero.domain.locator.DAOLocator;

/**
 * An implementation of AbstractInfoNodeService that retrieves its data exclusively using an HTTP connection,
 * enabling each user to have a personal remote data-store (e.g., DropBox, box.net, etc).
 * <p>
 * The implementation uses {@link com.thruzero.domain.HttpTextEnvelopeDAO.dao.RemoteTextEnvelopeDAO RemoteTextEnvelopeDAO}
 *
 * @author George Norman
 */
public final class HttpInfoNodeService extends AbstractInfoNodeService {

  private HttpInfoNodeService() {
    super(DAOLocator.locate(HttpTextEnvelopeDAO.class));
  }

  @Override
  public SimpleInfo getSimpleInfo() {
    return SimpleInfo.createSimpleInfo(this, getTextEnvelopeDAO());
  }
}
