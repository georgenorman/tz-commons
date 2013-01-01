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

import org.apache.commons.lang3.StringEscapeUtils;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.utils.PerformanceTimerUtils.PerformanceLoggerHelper;
import com.thruzero.common.web.model.container.AbstractPanel;
import com.thruzero.common.web.model.container.RssFeedPanel;
import com.thruzero.common.web.model.container.builder.xml.AbstractXmlPanelBuilder.XmlPanelBuilderAnnotation;
import com.thruzero.domain.model.RssFeed;
import com.thruzero.domain.service.RssFeedService;

/**
 * Builds an RSS Feed panel from XML which is read from a Data Store (SQL, DSC, etc).
 *
 * @author George Norman
 */
@XmlPanelBuilderAnnotation(panelTypeName = "rssFeedPanel")
public class XmlRssFeedPanelBuilder extends AbstractXmlPanelBuilder {

  public XmlRssFeedPanelBuilder(InfoNodeElement panelNode) {
    super(panelNode);
  }

  @Override
  public AbstractPanel build() throws Exception {
    int maxEntries = getPanelNode().getAttributeTransformer("size").getIntValue(5);
    int refreshRate = getPanelNode().getAttributeTransformer("refreshRate").getIntValue(480);
    int quoteTooltipsCount = getPanelNode().getAttributeTransformer("quoteTooltipsCount").getIntValue(-1);
    boolean includeImage = getPanelNode().getAttributeTransformer("includeImage").getBooleanValue(false);
    String feedUrl = getPanelNode().getText();

    PerformanceLoggerHelper performanceLoggerHelper = new PerformanceLoggerHelper();
    RssFeedService service = ServiceLocator.locate(RssFeedService.class);
    RssFeed rssFeed = service.readRssFeed(maxEntries, feedUrl, refreshRate, includeImage);
    performanceLoggerHelper.debug("readRssFeed [" + StringEscapeUtils.escapeHtml4(feedUrl) + "]");
    String titleLink = getPanelNode().getAttributeValue("titleLink");

    RssFeedPanel result = new RssFeedPanel(getPanelId(), getPanelTitle(), getPanelHeaderStyleClass(), rssFeed, titleLink, quoteTooltipsCount);

    return result;
  }

}
