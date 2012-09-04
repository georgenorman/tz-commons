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

import java.util.StringTokenizer;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.infonode.InfoNodeElement;

/**
 * Builds a {@code InfoNodeElement} from a series of tokens in a string (e.g., "title=FAQ TITLE").
 * <p>
 * <b>Example</b>:
 *
 * <pre>
 * <code>
 *   TokenStreamInfoNodeBuilder builder = TokenStreamInfoNodeBuilder.DEFAULT;
 *   InfoNodeElement newFaqDescNode = builder.buildInfoNode("contentDescription");
 *
 *   newFaqDescNode.addChildNode(builder.buildInfoNode("title=FAQ TITLE|contentBanner=BANNER"));
 *   newFaqDescNode.addChildNode(builder.buildInfoNode("toc", "item[@primaryKey='topic', @title='TOPIC']=Note 123"));
 * </code>
 * </pre>
 *
 * Builds a {@code InfoNodeElement} representing this:
 *
 * <pre>
 * {@code
 * <contentDescription>
 *   <title>FAQ TITLE</title>
 *   <contentBanner>BANNER</contentBanner>
 *   <toc>
 *     <item primaryKey="topic" title="TOPIC">Note 123</item>
 *   </toc>
 * </contentDescription>
 * }
 * </pre>
 *
 * @author George Norman
 */
public final class TokenStreamInfoNodeBuilder extends AbstractInfoNodeBuilder {
  public static final String PIPE_SEPARATOR = "|";
  public static final String COMMA = ",";

  /** Basic builder; does not provide parent document (only relative xpath supported) and no primary key. */
  public static final TokenStreamInfoNodeBuilder DEFAULT = new TokenStreamInfoNodeBuilder(false, false);

  /** Builder providing parent document (full xpath support) and no primary key. */
  public static final TokenStreamInfoNodeBuilder WITH_ROOT_NODE = new TokenStreamInfoNodeBuilder(false, true);

  /** Builder without parent document (only relative xpath supported) and provides a primary key. */
  public static final TokenStreamInfoNodeBuilder WITH_PRIMARY_KEY = new TokenStreamInfoNodeBuilder(true, false);

  /** Builder providing parent document (full xpath support) and a primary key. */
  public static final TokenStreamInfoNodeBuilder WITH_PRIMARY_KEY_AND_ROOT_NODE = new TokenStreamInfoNodeBuilder(true, true);

  // -----------------------------------------------------------
  // TokenStreamException
  // -----------------------------------------------------------

  public static class TokenStreamException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TokenStreamException(final String message, final Throwable cause) {
      super(message, cause);
    }

    public TokenStreamException(final String message) {
      super(message);
    }
  }

  // ===========================================================================
  // TokenStreamInfoNodeBuilder
  // ===========================================================================

  /**
   * Instead of creating a new builder, use one of the pre-configured builders (e.g., DEFAULT)
   */
  private TokenStreamInfoNodeBuilder(final boolean primaryKeyGenerationEnabled, final boolean rootNodeGenerationEnabled) {
    super(primaryKeyGenerationEnabled, rootNodeGenerationEnabled);
  }

  // Builder functions //////////////////////////////////////////////////////

  /**
   * Construct a complete {@code InfoNodeElement} from a token stream.
   * <p>
   * <b>Example</b>:
   *
   * <pre>
   * {@code
   * String result = TokenStreamInfoNodeBuilder.DEFAULT.buildInfoNode("toc", "item[@primaryKey='topic', @title='TOPIC']=Note 123")
   * }
   * </pre>
   *
   * <b>Result</b>:
   *
   * <pre>
   * {@code
   * <item primaryKey="topic" title="TOPIC">Note 123</item>
   * }
   * </pre>
   *
   * @param tokenStream name of node plus comma-separated list of attributes (e.g., "
   * {@code contact[@primaryKey='c1', @type='family']}").
   */
  public InfoNodeElement buildInfoNode(final String tokenStream) {
    InfoNodeElement result = initLeafNode(tokenStream, COMMA, new InfoNodeElement());

    handlePrimaryKey(result);
    handleRootNode(result);

    return result;
  }

  /**
   * Create a parent {@code InfoNodeElement} with children.
   * <p>
   * <b>Example</b>:
   * <ul>
   * <li>Parent stream: "{@code contact[@primaryKey='c1', @type='family']}"
   * <li>Child stream: "
   * {@code fname=George|lname=Norman|phone[@type='work']=555-1234|phone[@type='home']=555-1234|phone[@type='cell', @preferred='true']=555-9012|email[@type='home']=foo@barh.com|email[@type='work']=foo@barw.com|address[@type='work']=yy|address[@type='home']=zz}"
   * </ul>
   * <p>
   * <b>Results</b>:
   *
   * <pre>
   * {@code
   * <contact primaryKey="c1" type="family">
   *   <fname>George</fname>
   *   <lname>Norman</lname>
   *   <phone type="work">555-1234</phone>
   *   <phone type="home">555-5678</phone>
   *   <phone type="cell" preferred="true">555-9012</phone>
   *   <email type="home">foo@barh.com</email>
   *   <email type="work">foo@barw.com</email>
   *   <address type="work">yy</address>
   *   <address type="home">zz</address>
   * </contact>
   * }
   * </pre>
   *
   * @param parentTokenStream name of parent node plus comma-separated list of attributes (e.g., "
   * {@code contact[@primaryKey='c1', @type='family']}").
   * @param childTokenStream pipe-separated list of child nodes with comma-separated list of attributes (e.g., "
   * {@code fname=George|lname=Norman|phone[@type='cell', @preferred='true']=555-9012}").
   */
  public InfoNodeElement buildInfoNode(final String parentTokenStream, final String childTokenStream) {
    InfoNodeElement result = new InfoNodeElement();

    initLeafNode(parentTokenStream, COMMA, result);

    return buildInfoNode(result, childTokenStream);
  }

  public InfoNodeElement buildInfoNode(final InfoNodeElement parentNode, final String childTokenStream) {
    handlePrimaryKey(parentNode);
    initChildNodes(childTokenStream, PIPE_SEPARATOR, parentNode);
    handleRootNode(parentNode);

    return parentNode;
  }

  // IMPLEMENTATION ///////////////////////////////////////////////////////////////////////////////

  // parse xml token stream:
  //      fname=Geo|lname=Man|phone[@type='work']=555-1234|phone[@type='home']=555-1234|phone[@type='cell']=555-1234|email[@type='home']=foo@barh.com|email[@type='work']=foo@barw.com|address[@type='work']=yy|address[@type='home']=zz","|");
  protected void initChildNodes(final String tokenStream, final String separator, final InfoNodeElement targetNode) {
    StringTokenizer st1 = new StringTokenizer(tokenStream, separator);

    while (st1.hasMoreTokens()) {
      InfoNodeElement childNode = new InfoNodeElement();

      handlePrimaryKey(childNode);
      targetNode.addChildNode(initLeafNode(st1.nextToken(), COMMA, childNode));
    }
  }

  // parse token stream:
  //      phone[@type='work',@foo='bar']=555-1234
  protected InfoNodeElement initLeafNode(final String tokenStream, final String separator, final InfoNodeElement targetNode) {
    // TODO-p1(george) Rewrite this using RegEx
    String attributeStream = StringUtils.substringBetween(tokenStream, "[@", "]");
    String elementName;
    String elementValue;

    // handle the attributes
    if (StringUtils.isEmpty(attributeStream)) {
      elementName = StringUtils.trimToNull(StringUtils.substringBefore(tokenStream, "="));
      elementValue = StringUtils.trimToNull(StringUtils.substringAfter(tokenStream, "="));
    } else {
      StringTokenizer st1 = new StringTokenizer(attributeStream, separator);

      while (st1.hasMoreTokens()) {
        String attributeSpec = st1.nextToken();
        StringTokenizer st2 = new StringTokenizer(attributeSpec, "=");

        if (!st2.hasMoreTokens()) {
          throw new TokenStreamException("Malformed Token Stream (missing attribute name and value): " + tokenStream);
        }
        String attributeName = StringUtils.removeStart(st2.nextToken().trim(), "@");

        String attributeValue = null;
        if (st2.hasMoreTokens()) {
          attributeValue = trimQuotes(st2.nextToken().trim());
        }

        if (StringUtils.isEmpty(attributeValue)) {
          throw new TokenStreamException("Malformed Token Stream (missing attribute value): " + tokenStream);
        }

        targetNode.setAttribute(attributeName, attributeValue);
      }

      elementName = StringUtils.trimToNull(StringUtils.substringBefore(tokenStream, "["));
      elementValue = StringUtils.trimToNull(StringUtils.substringAfter(tokenStream, "]"));
    }

    // set the element name
    if (elementName == null) {
      throw new TokenStreamException("Malformed Token Stream (missing element name): " + tokenStream);
    }
    targetNode.setName(elementName);

    // set the element value
    if (elementValue != null) {
      elementValue = StringUtils.removeStart(elementValue, "=");
      targetNode.setText(elementValue);
    }

    return targetNode;
  }

  /** Remove single quotes from start and end of the given string. */
  private String trimQuotes(String value) {
    // TODO-p1(george) Rewrite this using RegEx
    value = StringUtils.trimToNull(value);
    value = StringUtils.removeStart(value, "'");
    value = StringUtils.removeStart(value, "\"");
    value = StringUtils.removeEnd(value, "'");
    value = StringUtils.removeEnd(value, "\"");

    return value;
  }
}
