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

import com.google.common.testing.junit3.TearDownTestCase;

/**
 * An object that can perform a {@link #tearDown} operation.
 *
 * @author Kevin Bourrillion
 */
public interface TearDown {

  /**
   * Performs a <b>single</b> tear-down operation.  Used only in the context
   * of a running {@link TearDownTestCase}.
   *
   * @throws Exception for any reason. {@link TearDownTestCase} ensures that
   *     any exception thrown will not interfere with other TearDown
   *     operations.
   */
  void tearDown() throws Exception;
}
