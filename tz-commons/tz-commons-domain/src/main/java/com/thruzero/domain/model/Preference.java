/*
 *   Copyright 2010 - 2012 George Norman
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
package com.thruzero.domain.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.thruzero.domain.store.AbstractPersistent;
import com.thruzero.domain.store.Persistent;

/**
 * An instance represents a user preference within a given context.
 *
 * @author George Norman
 */
public class Preference extends AbstractPersistent {
  private static final long serialVersionUID = 1L;

  private String owner;

  private String context;
  private String name;
  private String value;

  public Preference() {
  }

  public Preference(final String owner, final String context, final String name, final String value) {
    this.owner = owner;

    this.context = context;
    this.name = name;
    this.value = value;
  }

  @Override
  public void copyFrom(final Persistent source) {
    super.copyFrom(source);

    Preference preferenceSource = (Preference)source;
    owner = preferenceSource.getOwner();
    context = preferenceSource.getContext();
    name = preferenceSource.getName();
    value = preferenceSource.getValue();
  }

  public String getOwner() {
    return owner;
  }

  /**
   * Set the subject (e.g., user) this preference is for.
   * Choose something about the owner that will never change (e.g., userId or user primary key).
   * User email address is risky, because that can change.
   */
  public void setOwner(final String owner) {
    this.owner = owner;
  }

  public String getContext() {
    return context;
  }

  /**
   * Set the situation this preference is to be used (e.g., 'Network Proxy Options', 'General').
   */
  public void setContext(final String context) {
    this.context = context;
  }

  public String getName() {
    return name;
  }

  /**
   * Set a name used to identify this preference.
   */
  public void setName(final String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public int getIntValue() {
    return Integer.parseInt(value);
  }

  public void setIntValue(final int value) {
    setValue(value + "");
  }

  public boolean getBooleanValue() {
    return Boolean.parseBoolean(value);
  }

  public void setBooleanValue(final boolean value) {
    setValue(value + "");
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this).append("owner", owner).append("context", context).append("name", name).toString();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((context == null) ? 0 : context.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((owner == null) ? 0 : owner.hashCode());
    result = prime * result + ((value == null) ? 0 : value.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Preference other = (Preference)obj;
    if (context == null) {
      if (other.context != null)
        return false;
    } else if (!context.equals(other.context))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (owner == null) {
      if (other.owner != null)
        return false;
    } else if (!owner.equals(other.owner))
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

}
