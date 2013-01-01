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

package com.thruzero.domain.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import com.thruzero.common.core.support.SimpleIdGenerator;
import com.thruzero.common.core.utils.UiUtils;


/**
 * Represents a simple feed of news articles, independent of the RSS factory used to construct it.
 *
 * @author George Norman
 */
public class RssFeed {
  private final String feedUrl;
  private final Date publishedDate;
  private final List<NewsEntry> entries = new ArrayList<NewsEntry>();
  private final long refreshPoint;
  private final String errorStatus;

  // ------------------------------------------------------
  // NewsEntry
  // ------------------------------------------------------

  /**
   * An instance represents a single news item for an RSS feed.
   */
  public static class NewsEntry {
    private final String id;
    private final String title;
    private final String link;
    private final String description;
    private final String icon;

    public NewsEntry(String entryTitle, String entryLink, String rawDescription, boolean includeImage) {
      this.id = "e"+SimpleIdGenerator.getInstance().getNextIdAsString();
      this.title = Jsoup.clean(entryTitle, Whitelist.none());
      this.link = Jsoup.clean(entryLink, Whitelist.basic());

      if (StringUtils.isEmpty(rawDescription)) {
        description = "";
        icon = "";
      } else {
        String tempDesc = Jsoup.clean(rawDescription, Whitelist.none());
        this.description = StringUtils.abbreviate(tempDesc, 256);

        if (includeImage) {
          String tempIcon = Jsoup.clean(rawDescription, Whitelist.basicWithImages());
          Document document = Jsoup.parse(tempIcon);
          Elements imageElements = document.getElementsByTag("img");
          Element element = imageElements.first();
          icon = (element == null || element.attr("src") == null) ? "" : element.attr("src");
        } else {
          icon = "";
        }
      }
    }

    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    /** Return the href to the news article. */
    public String getLink() {
      return link;
    }

    /** Return some summary details about the news article. */
    public String getDescription() {
      return description;
    }

    /** Return optional icon for this news entry. */
    public String getIcon() {
      return icon;
    }
  }

  // ============================================================================
  // RssFeed
  // ============================================================================

  /**
   * @param feedUrl href to the news feed.
   * @param publishedDate date the article was published; if null, then current date is used.
   * @param entries the news articles associated with this feed.
   * @param refreshPoint the date/time this feed should be read again.
   * @param errorStatus if empty, then no error; otherwise, the message will be displayed to the user.
   */
  public RssFeed(String feedUrl, Date publishedDate, List<NewsEntry> entries, long refreshPoint, String errorStatus) {
    this.feedUrl = feedUrl;
    this.publishedDate = publishedDate == null ? new Date() : publishedDate;
    if (entries != null) {
      this.entries.addAll(entries);
    }
    this.refreshPoint = refreshPoint;
    this.errorStatus = errorStatus;
  }

  public String getFeedUrl() {
    return feedUrl;
  }

  public Date getPublishedDate() {
    return publishedDate;
  }

  public Collection<NewsEntry> getEntries() {
    return UiUtils.unmodifiableListHack(entries);
  }

  public long getRefreshPoint() {
    return refreshPoint;
  }

  public String getErrorStatus() {
    return errorStatus;
  }

}
