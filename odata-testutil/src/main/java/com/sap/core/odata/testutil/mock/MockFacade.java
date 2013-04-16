/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
