package org.odata4j.consumer;

import java.util.ArrayList;
import java.util.List;

import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityIds;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OEntityRequest;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.format.SingleLink;
import org.odata4j.format.SingleLinks;
import org.odata4j.internal.EntitySegment;

/**
 * Shared consumer request implementation for operations dealing with a single response entity.
 */
public abstract class AbstractConsumerEntityRequest<T> implements OEntityRequest<T> {

  private final ODataClient client;

  private final EdmDataServices metadata;
  private final String serviceRootUri;
  private final List<EntitySegment> segments = new ArrayList<EntitySegment>();

  public AbstractConsumerEntityRequest(ODataClient client, String serviceRootUri,
      EdmDataServices metadata, String entitySetName, OEntityKey key) {

    this.client = client;
    this.serviceRootUri = serviceRootUri;
    this.metadata = metadata;

    segments.add(new EntitySegment(entitySetName, key));
  }

  protected ODataClient getClient() {
    return client;
  }

  protected EdmDataServices getMetadata() {
    return metadata;
  }

  protected List<EntitySegment> getSegments() {
    return segments;
  }

  protected String getServiceRootUri() {
    return serviceRootUri;
  }

  @Override
  public OEntityRequest<T> nav(String navProperty, OEntityKey key) {
    segments.add(new EntitySegment(navProperty, key));
    return this;
  }

  @Override
  public OEntityRequest<T> nav(String navProperty) {
    segments.add(new EntitySegment(navProperty, null));
    return this;
  }

  protected SingleLink toSingleLink(OEntityId entity) {
    String uri = getServiceRootUri();
    if (!uri.endsWith("/"))
      uri += "/";
    uri += OEntityIds.toKeyString(entity);
    return SingleLinks.create(uri);
  }

}