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

import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@code TearDownStack} contains a stack of {@link TearDown} instances.
 *
 * @author Kevin Bourrillion
 */
public class TearDownStack implements TearDownAccepter {

  static final Logger logger
      = Logger.getLogger(TearDownStack.class.getName());

  private static final boolean SKIP_OPTIONAL_TASKS
      = Boolean.getBoolean(TearDownStack.class.getName() + ".SkipTearDown");

  private LinkedList<Task> stack = new LinkedList<Task>(); 

  public final void addTearDown(TearDown tearDown) {
    stack.addFirst(new OptionalTask(tearDown));
  }

  public final void addRequiredTearDown(TearDown tearDown) {
    stack.addFirst(new RequiredTask(tearDown));
  }

  /**
   * Causes tear-down to execute.
   */
  public final void runTearDown() {
    runTearDown(TearDownStack.SKIP_OPTIONAL_TASKS);
  }

  /**
   * Causes tear-down to execute.
   *
   * @param skip if true, optional tasks will be skipped over
   */
  public final void runTearDown(boolean skip) {
    for (Task task : stack) {
      task.possiblyExecute(skip);
    }
    stack.clear();
  }

  private abstract static class Task {
    final TearDown tearDown;
    Task(TearDown tearDown) {
      this.tearDown = tearDown;
    }
    void execute() {
      try {
        tearDown.tearDown();
      } catch (Throwable t) {
        TearDownStack.logger.log(Level.INFO,
            "exception thrown during tearDown: " + t.getMessage(), t);
      }
    }
    abstract void possiblyExecute(boolean skipOptionalTasks);
  }

  private static class RequiredTask extends Task {
    RequiredTask(TearDown tearDown) {
      super(tearDown);
    }
    @Override void possiblyExecute(boolean skipOptionalTasks) {
      execute();
    }
  }

  private static class OptionalTask extends Task {
    OptionalTask(TearDown tearDown) {
      super(tearDown);
    }
    @Override void possiblyExecute(boolean skipOptionalTasks) {
      if (skipOptionalTasks) {
        TearDownStack.logger.log(Level.INFO, "skipping optional TearDown");
      } else {
        execute();
      }
    }
  }
}
