package org.odata4j.test.integration;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.format.FormatType;

/**
 * Base integration test class that uses an ODataConsumer as client.
 */
public abstract class AbstractODataConsumerTest extends AbstractIntegrationTest {

  /**
   * The ODataConsumer instance.
   */
  protected ODataConsumer consumer;

  /**
   * The preferred FormatType.
   */
  protected final FormatType format;

  public AbstractODataConsumerTest(RuntimeFacadeType type, FormatType format) {
    super(type);
    this.format = format;
  }

  public AbstractODataConsumerTest(RuntimeFacadeType type) {
    super(type);
    this.format = null;
  }

  @Override
  protected void startClient() throws Exception {
    consumer = rtFacade.createODataConsumer(BASE_URI, format);
  }

  @Override
  protected void stopClient() throws Exception {
    consumer = null;
  }
}
