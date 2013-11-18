package com.thruzero.common.web.model.container.builder.xml;

import com.thruzero.common.core.infonode.InfoNodeElement;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.web.model.container.AbstractPanel;
import com.thruzero.common.web.model.container.DividerPanel;
import com.thruzero.common.web.model.container.builder.xml.AbstractXmlPanelBuilder.XmlPanelBuilderAnnotation;

/**
 * An instance represents a builder of DividerPanel objects.
 *
 * @author George Norman
 */
@XmlPanelBuilderAnnotation(panelTypeName = "dividerPanel")
public class XmlDividerPanelBuilder extends AbstractXmlPanelBuilder {

  /**
   * Name of the attribute representing an optional description for content under the divider. The default name is "description" and can be changed via config.
   */
  public static final String DESCRIPTION_ID = ConfigLocator.locate().getValue(AbstractXmlPanelBuilder.class.getName(), "description", "description");

  public XmlDividerPanelBuilder(InfoNodeElement panelNode) {
    super(panelNode);
  }

  @Override
  public AbstractPanel build() throws Exception {
    DividerPanel result = new DividerPanel(getPanelId(), getPanelTitle(), getPanelTitleLink(), getPanelHeaderStyleClass(), getToolbar(), getDescription());

    return result;
  }

  protected String getDescription() throws Exception {
    String result = null;
    InfoNodeElement descriptionNode = getPanelNode().findElement(DESCRIPTION_ID);
    
    if (descriptionNode != null) {
      result = descriptionNode.getText();
    }

    return result;
  }

}
