package com.sap.core.odata.core.uri.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.uri.UriParserFacade;
import com.sap.core.testutils.mocks.MockFacade;

public class UriParserFacadeTest {

  @Test
  public void parseWithFacade() throws Exception {
    UriParserFacade facade = new UriParserFacade();
    Map<String, String> queryParameter = new HashMap<String, String>();
    facade.parse(MockFacade.getMockEdm(), MockFacade.getPathSegmentsAsStringMock(), queryParameter);
  }

}
