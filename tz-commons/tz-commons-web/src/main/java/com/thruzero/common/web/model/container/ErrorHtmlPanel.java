package com.thruzero.common.web.model.container;


/**
 * Convenience panel for displaying errors (e.g., when a panel builder encounters and error).
 */
public class ErrorHtmlPanel extends HtmlPanel {

  public ErrorHtmlPanel(String id, String title, String errorContent) {
    super(id, title, title, null, null, null, errorContent);
  }
}
