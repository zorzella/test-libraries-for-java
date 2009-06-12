// Copyright 2008 Google Inc.  All Rights Reserved.
package com.google.common.testing.junit4;

import junit.framework.Assert;

import org.junit.Test;


public class Foo {

  public Foo() {
    System.out.println("foo");
  }
  
  @Test
  public void testBar() throws Exception {
    Assert.assertTrue(true);
    
  }
  
}
