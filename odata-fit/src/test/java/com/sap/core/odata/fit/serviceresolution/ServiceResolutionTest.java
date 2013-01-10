package com.sap.core.odata.fit.serviceresolution;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.api.processor.ODataSingleProcessor;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;
import com.sap.core.odata.api.processor.feature.Metadata;
import com.sap.core.odata.api.processor.feature.ServiceDocument;
import com.sap.core.odata.api.uri.info.GetMetadataUriInfo;
import com.sap.core.odata.api.uri.info.GetServiceDocumentUriInfo;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.fit.FitStaticServiceFactory;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.server.TestServer;

/**
 * @author SAP AG
 */
public class ServiceResolutionTest extends BaseTest {

  private HttpClient httpClient = new DefaultHttpClient();
  private TestServer server = new TestServer();
  private ODataContext context;
  private ODataSingleProcessorService service;

  @Before
  public void before() {
    try {
      ODataSingleProcessor processor = mock(ODataSingleProcessor.class);
      EdmProvider provider = mock(EdmProvider.class);

      this.service = new ODataSingleProcessorService(provider, processor) {};
      FitStaticServiceFactory.setService(this.service);

      // science fiction (return context after setContext)
      // see http://www.planetgeek.ch/2010/07/20/mockito-answer-vs-return/

      doAnswer(new Answer<Object>() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
          ServiceResolutionTest.this.context = (ODataContext) invocation.getArguments()[0];
          return null;
        }
      }).when(processor).setContext(any(ODataContext.class));

      when(processor.getContext()).thenAnswer(new Answer<ODataContext>() {
        @Override
        public ODataContext answer(InvocationOnMock invocation) throws Throwable {
          return ServiceResolutionTest.this.context;
        }
      });

      when(((Metadata) processor).readMetadata(any(GetMetadataUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("metadata").status(HttpStatusCodes.OK).build());
      when(((ServiceDocument) processor).readServiceDocument(any(GetServiceDocumentUriInfo.class), any(String.class))).thenReturn(ODataResponse.entity("servicedocument").status(HttpStatusCodes.OK).build());
    } catch (ODataException e) {
      throw new RuntimeException(e);
    }
  }

  @After
  public void after() {
    try {
      if (this.server != null) {
        this.server.stopServer();
      }
    } finally {
      FitStaticServiceFactory.setService(null);
    }
  }

  @Test
  public void testSplit0() throws ClientProtocolException, IOException, ODataException {
    this.server.setPathSplit(0);
    this.server.startServer(FitStaticServiceFactory.class);

    HttpGet get = new HttpGet(URI.create(this.server.getEndpoint().toString() + "/$metadata"));
    HttpResponse response = this.httpClient.execute(get);

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    ODataContext ctx = this.service.getProcessor().getContext();
    assertNotNull(ctx);

    assertTrue(ctx.getPathInfo().getPrecedingSegments().isEmpty());
    assertEquals("$metadata", ctx.getPathInfo().getODataSegments().get(0).getPath());
  }

  @Test
  public void testSplit1() throws ClientProtocolException, IOException, ODataException {
    this.server.setPathSplit(1);
    this.server.startServer(FitStaticServiceFactory.class);

    HttpGet get = new HttpGet(URI.create(this.server.getEndpoint().toString() + "/aaa/$metadata"));
    HttpResponse response = this.httpClient.execute(get);

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    ODataContext ctx = this.service.getProcessor().getContext();
    assertNotNull(ctx);

    assertEquals("aaa", ctx.getPathInfo().getPrecedingSegments().get(0).getPath());
    assertEquals("$metadata", ctx.getPathInfo().getODataSegments().get(0).getPath());
  }

  @Test
  public void testSplit2() throws ClientProtocolException, IOException, ODataException {
    this.server.setPathSplit(2);
    this.server.startServer(FitStaticServiceFactory.class);

    HttpGet get = new HttpGet(URI.create(this.server.getEndpoint().toString() + "/aaa/bbb/$metadata"));
    HttpResponse response = this.httpClient.execute(get);

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    ODataContext ctx = this.service.getProcessor().getContext();
    assertNotNull(ctx);

    assertEquals("aaa", ctx.getPathInfo().getPrecedingSegments().get(0).getPath());
    assertEquals("bbb", ctx.getPathInfo().getPrecedingSegments().get(1).getPath());
    assertEquals("$metadata", ctx.getPathInfo().getODataSegments().get(0).getPath());
  }

  @Test
  public void testSplitUrlToShort() throws ClientProtocolException, IOException, ODataException {
    this.server.setPathSplit(3);
    this.server.startServer(FitStaticServiceFactory.class);

    HttpGet get = new HttpGet(URI.create(this.server.getEndpoint().toString() + "/aaa/$metadata"));
    HttpResponse response = this.httpClient.execute(get);

    assertEquals(HttpStatusCodes.BAD_REQUEST.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testSplitUrlServiceDocument() throws ClientProtocolException, IOException, ODataException {
    this.server.setPathSplit(1);
    this.server.startServer(FitStaticServiceFactory.class);

    HttpGet get = new HttpGet(URI.create(this.server.getEndpoint().toString() + "/aaa/"));
    HttpResponse response = this.httpClient.execute(get);

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    ODataContext ctx = this.service.getProcessor().getContext();
    assertNotNull(ctx);

    assertEquals("", ctx.getPathInfo().getODataSegments().get(0).getPath());
    assertEquals("aaa", ctx.getPathInfo().getPrecedingSegments().get(0).getPath());
  }

  @Test
  public void testMatrixParameterInNonODataPath() throws ClientProtocolException, IOException, ODataException {
    this.server.setPathSplit(1);
    this.server.startServer(FitStaticServiceFactory.class);

    HttpGet get = new HttpGet(URI.create(this.server.getEndpoint().toString() + "aaa;n=2/"));
    HttpResponse response = this.httpClient.execute(get);

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    ODataContext ctx = this.service.getProcessor().getContext();
    assertNotNull(ctx);

    assertEquals("", ctx.getPathInfo().getODataSegments().get(0).getPath());
    assertEquals("aaa", ctx.getPathInfo().getPrecedingSegments().get(0).getPath());

    assertNotNull(ctx.getPathInfo().getPrecedingSegments().get(0).getMatrixParameters());

    String key, value;
    key = ctx.getPathInfo().getPrecedingSegments().get(0).getMatrixParameters().keySet().iterator().next();
    assertEquals("n", key);
    value = ctx.getPathInfo().getPrecedingSegments().get(0).getMatrixParameters().get(key).get(0);
    assertEquals("2", value);
  }

  @Test
  public void testNoMatrixParameterInODataPath() throws ClientProtocolException, IOException, ODataException {
    this.server.setPathSplit(0);
    this.server.startServer(FitStaticServiceFactory.class);

    HttpGet get = new HttpGet(URI.create(this.server.getEndpoint().toString() + "$metadata;matrix"));
    HttpResponse response = this.httpClient.execute(get);

    InputStream stream = response.getEntity().getContent();
    String body = StringHelper.inputStreamToString(stream);

    assertTrue(body.contains("metadata"));
    assertTrue(body.contains("matrix"));
    assertEquals(HttpStatusCodes.NOT_FOUND.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testBaseUriWithMatrixParameter() throws ClientProtocolException, IOException, ODataException, URISyntaxException {
    this.server.setPathSplit(3);
    this.server.startServer(FitStaticServiceFactory.class);

    HttpGet get = new HttpGet(URI.create(this.server.getEndpoint().toString() + "aaa/bbb;n=2,3;m=1/ccc/"));
    HttpResponse response = this.httpClient.execute(get);

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    ODataContext ctx = this.service.getProcessor().getContext();
    assertNotNull(ctx);
    assertEquals(this.server.getEndpoint() + "aaa/bbb;n=2,3;m=1/ccc/", ctx.getPathInfo().getServiceRoot().toASCIIString());
  }

  @Test
  public void testBaseUriWithEncoding() throws ClientProtocolException, IOException, ODataException, URISyntaxException {
    this.server.setPathSplit(3);
    this.server.startServer(FitStaticServiceFactory.class);

    URI uri = new URI(this.server.getEndpoint().getScheme(), null, this.server.getEndpoint().getHost(), this.server.getEndpoint().getPort(), this.server.getEndpoint().getPath() + "/aaa/äдержb;n=2,3;m=1/c c/", null, null);

    HttpGet get = new HttpGet(uri);
    HttpResponse response = this.httpClient.execute(get);

    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    ODataContext ctx = this.service.getProcessor().getContext();
    assertNotNull(ctx);
    assertEquals(this.server.getEndpoint() + "aaa/%C3%A4%D0%B4%D0%B5%D1%80%D0%B6b;n=2,3;m=1/c%20c/", ctx.getPathInfo().getServiceRoot().toASCIIString());
  }

}
