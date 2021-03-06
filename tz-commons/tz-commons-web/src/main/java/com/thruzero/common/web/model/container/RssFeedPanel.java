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

package com.thruzero.common.web.model.container;

import java.util.List;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.web.model.css.StyleClass;
import com.thruzero.domain.model.RssFeed;

/**
 * A simple RSS feed panel (model) used to render an RSS feed.
 *
 * @author George Norman
 */
public class RssFeedPanel extends AbstractPanel {
  /** Data read from the RSS feed. */
  private final RssFeed rssFeed; // read from the RSS Feed

  /** Number of NewsEntry descriptions to inline instead of pop-up (read from dynamic content). */
  private final int quoteTooltipsCount; // from dynamic content
  
  private final String titleIcon; // from dynamic content

  /**
   * @param id panel ID.
   * @param title panel title.
   * @param collapseDirection the direction of a collapsible panel (in or out)
   * @param headerStyleClass CSS style class used for the header.
   */
  public RssFeedPanel(String id, String title, String titleLink, String collapseDirection, boolean useWhiteChevron, StyleClass headerStyleClass, List<InfoNodeElement> toolbar, RssFeed rssFeed, int quoteTooltipsCount, String titleIcon) {
    super(id, title, titleLink, collapseDirection, useWhiteChevron, headerStyleClass, toolbar);

    this.rssFeed = rssFeed;
    this.quoteTooltipsCount = quoteTooltipsCount;
    this.titleIcon = titleIcon;
  }

  /** Return the number of items that will have their tooltips rendered inline, in the panel (no pop-up tooltips will be displayed for these items). */
  public int getQuoteTooltipsCount() {
    return quoteTooltipsCount;
  }

  /** Return the RssFeed instance containing the news items, feed URL, etc. */
  public RssFeed getRssFeed() {
    return rssFeed;
  }

  public String getTitleIcon() {
    return titleIcon;
  }

}
