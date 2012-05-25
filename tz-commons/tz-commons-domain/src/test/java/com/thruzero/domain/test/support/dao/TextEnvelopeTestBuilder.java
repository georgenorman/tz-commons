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

import java.io.Serializable;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.model.TextEnvelope;

/**
 *
 * @author George Norman
 */
public abstract class TextEnvelopeTestBuilder {

  // -----------------------------------------------------
  // SettingTestConst
  // -----------------------------------------------------

  public static interface TextEnvelopeTestConst {
    String TEST_ONE_CONTAINER_PATH = "/test-one-container/";
    String TEST_ONE_NODE_NAME = "test-one-node";
    String TEST_ONE_NODE_DATA = "<test-one-node>\n<child attr=\"arv1\">Child One</child>\n<child attr=\"arv2\">Child Two</child>\n</test-one-node>";

    String TEST_TWO_CONTAINER_PATH = "/test-two-container/";
    String TEST_TWO_NODE_NAME = "test-two-node";
    String TEST_TWO_NODE_DATA = "<test-two-node>\n<child attr=\"arv1\">Child One</child>\n<child attr=\"arv2\">Child Two</child>\n</test-two-node>";
  }

  // =======================================================================================
  // TextEnvelopeTestBuilder
  // =======================================================================================

  public EntityPath createNodePathOne() {
    return new EntityPath(new ContainerPath(TextEnvelopeTestConst.TEST_ONE_CONTAINER_PATH),TextEnvelopeTestConst.TEST_ONE_NODE_NAME);
  }

  public EntityPath createNodePathTwo() {
    return new EntityPath(new ContainerPath(TextEnvelopeTestConst.TEST_TWO_CONTAINER_PATH),TextEnvelopeTestConst.TEST_TWO_NODE_NAME);
  }

  public TextEnvelope createTestTextEnvelopeOne() {
    return new TextEnvelope(createNodePathOne(), TextEnvelopeTestConst.TEST_ONE_NODE_DATA);
  }

  public TextEnvelope createTestTextEnvelopeTwo() {
    return new TextEnvelope(createNodePathTwo(), TextEnvelopeTestConst.TEST_TWO_NODE_DATA);
  }

  protected abstract Serializable createPrimaryKeyFrom(TextEnvelope textEnvelope);


}
