/*
 *   Copyright 2006-2011 George Norman
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
package com.thruzero.common.core.infonode.builder;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom.input.DOMBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;

import com.thruzero.common.core.infonode.InfoNodeDocument;
import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.utils.XmlUtils;

/**
 * Builds a {@code InfoNodeElement} from a {@code w3c.dom.Node} or xml text string.
 * <p>
 * <b>Example:</b>
 *
 * <pre>
 * <code>
 * String subLinks =
 *   "<dataList>"+
 *   "  <anchor>"+
 *   "    <label>General</label>"+
 *   "    <name>design</name>"+
 *   "    <href>apps/resources/index.do?tab=design&amp;res=design</href>"+
 *   "  </anchor>"+
 *   "  <anchor>"+
 *   "    <label>Patterns</label>"+
 *   "    <name>patterns</name>"+
 *   "    <href>apps/resources/index.do?tab=design&amp;res=patterns</href>"+
 *   "  </anchor>"+
 *   "</dataList>";
 *
 * InfoNodeElement subLinks = DomInfoNodeBuilder.DEFAULT.buildInfoNode(subLinks);
 * </code>
 * </pre>
 *
 * @author George Norman
 */
public final class DomInfoNodeBuilder extends AbstractInfoNodeBuilder {
  /** Basic builder; does not provide parent document (only relative xpath supported) and no primary key. */
  public static final DomInfoNodeBuilder DEFAULT = new DomInfoNodeBuilder(PrimaryKeyOption.NO_PRIMARY_KEY, RootNodeOption.NO_ROOT_NODE);

  /** Builder providing parent document (full xpath support) and no primary key. */
  public static final DomInfoNodeBuilder WITH_ROOT_NODE = new DomInfoNodeBuilder(PrimaryKeyOption.NO_PRIMARY_KEY, RootNodeOption.GENERATE_ROOT_NODE);

  /** Builder without parent document (only relative xpath supported) and provides a primary key. */
  public static final DomInfoNodeBuilder WITH_PRIMARY_KEY = new DomInfoNodeBuilder(PrimaryKeyOption.GENERATE_PRIMARY_KEY, RootNodeOption.NO_ROOT_NODE);

  /** Builder providing parent document (full xpath support) and a primary key. */
  public static final DomInfoNodeBuilder WITH_PRIMARY_KEY_AND_ROOT_NODE = new DomInfoNodeBuilder(PrimaryKeyOption.GENERATE_PRIMARY_KEY, RootNodeOption.GENERATE_ROOT_NODE);

  /**
   * Instead of creating a new builder, use one of the pre-configured builders (e.g., DEFAULT)
   */
  private DomInfoNodeBuilder(final PrimaryKeyOption primaryKeyOption, final RootNodeOption rootNodeOption) {
    super(primaryKeyOption, rootNodeOption);
  }

  /** construct complete {@code InfoNodeDocument} from org.w3c.dom.Document. */
  public InfoNodeDocument buildInfoNode(final Document w3cDocument) {
    DOMBuilder domBuilder = new DOMBuilder();

    domBuilder.setFactory(new InfoNodeJDOMFactory());
    InfoNodeDocument result = (InfoNodeDocument)domBuilder.build(w3cDocument);

    return result;
  }

  /** construct complete {@code InfoNodeElement} from DOM. */
  public InfoNodeElement buildInfoNode(final Node sourceNode, final ErrorHandler xmlErrorHandler) throws ParserConfigurationException {
    Document w3cDocument = XmlUtils.createDocumentBuilder(xmlErrorHandler).newDocument();

    w3cDocument.adoptNode(sourceNode);

    InfoNodeDocument infoNodeDocument = buildInfoNode(w3cDocument);

    return infoNodeDocument.getRootInfoNodeElement();
  }

}
