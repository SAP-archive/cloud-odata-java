package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.feature.CustomContentType;
import com.sap.core.odata.api.processor.part.ServiceDocumentProcessor;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;
import com.sap.core.odata.testutil.helper.StringHelper;

/**
 * @author SAP AG
 */
public class ContentNegotiationDollarFormatTest extends AbstractBasicTest {

  /**
   * 
   */
  private static final String CUSTOM_CONTENT_TYPE = "application/csv";
  ODataSingleProcessor processor = mock(ODataSingleProcessor.class);

  @Override
  ODataSingleProcessor createProcessor() throws ODataException {
    // service document 
    final String contentType = HttpContentType.APPLICATION_ATOM_SVC_UTF8;
    final ODataResponse responseAtomXml = ODataResponse.status(HttpStatusCodes.OK).contentHeader(contentType).entity("Test passed.").build();
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), eq(contentType))).thenReturn(responseAtomXml);

    // csv
    final ODataResponse value = ODataResponse.status(HttpStatusCodes.OK).contentHeader(CUSTOM_CONTENT_TYPE).entity("any content").build();
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), eq("csv"))).thenReturn(value);
    when(((CustomContentType) processor).getCustomContentTypes(ServiceDocumentProcessor.class)).thenReturn(Arrays.asList("csv"));

    return processor;
  }

  @Test
  public void atomFormatForServiceDocument() throws Exception {
    final HttpResponse response = executeGetRequest("?$format=atom");

    final String responseEntity = StringHelper.httpEntityToString(response.getEntity());
    assertEquals("Test passed.", responseEntity);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    final Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
    assertEquals(HttpContentType.APPLICATION_ATOM_SVC_UTF8, header.getValue());
  }

  @Test
  public void formatCustom() throws Exception {
    final HttpResponse response = executeGetRequest("?$format=csv");

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    final Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
    assertNotNull(header);
    assertEquals(CUSTOM_CONTENT_TYPE, header.getValue());
  }

  @Test
  public void formatNotAccepted() throws Exception {
    final HttpResponse response = executeGetRequest("?$format=csvOrSomethingElse");

    assertEquals(HttpStatusCodes.NOT_ACCEPTABLE.getStatusCode(), response.getStatusLine().getStatusCode());
  }
}
