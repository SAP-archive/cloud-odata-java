package com.sap.core.testutils.mocks;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;

public class MockFacade {

  public static Edm getMockEdm() throws EdmException{
    return EdmMock.createMockEdm();
  }
  
  public static List<String> getPathSegmentsAsStringMock(){
    ArrayList<String> pathSegmentsMock = new ArrayList<String>();
    pathSegmentsMock.add("$metadata");
    return pathSegmentsMock;
  }
}
