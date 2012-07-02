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
import com.thruzero.common.jsf.utils.FacesUtils;
import com.thruzero.common.jsf.utils.FlashUtils;

/**
 * A confirmation dialog bean that manages a message plus OK and cancel actions.
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="confirmationDialogBean")
@javax.faces.bean.RequestScoped
public class ConfirmationDialogBean {

  private String flashHackKey;

  // ------------------------------------------------------
  // ConfirmationDialogModel
  // ------------------------------------------------------

  public static class ConfirmationDialogModel {
    private String title;
    private String header;
    private String message;
    private ActionCallback okActionCallback;
    private ActionCallback cancelActionCallback;

    public void reset() {
      title = null;
      header = null;
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

    public String getHeader() {
      return header;
    }

    public void setHeader(String header) {
      this.header = header;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
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

  public ConfirmationDialogBean() {
    setFlashHackKey(FacesUtils.getRequest().getParameter(FlashUtils.FLASH_HACK_REQUEST_PARAMETER_KEY));
  }

  public String getFlashHackKey() {
    return flashHackKey;
  }

  public void setFlashHackKey(String flashHackKey) {
    this.flashHackKey = flashHackKey;
  }

  public String getTitle() {
    String result;
    ConfirmationDialogModel model = (ConfirmationDialogModel)FlashUtils.getFlashAttribute(flashHackKey);

    if (model == null) {
      result = "ERROR: Model not found.";
    } else {
      result = model.getTitle();
    }

    return result;
  }

  public String getHeader() {
    String result;
    ConfirmationDialogModel model = (ConfirmationDialogModel)FlashUtils.getFlashAttribute(flashHackKey);

    if (model == null) {
      result = "ERROR: Model not found.";
    } else {
      result = model.getHeader();
    }

    return result;
  }

  public String getMessage() {
    String result;
    ConfirmationDialogModel model = (ConfirmationDialogModel)FlashUtils.getFlashAttribute(flashHackKey);

    if (model == null) {
      result = "ERROR: Model not found.";
    } else {
      result = model.getMessage();
    }

    return result;
  }

  public String handleOkAction() {
    ConfirmationDialogModel model = (ConfirmationDialogModel)FlashUtils.removeFlashAttribute(flashHackKey);

    return model.getOkActionCallback().handleAction().getUrl();
  }

  public String handleOkActionWithContext() {
    ConfirmationDialogModel model = (ConfirmationDialogModel)FlashUtils.removeFlashAttribute(flashHackKey);

    return model.getOkActionCallback().handleAction().getUrlWithContext();
  }

  public String handleCancelAction() {
    ConfirmationDialogModel model = (ConfirmationDialogModel)FlashUtils.removeFlashAttribute(flashHackKey);

    return model.getCancelActionCallback().handleAction().getUrl();
  }

  public String handleCancelActionWithContext() {
    ConfirmationDialogModel model = (ConfirmationDialogModel)FlashUtils.removeFlashAttribute(flashHackKey);

    return model.getCancelActionCallback().handleAction().getUrlWithContext();
  }
}
