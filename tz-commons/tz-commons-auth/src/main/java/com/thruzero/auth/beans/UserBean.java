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
package com.thruzero.auth.beans;

import java.io.Serializable;

import org.apache.shiro.subject.Subject;

import com.thruzero.auth.model.User;
import com.thruzero.auth.service.UserService;
import com.thruzero.common.core.locator.ServiceLocator;

/**
 * An instance represents the state of the current user and can be used by an application
 * to display login information (e.g., login status, formal name, etc).
 *
 * @author George Norman
 */
public class UserBean implements Serializable {
  private static final long serialVersionUID = 1L;
  
  /** Guest ID ("guest") must be a valid user ID in the user data store. */
  public static final String GUEST_LOGIN_ID = "guest";
  public static final String GUEST_FORMAL_NAME = "Guest";

  public String getLoginId( ) {
    String result;
    User loggedInUser = getLoggedInUser();

    if ( loggedInUser == null ) {
      result = GUEST_LOGIN_ID;
    } else {
      result = loggedInUser.getLoginId();
    }

    return result;
  }

  public String getFormalName( ) {
    String result;
    User loggedInUser = getLoggedInUser();

    if ( loggedInUser == null ) {
      result = GUEST_FORMAL_NAME;
    } else {
      result = loggedInUser.getDetails().getFirstName() + " " + loggedInUser.getDetails().getLastName();
    }

    return result;
  }

  public boolean isLoggedIn() {
    Subject currentSubject = ServiceLocator.locate(UserService.class).getCurrentSubject();

    return currentSubject.isAuthenticated();
  }

  protected User getLoggedInUser() {
    User result = ServiceLocator.locate(UserService.class).getCurrentAuthenticatedUser();

    return result;
  }
}
