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

/**
 * Domain service implementations.
 * <p>
 * The common domain services are designed to be pluggable with different implementations, so if desired, they
 * can be tailored to a particular application. Paired with each common service API is a default implementation.
 * Some of these implementations are further designed to have pluggable persistence implementations. For example,
 * the GenericSettingService has a pluggable SettingDAO, that can be implemented for the file system, a SQL
 * database, a NoSQL database, etc.
 *
 */
package com.thruzero.domain.service.impl;

