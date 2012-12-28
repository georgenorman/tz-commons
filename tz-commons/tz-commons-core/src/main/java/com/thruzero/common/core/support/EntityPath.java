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

import org.apache.commons.lang3.StringUtils;

/**
 * An instance of this class represents an "absolute" path to a single entity in a data store, where the ROOT data store path is
 * considered the <b>ROOT</b> of the EntityPath. The path MUST always begin with a forward slash and never end with one
 * (regardless of file system). Examples of valid EntityPath's are:
 *
 * <pre>
 * "/ex1.txt"
 * "/bar/ex2.txt"
 * "/bar/ex3.xml"
 * </pre>
 *
 * If the EntityPath is used in a file system based data store and the ROOT store path is {@code "/home/foo"}, then the
 * resulting file system paths for the previous examples would be:
 *
 * <pre>
 * "/home/foo/ex1.txt"
 * "/home/foo/bar/ex2.txt"
 * "/home/foo/bar/ex3.xml".
 * </pre>
 *
 * @author George Norman
 */
public final class EntityPath implements Serializable, Cloneable, Comparable<EntityPath> {
  private static final long serialVersionUID = 1L;

  private static final PathAndNameValidator pathAndNameValidator = new PathAndNameValidator();

  private final String rootDataStorePath;
  private final ContainerPath containerPath;
  private final String entityName;

  // --------------------------------------------------
  // PathAndNameValidator
  // --------------------------------------------------

  public static class PathAndNameValidator {

    public void validateParentPath(String parentPath) {
      if (StringUtils.isEmpty(parentPath)) {
        throw new RuntimeException("Invalid Container path. Parent path can't be empty.");
      }

      if (!parentPath.startsWith(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
        throw new RuntimeException("Invalid Container path. Relative paths not supported. Path must begin with a container separator ('" + ContainerPath.CONTAINER_PATH_SEPARATOR + "'): "
            + parentPath);
      }

      if (!parentPath.endsWith(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
        throw new RuntimeException("Invalid Container path. Parent path must end with a container separator ('" + ContainerPath.CONTAINER_PATH_SEPARATOR + "'): " + parentPath);
      }

      validateSegments(parentPath);
    }

    public void validateChildPath(String childPath) {
      if (StringUtils.isEmpty(childPath)) {
        throw new RuntimeException("Invalid Container path. Child path can't be empty.");
      }

      if (childPath.startsWith(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
        throw new RuntimeException("Invalid Container path. Child path can't begin with a container separator ('" + ContainerPath.CONTAINER_PATH_SEPARATOR + "'): " + childPath);
      }

      if (!childPath.endsWith(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
        throw new RuntimeException("Invalid Container path. Child path must end with a container separator ('" + ContainerPath.CONTAINER_PATH_SEPARATOR + "'): " + childPath);
      }

      validateSegments(childPath);
    }

    public void validateEntityName(String name) {
      if (StringUtils.isEmpty(name)) {
        throw new RuntimeException("Invalid name. Name can't be empty.");
      }

      if (name.contains(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
        throw new RuntimeException("Invalid name. Name can't contain a path separator ('/'): " + name);
      }
    }

    public void validateSegments(String pathSegments) {
      String[] segments = StringUtils.split(pathSegments, ContainerPath.CONTAINER_PATH_SEPARATOR);
      for (String string : segments) {
        if (StringUtils.isEmpty(string)) {
          throw new RuntimeException("Invalid Container path. Path segments can't be empty: " + pathSegments);
        }
      }
    }
  }

  // ===================================================================
  // EntityPath
  // ===================================================================

  public EntityPath() {
    rootDataStorePath = null;
    containerPath = new ContainerPath();
    entityName = null;
  }

  /**
   * Construct an instance from the given {@code entityPath}, which must begin but not end with a "/".
   */
  public EntityPath(String entityPath) {
    if (StringUtils.isEmpty(entityPath)) {
      throw new RuntimeException("Invalid entity path. Path can't be empty.");
    }

    // Ensure path begins with CONTAINER_PATH_SEPARATOR
    if (!entityPath.startsWith(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
      entityPath = ContainerPath.CONTAINER_PATH_SEPARATOR + entityPath;
    }

    // split container and name
    String containerPathAsString = StringUtils.substringBeforeLast(entityPath, ContainerPath.CONTAINER_PATH_SEPARATOR);
    rootDataStorePath = null;
    containerPath = new ContainerPath(StringUtils.isEmpty(containerPathAsString) ? ContainerPath.CONTAINER_PATH_SEPARATOR : containerPathAsString + ContainerPath.CONTAINER_PATH_SEPARATOR);
    entityName = StringUtils.substringAfterLast(entityPath, ContainerPath.CONTAINER_PATH_SEPARATOR);
  }

  /**
   * Construct an instance from the given {@code containerPath} and {@code entityName} (the entityName can't contain a
   * "/").
   */
  public EntityPath(ContainerPath containerPath, String entityName) {
    pathAndNameValidator.validateEntityName(entityName);

    if (containerPath == null) {
      throw new IllegalArgumentException("ContainerPath is required.");
    }

    this.rootDataStorePath = null;
    this.containerPath = containerPath;
    this.entityName = entityName;
  }

  public EntityPath(String rootDataStorePath, ContainerPath containerPath, String entityName) {
    pathAndNameValidator.validateEntityName(entityName);

    if (containerPath == null) {
      throw new IllegalArgumentException("ContainerPath is required.");
    }

    this.rootDataStorePath = rootDataStorePath;
    this.containerPath = containerPath;
    this.entityName = entityName;
  }

  /**
   * Construct an instance from the given {@code containerPath} (which must begin and end with a "/") and
   * {@code entityName} (which can't contain a "/").
   */
  public EntityPath(String containerPath, String entityName) {
    this(new ContainerPath(containerPath), entityName);
  }

  protected EntityPath(EntityPath copyFrom) {
    rootDataStorePath = copyFrom.getRootDataStorePath();
    containerPath = new ContainerPath(copyFrom.getContainerPath());
    entityName = copyFrom.getEntityName();
  }

  @Override
  public Object clone() {
    try {
      return super.clone();
    } catch (CloneNotSupportedException e) {
      throw new RuntimeException("ERROR: Clone not supported.", e);
    }
  }

  /**
   * Attempt to create an EntityPath instance from the given {@code entityPath} string, by adding missing path
   * separators (e.g., if the given entityPath does not begin with a "/", then add it).
   */
  public static EntityPath createFromString(String entityPath) {
    EntityPath result;

    if (StringUtils.isEmpty(entityPath)) {
      result = new EntityPath();
    } else {
      if (entityPath.startsWith(ContainerPath.CONTAINER_PATH_SEPARATOR)) {
        result = new EntityPath(entityPath);
      } else {
        result = new EntityPath(new ContainerPath(), entityPath);
      }
    }

    return result;
  }

  public String getRootDataStorePath() {
    return rootDataStorePath;
  }

  public String getContainerPathAsString() {
    return containerPath.getPath();
  }

  public ContainerPath getContainerPath() {
    return containerPath;
  }

  public String getEntityName() {
    return entityName;
  }

  @Override
  public int compareTo(EntityPath o) {
    return toString().compareTo(o.toString());
  }

  @Override
  public String toString() {
    String root = "";
    if (rootDataStorePath != null) {
      root = rootDataStorePath;
    }
    return root + getContainerPathAsString() + entityName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((containerPath == null) ? 0 : containerPath.hashCode());
    result = prime * result + ((entityName == null) ? 0 : entityName.hashCode());
    result = prime * result + ((rootDataStorePath == null) ? 0 : rootDataStorePath.hashCode());
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
    EntityPath other = (EntityPath)obj;
    if (containerPath == null) {
      if (other.containerPath != null)
        return false;
    } else if (!containerPath.equals(other.containerPath))
      return false;
    if (entityName == null) {
      if (other.entityName != null)
        return false;
    } else if (!entityName.equals(other.entityName))
      return false;
    if (rootDataStorePath == null) {
      if (other.rootDataStorePath != null)
        return false;
    } else if (!rootDataStorePath.equals(other.rootDataStorePath))
      return false;
    return true;
  }

}
