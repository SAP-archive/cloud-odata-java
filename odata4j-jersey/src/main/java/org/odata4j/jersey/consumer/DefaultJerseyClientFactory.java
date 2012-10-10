package org.odata4j.jersey.consumer;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;

/**
 * The default factory implementation for Jersey clients.
 *
 * <p>Use {@link #INSTANCE} to obtain a reference to the singleton instance of this factory.</p>
 */
public class DefaultJerseyClientFactory implements JerseyClientFactory {

  public static final DefaultJerseyClientFactory INSTANCE = new DefaultJerseyClientFactory();

  private DefaultJerseyClientFactory() {}

  /**
   * Creates a new default {@link Client} by calling: <code>Client.create(clientConfig)</code>
   */
  @Override
  public Client createClient(ClientConfig clientConfig) {
    Client client = Client.create(clientConfig);

    return client;
  }

}
