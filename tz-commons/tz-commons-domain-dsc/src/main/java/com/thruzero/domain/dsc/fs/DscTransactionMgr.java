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
package com.thruzero.domain.dsc.fs;

import com.thruzero.common.core.transaction.TransactionMgr;

/**
 *
 * @author George Norman
 */
public class DscTransactionMgr implements TransactionMgr {
  private boolean active;
  private boolean commitRequested;

  @Override
  public boolean isTransactionActive() {
    return active;
  }

  @Override
  public Object beginTransaction() {
    active = true;

    return null;
  }

  @Override
  public void commitTransaction() {
    active = false;
  }

  @Override
  public void rollbackTransaction() {
    active = false;
  }

  @Override
  public void setCommitRequested(boolean commitRequested) {
    this.commitRequested = commitRequested;
  }

  @Override
  public void commitOrRollbackTransaction() {
    if (commitRequested) {
      commitTransaction();
    } else {
      rollbackTransaction();
    }
  }

}
