/*
 *   Copyright 2011-2012 George Norman
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
package com.thruzero.common.core.locator;

import org.apache.commons.lang3.StringUtils;

/**
 * Binds an interface to a concrete-implementation. Optionally, parameters can be given to the concrete-implementation
 * at construction-time. An interface is not required to be used for the interface parameter; a class can be used just
 * as well. Interfaces are typically used at the framework level, to allow for clients to choose alternate
 * implementations. However, at the application level, implementation types rarely change, so it's appropriate to use
 * the same class for interface and implementation.
 * <p>
 * Here are two examples:
 *
 * <pre>
 * <code>
 * // bind an interface to a specific implementation
 * new InterfaceToClassBinding<Service>(SettingService.class, GenericSettingService.class);
 *
 * // use an implementation as the interface, binding it to itself (in the case of an application-level service).
 * new InterfaceToClassBinding<Service>(AssignmentService.class, AssignmentService.class);
 * </code>
 * </pre>
 */
public class InterfaceToClassBinding<T> {
  private String interfaceName;
  private String instanceClassName;
  private InitializationStrategy initStrategy;

  public InterfaceToClassBinding(final Class<? extends T> interfaceType, final Class<? extends T> instanceType) {
    this(interfaceType, instanceType, null);
  }

  public InterfaceToClassBinding(final Class<? extends T> interfaceType, final Class<? extends T> instanceType, final InitializationStrategy initStrategy) {
    this(interfaceType.getName(), instanceType == null ? null : instanceType.getName(), initStrategy);
  }

  public InterfaceToClassBinding(final String interfaceName, final String className) {
    this(interfaceName, className, null);
  }

  public InterfaceToClassBinding(final String interfaceName, final String instanceClassName, final InitializationStrategy initStrategy) {
    if (StringUtils.isEmpty(interfaceName)) {
      throw new IllegalArgumentException("InterfaceToClassBinding Error: interfaceName is required");
    }
    this.interfaceName = interfaceName;
    this.instanceClassName = instanceClassName;
    this.initStrategy = initStrategy;
  }

  public String getInterfaceName() {
    return interfaceName;
  }

  public void setInterfaceName(final String interfaceName) {
    this.interfaceName = interfaceName;
  }

  public String getInstanceClassName() {
    return instanceClassName;
  }

  public void setInstanceClassName(final String className) {
    this.instanceClassName = className;
  }

  public InitializationStrategy getInitStrategy() {
    return initStrategy;
  }

  public void setInitStrategy(InitializationStrategy initStrategy) {
    this.initStrategy = initStrategy;
  }

  @Override
  public String toString() {
    return "InterfaceToClassBinding [interfaceName=" + interfaceName + ", instanceClassName=" + instanceClassName + "]";
  }

}
