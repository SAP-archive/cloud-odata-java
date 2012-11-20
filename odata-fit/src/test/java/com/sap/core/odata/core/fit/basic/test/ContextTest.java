package com.sap.core.odata.core.fit.basic.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
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
import com.sap.core.odata.api.service.ODataService;
import com.sap.core.odata.api.uri.resultviews.GetMetadataView;

public class ContextTest extends AbstractBasicTest {

  @Override
  protected ODataSingleProcessor createProcessorMock() throws ODataException {
    ODataSingleProcessor processor = super.createProcessorMock();
    when(((Metadata) processor).readMetadata(any(GetMetadataView.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
    return processor;
  }

  @Test
  public void checkContextExists() throws ClientProtocolException, IOException, ODataException {
    assertNull(this.getProcessor().getContext());
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);

    ODataContext ctx = this.getProcessor().getContext();
    assertNotNull(ctx);

    ODataService service = ctx.getService();
    assertNotNull(service);

    assertEquals(Status.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    assertEquals("$metadata", ctx.getODataPathSegmentList().get(0).getPath());
  }

}
