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

package com.google.common.testing.junit3;

import static com.google.common.testing.junit3.JUnitAsserts.assertContentsInOrder;

import com.google.common.testing.TearDown;
import com.google.common.testing.TearDownStack;
import com.google.common.testing.TestLogHandler;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * Unit test for {@link TearDownTestCase}.
 *
 * @author kevinb
 */
public class TearDownTestCaseTest extends TestCase {

  private TearDownTestCase test;
  private List<String> messages;
  private TestLogHandler handler;

  @Override protected void setUp() throws Exception {
    test = new TearDownTestCase() {};
    test.setUp();
    messages = new ArrayList<String>();
    handler = new TestLogHandler();
    TearDownStack.logger.addHandler(handler);
    TearDownStack.logger.setUseParentHandlers(false);
  }

  public void testAdHocTearDownObject() throws Exception {
    final SomeObject obj = new SomeObject("a");
    test.addTearDown(new TearDown() {
      public void tearDown() {
        messages.add(obj.desc);
      }
    });

    test.stack.runTearDown();
    assertNothingWasLogged();

    assertContentsInOrder(messages, "a");
  }

  public void testReusableTearDownObject() throws Exception {
    SomeObject obj = new SomeObject("b");
    test.addTearDown(new SomeObjectTearDown(obj));

    test.stack.runTearDown();
    assertNothingWasLogged();

    assertContentsInOrder(messages, "b");
  }

  public void testSelfCleaningObject() throws Exception {
    TidyObject obj = new TidyObject("c");
    test.addTearDown(obj);

    test.stack.runTearDown();
    assertNothingWasLogged();

    assertContentsInOrder(messages, "c");
  }

  public void testReverseOrder() throws Exception {
    test.addTearDown(new TidyObject("x"));
    test.addTearDown(new TidyObject("y"));
    test.addTearDown(new TidyObject("z"));

    test.stack.runTearDown();
    assertNothingWasLogged();

    assertContentsInOrder(messages, "z", "y", "x");
  }

  public void testTearDownFailure() throws Exception {
    test.addTearDown(new TidyObject("before"));
    test.addTearDown(new FailingTearDown());
    test.addTearDown(new TidyObject("after"));

    assertNothingWasLogged();
    test.stack.runTearDown();
    assertFailureWasLogged();

    assertContentsInOrder(messages, "after", "whoops", "before");
  }

  public void testDontSkipOptionalTearDowns() throws Exception {
    test.addTearDown(new TidyObject("sometimes"));
    test.addTearDown(new TidyObject("always"));
    test.stack.runTearDown();
    assertNothingWasLogged();
    assertContentsInOrder(messages, "always", "sometimes");
  }

  public void testWithNoTestEnvironments() throws Throwable {
    test.tearDown();
    assertNothingWasLogged();
  }

  private void assertNothingWasLogged() {
    assertTrue(handler.getStoredLogRecords().isEmpty());
  }

  private void assertFailureWasLogged() {
    LogRecord record = handler.getStoredLogRecords().get(0);
    assertEquals("exception thrown during tearDown: "
        + "Don't worry, this exception is expected.", record.getMessage());
    assertEquals(Level.INFO, record.getLevel());
    assertNotNull(record.getThrown());
  }

  /** This is deeply ironic. */
  @Override protected void tearDown() throws Exception {
    TearDownStack.logger.removeHandler(handler);
    TearDownStack.logger.setUseParentHandlers(true);
    super.tearDown();
  }

  private static class SomeObject {
    String desc;
    SomeObject(String desc) {
      this.desc = desc;
    }
  }

  private class TidyObject implements TearDown {
    String desc;
    TidyObject(String desc) {
      this.desc = desc;
    }
    public void tearDown() {
      messages.add(desc);
    }
  }

  private class SomeObjectTearDown implements TearDown {
    SomeObject toclean;
    SomeObjectTearDown(SomeObject toclean) {
      this.toclean = toclean;
    }
    public void tearDown() {
      messages.add(toclean.desc);
    }
  }

  private class FailingTearDown implements TearDown {
    public void tearDown() {
      messages.add("whoops");
      doSomethingThatFails();

      // never try to do two things in the same TearDown!
      messages.add("this will never appear");
    }
    void doSomethingThatFails() {
      throw new RuntimeException("Don't worry, this exception is expected.");
    }
  }
}
