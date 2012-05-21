/*
 *   Copyright 2011 George Norman
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

import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.xpath.XPath;

/**
 * A specialization of the JDOM {@code Document}, which is only needed when working with InfoNodeElements. The JDOM
 * {@code Document} is required anytime you plan to access child nodes via an xpath.
 * 
 * @author George Norman
 */
public class InfoNodeDocument extends Document {
  private static final long serialVersionUID = 1L;

  public InfoNodeDocument() {
    super();
  }

  public InfoNodeDocument(final Element rootElement, final DocType docType, final String baseURI) {
    super(rootElement, docType, baseURI);
  }

  public InfoNodeDocument(final Element rootElement, final DocType docType) {
    super(rootElement, docType);
  }

  public InfoNodeDocument(final Element rootElement) {
    super(rootElement);
  }

  public InfoNodeElement getRootInfoNodeElement() {
    return (InfoNodeElement)getRootElement();
  }

  /**
   * Returns the {@code InfoNodeElement} specified by the given {@code xpathExpr}, or {@code null}, if not found.
   */
  public InfoNodeElement findNode(final String xpathExpr) throws JDOMException {
    XPath xpath = XPath.newInstance(xpathExpr);

    return (InfoNodeElement)xpath.selectSingleNode(this);
  }

}
