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
package com.thruzero.common.core.support;

/**
 * Used to sort a {@code Collection} of KeyValuePair instances with a given sort-direction.
 * 
 * @author George Norman
 */
public class KeyValuePairRowComparator extends AbstractSortComparator<KeyValuePair> {

  // ---------------------------------------------
  // KeyValuePairRowColumnNames
  // ---------------------------------------------

  public interface KeyValuePairRowColumnNames {
    String KEY = "key";
    String VALUE = "value";
  }

  // =================================================================
  // KeyValuePairRowComparator
  // =================================================================

  public KeyValuePairRowComparator(final String sortColumnName, final boolean sortAscending) {
    super(sortColumnName, sortAscending);
  }

  @Override
  public int compare(final KeyValuePair a1, final KeyValuePair a2) {
    int result = 0;

    if (KeyValuePairRowColumnNames.KEY.equals(getSortColumnName())) {
      result = doCompare(a1.getKey(), a2.getKey());
    } else if (KeyValuePairRowColumnNames.VALUE.equals(getSortColumnName())) {
      result = doCompare(a1.getValueAsString(), a2.getValueAsString());
    }

    return isSortAscending() ? result : -result;
  }

}
