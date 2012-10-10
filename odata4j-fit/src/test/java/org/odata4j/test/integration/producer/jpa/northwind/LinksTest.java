package org.odata4j.test.integration.producer.jpa.northwind;

import java.io.StringReader;
import java.util.Set;

import junit.framework.Assert;

import org.core4j.Func1;
import org.junit.Before;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityIds;
import org.odata4j.format.SingleLink;
import org.odata4j.format.json.JsonSingleLinkFormatParser;
import org.odata4j.producer.ODataProducer;
import org.odata4j.test.integration.producer.jpa.northwind.InterceptLinkModificationCalls.LinksMethod;

public class LinksTest extends NorthwindJpaProducerTest {

  public LinksTest(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUpClass() throws Exception {
    super.setUp(20, new Func1<ODataProducer, ODataProducer>() {
      public ODataProducer apply(final ODataProducer jpa) {
        interceptor = new InterceptLinkModificationCalls(jpa);
        return interceptor;
      }
    });
  }

  private static InterceptLinkModificationCalls interceptor;

  @Test
  public void testJsonSingleLinkFormatParser() {
    String uri = "http://host/service.svc/Orders(1)";
    SingleLink link = new JsonSingleLinkFormatParser(null).parse(new StringReader("{\"uri\": \"" + uri + "\"}"));
    Assert.assertEquals(uri, link.getUri());
  }

  @Test
  public void testLinks() throws Exception {
    ODataConsumer.dump.all(true);
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);
    OEntity product1 = consumer.getEntity("Products", 1).execute();

    // get
    OEntityId category = consumer.getLinks(product1, "Category").execute().first();
    Assert.assertEquals(1, category.getEntityKey().asSingleValue());

    Set<OEntityId> orders = consumer.getLinks(product1, "Order_Details").execute().toSet();
    Assert.assertEquals(20, orders.size());

    // delete
    Assert.assertNull(interceptor.lastCall);
    OEntityId order10285_1 = OEntityIds.create("Order_Details", "OrderID", 10285, "ProductID", 1);
    consumer.deleteLink(product1, "Order_Details", order10285_1).execute();
    Assert.assertEquals(LinksMethod.DELETE, interceptor.lastCall.methodCalled);
    Assert.assertTrue(OEntityIds.equals(product1, interceptor.lastCall.sourceEntity));
    Assert.assertEquals("Order_Details", interceptor.lastCall.targetNavProp);
    Assert.assertEquals(order10285_1.getEntityKey(), interceptor.lastCall.oldTargetEntityKey);
    Assert.assertNull(interceptor.lastCall.newTargetEntity);

    // create
    consumer.createLink(product1, "Order_Details", order10285_1).execute();
    Assert.assertEquals(LinksMethod.CREATE, interceptor.lastCall.methodCalled);
    Assert.assertTrue(OEntityIds.equals(product1, interceptor.lastCall.sourceEntity));
    Assert.assertEquals("Order_Details", interceptor.lastCall.targetNavProp);
    Assert.assertTrue(OEntityIds.equals(order10285_1, interceptor.lastCall.newTargetEntity));
    Assert.assertNull(interceptor.lastCall.oldTargetEntityKey);

    // update
    OEntityId order10286_1 = OEntityIds.create("Order_Details", "OrderID", 10286, "ProductID", 1);
    consumer.updateLink(product1, order10286_1, "Order_Details", order10285_1).execute();
    Assert.assertEquals(LinksMethod.UPDATE, interceptor.lastCall.methodCalled);
    Assert.assertTrue(OEntityIds.equals(product1, interceptor.lastCall.sourceEntity));
    Assert.assertEquals("Order_Details", interceptor.lastCall.targetNavProp);
    Assert.assertTrue(OEntityIds.equals(order10286_1, interceptor.lastCall.newTargetEntity));
    Assert.assertEquals(order10285_1.getEntityKey(), interceptor.lastCall.oldTargetEntityKey);

  }

  //  public static void main(String[] args) {
  //    ODataConsumer.dump.all(true);
  //    ODataConsumer consumer = this.create(ODataEndpoints.ODATA_TEST_SERVICE_READWRITE1,null,);
  //
  //    OEntityId product0 = OEntityIds.create("Products", 0);
  //    //OEntityId category0 = OEntityIds.create("Category", 0);
  //    OEntityId category1 = OEntityIds.create("Category", 1);
  //    consumer.updateLink(product0, category1, "Category").execute();
  //
  //    // consumer.updateLink(supplier1, products2, "Products", products0).execute();
  //
  //    //OEntityId supplier1 = OEntityIds.create("Suppliers", 1);
  //
  //    //    consumer.deleteLink(supplier1, "Products", 0).execute();
  //    // OEntityId products0 = OEntityIds.create("Products", 0);
  //
  //    //    consumer.createLink(supplier1, "Products", products0).execute();
  //    //    
  //    //    for (OEntityId product : consumer.getLinks(supplier1, "Products").execute()) {
  //    //      System.out.println(product);
  //    //    }
  //  }

}
