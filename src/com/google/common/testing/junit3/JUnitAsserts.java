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

import junit.framework.Assert;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Contains additional assertion methods not found in JUnit.
 *
 * @author kevinb
 */
public final class JUnitAsserts {

  private JUnitAsserts() { }

  /**
   * Asserts that {@code actual} contains precisely the elements
   * {@code expected}, and in the same order.
   */
  public static void assertContentsInOrder(
      String message, Iterable<?> actual, Object... expected) {
    List<Object> expectedAsList = Arrays.asList(expected);
    Iterator<?> actualAsIterator = actual.iterator();
    for(Object expectedElement: expectedAsList) {
      Assert.assertTrue(message,
          actualAsIterator.hasNext());
      Assert.assertEquals(message,
          expectedElement, actualAsIterator.next());
    }
    Assert.assertFalse(message,
        actualAsIterator.hasNext());
  }

  /**
   * Variant of {@link #assertContentsInOrder(String,Iterable,Object...)}
   * using a generic message.
   */
  public static void assertContentsInOrder(
      Iterable<?> actual, Object... expected) {
    assertContentsInOrder((String) null, actual, expected);
  }
}
