/*
 *   Copyright 2011-2012 George Norman
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
package com.thruzero.common.jsf.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.NavigationHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

/**
 * Static utility methods pertaining to JSF.
 *
 * @author George Norman
 */
public class FacesUtils {
  public static final String REDIRECT_URL = "redirectUrl";
  public static final String REDIRECT_UTIL = "/utils/redirect.xhtml"; // TODO-p1(george) make this a servlet

  public static void handleNavigation(String fromAction, String outcome) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();

    navigationHandler.handleNavigation(facesContext, fromAction, outcome);
  }

  public static HttpServletRequest getRequest() {
    return (HttpServletRequest)getExternalContext().getRequest();
  }

  public static HttpServletResponse getResponse() {
    return (HttpServletResponse)getExternalContext().getResponse();
  }

  /**
   * @param create
   * @return the current valid session associated with this request, if create is false or, if necessary, creates a new
   * session for the request, if create is true.
   */
  public static HttpSession getSession(boolean create) {
    FacesContext facesContext = FacesContext.getCurrentInstance();

    return (HttpSession)facesContext.getExternalContext().getSession(create);
  }

  public static String getRequestHeader(String name) {
    return getRequest().getHeader(name);
  }

  public static String getServletContextName() {
    ServletContext ServletContext = (ServletContext)getExternalContext().getContext();

    return ServletContext.getServletContextName();
  }

  public static String getUrlWithContextName(String url) {
    return "/" + getServletContextName() + url;
  }

  public static File getWebInfDir() {
    File webAppPath = new File(getExternalContext().getRealPath("/"));
    String warDeployPath = webAppPath.getAbsolutePath();
    if (StringUtils.isEmpty(warDeployPath)) {
      return null;
    }

    File webInfDir = new File(warDeployPath, "WEB-INF");
    if (webInfDir.exists() && webInfDir.isDirectory()) {
      return webInfDir;
    }

    return null;
  }

  public static Serializable locateManagedBean(String name) {
    Serializable result = (Serializable)getExpressionValue("#{" + name + "}", Serializable.class);

    return result;
  }

  private static Object getExpressionValue(String el, Class<?> cls) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
    ValueExpression valueExpression = expressionFactory.createValueExpression(facesContext.getELContext(), el, cls);

    Object result = valueExpression.getValue(facesContext.getELContext());

    return result;
  }

  public static ExternalContext getExternalContext() {
    return FacesContext.getCurrentInstance().getExternalContext();
  }

  /**
   * create a sequence of SelectItem's starting from the given start param, incrementing by the given increment, until
   * the maxItems number of items has been completed.
   *
   * @param start first item value.
   * @param maxItems total number of items (plus one if provideEmptyValue is true)
   * @param increment
   * @param provideEmptyValue if true, then an empty SelectItem will be added as the first item.
   * @return
   */
  public static SelectItem[] createSelectSequence(int start, int maxItems, int increment, boolean provideEmptyValue) {
    maxItems += provideEmptyValue ? 1 : 0;
    SelectItem[] result = new SelectItem[maxItems];
    int item = start;

    if (provideEmptyValue) {
      result[0] = new SelectItem("");
    }

    for (int i = provideEmptyValue ? 1 : 0; i < maxItems; i++) {
      result[i] = new SelectItem(item + "");
      item += increment;
    }

    return result;
  }
}
