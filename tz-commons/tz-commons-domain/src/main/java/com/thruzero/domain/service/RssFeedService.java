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

package com.thruzero.domain.service;

import java.io.Writer;
import java.util.concurrent.TimeUnit;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.service.Service;
import com.thruzero.domain.model.RssFeed;


/**
 * API to read and publish RSS feeds.
 *
 * @author George Norman
 */
public interface RssFeedService extends Service {
  long DEFAULT_RETRY_DELAY = TimeUnit.HOURS.toMillis(4); // wait 4-hours before the next refresh

  /**
   * Return an RssFeed instance for the given {@code feedUrl}.
   *
   * @param maxEntries maximum entries to include in the returned RssFeed.
   * @param feedUrl the URL to the RSS feed.
   * @param refreshRateInHours specifies how long to cache the RssFeed instance, before requesting new data from the source feed (in hours).
   * @param includeImage if true, then retrieve the first image from each news entry description; otherwise, image is null.
   */
  RssFeed readRssFeed(int maxEntries, String feedUrl, int refreshRateInHours, boolean includeImage);

  /** Force the feed cache to be cleared, causing all feed entries to be reloaded from the RSS source. */
  void clearReadCache();

  /**
   * Write the RSS feed, defined by the given {@code rssFeedNode}, to the the given {@code writer}. The writer may be associated with an HTTP connection, a file, etc.
   * <p>
   * Example data from an rssFeedNode:
   * <pre>
   * {@code
   * <rss id="test1" title="Title for test feed #1" link="http://thruzero.com/" description="Description for test feed #1" publishedDate="12/20/2012">
   *   <entries>
   *     <newsEntry title="This is News entry #2" link="http://thruzero.com/rss/news.jsf?id=news1" publishedDate="12/18/2012">
   *       This is a test description for news entry #1.
   *     </newsEntry>
   *     <newsEntry title="This is News entry #2" link="http://thruzero.com/rss/news.jsf?id=news2" publishedDate="12/19/2012">
   *       This is a test description for news entry #2.
   *     </newsEntry>
   *     <newsEntry title="This is News entry #3" link="http://thruzero.com/rss/news.jsf?id=news3" publishedDate="12/20/2012">
   *       This is a test description for news entry #3.
   *     </newsEntry>
   *   </entries>
   * </rss>
   * }
   * </pre>
   *
   * @param rssFeedNode the RSS feed data (e.g., feed title and description plus all news items).
   * @param writer where to "publish" the feed.
   */
  void writeRssFeed(InfoNodeElement rssFeedNode, Writer writer);
}
