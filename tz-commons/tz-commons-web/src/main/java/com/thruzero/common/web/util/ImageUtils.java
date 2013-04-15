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
import java.util.Set;

import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.map.StringMap;
import com.thruzero.common.core.utils.StringUtilsExt;

/**
 * Utilities to initialize the Image Registry and return instances of ImageInfo for named images.
 * <p>
 * Example:
 * 
 * <pre>
 *   &lt;section name=&quot;imageCategories&quot;&gt;
 *     &lt;entry key=&quot;icons&quot; value=&quot;imageRegistry-icons&quot;/&gt;
 *   &lt;/section&gt;
 *   
 *   &lt;section name=&quot;imageRegistry-icons&quot;&gt;
 *     &lt;entry key=&quot;iconEdit&quot; value=&quot;src=[imagePaths]{icons}/add.png|width=16|height=16&quot;/&gt;
 *     &lt;entry key=&quot;iconEdit&quot; value=&quot;src=[imagePaths]{icons}/edit.png|width=16|height=16&quot;/&gt;
 *     &lt;entry key=&quot;iconEdit&quot; value=&quot;src=[imagePaths]{icons}/delete.png|width=16|height=16&quot;/&gt;
 *   &lt;/section&gt;
 * </pre>
 * 
 * @author George Norman
 */
public class ImageUtils {
  private static final Map<String, ImageMap> imageRegistry = initRegistry();

  // -----------------------------------------------
  // ImageMap
  // -----------------------------------------------

  public static class ImageMap extends HashMap<String, ImageInfo> {
    private static final long serialVersionUID = 1L;

  }

  // -----------------------------------------------
  // ImageInfo
  // -----------------------------------------------

  public static final class ImageInfo {
    private final String name;
    private final StringMap imageInfo;

    public ImageInfo(final String name, final String imageInfoStream) {
      imageInfo = StringUtilsExt.tokensToMap(imageInfoStream, "|");
      this.name = name;
    }

    public String getName() {
      return name;
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
    ImageInfo result = null;

    for (String category : imageRegistry.keySet()) {
      result = getImageInfo(category, key);

      if (result != null) {
        break;
      }
    }

    return result;
  }

  public static Set<String> getCategories() {
    return imageRegistry.keySet();
  }

  public static ImageInfo getImageInfo(final String category, final String key) {
    ImageInfo result = null;
    ImageMap imageMap = getImageMap(category);

    if (imageMap != null) {
      result = imageMap.get(key);
    }

    return result;
  }

  public static ImageMap getImageMap(final String category) {
    ImageMap result = imageRegistry.get(category);

    return result;
  }

  private static Map<String, ImageMap> initRegistry() {
    Map<String, ImageMap> result = new HashMap<String, ImageMap>();
    Map<String, String> categoriesSection = ConfigLocator.locate().getSection("imageCategories");

    // read the images for each category
    for (String category : categoriesSection.values()) {
      result.put(category, getImagesForCategory(category));
    }

    return result;
  }

  private static ImageMap getImagesForCategory(String imageCategory) {
    ImageMap result = new ImageMap();
    Map<String, String> imageCateorySection = ConfigLocator.locate().getSection(imageCategory);

    for (Entry<String, String> entry : imageCateorySection.entrySet()) {
      result.put(entry.getKey(), new ImageInfo(entry.getKey(), entry.getValue()));
    }

    return result;
  }
}
