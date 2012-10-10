package org.odata4j.test.integration.server;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.regex.Pattern;

import javax.ws.rs.core.MediaType;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpExchange;
import org.eclipse.jetty.http.HttpHeaders;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Test;
import org.odata4j.producer.ErrorResponseExtension;
import org.odata4j.producer.ErrorResponseExtensions;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.test.integration.AbstractJettyHttpClientTest;
import org.odata4j.test.integration.TestInMemoryProducers;

public class ErrorTest extends AbstractJettyHttpClientTest {

  private static final String FEED_URL = BASE_URI + TestInMemoryProducers.SIMPLE_ENTITY_SET_NAME;

  private InMemoryProducer producerSpy;

  public ErrorTest(RuntimeFacadeType type) {
    super(type);
  }

  @Override
  protected void registerODataProducer() throws Exception {
    producerSpy = spy(TestInMemoryProducers.simple());
    DefaultODataProducerProvider.setInstance(producerSpy);
  }

  private void simulateErrorResponseExtension() {
    when(producerSpy.findExtension(ErrorResponseExtension.class)).thenReturn(ErrorResponseExtensions.returnInnerErrors());
  }

  @Test
  public void notFoundXml() throws Exception {
    ContentExchange exchange = sendRequest(FEED_URL + "('Z')");
    exchange.waitForDone();
    assertThat(exchange.getStatus(), is(HttpExchange.STATUS_COMPLETED));
    assertThat(exchange.getResponseStatus(), is(HttpStatus.NOT_FOUND_404));
    assertThat(exchange.getResponseFields().getStringField(HttpHeaders.CONTENT_TYPE), containsString(MediaType.APPLICATION_XML));
    assertThat(exchange.getResponseContent().length(), greaterThan(0));
    assertRegexMatches(exchange.getResponseContent(), ".*<code>NotFoundException</code>.*");
    assertRegexNotMatches(exchange.getResponseContent(), ".*<innererror>.+</innererror>.*");
  }

  @Test
  public void notFoundJson() throws Exception {
    ContentExchange exchange = sendRequest(FEED_URL + "('Z')?$format=json");
    exchange.waitForDone();
    assertThat(exchange.getStatus(), is(HttpExchange.STATUS_COMPLETED));
    assertThat(exchange.getResponseStatus(), is(HttpStatus.NOT_FOUND_404));
    assertThat(exchange.getResponseFields().getStringField(HttpHeaders.CONTENT_TYPE), containsString(MediaType.APPLICATION_JSON));
    assertThat(exchange.getResponseContent().length(), greaterThan(0));
    assertRegexMatches(exchange.getResponseContent(), ".*\"code\"\\s*:\\s*\"NotFoundException\".*");
    assertRegexNotMatches(exchange.getResponseContent(), ".*\"innererror\"\\s*:\\s*\".+\".*");
  }

  @Test
  public void badRequestXmlWithInnerError() throws Exception {
    simulateErrorResponseExtension();
    ContentExchange exchange = sendRequest(FEED_URL + "(1.2)");
    exchange.waitForDone();
    assertThat(exchange.getStatus(), is(HttpExchange.STATUS_COMPLETED));
    assertThat(exchange.getResponseStatus(), is(HttpStatus.BAD_REQUEST_400));
    assertThat(exchange.getResponseFields().getStringField(HttpHeaders.CONTENT_TYPE), containsString(MediaType.APPLICATION_XML));
    assertThat(exchange.getResponseContent().length(), greaterThan(0));
    assertRegexMatches(exchange.getResponseContent(), ".*<code>BadRequestException</code>.*<innererror>.+</innererror>.*");
  }

  @Test
  public void badRequestJsonWithInnerError() throws Exception {
    simulateErrorResponseExtension();
    ContentExchange exchange = sendRequest(FEED_URL + "(1.2)?$format=json");
    exchange.waitForDone();
    assertThat(exchange.getStatus(), is(HttpExchange.STATUS_COMPLETED));
    assertThat(exchange.getResponseStatus(), is(HttpStatus.BAD_REQUEST_400));
    assertThat(exchange.getResponseFields().getStringField(HttpHeaders.CONTENT_TYPE), containsString(MediaType.APPLICATION_JSON));
    assertThat(exchange.getResponseContent().length(), greaterThan(0));
    assertRegexMatches(exchange.getResponseContent(), ".*\"code\"\\s*:\\s*\"BadRequestException\".*\"innererror\"\\s*:\\s*\".+\".*");
  }

  private void assertRegexMatches(String source, String target) throws Exception {
    assertRegexMatches(source, target, true);
  }

  private void assertRegexNotMatches(String source, String target) throws Exception {
    assertRegexMatches(source, target, false);
  }

  private void assertRegexMatches(String source, String target, boolean matches) throws Exception {
    assertEquals(matches, Pattern.compile(target, Pattern.DOTALL).matcher(source).matches());
  }
}
