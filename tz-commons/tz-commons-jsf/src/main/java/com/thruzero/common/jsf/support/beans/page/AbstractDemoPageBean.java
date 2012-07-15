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
package com.thruzero.common.jsf.support.beans.page;

import java.io.Serializable;

import com.thruzero.common.core.support.SimpleInfoProvider;
import com.thruzero.common.jsf.support.beans.UrlBean;
import com.thruzero.common.jsf.support.beans.dialog.MessageDialogBean;
import com.thruzero.common.jsf.support.beans.dialog.MessageDialogBean.MessageDialogModel;

/**
 * Abstract base class for a simple DEMO page bean that requires support for a single Info Dialog.
 *
 * @author George Norman
 */
public class AbstractDemoPageBean implements Serializable {
  private static final long serialVersionUID = 1L;

  @javax.faces.bean.ManagedProperty(value="#{messageDialogBean}")
  private MessageDialogBean messageDialogBean;

  /** Init the Info Dialog using the class name in the title and message and no back action. */
  protected void initInfoDialog() {
    initInfoDialog((UrlBean)null);
  }

  /** Init the Info Dialog using the class name in the title, plus the given info for the message and no back action. */
  protected void initInfoDialog(SimpleInfoProvider info) {
    initInfoDialog(info, null);
  }

  /** Init the Info Dialog using the class name in the title, plus the given info for the message, plus the given backAction. */
  protected void initInfoDialog(SimpleInfoProvider info, UrlBean backAction) {
    doInitInfoDialog("The backing bean for this page is " + getClass().getSimpleName() + " and its " + info.getSimpleInfo().getInfo(), backAction);
  }

  /** Init the Info Dialog using the given backAction, plus using the bean's class name in the title and message. */
  protected void initInfoDialog(UrlBean backAction) {
    doInitInfoDialog("The backing bean for this page is " + getClass().getSimpleName() + ".", backAction);
  }

  /** Init the Info Dialog using the given message and backAction, plus using the bean's class name in the title. */
  protected void doInitInfoDialog(String message, UrlBean backAction) {
    doInitInfoDialog("Info about " + getClass().getSimpleName(), message, backAction);
  }

  /** Init the Info Dialog using the given title, message and backAction. */
  protected void doInitInfoDialog(String title, String message, UrlBean doneOutcome) {
    MessageDialogModel model = new MessageDialogModel();

    model.setTitle(title);
    model.setMessage(message);
    model.setDoneOutcome(doneOutcome);

    getMessageDialogBean().setDialogModel(model);
  }

  // IoC functions /////////////////////////////////////////////////////////////////

  public MessageDialogBean getMessageDialogBean() {
    return messageDialogBean;
  }

  public void setMessageDialogBean(MessageDialogBean messageDialogBean) {
    this.messageDialogBean = messageDialogBean;

    messageDialogBean.reset();
  }

}
