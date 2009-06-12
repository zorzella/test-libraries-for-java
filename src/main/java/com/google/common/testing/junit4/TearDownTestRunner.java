// Copyright 2008 Google Inc.  All Rights Reserved.
package com.google.common.testing.junit4;

import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class TearDownTestRunner extends BlockJUnit4ClassRunner {

  private final Class<?> klass;

  public TearDownTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
    this.klass = klass;
  }
  
//  @Override
//  public Description getDescription() {
//    return delegate.getDescription();
//  }
//
//  @Override
//  public void run(RunNotifier notifier) {
////    if (klass instanceof TearDownAccepter) {
////      
////    }
//    delegate.run(notifier);
//    
//  }
  
  @Override
  protected Statement withAfters(FrameworkMethod method, Object target, Statement statement) {
    return new MoreAfters(super.withAfters(method, target, statement));
  }
  
  private static final class MoreAfters extends Statement {

    private final Statement orig;

    public MoreAfters(Statement orig) {
      this.orig = orig;
    }

    @Override
    public void evaluate() throws Throwable {
      orig.evaluate();
    }
  }
}
