package org.odata4j.consumer;

import org.core4j.Enumerable;
import org.odata4j.core.OEntityId;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.exceptions.ODataProducerException;

/**
 * Create-link-request implementation.
 */
public class ConsumerCreateLinkRequest extends AbstractConsumerEntityRequest<Void> {

  private final String targetNavProp;
  private final OEntityId targetEntity;

  public ConsumerCreateLinkRequest(ODataClient client, String serviceRootUri,
      EdmDataServices metadata, OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
    super(client, serviceRootUri, metadata, sourceEntity.getEntitySetName(), sourceEntity.getEntityKey());
    this.targetNavProp = targetNavProp;
    this.targetEntity = targetEntity;
  }

  @Override
  public Void execute() throws ODataProducerException {
    String path = Enumerable.create(getSegments()).join("/");
    path = ConsumerQueryLinksRequest.linksPath(targetNavProp, null).apply(path);

    ODataClientRequest request = ODataClientRequest.post(getServiceRootUri() + path, toSingleLink(targetEntity));
    getClient().createLink(request);
    return null;
  }

}
