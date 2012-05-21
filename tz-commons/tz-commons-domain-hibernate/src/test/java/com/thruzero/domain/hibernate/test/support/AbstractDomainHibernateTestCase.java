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
package com.thruzero.domain.hibernate.test.support;

import com.thruzero.common.core.locator.TransactionMgrLocator;
import com.thruzero.common.core.transaction.DatabaseTransactionMgr;
import com.thruzero.common.core.transaction.TransactionMgr;
import com.thruzero.domain.hibernate.dao.HibernateDAORegistry;
import com.thruzero.domain.hibernate.transaction.HibernateTransactionRegistry;
import com.thruzero.domain.service.impl.DomainServiceRegistry;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Base test class that provides setup and tear-down for the domain-hibernate tests.
 *
 * @author George Norman
 */
public abstract class AbstractDomainHibernateTestCase extends AbstractCoreTestCase {
  private DatabaseTransactionMgr transactionService;

  @Override
  public void setUp() throws Exception {
    super.setUp();

    HibernateTransactionRegistry.getInstance().registerAllInterfaces();
    DomainServiceRegistry.getInstance().registerAllInterfaces(); // register default domain services. The default persistent services use what ever DAOs are registered (see below).
    HibernateDAORegistry.getInstance().registerAllInterfaces();

    transactionService = (DatabaseTransactionMgr)TransactionMgrLocator.locate(TransactionMgr.class);
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();

    transactionService.rollbackTransaction();
    TransactionMgrLocator.reset(); // reset, so that database will be recreated each time

    HibernateTransactionRegistry.getInstance().reset();
    DomainServiceRegistry.getInstance().reset();
    HibernateDAORegistry.getInstance().reset();
  }

  protected DatabaseTransactionMgr getTransactionService() {
    return transactionService;
  }

}
