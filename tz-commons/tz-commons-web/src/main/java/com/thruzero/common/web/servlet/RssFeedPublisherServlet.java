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

package com.thruzero.common.web.servlet;

import java.io.IOException;
import java.io.Writer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ProviderLocator;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.model.DataStoreInfo;
import com.thruzero.domain.provider.DataStoreInfoProvider;
import com.thruzero.domain.service.RssFeedService;
import com.thruzero.domain.utils.DomainUtils;

/**
 * A servlet that generates RSS 2.0 feeds for a given request. The request must contain the feed id as a request parameter
 * with the following format: {@code <username>.<feedName>}
 * <p>
 * Example feed link: http://www.thruzero.com/jcat3/rss?id=dilbert.test1
 *
 * @author George Norman
 */
public class RssFeedPublisherServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /** Valid userName and entityName pattern (components of a feedId). */
  private Pattern namePattern = Pattern.compile("^[a-z0-9_-]{1,60}$");

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    // feed id is <userName>.<entityName>
    String feedId = request.getParameter("id");
    String[] feedInfo = StringUtils.split(feedId, '.');

    if (feedInfo == null || feedInfo.length != 2) {
      throw new IllegalArgumentException("Invalid Feed ID. Verify the format matches this: \"id=userName.feedName\"");
    }

    // get and validate userName
    String userName = feedInfo[0];
    Matcher userNameMatcher = namePattern.matcher(userName);
    if (!userNameMatcher.matches()) {
      throw new IllegalArgumentException("Invalid Feed ID");
    }

    // get and validate entityName
    String entityName = feedInfo[1];
    Matcher entityNameMatcher = namePattern.matcher(entityName);
    if (!entityNameMatcher.matches()) {
      throw new IllegalArgumentException("Invalid Feed ID");
    }

    // load the user that has published the requested feed
    DataStoreInfo dataStoreInfo = ProviderLocator.locate(DataStoreInfoProvider.class).getDataStoreInfo(userName);
    if (dataStoreInfo == null) {
      throw new IllegalArgumentException("Feed Not Found");
    }

    // load the InfoNode from the user's datastore for the requested feed
    EntityPath entityPath = new EntityPath("/" + userName + "/rss/" + entityName + ".xml");
    InfoNodeElement rssFeedNode = DomainUtils.getRootNode(entityPath, dataStoreInfo);

    // publish the feed
    RssFeedService rssFeedService = ServiceLocator.locate(RssFeedService.class);
    Writer writer = response.getWriter();
    response.setContentType("application/xml; charset=UTF-8");
    rssFeedService.writeRssFeed(rssFeedNode, writer);
  }
}
