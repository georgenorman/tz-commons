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

import com.thruzero.common.jsf.utils.FacesUtils;
import com.thruzero.common.jsf.utils.FlashUtils;

/**
 * An abstract base class for a dialog that uses the "flash-hack" scope to shuttle the
 * dialog model between pages.
 * <p>
 * Note: It's expected that the "flash-hack" will be replaced with something better, someday.
 *
 * @author George Norman
 */
public abstract class AbstractFlashDialogBean extends AbstractDialogBean {
  private static final long serialVersionUID = 1L;

  private String flashHackKey;

  // ----------------------------------------------------
  // DialogFlashPayload
  // ----------------------------------------------------

  public static interface DialogFlashPayload {
    AbstractDialogModel getDialogModel();
  }

  // ============================================================================
  // AbstractFlashDialogBean
  // ============================================================================

  public AbstractFlashDialogBean() {
    this(FacesUtils.getRequest().getParameter(FlashUtils.FLASH_HACK_REQUEST_PARAMETER_KEY));
  }

  public AbstractFlashDialogBean(String flashHackKey) {
    this.flashHackKey = flashHackKey;
  }

  public String getFlashHackKey() {
    return flashHackKey;
  }

  public void setFlashHackKey(String flashHackKey) {
    this.flashHackKey = flashHackKey;
  }

  @Override
  protected AbstractDialogModel getDialogModel(boolean remove) {
    DialogFlashPayload flashPayload;

    if (remove) {
      flashPayload = (DialogFlashPayload)FlashUtils.removeFlashAttribute(flashHackKey);
    } else {
      flashPayload = (DialogFlashPayload)FlashUtils.getFlashAttribute(flashHackKey);
    }

    return (flashPayload == null) ? null : flashPayload.getDialogModel();
  }

}
