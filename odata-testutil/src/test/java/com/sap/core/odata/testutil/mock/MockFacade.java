package com.sap.core.odata.testutil.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.PathSegment;

/**
 * @author SAP AG
 */
public class MockFacade {

  public static Edm getMockEdm() throws ODataException {
    return EdmMock.createMockEdm();
  }

  public static List<String> getPathSegmentsAsStringMock() {
    final ArrayList<String> pathSegmentsMock = new ArrayList<String>();
    pathSegmentsMock.add("$metadata");
    return pathSegmentsMock;
  }

  public static List<PathSegment> getPathSegmentsAsODataPathSegmentMock() {
    final ArrayList<PathSegment> pathSegmentsMock = new ArrayList<PathSegment>();
    pathSegmentsMock.add(new PathSegment() {

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
