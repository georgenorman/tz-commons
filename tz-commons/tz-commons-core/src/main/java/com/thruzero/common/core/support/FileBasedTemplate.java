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
package com.thruzero.common.core.support;

import java.io.File;

import com.thruzero.common.core.bookmarks.ConfigKeysBookmark;
import com.thruzero.common.core.config.Config.ConfigKeys;
import com.thruzero.common.core.locator.ConfigLocator;
import com.thruzero.common.core.utils.FileUtilsExt;
import com.thruzero.common.core.utils.FileUtilsExt.FileUtilsException;

/**
 * A specialization of the Template class that reads the template text from the file system. Below is an example
 * template named "confirmationEventUpdateEmailTemplate.txt":
 * 
 * <p>
 * Hello ${recipientName}.
 * <p>
 * There are changes to the "<b>${eventName}</b>" Event you have been selected to help out with on
 * <b>${eventStartDate}</b>.
 * <p>
 * <b>Event Description:</b> <div style="margin-left:20px;"> ${eventDescription} </div>
 * <p>
 * <b>Current Event Update:</b> <div style="margin-left:20px;"> ${updateMessage} </div>
 * <p>
 * Thanks for volunteering.
 * <p>
 * ${eventCoordinatorSignature}
 * 
 * @author George Norman
 */
public class FileBasedTemplate extends Template {
  private final File templatesDir;
  private final String templateName;
  private final String templateFileExtension;

  // ------------------------------------------------
  // FileBasedTemplateConfigKeys
  // ------------------------------------------------

  /**
   * Config keys defined for FileBasedTemplate and are defined inside of the config file section named by
   * CONFIG_SECTION: "com.thruzero.common.core.support.FileBasedTemplate".
   */
  @ConfigKeysBookmark
  public interface FileBasedTemplateConfigKeys extends ConfigKeys {
    /** The config section to use */
    String CONFIG_SECTION = FileBasedTemplate.class.getName();

    /** The config key that defines where the templates are stored: "templatesPath". */
    String TEMPLATE_PATH = "templatesPath";
  }

  // ============================================================
  // FileBasedTemplate
  // ============================================================

  /**
   * Construct a template that uses the given substitutionStrategy to perform substitutions on the named template.
   * 
   * @param templateName name of the template (the template file name is templateName + templateFileExtension).
   * @param templateFileExtension extension of the template file.
   */
  public FileBasedTemplate(final String templateName, final String templateFileExtension) {
    this(new File(ConfigLocator.locate().getValue(FileBasedTemplateConfigKeys.CONFIG_SECTION, FileBasedTemplateConfigKeys.TEMPLATE_PATH)), templateName, templateFileExtension);
  }

  /**
   * Construct a template that uses the given substitutionStrategy to perform substitutions on the named template.
   * 
   * @param templateName name of the template (the template file name is templateName + templateFileExtension).
   * @param templateFileExtension extension of the template file.
   */
  public FileBasedTemplate(final File templatesDir, final String templateName, final String templateFileExtension) {
    this.templatesDir = templatesDir;
    this.templateName = templateName;
    this.templateFileExtension = templateFileExtension;
  }

  /**
   * Return the name of the template (Note: the template file is templateName + templateFileExtension).
   */
  public String getTemplateName() {
    return templateName;
  }

  /**
   * Read the contents of the template file and return the results (un-evaluated).
   */
  @Override
  protected String loadRawTemplateText() {
    String result = null;
    File templateFile = new File(templatesDir, templateName + templateFileExtension);

    try {
      result = FileUtilsExt.readFromFile(templateFile);
    } catch (FileUtilsException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("ERROR: There was a problem reading the template named '" + templateFile.getAbsolutePath() + "' (" + e + ").");
    }

    return result;
  }

}
