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
package com.thruzero.domain.test.support;

import java.net.ServerSocket;

import org.junit.After;
import org.junit.Before;

import com.dumbster.smtp.SimpleSmtpServer;

/**
 * Abstract test class that starts and stops the mock SMTP mail server (used for testing mail-related classes).
 * 
 * @author George Norman
 */
public abstract class AbstractMailTestCase extends AbstractDomainTestCase {
  private SimpleSmtpServer server;

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    try {
      // Dumbster throws exception, without stopping server and then barfs if stop is called.
      // Do a pre-test before calling dumbster.
      new ServerSocket(SimpleSmtpServer.DEFAULT_SMTP_PORT);

      // okay to start
      server = SimpleSmtpServer.start();
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  @Override
  @After
  public void tearDown() throws Exception {
    super.tearDown();

    if (server != null) {
      server.stop();
    }
  }

  protected SimpleSmtpServer getSmtpServer() {
    return server;
  }

}
