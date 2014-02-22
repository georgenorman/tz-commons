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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.math.NumberUtils;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.support.LogHelper;
import com.thruzero.common.core.utils.ClassUtils;
import com.thruzero.common.core.utils.ClassUtils.ClassUtilsException;
import com.thruzero.common.web.model.container.AbstractPanel;
import com.thruzero.common.web.model.container.ErrorHtmlPanel;
import com.thruzero.common.web.model.container.PanelSet;
import com.thruzero.common.web.model.container.builder.PanelBuilder;
import com.thruzero.common.web.model.container.builder.PanelSetBuilder;
import com.thruzero.common.web.model.container.builder.xml.AbstractXmlPanelBuilder.XmlPanelBuilderAnnotation;

/**
 * A builder of PanelSet instances, using XML as the definition. Below is a sample &lt;panelSet&gt; node containing the
 * set of panel nodes (in presentation order). The XmlPanelSetBuilder will parse the XML and build each panel and add it
 * to the result.
 *
 * <pre>
 * {@code
 * <index>
 *  <panelSet id="col1">
 *   <listPanel title="Panel-1" id="col1_panel1">
 *     <dataList>
 *       <a href="item11.html" title="Item-11"/>
 *       <a href="item12.html" title="Item-12"/>
 *       <a href="item13.html" title="Item-13"/>
 *     </dataList>
 *   </listPanel>
 *
 *   <listPanel title="Panel-2" id="col1_panel2">
 *     <dataList>
 *       <a href="item21.html" title="Item-21"/>
 *       <a href="item22.html" title="Item-22"/>
 *     </dataList>
 *   </listPanel>
 *  </panelSet>
 *
 *   ...
 *
 * </index>
 * }
 * </pre>
 *
 * @author George Norman
 */
public class XmlPanelSetBuilder implements PanelSetBuilder {
  private static final SimpleXmlPanelBuilderLogHelper logHelper = new SimpleXmlPanelBuilderLogHelper(XmlPanelSetBuilder.class, false);

  private static final String ID = ConfigLocator.locate().getValue(XmlPanelSetBuilder.class.getName(), "id", "id");
  
  private static final String TIME_OUT_IN_SECONDS = ConfigLocator.locate().getValue(XmlPanelSetBuilder.class.getName(), "timeOutInSeconds", "timeOutInSeconds");

  private final String panelSetId;
  private final long timeoutInSeconds;
  private final List<InfoNodeElement> panelNodes;
  private final XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry;

  // ------------------------------------------------------
  // XmlPanelBuilderTypeRegistry
  // ------------------------------------------------------

  /**
   * A registry of XML-based panel builders, that maps panel names (e.g., 'listPanel') to panel builder types (e.g.,
   * 'XmlListPanelBuilder').
   * <p/>
   * Clients must manually register builder types with the xregistry. Below is an example of the FAQ page bean's
   * registry initialization:
   *
   * <pre>
   * {
   *   &#064;code
   *   // Enable FAQ page to build FAQ and HTML panels
   *   private static XmlPanelBuilderTypeRegistry panelBuilderRegistry = new XmlPanelBuilderTypeRegistry(XmlFaqPanelBuilder.class, XmlHtmlPanelBuilder.class);
   * }
   * </pre>
   *
   * @author George Norman
   */
  public static class XmlPanelBuilderTypeRegistry {
    private final Map<String, Class<? extends AbstractXmlPanelBuilder>> builderRegistry = new HashMap<String, Class<? extends AbstractXmlPanelBuilder>>();

    /**
     * Builds an empty registry (use <code>registerBuilderType</code> to register builder types).
     */
    public XmlPanelBuilderTypeRegistry() {
    }

    /**
     * Builds a registry from the given <code>builderTypes</code>, using the <code>XmlPanelBuilderAnnotation</code> of
     * each <code>builderType</code> as the panel type name.
     *
     * @param builderTypes
     */
    public XmlPanelBuilderTypeRegistry(Class<? extends AbstractXmlPanelBuilder>... builderTypes) {
      registerBuilderTypes(builderTypes);
    }

    /**
     * Registers all of the given <code>builderTypes</code>, using the <code>XmlPanelBuilderAnnotation</code> from the
     * <code>builderType</code> as the panel type name.
     *
     * @param builderTypes
     */
    public final void registerBuilderTypes(Class<? extends AbstractXmlPanelBuilder>... builderTypes) {
      for (Class<? extends AbstractXmlPanelBuilder> builderType : builderTypes) {
        registerBuilderType(builderType);
      }
    }

    /**
     * Registers the given <code>builderTypes</code>, using the <code>XmlPanelBuilderAnnotation</code> from the
     * <code>builderType</code> as the panel type name.
     *
     * @param builderTypes
     */
    public final Class<? extends AbstractXmlPanelBuilder> registerBuilderType(Class<? extends AbstractXmlPanelBuilder> builderType) {
      XmlPanelBuilderAnnotation builderAnnotation = builderType.getAnnotation(XmlPanelBuilderAnnotation.class);

      return builderRegistry.put(builderAnnotation.panelTypeName(), builderType);
    }

    /**
     * Registers the given <code>builderTypes</code>, using the given <code>panelTypeName</code> as the name (ignores
     * the <code>XmlPanelBuilderAnnotation</code> from the <code>builderType</code>).
     *
     * @param builderTypes
     */
    public final Class<? extends AbstractXmlPanelBuilder> registerBuilderType(String panelTypeName, Class<? extends AbstractXmlPanelBuilder> builderType) {
      return builderRegistry.put(panelTypeName, builderType);
    }

    /**
     * Create a panel builder of the type identified by panelTypeName, using the given panelNode as the XML source.
     *
     * @throws ClassUtilsException
     */
    public AbstractXmlPanelBuilder createBuilder(String panelTypeName, InfoNodeElement panelNode) throws ClassUtilsException {
      Class<? extends AbstractXmlPanelBuilder> builderType = builderRegistry.get(panelTypeName);
      AbstractXmlPanelBuilder result = ClassUtils.instanceFrom(builderType, new Class[] { InfoNodeElement.class }, new InfoNodeElement[] { panelNode });

      return result;
    }
  }

  // ------------------------------------------------------
  // StandardXmlPanelBuilderTypeRegistry
  // ------------------------------------------------------

  /**
   * Pre-configured registry that builds all standard panels. Additional panels can be passed into the constructor, allowing clients
   * to add new panel types or replace any of the pre-registered standard types. If none of the standard builders are required, then clients
   * should consider using the <code>XmlPanelBuilderTypeRegistry</code> class.
   * <p>Pre configured with the following builder types:
   * <ul>
   *  <li>{@link com.thruzero.common.web.model.container.builder.xml.XmlFaqPanelBuilder XmlFaqPanelBuilder}</li>
   *  <li>{@link com.thruzero.common.web.model.container.builder.xml.XmlListPanelBuilder XmlListPanelBuilder}</li>
   *  <li>{@link com.thruzero.common.web.model.container.builder.xml.XmlHtmlPanelBuilder XmlHtmlPanelBuilder}</li>
   *  <li>{@link com.thruzero.common.web.model.container.builder.xml.XmlRssFeedPanelBuilder XmlRssFeedPanelBuilder}</li>
   *  <li>{@link com.thruzero.common.web.model.container.builder.xml.XmlDividerPanelBuilder XmlDividerPanelBuilder}</li>
   * </ul>
   */
  public static class StandardXmlPanelBuilderTypeRegistry extends XmlPanelBuilderTypeRegistry {
    @SuppressWarnings("unchecked")
    private static final Class<? extends AbstractXmlPanelBuilder>[] STANDARD_BUILDER_TYPES = 
      new Class[] {XmlFaqPanelBuilder.class, XmlListPanelBuilder.class, XmlHtmlPanelBuilder.class, XmlRssFeedPanelBuilder.class, XmlDividerPanelBuilder.class};

    public StandardXmlPanelBuilderTypeRegistry() {
      registerBuilderTypes(STANDARD_BUILDER_TYPES);
    }

    public StandardXmlPanelBuilderTypeRegistry(Class<? extends AbstractXmlPanelBuilder>... builderTypes) {
      registerBuilderTypes(STANDARD_BUILDER_TYPES);

      registerBuilderTypes(builderTypes);
    }
  }

  // -----------------------------------------------------------
  // SimpleXmlPanelBuilderLogHelper
  // -----------------------------------------------------------

  public static final class SimpleXmlPanelBuilderLogHelper extends LogHelper {
    private final boolean paranoidLogging;
    
    public SimpleXmlPanelBuilderLogHelper(Class<?> clazz, boolean paranoidLogging) {
      super(clazz);
      
      this.paranoidLogging = paranoidLogging;
    }
    
    public void logExecutorServiceCreated(String panelSetId) {
      if (paranoidLogging && getLogger().isDebugEnabled()) {
        getLogger().debug("* ExecutorService created for: " + panelSetId);
      }
    }
    
    public void logExecutorServiceShutdown(String panelSetId) {
      if (paranoidLogging && getLogger().isDebugEnabled()) {
        logHelper.getLogger().debug("* ExecutorService shutdown for: " + panelSetId);
      }
    }
    
    public void logExecutorServiceInterrupted(String panelSetId) {
      if (getLogger().isDebugEnabled()) {
        logHelper.getLogger().debug("* ExecutorService termination was interrupted for: " + panelSetId);
      }
    }

    public void logExecutorServiceIsTerminated(String panelSetId) {
      if (paranoidLogging && getLogger().isDebugEnabled()) {
        getLogger().debug("* ExecutorService is terminated for: " + panelSetId);
      }
    }

    public void logExecutorServiceIsNotTerminated(ExecutorService executorService, List<Runnable> straglers, String panelSetId) {
      if (getLogger().isDebugEnabled()) {
        for (Runnable runnable : straglers) {
          getLogger().debug("  * " + runnable);
        }
        
        if (executorService.isTerminated()) {
          logExecutorServiceIsTerminated(panelSetId);
        } else {
          getLogger().debug("******* ExecutorService is NOT terminated for: " + panelSetId + ". ActiveThreadCount: " + Thread.activeCount());
        }
      }
    }

    public void logPanelSetCompleted(String panelSetId) {
      if (paranoidLogging && getLogger().isDebugEnabled()) {
        logHelper.getLogger().debug("* Completed XmlPanelSetBuilder.build() for: " + panelSetId);
      }
    }
  }

  // ============================================================================
  // XmlPanelSetBuilder
  // ============================================================================

  /**
   * Builds a <code>PanelSet</code> from the given <code>panelSetNode</code>.
   *
   * @param panelSetNode node containing the set of panels (each child of this node will be a panel node).
   * @param panelBuilderTypeRegistry registry that specifies which builder to use for a given panel definition (based on
   * panel name - e.g., 'listPanel').
   */
  @SuppressWarnings("unchecked")
  public XmlPanelSetBuilder(InfoNodeElement panelSetNode, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this(panelSetNode.getAttributeValue(ID), NumberUtils.toLong(panelSetNode.getAttributeValue(TIME_OUT_IN_SECONDS), 0), panelSetNode.getChildren(), panelBuilderTypeRegistry);
  }

  public XmlPanelSetBuilder(String panelSetId, long timeoutInSeconds, List<InfoNodeElement> panelNodes, XmlPanelBuilderTypeRegistry panelBuilderTypeRegistry) {
    this.panelSetId = panelSetId;
    this.timeoutInSeconds = timeoutInSeconds;
    this.panelNodes = panelNodes;
    this.panelBuilderTypeRegistry = panelBuilderTypeRegistry;
  }

  @Override
  public PanelSet build() throws Exception {
    PanelSet result;

    // build concurrently if more than one panel and if time-out is specified (e.g., making remote requests).
    if (panelNodes.size() > 1 && timeoutInSeconds > 0) {
      result = buildConcurrently();
    } else {
      result = buildSequentially();
    }

    return result;
  }

  protected PanelSet buildConcurrently() throws Exception {
    PanelSet result = new PanelSet(panelSetId);

    if (!panelNodes.isEmpty()) {
      // Build the panels in parallel (e.g., RSS Feed panels should be created in parallel).
      ExecutorService executorService = Executors.newFixedThreadPool(panelNodes.size());
      logHelper.logExecutorServiceCreated(panelSetId);
      
      final Map<String,AbstractPanel> panels = new HashMap<String,AbstractPanel>();
      for (final InfoNodeElement panelNode : panelNodes) {
        final AbstractXmlPanelBuilder panelBuilder = panelBuilderTypeRegistry.createBuilder(panelNode.getName(), panelNode);
        final String panelKey = Integer.toHexString(panelNode.hashCode());
  
        if (panelBuilder == null) {
          panels.put(panelKey, new ErrorHtmlPanel("error", "Panel ERROR", "PanelBuilder not found for panel type " + panelNode.getName()));
        } else {
          //logger.debug("  - prepare to build: " + panelNode.getName());
          executorService.execute(new Runnable() {
            @Override
            public void run() {
              try {
                AbstractPanel panel = panelBuilder.build();
                panels.put(panelKey, panel);
              } catch (Exception e) {
                panels.put(panelKey, panelBuilder.buildErrorPanel(panelBuilder.getPanelId(), "Panel ERROR", "PanelBuilder encountered an Exception: " + e.getClass().getSimpleName()));
              }
            }
            
            @Override
            public String toString() {
              return panelBuilder.getPanelInfoForError();
            }
          });
        }
      }

      // Wait for all panels to be built
      executorService.shutdown();
      logHelper.logExecutorServiceShutdown(panelSetId);
      try {
        executorService.awaitTermination(timeoutInSeconds, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        // ignore (handled below)
        logHelper.logExecutorServiceInterrupted(panelSetId);
      }
      
      if (executorService.isTerminated()) {
        logHelper.logExecutorServiceIsTerminated(panelSetId);
      } else {
        logHelper.logExecutorServiceIsNotTerminated(executorService, executorService.shutdownNow(), panelSetId);
      }

      // add panels in the same order as defined
      for (InfoNodeElement panelNode : panelNodes) {
        String panelKey = Integer.toHexString(panelNode.hashCode());
        AbstractPanel panel = panels.get(panelKey);
        if (panel == null) {
          // if it wasn't added to the panelNodes map, then there must have been a timeout error
          AbstractXmlPanelBuilder panelBuilder = panelBuilderTypeRegistry.createBuilder(panelNode.getName(), panelNode);
          
          result.addPanel(panelBuilder.buildErrorPanel(panelKey, "Panel ERROR", "PanelBuilder encountered a timeout error: " + panelNode.getName()));
        } else {
          result.addPanel(panel);
        }
      }
    }
    logHelper.logPanelSetCompleted(panelSetId);

    return result;
  }

  protected PanelSet buildSequentially() throws Exception {
    PanelSet result = new PanelSet(panelSetId);

    for (InfoNodeElement panelNode : panelNodes) {
      PanelBuilder panelBuilder = panelBuilderTypeRegistry.createBuilder(panelNode.getName(), panelNode);

      if (panelBuilder == null) {
        result.addPanel(new ErrorHtmlPanel("error", "Panel ERROR", "PanelBuilder not found for panel type " + panelNode.getName()));
      } else {
        result.addPanel(panelBuilder.build());
      }
    }

    return result;
  }

}
