/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.rt;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.testutil.fit.BaseTest;

public class RuntimeDelegateTest extends BaseTest {

  @Test
  public void testInstance() {
    assertNotNull(RuntimeDelegate.createODataResponseBuilder());
  }

}
