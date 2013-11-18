package com.thruzero.common.web.model.container.builder.xml;

import com.thruzero.common.core.infonode.InfoNodeElement;
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

  public XmlDividerPanelBuilder(InfoNodeElement panelNode) {
    super(panelNode);
  }

  @Override
  public AbstractPanel build() throws Exception {
    DividerPanel result = new DividerPanel(getPanelId(), getPanelTitle(), getPanelTitleLink(), getPanelHeaderStyleClass(), getToolbar());

    return result;
  }

}
