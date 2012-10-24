package com.sap.core.odata.core.dispatcher.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.enums.UriType;
import com.sap.core.odata.api.exception.ODataError;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;
import com.sap.core.odata.api.rest.ODataResponse;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.core.dispatcher.Dispatcher;
import com.sap.core.odata.core.enums.ODataHttpMethod;

public class DispatcherTest {

  private static ODataProcessor processor;

  @BeforeClass
  public static void createMockProcessor() throws ODataError {
    ODataResponse readServiceDocumentResponse = mockResponse("readServiceDocument");
    ServiceDocument serviceDocument = mock(ServiceDocument.class);
    when(serviceDocument.readServiceDocument()).thenReturn(readServiceDocumentResponse);
    processor = mock(ODataProcessor.class);
    when(processor.getServiceDocumentProcessor()).thenReturn(serviceDocument);
  }

  private static ODataResponse mockResponse(final String value) {
    ODataResponse response = mock(ODataResponse.class);
    when(response.getEntity()).thenReturn(value);
    return response;
  }

  public void checkDispatch(final ODataHttpMethod method, final UriType uriType, final String expectedMethodName) throws ODataError {
    Dispatcher dispatcher = new Dispatcher();
    dispatcher.setProcessor(processor);

    UriParserResult uriParserResult = mock(UriParserResult.class);
    when(uriParserResult.getUriType()).thenReturn(uriType);
    final ODataResponse response = dispatcher.dispatch(method, uriParserResult);
    assertEquals(expectedMethodName, response.getEntity());
  }

  private void wrongDispatch(final ODataHttpMethod method, final UriType uriType) {
    try {
      checkDispatch(method, uriType, null);
      fail("Expected ODataError not thrown");
    } catch (ODataError e) {
      assertNotNull(e);
    }
  }

  @Test
  public void dispatch() throws Exception {
    checkDispatch(ODataHttpMethod.GET, UriType.URI0, "readServiceDocument");
  }

  @Test
  public void dispatchNotAllowedCombinations() throws Exception {
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI0);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI8);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI50A);
    wrongDispatch(ODataHttpMethod.DELETE, UriType.URI50B);
  }
}
