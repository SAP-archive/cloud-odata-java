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
import org.junit.Before;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataSingleProcessorService;
import com.sap.core.odata.testutil.fit.AbstractFitTest;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.helper.XMLUnitHelper;
import com.sap.core.odata.testutil.server.TestServer;

public class MappingTest extends AbstractFitTest {

  public static void main(String[] args) {
    TestServer server = new TestServer();
    try {
      server.startServer(MapFactory.class);
      System.out.println("Press any key to exit");
      new BufferedReader(new InputStreamReader(System.in)).readLine();
    } catch (IOException e) {
      e.printStackTrace(System.err);
    } finally {
      if (server != null)
        server.stopServer();
    }
  }

  @Before
  public void before() {
    super.before();
    Map<String, String> ns = new HashMap<String, String>();
    ns.put("d", Edm.NAMESPACE_D_2007_08);
    ns.put("m", Edm.NAMESPACE_M_2007_08);
    ns.put("a", Edm.NAMESPACE_ATOM_2005);
    XMLUnitHelper.registerXmlNs(ns);
  }

  @Override
  protected ODataSingleProcessorService createService() throws ODataException {
    return MapFactory.create();
  }

  @Test
  public void testServiceDocument() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    System.out.println(payload);
    assertEquals(200, response.getStatusLine().getStatusCode());

  }

  @Test
  public void testMetadata() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/$metadata"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    System.out.println(payload);
    assertEquals(200, response.getStatusLine().getStatusCode());

  }

  @Test
  public void testEntitySet() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/mappings"));
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(200, response.getStatusLine().getStatusCode());

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    for (int i = 1; i <= MapProcessor.RECORD_COUNT; i++) {
      assertXpathEvaluatesTo("V01." + i, "/a:feed/a:entry[" + i + "]/a:content/m:properties/d:p1", payload);
      assertXpathEvaluatesTo("V02." + i, "/a:feed/a:entry[" + i + "]/a:content/m:properties/d:p2", payload);
      assertXpathEvaluatesTo("V03." + i, "/a:feed/a:entry[" + i + "]/a:content/m:properties/d:p3", payload);
    }
  }

  @Test
  public void testEntity() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/mappings('V01.7')"));
    HttpResponse response = this.getHttpClient().execute(get);
    assertEquals(200, response.getStatusLine().getStatusCode());

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertXpathEvaluatesTo("V01.7" , "/a:entry/a:content/m:properties/d:p1", payload);
    assertXpathEvaluatesTo("V02.7" , "/a:entry/a:content/m:properties/d:p2", payload);
    assertXpathEvaluatesTo("V03.7" , "/a:entry/a:content/m:properties/d:p3", payload);

  }

  @Test
  public void testPropertyValue() throws Exception {
    HttpGet get = new HttpGet(URI.create(this.getEndpoint().toString() + "/mappings('V01.7')/p2/$value"));
    HttpResponse response = this.getHttpClient().execute(get);

    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals(200, response.getStatusLine().getStatusCode());
    
    assertEquals("V02.7", payload);
  }
}
