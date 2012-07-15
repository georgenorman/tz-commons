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

import com.thruzero.common.jsf.support.beans.UrlBean;

/**
 * Manages a message plus a back action, for a simple message dialog.
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="messageDialogBean")
@javax.faces.bean.SessionScoped // TODO-p1(george) prefer ConversationScoped or try FlashHack
public class MessageDialogBean extends AbstractDialogBean {
  private static final long serialVersionUID = 1L;

  private MessageDialogModel messageDialogModel;

  // ------------------------------------------------------
  // MessageDialogModel
  // ------------------------------------------------------

  public static class MessageDialogModel extends AbstractDialogModel {
    private static final long serialVersionUID = 1L;

    private UrlBean doneOutcome;

    @Override
    public void reset() {
      super.reset();

      doneOutcome = null;
    }

    public UrlBean getDoneOutcome() {
      return doneOutcome;
    }

    public void setDoneOutcome(UrlBean doneOutcome) {
      this.doneOutcome = doneOutcome;
    }
  }

  // ============================================================================
  // MessageDialogBean
  // ============================================================================

  public void reset() {
    if (getDialogModel(false) != null) {
      getDialogModel(false).reset();
    }
  }

  public UrlBean getDoneOutcome() {
    MessageDialogModel model = getDialogModel(false);

    return model.getDoneOutcome();
  }

  @Override
  public MessageDialogModel getDialogModel(boolean remove) {
    MessageDialogModel result = messageDialogModel;

    if (remove) {
      messageDialogModel = null;
    }

    return result;
  }

  public void setDialogModel(MessageDialogModel messageDialogModel) {
    this.messageDialogModel = messageDialogModel;
  }

}
