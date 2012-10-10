package org.odata4j.consumer;

import org.core4j.Enumerable;
import org.odata4j.core.OEntityId;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.exceptions.ODataProducerException;

/**
 * Delete-link-request implementation.
 */
public class ConsumerDeleteLinkRequest extends AbstractConsumerEntityRequest<Void> {

  private final String targetNavProp;
  private final Object[] targetKeyValues;

  public ConsumerDeleteLinkRequest(ODataClient client, String serviceRootUri,
      EdmDataServices metadata, OEntityId sourceEntity, String targetNavProp, Object... targetKeyValues) {
    super(client, serviceRootUri, metadata, sourceEntity.getEntitySetName(), sourceEntity.getEntityKey());
    this.targetNavProp = targetNavProp;
    this.targetKeyValues = targetKeyValues;
  }

  @Override
  public Void execute() throws ODataProducerException {
    String path = Enumerable.create(getSegments()).join("/");
    path = ConsumerQueryLinksRequest.linksPath(targetNavProp, targetKeyValues).apply(path);
    ODataClientRequest request = ODataClientRequest.delete(getServiceRootUri() + path);
    getClient().deleteLink(request);
    return null;
  }

}
