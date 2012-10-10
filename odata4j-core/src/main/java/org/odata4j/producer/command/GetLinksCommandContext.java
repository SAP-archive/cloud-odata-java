package org.odata4j.producer.command;

import org.odata4j.core.OEntityId;
import org.odata4j.producer.EntityIdResponse;

public interface GetLinksCommandContext extends ProducerCommandContext<EntityIdResponse> {

  OEntityId getSourceEntity();

  String getTargetNavProp();

}
