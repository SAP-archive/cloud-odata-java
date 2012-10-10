package org.odata4j.test.integration.producer.jpa.northwind;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.core.ORelatedEntityLink;

public class CreateWithLink2Test extends NorthwindJpaProducerTest {

  public CreateWithLink2Test(RuntimeFacadeType type) {
    super(type);
  }

  protected static final String endpointUri = "http://localhost:8810/northwind/Northwind.svc/";

  @Before
  public void setUp() {
    super.setUp(20);
  }

  @Test
  public void passEntityRefFromFilter() throws Exception {
    final long now = System.currentTimeMillis();
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);

    OEntity customer = consumer
        .createEntity("Customers")
        .properties(OProperties.string("CustomerID", "ID" + now))
        .properties(OProperties.string("CompanyName", "Company" + now))
        .execute();
    Assert.assertNotNull(customer);

    String filterQuery = "CompanyName eq 'Company" + now + "'";
    OEntity customerRet = consumer.getEntities("Customers").top(1).filter(filterQuery).execute().first();

    Assert.assertNotNull(customerRet);

    OEntity order = consumer
        .createEntity("Orders")
        .properties(OProperties.string("ShipName", "Ship" + now))
        .link("Customer", customerRet)
        .execute();

    Assert.assertNotNull(order);

    ORelatedEntityLink link = order.getLink("Customer", ORelatedEntityLink.class);
    OEntity customerValid = consumer.getEntity(link).execute();

    Assert.assertNotNull(customerValid);
    Assert.assertEquals("Company" + now, customerValid.getProperty("CompanyName").getValue());

  }

}
