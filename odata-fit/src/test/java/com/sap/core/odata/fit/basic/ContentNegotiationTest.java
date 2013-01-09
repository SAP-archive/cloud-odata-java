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
import com.sap.core.odata.api.processor.feature.ServiceDocument;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;

public class ContentNegotiationTest extends AbstractBasicTest {

  ODataSingleProcessor processor = mock(ODataSingleProcessor.class);

  @Override
  ODataSingleProcessor createProcessor() throws ODataException {
    ODataResponse value = ODataResponse.status(HttpStatusCodes.OK).contentHeader("cvs").build();
    when(((ServiceDocument) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), eq("cvs"))).thenReturn(value);
    when(((CustomContentType) processor).getCustomContentTypes(ServiceDocument.class)).thenReturn(Arrays.asList("cvs"));

    return processor;
  }

  @Test
  public void testFormatCustom() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "?$format=cvs"));

    HttpResponse response = this.getHttpClient().execute(get);

    assertEquals(200, response.getStatusLine().getStatusCode());
    Header header = response.getFirstHeader(HttpHeaders.CONTENT_TYPE);

    assertEquals("cvs", header.getValue());
  }

  @Test
  public void testFormatNotAccepted() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "?$format=cvsOrSomethingElse"));

    HttpResponse response = this.getHttpClient().execute(get);

    assertEquals(406, response.getStatusLine().getStatusCode());
  }
}
