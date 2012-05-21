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
package com.thruzero.domain.dsc.test.support;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;

import com.thruzero.common.core.fs.HierarchicalFileWalker;
import com.thruzero.common.core.fs.walker.visitor.DirectoryDeletingVisitor;
import com.thruzero.common.core.locator.ConfigInitializationStrategy;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.domain.dao.DAO;
import com.thruzero.domain.dsc.dao.AbstractDataStoreDAO.DataStoreDAOInitParamKeys;
import com.thruzero.domain.dsc.dao.DscDAORegistry;
import com.thruzero.domain.dsc.fs.FileDataStoreContainerFactory;
import com.thruzero.domain.service.impl.DomainServiceRegistry;
import com.thruzero.domain.store.BaseStorePath;
import com.thruzero.test.support.AbstractCoreTestCase;

/**
 * Base test class that provides setup and tear-down for the domain-dsc tests.
 *
 * @author George Norman
 */
public abstract class AbstractDomainDscTestCase<T extends DAO> extends AbstractCoreTestCase {
  public static final String DEFAULT_TEMP_DIR_NAME = "temp";

  private Class<? extends DAO> daoClass;

  protected AbstractDomainDscTestCase(Class<T> daoClass) {
    this.daoClass = daoClass;
  }

  @Override
  @Before
  public void setUp() throws Exception {
    super.setUp();

    DomainServiceRegistry.getInstance().registerAllInterfaces(); // register default domain services. The default persistent services use what ever DAOs are registered (see below).
    DscDAORegistry.getInstance().registerAllInterfaces();

    // if this is a file-system based DSC, then create the data-store directory
    BaseStorePath baseStorePath = getBaseStorePath();

    if (baseStorePath != null) {
      File startDir = new File(baseStorePath.toString());
      startDir.mkdirs();
    }
  }

  @Override
  @After
  public void tearDown() throws Exception {

    try {
      // if this is a file-system based DSC, then delete the contents of the data-store directory
      BaseStorePath baseStorePath = getBaseStorePath();

      if (baseStorePath != null) {
        File startDir = new File(baseStorePath.toString());

        if (startDir.exists()) {
          HierarchicalFileWalker walker = new HierarchicalFileWalker(startDir);
          walker.accept(new DirectoryDeletingVisitor());
        }
      }
    } catch (IOException e) {
      fail("DirectoryDeletingVisitor generated exception: " + e);
    }

    super.tearDown();

    DomainServiceRegistry.getInstance().reset();
    DscDAORegistry.getInstance().reset();
  }

  /**
   * If this is a File-style based DSC, then return the absolute base path (so the test case can set up or tear down the directory).
   */
  protected BaseStorePath getBaseStorePath() {
    BaseStorePath result = null;
    InitializationStrategy daoInitStrategy = new ConfigInitializationStrategy();
    StringMap daoParams = daoInitStrategy.getSectionAsStringMap(daoClass.getName());

    if (daoParams == null || daoParams.isEmpty()) {
      throw new RuntimeException("ERROR: Mising config section: '" + daoClass.getName() + "'.");
    }

    String dataStoreContainerFactoryClassName = daoParams.get(DataStoreDAOInitParamKeys.STORE_CONTAINER_FACTORY);
    if (StringUtils.isEmpty(dataStoreContainerFactoryClassName)) {
      throw new RuntimeException("ERROR: dataStoreContainerFactoryClassName not specified in config section: '" + daoClass.getName() + "'.");
    }

    if (FileDataStoreContainerFactory.class.getName().equals(dataStoreContainerFactoryClassName)) {
      FileDataStoreContainerFactory dscFactory = new FileDataStoreContainerFactory();
      dscFactory.init(daoInitStrategy, daoClass.getName());

      result = dscFactory.getBaseStorePath();
      if (result == null) {
        throw new RuntimeException("ERROR: Base store path for '" + FileDataStoreContainerFactory.class.getName() + "' was not found.");
      }
    }

    return result;
  }

}
