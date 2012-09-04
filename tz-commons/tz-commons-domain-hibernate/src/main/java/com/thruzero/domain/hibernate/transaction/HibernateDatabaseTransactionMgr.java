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
package com.thruzero.domain.hibernate.transaction;

import java.io.File;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.locator.Initializable;
import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.locator.LocatorUtils;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.security.SimpleCipher;
import com.thruzero.common.core.transaction.DatabaseTransactionMgr;

/**
 * An instance of this class provides the single access for creating a Hibernate Session, committing it or rolling it
 * back.
 * <p>
 * Note: Use {@link com.thruzero.common.core.locator.TransactionMgrLocator TransactionMgrLocator} to locate the
 * configured instance of DatabaseTransactionMgr.
 *
 * @author George Norman
 */
public final class HibernateDatabaseTransactionMgr implements DatabaseTransactionMgr, Initializable {
  private static final Logger logger = Logger.getLogger(HibernateDatabaseTransactionMgr.class);

  private static Configuration hibernateConfiguration; // for lazy init
  private static SessionFactory sessionFactory;
  private static ThreadLocal<HibernateTransactionState> localTransactionState = new ThreadLocal<HibernateTransactionState>() {
    @Override
    protected HibernateTransactionState initialValue() {
      return new HibernateTransactionState();
    }
  };

  private enum TransactionAction {
    ROLLBACK, COMMIT
  };

  // ------------------------------------------------
  // HibernateDatabaseTransactionMgrInitParamKeys
  // ------------------------------------------------

  /**
   * Initialization parameter keys defined for HibernateDatabaseTransactionMgr.
   */
  @InitializationParameterKeysBookmark
  public interface HibernateDatabaseTransactionMgrInitParamKeys extends InitializableParameterKeys {
    /** The section to use when loading the parameters (e.g., config file section, settings context, etc) */
    String SOURCE_SECTION = HibernateDatabaseTransactionMgr.class.getName();

    /** The config key that defines the name of the hibernate config file. */
    String HIBERNATE_CONFIG_FILE_URI = "hibernateConfigFileUri";
  }

  // ------------------------------------------------
  // HibernateTransactionState
  // ------------------------------------------------

  /**
   * An instance of this class represents a true/false flag for whether getCurrentSession() has been called. If
   * getCurrentSession() has not been called, then Hibernate will not need to be accessed or initialized, in order to
   * determine if an active transaction is present. Initialization entails loading the config file, the mapping files,
   * building the session factory, etc. This class enables deferring that cost until needed (or if needed).
   */
  public static class HibernateTransactionState {
    private Session session;
    private boolean commitRequested;

    public boolean isTransactionActive() {
      return session != null && session.isOpen() && session.getTransaction().isActive();
    }

    public Session getSession() {
      return session;
    }

    public void setSession(Session session) {
      this.session = session;
    }

    public boolean isCommitRequested() {
      return commitRequested;
    }

    public void setCommitRequested(boolean commitRequested) {
      this.commitRequested = commitRequested;
    }
  }

  // =================================================================
  // HibernateDatabaseTransactionMgr
  // =================================================================

  /**
   * Use com.thruzero.common.core.locator.TransactionMgrLocator to access a particular TransactionMgr.
   */
  private HibernateDatabaseTransactionMgr() {
  }

  /**
   * @throws InitializationException if a problem is encountered with the given initParams.
   */
  @Override
  public void init(InitializationStrategy initStrategy) {
    synchronized (HibernateDatabaseTransactionMgr.class) {
      StringMap initParams = LocatorUtils.getInheritedParameters(initStrategy, this.getClass(), DatabaseTransactionMgr.class);

      try {
        // Create the hibernate Configuration from hibernate.cfg.xml
        String hibernateConfigFileUri = LocatorUtils.getRequiredParam(initParams, this.getClass().getName(), HibernateDatabaseTransactionMgrInitParamKeys.HIBERNATE_CONFIG_FILE_URI, initStrategy);
        File hibernateConfigFile = new File(hibernateConfigFileUri);

        // initialize hibernate config
        if (hibernateConfigFile.exists()) {
          hibernateConfiguration = new Configuration().configure(hibernateConfigFile);
        } else {
          hibernateConfiguration = new Configuration().configure(hibernateConfigFileUri);
        }

        // http://docs.jboss.org/hibernate/core/3.3/reference/en/html/session-configuration.html
        String pw = hibernateConfiguration.getProperty("hibernate.connection.password");
        if (StringUtils.isNotEmpty(pw)) {
          SimpleCipher cipher = new SimpleCipher();

          hibernateConfiguration.setProperty("hibernate.connection.password", cipher.decrypt(pw));
        }
      } catch (Throwable ex) {
        String msg = "* ERROR: Hibernate Configuration creation failed.";
        logger.error(msg, ex);
        throw new InitializationException(msg, ex, initStrategy);
      }
    }
  }

  @Override
  public void reset() {
    synchronized (HibernateDatabaseTransactionMgr.class) {
      sessionFactory = null;

      if (hibernateConfiguration != null) {
        hibernateConfiguration = null;
      }
    }
  }

  @Override
  public Session getCurrentPersistenceManager() {
    return doGetCurrentPersistenceManager(true);
  }

  protected Session doGetCurrentPersistenceManager(boolean autoCreateTransaction) {
    HibernateTransactionState txState = localTransactionState.get();
    Session currentSession = txState.getSession();

    if (autoCreateTransaction && !isTransactionActive()) {
      if (currentSession != null && currentSession.isOpen()) {
        logger.error("* ERROR: Transaction is not active, yet Session is OPEN. The Session was never closed - Closing it now.");
        currentSession.close();
      }

      // begins a transaction if not currently active
      currentSession = createSession();
      txState.setSession(currentSession);
    }

    return currentSession;
  }

  protected static synchronized SessionFactory ensureSessionFactory() { // TODO-p1(george) static synchronized bottleneck
    if (sessionFactory == null) {
      try {
        // Create the SessionFactory from hibernate Configuration
        if (logger.isDebugEnabled()) {
          logger.debug("Building SessionFactory from hibernate Configuration");
        }
        sessionFactory = hibernateConfiguration.buildSessionFactory(); // TODO-p1(george) c3p0 error
      } catch (Throwable ex) {
        String msg = "* ERROR: Hibernate SessionFactory creation failed.";
        logger.error(msg, ex);
        throw new RuntimeException(msg, ex);
      }
    }

    return sessionFactory;
  }

  /**
   * Begin a transaction, if not already started.
   */
  protected Session createSession() {
    Session result = ensureSessionFactory().getCurrentSession();

    // begin transaction, if not already started
    if (!result.getTransaction().isActive()) {
      result.beginTransaction();
    }

    return result;
  }

  public static Statistics getStatistics() {
    Statistics result = ensureSessionFactory().getStatistics();

    return result;
  }

  @Override
  public boolean isTransactionActive() {
    return localTransactionState.get().isTransactionActive();
  }

  @Override
  public Transaction beginTransaction() {
    Session currentSession = doGetCurrentPersistenceManager(true);

    return currentSession.getTransaction();
  }

  @Override
  public void commitTransaction() {
    doRollbackOrCommitTransaction(TransactionAction.COMMIT);
  }

  @Override
  public void rollbackTransaction() {
    doRollbackOrCommitTransaction(TransactionAction.ROLLBACK);
  }

  private void doRollbackOrCommitTransaction(TransactionAction action) {
    if (isTransactionActive()) {

      Session hibernateSession = doGetCurrentPersistenceManager(false);
      if (action == TransactionAction.ROLLBACK) {
        hibernateSession.getTransaction().rollback();
      } else {
        hibernateSession.getTransaction().commit();
      }

      if (hibernateSession.isOpen()) {
        hibernateSession.close();
      }
    }
  }

  @Override
  public void setCommitRequested(boolean commitRequested) {
    HibernateTransactionState txState = localTransactionState.get();

    txState.setCommitRequested(commitRequested);
  }

  @Override
  public void commitOrRollbackTransaction() {
    HibernateTransactionState txState = localTransactionState.get();

    if (txState.isCommitRequested()) {
      commitTransaction();
    } else {
      rollbackTransaction();
    }
  }

}
