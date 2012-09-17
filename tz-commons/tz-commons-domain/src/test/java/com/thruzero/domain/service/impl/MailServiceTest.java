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
package com.thruzero.domain.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.junit.Test;

import com.dumbster.smtp.SmtpMessage;
import com.thruzero.common.core.config.Config;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.strategy.MapSubstitutionStrategy;
import com.thruzero.domain.service.MailService;
import com.thruzero.domain.support.EmailTemplate;
import com.thruzero.domain.test.support.AbstractMailTestCase;

/**
 *
 * @author George Norman
 */
public class MailServiceTest extends AbstractMailTestCase {
  public static final String DISABLE_EMAIL_TEST = "DISABLE_EMAIL_TEST";

  @Test
  public void simpleMailTest() throws UnsupportedEncodingException, AddressException {
    if (!isEmailTestDisabled()) {
      MailService mailService = ServiceLocator.locate(MailService.class);
      InternetAddress to = new InternetAddress("testy@thruzero.com");
      InternetAddress from = new InternetAddress("testx@thruzero.com", "testx@thruzero.com");
      String subject = "Test Subject";
      String body = "This is a test email";

      boolean emailWasSent = mailService.sendEmailMessage(to, from, subject, body, null);

      assertTrue(emailWasSent);
      assertTrue(getSmtpServer().getReceivedEmailSize() == 1);
      @SuppressWarnings("unchecked") // dumbster isn't generic
      Iterator<SmtpMessage> emailIter = getSmtpServer().getReceivedEmail();
      SmtpMessage email = emailIter.next();
      assertTrue(email.getHeaderValue("Subject").equals("Test Subject"));
      assertTrue(email.getBody().equals("This is a test email"));
    }
  }

  @Test
  public void simpleMailFilterTest1() throws UnsupportedEncodingException, AddressException {
    if (!isEmailTestDisabled()) {
      MailService mailService = ServiceLocator.locate(MailService.class);
      InternetAddress to = new InternetAddress("testy@thruzero.com");
      InternetAddress from = new InternetAddress("testx@thruzero.com", "testx@thruzero.com");
      String subject = "Test Subject";
      String body = "This is a test email";
      Set<String> filters = new HashSet<String>();
      filters.add("testy@thruzero.com");

      boolean emailWasSent = mailService.sendEmailMessage(to, from, subject, body, filters);

      assertTrue(emailWasSent);
      assertTrue(getSmtpServer().getReceivedEmailSize() == 1);
      @SuppressWarnings("unchecked") // dumbster isn't generic
      Iterator<SmtpMessage> emailIter = getSmtpServer().getReceivedEmail();
      SmtpMessage email = emailIter.next();
      assertTrue(email.getHeaderValue("Subject").equals("Test Subject"));
      assertTrue(email.getBody().equals("This is a test email"));
    }
  }

  @Test
  public void simpleMailFilterTest2() throws UnsupportedEncodingException, AddressException {
    MailService mailService = ServiceLocator.locate(MailService.class);
    InternetAddress to = new InternetAddress("testy@thruzero.com");
    InternetAddress from = new InternetAddress("testx@thruzero.com", "testx@thruzero.com");
    String subject = "Test Subject";
    String body = "This is a test email";
    Set<String> filters = new HashSet<String>();
    filters.add("testz@thruzero.com");

    boolean emailWasSent = mailService.sendEmailMessage(to, from, subject, body, filters);

    assertFalse(emailWasSent);
  }

  @Test
  public void templateMailTest1() throws UnsupportedEncodingException, AddressException {
    MailService mailService = ServiceLocator.locate(MailService.class);
    EmailTemplate emailTemplate = mailService.createEmailTemplate("email-template-1");

    StringMap subs = new StringMap("param1|val1", "key2|val2");
    mailService.sendEmailMessage(emailTemplate, new MapSubstitutionStrategy(subs), null);

  }

  @Test
  public void templateMailTest2() throws UnsupportedEncodingException, AddressException {
    if (!isEmailTestDisabled()) {
      MailService mailService = ServiceLocator.locate(MailService.class);
      InternetAddress to = new InternetAddress("testy@thruzero.com");
      InternetAddress from = new InternetAddress("testx@thruzero.com", "testx@thruzero.com");
      String subject = "Test Subject";
      String body = "This is a test email";

      boolean emailWasSent = mailService.sendEmailMessage(to, from, subject, body, null);
    }
  }

  public static boolean isEmailTestDisabled() {
    Config config = ConfigLocator.locate();

    return config.getBooleanValue(SimpleMailService.class.getName(), DISABLE_EMAIL_TEST, true);
  }
}
