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

import java.io.StringReader;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.support.Singleton;

/**
 * A builder that uses a SAX parser to create a {@code InfoNodeElement} from an xml string.
 * 
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
 * InfoNodeElement subLinks = SaxInfoNodeBuilder.DEFAULT.buildInfoNode(subLinks);
 * </code>
 * </pre>
 * 
 * @author George Norman
 */
public class SaxInfoNodeBuilder extends AbstractInfoNodeBuilder implements Singleton {
  private static final Logger logger = Logger.getLogger(SaxInfoNodeBuilder.class);

  /** Basic builder; does not provide parent document (only relative xpath supported) and no primary key. */
  public static final SaxInfoNodeBuilder DEFAULT = new SaxInfoNodeBuilder(false, false);

  /** Builder providing parent document (full xpath support) and no primary key. */
  public static final SaxInfoNodeBuilder WITH_ROOT_NODE = new SaxInfoNodeBuilder(false, true);

  /** Builder without parent document (only relative xpath supported) and provides a primary key. */
  public static final DomInfoNodeBuilder WITH_PRIMARY_KEY = new DomInfoNodeBuilder(true, false);

  /** Builder providing parent document (full xpath support) and a primary key. */
  public static final SaxInfoNodeBuilder WITH_PRIMARY_KEY_AND_ROOT_NODE = new SaxInfoNodeBuilder(true, true);

  // ------------------------------------------------
  // InfoNodeSaxHandler
  // ------------------------------------------------

  /**
   * SAX2 event handler for building info nodes.
   */
  public class InfoNodeSaxHandler extends DefaultHandler {
    private final InfoNodeElement rootNode;
    private final ArrayStack nodeStack = new ArrayStack();
    private final StringBuffer elementValue = new StringBuffer();

    protected InfoNodeSaxHandler(final InfoNodeElement targetNode) {
      this.rootNode = targetNode;
    }

    @Override
    public void startDocument() throws SAXException {
      super.startDocument();

      elementValue.setLength(0);
    }

    @Override
    public void endDocument() throws SAXException {
      super.endDocument();

      elementValue.setLength(0);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException {
      super.startElement(uri, localName, qName, attributes);

      InfoNodeElement infoNode;
      if (nodeStack.empty()) {
        infoNode = rootNode;
        infoNode.setName(localName);
        addValueTo(infoNode);
      } else {
        infoNode = new InfoNodeElement();
        handlePrimaryKey(infoNode);
        infoNode.setName(localName);
        InfoNodeElement parentNode = (InfoNodeElement)nodeStack.get();
        parentNode.addChildNode(infoNode);
        addValueTo(parentNode);
      }
      nodeStack.push(infoNode);

      int len = attributes.getLength();
      for (int i = 0; i < len; i++) {
        String name = attributes.getLocalName(i);
        String value = attributes.getValue(i);

        infoNode.setAttribute(name, value);
      }
    }

    @Override
    public void characters(final char[] buffer, final int start, final int length) {
      elementValue.append(buffer, start, length);
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
      super.endElement(uri, localName, qName);

      InfoNodeElement currentNode = (InfoNodeElement)nodeStack.get();
      addValueTo(currentNode);
      nodeStack.pop();
    }

    protected void addValueTo(final InfoNodeElement dn) {
      if (elementValue.length() != 0) {
        dn.addContent(elementValue.toString());
        elementValue.setLength(0);
      }
    }
  }

  // =============================================================
  // SaxInfoNodeBuilder
  // =============================================================

  /**
   * Allow for class extensions; disallow client instantiation. Instead of creating a new builder, use one of the
   * pre-configured builders (e.g., DEFAULT)
   */
  protected SaxInfoNodeBuilder(final boolean primaryKeyGenerationEnabled, final boolean rootNodeGenerationEnabled) {
    super(primaryKeyGenerationEnabled, rootNodeGenerationEnabled);
  }

  /** Build a complete {@code InfoNodeElement} from xml. */
  public InfoNodeElement buildInfoNode(final String xml) {
    return doBuildInfoNode(xml, new InfoNodeElement());
  }

  /** Build a complete {@code InfoNodeElement} from xml. */
  public InfoNodeElement buildInfoNode(final String xml, final InfoNodeElement targetNode) {
    return doBuildInfoNode(xml, targetNode);
  }

  // IMPLEMENTATION //////////////////////////////////////////////////////////////////////////////////////////////////////

  /** construct complete {@code InfoNodeElement} from dom. */
  protected InfoNodeElement doBuildInfoNode(final String xml, final InfoNodeElement targetNode) {
    handlePrimaryKey(targetNode);
    if (StringUtils.isNotEmpty(xml)) {
      try {
        XMLReader parser = XMLReaderFactory.createXMLReader();
        InfoNodeSaxHandler dnHandler = new InfoNodeSaxHandler(targetNode); // state for this build is kept in this InfoNode Handler instance

        parser.setContentHandler(dnHandler);
        parser.setErrorHandler(dnHandler);
        parser.setFeature("http://xml.org/sax/features/validation", false);

        InputSource input = new InputSource(new StringReader(xml));
        parser.parse(input);
      } catch (Exception e) {
        logger.error("Failed to build InfoNodeElement from xml: ", e);
      }
    }
    handleRootNode(targetNode);

    return targetNode;
  }

}
