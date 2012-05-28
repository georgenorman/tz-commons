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
package com.thruzero.common.core.support;

/**
 * An instance represents a simple description of an item, service, etc
 * (e.g., "GenericInfoNodeService configured using JpaTextEnvelopeDAO").
 *
 * @author George Norman
 */
public class SimpleInfo {
  private String info;
  private String compactInfo;

  public SimpleInfo(String info, String compactInfo) {
    this.info = info;
    this.compactInfo = compactInfo;
  }

  public SimpleInfo(String info) {
    this.info = info;
    this.compactInfo = info;
  }

  /** Return simple info. */
  public String getInfo() {
    return info;
  }

  /** Return a shortened version of the info (suitable for small screens). */
  public String getCompactInfo() {
    return compactInfo;
  }

  public static SimpleInfo createSimpleInfo(SimpleInfoProvider simpleInfoProvider1, SimpleInfoProvider simpleInfoProvider2) {
    return createSimpleInfo(simpleInfoProvider1, " is configured to use ", simpleInfoProvider2);
  }

  public static SimpleInfo createSimpleInfo(SimpleInfoProvider simpleInfoProvider1, String text, SimpleInfoProvider simpleInfoProvider2) {
    return new SimpleInfo(simpleInfoProvider1.getClass().getSimpleName() + text + simpleInfoProvider2.getSimpleInfo().getInfo(),
        simpleInfoProvider1.getClass().getSimpleName() + " using " + simpleInfoProvider2.getClass().getSimpleName());
  }

  public static SimpleInfo createSimpleInfo(SimpleInfoProvider simpleInfoProvider1) {
    return new SimpleInfo(simpleInfoProvider1.getClass().getSimpleName() + ".", simpleInfoProvider1.getClass().getSimpleName() + ".");
  }
}
