package com.sap.core.odata.testutil.mock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.uri.PathSegment;

/**
 * Mocked Entity Data Model, more or less aligned to the Reference Scenario.
 * @author SAP AG
 */
public class MockFacade {

  public static Edm getMockEdm() throws ODataException {
    return EdmMock.createMockEdm();
  }

  public static List<PathSegment> getPathSegmentsAsODataPathSegmentMock(final List<String> segments) {
    final ArrayList<PathSegment> pathSegmentsMock = new ArrayList<PathSegment>();
    for (final String segment : segments)
      pathSegmentsMock.add(new PathSegment() {
        @Override
        public String getPath() {
          return segment;
        }

        @Override
        public Map<String, List<String>> getMatrixParameters() {
          return null;
        }
      });
    return pathSegmentsMock;
  }
}
