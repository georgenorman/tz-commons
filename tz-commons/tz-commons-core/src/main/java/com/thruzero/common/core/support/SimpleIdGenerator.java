/*
 *   Copyright 2011 George Norman
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

import java.util.concurrent.atomic.AtomicLong;

/**
 * A simple generator of identifiers that contains 2^64 unique identifiers, within the lifetime of the instance. If the
 * instance is recreated (e.g., due to application restart), then the sequence begins all over again.
 *
 * @author George Norman
 */
public final class SimpleIdGenerator {
  private static SimpleIdGenerator instance = new SimpleIdGenerator();

  private AtomicLong id = new AtomicLong(1000); // starting off at 1000 (to improve reliability of tests).

  private SimpleIdGenerator() {
  }

  public long getNextId() {
    return id.incrementAndGet();
  }

  public String getNextIdAsString() {
    return String.valueOf(getNextId());
  }

  public static SimpleIdGenerator getInstance() {
    return instance;
  }
}
