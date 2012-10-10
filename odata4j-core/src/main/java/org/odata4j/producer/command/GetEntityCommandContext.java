package org.odata4j.producer.command;

import org.odata4j.core.OEntityKey;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;

public interface GetEntityCommandContext extends ProducerCommandContext<EntityResponse> {

  String getEntitySetName();

  OEntityKey getEntityKey();

  EntityQueryInfo getQueryInfo();

}
