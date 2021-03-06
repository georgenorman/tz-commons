/*
 *   Copyright 2002-2012 George Norman
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
package com.thruzero.common.core.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.LogHelper;

/**
 * Static utility methods pertaining to DOMs (e.g., {@code org.w3c.dom.Document}, {@code org.w3c.dom.Node}, etc)..
 *
 * @author George Norman
 */
public class XmlUtils {
  private static final Logger logger = Logger.getLogger(XmlUtils.class);
  private static final XmlUtilsLogHelper xmlUtilsLogHelper = new XmlUtilsLogHelper(XmlUtils.class);

  public static final String XML_HEADER = "<?xml version=\"1.0\"?>";

  // ---------------------------------------------------------------
  // XmlParseException
  // ---------------------------------------------------------------

  /**
   * An exception that can be thrown while parsing a {@code DocumentBuilder}'s XML input string.
   */
  public static class XmlParseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public XmlParseException(final String message, final Throwable cause) {
      super(message, cause);
    }
  }

  // ---------------------------------------------------------------
  // LoggingErrorHandler
  // ---------------------------------------------------------------

  /**
   * An {@code ErrorHandler} that logs the warnings and errors encountered while parsing a {@code DocumentBuilder}'s XML
   * input string.
   */
  public static class LoggingErrorHandler implements ErrorHandler {
    private final String title;

    public LoggingErrorHandler(final String title) {
      this.title = title;
    }

    @Override
    public void warning(final SAXParseException exception) throws SAXException {
      logger.warn("Warning generated by: " + title + " - " + exception.getMessage());
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
      logger.error("ERROR generated by: " + title, exception);
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
      logger.error("FATAL ERROR generated by: " + title, exception);
    }
  }

  // ---------------------------------------------------------------
  // ExceptionErrorHandler
  // ---------------------------------------------------------------

  /**
   * An {@code ErrorHandler} that throws an {@code XmlParseException} when an error is encountered while parsing a
   * {@code DocumentBuilder}'s XML input string.
   */
  public static class ExceptionErrorHandler implements ErrorHandler {
    private final String title;

    public ExceptionErrorHandler(final String title) {
      this.title = title;
    }

    @Override
    public void warning(final SAXParseException exception) throws SAXException {
      logger.warn("Warning generated by: " + title + " - " + exception.getMessage());
    }

    @Override
    public void error(final SAXParseException exception) throws SAXException {
      throw new XmlParseException("ERROR generated by: " + title, exception);
    }

    @Override
    public void fatalError(final SAXParseException exception) throws SAXException {
      throw new XmlParseException("FATAL ERROR generated by: " + title, exception);
    }
  }

  // -----------------------------------------------------------
  // XmlUtilsLogHelper
  // -----------------------------------------------------------

  public static final class XmlUtilsLogHelper extends LogHelper {
    public XmlUtilsLogHelper(final Class<?> clazz) {
      super(clazz);
    }

    public void logDocumentCreated(final Document result) {
      if (getLogger().isDebugEnabled()) {
        logger.debug("#Document created:" + result);
      }
    }

    public void logDocumentCreatedFor(final String in) {
      if (getLogger().isDebugEnabled()) {
        logger.debug("#Document created for:" + in);
      }
    }
  }

  // =======================================================================================
  // XmlUtils
  // =======================================================================================

  /** This is a utility class - Allow for class extensions; disallow client instantiation */
  protected XmlUtils() {
  }

  /**
   * Creates a non-validating DocumentBuilder with the following settings:
   * <ul>
   * <li>Validating: false
   * <li>NamespaceAware: true
   * </ul>
   */
  public static DocumentBuilder createNonValidatingDocumentBuilder() throws ParserConfigurationException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    dbFactory.setValidating(false); // if true, then need to also set an org.xml.sax.ErrorHandler on DocumentBuilder
    dbFactory.setNamespaceAware(true);

    DocumentBuilder result = dbFactory.newDocumentBuilder();

    return result;
  }

  public static Document createDocument(final DocumentBuilder parser, final URL sourceURL) throws IOException, SAXException {
    Document result = null;
    InputStream inStream = null;

    try {
      inStream = sourceURL.openStream();
      result = createDocument(parser, inStream);
      xmlUtilsLogHelper.logDocumentCreated(result);
    } catch (IOException e) {
      logger.error("*** IOException: " + e);
      throw e;
    } finally {
      if (inStream != null) {
        try {
          inStream.close();
        } catch (IOException e) {
          // ignore close error.
        }
      }
    }

    return result;
  }

  public static Document createDocument(final DocumentBuilder parser, final InputStream inStream) throws IOException, SAXException {
    return parser.parse(inStream);
  }

  public static Document createDocument(final URL sourceURL) throws IOException, SAXException {
    try {
      return createDocument(createNonValidatingDocumentBuilder(), sourceURL);
    } catch (ParserConfigurationException e) {
      logger.error("*** ParserConfigurationException: " + e);
    }

    return null;
  }

  public static Document createDocument(final InputStream inStream) throws IOException, SAXException {
    try {
      return createDocument(createNonValidatingDocumentBuilder(), inStream);
    } catch (ParserConfigurationException e) {
      logger.error("*** ParserConfigurationException: " + e);
    }

    return null;
  }

  public static Document createDocument(final DocumentBuilder parser, final String in) throws IOException, SAXException {
    xmlUtilsLogHelper.logDocumentCreatedFor(in);

    return createDocument(parser, StringUtilsExt.stringToInputStream(in));
  }

  public static Document createDocument(final String in) throws IOException, SAXException {
    xmlUtilsLogHelper.logDocumentCreatedFor(in);

    return createDocument(StringUtilsExt.stringToInputStream(in));
  }

  /** return null if content text is valid; otherwise return error message. */
  public static String validateContentText(final String contentText, final ErrorHandler xmlErrorHandler) {
    InputStream inputStream = StringUtilsExt.stringToInputStream(contentText);

    // create builder
    try {
      DocumentBuilder documentParser = XmlUtils.createNonValidatingDocumentBuilder();
      XmlUtils.createDocument(documentParser, inputStream);
    } catch (SAXException e) {
      return "SAXException:" + e.getMessage(); // "SAXException:" added to guarantee that the return result will not be empty on error.
    } catch (IOException e) {
      return "IOException:" + e.getMessage(); // "IOException:" added to guarantee that the return result will not be empty on error.
    } catch (ParserConfigurationException e) {
      return "Error: Could not create document builder.";
    }

    return null;
  }

  public static Node selectSingleNode(final Node sourceNode, final String xPathExpression) {
    Node result;
    XPathFactory factory = XPathFactory.newInstance(); // http://www.ibm.com/developerworks/library/x-javaxpathapi/index.html
    XPath xPathParser = factory.newXPath();

    try {
      result = (Node)xPathParser.evaluate(xPathExpression, sourceNode, XPathConstants.NODE);
    } catch (XPathExpressionException e) {
      result = null;
    }

    return result;
  }

  // Return text value of {@code sourceNode}.
  public static String getElementValue(final Node sourceNode) {
    String result = null;
    if (sourceNode == null) {
      return result;
    }

    if (sourceNode.getNodeType() == Node.ELEMENT_NODE) {
      Node firstChild = sourceNode.getFirstChild();

      if (firstChild != null) {
        result = firstChild.getNodeValue();
      }
    }

    return result;
  }

  // Return the parent xpath of the given {@code xpath}
  public static String getParentPath(final String xPath) {
    int indexOfLastSlash = StringUtils.lastIndexOf(xPath, "/");

    if (indexOfLastSlash > 0) {
      return StringUtils.left(xPath, indexOfLastSlash);
    }

    return null;
  }

  // read to map ////////////////////////////////////////////////////////////////

  /**
   * For the given {@code sourceNode}, read each "top level" element into the resulting {@code Map}. Each element name
   * is a key to the map, each element value is the value paired to the key. Example - anchor is the node, label and
   * href are keys:
   *
   * <pre>
   * {@code
   * <anchor>
   *  <label>Slashdot</label>
   *    <href>http://slashdot.org/</href>
   *  </anchor>
   * }
   * </pre>
   */
  public static Map<String, String> readNodeElementsToMap(final Node sourceNode) {
    Map<String, String> result = new HashMap<String, String>();

    if (sourceNode == null) {
      return result;
    }

    NodeList childNodes = sourceNode.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++) {
      Node element = childNodes.item(i);

      if (element.getNodeType() == Node.ELEMENT_NODE) {
        String elementName = element.getNodeName();
        String elementValue = "";
        Node firstChild = element.getFirstChild();

        if (firstChild != null) {
          elementValue = firstChild.getNodeValue();
        }

        if (elementValue != null) {
          result.put(elementName, elementValue);
        }
      }
    }

    return result;
  }

  public static StringMap readNodeElementsToStringMap(final Node sourceNode) {
    StringMap result = null;
    if (sourceNode != null) {
      result = new StringMap(readNodeElementsToMap(sourceNode));
    }
    return result;
  }

  public static StringMap readNodeElementsToStringMap(final Node sourceNode, final String xPathExpression) throws XPathExpressionException {
    StringMap result = null;
    Node node = selectSingleNode(sourceNode, xPathExpression);

    if (node != null) {
      result = XmlUtils.readNodeElementsToStringMap(node);
    }

    return result;
  }
}
