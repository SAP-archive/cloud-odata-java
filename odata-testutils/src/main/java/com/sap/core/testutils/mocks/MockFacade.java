package com.sap.core.testutils.mocks;

import com.sap.core.odata.api.edm.Edm;

public class MockFacade {

  public static Edm getMockEdm(){
    EdmMock edm = new EdmMock();
    return edm.getEdmMock();
  }
  
}
