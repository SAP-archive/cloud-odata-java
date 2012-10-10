package org.odata4j.test.integration.producer.jpa.northwind;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntityIds;
import org.odata4j.exceptions.BadRequestException;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.exceptions.NotImplementedException;

public class ExceptionTest extends NorthwindJpaProducerTest {

  protected ODataConsumer consumer;

  public ExceptionTest(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUp() {
    super.setUp(20);
    consumer = this.rtFacade.createODataConsumer(endpointUri, null);
  }

  @Test
  public void noEntityType() throws Exception {
    try {
      consumer.getEntities("UnknownEntity").execute();
      fail("Expected exception missing");
    } catch (NotFoundException e) {
      assertThat(e.getOError().getMessage(), containsString("UnknownEntity"));
      assertThat(e.getMessage(), containsString("UnknownEntity"));
    }
  }

  @Test
  public void noEntity() throws Exception {
    try {
      consumer.getEntity("Customers", "NOUSER").execute();
      fail("Expected exception missing");
    } catch (NotFoundException e) {
      assertThat(e.getOError().getMessage(), containsString("NOUSER"));
      assertThat(e.getMessage(), containsString("NOUSER"));
    }
  }

  @Test(expected = BadRequestException.class)
  public void invalidKeyInteger() throws Exception {
    consumer.getEntity("Customers", 1).execute();
  }

  @Test(expected = BadRequestException.class)
  public void invalidKeyString() throws Exception {
    consumer.getEntity("Employees", "WrongKey").execute();
  }

  @Test(expected = NotFoundException.class)
  public void noNavigation() throws Exception {
    consumer.getEntity("Customers", "QUEEN").nav("NoNavigation").execute();
  }

  @Test(expected = NotFoundException.class)
  public void noLinks() throws Exception {
    consumer.getLinks(OEntityIds.create("Customers", "QUEEN"), "NoNavigation").execute();
  }

  @Test
  public void deleteNotExistingEntity() throws Exception {
    try {
      consumer.deleteEntity("Customers", "NOUSER").execute();
      fail("Expected exception missing");
    } catch (NotFoundException e) {
      assertThat(e.getOError().getMessage(), containsString("NOUSER"));
      assertThat(e.getMessage(), containsString("NOUSER"));
    }
  }

  @Test(expected = NotImplementedException.class)
  public void deleteLink() throws Exception {
    consumer.deleteLink(OEntityIds.create("Customers", "CENTC"), "Orders", 10259).execute();
  }

  @Test
  public void noFunction() throws Exception {
    try {
      consumer.callFunction("NoFunction").execute();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), containsString("NoFunction"));
    }
  }
}
