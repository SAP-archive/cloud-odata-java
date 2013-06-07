package com.sap.core.odata.fit.basic.issues;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.part.MetadataProcessor;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.fit.basic.AbstractBasicTest;

/**
 * @author SAP AG
 */
public class TestIssue105 extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    final ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((MetadataProcessor) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void checkContextForDifferentHostNamesRequests() throws ClientProtocolException, IOException, ODataException, URISyntaxException {
    URI uri1 = URI.create(getEndpoint().toString() + "$metadata");

    HttpGet get1 = new HttpGet(uri1);
    HttpResponse response1 = getHttpClient().execute(get1);
    assertNotNull(response1);

    URI serviceRoot1 = getService().getProcessor().getContext().getPathInfo().getServiceRoot();
    assertEquals(uri1.getHost(), serviceRoot1.getHost());

    get1.reset();

    URI uri2 = new URI(uri1.getScheme(), uri1.getUserInfo(), "127.0.0.1", uri1.getPort(), uri1.getPath(), uri1.getQuery(), uri1.getFragment());

    HttpGet get2 = new HttpGet(uri2);
    HttpResponse response2 = getHttpClient().execute(get2);
    assertNotNull(response2);

    URI serviceRoot2 = getService().getProcessor().getContext().getPathInfo().getServiceRoot();
    assertEquals(uri2.getHost(), serviceRoot2.getHost());
  }

}
