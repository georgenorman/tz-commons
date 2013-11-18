package com.thruzero.common.web.model.container;

import java.util.List;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.web.model.css.StyleClass;

/**
 * A simple panel for visually separating content.
 *
 * @author George Norman
 */
public class DividerPanel extends AbstractPanel {
  private final String description;

  /**
   * @param content raw HTML used to render the panel's contents.
   */
  public DividerPanel(String id, String title, String titleLink, StyleClass headerStyleClass, List<InfoNodeElement> toolbar, String description) {
    super(id, title, titleLink, null, headerStyleClass, toolbar);

    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
