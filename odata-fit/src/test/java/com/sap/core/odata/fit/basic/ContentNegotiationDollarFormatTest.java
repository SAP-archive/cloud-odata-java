package com.sap.core.odata.fit.basic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

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

  ODataSingleProcessor processor = mock(ODataSingleProcessor.class);

  @Override
  ODataSingleProcessor createProcessor() throws ODataException {
    // service document 
    String contentType = "application/atom+xml; charset=utf-8";
    ODataResponse responseAtomXml = ODataResponse.status(HttpStatusCodes.OK).contentHeader(contentType).entity("Test passed.").build();
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), eq(contentType))).thenReturn(responseAtomXml);

    // csv
    ODataResponse value = ODataResponse.status(HttpStatusCodes.OK).contentHeader("csv").build();
    when(((ServiceDocumentProcessor) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), eq("csv"))).thenReturn(value);
    when(((CustomContentType) processor).getCustomContentTypes(ServiceDocumentProcessor.class)).thenReturn(Arrays.asList("csv"));

    return processor;
  }

  @Test
  public void testAtomFormatForServiceDocument() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "?$format=atom"));

    HttpResponse response = this.getHttpClient().execute(get);
    
    String responseEntity = StringHelper.httpEntityToString(response.getEntity());
    assertEquals("Test passed.", responseEntity);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
    assertEquals("application/atom+xml; charset=utf-8", header.getValue());
  }

  @Test
  public void testFormatCustom() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "?$format=csv"));

    HttpResponse response = this.getHttpClient().execute(get);

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);
    assertEquals("csv", header.getValue());
  }

  @Test
  public void testFormatNotAccepted() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "?$format=csvOrSomethingElse"));

    HttpResponse response = this.getHttpClient().execute(get);

    assertEquals(HttpStatusCodes.NOT_ACCEPTABLE.getStatusCode(), response.getStatusLine().getStatusCode());
  }
}
