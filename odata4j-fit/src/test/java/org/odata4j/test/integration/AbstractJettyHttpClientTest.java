package org.odata4j.test.integration;

import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;

/**
 * Base integration test class that uses a Jetty HTTP client.
 */
public abstract class AbstractJettyHttpClientTest extends AbstractIntegrationTest {

  /**
   * The HttpClient instance.
   */
  protected HttpClient client;

  public AbstractJettyHttpClientTest(RuntimeFacadeType type) {
    super(type);
  }

  @Override
  protected void startClient() throws Exception {
    client = new HttpClient();
    client.setConnectorType(HttpClient.CONNECTOR_SELECT_CHANNEL);
    client.start();
  }

  @Override
  protected void stopClient() throws Exception {
    client.stop();
  }

  /**
   * Helper method to send an HTTP request.
   */
  protected ContentExchange sendRequest(String url) throws Exception {
    ContentExchange exchange = new ContentExchange(true);
    exchange.setURL(url);
    client.send(exchange);
    exchange.waitForDone();
    return exchange;
  }
}
