package com.sap.core.odata.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.URI;

import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.aspect.Metadata;
import com.sap.core.odata.api.processor.aspect.ServiceDocument;
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;
import com.sap.core.odata.api.uri.resultviews.GetServiceDocumentView;

public class ContextTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessor() throws ODataException {
    ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
    when(((Metadata) processor).readMetadata(any(GetMetadataView.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    when(((ServiceDocument) processor).readServiceDocument(any(GetServiceDocumentView.class))).thenReturn(ODataResponse.entity("service document").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void checkContextExists() throws ClientProtocolException, IOException, ODataException {
    assertNull(this.getService().getProcessor().getContext());
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);

    ODataContext ctx = this.getService().getProcessor().getContext();
    assertNotNull(ctx);

    ODataService service = ctx.getService();
    assertNotNull(service);

    assertEquals(Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    assertEquals("$metadata", ctx.getUriInfo().getODataPathSegmentList().get(0).getPath());
  }

  @Test
  public void checkBaseUriForServiceDocument() throws ClientProtocolException, IOException, ODataException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString()));
    this.getHttpClient().execute(get);

    ODataContext ctx = this.getService().getProcessor().getContext();
    assertNotNull(ctx);
    assertEquals(this.getEndpoint().toString(), ctx.getUriInfo().getBaseUri().toASCIIString());
  }

  @Test
  public void checkBaseUriForMetadata() throws ClientProtocolException, IOException, ODataException {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/$metadata"));
    this.getHttpClient().execute(get);

    ODataContext ctx = this.getService().getProcessor().getContext();
    assertNotNull(ctx);
    assertEquals(this.getEndpoint().toString(), ctx.getUriInfo().getBaseUri().toASCIIString());
  }

}
