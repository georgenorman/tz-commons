/*
 *   Copyright 2011-2012 George Norman
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
package com.thruzero.common.jsf.support.beans.dialog;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.bookmarks.ConfigKeysBookmark;
import com.thruzero.common.core.config.Config.ConfigKeys;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.locator.ProviderLocator;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.provider.ResourceProvider;
import com.thruzero.common.jsf.utils.FacesUtils;
import com.thruzero.common.jsf.utils.FlashUtils;
import com.thruzero.common.web.util.HtmlUtils.TableUtils;

/**
 * Application about box.
 *
 * @author George Norman
 */
public abstract class AbstractAboutApplicationBean implements Serializable {
  private static final long serialVersionUID = 1L;

  // ------------------------------------------------
  // ConfigKeys
  // ------------------------------------------------

  /** An interface that defines keys used to lookup common values in a config file. */
  @ConfigKeysBookmark(comment = "Config keys for about box.")
  public interface AboutBoxConfigKeys extends ConfigKeys {
    /** The section that contains the libraries to be included in the application about box. */
    String ABOUT_LIBS_SECTION = AbstractAboutApplicationBean.class.getName();
  }

  // =============================================================
  // AbstractAboutApplicationBean
  // =============================================================

  public abstract String getAppVersion();

  public String getTitle() {
    return ProviderLocator.locate(ResourceProvider.class).getResource("about.app.title") + " - " + getAppVersion();
  }

  public String getDescription() {
    return ProviderLocator.locate(ResourceProvider.class).getResource("about.app.description");
  }

  public String getShortDescription() {
    return ProviderLocator.locate(ResourceProvider.class).getResource("about.app.description.short");
  }

  public String getCopyright() {
    return ProviderLocator.locate(ResourceProvider.class).getResource("about.app.copyright");
  }

  public String getJarRows() {
    StringBuffer result = new StringBuffer();

    try {
      File webInfDir = FacesUtils.getWebInfDir();
      if (webInfDir != null) {
        File libDir = new File(webInfDir, "lib");
        String[] extensions = { "jar" };
        @SuppressWarnings("unchecked")
        Collection<File> jarFiles = FileUtils.listFiles(libDir, extensions, false);
        StringMap aboutLibs = ConfigLocator.locate().getSectionAsStringMap(AboutBoxConfigKeys.ABOUT_LIBS_SECTION);

        if (aboutLibs == null) {
          result.append("<br/><span style=\"color:red;\">The " + AboutBoxConfigKeys.ABOUT_LIBS_SECTION + " config section was not found.</span>");
        } else {
          for (File jarFile : jarFiles) {
            String version = StringUtils.substringAfterLast(jarFile.getName(), "-");
            version = StringUtils.remove(version, ".jar");
            String aboutKey = StringUtils.substringBeforeLast(jarFile.getName(), "-");

            // make sure it's the full version number (e.g., "hibernate-c3p0-3.5.0-Final" at this point will be "Final". Need to back up a segment to get the version number.
            String versComp = StringUtils.substringBefore(version, ".");
            if (!StringUtils.isNumeric(versComp)) {
              String version2 = StringUtils.substringAfterLast(aboutKey, "-");
              versComp = StringUtils.substringBefore(version2, ".");
              if (StringUtils.isNumeric(versComp)) {
                aboutKey = StringUtils.substringBeforeLast(aboutKey, "-");
                version = version2 + "-" + version;
              } else {
                continue; // give up on this one
              }
            }

            // get config value for this jar file
            if (StringUtils.isNotEmpty(aboutKey) && StringUtils.isNotEmpty(version) && aboutLibs.containsKey(aboutKey)) {
              String href = aboutLibs.get(aboutKey);
              aboutKey = StringUtils.remove(aboutKey, "-core");
              result.append(TableUtils.createSimpleFormRow(aboutKey, href, version));
            }
          }
        }
      }
    } catch (Exception e) {
      // don't let the about box crash the app
    }

    return result.toString();
  }

  public String getEnvironmentRows() {
    StringBuffer result = new StringBuffer();

    try {
      result.append(TableUtils.createSimpleFormRow("Web Server", FacesUtils.getSession(false).getServletContext().getServerInfo()));
      result.append(TableUtils.createSimpleFormRow("OS", System.getProperty("os.name")));
      result.append(TableUtils.createSimpleFormRow("Java Version", System.getProperty("java.version")));
      result.append(TableUtils.createSimpleFormRow("Total Memory", Runtime.getRuntime().totalMemory()/(1024000)+"M"));
      result.append(TableUtils.createSimpleFormRow("Free Memory", Runtime.getRuntime().freeMemory()/(1024000)+"M"));
      result.append(TableUtils.createSimpleFormRow("Max Memory", Runtime.getRuntime().maxMemory()/(1024000)+"M"));
      result.append(TableUtils.createSimpleFormRow("Flash Count", FlashUtils.getFlashCount()+""));
    } catch (Exception e) {
      // don't let the about box crash the app
    }

    return result.toString();
  }

}
