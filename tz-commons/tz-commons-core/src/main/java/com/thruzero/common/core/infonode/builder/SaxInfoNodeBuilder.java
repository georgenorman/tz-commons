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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.ArrayStack;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Attribute;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.infonode.builder.filter.InfoNodeFilterChain;

/**
 * A builder that uses a SAX parser to create a {@code InfoNodeElement} from an xml string.
 *
 * <p>
 * <b>Example:</b>
 *
 * <pre>
 * {@code
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
 * }
 * </pre>
 *
 * @author George Norman
 */
public final class SaxInfoNodeBuilder extends AbstractInfoNodeBuilder {

  /** Basic builder; does not provide parent document (only relative xpath supported) and no primary key. */
  public static final SaxInfoNodeBuilder DEFAULT = new SaxInfoNodeBuilder(PrimaryKeyOption.NO_PRIMARY_KEY, RootNodeOption.NO_ROOT_NODE);

  /** Builder providing parent document (full xpath support) and no primary key. */
  public static final SaxInfoNodeBuilder WITH_ROOT_NODE = new SaxInfoNodeBuilder(PrimaryKeyOption.NO_PRIMARY_KEY, RootNodeOption.GENERATE_ROOT_NODE);

  /** Builder without parent document (only relative xpath supported) and provides a primary key. */
  public static final SaxInfoNodeBuilder WITH_PRIMARY_KEY = new SaxInfoNodeBuilder(PrimaryKeyOption.GENERATE_PRIMARY_KEY, RootNodeOption.NO_ROOT_NODE);

  /** Builder providing parent document (full xpath support) and a primary key. */
  public static final SaxInfoNodeBuilder WITH_PRIMARY_KEY_AND_ROOT_NODE = new SaxInfoNodeBuilder(PrimaryKeyOption.GENERATE_PRIMARY_KEY, RootNodeOption.GENERATE_ROOT_NODE);

  // ------------------------------------------------
  // NullInfoNodeElement
  // ------------------------------------------------

  /**
   * An InfoNode used to manage elements that have been removed from the DOM via filtering.
   */
  public class NullInfoNodeElement extends InfoNodeElement {
    private static final long serialVersionUID = 1L;

    private int depth = 1;

    public NullInfoNodeElement() {
      setName(NullInfoNodeElement.class.getSimpleName());
    }

    public void incrementDepth() {
      depth++;
    }

    public int decrementDepth() {
      return --depth;
    }
  }

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

    private final InfoNodeFilterChain infoNodeFilterChain;

    protected InfoNodeSaxHandler(final InfoNodeElement targetNode, final InfoNodeFilterChain infoNodeFilterChain) {
      this.rootNode = targetNode;
      this.infoNodeFilterChain = infoNodeFilterChain;
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

      List<Attribute> infoNodeAttributeList = new ArrayList<Attribute>();
      int len = attributes.getLength();
      for (int i = 0; i < len; i++) {
        String name = attributes.getLocalName(i);
        String value = attributes.getValue(i);

        infoNodeAttributeList.add(new Attribute(name, value));
      }

      // reset filter before starting a new element.
      if (infoNodeFilterChain != null) {
        infoNodeFilterChain.reset();
      }

      // start the element
      InfoNodeElement infoNode;
      if (nodeStack.empty()) {
        infoNode = rootNode;
        infoNode.setName(localName);
        infoNode.setAttributes(infoNodeAttributeList);
        infoNode = handleFilters(infoNode, infoNodeFilterChain);

        if (infoNode == null) {
          nodeStack.push(new NullInfoNodeElement());
        } else {
          addValueTo(infoNode);
          nodeStack.push(infoNode);
        }
      } else {
        if (isNullElement()) {
          getNullElement().incrementDepth();
        } else {
          infoNode = new InfoNodeElement();
          handlePrimaryKey(infoNode);
          infoNode.setName(localName);
          infoNode.setAttributes(infoNodeAttributeList);
          infoNode = handleFilters(infoNode, infoNodeFilterChain);

          if (infoNode == null) {
            nodeStack.push(new NullInfoNodeElement());
          } else {
            InfoNodeElement parentNode = (InfoNodeElement)nodeStack.get();
            parentNode.addChildNode(infoNode);
            addValueTo(parentNode);
            nodeStack.push(infoNode);
          }
        }
      }
    }

    @Override
    public void characters(final char[] buffer, final int start, final int length) {
      if (!isNullElement()) {
        elementValue.append(buffer, start, length);
      }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
      super.endElement(uri, localName, qName);

      InfoNodeElement currentNode = (InfoNodeElement)nodeStack.get();
      addValueTo(currentNode);
      if (isNullElement()) {
        if (getNullElement().decrementDepth() <= 0) {
          nodeStack.pop();
        }
      } else {
        nodeStack.pop();
      }
    }

    protected void addValueTo(final InfoNodeElement dn) {
      if (elementValue.length() != 0) {
        dn.addContent(elementValue.toString());
        elementValue.setLength(0);
      }
    }

    protected boolean isNullElement() {
      return !nodeStack.isEmpty() && nodeStack.peek() instanceof NullInfoNodeElement;
    }

    protected NullInfoNodeElement getNullElement() {
      return (NullInfoNodeElement)nodeStack.peek();
    }
  }

  // =============================================================
  // SaxInfoNodeBuilder
  // =============================================================

  /**
   * Instead of creating a new builder, use one of the pre-configured builders (e.g., DEFAULT)
   */
  private SaxInfoNodeBuilder(final PrimaryKeyOption primaryKeyOption, final RootNodeOption rootNodeOption) {
    super(primaryKeyOption, rootNodeOption);
  }

  /** Build a complete {@code InfoNodeElement} from xml. */
  public InfoNodeElement buildInfoNode(final String xml, final InfoNodeFilterChain infoNodeFilterChain) throws Exception {
    return doBuildInfoNode(xml, new InfoNodeElement(), infoNodeFilterChain);
  }

  /** Build a complete {@code InfoNodeElement} from xml. */
  public InfoNodeElement buildInfoNode(final String xml, final InfoNodeElement targetNode, final InfoNodeFilterChain infoNodeFilterChain) throws Exception {
    return doBuildInfoNode(xml, targetNode, infoNodeFilterChain);
  }

  // IMPLEMENTATION //////////////////////////////////////////////////////////////////////////////////////////////////////

  /** construct complete {@code InfoNodeElement} from dom. */
  protected InfoNodeElement doBuildInfoNode(final String xml, final InfoNodeElement targetNode, final InfoNodeFilterChain infoNodeFilterChain) throws Exception {
    handlePrimaryKey(targetNode);
    if (StringUtils.isNotEmpty(xml)) {
      XMLReader parser = XMLReaderFactory.createXMLReader();
      InfoNodeSaxHandler dnHandler = new InfoNodeSaxHandler(targetNode, infoNodeFilterChain); // state for this build is kept in this InfoNode Handler instance

      parser.setContentHandler(dnHandler);
      parser.setErrorHandler(dnHandler);
      parser.setFeature("http://xml.org/sax/features/validation", false);

      InputSource input = new InputSource(new StringReader(xml));
      parser.parse(input);
    }
    handleRootNode(targetNode);

    return targetNode;
  }
}
