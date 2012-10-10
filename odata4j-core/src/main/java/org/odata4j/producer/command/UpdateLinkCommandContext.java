package org.odata4j.producer.command;

import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;

public interface UpdateLinkCommandContext extends ProducerCommandContext<Void> {

  OEntityId getSourceEntity();

  String getTargetNavProp();

  OEntityKey getOldTargetEntityKey();

  OEntityId getNewTargetEntity();

}
