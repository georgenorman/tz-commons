/*
 *   Copyright 2009 George Norman
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

import com.thruzero.common.core.bookmarks.SettingBookmark;
import com.thruzero.domain.store.AbstractPersistent;
import com.thruzero.domain.store.Persistent;

/**
 * A configuration setting that is typically saved to a database.
 *
 * @author George Norman
 */
@SettingBookmark
public class Setting extends AbstractPersistent {
  private static final long serialVersionUID = 1L;

  private String name;
  private String context;

  private String accessPrivilege;
  private String type;
  private String value;
  private String description;
  private boolean editable;

  public static interface Types {
    String INPUT_TEXT = "inputText";
    String INPUT_TEXT_AREA = "inputTextarea";
    String INPUT_PASSWORD = "inputPassword";
  }

  @Override
  public void copyFrom(final Persistent source) {
    super.copyFrom(source);

    Setting settingSource = (Setting)source;
    name = settingSource.getName();
    context = settingSource.getContext();
    accessPrivilege = settingSource.getAccessPrivilege();
    type = settingSource.getType();
    value = settingSource.getValue();
    description = settingSource.getDescription();
    editable = settingSource.isEditable();
  }

  public String getAccessPrivilege() {
    return accessPrivilege;
  }

  public void setAccessPrivilege(final String accessPrivilege) {
    this.accessPrivilege = accessPrivilege;
  }

  public String getContext() {
    return context;
  }

  public void setContext(final String context) {
    this.context = context;
  }

  public String getType() {
    return type;
  }

  public void setType(final String type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public boolean isEditable() {
    return editable;
  }

  public void setEditable(final boolean editable) {
    this.editable = editable;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((accessPrivilege == null) ? 0 : accessPrivilege.hashCode());
    result = prime * result + ((context == null) ? 0 : context.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + (editable ? 1231 : 1237);
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
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
    Setting other = (Setting)obj;
    if (accessPrivilege == null) {
      if (other.accessPrivilege != null)
        return false;
    } else if (!accessPrivilege.equals(other.accessPrivilege))
      return false;
    if (context == null) {
      if (other.context != null)
        return false;
    } else if (!context.equals(other.context))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (editable != other.editable)
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    if (value == null) {
      if (other.value != null)
        return false;
    } else if (!value.equals(other.value))
      return false;
    return true;
  }

}
