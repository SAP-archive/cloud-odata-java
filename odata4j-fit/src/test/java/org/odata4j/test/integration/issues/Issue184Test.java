package org.odata4j.test.integration.issues;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.test.integration.AbstractRuntimeTest;

public class Issue184Test extends AbstractRuntimeTest {

  private static final String BODY_JSON = "{\"name\":\"New Product\",\"releaseDate\":\"\\/Date(1337690793987)\\/\",\"rating\":1,\"price\":\"99.99\"}";

  private ODataServer server;

  private final static String endpointUri = "http://localhost:8810/Issue184.svc/";

  private Issue184MockProducer mockProducer;

  public Issue184Test(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void before() {

    this.mockProducer = new Issue184MockProducer();

    DefaultODataProducerProvider.setInstance(this.mockProducer);
    this.server = this.rtFacade.startODataServer(Issue184Test.endpointUri);
  }

  @After
  public void after() {
    if (this.server != null) {
      this.server.stop();
    }
  }

  @Test
  public void readMetadata() throws ClientProtocolException, IOException {
    HttpGet request = new HttpGet(Issue184Test.endpointUri + "$metadata");

    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = httpclient.execute(request);

    System.out.println(response.getEntity().toString());

    assertEquals(200, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPutDataServiceVersion10() throws ClientProtocolException, IOException {
    HttpPost request = new HttpPost(Issue184Test.endpointUri + "Products");

    StringEntity entity = new StringEntity(Issue184Test.BODY_JSON);
    request.setEntity(entity);

    request.setHeader("Content-Type", "application/json;odata=verbose");
    request.setHeader("Accept", "application/json");
    request.setHeader("DataServiceVersion", "1.0");
    request.setHeader("MaxDataServiceVersion", "1.0");

    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = httpclient.execute(request);

    assertEquals(201, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPutDataServiceVersion20() throws ClientProtocolException, IOException {
    HttpPost request = new HttpPost(Issue184Test.endpointUri + "Products");

    StringEntity entity = new StringEntity(Issue184Test.BODY_JSON);
    request.setEntity(entity);

    request.setHeader("Content-Type", "application/json;odata=verbose");
    request.setHeader("Accept", "application/json");
    request.setHeader("DataServiceVersion", "2.0");
    request.setHeader("MaxDataServiceVersion", "2.0");

    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = httpclient.execute(request);

    assertEquals(201, response.getStatusLine().getStatusCode());
  }

  @Test
  public void testPutDataServiceVersionNone() throws ClientProtocolException, IOException {
    HttpPost request = new HttpPost(Issue184Test.endpointUri + "Products");

    StringEntity entity = new StringEntity(Issue184Test.BODY_JSON);
    request.setEntity(entity);

    request.setHeader("Content-Type", "application/json;odata=verbose");
    request.setHeader("Accept", "application/json");

    HttpClient httpclient = new DefaultHttpClient();
    HttpResponse response = httpclient.execute(request);

    assertEquals(201, response.getStatusLine().getStatusCode());
  }
}
