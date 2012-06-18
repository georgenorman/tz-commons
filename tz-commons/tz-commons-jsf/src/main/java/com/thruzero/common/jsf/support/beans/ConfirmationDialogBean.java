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
package com.thruzero.common.jsf.support.beans;

import com.thruzero.common.jsf.support.ActionCallback;



/**
 * A confirmation dialog bean that manages a message plus OK and cancel actions.
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="confirmationDialogBean")
@javax.faces.bean.SessionScoped // TODO-p1(george) prefer ConversationScoped
public class ConfirmationDialogBean {
  private String title;
  private String message;
  private ActionCallback okActionCallback;
  private ActionCallback cancelActionCallback;

  public void reset() {
    title = null;
    message = null;
    okActionCallback = null;
    cancelActionCallback = null;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setOkAction(ActionCallback actionCallback) {
    this.okActionCallback = actionCallback;
  }

  public void setCancelAction(ActionCallback cancelActionCallback) {
    this.cancelActionCallback = cancelActionCallback;
  }

  public String handleOkAction() {
    return okActionCallback.handleAction().getUrl();
  }

  public String handleOkActionWithContext() {
    return okActionCallback.handleAction().getUrlWithContext();
  }

  public String handleCancelAction() {
    return cancelActionCallback.handleAction().getUrl();
  }

  public String handleCancelActionWithContext() {
    return cancelActionCallback.handleAction().getUrlWithContext();
  }

}
