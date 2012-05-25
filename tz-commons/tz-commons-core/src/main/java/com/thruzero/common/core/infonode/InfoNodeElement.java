/*
 *   Copyright 2005-2011 George Norman
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
package com.thruzero.common.core.infonode;

import java.util.Iterator;

import org.jdom.DocType;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.xpath.XPath;

import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.support.ValueTransformer;

/**
 * A representation of a node in a data tree.
 *
 * A {@code InfoNodeElement} can be constructed manually or from a variety of builders that create nodes from XML
 * strings, XML files, property files, etc.
 * <p>
 * The following builders are currently implemented:
 * <ul>
 * <li>{@link com.thruzero.common.core.infonode.builder.ConfigInfoNodeBuilder ConfigInfoNodeBuilder} - Builds a
 * {@code InfoNodeElement} from a config file.
 * <li>{@link com.thruzero.common.core.infonode.builder.DomInfoNodeBuilder DomInfoNodeBuilder} - Builds a
 * {@code InfoNodeElement} from a {@code w3c.dom.Node} or XML string.
 * <li>{@link com.thruzero.common.core.infonode.builder.SaxInfoNodeBuilder SaxInfoNodeBuilder} - Builds a
 * {@code InfoNodeElement} from an XML string using a SAX parser.
 * <li>{@link com.thruzero.common.core.infonode.builder.TokenStreamInfoNodeBuilder TokenStreamInfoNodeBuilder} - Builds
 * a {@code InfoNodeElement} from a series of tokens in a string.
 * <li>{@link com.thruzero.common.core.infonode.builder.ExpressInfoNodeBuilder ExpressInfoNodeBuilder} - Builds a
 * {@code InfoNodeElement} piecemeal.
 * </ul>
 *
 * @author George Norman
 */
public class InfoNodeElement extends Element {
  private static final long serialVersionUID = 1L;

  // ---------------------------------------------
  // AttributeKeys
  // ---------------------------------------------

  /**
   * Defines reserved attribute keys used by a {@code InfoNodeElement}.
   */
  public static interface AttributeKeys {
    /** The entity path this InfoNodeElement */
    String ENTITY_PATH_ATTR_KEY = "entityPath";
  }

  // =======================================================================
  // InfoNodeElement
  // =======================================================================

  public InfoNodeElement() {
    super();

    setNamespace(Namespace.NO_NAMESPACE);
  }

  public InfoNodeElement(final String name, final Namespace namespace) {
    super(name, namespace);
  }

  public InfoNodeElement(final String name, final String prefix, final String uri) {
    super(name, prefix, uri);
  }

  public InfoNodeElement(final String name, final String uri) {
    super(name, uri);
  }

  public InfoNodeElement(final String name) {
    super(name);

    setNamespace(Namespace.NO_NAMESPACE);
  }

  public EntityPath getEntityPath() {
    EntityPath result = new EntityPath(getAttributeValue(AttributeKeys.ENTITY_PATH_ATTR_KEY));

    return result;
  }

  public void setEntityPath(final EntityPath value) {
    setAttribute(AttributeKeys.ENTITY_PATH_ATTR_KEY, value.toString());
  }

  /**
   * Find a child name using the given {@code xpathExpr}. Examples:
   * <pre>
   * "ChildElement1/TestOneElement[@TestOneAttributeOne='attrone2']"
   * </pre>
   */
  public Object find(final String xpathExpr) throws JDOMException {
    XPath xpath = XPath.newInstance(xpathExpr);

    return xpath.selectSingleNode(this);
  }

  public InfoNodeElement addChildNode(final InfoNodeElement child) {
    return (InfoNodeElement)addContent(child);
  }

  @SuppressWarnings("unchecked")
  public Iterator<? extends InfoNodeElement> getChildNodeIterator() {
    return getChildren().iterator();
  }

  @Override
  public String toString() {
    return toStringFormatted();
  }

  public String toStringFormatted() {
    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

    return xmlOutputter.outputString(this);
  }

  public String toStringUnformatted() {
    XMLOutputter xmlOutputter = new XMLOutputter();

    return xmlOutputter.outputString(this);
  }

  /**
   * Return the value as a ValueTransformer. If the value is null, a ValueTransformer will be returned constructed from the null.
   */
  public ValueTransformer<String> getValueTransformer() {
    return new ValueTransformer<String>(getValue());
  }

  /**
   * Return the specified attribute as a ValueTransformer. If the attribute is null, a ValueTransformer will be returned constructed from the null.
   */
  public ValueTransformer<String> getAttributeTransformer(String key) {
    return new ValueTransformer<String>(getAttributeValue(key));
  }

  /**
   * Return true, if {@code otherObj} is a {@code InfoNodeElement} and its toString function matches this node's
   * toString results.
   */
  public boolean equivalent(final Object otherObj) {
    boolean result = false;

    if (otherObj instanceof InfoNodeElement) {
      result = otherObj.toString().equals(this.toString());
    }

    return result;
  }

  /**
   * Wrap InfoNodeElement with a Document (so that full xpath is supported; otherwise, only relative paths are
   * supported).
   */
  public InfoNodeDocument enableRootNode() {
    return new InfoNodeDocument(this, new DocType(getName()));
  }

}
