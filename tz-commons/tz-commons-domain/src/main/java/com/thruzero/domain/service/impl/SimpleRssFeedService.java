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

package com.thruzero.domain.service.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.SyndFeedOutput;
import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.utils.DateTimeUtilsExt;
import com.thruzero.common.core.utils.LogUtils;
import com.thruzero.common.core.utils.XmlUtils;
import com.thruzero.domain.model.RssFeed;
import com.thruzero.domain.model.RssFeed.NewsEntry;
import com.thruzero.domain.service.RssFeedService;

/**
 * Implementation of RssFeedService that reads RSS feeds from various news sources plus writes/publishes feeds 
 * generated from clients of this service.
 * <p>
 * The requested RSS feeds will be read and cached locally, so that subsequent requests return the cached results.
 * Each feed in the cache will be refreshed according to its refresh rate.
 *
 * @author George Norman
 */
public class SimpleRssFeedService implements RssFeedService {
  private static final Logger logger = Logger.getLogger(SimpleRssFeedService.class);

  // TODO-p1(george). Should cache be saved to file?
  private final Map<String, RssFeed> feedCache = new HashMap<String, RssFeed>();
  
  public SimpleRssFeedService() {
    logger.debug(LogUtils.getObjectCreationMessage(this));
  }

  // TODO-p1(george). Add support for multiple users (so every user can have their own title, refresh-rate, etc).
  @Override
  public RssFeed readRssFeed(int maxEntries, String feedUrl, int refreshRateInHours, boolean includeImage) {
    RssFeed result;
    boolean feedNeedsRefresh = false;
    
    // block just long enough to get the existing feed from the cache or create and add a new one if refresh is required
    synchronized(feedCache) {
      result = feedCache.get(feedUrl);

      // if node not found or the refresh point has been exceeded, then it's time to refresh this node.
      if (result == null || result.getRefreshPoint() < System.currentTimeMillis()) {
        feedNeedsRefresh = true;

        // allow 3-minutes to refresh this feed (if timeouts are working properly, then the feed should be refreshed in about 9-seconds, or else fail out).
        long refreshPoint = TimeUnit.MINUTES.toMillis(3) + System.currentTimeMillis();
        if (result == null) {
          // create new empty feed for refresh
          result = new RssFeed(feedUrl, null, null, refreshPoint, "Refresh is in progress...");
        } else {
          // use existing feed, for other users, until the latest feed content is loaded
          result = new RssFeed(result, refreshPoint);
        }
        feedCache.put(feedUrl, result);
      }
    }

    // refresh node, if needed, by connecting to RSS feed end-point and fetching the latest data.
    if (feedNeedsRefresh) {
      HttpURLConnection connection = null;
      BufferedInputStream feedInputStream = null;
      
      try {        
        URL feedSource = new URL(feedUrl);
        HttpURLConnection.setFollowRedirects(true);
        connection = (HttpURLConnection)feedSource.openConnection();
        connection.setRequestMethod("GET");
        connection.setAllowUserInteraction(false);
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(6000);
        connection.connect();
        
        SyndFeedInput input = new SyndFeedInput();
        feedInputStream = new BufferedInputStream(connection.getInputStream(), 65536);
        Document document = XmlUtils.createDocument(feedInputStream);
        SyndFeed feed = input.build(document);
        feedInputStream.close();
        connection.disconnect();
        //SyndFeed feed = input.build(new XmlReader(connection.getInputStream()));

        @SuppressWarnings("unchecked")
        List<SyndEntry> syndEntries = feed.getEntries();
        List<NewsEntry> newsEntries = new ArrayList<NewsEntry>();
        int count = 0;

        for (SyndEntry syndEntry : syndEntries) {
          String description = syndEntry.getDescription() == null ? "" : syndEntry.getDescription().getValue();
          newsEntries.add(new NewsEntry(syndEntry.getTitle(), syndEntry.getLink(), description, includeImage));
          if (++count >= maxEntries) {
            break;
          }
        }

        long refreshPoint = refreshRateInHours * TimeUnit.HOURS.toMillis(1) + System.currentTimeMillis(); // next time to refresh the feed
        result = new RssFeed(feedUrl, feed.getPublishedDate(), newsEntries, refreshPoint, "");

        synchronized(feedCache) {
          feedCache.put(feedUrl, result);
        }
      } catch (MalformedURLException e) {
        // on feed error, don't try again for another N-hours
        result = createErrorFeed(feedUrl, "RSS Feed URL is malformed: " + feedUrl);
        synchronized(feedCache) {
          feedCache.put(feedUrl, result);
        }
      } catch (SocketTimeoutException e) {
        // on feed error, don't try again for another N-hours
        result = createErrorFeed(feedUrl, "RSS Feed could not be read: "+ e.getClass().getSimpleName() + " [using url: " + feedUrl + "]" );
        synchronized(feedCache) {
          feedCache.put(feedUrl, result);
        }
        logger.debug("** SocketTimeoutException occurred‎.");
      } catch (Exception e) {
        // on feed error, don't try again for another N-hours
        result = createErrorFeed(feedUrl, "RSS Feed could not be read: "+ e.getClass().getSimpleName() + " [using url: " + feedUrl + "]" );
        synchronized(feedCache) {
          feedCache.put(feedUrl, result);
        }
      } finally {
        if (feedInputStream != null) {
          try {
            feedInputStream.close();
          } catch (IOException e) {
            logger.error("****** Failed to close RSS Feed InputStream: ", e);
          }
        }
        
        if (connection != null) {
          connection.disconnect();
        }
      }
    }

    return result;
  }
  
  private RssFeed createErrorFeed(String feedUrl, String errorMessage) {
    RssFeed result = new RssFeed(feedUrl, null, null, DEFAULT_RETRY_DELAY + System.currentTimeMillis(), errorMessage);
    
    return result;
  }

  @Override
  public void clearReadCache() {
    feedCache.clear();
  }

  @Override
  public void writeRssFeed(InfoNodeElement rssFeedNode, Writer writer) {
    // create the RSS feed to be rendered as output
    SyndFeed syndFeed = new SyndFeedImpl();

    syndFeed.setFeedType("rss_2.0");
    syndFeed.setTitle(rssFeedNode.getAttributeValue("title"));
    syndFeed.setLink(rssFeedNode.getAttributeValue("link"));
    syndFeed.setDescription(rssFeedNode.getAttributeValue("description"));

    Date feedPublishedDate = DateTimeUtilsExt.stringToDate(rssFeedNode.getAttributeValue("publishedDate"), new Date(0));
    syndFeed.setPublishedDate(feedPublishedDate);

    // get entries and add each to syndFeed
    InfoNodeElement entriesNode = (InfoNodeElement)rssFeedNode.getChild("entries");
    @SuppressWarnings("unchecked")
    List<InfoNodeElement> entryNodeList = entriesNode.getChildren();
    List<SyndEntry> syndEntryList = new ArrayList<SyndEntry>();

    for (InfoNodeElement entryNode : entryNodeList) {
      SyndEntry syndEntry = new SyndEntryImpl();
      syndEntry.setTitle(entryNode.getAttributeValue("title"));
      syndEntry.setLink(entryNode.getAttributeValue("link"));

      Date entryPublishedDate = DateTimeUtilsExt.stringToDate(entryNode.getAttributeValue("publishedDate"), new Date(0));
      syndEntry.setPublishedDate(entryPublishedDate);

      SyndContent description = new SyndContentImpl();
      description.setType("text/html");
      description.setValue(entryNode.getText());
      syndEntry.setDescription(description);

      syndEntryList.add(syndEntry);
    }
    syndFeed.setEntries(syndEntryList);

    // write the feed
    SyndFeedOutput syndFeedOutput = new SyndFeedOutput();
    try {
      syndFeedOutput.output(syndFeed, writer);
      writer.flush();
    } catch (Exception e) {
      throw new RuntimeException("Error - failed to write feed output.", e);
    } finally {
      try {
        writer.close();
      } catch (IOException e) {
        logger.error("Failed to close the Feed publisher writer.", e);
      }
    }
  }
}
