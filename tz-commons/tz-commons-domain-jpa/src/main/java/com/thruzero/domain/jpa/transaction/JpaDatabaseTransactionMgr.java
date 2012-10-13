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
package com.thruzero.domain.jpa.transaction;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.ejb.AvailableSettings;

import com.thruzero.common.core.bookmarks.InitializationParameterKeysBookmark;
import com.thruzero.common.core.locator.Initializable;
import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.locator.LocatorUtils;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.security.SimpleCipher;
import com.thruzero.common.core.security.SimpleCipher.SimpleCipherException;
import com.thruzero.common.core.support.LogHelper;
import com.thruzero.common.core.transaction.DatabaseTransactionMgr;
import com.thruzero.common.core.utils.ExceptionUtilsExt;

/**
 * An instance of this class provides the single point of access for creating an EntityManager.
 * <p>
 * Note: Use {@link com.thruzero.common.core.locator.TransactionMgrLocator TransactionMgrLocator} to locate the
 * configured instance of DatabaseTransactionMgr.
 *
 * <p>
 * JpaDatabaseTransactionMgr requires initialization (see {@link paDatabaseTransactionMgr.JpaDatabaseTransactionMgrInitParamKeys} for details).
 * Following is an example initialization using the config file:
 *
 * <pre>
 * {@code
 *   <section name="com.thruzero.domain.jpa.transaction.JpaDatabaseTransactionMgr">
 *     <entry key="persistence-unit.name" value="tz-commons-domain-manager" />
 *
 *     <entry key="javax.persistence.jdbc.user" value="testy" />
 *     <entry key="javax.persistence.jdbc.password" value="GsXez0K6dd8" />
 *     <entry key="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver" />
 *     <entry key="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/testy_schema" />
 *     <entry key="hibernate.dialect" value="org.hibernate.dialect.HSQLDialect" />
 *     <entry key="hibernate.max_fetch_depth" value="3" />
 *     <entry key="hibernate.show_sql" value="true" />
 *     <entry key="hibernate.format_sql" value="true" />
 *   </section>
 * }
 * </pre>
 *
 * @author George Norman
 */
public final class JpaDatabaseTransactionMgr implements DatabaseTransactionMgr, Initializable {
  private static TransactionMgrLogHelper transactionMgrLogHelper = new TransactionMgrLogHelper(JpaDatabaseTransactionMgr.class);

  private static ThreadLocal<JpaTransactionState> localTransactionState = new ThreadLocal<JpaTransactionState>() {
    @Override
    protected JpaTransactionState initialValue() {
      return new JpaTransactionState();
    }
  };
  private static EntityManagerFactory entityManagerFactory;

  private static String persistenceUnitName;
  private static StringMap cachedParams; // for lazy init

  // ------------------------------------------------
  // JpaDatabaseTransactionMgrInitParamKeys
  // ------------------------------------------------

  /**
   * Initialization parameter keys defined for JpaDatabaseTransactionMgr.
   */
  @InitializationParameterKeysBookmark
  public interface JpaDatabaseTransactionMgrInitParamKeys extends InitializableParameterKeys {
    /** The section to use when loading the parameters (e.g., config file section, settings context, etc) */
    String SOURCE_SECTION = JpaDatabaseTransactionMgr.class.getName();

    /** the key that defines the name of the persistence unit to be created */
    String PERSISTENCE_UNIT_NAME = "persistence-unit.name";

    String JDBC_PASSWORD = AvailableSettings.JDBC_PASSWORD;
  }

  // -----------------------------------------------------------
  // TransactionMgrLogHelper
  // -----------------------------------------------------------

  public static final class TransactionMgrLogHelper extends LogHelper {
    public TransactionMgrLogHelper(Class<?> clazz) {
      super(clazz);
    }

    public void logEntityManagerNotClosedWarning() {
      getLogger().warn(ExceptionUtilsExt.decorateMessageLevel1("ERROR: Transaction is not active, yet EntityManager is OPEN. The EntityManager was never closed - Closing it now."));
    }

    public void logCreatingEntityManagerFactory() {
      if (getLogger().isDebugEnabled()) {
        getLogger().debug("Creating EntityManagerFactory.");
      }
    }
  }

  // ------------------------------------------------
  // JpaTransactionState
  // ------------------------------------------------

  /**
   * An instance of this class represents the currently active EntityManager. If getEntityManager() has not been called,
   * then persistence framework will not need to be accessed or initialized, in order to determine if an active
   * transaction is present. Initialization entails loading the config file, the mapping files (or annotations),
   * building the EntityManager factory, etc. This class enables deferring that cost until actually needed (or if
   * needed). For example, one of the test apps built on top of this framework has a database component. If that
   * component is never accessed, then the database-related code is never initialized.
   */
  public static class JpaTransactionState {
    private EntityManager entityManager;
    private boolean commitRequested;

    public boolean isTransactionActive() {
      return entityManager != null && entityManager.isOpen() && entityManager.getTransaction().isActive();
    }

    public EntityManager getEntityManager() {
      return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
      this.entityManager = entityManager;
    }

    public boolean isCommitRequested() {
      return commitRequested;
    }

    public void setCommitRequested(boolean commitRequested) {
      this.commitRequested = commitRequested;
    }
  }

  // =================================================================
  // JpaDatabaseTransactionMgr
  // =================================================================

  /**
   * Use com.thruzero.common.core.locator.TransactionMgrLocator to access a particular TransactionMgr.
   */
  private JpaDatabaseTransactionMgr() {
  }

  /**
   * @throws InitializationException if a problem is encountered with the given initParams.
   */
  @Override
  public void init(InitializationStrategy initStrategy) {
    synchronized (JpaDatabaseTransactionMgr.class) {
      StringMap initParams = LocatorUtils.getInheritedParameters(initStrategy, this.getClass(), DatabaseTransactionMgr.class);

      persistenceUnitName = LocatorUtils.getRequiredParam(initParams, this.getClass().getName(), JpaDatabaseTransactionMgrInitParamKeys.PERSISTENCE_UNIT_NAME, initStrategy);

      String jdbcPassword = initParams.get(JpaDatabaseTransactionMgrInitParamKeys.JDBC_PASSWORD);
      if ( StringUtils.isNotEmpty(jdbcPassword) ) {
        try {
          SimpleCipher cipher = new SimpleCipher();

          initParams.put(JpaDatabaseTransactionMgrInitParamKeys.JDBC_PASSWORD, cipher.decrypt(jdbcPassword));
        } catch (SimpleCipherException e) {
          throw new InitializationException("Exception while attempting to decode JDBC password: ", e, initStrategy);
        }
      }

      cachedParams = (StringMap)initParams.clone();
    }
  }

  @Override
  public void reset() {
    synchronized (JpaDatabaseTransactionMgr.class) {
      entityManagerFactory = null;
      persistenceUnitName = null;

      if (cachedParams != null) {
        cachedParams.clear();
      }
    }
  }

  @Override
  public boolean isTransactionActive() {
    return localTransactionState.get().isTransactionActive();
  }

  @Override
  public EntityManager getCurrentPersistenceManager() {
    return doGetCurrentPersistenceManager(true);
  }

  protected EntityManager doGetCurrentPersistenceManager(boolean autoCreateTransaction) {
    JpaTransactionState txState = localTransactionState.get();
    EntityManager currentEntityManager = txState.getEntityManager();

    if (autoCreateTransaction && !isTransactionActive()) {
      if (currentEntityManager != null && currentEntityManager.isOpen()) {
        transactionMgrLogHelper.logEntityManagerNotClosedWarning();
        currentEntityManager.close();
      }

      // begins a transaction if not currently active
      currentEntityManager = createEntityManager();
      if (!isTransactionActive()) {
        currentEntityManager.getTransaction().begin();
      }

      txState.setEntityManager(currentEntityManager);
    }

    return currentEntityManager;
  }

  protected static synchronized EntityManager createEntityManager() { // TODO-p1(george) static synchronized bottleneck
    if (entityManagerFactory == null) {
      transactionMgrLogHelper.logCreatingEntityManagerFactory();
      entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName, cachedParams);
    }

    return entityManagerFactory.createEntityManager();
  }

  @Override
  public EntityTransaction beginTransaction() {
    EntityManager currentEntityManager = doGetCurrentPersistenceManager(true);

    return currentEntityManager.getTransaction();
  }

  @Override
  public void commitTransaction() {
    if (isTransactionActive()) {
      EntityManager entityManager = doGetCurrentPersistenceManager(false);

      entityManager.getTransaction().commit();

      if (entityManager.isOpen()) {
        entityManager.close();
      }
    }
  }

  @Override
  public void rollbackTransaction() {
    if (isTransactionActive()) {
      EntityManager entityManager = doGetCurrentPersistenceManager(false);

      entityManager.getTransaction().rollback();

      if (entityManager.isOpen()) {
        entityManager.close();
      }
    }
  }

  @Override
  public void setCommitRequested(boolean commitRequested) {
    JpaTransactionState txState = localTransactionState.get();

    txState.setCommitRequested(commitRequested);
  }

  @Override
  public void commitOrRollbackTransaction() {
    JpaTransactionState txState = localTransactionState.get();

    if (txState.isCommitRequested()) {
      commitTransaction();
    } else {
      rollbackTransaction();
    }
  }

}
