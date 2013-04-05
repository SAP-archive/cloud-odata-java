package com.sap.core.odata.testutil.mock;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

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
    final List<PathSegment> pathSegmentsMock = new ArrayList<PathSegment>();
    for (final String segment : segments) {
      PathSegment pathSegment = Mockito.mock(PathSegment.class);
      Mockito.when(pathSegment.getPath()).thenReturn(segment);
      pathSegmentsMock.add(pathSegment);
    }
    return pathSegmentsMock;
  }
}
