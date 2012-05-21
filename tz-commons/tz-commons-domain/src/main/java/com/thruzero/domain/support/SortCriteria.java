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
package com.thruzero.domain.support;

import java.io.Serializable;

/**
 * An instance represents which column in a table should be used for sorting its contents and in which order.
 *
 * @author George Norman
 */
public class SortCriteria implements Serializable {
  private static final long serialVersionUID = 1L;

  private String sortColumnName;
  private boolean sortAscending;

  public String getSortColumnName() {
    return sortColumnName;
  }

  public void setSortColumnName(final String sortColumnName) {
    this.sortColumnName = sortColumnName;
  }

  public boolean isSortAscending() {
    return sortAscending;
  }

  public void setSortAscending(final boolean sortAscending) {
    this.sortAscending = sortAscending;
  }

}
