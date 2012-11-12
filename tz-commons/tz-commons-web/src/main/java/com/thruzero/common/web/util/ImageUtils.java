/*
 *   Copyright 2006 - 2012 George Norman
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
package com.thruzero.common.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.utils.StringUtilsExt;

/**
 * Image utilities, for reading image info from a Config.
 *
 * @author George Norman
 */
public class ImageUtils {
  private static Map<String, ImageInfo> imagesRegistry;

  // -----------------------------------------------
  // ImageInfo
  // -----------------------------------------------

  public static class ImageInfo {
    private StringMap imageInfo;

    public ImageInfo(final String imageInfoStream) {
      imageInfo = StringUtilsExt.tokensToMap(imageInfoStream, "|");
    }

    public String getSrc() {
      return imageInfo.get("src");
    }

    public String getHeight() {
      return imageInfo.get("height");
    }

    public String getWidth() {
      return imageInfo.get("width");
    }
  }

  // =========================================================================
  // ImageUtils
  // =========================================================================

  public static ImageInfo getImageInfo(final String key) {
    ensureImagesInfo();

    return imagesRegistry.get(key);
  }

  private static void ensureImagesInfo() {
    if (imagesRegistry == null) {
      imagesRegistry = new HashMap<String, ImageInfo>();

      // read the image properties
      Map<String, String> registrySection = ConfigLocator.locate().getSection("imagesRegistry");

      for (Entry<String, String> entry : registrySection.entrySet()) {
        imagesRegistry.put(entry.getKey(), new ImageInfo(entry.getValue()));
      }
    }
  }
}
