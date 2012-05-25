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
package com.thruzero.domain.dsc.fs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.thruzero.common.core.support.ContainerPath;
import com.thruzero.common.core.support.EntityPath;
import com.thruzero.domain.dao.GenericDAO.DAOException;
import com.thruzero.domain.dsc.store.DataStoreContainer;
import com.thruzero.domain.dsc.store.DataStoreException;
import com.thruzero.domain.store.BaseStorePath;

/**
 * A DataStoreContainer that simply manages files within a single directory; it doesn't manage sub-directories or parent
 * directories. Each parent directory and sub-directory is managed by a separate instance of FileDataStoreContainer.
 *
 * @author George Norman
 */
public class FileDataStoreContainer implements DataStoreContainer {
  /** Absolute path to the directory managed by this FileDataStoreContainer instance. */
  private File containerStore;

  /** Saved ContainerPath instance. */
  private ContainerPath savedContainerPath;

  // ------------------------------------------------
  // FileDataStoreEntity
  // ------------------------------------------------

  /**
   * Represents a file in the file system. It uses FileInputStream to get the data to be read or saved.
   */
  public static class FileDataStoreEntity implements DataStoreEntity {
    private File directoryStore;
    private EntityPath entityPath;

    public FileDataStoreEntity(final File directoryStore, final EntityPath entityPath) {
      this.directoryStore = directoryStore;
      this.entityPath = (EntityPath)entityPath.clone();
    }

    @Override
    public InputStream getData() {
      InputStream result;

      try {
        result = new FileInputStream(getFile());
      } catch (FileNotFoundException e) {
        result = null;
      }
      return result;
    }

    @Override
    public EntityPath getEntityPath() {
      return entityPath;
    }

    public File getFile() {
      File directory = new File(directoryStore, entityPath.getContainerPathAsString());
      File file = new File(directory, entityPath.getEntityName());

      return file;
    }
  }

  // ============================================================
  // FileDataStoreContainer
  // ============================================================

  /**
   * The given baseStorePath and directoryPath are combined to produce an absolute file path to the directory, which
   * is validated to ensure it exists and is a directory. If it's nonexistent and createDirsIfNonExistent is true, then
   * the directory will be created, including all nonexistent parent directories.
   */
  public FileDataStoreContainer(BaseStorePath baseStorePath, ContainerPath containerPath, boolean createParentContainersIfNonExistent) {
    this.containerStore = new File(baseStorePath.toString(), containerPath.getPath());
    this.savedContainerPath = containerPath;

    boolean exists = containerStore.exists();

    // create directory if does not exist
    if (createParentContainersIfNonExistent && !exists) {
      exists = containerStore.mkdirs();
    }

    // assert that the containerStore is a directory
    if (exists && !containerStore.isDirectory()) {
      throw new DAOException("Error - The directory store is not a directory: " + containerStore.getAbsolutePath());
    }
  }

  /**
   * Return a list of all the {@code DataStoreEntity} instances within this container and if {@code recursive} is true,
   * then return all of the {@code DataStoreEntity} instances within all of the sub containers as well.
   */
  @Override
  public List<? extends DataStoreEntity> getAllEntities(boolean recursive) {
    return doGetAllEntities(savedContainerPath, recursive);
  }

  protected List<? extends DataStoreEntity> doGetAllEntities(ContainerPath containerPath, boolean recursive) {
    List<DataStoreEntity> result = new ArrayList<DataStoreEntity>();
    File[] files = containerStore.listFiles();

    for (File file : files) {
      if (file.isFile()) {
        EntityPath id = new EntityPath(containerPath.getPath(), file.getName());

        result.add(createDataStoreEntityFrom(id));
      } else if (recursive) {
        ContainerPath childDirectoryPath = new ContainerPath(containerPath, file.getName() + ContainerPath.CONTAINER_PATH_SEPARATOR);

        result.addAll(doGetAllEntities(childDirectoryPath, recursive));
      }
    }

    return result;
  }

  protected DataStoreEntity createDataStoreEntityFrom(final EntityPath id) {
    return new FileDataStoreEntity(containerStore, id);
  }

  /**
   * Returns a DataStoreEntity that represents the data from the file specified by the given fileName (can have any file
   * extension).
   */
  @Override
  public DataStoreEntity readEntity(final String fileName) {
    return new FileDataStoreEntity(containerStore, new EntityPath(new ContainerPath(), fileName));
  }

  /**
   * Creates a new data file, if nonexistent and then writes the given data to it.
   */
  @Override
  public void saveOrUpdateEntity(String fileName, DataStoreEntity fileData) {
    if (!isExistingEntity(fileName)) {
      // create the file
      createNewEntity(fileName);
    }

    // then, write data to it
    updateEntity(fileName, fileData);
  }

  /**
   * Update an existing data file with the given fileData.
   *
   * @throws DAOException if nonexistent.
   */
  @Override
  public void updateEntity(String fileName, DataStoreEntity fileData) {
    File fileToWrite = getFileFor(fileName);

    if (fileToWrite.exists()) {
      byte[] dataAsBytes;
      try {
        dataAsBytes = IOUtils.toByteArray(fileData.getData());
      } catch (IOException e1) {
        throw new DAOException("ERROR: Can't read fileData (to byte array).");
      }

      try {
        FileUtils.writeByteArrayToFile(fileToWrite, dataAsBytes);
      } catch (IOException e) {
        throw new DAOException("Error writing to file: " + fileToWrite.getAbsolutePath(), e);
      }
    } else {
      throw new DAOException("Error - Can't write to file that doesn't exist: " + fileToWrite.getAbsolutePath());
    }
  }

  /**
   * Create a new data file.
   *
   * @throws DAOException if file already exists or could not be created.
   */
  @Override
  public void createNewEntity(String fileName) {
    File fileToCreate = getFileFor(fileName);

    if (fileToCreate.exists()) {
      throw new DAOException("Error - Can't create a file that already exists: " + fileToCreate.getAbsolutePath());
    } else {
      boolean fileCreated = false;

      try {
        fileCreated = fileToCreate.createNewFile();
      } catch (IOException e) {
        // ignore
      }

      if (!fileCreated) {
        throw new DAOException("Error creating file: " + fileToCreate.getAbsolutePath());
      }
    }
  }

  /**
   * Deletes the file specified by the given fileName.
   *
   * @throws DAOException if file is a directory or could not be deleted.
   */
  @Override
  public void deleteEntity(String fileName) {
    File fileToDelete = getFileFor(fileName);

    // assert that the file is not a directory
    if (fileToDelete.isDirectory()) {
      throw new DAOException("Error - The file name refers to a directory: " + fileToDelete.getAbsolutePath());
    }

    // delete it if it exists
    if (fileToDelete.exists()) {
      if (!fileToDelete.delete()) {
        throw new DAOException("Error - Couldn't delete the file named: " + fileToDelete.getAbsolutePath());
      }
    }
  }

  /**
   * Returns true if the specified file exists in the file system.
   */
  @Override
  public boolean isExistingEntity(String fileName) {
    File fileToCreate = getFileFor(fileName);

    return fileToCreate.exists();
  }

  protected File getFileFor(String fileName) {
    File result = new File(containerStore, fileName);

    return result;
  }

  @Override
  public String getDebugPathInfo(String fileName) {
    return getFileFor(fileName).getAbsolutePath();
  }

  @Override
  public void validate() {
    File parentDirectory = containerStore.getParentFile();
    if (!parentDirectory.exists() && !parentDirectory.getParentFile().exists()) {
      throw new DataStoreException("ERROR: The parent directory for the data store does not exist: '" + parentDirectory.getAbsolutePath() + "'.");
    }
  }

}
