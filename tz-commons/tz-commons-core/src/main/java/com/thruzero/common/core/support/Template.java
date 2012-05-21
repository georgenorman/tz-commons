/*
 *   Copyright 2009-2012 George Norman
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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.thruzero.common.core.strategy.StrMatcherSubstitutionStrategy;
import com.thruzero.common.core.strategy.SubstitutionStrategy;

/**
 * A text-based template that uses a substitution strategy to replace variables defined within the text. The default
 * strategy is KeyValuePairSubstitutionStrategy using "${" as the variable name prefix and "}" as the variable name
 * suffix.
 * <p>
 * Below is an example template:
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
public class Template {
  private String templateText;

  /**
   * Subclasses use this constructor if they override loadRawTemplateText.
   */
  protected Template() {
  }

  public Template(final String templateText) {
    this.templateText = templateText;
  }

  public String evaluateTemplate(final SubstitutionStrategy substitutionStrategy) {
    String result = getRawTemplateText();

    result = substitutionStrategy.replaceAll(result);

    return result;
  }

  public <V> String evaluateTemplate(final Map<String, V> valueMap) {
    return evaluateTemplate(valueMap, "${", "}", '$');
  }

  public <V> String evaluateTemplate(final Map<String, V> valueMap, final String prefix, final String suffix, final char escape) {
    return evaluateTemplate(new StrMatcherSubstitutionStrategy(valueMap, prefix, suffix, escape));
  }

  /**
   * Return the raw, un-evaluated text of the template.
   */
  public String getRawTemplateText() {
    if (StringUtils.isEmpty(templateText)) {
      templateText = loadRawTemplateText();
    }

    return templateText;
  }

  /**
   * Optional hook, used to lazy-load the template text from a resource (e.g., file, database, etc).
   */
  protected String loadRawTemplateText() {
    return "";
  }

}
