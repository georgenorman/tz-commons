/*
 *   Copyright 2005 George Norman
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
 * A <i>marker interface</i> to show that a class implements the <a
 * href="http://en.wikipedia.org/wiki/Singleton_pattern">Singleton</a> pattern (only one instance of the class is
 * allowed).
 * <p>
 * Note: {@link com.thruzero.common.core.locator.RegistryLocatorStrategy RegistryLocatorStrategy} will assert that the
 * instance class implement this interface.
 * <p>
 * <i>Note</i>: not all {@code Singleton}s are locatable.
 * 
 * @author George Norman
 */
public interface Singleton {

}
