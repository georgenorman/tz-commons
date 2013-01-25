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
 * Utilities to initialize the Image Registry and return instances of ImageInfo for named images.
 * <p>
 * Example:
 * <pre>
 *   &lt;section name=&quot;imageRegistry&quot;&gt;
 *     &lt;entry key=&quot;iconEdit&quot; value=&quot;src=[imagePaths]{icons}/add.png|width=16|height=16&quot;/&gt;
 *     &lt;entry key=&quot;iconEdit&quot; value=&quot;src=[imagePaths]{icons}/edit.png|width=16|height=16&quot;/&gt;
 *     &lt;entry key=&quot;iconEdit&quot; value=&quot;src=[imagePaths]{icons}/delete.png|width=16|height=16&quot;/&gt;
 *   &lt;/section&gt;
 * </pre>
 *
 * @author George Norman
 */
public class ImageUtils {
  private static final Map<String, ImageInfo> imageRegistry = initRegistry();

  // -----------------------------------------------
  // ImageInfo
  // -----------------------------------------------

  public static final class ImageInfo {
    private final StringMap imageInfo;

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
    return imageRegistry.get(key);
  }

  private static Map<String, ImageInfo> initRegistry() {
    Map<String, ImageInfo> result = new HashMap<String, ImageInfo>();

    // read the image properties
    Map<String, String> registrySection = ConfigLocator.locate().getSection("imageRegistry");

    for (Entry<String, String> entry : registrySection.entrySet()) {
      result.put(entry.getKey(), new ImageInfo(entry.getValue()));
    }

    return result;
  }
}
