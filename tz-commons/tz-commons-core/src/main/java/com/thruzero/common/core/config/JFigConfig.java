/*
 *   Copyright 2005-2011 George Norman
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
package com.thruzero.common.core.config;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.igfay.jfig.JFig;
import org.igfay.jfig.JFigConstants;
import org.igfay.jfig.JFigException;
import org.igfay.jfig.JFigIF;
import org.igfay.jfig.JFigLocator;

import com.thruzero.common.core.locator.InitializationException;
import com.thruzero.common.core.locator.InitializationStrategy;
import com.thruzero.common.core.locator.LocatorUtils;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.utils.ExceptionUtilsExt;

/**
 * A {@code Config} implementation based on <a href="http://jfig.sourceforge.net/">JFig</a>.
 *
 * @author George Norman
 * @see {@link com.thruzero.common.core.locator.ConfigLocator ConfigLocator}
 */
public final class JFigConfig extends AbstractConfig {
  private static final Logger logger = Logger.getLogger(AbstractConfig.class);

  private JFigIF jFig;

  /**
   * The SecurityManager does not allow construction with a non-default constructor (if the constructor is private), so
   * the init(StringMap) method is used instead of a constructor that takes params.
   * <p>
   * Use {@link com.thruzero.common.core.locator.ConfigLocator#locate()} to get an instance of the registered Config.
   */
  private JFigConfig() {
  }

  /**
   * Initializes the JFig instance, on first call; all subsequent calls will be ignored.
   *
   * @throws InitializationException if a problem is encountered with the given initParams.
   */
  @Override
  public void init(final InitializationStrategy initStrategy) {
    if (jFig == null) {
      // Note: The ConfigLocatorStrategy creates a MapInitializationStrategy containing the config file name (and path).
      // These params are typically read from the web.xml file when used in a web server environment.
      StringMap initParams = LocatorUtils.getInheritedParameters(initStrategy, this.getClass(), Config.class);

      // get the config file and directory
      String filePath = LocatorUtils.getRequiredParam(initParams, this.getClass().getName(), ConfigInitParamKeys.CONFIG_FILE_PATH_PARAM, initStrategy);
      File configFile = new File(filePath);
      String configDirectory = getConfigDirectory(configFile);

      // init JFig via, first using file path and falling back on classpath if that fails.
      JFigLocator jfigLocator = new JFigLocator(configFile.getName());

      try {
        jfigLocator.setConfigDirectory(configDirectory);
        jfigLocator.setConfigLocation(JFigConstants.FILE);
        JFig.initialize(jfigLocator);
        logger.debug("# JFig initialized from: " + configFile.getAbsolutePath());
      } catch (JFigException jfe) {
        try {
          // try again, this time using classpath
          jfigLocator.setConfigLocation(JFigConstants.CLASSPATH);
          JFig.initialize(jfigLocator);
        } catch (JFigException e) {
          logger.error("**************************");
          logger.error("ERROR: JFig failed initialization: " + e.getMessage());
          logger.error("**************************");
          throw new InitializationException("JFig failed initialization: ", e, initStrategy);
        }
      }

      jFig = JFig.getInstance();
    }
  }

  @Override
  public void reset() {

  }

  @Override
  public Map<String, String> getSection(final String sectionName) {
    Map<String, String> result = null;
    @SuppressWarnings("unchecked")
    Map<String, String> section = jFig.getSection(sectionName);

    if (StringUtils.isEmpty(sectionName)) {
      throw new RuntimeException(ExceptionUtilsExt.decorateMessageLevel1("The requested Config Section name is empty. Section name is: " + sectionName));
    }

    if (section != null) {
      result = Collections.unmodifiableMap(section);
    }

    return result;
  }

  @Override
  public Set<String> getSectionNames() {
    @SuppressWarnings("unchecked")
    Set<String> result = Collections.unmodifiableSet(jFig.getConfigDictionary().getDictionaryOfSectionDictionaries().keySet());

    return result;
  }

}
