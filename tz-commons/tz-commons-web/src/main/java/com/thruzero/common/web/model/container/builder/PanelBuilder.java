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

package com.thruzero.common.web.model.container.builder;

import com.thruzero.common.web.model.container.AbstractPanel;

/**
 * An interface for a panel builder (of type {@link com.thruzero.common.web.model.container.AbstractPanel AbstractPanel}), where each subclass
 * represents a builder of a particular type of panel.
 *
 * @author George Norman
 */
public interface PanelBuilder {

  /**
   * Builds a panel (of type {@link com.thruzero.common.web.model.container.AbstractPanel AbstractPanel}).
   */
  AbstractPanel build() throws Exception;

}
