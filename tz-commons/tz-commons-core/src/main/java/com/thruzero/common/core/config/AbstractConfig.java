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
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.support.EnvironmentHelper;
import com.thruzero.common.core.utils.StringUtilsExt;

/**
 * An abstract base class that implements most of the {@link com.thruzero.common.core.config.Config Config} interface.
 * Subclasses only need to implement the {@link #getSection(String) getSection(String)} method, which returns a Map of
 * key/value pairs, representing a particular section from a config file.
 *
 * <p>
 * Subclasses can use the <a href="http://en.wikipedia.org/wiki/Adapter_pattern">Adapter</a> pattern to wrap a
 * particular {@code Config} provider. The default implementation ( {@link com.thruzero.common.core.config.JFigConfig
 * JFigConfig}) uses <a href="http://jfig.sourceforge.net/">JFig</a> as the provider.
 *
 * @author George Norman
 */
public abstract class AbstractConfig implements Config {

  @Override
  public String getValue(final String sectionName, final String key) {
    return MapUtils.getString(getSection(sectionName), key, null);
  }

  @Override
  public String getValue(final String sectionName, final String key, final String defaultValue) {
    return MapUtils.getString(getSection(sectionName), key, defaultValue);
  }

  @Override
  public boolean getBooleanValue(final String sectionName, final String key, final boolean defaultValue) {
    return StringUtilsExt.stringToBoolean(getValue(sectionName, key), defaultValue);
  }

  @Override
  public int getIntegerValue(final String sectionName, final String key, final int defaultValue) {
    return StringUtilsExt.stringToInt(getValue(sectionName, key), defaultValue);
  }

  @Override
  public long getLongValue(final String sectionName, final String key, final long defaultValue) {
    return StringUtilsExt.stringToLong(getValue(sectionName, key), defaultValue);
  }

  @Override
  public float getFloatValue(final String sectionName, final String key, final float defaultValue) {
    return StringUtilsExt.stringToFloat(getValue(sectionName, key), defaultValue);
  }

  @Override
  public URL getURLValue(final String sectionName, final String key, final URL defaultValue) {
    return StringUtilsExt.stringToUrl(getValue(sectionName, key), defaultValue);
  }

  @Override
  public Date getValueAsDate(final String sectionName, final String key, final Date defaultValue) throws ParseException {
    return StringUtilsExt.stringToDate(getValue(sectionName, key), defaultValue);
  }

  @Override
  public Date getValueAsDate(final String sectionName, final String key, final Date defaultValue, final String... parsePatterns) throws ParseException {
    return StringUtilsExt.stringToDate(getValue(sectionName, key), defaultValue, parsePatterns);
  }

  @Override
  public Properties getSectionAsProperties(final String sectionName) {
    return MapUtils.toProperties(getSection(sectionName));
  }

  @Override
  public StringMap getSectionAsStringMap(String sectionName) {
    StringMap result;

    if (StringUtils.isEmpty(sectionName)) {
      result = null;
    } else {
      Map<String, String> section = getSection(sectionName);

      result = (section == null) ? null : new StringMap(section);
    }

    return result;
  }

  @Override
  public boolean valueExists(final String sectionName, final String key) {
    return getValue(sectionName, key) != null;
  }

  // support /////////////////////////////////////////////////////////////

  protected final String getConfigDirectory(final File configFile) {
    String configDirectory;

    if (configFile.getParent() == null) {
      configDirectory = "." + EnvironmentHelper.FILE_PATH_SEPARATOR;
    } else {
      configDirectory = configFile.getParent() + EnvironmentHelper.FILE_PATH_SEPARATOR;
    }

    return configDirectory;
  }
}
