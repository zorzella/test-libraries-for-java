/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.testing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Tests may use this to intercept messages that are logged by the code under
 * test.  Example:
 * <pre>
 *   TestLogHandler handler;
 *
 *   protected void setUp() throws Exception {
 *     super.setUp();
 *     handler = new TestLogHandler();
 *     SomeClass.logger.addHandler(handler);
 *     addRequiredTearDown(new TearDown() {
 *       public void tearDown() throws Exception {
 *         SomeClass.logger.removeHandler(handler);
 *       }
 *     });
 *   }
 *
 *   public void test() {
 *     SomeClass.foo();
 *     LogRecord firstRecord = handler.getStoredLogRecords().get(0);
 *     assertEquals("some message", firstRecord.getMessage());
 *   }
 * </pre>
 *
 * You can see more usage examples in {@link TestLogHandlerTest}.
 *
 * Note a superset of this functionality is available in
 * {@link AssertingHandler}
 *
 * @author kevinb
 */
public class TestLogHandler extends Handler {

  /** We will keep a private list of all logged records */
  private final List<LogRecord> list = new ArrayList<LogRecord>();

  /** And also the same list in an unmodifiable view (for the getter) */
  private final List<LogRecord> storedLogRecords
      = Collections.unmodifiableList(list);

  /**
   * Adds the most recently logged record to our list.
   */
  @Override
  public void publish(LogRecord record) {
    list.add(record);
  }

  @Override
  public void flush() { }

  @Override
  public void close() { }

  public void clear() {
    list.clear();
  }

  /**
   * Fetch the list of logged records
   * @return unmodifiablie LogRecord list of all logged records
   */
  public List<LogRecord> getStoredLogRecords() {
    return storedLogRecords;
  }
}
