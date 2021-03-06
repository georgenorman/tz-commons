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

import java.io.Serializable;

import org.apache.log4j.Logger;

import com.thruzero.common.jsf.utils.FacesUtils;

/**
 * The commandLink JSF tag requires a backing bean for the action attribute - this bean satisfies that requirement.
 * For example, the format of an action attribute must be: action="#{backingBean.action}". If you want the
 * outcome of the action to be "/apps/demo/dashboard?faces-redirect=true", you can't simply pass it in to
 * the action attribute as shown below:
 * <pre>
 *   {@literal <h:commandLink action="/apps/demo/dashboard?faces-redirect=true" ...    ### Redirect will not work }
 * </pre>
 *
 * The ActionBean class is designed to read the navigation outcome from the request. This enables a JSF page
 * to pass in the desired outcome to a particular page fragment using the standard {@code param} tag.
 * <p>
 *
 * Example - passing action value via facelet:
 *
 * <pre>
 * {@code
 *    <ui:include src="/common/compositions/standardHeader.xhtml">
 *       <ui:param name="backAction" value="/apps/demo/dashboard?faces-redirect=true"/>
 *    </ui:include>
 * }
 * </pre>
 *
 * Example - facelet using value in command link:
 *
 * <pre>
 * &lt;h:commandLink action=&quot;#{actionBean.action}&quot; immediate=&quot;true&quot; rendered=&quot;#{not empty backAction}&quot;&gt;
 *    &lt;f:param name=&quot;action&quot; value=&quot;#{backAction}&quot; /&gt;
 *    &lt;p:graphicImage url=&quot;/images/common/icons/nav/action_back.png&quot; /&gt;
 * &lt;/h:commandLink&gt;
 * </pre>
 *
 * @author George Norman
 */
@javax.faces.bean.ManagedBean(name = "actionBean")
@javax.faces.bean.RequestScoped
public class ActionBean implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = Logger.getLogger(ActionBean.class);

  public String action() {
    String action = FacesUtils.getRequest().getParameter("action");

    logger.debug("ActionBean returning action: " + action);

    return action;
  }
}
