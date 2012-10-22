package com.sap.core.odata.core.rest.test;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.core.odata.api.rest.RuntimeDelegate;

public class RuntimeDelegateTest {

  @Test
  public void testInstance() {
    assertNotNull(RuntimeDelegate.getInstance());
  }
  
  
}
