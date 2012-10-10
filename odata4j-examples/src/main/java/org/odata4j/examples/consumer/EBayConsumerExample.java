package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;
import org.odata4j.core.ORelatedEntityLink;
import org.odata4j.examples.AbstractExample;

public class EBayConsumerExample extends AbstractExample {

  public static void main(String[] args) {
    EBayConsumerExample example = new EBayConsumerExample();
    example.run(args);
  }

  private void run(String[] args) {

    ODataConsumer c = ODataConsumers.create(ODataEndpoints.EBAY);

    OEntity firstDeal = c.getEntities("Deals").top(1).execute().first();
    reportEntity(firstDeal.getProperty("Title").getValue().toString(),
        c.getEntity(firstDeal.getLink("Item", ORelatedEntityLink.class))
            .execute());
  }

}
