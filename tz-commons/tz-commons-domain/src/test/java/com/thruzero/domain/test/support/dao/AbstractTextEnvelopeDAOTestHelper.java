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
package com.thruzero.domain.test.support.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.thruzero.domain.dao.TextEnvelopeDAO;
import com.thruzero.domain.model.TextEnvelope;
import com.thruzero.domain.test.support.dao.TextEnvelopeTestBuilder.TextEnvelopeTestConst;

/**
 *
 * @author George Norman
 */
public abstract class AbstractTextEnvelopeDAOTestHelper {
  private TextEnvelopeTestBuilder textEnvelopeTestBuilder;

  protected AbstractTextEnvelopeDAOTestHelper(TextEnvelopeTestBuilder textEnvelopeTestBuilder) {
    this.textEnvelopeTestBuilder = textEnvelopeTestBuilder;
  }

  public void doTestGetNonExistantNode(TextEnvelopeDAO dao) {
    for (int i = 1; i > 0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          TextEnvelope textEnvelope = dao.getTextEnvelope(textEnvelopeTestBuilder.createNodePathOne());
          assertNull(textEnvelope);
        }
          break;
        default:
          i = -1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestSaveNewNode(TextEnvelopeDAO dao) {
    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          TextEnvelope textEnvelope = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertNull(textEnvelope);
        }
          break;
        case 2: {
          TextEnvelope testNode = (textEnvelopeTestBuilder.createTestTextEnvelopeTwo());
          assertNull(testNode.getId());
          dao.save(testNode); // no errors expected
          assertNotNull(testNode.getId());
        }
          break;
        case 3: {
          TextEnvelope testNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertEquals(TextEnvelopeTestConst.TEST_TWO_NODE_DATA, testNode.getData());
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestDeletePersistedNode(TextEnvelopeDAO dao) {
    TextEnvelope persistedNode = null;

    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          TextEnvelope newNode = (textEnvelopeTestBuilder.createTestTextEnvelopeTwo());
          dao.save(newNode);
        }
          break;
        case 2: {
          persistedNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertTrue(persistedNode != null);
        }
          break;
        case 3: {
          dao.delete(persistedNode);
        }
          break;
        case 4: {
          persistedNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertTrue(persistedNode == null);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestUpdatePersistedNode(TextEnvelopeDAO dao) {
    TextEnvelope persistedNode = null;

    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          TextEnvelope newNode = (textEnvelopeTestBuilder.createTestTextEnvelopeTwo());
          dao.save(newNode);
        }
          break;
        case 2: {
          persistedNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertTrue(persistedNode != null);
        }
          break;
        case 3: {
          persistedNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          persistedNode.setData(TextEnvelopeTestConst.TEST_ONE_NODE_DATA);
          dao.update(persistedNode);
        }
          break;
        case 4: {
          persistedNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertNotNull(persistedNode);
          assertEquals(TextEnvelopeTestConst.TEST_ONE_NODE_DATA, persistedNode.getData());
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestSaveOrUpdatePersistedNode(TextEnvelopeDAO dao) {
    TextEnvelope persistedNode = null;

    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          TextEnvelope newNode = (textEnvelopeTestBuilder.createTestTextEnvelopeTwo());
          dao.saveOrUpdate(newNode);
        }
          break;
        case 2: {
          persistedNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertTrue(persistedNode != null);
        }
          break;
        case 3: {
          persistedNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          persistedNode.setData(TextEnvelopeTestConst.TEST_ONE_NODE_DATA);
          dao.saveOrUpdate(persistedNode);
        }
          break;
        case 4: {
          persistedNode = dao.getTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertNotNull(persistedNode);
          assertEquals(TextEnvelopeTestConst.TEST_ONE_NODE_DATA, persistedNode.getData());
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  public void doTestIsExistingTextEnvelope(TextEnvelopeDAO dao) {
    for (int i = 1; i>0; i++) {
      beginTransaction(); // simulate the Transaction per Request pattern - Begin request cycle
      switch (i) {
        case 1: {
          TextEnvelope newNode = (textEnvelopeTestBuilder.createTestTextEnvelopeTwo());
          dao.save(newNode);
        }
          break;
        case 2: {
          boolean result = dao.isExistingTextEnvelope((textEnvelopeTestBuilder.createNodePathTwo()));
          assertTrue(result);
        }
          break;
        default:
          i=-1;
      }
      commitTransaction(); // simulate the Transaction per Request pattern - End request cycle
    }
  }

  protected abstract void beginTransaction();

  protected abstract void commitTransaction();
}
