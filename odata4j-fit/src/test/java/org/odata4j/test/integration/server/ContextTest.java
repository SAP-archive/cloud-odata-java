package org.odata4j.test.integration.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.SecurityContext;

import org.eclipse.jetty.client.ContentExchange;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.odata4j.core.ODataConstants.Headers;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataHeadersContext;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.OMediaLinkExtensions;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.test.integration.AbstractJettyHttpClientTest;
import org.odata4j.test.integration.producer.custom.CustomProducer;

/**
 * test for the new ODataContext producer parameter.
 * 
 */
public class ContextTest extends AbstractJettyHttpClientTest {

  public ContextTest(RuntimeFacadeType type) {
    super(type);
  }

  @Override
  protected void registerODataProducer() throws Exception {
    DefaultODataProducerProvider.setInstance(mockProducer());
  }

  CustomProducer producer;

  protected ODataProducer mockProducer() {
    CustomProducer cp = new CustomProducer();
    producer = spy(cp); // mock(ODataProducer.class);

    return producer;
  }

  @BeforeClass
  public static void initClass() {
    myHeaders = getHeaders();
  }

  private static Map<String, List<String>> getHeaders() {

    Map<String, List<String>> h = new HashMap<String, List<String>>();
    h.put("X-Foo", Collections.singletonList("Bar"));
    List<String> cookies = new ArrayList<String>();
    cookies.add("Cookie1=c1val");
    cookies.add("Cookie2=c2val");
    h.put("Set-Cookie", cookies);
    h.put("Content-Type", Collections.singletonList("application/json"));
    return h;
  }

  private static Map<String, List<String>> myHeaders;
  private ArgumentCaptor<ODataContext> context;

  @Before
  public void initTest() {
    context = ArgumentCaptor.forClass(ODataContext.class);
  }

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void testGetEntities() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories", myHeaders);

    verify(producer).getEntities(context.capture(), eq("Directories"), any(QueryInfo.class));

    assertContext();
  }

  @Test
  public void testGetEntitiesCount() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories/$count", myHeaders);

    verify(producer).getEntitiesCount(context.capture(), eq("Directories"), any(QueryInfo.class));

    assertContext();
  }

  @Test
  public void testGetEntity() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('Dir-0')", myHeaders);

    verify(producer).getEntity(context.capture(), eq("Directories"), any(OEntityKey.class), any(EntityQueryInfo.class));

    assertContext();
  }

  @Test
  public void testGetNavProperty() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('Dir-0')/Files", myHeaders);

    verify(producer).getNavProperty(context.capture(), eq("Directories"), any(OEntityKey.class), eq("Files"), any(QueryInfo.class));

    assertContext();
  }

  @Test
  public void testGetNavPropertyCount() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('Dir-0')/Files/$count", myHeaders);

    verify(producer).getNavPropertyCount(context.capture(), eq("Directories"), any(OEntityKey.class), eq("Files"), any(QueryInfo.class));

    assertContext();
  }

  @Test
  public void testCreateEntity() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories", myHeaders, "POST", "{ \"Name\" : \"NewDir\" }");

    verify(producer).createEntity(context.capture(), eq("Directories"), any(OEntity.class));

    assertContext();
  }

  @Test
  public void testCreateRelatedEntity() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('MyDir')/Items", myHeaders, "POST", "{ \"Name\" : \"NewFile\" }");

    verify(producer).createEntity(context.capture(), eq("Directories"), any(OEntityKey.class), eq("Items"), any(OEntity.class));

    assertContext();
  }

  @Test
  public void testDeleteEntity() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('MyDir')", myHeaders, "DELETE", "");

    verify(producer).deleteEntity(context.capture(), eq("Directories"), any(OEntityKey.class));

    assertContext();
  }

  @Test
  public void testMergeEntity() throws IOException, Exception {

    Map<String, List<String>> headers = getHeaders();
    headers.put(Headers.X_HTTP_METHOD, Collections.singletonList("MERGE"));
    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('MyDir')", headers, "POST", "{ \"DirProp1\" : \"prop1value\" }");

    verify(producer).mergeEntity(context.capture(), eq("Directories"), any(OEntity.class));

    assertContext(headers);
  }

  @Test
  public void testUpdateEntity() throws IOException, Exception {

    Map<String, List<String>> headers = getHeaders();
    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('MyDir')", headers, "PUT", "{ \"DirProp1\" : \"prop1value\" }");

    verify(producer).updateEntity(context.capture(), eq("Directories"), any(OEntity.class));

    assertContext(headers);
  }

  @Test
  public void testGetLinks() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('Dir-0')/$links/Files", myHeaders);

    verify(producer).getLinks(context.capture(), any(OEntityId.class), eq("Files"));

    assertContext();
  }

  @Test
  public void testCreateLink() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('Dir-0')/$links/Files", myHeaders, "POST", "{\"uri\": \"http://host/service.svc/Files('myfile')\"}");

    verify(producer).createLink(context.capture(), any(OEntityId.class), eq("Files"), any(OEntityId.class));

    assertContext();
  }

  @Test
  public void testUpdateLink() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('Dir-0')/$links/Files", myHeaders, "PUT", "{\"uri\": \"http://host/service.svc/Files('myfile2')\"}");

    verify(producer).updateLink(context.capture(), any(OEntityId.class), eq("Files"), any(OEntityKey.class), any(OEntityId.class));

    assertContext();
  }

  @Test
  public void testDeleteLink() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "Directories('Dir-0')/$links/Files", myHeaders, "DELETE", "{\"uri\": \"http://host/service.svc/Files('myfile2')\"}");

    verify(producer).deleteLink(context.capture(), any(OEntityId.class), eq("Files"), any(OEntityKey.class));

    assertContext();
  }

  @Test
  public void testCallFunction() throws IOException, Exception {

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "f?p='foo'", myHeaders);

    verify(producer).callFunction(context.capture(), any(EdmFunctionImport.class), any(Map.class), any(QueryInfo.class));

    assertContext();
  }

  @Test
  public void testMLE() throws IOException, Exception {

    producer.extensionFactory = mock(OMediaLinkExtensions.class);

    ContentExchange exchange = sendRequestWithHeaders(BASE_URI + "MLEs('foobar')/$value", myHeaders);

    verify(producer).findExtension(OMediaLinkExtensions.class);
    verify(producer.extensionFactory).create(context.capture());
    assertContext();
  }

  private void assertContext() {
    assertContext(myHeaders);
  }

  private void assertContext(Map<String, List<String>> headers) {

    // first: did all of the headers we sent make it into the producer via ODataContext?
    assertHeaders(headers);

    // next: did the SecurityContext make it into the producer via ODataContext?
    Object o = context.getValue().getContextAspect(SecurityContext.class);
    assertTrue(o != null && SecurityContext.class.isAssignableFrom(o.getClass()));
  }

  private void assertHeaders(Map<String, List<String>> headers) {
    ODataHeadersContext got = context.getValue().getRequestHeadersContext();

    for (Entry<String, List<String>> e : headers.entrySet()) {
      Iterable<String> gotVals = got.getRequestHeaderValues(e.getKey());
      int n = 0;
      String firstGotVal = null;
      for (String gotV : gotVals) {
        if (n == 0) {
          firstGotVal = gotV;
        }
        assertTrue(e.getValue().contains(gotV));
        n += 1;
      }
      assertEquals(e.getValue().size(), n);
      if (n == 1) {
        assertEquals(firstGotVal, got.getRequestHeaderValue(e.getKey()));
      }
    }
  }

  private ContentExchange sendRequestWithHeaders(String url, Map<String, List<String>> headers) throws IOException, InterruptedException {
    return sendRequestWithHeaders(url, headers, null, null);
  }

  private ContentExchange sendRequestWithHeaders(String url, Map<String, List<String>> headers, String method, String payload) throws IOException, InterruptedException {
    ContentExchange exchange = new ContentExchange(true);
    exchange.setURL(url);
    if (null != method) {
      exchange.setMethod(method);
    }
    if (null != payload) {
      exchange.setRequestContentSource(new ByteArrayInputStream(payload.getBytes()));
    }

    for (Entry<String, List<String>> e : headers.entrySet()) {
      for (String val : e.getValue()) {
        exchange.addRequestHeader(e.getKey(), val);
      }
    }

    client.send(exchange);
    exchange.waitForDone();
    return exchange;
  }

}
