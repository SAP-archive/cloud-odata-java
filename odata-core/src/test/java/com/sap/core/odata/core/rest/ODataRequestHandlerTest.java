package com.sap.core.odata.core.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.ODataServiceFactory;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpMethod;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataProcessor;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.part.ServiceDocumentProcessor;
import com.sap.core.odata.api.uri.PathInfo;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;
import com.sap.core.odata.core.ODataRequestHandler;
import com.sap.core.odata.core.ODataResponseImpl;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.testutil.fit.BaseTest;

/**
 * @author SAP AG
 */
public class ODataRequestHandlerTest extends BaseTest {
  @Mock
  ODataServiceFactory mockFactory;
  @Mock
  ODataService mockService;
  @Mock
  ODataProcessor mockProcessor;

  @SuppressWarnings("unchecked")
  @Before
  public void initialize() throws Exception {
    MockitoAnnotations.initMocks(this);
    when(mockFactory.createService(Mockito.any(ODataContext.class))).thenReturn(mockService);

    when(mockService.getProcessor()).thenReturn(mockProcessor);
    List<String> supportedContentTypes = Arrays.asList(ContentType.APPLICATION_ATOM_XML_CS_UTF_8.toContentTypeString());
    when(mockService.getSupportedContentTypes(Mockito.any(Class.class))).thenReturn(supportedContentTypes);
  }

  @Test
  public void handleSimpleRequest() throws Exception {
    ServiceDocumentProcessor serviceDocumentProcessor = mock(ServiceDocumentProcessor.class);
    GetServiceDocumentUriInfo uriInfo = Mockito.any(GetServiceDocumentUriInfo.class);
    String contentType = Mockito.anyString();// "application/atom+xml";
    ODataResponse expectedResponse = ODataResponseImpl.newBuilder().status(HttpStatusCodes.OK).build();
    when(serviceDocumentProcessor.readServiceDocument(uriInfo, contentType)).thenReturn(expectedResponse);
    when(mockService.getServiceDocumentProcessor()).thenReturn(serviceDocumentProcessor);
    ODataRequestHandler handler = new ODataRequestHandler(mockFactory);

    ODataRequest request = mockDefaultRequest();
    ODataResponse response = handler.handle(request);

    assertNotNull(response);
    assertEquals(expectedResponse.getStatus(), response.getStatus());
  }

  private ODataRequest mockDefaultRequest() {
    ODataRequest request = mock(ODataRequest.class);
    when(request.getAcceptHeaders()).thenReturn(Arrays.asList("application/atom+xml"));
    when(request.getAcceptableLanguages()).thenReturn(Arrays.asList(Locale.ENGLISH));
    when(request.getMethod()).thenReturn(ODataHttpMethod.GET);

    PathInfo pathInfo = mock(PathInfo.class);
    when(request.getPathInfo()).thenReturn(pathInfo);

    return request;
  }
}