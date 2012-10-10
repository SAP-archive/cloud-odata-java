package org.odata4j.consumer;

import org.core4j.Enumerable;
import org.odata4j.core.OEntityId;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.exceptions.ODataProducerException;

/**
 * Update-link-request implementation.
 */
public class ConsumerUpdateLinkRequest extends AbstractConsumerEntityRequest<Void> {

  private final String targetNavProp;
  private final Object[] oldTargetKeyValues;
  private final OEntityId newTargetEntity;

  public ConsumerUpdateLinkRequest(ODataClient client, String serviceRootUri,
      EdmDataServices metadata, OEntityId sourceEntity, OEntityId newTargetEntity, String targetNavProp, Object... oldTargetKeyValues) {
    super(client, serviceRootUri, metadata, sourceEntity.getEntitySetName(), sourceEntity.getEntityKey());
    this.targetNavProp = targetNavProp;
    this.oldTargetKeyValues = oldTargetKeyValues;
    this.newTargetEntity = newTargetEntity;
  }

  @Override
  public Void execute() throws ODataProducerException {
    String path = Enumerable.create(getSegments()).join("/");
    path = ConsumerQueryLinksRequest.linksPath(targetNavProp, oldTargetKeyValues).apply(path);

    ODataClientRequest request = ODataClientRequest.put(getServiceRootUri() + path, toSingleLink(newTargetEntity));
    getClient().updateLink(request);
    return null;
  }

}