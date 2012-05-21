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
package com.thruzero.domain.store;

/**
 * Represents the path to the base store, depending on the store type. For example, a {@code FileDataStoreContainer}
 * uses the base path as an absolute path to a the directory which it uses as the base to access files for a particular
 * {@code ContainerPath}. So, in this case, the {@code toString()} function returns the {@code rootStorePath} + base and
 * this base-store-path is then prepended to the container path (e.g., "/users/george/root/base/containerPath" =>
 * {@code "/users/george/root"} is the {@code rootStorePath}, {@code "/users/george/root/base"} is the
 * {@code baseStorePath} and {@code "/containerPath"} is relative to the base-store-path).
 *
 * @author George Norman
 */
public interface BaseStorePath {
  @Override
  String toString();
}
