package org.odata4j.producer.command;

import org.odata4j.core.OEntity;

public interface MergeEntityCommandContext extends ProducerCommandContext<Void> {

  String getEntitySetName();

  OEntity getEntity();

}
