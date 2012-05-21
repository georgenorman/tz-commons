/*
 *   Copyright 2009-2012 George Norman
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
package com.thruzero.domain.support;

import java.io.File;

import javax.mail.internet.InternetAddress;

import com.thruzero.common.core.support.FileBasedTemplate;

/**
 * A file-based template, geared towards generating customizable email messages. it allows for a variety of mechanisms
 * to replace variables defined within the text of the email template (e.g., substitution strategy, Map).
 *
 * @author George Norman
 */
public class EmailTemplate extends FileBasedTemplate { // TODO-p1(george) Add support to retrieve email templates from a database
  private InternetAddress to;
  private InternetAddress from;
  private String subject;

  public EmailTemplate(final File emailTemplateDir, final String templateName, String templateFileExtension) {
    super(emailTemplateDir, templateName, templateFileExtension);
  }

  public InternetAddress getTo() {
    return to;
  }

  public void setTo(final InternetAddress to) {
    this.to = to;
  }

  public InternetAddress getFrom() {
    return from;
  }

  public void setFrom(final InternetAddress from) {
    this.from = from;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(final String subject) {
    this.subject = subject;
  }
}
