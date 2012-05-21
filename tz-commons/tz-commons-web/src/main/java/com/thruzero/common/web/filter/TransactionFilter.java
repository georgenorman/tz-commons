/*
 *   Copyright 2011-2012 George Norman
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
package com.thruzero.common.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.thruzero.common.core.locator.TransactionMgrLocator;
import com.thruzero.common.core.transaction.TransactionMgr;

/**
 * A simple implementation of the lazy transaction-per-request pattern - actual creation of a transaction is deferred
 * until (or if) requested by the Domain layer. It will be committed or rolled back only if it is active (i.e., if no
 * transaction is requested by the Domain layer, then there is nothing to commit or roll back).
 *
 * @author George Norman
 */
public class TransactionFilter implements Filter {
  private TransactionMgr transactionMgr;

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (transactionMgr == null) {
      transactionMgr = TransactionMgrLocator.locate(TransactionMgr.class);
    }

    try {
      // transaction is automatically created on first request of TransactionService.getCurrentSession()
      chain.doFilter(request, response);

      // transaction is committed, if active and requested (otherwise, rollback)
      transactionMgr.commitOrRollbackTransaction();
    } catch (Exception e) {
      // transaction is rolled-back, if active
      transactionMgr.rollbackTransaction();
      throw new ServletException("TransactionFilter encountered and Exception: ", e);
    }
  }

  @Override
  public void destroy() {
  }

}
