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

package com.thruzero.common.web.model.container.builder.xml;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.web.model.container.AbstractPanel;
import com.thruzero.common.web.model.container.HtmlPanel;
import com.thruzero.common.web.model.container.builder.xml.AbstractXmlPanelBuilder.XmlPanelBuilderAnnotation;

/**
 * An instance represents a builder of HtmlPanel objects (a panel containing raw HTML).
 * <p/>
 * The following XML, if used by an XmlHtmlPanelBuilder, would build a simple HTML panel (a single div with an h3 element and span):
 *
 * <pre>
 * {@code
 *  <htmlPanel title="HTML Sample" id="htmlSample">
 *    <html>
 *      <![CDATA[
 *      <div>
 *        <h3>Sample Heading-1</h3>
 *        <span style="color:red;">This is a test.</span>
 *      </div>
 *      ]]>
 *    </html>
 *  </htmlPanel>
 * }
 * </pre>
 *
 * @author George Norman
 */
@XmlPanelBuilderAnnotation(panelTypeName = "htmlPanel")
public class XmlHtmlPanelBuilder extends AbstractXmlPanelBuilder {
  private static final String HTML_ID = ConfigLocator.locate().getValue(XmlHtmlPanelBuilder.class.getName(), "html", "html");

  public XmlHtmlPanelBuilder(InfoNodeElement panelNode) {
    super(panelNode);
  }

  @Override
  public AbstractPanel build() throws Exception {
    String content = getPanelNode().getChildText(HTML_ID);

    HtmlPanel result = new HtmlPanel(getPanelId(), getPanelTitle(), getCollapseDirection(), getPanelHeaderStyleClass(), getToolbar(), content);

    return result;
  }
}
