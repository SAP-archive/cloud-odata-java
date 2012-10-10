package org.odata4j.test.integration.producer.jpa.northwind;

import java.util.Set;

import junit.framework.Assert;

import org.core4j.Enumerable;
import org.junit.Before;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OFuncs;
import org.odata4j.core.OLink;

public class ConsumerLinksTest extends NorthwindJpaProducerTest {

  public ConsumerLinksTest(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUpClass() throws Exception {
    super.setUp(20);
  }

  @Test
  public void linksReturnedToClient() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);

    OEntity order = consumer.getEntity("Orders", 10248).execute();
    Assert.assertEquals(3, order.getLinks().size());
    Set<String> linkTitles = Enumerable.create(order.getLinks()).select(OFuncs.title(OLink.class)).toSet();
    Assert.assertEquals(Enumerable.create("Customer", "Employee", "OrderDetails").toSet(), linkTitles);
  }

}
