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
package com.thruzero.test.support;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.log4j.Logger;


/**
 * An abstract base class that helps verify linear growth of a function as the dataset doubles for each iteration.
 *
 * @author George Norman
 */
public abstract class AbstractLinearGrowthPerformanceTestHelper {
  private static final Logger logger = Logger.getLogger(AbstractLinearGrowthPerformanceTestHelper.class);

  /** Doubling the size should approximately double the time (e.g., O(n)). */
  public void execute(final int initialSize, final int numTrials, final float slack) throws Exception {
    StopWatch timer = new StopWatch();
    long previousTime = 0;
    int currentSize = initialSize;

    for (int i=0; i<numTrials; i++) {
      // since the data doesn't change, the time differences should be a result of garbage collection, system tasks, etc, so try 10 samples and keep the lowest time.
      long bestTimeAtThisSize = Long.MAX_VALUE;
      for (int j=0; j<10; j++) {
        doSetup(currentSize);

        // perform test with timer
        timer.start();
        doExecute();
        timer.stop();
        doDataValidation(currentSize);

        // keep the best time for each sample
        bestTimeAtThisSize = bestTimeAtThisSize > timer.getTime() ? timer.getTime() : bestTimeAtThisSize;
        timer.reset();
      }

      // if time is more than double (plus a little slack), then fail the test (subclass defines what fail means - default just prints a warning).
      String warning = "";
      if (previousTime > 0) {
        if (bestTimeAtThisSize > 2.0f * previousTime + (slack * previousTime)) {
          warning = handleTimeExceededFail(currentSize);
        } else if (bestTimeAtThisSize < previousTime) {
          warning = handleTimeReversalFail(currentSize);
        }
      }
      logger.debug(String.format("##### Time[%5d]: " + bestTimeAtThisSize + "ms" + warning, currentSize));

      currentSize *= 2;
      previousTime = bestTimeAtThisSize;
    }
  }

  protected void doSetup(int currentSize) {
  }

  /** Execute a function using the current dataset. */
  protected abstract void doExecute();

  /** Optional validation to ensure doExecute created reasonable data (this is done after timing values for doExecute have been captured). */
  protected void doDataValidation(int currentSize) {
  }

  /** Validate the time it took to execute the function against the current dataset. */
  protected String doTimerValidation(final long bestTimeAtThisSize, final long previousTime, final float slack, final int currentSize) throws Exception {
    String result;

    if (bestTimeAtThisSize == 0) {
      result = handleZeroTimeFail(currentSize);
    } else if (bestTimeAtThisSize > 2.0f * previousTime + (slack * previousTime)) {
      result = handleTimeExceededFail(currentSize);
    } else if (bestTimeAtThisSize < previousTime) {
      result = handleTimeReversalFail(currentSize);
    } else {
      result = "";
    }

    return result;
  }

  /** Optional cleanup after test. */
  protected void doTeardown() {
  }

  /** Time of current sample is zero - may want to choose a larger dataset. */
  protected String handleZeroTimeFail(final int currentSize) throws Exception {
    return " *** WARNING: Time at this size is ZERO - you may want to choose a larger dataset. Current size="+currentSize;
  }

  /** Time of the next sample is greater than twice the previous sample (plus the alloted slack). This sample took longer than expected to complete. */
  protected String handleTimeExceededFail(final int currentSize) {
    return " *** WARNING: Time at this size is > double previous time (plus alloted slack). Current size="+currentSize;
  }

  /** The current sample took longer than the previous sample, even though the current sample is double in size! */
  protected String handleTimeReversalFail(final int currentSize) {
    return " *** WARNING: Time at this size is < previous time, even though this sample is double the size!. Current size="+currentSize;
  }

}
