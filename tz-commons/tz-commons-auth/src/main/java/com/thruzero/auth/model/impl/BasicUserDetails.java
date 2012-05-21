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
package com.thruzero.auth.model.impl;

import com.thruzero.auth.model.UserDetails;
import com.thruzero.domain.store.AbstractPersistent;
import com.thruzero.domain.store.Persistent;

/**
 * Simple implementation of UserDetails.
 *
 * @author George Norman
 */
public class BasicUserDetails extends AbstractPersistent implements UserDetails {
  private static final long serialVersionUID = 1L;

  private String firstName;
  private String middleName;
  private String lastName;

  private String primaryEmail;
  private String secondaryEmail;
  private String workEmail;

  private String primaryPhone;
  private String secondaryPhone;
  private String workPhone;

  @Override
  public void copyFrom(final Persistent source) {
    super.copyFrom(source);

    BasicUserDetails basicUserDetailsSource = (BasicUserDetails)source;
    firstName = basicUserDetailsSource.getFirstName();
    middleName = basicUserDetailsSource.getMiddleName();
    lastName = basicUserDetailsSource.getLastName();

    primaryEmail = basicUserDetailsSource.getPrimaryEmail();
    secondaryEmail = basicUserDetailsSource.getSecondaryEmail();
    workEmail = basicUserDetailsSource.getWorkEmail();

    primaryPhone = basicUserDetailsSource.getPrimaryPhone();
    secondaryPhone = basicUserDetailsSource.getSecondaryPhone();
    workPhone = basicUserDetailsSource.getWorkPhone();
  }

  @Override
  public String getFirstName() {
    return firstName;
  }

  @Override
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @Override
  public String getMiddleName() {
    return middleName;
  }

  @Override
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }

  @Override
  public String getLastName() {
    return lastName;
  }

  @Override
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String getPrimaryEmail() {
    return primaryEmail;
  }

  @Override
  public void setPrimaryEmail(String primaryEmail) {
    this.primaryEmail = primaryEmail;
  }

  public String getSecondaryEmail() {
    return secondaryEmail;
  }

  public void setSecondaryEmail(String secondaryEmail) {
    this.secondaryEmail = secondaryEmail;
  }

  public String getWorkEmail() {
    return workEmail;
  }

  public void setWorkEmail(String workEmail) {
    this.workEmail = workEmail;
  }

  public String getPrimaryPhone() {
    return primaryPhone;
  }

  public void setPrimaryPhone(String primaryPhone) {
    this.primaryPhone = primaryPhone;
  }

  public String getSecondaryPhone() {
    return secondaryPhone;
  }

  public void setSecondaryPhone(String secondaryPhone) {
    this.secondaryPhone = secondaryPhone;
  }

  public String getWorkPhone() {
    return workPhone;
  }

  public void setWorkPhone(String workPhone) {
    this.workPhone = workPhone;
  }

}
