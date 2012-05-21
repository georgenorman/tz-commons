/*
 *   Copyright 2010 George Norman
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

import org.apache.commons.lang3.StringUtils;

/**
 * An instance of this class represents the time of day. It supports 12 and 24-hour mode.
 *
 * @author George Norman
 */
public class Time implements Comparable<Time> {
  private boolean mode24Hr = true;
  private byte hours;
  private byte minutes;

  public byte getHours() {
    return hours;
  }

  public void setHours(final byte hours) {
    this.hours = hours;
  }

  public byte getMinutes() {
    return minutes;
  }

  public void setMinutes(final byte minutes) {
    this.minutes = minutes;
  }

  public boolean isMode24Hr() {
    return mode24Hr;
  }

  public void setMode24Hr(final boolean mode24Hr) {
    this.mode24Hr = mode24Hr;
  }

  /**
   * @return true if hours or minutes are set
   */
  public boolean isSet() {
    return hours != 0 || minutes != 0;
  }

  public boolean isBefore(final Time otherTime) {
    boolean result = hours < otherTime.getHours() || minutes < otherTime.getMinutes();

    return result;
  }

  @Override
  public String toString() {
    return StringUtils.leftPad(Byte.toString(hours), 2, "0") + ":" + StringUtils.leftPad(Byte.toString(minutes), 2, "0");
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + hours;
    result = prime * result + minutes;
    result = prime * result + (mode24Hr ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    return compareTo((Time)obj) == 0;
  }

  @Override
  public int compareTo(final Time o) {
    int result = 0;

    if (hours > o.hours) {
      result = 1;
    } else if (hours < o.hours) {
      result = -1;
    } else {
      if (minutes > o.minutes) {
        result = 1;
      } else if (minutes < o.minutes) {
        result = -1;
      }
    }

    return result;
  }
}
