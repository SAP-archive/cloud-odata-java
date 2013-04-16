package com.sap.core.odata.fit.mapping;

import static junit.framework.Assert.assertEquals;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.ODataService;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.testutil.fit.AbstractFitTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.server.TestServer;

/**
 * @author SAP AG
 */
public class MappingTest extends AbstractFitTest {

  public static void main(final String[] args) {
    final TestServer server = new TestServer();
    try {
      server.startServer(MapFactory.class);
      System.out.println("Press any key to exit");
      new BufferedReader(new InputStreamReader(System.in)).readLine();
    } catch (final IOException e) {
      e.printStackTrace(System.err);
    } finally {
      server.stopServer();
    }
  }

  @Override
  @Before
  public void before() {
    super.before();
    Map<String, String> prefixMap = new HashMap<String, String>();
    prefixMap.put("a", Edm.NAMESPACE_ATOM_2005);
    prefixMap.put("d", Edm.NAMESPACE_D_2007_08);
    prefixMap.put("m", Edm.NAMESPACE_M_2007_08);
    XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(prefixMap));
  }

  @Override
  protected ODataService createService() throws ODataException {
    return MapFactory.create();
  }

  @Test
  public void testServiceDocument() throws Exception {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/"));
    final HttpResponse response = getHttpClient().execute(get);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testMetadata() throws Exception {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/$metadata"));
    final HttpResponse response = getHttpClient().execute(get);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());
  }

  @Test
  public void testEntitySet() throws Exception {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/mappings"));
    final HttpResponse response = getHttpClient().execute(get);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    for (int i = 1; i <= MapProcessor.RECORD_COUNT; i++) {
      assertXpathEvaluatesTo("V01." + i, "/a:feed/a:entry[" + i + "]/a:content/m:properties/d:p1", payload);
      assertXpathEvaluatesTo("V02." + i, "/a:feed/a:entry[" + i + "]/a:content/m:properties/d:p2", payload);
      assertXpathEvaluatesTo("V03." + i, "/a:feed/a:entry[" + i + "]/a:content/m:properties/d:p3", payload);
    }
  }

  @Test
  public void testEntity() throws Exception {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/mappings('V01.7')"));
    final HttpResponse response = getHttpClient().execute(get);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertXpathEvaluatesTo("V01.7", "/a:entry/a:content/m:properties/d:p1", payload);
    assertXpathEvaluatesTo("V02.7", "/a:entry/a:content/m:properties/d:p2", payload);
    assertXpathEvaluatesTo("V03.7", "/a:entry/a:content/m:properties/d:p3", payload);
  }

  @Test
  public void testPropertyValue() throws Exception {
    final HttpGet get = new HttpGet(URI.create(getEndpoint().toString() + "/mappings('V01.7')/p2/$value"));
    final HttpResponse response = getHttpClient().execute(get);
    assertEquals(HttpStatusCodes.OK.getStatusCode(), response.getStatusLine().getStatusCode());

    final String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("V02.7", payload);
  }
}
