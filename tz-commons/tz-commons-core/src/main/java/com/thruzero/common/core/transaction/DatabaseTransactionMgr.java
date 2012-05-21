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

/**
 * An extension of the TransactionMgr that's specific to databases and introduces the notion of a database session for
 * CRUD operations. An implementation of this can be used as a wrapper around a particular type of transaction/session
 * provider (e.g., Hibernate SessionFactory).
 * <p>
 * An implementation of this interface is used by the {@link com.thruzero.common.web.filter.TransactionFilter
 * TransactionFilter} to manage transactions via a web application's request cycle. The TransactionFilter does not open
 * a transaction until {@link com.thruzero.common.core.transaction.DatabaseTransactionMgr#getCurrentSession()} is
 * called, to minimize the time a transaction is open as well as to prevent a transaction from being opened if it isn't
 * needed.
 * <p>
 * Note: Thus far, I've only used this with JPA and Hibernate, so I'm not sure how well it will translate to other ORM
 * libraries or NoSQL databases.
 *
 * @author George Norman
 */
public interface DatabaseTransactionMgr extends TransactionMgr {

  /**
   * Open a transaction, if not already open, and return the underlying persistence management instance to be used for
   * CRUD operations (e.g., Hibernate Session, JPA EntityManager, etc).
   */
  Object getCurrentPersistenceManager();

}
