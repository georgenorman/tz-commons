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
package com.thruzero.domain.dsc.store;

import com.thruzero.common.core.utils.ExceptionUtilsExt;

/**
 * An exception which can be thrown by any implementation of {@code DataStoreContainer}.
 */
public class DataStoreException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public DataStoreException(final String message) {
    super(ExceptionUtilsExt.decorateMessageLevel1(message));
  }

  public DataStoreException(final String message, final Throwable cause) {
    super(ExceptionUtilsExt.decorateMessageLevel1(message), cause);
  }
}
