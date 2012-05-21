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
package com.thruzero.common.core.service;

import com.thruzero.common.core.support.Singleton;

/**
 * Marker interface for services. Services are {@code Singletons} and are located using the {@link com.thruzero.common.core.locator.ServiceLocator ServiceLocator}.
 *
 * @author George Norman
 */
public interface Service extends Singleton {

}
