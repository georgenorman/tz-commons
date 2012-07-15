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
package com.thruzero.common.jsf.support.beans.dialog;

import com.thruzero.common.jsf.support.ActionCallback;

/**
 * A confirmation dialog bean that manages a message plus OK and cancel actions.
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="confirmationDialogBean")
@javax.faces.bean.RequestScoped
public class ConfirmationDialogBean extends AbstractFlashDialogBean {
  private static final long serialVersionUID = 1L;

  // ------------------------------------------------------
  // ConfirmationDialogModel
  // ------------------------------------------------------

  public static class ConfirmationDialogModel extends AbstractDialogModel {
    private static final long serialVersionUID = 1L;

    private ActionCallback okActionCallback;
    private ActionCallback cancelActionCallback;

    @Override
    public void reset() {
      super.reset();

      okActionCallback = null;
      cancelActionCallback = null;
    }

    public ActionCallback getOkActionCallback() {
      return okActionCallback;
    }

    public void setOkActionCallback(ActionCallback okActionCallback) {
      this.okActionCallback = okActionCallback;
    }

    public ActionCallback getCancelActionCallback() {
      return cancelActionCallback;
    }

    public void setCancelActionCallback(ActionCallback cancelActionCallback) {
      this.cancelActionCallback = cancelActionCallback;
    }
  }

  // ============================================================================
  // ConfirmationDialogBean
  // ============================================================================

  /**
   * Execute the Ok ActionCallback, in the model, and return the outcome as a URL without the context (for use with a JSF anchor tag).
   */
  public String handleOkAction() {
    ConfirmationDialogModel model = (ConfirmationDialogModel)getDialogModel(true);

    return model.getOkActionCallback().handleAction().getUrl();
  }

  /**
   * Execute the Ok ActionCallback, in the model, and return the outcome as a URL with the context (for use with a plain HTML anchor).
   */
  public String handleOkActionWithContext() {
    ConfirmationDialogModel model = (ConfirmationDialogModel)getDialogModel(true);

    return model.getOkActionCallback().handleAction().getUrlWithContext();
  }

  /**
   * Execute the Cancel ActionCallback, in the model, and return the outcome as a URL without the context (for use with a JSF anchor tag).
   */
  public String handleCancelAction() {
    ConfirmationDialogModel model = (ConfirmationDialogModel)getDialogModel(true);

    return model.getCancelActionCallback().handleAction().getUrl();
  }

  /**
   * Execute the Cancel ActionCallback, in the model, and return the outcome as a URL with the context (for use with a plain HTML anchor).
   */
  public String handleCancelActionWithContext() {
    ConfirmationDialogModel model = (ConfirmationDialogModel)getDialogModel(true);

    return model.getCancelActionCallback().handleAction().getUrlWithContext();
  }

}
