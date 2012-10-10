package org.odata4j.producer.command;

import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.producer.EntityResponse;

public interface CreateEntityAtPropertyCommandContext extends ProducerCommandContext<EntityResponse> {

  String getEntitySetName();

  OEntityKey getEntityKey();

  String getNavProp();

  OEntity getEntity();

}
