package org.odata4j.test.integration.producer.jpa.northwind;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;

public class UpdateTest extends NorthwindJpaProducerTest {

  public UpdateTest(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUpClass() throws Exception {
    super.setUp(20);
  }

  @Test
  public void tunneledUpdateEntity() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null, OClientBehaviors.methodTunneling("PUT"));
    updateEntityAndTest(consumer);
  }

  @Test
  public void updateEntity() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);

    updateEntityAndTest(consumer);
  }

  protected void updateEntityAndTest(ODataConsumer consumer) throws Exception {
    OEntity customer = consumer.getEntity("Customers", "ALFKI").execute();

    consumer.updateEntity(customer).properties(OProperties.string("ContactName", "Maria Gleich")).execute();

    customer = consumer.getEntity("Customers", "ALFKI").execute();
    Assert.assertEquals("Maria Gleich", customer.getProperty("ContactName").getValue());
    Assert.assertEquals("Alfreds Futterkiste", customer.getProperty("CompanyName").getValue());
  }

  @Test
  public void mergeEntityTest() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);

    final long now = System.currentTimeMillis();
    consumer.mergeEntity("Categories", 1).properties(OProperties.string("Description", "D" + now)).execute();

    OEntity category = consumer.getEntity("Categories", 1).execute();

    Assert.assertEquals("Beverages", category.getProperty("CategoryName").getValue());
    Assert.assertEquals("D" + now, category.getProperty("Description").getValue());
  }

}
