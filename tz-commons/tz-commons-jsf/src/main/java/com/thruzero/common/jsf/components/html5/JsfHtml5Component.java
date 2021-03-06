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
package com.thruzero.common.jsf.components.html5; // TODO-p0(george) Rename components to component.

/**
 * Interface for common methods and constants required by the ThruZero HTML5 components.
 *
 * @author George Norman
 */
public interface JsfHtml5Component {
  // TODO-p0(george) Effective Java, Item 19: Use interfaces only to define types. Don't use JsfHtml5Component as a "constant interface"
  String COMPONENT_FAMILY = "thruzero.html5.components.family";
}
