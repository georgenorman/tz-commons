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
package com.thruzero.common.core.support;

import java.util.Comparator;
import java.util.Date;

/**
 * An abstract base class that provides common functionality for sorting <i>collection-based</i>, single and
 * multi-column tables. It provides helper functions for comparing strings, dates and numbers as well as managing sort
 * direction. This class is not intended to be used for database tables (a {@code Criterion} object would typically be
 * used in those cases).
 *
 * <p>
 * Below is an example of an {@code EventComparator}, that could be used for an {@code Event} that had two columns -
 * name and start-date:
 *
 * <pre>
 * <code>
 *   public static class <b>EventComparator</b> extends <b>AbstractSortComparator</b>&lt;Event&gt; {
 *
 *     public EventComparator( String sortColumnName, SortDirection sortDirection ) {
 *       super(sortColumnName,sortDirection);
 *     }
 *
 *     public int compare(Event e1, Event e2) {
 *       int result = 0;
 *
 *       if ( EventColumnNames.NAME.equals(getSortColumnName()) ) {
 *         result = doCompare(e1.getName(),e2.getName());
 *       } else if ( EventColumnNames.START_DATE.equals(getSortColumnName()) ) {
 *         result = doCompare(e1.getStartDate(),e2.getStartDate());
 *       }
 *
 *       return result;
 *     }
 *   }
 * </code>
 * </pre>
 *
 * Clients would use this comparator, as follows:
 *
 * <pre>
 * {@code
 *   Collections.sort( eventRows, new EventComparator( sortColumnName, sortDirection ) );
 * }
 * </pre>
 *
 * @author George Norman
 * @param <T> Type of Object compared.
 */
public abstract class AbstractSortComparator<T> implements Comparator<T> {
  private String sortColumnName;
  private SortDirection sortDirection;

  /** An enum for specifying the sort direction; either ASCENDING or DESCENDING. */
  public enum SortDirection {
    ASCENDING, DESCENDING
  };

  /** Single-column table sort comparator. */
  protected AbstractSortComparator(final SortDirection sortDirection) {
    this(null, sortDirection);
  }

  /** multi-column table sort comparator, using boolean to determine sort-direction. */
  protected AbstractSortComparator(final String sortColumnName, final boolean sortAscending) {
    this(sortColumnName, sortAscending ? SortDirection.ASCENDING : SortDirection.DESCENDING);
  }

  /** multi-column table sort comparator. */
  protected AbstractSortComparator(final String sortColumnName, final SortDirection sortDirection) {
    this.sortColumnName = sortColumnName;
    this.sortDirection = sortDirection;
  }

  public String getSortColumnName() {
    return sortColumnName;
  }

  public void setSortColumnName(final String sortColumnName) {
    this.sortColumnName = sortColumnName;
  }

  /** Return the SortDirection, if sorting is supported; otherwise, return null (no sorting is performed). */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  public void setSortDirection(final SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  public boolean isSortAscending() {
    return sortDirection == null ? true : sortDirection == SortDirection.ASCENDING;
  }

  // helper functions /////////////////////////////////////////////////////////

  protected int doCompare(final String s1, final String s2) {
    return applySortDirection(s1.compareTo(s2));
  }

  protected int doCompare(final Integer i1, final Integer i2) {
    return applySortDirection(i1.compareTo(i2));
  }

  protected int doCompare(final Date d1, final Date d2) {
    return applySortDirection(d1.compareTo(d2));
  }

  protected int applySortDirection(final int compareResult) {
    return isSortAscending() ? compareResult : -compareResult;
  }
}
