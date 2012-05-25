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
package com.thruzero.domain.dsc.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;

import org.junit.Test;

import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.dsc.test.support.AbstractDomainDscTestCase;
import com.thruzero.domain.locator.DAOLocator;
import com.thruzero.domain.model.TextEnvelope;
import com.thruzero.domain.store.EntityPath;
import com.thruzero.domain.test.support.dao.AbstractTextEnvelopeDAOTestHelper;
import com.thruzero.domain.test.support.dao.TextEnvelopeTestBuilder;

/**
 *
 * @author George Norman
 */
public class DscTextEnvelopeDAOTest extends AbstractDomainDscTestCase<DscTextEnvelopeDAO> {
  private static final AbstractTextEnvelopeDAOTestHelper testHelper = new DscTextEnvelopeDAOTestHelper();

  // ------------------------------------------------------
  // DscTextEnvelopeDAOTestHelper
  // ------------------------------------------------------

  public static class DscTextEnvelopeDAOTestHelper extends AbstractTextEnvelopeDAOTestHelper {

    protected DscTextEnvelopeDAOTestHelper() {
      super(new TextEnvelopeTestBuilder() {
        @Override
        protected Serializable createPrimaryKeyFrom(final TextEnvelope textEnvelope) {
          EntityPath result = (EntityPath)textEnvelope.getEntityPath().clone();

          return result;
        }
      });
    }

    @Override
    protected void beginTransaction() {
    }

    @Override
    protected void commitTransaction() {
    }
  }

  // ============================================================================
  // DscTextEnvelopeDAOTest
  // ============================================================================

  public DscTextEnvelopeDAOTest() {
    super(DscTextEnvelopeDAO.class);
  }

  @Test
  public void testLocateDAO() {
    TextEnvelopeDAO dao = DAOLocator.locate(TextEnvelopeDAO.class);

    assertNotNull(dao);
    assertTrue(dao.getClass().equals(DscTextEnvelopeDAO.class)); // make sure the locator is configured properly
  }

  @Test
  public void testGetNonExistantNode() {
    testHelper.doTestGetNonExistantNode(DAOLocator.locate(TextEnvelopeDAO.class));
  }

  @Test
  public void testSaveNewNode() {
    testHelper.doTestSaveNewNode(DAOLocator.locate(TextEnvelopeDAO.class));
  }

  @Test
  public void testUpdatePersistedNode() {
    testHelper.doTestUpdatePersistedNode(DAOLocator.locate(TextEnvelopeDAO.class));
  }

  @Test
  public void testSaveOrUpdatePersistedNode() {
    testHelper.doTestSaveOrUpdatePersistedNode(DAOLocator.locate(TextEnvelopeDAO.class));
  }

  @Test
  public void testDeletePersistedNode() {
    testHelper.doTestDeletePersistedNode(DAOLocator.locate(TextEnvelopeDAO.class));
  }

  @Test
  public void testIsExistingTextEnvelope() {
    testHelper.doTestIsExistingTextEnvelope(DAOLocator.locate(TextEnvelopeDAO.class));
  }

}
