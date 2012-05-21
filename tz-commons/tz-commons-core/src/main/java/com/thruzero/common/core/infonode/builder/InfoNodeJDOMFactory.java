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
package com.thruzero.common.core.infonode.builder;

import org.jdom.DefaultJDOMFactory;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

import com.thruzero.common.core.infonode.InfoNodeDocument;
import com.thruzero.common.core.infonode.InfoNodeElement;

/**
 * A JDOM Factory used create InfoNode-related objects when using JDOM builders (e.g., DOMBuilder, SAXBuilder).
 * 
 * @see org.jdom.input.DOMBuilder#setFactory(org.jdom.JDOMFactory)
 * @author George Norman
 */
public class InfoNodeJDOMFactory extends DefaultJDOMFactory {

  @Override
  public Element element(final String name, final Namespace namespace) {
    return new InfoNodeElement(name, namespace);
  }

  @Override
  public Element element(final String name) {
    return new InfoNodeElement(name);
  }

  @Override
  public Element element(final String name, final String uri) {
    return new InfoNodeElement(name, uri);
  }

  @Override
  public Element element(final String name, final String prefix, final String uri) {
    return new InfoNodeElement(name, prefix, uri);
  }

  @Override
  public Document document(final Element rootElement, final DocType docType) {
    return new InfoNodeDocument(rootElement, docType);
  }

  @Override
  public Document document(final Element rootElement, final DocType docType, final String baseURI) {
    return new InfoNodeDocument(rootElement, docType, baseURI);
  }

  @Override
  public Document document(final Element rootElement) {
    return new InfoNodeDocument(rootElement);
  }

}
