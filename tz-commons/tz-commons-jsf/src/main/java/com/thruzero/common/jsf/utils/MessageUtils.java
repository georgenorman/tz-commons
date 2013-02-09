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
package com.thruzero.common.jsf.utils;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * Static utility methods pertaining to {@code FacesMessage} objects.
 *
 * @author George Norman
 */
public class MessageUtils {
  private static final String STICKY_MESSAGE_CACHE_ID = "com.thruzero.common.jsf.utils.MessageUtils.stickyMessageCache";

  // ----------------------------------------------------
  // StickyMessage
  // ----------------------------------------------------

  public static class StickyMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String clientId;
    private FacesMessage facesMessage;

    public StickyMessage(String clientId, FacesMessage facesMessage) {
      this.clientId = clientId;
      this.facesMessage = facesMessage;

      @SuppressWarnings("unchecked")
      Set<StickyMessage> cache = (Set<StickyMessage>)FacesUtils.getSession(false).getAttribute(STICKY_MESSAGE_CACHE_ID);
      if (cache == null) {
        cache = new HashSet<StickyMessage>();
        FacesUtils.getSession(false).setAttribute(STICKY_MESSAGE_CACHE_ID, cache);
      }
      cache.add(this);
    }

    public String getClientId() {
      return clientId;
    }

    public FacesMessage getFacesMessage() {
      return facesMessage;
    }
  }

  // ===========================================================================================
  // MessageUtils
  // ===========================================================================================

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_INFO}, and add it to the {@code FacesContext}.
   */
  public static void addInfo(String detail) {
    addInfo(null, null, detail);
  }

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_INFO}, and add it to the {@code FacesContext}.
   */
  public static void addInfo(String clientId, String summary, String detail) {
    FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
  }

  public static void addStickyInfo(String clientId, String summary, String detail) {
    StickyMessage stickyMessage = new StickyMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    FacesContext.getCurrentInstance().addMessage(clientId, stickyMessage.getFacesMessage());
  }

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_WARN}, and add it to the {@code FacesContext}.
   */
  public static void addWarn(String detail) {
    addWarn(null, null, detail);
  }

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_WARN}, and add it to the {@code FacesContext}.
   */
  public static void addWarn(String clientId, String summary, String detail) {
    FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
  }

  public static void addStickyWarn(String clientId, String summary, String detail) {
    StickyMessage stickyMessage = new StickyMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
    FacesContext.getCurrentInstance().addMessage(clientId, stickyMessage.getFacesMessage());
  }

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_ERROR}, and add it to the {@code FacesContext}.
   */
  public static void addError(Exception error) {
    addError(null, error);
  }

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_ERROR}, and add it to the {@code FacesContext}.
   */
  public static void addError(String clientId, Exception error) {
    addError(clientId, null, error);
  }

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_ERROR}, and add it to the {@code FacesContext}.
   */
  public static void addError(String clientId, String summary, Exception error) {
    addError(clientId, summary, error.getMessage());
  }

  public static void addStickyError(String clientId, String summary, String detail) {
    StickyMessage stickyMessage = new StickyMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    FacesContext.getCurrentInstance().addMessage(clientId, stickyMessage.getFacesMessage());
  }

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_ERROR}, and add it to the {@code FacesContext}.
   */
  public static void addError(String clientId, String summary, String detail) {
    FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
  }

  /**
   * Create a {@code FacesMessage}, with {@code FacesMessage.SEVERITY_ERROR}, and add it to the {@code FacesContext}.
   */
  public static void addError(String detail) {
    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, null, detail));
  }

  public static Set<StickyMessage> detachStickyMessages(boolean moveToFaces) {
    @SuppressWarnings("unchecked")
    Set<StickyMessage> result = (Set<StickyMessage>)FacesUtils.getSession(false).getAttribute(STICKY_MESSAGE_CACHE_ID);

    FacesUtils.getSession(false).removeAttribute(STICKY_MESSAGE_CACHE_ID);

    if (moveToFaces && result != null) {
      for (StickyMessage stickyMessage : result) {
        FacesContext.getCurrentInstance().addMessage(stickyMessage.getClientId(), stickyMessage.getFacesMessage());
      }
    }

    return result;
  }
}
