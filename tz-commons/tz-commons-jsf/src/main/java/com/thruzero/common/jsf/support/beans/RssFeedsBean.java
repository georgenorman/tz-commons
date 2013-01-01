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

package com.thruzero.common.jsf.support.beans;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ActionEvent;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ServiceLocator;
import com.thruzero.common.core.utils.PerformanceTimerUtils.PerformanceLoggerHelper;
import com.thruzero.common.jsf.support.beans.DynamicContentBean;
import com.thruzero.common.web.model.container.AbstractPanel;
import com.thruzero.common.web.model.container.PanelSet;
import com.thruzero.common.web.model.container.builder.xml.XmlCarouselPanelSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.StandardXmlPanelBuilderTypeRegistry;
import com.thruzero.common.web.model.container.builder.xml.XmlPanelSetBuilder.XmlPanelBuilderTypeRegistry;
import com.thruzero.domain.service.RssFeedService;

/**
 * Support bean used to handle requests related to RSS feeds (e.g., force all feeds to refresh or create
 * an RSS panel set).
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="rssFeedsBean")
@javax.faces.bean.RequestScoped
public class RssFeedsBean {
  private static XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry = new StandardXmlPanelBuilderTypeRegistry();

  private Map<String, List<PanelSet>> feedMap = new HashMap<String,List<PanelSet>>();

  @javax.faces.bean.ManagedProperty(value="#{dynamicContentBean}")
  private DynamicContentBean dynamicContentBean;

  /** Force the RSS feed to be reloaded from the news syndicate. */
  public void forceFeedRefreshListener(ActionEvent event) throws IOException {
    RssFeedService service = ServiceLocator.locate(RssFeedService.class);

    service.clearReadCache();
  }

  /** Read the set of RSS feeds, using the query specified by the given key, and return a list of PanelSet objects
   * suitable for use in a carousel view.
   */
  public List<PanelSet> getFeedsCarousel(String key) throws Exception {
    List<PanelSet> result = feedMap.get(key);

    if (result == null) {
      InfoNodeElement rssFeedSetNode = dynamicContentBean.getContentNode(key);
      PerformanceLoggerHelper performanceLoggerHelper = new PerformanceLoggerHelper();
      XmlCarouselPanelSetBuilder builder = new XmlCarouselPanelSetBuilder(rssFeedSetNode, panelBuilderTypeRegistry);

      result = builder.build();
      feedMap.put(key, result);
      performanceLoggerHelper.debug("getFeedsCarousel");
    }

    return result;
  }

  /** Read the set of RSS feeds, using the query specified by the given key, and return the results as a list of panels. */
  public List<AbstractPanel> getFeedsList(String key) throws Exception {
    List<AbstractPanel> result = new ArrayList<AbstractPanel>();
    List<PanelSet> carousel = getFeedsCarousel(key);

    for (PanelSet panelSet : carousel) {
      result.addAll(panelSet.getPanels());
    }

    return result;
  }

  // IoC Functions ///////////////////////////////////////////////////////

  public DynamicContentBean getDynamicContentBean() {
    return dynamicContentBean;
  }

  public void setDynamicContentBean(DynamicContentBean dynamicContentBean) {
    this.dynamicContentBean = dynamicContentBean;
  }

}
