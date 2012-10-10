package org.odata4j.consumer;

import org.core4j.Enumerable;
import org.odata4j.core.OCreateRequest;
import org.odata4j.core.ODataConstants;
import org.odata4j.core.ODataVersion;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.exceptions.ODataProducerException;
import org.odata4j.format.Entry;
import org.odata4j.format.FormatParser;
import org.odata4j.format.FormatParserFactory;
import org.odata4j.format.Settings;
import org.odata4j.internal.FeedCustomizationMapping;
import org.odata4j.internal.InternalUtil;

/**
 * Create-request implementation.
 */
public class ConsumerCreateEntityRequest<T> extends AbstractConsumerEntityPayloadRequest implements OCreateRequest<T> {

  private final ODataClient client;
  private OEntity parent;
  private String navProperty;

  private final FeedCustomizationMapping fcMapping;

  public ConsumerCreateEntityRequest(ODataClient client, String serviceRootUri, EdmDataServices metadata, String entitySetName, FeedCustomizationMapping fcMapping) {
    super(entitySetName, serviceRootUri, metadata);
    this.client = client;
    this.fcMapping = fcMapping;
  }

  @SuppressWarnings("unchecked")
  @Override
  public T execute() throws ODataProducerException {

    EdmEntitySet ees = metadata.getEdmEntitySet(entitySetName);
    Entry entry = client.createRequestEntry(ees, null, props, links);

    StringBuilder url = new StringBuilder(serviceRootUri);
    if (parent != null) {
      url.append(InternalUtil.getEntityRelId(parent))
          .append("/")
          .append(navProperty);
    } else {
      url.append(entitySetName);
    }

    ODataClientRequest request = ODataClientRequest.post(url.toString(), entry);
    ODataClientResponse response = client.createEntity(request);

    ODataVersion version = InternalUtil.getDataServiceVersion(response.getHeaders()
        .getFirst(ODataConstants.Headers.DATA_SERVICE_VERSION));

    FormatParser<Entry> parser = FormatParserFactory.getParser(Entry.class,
        client.getFormatType(), new Settings(version, metadata, entitySetName, null, fcMapping));
    entry = parser.parse(client.getFeedReader(response));
    response.close();

    return (T) entry.getEntity();
  }

  @SuppressWarnings("unchecked")
  @Override
  public T get() {
    EdmEntitySet entitySet = metadata.getEdmEntitySet(entitySetName);
    return (T) OEntities.createRequest(entitySet, props, links);
  }

  @Override
  public OCreateRequest<T> properties(OProperty<?>... props) {
    return super.properties(this, props);
  }

  @Override
  public OCreateRequest<T> properties(Iterable<OProperty<?>> props) {
    return super.properties(this, props);
  }

  @Override
  public OCreateRequest<T> link(String navProperty, OEntity target) {
    return super.link(this, navProperty, target);
  }

  @Override
  public OCreateRequest<T> link(String navProperty, OEntityKey targetKey) {
    return super.link(this, navProperty, targetKey);
  }

  @Override
  public OCreateRequest<T> addToRelation(OEntity parent, String navProperty) {
    if (parent == null || navProperty == null) {
      throw new IllegalArgumentException("please provide the parent and the navProperty");
    }

    this.parent = parent;
    this.navProperty = navProperty;
    return this;
  }

  @Override
  public OCreateRequest<T> inline(String navProperty, OEntity... entities) {
    return super.inline(this, navProperty, entities);
  }

  @Override
  public OCreateRequest<T> inline(String navProperty, Iterable<OEntity> entities) {
    return super.inline(this, navProperty, Enumerable.create(entities).toArray(OEntity.class));
  }

}
