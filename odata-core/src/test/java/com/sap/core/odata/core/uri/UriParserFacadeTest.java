package com.sap.core.odata.core.uri;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class UriParserFacadeTest extends BaseTest {

  @Test
  public void parseWithFacade() throws Exception {
    UriParser.parse(MockFacade.getMockEdm(),
        MockFacade.getPathSegmentsAsODataPathSegmentMock(Arrays.asList("$metadata")),
        Collections.<String, String> emptyMap());
  }

}
