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
package com.thruzero.domain.jpa.utils;

import java.util.List;

import javax.persistence.Query;

import com.thruzero.domain.dao.GenericDAO.DAOException;

/**
 *
 * @author George Norman
 */
public class JpaUtils {

  /**
   * Return the single result generated by the given hqlQuery. This is needed because hqlQuery.getSingleResult() generates a MySQLSyntaxErrorException
   * (when using component mapping/embedded objects - like ContainerPath). Below is an example:
   * <pre>
   *   MySQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near
   *   '2 textentity0_.TEXT_ENVELOPE_ID as TEXT1_0_, textentity0_.PATH as PATH0_, tex' at line 1
   * </pre>
   */
  public static <T> T getSingleResultHack(Query hqlQuery) {
    @SuppressWarnings("unchecked") // Hibernate isn't generic
    List<? extends T> hack = hqlQuery.getResultList();

    T result = null;
    if (!hack.isEmpty()) {
      if (hack.size() > 1) {
        throw new DAOException("ERROR: getSingleResultHack found more than one result.");
      }
      result = hack.get(0);
    }

    return result;
  }
}
