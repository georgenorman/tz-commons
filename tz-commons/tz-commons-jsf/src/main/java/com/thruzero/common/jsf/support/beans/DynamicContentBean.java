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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.locator.ProviderLocator;
import com.thruzero.common.core.provider.ResourceProvider;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.common.core.utils.PerformanceTimerUtils.PerformanceLoggerHelper;
import com.thruzero.common.jsf.support.ContentQuery;
import com.thruzero.common.jsf.support.provider.RootNodeCacheBuilderProvider;
import com.thruzero.common.web.model.container.HtmlPanel;
import com.thruzero.common.web.model.container.PanelSet;
import com.thruzero.common.web.model.nav.MenuBar;
import com.thruzero.common.web.model.nav.MenuNode;
import com.thruzero.domain.model.DataStoreInfo;
import com.thruzero.domain.provider.DataStoreInfoProvider;

/**
 * Reads content from the data store and builds component models used to render UI components on a page.
 * The data-store may reside in a relational database, the file system or be a remote private service (e.g., Dropbox).
 * The relational database and file system choices are mutually exclusive, however, the private data-store is optional
 * and unique for each individual user.
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="dynamicContentBean")
@javax.faces.bean.SessionScoped
public class DynamicContentBean implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final ErrorBuilder errorBuilder = new ErrorBuilder();
  private static final ContentQueryBuilder contentQueryBuilder = new ContentQueryBuilder();

  private final RootNodeCacheBuilder rootNodeCacheBuilder = ProviderLocator.locate(RootNodeCacheBuilderProvider.class).createRootNodeCacheBuilder();

  private final transient DataStoreCache dataStoreCache = new DataStoreCache();

  // ------------------------------------------------------
  // ContentException
  // ------------------------------------------------------

  public static class ContentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ContentException(String message) {
      super(message);
    }

    public ContentException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  // ------------------------------------------------------
  // DataStoreCache
  // ------------------------------------------------------

  /**
   * A cache of root nodes as well as the list of component models built from the root node (see RootNodeCache).
   */
  public static class DataStoreCache {
    private int maxCacheSize;

    // TODO-p0(george). ConcurrentHashMap may not be the best for this (investigate this vs rw lock)
    private final transient Map<String, RootNodeCache> cache = new ConcurrentHashMap<String, RootNodeCache>();

    public DataStoreCache() {
      maxCacheSize = ConfigLocator.locate().getIntegerValue(DataStoreCache.class.getName(), "maxCacheSize", 3);
    }

    public RootNodeCache getRootNodeCache(String key) {
      return cache.get(key);
    }

    public void putRootNodeCache(String key, RootNodeCache value) {
      if (cache.size() > maxCacheSize) {
        cache.clear();
      }
      cache.put(key, value);
    }

    public void clear() {
      cache.clear();
    }

    public void clear(String key) {
      cache.remove(key);
    }
  }

  // ------------------------------------------------------
  // RootNodeCacheBuilder
  // ------------------------------------------------------

  public static interface RootNodeCacheBuilder {
    RootNodeCache createRootNodeCache(EntityPath entityPath, DataStoreInfo dataStoreInfo);
  }

  // ------------------------------------------------------
  // ContentQueryBuilder
  // ------------------------------------------------------

  public static class ContentQueryBuilder {

    /**
     * Create the query associated with the given key, for the logged in user (or default, database context, if user isn't logged in).
     */
    public ContentQuery buildFromKey(String key, DataStoreInfo dataStoreInfo) {
      ResourceProvider resourceProvider = ProviderLocator.locate(ResourceProvider.class);
      ContentQuery result = buildFromQuerySpec(resourceProvider.getResource(key), dataStoreInfo);

      return result;
    }

    /**
     * Create the query associated with the given key, for the logged in user (or default, database context, if user isn't logged in).
     * -----------------------------
     * Construct a new ContentQuery using a querySpec that contains a full path for the entity segment (entity path begins with a '/').
     * The format of the querySpec is a pipe-separated pair, where the first segment defines the
     * full entity path and the second segment defines the xpath. The following example shows a query spec with a full entity path:
     * "/jcat3/home/index.xml|/home/listPanel[@id='quickReference']"
     * -----------------------------
     * Construct a new ContentQuery using a querySpec that contains a partial path for the entity segment (entity path does not begin with a '/').
     * The format of the querySpec is a pipe-separated pair, where the first segment defines the
     * partial entity path and the second segment defines the xpath. The following example shows a query spec with a partial entity path:
     * "home/index.xml|/home/listPanel[@id='quickReference']"
     */
    public ContentQuery buildFromQuerySpec(String querySpec, DataStoreInfo dataStoreInfo) {
      ContentQuery result;
      String[] specPaths = StringUtils.split(querySpec, "|");

      if (specPaths.length != 2) {
        throw new ContentException("ERROR with querySpec (may not be defined in resources.properties file): " + querySpec);
      }

      if (querySpec.startsWith("/")) {
        // fully qualified entity path (does not consider the logged in user)
        result = new ContentQuery(new EntityPath(specPaths[0]), specPaths[1]);
      } else {
        // partial entity path, so use the DataStore context, for this user
        String dataStoreContext = dataStoreInfo.getDataStoreContext();

        result = new ContentQuery(new EntityPath("/"+dataStoreContext+"/"+specPaths[0]), specPaths[1]);
      }

      return result;
    }
  }

  // ------------------------------------------------------
  // ErrorBuilder
  // ------------------------------------------------------

  public static class ErrorBuilder {
    public PanelSet buildMissingPanelSetError(String xPath) {
      PanelSet result = new PanelSet("error");

      result.addPanel(new HtmlPanel("error", "Error", null, "ERROR: Missing PanelSet for " + xPath));

      return result;
    }

    public MenuBar buildMissingMenuBarError(String xPath) {
      MenuBar result = new MenuBar();

      result.addMenu("errorMenuNode", new MenuNode(null, "errorMenuNode", "Menu Error - XPath not found: " + xPath, null));

      return result;
    }
  }

  // ------------------------------------------------------
  // RootNodeCache
  // ------------------------------------------------------

  public static abstract class RootNodeCache {
    private final InfoNodeElement rootNode;
    // TODO-p0(george). ConcurrentHashMap may not be the best for this (investigate this vs rw lock)
    private final Map<String, InfoNodeElement> contentNodeCache = new ConcurrentHashMap<String, InfoNodeElement>();
    private final Map<String, PanelSet> panelSetCache = new ConcurrentHashMap<String, PanelSet>();
    private final Map<String, List<PanelSet>> panelSetListCache = new ConcurrentHashMap<String, List<PanelSet>>();
    private final Map<String, MenuBar> menuBarCache = new ConcurrentHashMap<String, MenuBar>();

    public RootNodeCache(InfoNodeElement rootNode) {
      this.rootNode = rootNode;
    }

    public InfoNodeElement getRootNode() {
      return rootNode;
    }

    /**
     * Return the content node, for the given xPath, by returning it from the cache or finding it in the RootNode
     * and then caching it for future access).
     */
    public InfoNodeElement getContentNode(String xPath) {
      InfoNodeElement result = contentNodeCache.get(xPath);

      if (result == null) {
        try {
          result = getRootNode().findElement(xPath);
        } catch (JDOMException e) {
          // return null
        }

        // if found, then cache it; otherwise, return null
        if (result != null) {
          contentNodeCache.put(xPath, result);
        }
      }

      return result;
    }

    /**
     * Return the PanelSet, for the given xPath, by returning it from the cache or finding its model in the RootNode,
     * then building it and finally caching it for future access.
     */
    public PanelSet getPanelSet(String xPath) throws ContentException {
      PanelSet result = panelSetCache.get(xPath);

      if (result == null) {
        InfoNodeElement panelSetNode = getContentNode(xPath);

        if (panelSetNode != null) {
          result = buildPanelSet(panelSetNode);
          panelSetCache.put(xPath, result);
        }
      }

      return result;
    }

    protected abstract PanelSet buildPanelSet(InfoNodeElement panelSetNode) throws ContentException;

    public List<PanelSet> getPanelSetList(String xPath) throws ContentException {
      List<PanelSet> result = panelSetListCache.get(xPath);

      if (result == null) {
        InfoNodeElement panelSetListNode = getContentNode(xPath);

        if (panelSetListNode != null) {
          result = new ArrayList<PanelSet>();

          @SuppressWarnings("unchecked")
          List<InfoNodeElement> panelSetNodes = panelSetListNode.getChildren();

          for (InfoNodeElement panelSetNode : panelSetNodes) {
            PanelSet panelSet = buildPanelSet(panelSetNode);

            result.add(panelSet);
          }

          panelSetListCache.put(xPath, result);
        }
      }

      return result;
    }

    public MenuBar getMenuBar(String xPath) throws ContentException {
      MenuBar result = menuBarCache.get(xPath);

      if (result == null) {
        InfoNodeElement menuBarNode = getContentNode(xPath);

        if (menuBarNode != null) {
          result = buildMenuBar(menuBarNode);
          menuBarCache.put(xPath, result);
        }
      }

      return result;
    }

    protected abstract MenuBar buildMenuBar(InfoNodeElement menuBarNode) throws ContentException;

    // Cache management ////////////////////////////////

    /** Delete all content that has been cached by this instance. */
    public void clearContentCache() {
      contentNodeCache.clear();
      panelSetCache.clear();
      menuBarCache.clear();
    }

    /** Delete only the content that has been cached by this instance for the given xPath. */
    public void clearContentCacheFor(String xPath) {
      contentNodeCache.remove(xPath);
      panelSetCache.remove(xPath);
      menuBarCache.remove(xPath);
    }
  }

  // ============================================================================
  // AbstractDynamicContentBean
  // ============================================================================

  public String getText(String key) {
    ContentQuery contentQuery = contentQueryBuilder.buildFromKey(key, getDataStoreInfo());
    RootNodeCache rootNodeCache = getRootNodeCache(contentQuery.getEntityPath());
    InfoNodeElement resultNode = rootNodeCache.getContentNode(contentQuery.getXPath());

    return resultNode.getText();
  }

  public String getSafeText(String key) {
    String result = getText(key);

    if (StringUtils.isNotEmpty(result)) {
      result = Jsoup.clean(result, Whitelist.relaxed());
    }

    return result;
  }

  public InfoNodeElement getContentNode(String key) throws ContentException {
    ContentQuery contentQuery = contentQueryBuilder.buildFromKey(key, getDataStoreInfo());
    InfoNodeElement result = loadContentNode(contentQuery);

    return result;
  }

  //TODO-p1(george). Investigate why JSF EL can't distinguish between ContentQuery and String. Renamed to loadPanelSetList for now.
  public InfoNodeElement loadContentNode(ContentQuery contentQuery) throws ContentException {
    PerformanceLoggerHelper performanceLoggerHelper = new PerformanceLoggerHelper();
    RootNodeCache rootNodeCache = getRootNodeCache(contentQuery.getEntityPath());
    InfoNodeElement result = rootNodeCache.getContentNode(contentQuery.getXPath());
    performanceLoggerHelper.debug("loadContentNode");

    return result;
  }

  public PanelSet getPanelSet(String key) throws Exception {
    PerformanceLoggerHelper performanceLoggerHelper = new PerformanceLoggerHelper();
    ContentQuery contentQuery = contentQueryBuilder.buildFromKey(key, getDataStoreInfo());
    RootNodeCache rootNodeCache = getRootNodeCache(contentQuery.getEntityPath());
    PanelSet result = rootNodeCache.getPanelSet(contentQuery.getXPath());

    if (result == null) {
      result = errorBuilder.buildMissingPanelSetError(contentQuery.getXPath());

      // remove bad node
      dataStoreCache.clear(contentQuery.getEntityPath().toString());
    }
    performanceLoggerHelper.debug("getPanelSet {"+key+"}");

    return result;
  }

  public List<PanelSet> getPanelSetList(String key) throws ContentException {
    PerformanceLoggerHelper performanceLoggerHelper = new PerformanceLoggerHelper();
    ContentQuery contentQuery = contentQueryBuilder.buildFromKey(key, getDataStoreInfo());
    List<PanelSet> result = loadPanelSetList(contentQuery);

    if (result == null) {
      result = new ArrayList<PanelSet>();
      result.add(errorBuilder.buildMissingPanelSetError(contentQuery.getXPath()));

      // remove bad node
      dataStoreCache.clear(contentQuery.getEntityPath().toString());
    }
    performanceLoggerHelper.debug("getPanelSetList {"+key+"}");

    return result;
  }

  public List<PanelSet> loadPanelSetList(ContentQuery contentQuery) throws ContentException {
    PerformanceLoggerHelper performanceLoggerHelper = new PerformanceLoggerHelper();
    List<PanelSet> result;
    EntityPath entityPath = contentQuery.getEntityPath();
    String xPath = contentQuery.getXPath();

    if (entityPath == null) {
      result = new ArrayList<PanelSet>();
    } else {
      RootNodeCache rootNodeCache = getRootNodeCache(entityPath);
      try {
        result = rootNodeCache.getPanelSetList(xPath);
      } catch (Exception e) {
        throw new ContentException("ERROR loading PanelSet list with: " + xPath, e);
      }
    }
    performanceLoggerHelper.debug("loadPanelSetList");

    return result;
  }

  public MenuBar getMenuBar(String key) throws ContentException {
    PerformanceLoggerHelper performanceLoggerHelper = new PerformanceLoggerHelper();
    ContentQuery contentQuery = contentQueryBuilder.buildFromKey(key, getDataStoreInfo());
    RootNodeCache rootNodeCache = getRootNodeCache(contentQuery.getEntityPath());
    MenuBar result = rootNodeCache.getMenuBar(contentQuery.getXPath());

    if (result == null) {
      result = errorBuilder.buildMissingMenuBarError(contentQuery.getXPath());

      // remove bad node
      dataStoreCache.clear(contentQuery.getEntityPath().toString());
    }
    performanceLoggerHelper.debug("getMenuBar {"+key+"}");

    return result;
  }

  public void clearCache() {
    dataStoreCache.clear();
  }

  // Support methods /////////////////////////////////////////////////////////

  protected RootNodeCache getRootNodeCache(EntityPath entityPath) {
    RootNodeCache result = dataStoreCache.getRootNodeCache(entityPath.toString());

    // if not found in cache, then load it from the data store and cache it
    if (result == null) {
      result = rootNodeCacheBuilder.createRootNodeCache(entityPath, getDataStoreInfo());

      dataStoreCache.putRootNodeCache(entityPath.toString(), result);
    }

    return result;
  }

  public DataStoreInfo getDataStoreInfo() {
    DataStoreInfo result = ProviderLocator.locate(DataStoreInfoProvider.class).getDataStoreInfo();

    return result;
  }

}
