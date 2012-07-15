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

import java.io.Serializable;

/**
 * Abstract base class for simple dialogs that have a title, header and message.
 * Provides an abstract dialog model (AbstractDialogModel) for subclasses to
 * shuttle dialog data between pages independent of any dialog scope.
 *
 * @author George Norman
 */
public abstract class AbstractDialogBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // ------------------------------------------------------
  // AbstractDialogModel
  // ------------------------------------------------------

  public static class AbstractDialogModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String header;
    private String message;

    public void reset() {
      title = null;
      header = null;
      message = null;
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
  }

  // ============================================================================
  // AbstractDialogBean
  // ============================================================================

  /**
   * Return the dialog title - typically displayed in the browser's title bar.
   */
  public String getTitle() {
    String result;
    AbstractDialogModel model = getDialogModel(false);

    if (model == null) {
      result = "ERROR: Model not found.";
    } else {
      result = model.getTitle();
    }

    return result;
  }

  /**
   * Return the dialog header - typically displayed in larger strong text at the top of the dialog.
   */
  public String getHeader() {
    String result;
    AbstractDialogModel model = getDialogModel(false);

    if (model == null) {
      result = "ERROR: Model not found.";
    } else {
      result = model.getHeader();
    }

    return result;
  }

  /**
   * Return the dialog message - typically displayed in the dialog's body. The message may contain markup, however,
   * the template chosen to render the dialog determines if the markup is rendered.
   */
  public String getMessage() {
    String result;
    AbstractDialogModel model = getDialogModel(false);

    if (model == null) {
      result = "ERROR: Model not found.";
    } else {
      result = model.getMessage();
    }

    return result;
  }

  /**
   * Return the dialog model associated with this bean and if the given remove parameter is true, then
   * also disassociate it from this bean (e.g., remove the model from flash scope).
   */
  protected abstract AbstractDialogModel getDialogModel(boolean remove);

}
