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

import com.thruzero.common.web.model.container.PanelGrid;

/**
 * An interface for a builder of carousel panel sets ({@link com.thruzero.common.web.model.container.CarouselPanelSet CarouselPanelSet}).
 *
 * @author George Norman
 */
public interface CarouselPanelSetBuilder {

  /**
   * Builds a List of {@link com.thruzero.common.web.model.container.PanelGrid PanelGrid} instances (used by a Carousel panel).
   * A Carousel panel supports multiple pages, where each page may contain multiple rows and columns. 
   * The Carousel panel renderer uses a horizontal layout, so that each PanelGrid represents a full page and renders each panel from the PanelSet as a column. 
   * <br><br>
   * For example, if a carouselPanelSet data model contains 8 panels, with pagination set to 4 and maxRows set to 1, then there will be 2 pages of single 
   * rows, with each row containing 4 panels, as shown below.
   * <pre>
   * PanelGrid-1
   *   [panelSet1[1]] [panelSet1[2]] [panelSet1[3]] [panelSet1[4]]
   * PanelGrid-2
   *   [panelSet1[1]] [panelSet1[2]] [panelSet1[3]] [panelSet1[4]]
   * </pre>
   * In the same example, if pagination is set to 3 and maxRows is set to 2, then each page would contain 2 rows.
   * <pre>
   * PanelGrid-1
   *   [panelSet1[1]] [panelSet1[2]]
   *   [panelSet2[1]] [panelSet2[2]]
   * PanelGrid-2
   *   [panelSet1[1]] [panelSet1[2]]
   *   [panelSet2[1]] [panelSet2[2]]
   * </pre>
   * 
   */
  List<PanelGrid> build() throws Exception;

}
