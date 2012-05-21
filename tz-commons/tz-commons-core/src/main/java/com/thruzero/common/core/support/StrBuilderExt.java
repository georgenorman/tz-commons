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

import org.apache.commons.lang3.text.StrBuilder;

/**
 * Extensions to the apache.commons {@code StrBuilder} class.
 * 
 * @author George Norman
 */
public class StrBuilderExt extends StrBuilder {

  public StrBuilderExt() {
  }

  public StrBuilderExt(final int initialCapacity) {
    super(initialCapacity);
  }

  public StrBuilderExt(final String str) {
    super(str);
  }

  public StrBuilderExt append(final String... str) {
    this.appendAll(str);

    return this;
  }

  public StrBuilderExt appendln(final String... str) {
    this.appendAll(str);
    this.appendNewLine();

    return this;
  }
}
