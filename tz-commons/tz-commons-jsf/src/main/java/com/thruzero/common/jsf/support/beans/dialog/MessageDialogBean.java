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
 * Manages a message and back action for a simple message dialog.
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="messageDialogBean")
@javax.faces.bean.SessionScoped // TODO-p1(george) prefer ConversationScoped or try FlashHack
public class MessageDialogBean {
  private String title;
  private String header;
  private String message;
  private UrlBean backAction;

  public void reset() {
    title = null;
    header = null;
    message = null;
    backAction = null;
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

  public void setBackAction(UrlBean backAction) {
    this.backAction = backAction;
  }

  public UrlBean getBackAction() {
    return backAction;
  }

  public String goBack() {
    return backAction.getUrl();
  }

  public String goBackWithContext() {
    return backAction.getUrlWithContext();
  }

}
