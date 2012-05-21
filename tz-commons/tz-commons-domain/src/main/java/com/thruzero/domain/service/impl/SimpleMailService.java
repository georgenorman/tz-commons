/*
 *   Copyright 2009 George Norman
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
package com.thruzero.domain.service.impl;

import java.io.File;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;

import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.locator.Initializable;
import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.locator.LocatorUtils;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.strategy.SubstitutionStrategy;
import com.thruzero.common.core.support.LogHelper;
import com.thruzero.domain.service.MailService;
import com.thruzero.domain.support.EmailTemplate;

// http://java.sun.com/products/javamail/downloads/index.html
/**
 * A simple implementation of the MailService.
 *
 * @author George Norman
 */
/**
 * A simple implementation of the MailService.
 *
 * <p>
 * SimpleMailService requires initialization (see {@link SimpleMailService.SimpleMailServiceInitParamKeys} for details).
 * Following is an example initialization using the config file:
 *
 * <pre>
 * {@code
 *   <section name="com.thruzero.domain.service.impl.SimpleMailService">
 *     <entry key="email-enabled" value="false" />
 *     <entry key="email-logging-enabled" value="true" />
 *     <entry key="email-smtp-host" value="mail.thruzero.com" />
 *     <entry key="email-user" value="xxx@thruzero.com" />
 *     <entry key="email-password" value="***" />
 *     <entry key="email-filter" value="" />
 *   </section>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class SimpleMailService implements MailService, Initializable {
  private static SimpleMailServiceLogHelper logHelper = new SimpleMailServiceLogHelper(SimpleMailService.class);

  private boolean emailEnabled;
  private boolean emailLoggingEnabled;

  private String mailHost;
  private String mailUserAccountName;
  private String mailUserAccountPassword;

  private String emailTemplateDir;
  private String emailTemplateFileExtension;

  // ------------------------------------------------
  // SimpleMailServiceInitParamKeys
  // ------------------------------------------------

  /**
   * Initialization parameter keys defined for MailService.
   */
  @InitializationParameterKeysBookmark
  public interface SimpleMailServiceInitParamKeys extends MailServiceInitParamKeys {
    /** The section to use when loading the parameters (e.g., config file section, settings context, etc) */
    String SOURCE_SECTION = SimpleMailService.class.getName();
  }

  // -----------------------------------------------------------
  // SimpleMailServiceLogHelper
  // -----------------------------------------------------------

  public static class SimpleMailServiceLogHelper extends LogHelper {
    public SimpleMailServiceLogHelper(Class<?> clazz) {
      super(clazz);
    }

    public void logInitTemplateDirWarn(final File fsEmailTemplateDir) {
      if (getLogger().isInfoEnabled()) {
        getLogger().warn("* WARNING: Email template directory was specified but does not exist: " + fsEmailTemplateDir.getAbsolutePath());
      }
    }

    protected String logInitHostError(String mailHost, String mailUserAccountName) {
      StringBuilder errMsg = new StringBuilder("* ERROR: " + SimpleMailService.class.getSimpleName() + " failed to initialize because one or more required parameters were not provided:");
      String separator = "";

      if (StringUtils.isEmpty(mailHost)) {
        errMsg.append(" parameter " + SimpleMailServiceInitParamKeys.EMAIL_SMTP_HOST + " is required");
        separator = ",";
      }

      if (StringUtils.isEmpty(mailUserAccountName)) {
        errMsg.append(separator + " parameter " + SimpleMailServiceInitParamKeys.EMAIL_USER + " is required");
        separator = ",";
      }

      errMsg.append(".");

      getLogger().error(errMsg);

      return errMsg.toString();
    }

    public void logEmailWasFiltered(final InternetAddress to) {
      if (getLogger().isInfoEnabled()) {
        getLogger().info("# EMAIL was not sent to '" + to.getAddress() + "' because it was filtered.");
      }
    }

    public void logEmailStatus(final MimeMessage message) throws MessagingException {
      if (getLogger().isInfoEnabled()) {
        getLogger().info("# EMAIL was sent to " + getAllRecipientsAsString(message));
      }
    }

    public void logEmailMessage(final MimeMessage message) throws Exception {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("# EMAIL IS IN DEBUG MODE. No Mail was sent.");
        getLogger().debug("  - Subject: " + message.getSubject());
        getLogger().debug("  - Recipients: " + getAllRecipientsAsString(message) );
        StrBuilder fromAddresses = new StrBuilder();
        String separator = ", ";
        for (Address address : message.getFrom()) {
          fromAddresses.appendSeparator(separator);
          fromAddresses.append(((InternetAddress)address).getPersonal());
        }
        getLogger().debug("  - From: " + fromAddresses);
        getLogger().debug("  - Content: " + message.getContent() );
      }
    }

    private String getAllRecipientsAsString(MimeMessage message) throws MessagingException {
      StrBuilder result = new StrBuilder();
      String separator = ", ";

      for (Address recipient : message.getAllRecipients()) {
        result.appendSeparator(separator).append(recipient);
      }

      return result.toString();
    }
  }

  // --------------------------------------------
  // SimpleMailSender
  // --------------------------------------------

  public class SimpleMailSender {
    public boolean sendMail(InternetAddress to, InternetAddress from, String subject, String body, Set<String> emailFilters) throws Exception {
      boolean emailWasSent = false;

      if (to == null || StringUtils.isEmpty(to.getAddress())) {
        logHelper.getLogger().error("*** Could not send email. The recipient was empty.");
      } else {
        Session mailSession = createSession();

        if (isSendMailAllowed(emailFilters, to)) {
          MimeMessage message = createMimeMessage(mailSession, to, from, subject, body);

          emailWasSent = sendMessage(mailSession, message);
        } else {
          logHelper.logEmailWasFiltered(to);

          if (emailLoggingEnabled) {
            logHelper.logEmailMessage(createMimeMessage(mailSession, to, from, subject, body));
          }
        }

      }

      return emailWasSent;
    }

    private MimeMessage createMimeMessage( Session mailSession, InternetAddress to, InternetAddress from, String subject, String body ) throws Exception {
      MimeMessage message = new MimeMessage(mailSession);

      message.setSubject(subject);
      message.setContent(body, "text/html");
      message.addRecipient(Message.RecipientType.TO, to);
      message.setFrom(from);

      return message;
    }

    protected Session createSession( ) throws Exception {
      Session result;
      Properties props = new Properties();

      props.setProperty(JavaMailPropertyKeys.MAIL_TRANSPORT_PROTOCOL, "smtp");
      props.setProperty(JavaMailPropertyKeys.MAIL_HOST, mailHost);
      props.setProperty(JavaMailPropertyKeys.MAIL_DEBUG, "true");
      props.setProperty(JavaMailPropertyKeys.MAIL_USER, mailUserAccountName);
      props.setProperty(JavaMailPropertyKeys.MAIL_PASSWORD, mailUserAccountPassword);

      result = Session.getDefaultInstance(props, null);

      return result;
    }

    protected boolean sendMessage( Session mailSession, MimeMessage message ) throws Exception {
      boolean emailWasSent = false;

      if (emailEnabled) {
        Transport transport = mailSession.getTransport();
        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
        emailWasSent = true;
        if ( emailLoggingEnabled) {
          logHelper.logEmailStatus(message);
        }
      } else if ( emailLoggingEnabled ) {
        logHelper.logEmailMessage(message);
      }

      return emailWasSent;
    }
  }

  // ===============================================
  // MailService
  // ===============================================

  /**
   * @throws InitializationException if a problem is encountered with the given initParams.
   */
  @Override
  public void init(InitializationStrategy initStrategy) {
    StringMap initParams = LocatorUtils.getInheritedParameters(initStrategy, this.getClass(), MailService.class);

    emailEnabled = initParams.getValueTransformer(SimpleMailServiceInitParamKeys.EMAIL_ENABLED).getBooleanValue(false);
    emailLoggingEnabled = initParams.getValueTransformer(SimpleMailServiceInitParamKeys.EMAIL_LOGGING_ENABLED).getBooleanValue(false);

    mailHost = initParams.get(SimpleMailServiceInitParamKeys.EMAIL_SMTP_HOST);
    mailUserAccountName = initParams.get(SimpleMailServiceInitParamKeys.EMAIL_USER);
    mailUserAccountPassword = initParams.get(SimpleMailServiceInitParamKeys.EMAIL_PASSWORD);

    emailTemplateDir = initParams.getValueTransformer(SimpleMailServiceInitParamKeys.EMAIL_TEMPLATE_DIRECTORY).getStringValue("");
    emailTemplateFileExtension = initParams.getValueTransformer(SimpleMailServiceInitParamKeys.EMAIL_TEMPLATE_FILE_EXTENSION).getStringValue(".txt");

    if (StringUtils.isEmpty(mailHost) || StringUtils.isEmpty(mailUserAccountName)) {
      String errMsg = logHelper.logInitHostError(mailHost, mailUserAccountName);
      throw new InitializationException(errMsg, initStrategy);
    }
  }

  @Override
  public void reset() {
  }

  @Override
  public boolean sendEmailMessage( InternetAddress to, InternetAddress from, String subject, String body, Set<String> emailFilters ) {
    boolean emailWasSent;
    SimpleMailSender simpleMailSender = new SimpleMailSender();

    try {
      emailWasSent = simpleMailSender.sendMail(to, from, subject, body, emailFilters);
    } catch (Exception e) {
      throw new RuntimeException("ERROR: Could not send email.",e);
    }

    return emailWasSent;
  }

  @Override
  public boolean sendEmailMessage( EmailTemplate template, SubstitutionStrategy substitutionStrategy, Set<String> emailFilters ) {
    boolean emailWasSent;
    SimpleMailSender simpleMailSender = new SimpleMailSender();

    try {
      String body = template.evaluateTemplate(substitutionStrategy);

      emailWasSent = simpleMailSender.sendMail(template.getTo(),template.getFrom(),template.getSubject(), body, emailFilters);
    } catch (Exception e) {
      throw new RuntimeException("ERROR: Could not send email.",e);
    }

    return emailWasSent;
  }

  @Override
  public EmailTemplate createEmailTemplate(String templateName) {
    File fsEmailTemplateDir = null;

    if (StringUtils.isNotEmpty(emailTemplateDir)) {
      fsEmailTemplateDir = new File(emailTemplateDir);
      if (!fsEmailTemplateDir.exists()) {
        logHelper.logInitTemplateDirWarn(fsEmailTemplateDir);
        fsEmailTemplateDir = null;
      }
    }

    if (fsEmailTemplateDir == null) {
      throw new RuntimeException("* ERROR: Could not create email template because the email template directory does not exist:" + emailTemplateDir);
    }

    return new EmailTemplate(fsEmailTemplateDir, templateName, emailTemplateFileExtension);
  }

  /**
   * @param filters set of recipients that can receive notifications
   * @return true if the recipient can receive an email
   */
  @Override
  public boolean isSendMailAllowed(Set<String> filters, InternetAddress to) {
    return filters == null || filters.isEmpty() || filters.contains( to.getAddress().toLowerCase() );
  }

}
