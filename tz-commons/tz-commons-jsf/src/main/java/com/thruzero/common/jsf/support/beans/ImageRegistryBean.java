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

import com.thruzero.common.web.util.ImageUtils;
import com.thruzero.common.web.util.ImageUtils.ImageInfo;

/**
 * Support bean used to retrieve image info for a named image (e.g., src URL, width and height).
 * This enables images to be independent of the filename or location. It also enables the
 * Image registry to be used by any JSF page or tag, since it can be used directly on the page
 * for an HTML IMG tag SRC attribute or embedded as a value for any JSF image tag attribute.
 * <p>
 * Example:
 * <pre>
 * &lt;h:graphicImage value=&quot;#{imageRegistryBean.getImageInfo(faqItem.getChildText(&apos;icon&apos;)).src}&quot;
 * </pre>
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name="imageRegistryBean")
@javax.faces.bean.SessionScoped
public class ImageRegistryBean {

  public ImageInfo getImageInfo(String imageKey) {
    return ImageUtils.getImageInfo(imageKey);
  }
}
