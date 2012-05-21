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
package com.thruzero.common.core.transaction;

import com.thruzero.common.core.support.Singleton;

/**
 * A simple transaction API that allows clients to begin a <i>unit of work</i> and then commit or roll it back.
 *
 * @author George Norman
 */
public interface TransactionMgr extends Singleton {

  boolean isTransactionActive();

  /** Begin a unit of work and return the implementation-specific transaction object. */
  Object beginTransaction();

  /** Commit the transaction, ending the unit of work. */
  void commitTransaction();

  /** Rollback the transaction, ending the unit of work. */
  void rollbackTransaction();

  /**
   * An optional flag used to track if a commit or rollback should be performed at the end of a transaction cycle. For
   * example, a filter using the transaction-per-request pattern needs to know if a commit or rollback is required at
   * the end of the request. A rollback happens automatically when an exception is thrown, but a rollback may
   * also be desired if save, update or delete was never called. When an application uses a service that calls
   * save, update or delete, this flag is set to true (by the DAO).
   */
  void setCommitRequested(boolean commitRequested);

  /**
   * Works in concert with the setCommitRequested function and is an optional feature of TransactionMgr added to support
   * the transaction-per-request pattern. When commitOrRollbackTransaction is called, it will commit the transaction
   * if setCommitRequested(true) was called during an active transaction, otherwise, it will roll back the transaction.
   */
  void commitOrRollbackTransaction();

}
