package com.sap.core.odata.testutils.mocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.processor.ODataPathSegment;

public class MockFacade {

  public static Edm getMockEdm() {
    try {
      return EdmMock.createMockEdm();
    } catch (EdmException e) {
      throw new RuntimeException(e);
    }
  }

  public static List<String> getPathSegmentsAsStringMock() {
    ArrayList<String> pathSegmentsMock = new ArrayList<String>();
    pathSegmentsMock.add("$metadata");
    return pathSegmentsMock;
  }

  public static List<ODataPathSegment> getPathSegmentsAsODataPathSegmentMock() {
    ArrayList<ODataPathSegment> pathSegmentsMock = new ArrayList<ODataPathSegment>();
    pathSegmentsMock.add(new ODataPathSegment() {
      
      @Override
      public String getPath() {
        return "$metadata";
      }
      
      @Override
      public Map<String, List<String>> getMatrixParameters() {
        return null;
      }
    });
    return pathSegmentsMock;
  }
}
