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

/**
 * Any object which can accept registrations of {@link TearDown} instances.
 *
 * @author Kevin Bourrillion
 */
public interface TearDownAccepter {

  /**
   * Registers a TearDown implementor which will be run during
   * {@link TearDownStack#runTearDown()} unless the system property to skip
   * tearDown was set.
   */
  void addTearDown(TearDown tearDown);

  /**
   * Registers a TearDown implementor which will always be run during
   * {@link TearDownStack#runTearDown()} regardless of the SkipTearDown
   * preference. Make sure to use this for operations that should be performed
   * regardless of the user preference, for example, closing file handles.
   */
  void addRequiredTearDown(TearDown tearDown);
}
