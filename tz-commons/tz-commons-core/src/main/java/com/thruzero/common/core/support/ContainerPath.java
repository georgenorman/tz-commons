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

import java.io.Serializable;

import com.thruzero.common.core.support.EntityPath.PathAndNameValidator;

/**
 * An instance represents a path to a container of entities. Examples of valid paths (regardless of OS): "/", "/foo/",
 * "/foo/bar/".
 *
 * @author George Norman
 */
public class ContainerPath implements Serializable, Cloneable {
  private static final long serialVersionUID = 1L;

  public static final String CONTAINER_PATH_SEPARATOR = "/";

  private static final PathAndNameValidator pathAndNameValidator = new PathAndNameValidator();

  private final String path;

  public ContainerPath() {
    this.path = CONTAINER_PATH_SEPARATOR;
  }

  public ContainerPath(String path) {
    pathAndNameValidator.validateParentPath(path);

    this.path = path;
  }

  public ContainerPath(ContainerPath parent, String path) {
    pathAndNameValidator.validateChildPath(path);

    this.path = parent.getPath() + path;
  }

  protected ContainerPath(ContainerPath copyFrom) {
    this.path = copyFrom.getPath();
  }

  @Override
  public Object clone() throws CloneNotSupportedException {
    return super.clone();
  }

  public String getPath() {
    return path;
  }

  public ContainerPath getParent() { // TODO-p1(george) test this
    ContainerPath result = null;

    if (path != null && path.length() > 1) {
      int parentTrailingSeparator = path.lastIndexOf(CONTAINER_PATH_SEPARATOR, path.length() - 1); // skip trailing slash to get parent's trailing slash

      result = new ContainerPath(path.substring(0, parentTrailingSeparator));
    }

    return result;
  }

  @Override
  public String toString() {
    return path;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((path == null) ? 0 : path.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ContainerPath other = (ContainerPath)obj;
    if (path == null) {
      if (other.path != null)
        return false;
    } else if (!path.equals(other.path))
      return false;
    return true;
  }
}
