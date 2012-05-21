/*
 *   Copyright 2009-2011 George Norman
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
package com.thruzero.domain.service;

import java.util.Set;

import javax.mail.internet.InternetAddress;

import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.locator.Initializable.InitializableParameterKeys;
import com.thruzero.common.core.service.Service;
import com.thruzero.common.core.strategy.SubstitutionStrategy;
import com.thruzero.domain.support.EmailTemplate;

/**
 * A Service interface that enables clients to send simple email messages (without attachments). Coupled with the
 * <i>EmailTemplate</i> support class, emails can be defined in text files and can contain substitution variables which
 * can be replaced when an email is sent, using one of the available substitution strategies.
 *
 * @author George Norman
 */
public interface MailService extends Service {

  // ------------------------------------------------
  // JavaMailPropertyKeys
  // ------------------------------------------------

  public interface JavaMailPropertyKeys {
    /** The message transport protocol. */
    String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";

    /** The host name of the mail server. */
    String MAIL_HOST = "mail.host";

    /** The mail debug mode. */
    String MAIL_DEBUG = "mail.debug";

    /** The user name to use when connecting to the mail server. Used if the mail.protocol.user property isn't set. */
    String MAIL_USER = "mail.user";

    /** unencrypted password of email account */
    String MAIL_PASSWORD = "mail.password";
  }

  // ------------------------------------------------
  // MailServiceInitParamKeys
  // ------------------------------------------------

  /**
   * Initialization parameter keys defined for MailService.
   */
  @InitializationParameterKeysBookmark
  public interface MailServiceInitParamKeys extends InitializableParameterKeys {
    /** The config key that defines if email sending is enabled: "email-enabled". */
    String EMAIL_ENABLED = "email-enabled";

    /** The config key that defines if email logging is enabled: "email-logging-enabled". */
    String EMAIL_LOGGING_ENABLED = "email-logging-enabled";

    /** SMTP Host: "email-smtp-host" */
    String EMAIL_SMTP_HOST = "email-smtp-host";

    /** The config key that defines the user name for the email login: "email-user". */
    String EMAIL_USER = "email-user";

    /**
     * The config key that defines the encrypted password for the email login (will be decrypted before use):
     * "email-password".
     */
    String EMAIL_PASSWORD = "email-password";

//    /** The config key that defines live email addresses; if empty, then all addresses are live: "email-filter". */
//    String EMAIL_FILTER = "email-filter";

    /** The config key that defines the directory path where email templates are stored. */
    String EMAIL_TEMPLATE_DIRECTORY = "email-template-directory";

    /**
     * The config key that defines file extension used for the email templates in the template directory (e.g., ".html",
     * ".txt", ".tmpl").
     */
    String EMAIL_TEMPLATE_FILE_EXTENSION = "email-template-file-extension";
  }

  // ============================================================
  // MailService
  // ============================================================

  /**
   * Send an email to the given recipient (to), from the given sender (from), with the given subject and body. If
   * {@code emailFilters} is not empty, then email is only sent to the recipients contained within the
   * {@code emailFilters} set (this is often used for testing, but is not restricted to that purpose).
   *
   * @return true if email was sent.
   */
  boolean sendEmailMessage(InternetAddress to, InternetAddress from, String subject, String body, Set<String> emailFilters);

  /**
   * Apply the given {@code substitutionStrategy} to the given email {@code template} and send the email to the
   * {@code recipient} if allowed by the {@code emailFilters}.
   *
   * @return true if email was sent.
   */
  boolean sendEmailMessage(EmailTemplate template, SubstitutionStrategy substitutionStrategy, Set<String> emailFilters);

  /**
   * Create a new email template, to be used by sendEmailMessage. The template directory and file extension are
   * specified by the MailService (loaded from config - see {@link MailServiceInitParamKeys}).
   */
  EmailTemplate createEmailTemplate(String templateName);

  /**
   * Return true if the given emailAddress is allowed to be sent (i.e., if the given email address is contained within
   * the set of emailFilters).
   */
  boolean isSendMailAllowed(Set<String> emailFilters, InternetAddress emailAddress);
}
