package com.sap.core.odata.core.uri.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.uri.UriParserFacade;
import com.sap.core.odata.testutils.mocks.MockFacade;

public class UriParserFacadeTest {

  @Test
  public void parseWithFacade() throws Exception {
    Map<String, String> queryParameter = new HashMap<String, String>();
    UriParserFacade.parse(MockFacade.getMockEdm(), MockFacade.getPathSegmentsAsStringMock(), queryParameter);
  }

}
