package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;
import org.odata4j.core.ORelatedEntityLink;
import org.odata4j.examples.AbstractExample;

public class AgilitrainConsumerExample extends AbstractExample {

  public static void main(String[] args) {
    AgilitrainConsumerExample example = new AgilitrainConsumerExample();
    example.run(args);
  }

  private void run(String[] args) {
    ODataConsumer c = ODataConsumers.create(ODataEndpoints.AGILITRAIN);

    OEntity event = c.getEntities("Events").top(1).execute().first();
    ORelatedEntityLink link = event.getLink("Workshop", ORelatedEntityLink.class);
    OEntity entity = c.getEntity(link).execute();
    reportEntity("Workshop", entity);
  }
}
