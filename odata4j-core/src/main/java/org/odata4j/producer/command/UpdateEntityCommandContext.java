package org.odata4j.producer.command;

import org.odata4j.core.OEntity;

public interface UpdateEntityCommandContext extends ProducerCommandContext<Void> {

  String getEntitySetName();

  OEntity getEntity();

}
