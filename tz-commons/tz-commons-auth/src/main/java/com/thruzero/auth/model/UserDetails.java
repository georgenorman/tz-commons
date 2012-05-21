/*
 *   Copyright 2005-2012 George Norman
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
package com.thruzero.auth.model;

import com.thruzero.domain.store.Persistent;

/**
 * Represents personal information about a registered user.
 *
 * @author George Norman
 */
public interface UserDetails extends Persistent {

  public String getFirstName();

  public void setFirstName(String firstName);

  public String getMiddleName();

  public void setMiddleName(String middleName);

  public String getLastName();

  public void setLastName(String lastName);

  public String getPrimaryEmail();

  public void setPrimaryEmail(String primaryEmail);

}
