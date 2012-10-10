package org.odata4j.cxf.consumer;

import javax.ws.rs.ext.RuntimeDelegate;

import org.odata4j.consumer.AbstractODataConsumer;
import org.odata4j.consumer.ODataClient;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.format.FormatType;

/**
 * <code>ODataConsumer</code> is the client-side interface to an OData service.
 *
 * <p>Use {@link #create(String)} or one of the other static factory methods to connect to an existing OData service.</p>
 */
public class ODataCxfConsumer extends AbstractODataConsumer {

  private ODataCxfClient client;

  private ODataCxfConsumer(FormatType type, String serviceRootUri, OClientBehavior... behaviors) {
    super(serviceRootUri);

    // ensure that the correct JAX-RS implementation (CXF) is loaded
    if (!(RuntimeDelegate.getInstance() instanceof org.apache.cxf.jaxrs.impl.RuntimeDelegateImpl))
      RuntimeDelegate.setInstance(new org.apache.cxf.jaxrs.impl.RuntimeDelegateImpl());

    this.client = new ODataCxfClient(type, behaviors);
  }

  @Override
  protected ODataClient getClient() {
    return client;
  }

  public static class Builder implements ODataConsumer.Builder {

    private FormatType formatType;
    private String serviceRootUri;
    private OClientBehavior[] clientBehaviors;

    private Builder(String serviceRootUri) {
      this.serviceRootUri = serviceRootUri;
      this.formatType = FormatType.ATOM;
    }

    /**
     * Sets a preferred {@link FormatType}. Defaults to {@code FormatType.ATOM}.
     *
     * @param formatType  the format type
     * @return this builder
     */
    public Builder setFormatType(FormatType formatType) {
      this.formatType = formatType;
      return this;
    }

    /**
     * Sets one or more client behaviors.
     *
     * <p>Client behaviors transform http requests to interact with services that require custom extensions.
     *
     * @param clientBehaviors  the client behaviors
     * @return this builder
     */
    public Builder setClientBehaviors(OClientBehavior... clientBehaviors) {
      this.clientBehaviors = clientBehaviors;
      return this;
    }

    /**
     * Builds the {@link ODataCxfConsumer} object.
     *
     * @return a new OData consumer
     */
    public ODataCxfConsumer build() {
      if (this.clientBehaviors != null) {
        return new ODataCxfConsumer(this.formatType, this.serviceRootUri, this.clientBehaviors);
      } else {
        return new ODataCxfConsumer(this.formatType, this.serviceRootUri);
      }
    }
  }

  /**
   * Constructs a new builder for an {@link ODataCxfConsumer} object.
   *
   * @param serviceRootUri  the OData service root uri
   */
  public static Builder newBuilder(String serviceRootUri) {
    return new Builder(serviceRootUri);
  }

  /**
   * Creates a new consumer for the given OData service uri.
   *
   * <p>Wrapper for {@code ODataCxfConsumer.newBuilder(serviceRootUri).build()}.
   *
   * @param serviceRootUri  the service uri <p>e.g. <code>http://services.odata.org/Northwind/Northwind.svc/</code></p>
   * @return a new OData consumer
   */
  public static ODataCxfConsumer create(String serviceRootUri) {
    return ODataCxfConsumer.newBuilder(serviceRootUri).build();
  }
}
