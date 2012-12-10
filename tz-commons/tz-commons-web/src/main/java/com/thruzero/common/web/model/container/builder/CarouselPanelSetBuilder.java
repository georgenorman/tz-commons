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

package com.thruzero.common.web.model.container.builder;

import java.util.List;

import com.thruzero.common.web.model.container.PanelSet;

/**
 * An interface for a builder of carousel panel sets ({@link com.thruzero.common.web.model.container.CarouselPanelSet CarouselPanelSet}).
 *
 * @author George Norman
 */
public interface CarouselPanelSetBuilder {

  /**
   * Builds a List of {@link com.thruzero.common.web.model.container.PanelSet PanelSet} instances (used by a Carousel panel).
   */
  List<PanelSet> build() throws Exception;

}
