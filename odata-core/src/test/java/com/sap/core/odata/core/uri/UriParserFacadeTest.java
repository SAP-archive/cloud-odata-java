package com.sap.core.odata.core.uri;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.uri.UriParser;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

public class UriParserFacadeTest extends BaseTest {

  @Test
  public void parseWithFacade() throws Exception {
    Map<String, String> queryParameter = new HashMap<String, String>();
    UriParser.parse(MockFacade.getMockEdm(), MockFacade.getPathSegmentsAsODataPathSegmentMock(), queryParameter);
  }

}
