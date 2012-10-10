package org.odata4j.consumer;

import org.core4j.Enumerable;
import org.odata4j.core.ODataConstants;
import org.odata4j.core.OEntityGetRequest;
import org.odata4j.core.OEntityKey;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.exceptions.ODataProducerException;
import org.odata4j.format.Entry;
import org.odata4j.format.Feed;
import org.odata4j.format.FormatParser;
import org.odata4j.format.FormatParserFactory;
import org.odata4j.format.Settings;
import org.odata4j.internal.EntitySegment;
import org.odata4j.internal.FeedCustomizationMapping;
import org.odata4j.internal.InternalUtil;

/**
 * Get-entity-request implementation.
 */
public class ConsumerGetEntityRequest<T> extends AbstractConsumerEntityRequest<T> implements OEntityGetRequest<T> {

  private final Class<T> entityType;
  private final FeedCustomizationMapping fcMapping;

  private String select;
  private String expand;

  public ConsumerGetEntityRequest(ODataClient client, Class<T> entityType, String serviceRootUri,
      EdmDataServices metadata, String entitySetName, OEntityKey key, FeedCustomizationMapping fcMapping) {
    super(client, serviceRootUri, metadata, entitySetName, key);
    this.entityType = entityType;
    this.fcMapping = fcMapping;
  }

  @Override
  public ConsumerGetEntityRequest<T> select(String select) {
    this.select = select;
    return this;
  }

  @Override
  public ConsumerGetEntityRequest<T> expand(String expand) {
    this.expand = expand;
    return this;
  }

  @Override
  public T execute() throws ODataProducerException {

    String path = Enumerable.create(getSegments()).join("/");

    ODataClientRequest request = ODataClientRequest.get(getServiceRootUri() + path);

    if (select != null) {
      request = request.queryParam("$select", select);
    }

    if (expand != null) {
      request = request.queryParam("$expand", expand);
    }

    ODataClientResponse response = getClient().getEntity(request);
    if (response == null)
      return null;

    //  the first segment contains the entitySetName we start from
    EdmEntitySet entitySet = getMetadata().getEdmEntitySet(getSegments().get(0).segment);
    for (EntitySegment segment : getSegments().subList(1, getSegments().size())) {
      EdmNavigationProperty navProperty = entitySet.getType().findNavigationProperty(segment.segment);
      entitySet = getMetadata().getEdmEntitySet(navProperty.getToRole().getType());
    }

    OEntityKey key = Enumerable.create(getSegments()).last().key;

    // TODO determine the service version from header (and metadata?)
    FormatParser<Feed> parser = FormatParserFactory
        .getParser(Feed.class, getClient().getFormatType(),
            new Settings(ODataConstants.DATA_SERVICE_VERSION, getMetadata(), entitySet.getName(), key, fcMapping));

    Entry entry = Enumerable.create(parser.parse(getClient().getFeedReader(response)).getEntries())
        .firstOrNull();
    response.close();

    return (T) InternalUtil.toEntity(entityType, entry.getEntity());
  }

}
