package org.odata4j.producer.command;

import org.odata4j.core.OEntityKey;

public interface DeleteEntityCommandContext extends ProducerCommandContext<Void> {

  String getEntitySetName();

  OEntityKey getEntityKey();

}
