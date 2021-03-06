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
package com.thruzero.common.jsf.support;

import com.thruzero.common.jsf.support.beans.UrlBean;

/**
 * An abstract ActionCallback that adds a navigation outcome.
 *
 * @author George Norman
 */
public abstract class AbstractActionCallback implements ActionCallback {
  private UrlBean actionOutcome;

  protected AbstractActionCallback(UrlBean actionOutcome) {
    this.actionOutcome = actionOutcome;
  }

  protected UrlBean getActionOutcome() {
    return actionOutcome;
  }
}
