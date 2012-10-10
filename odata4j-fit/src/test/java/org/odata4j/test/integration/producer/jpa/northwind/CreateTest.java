package org.odata4j.test.integration.producer.jpa.northwind;

import org.core4j.Enumerable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OPredicates;
import org.odata4j.core.OProperties;
import org.odata4j.format.FormatType;

public class CreateTest extends NorthwindJpaProducerTest {

  public CreateTest(RuntimeFacadeType type) {
    super(type);
  }

  @Before
  public void setUpClass() throws Exception {
    super.setUp(20);
  }

  @Test
  public void tunneledInsertEntityToExistingEntityRelationAtom() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null, OClientBehaviors.methodTunneling("PUT"));
    insertEntityToExistingEntityRelationAndTest(consumer);
  }

  @Test
  public void tunneledInsertEntityToExistingEntityRelationJson() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, FormatType.JSON, OClientBehaviors.methodTunneling("PUT"));
    insertEntityToExistingEntityRelationAndTest(consumer);
  }

  @Test
  public void insertEntityToExistingEntityRelationAtom() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);
    insertEntityToExistingEntityRelationAndTest(consumer);
  }

  @Test
  public void insertEntityToExistingEntityRelationJson() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, FormatType.JSON);
    insertEntityToExistingEntityRelationAndTest(consumer);
  }

  @Test
  public void tunneledInsertEntityUsingLinksAtom() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null, OClientBehaviors.methodTunneling("PUT"));
    insertEntityUsingLinksAndTest(consumer);
  }

  @Test
  public void tunneledInsertEntityUsingLinksJson() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, FormatType.JSON, OClientBehaviors.methodTunneling("PUT"));
    insertEntityUsingLinksAndTest(consumer);
  }

  @Test
  public void insertEntityUsingLinksAtom() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);
    insertEntityUsingLinksAndTest(consumer);
  }

  @Test
  public void insertEntityUsingLinksJson() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, FormatType.JSON);
    insertEntityUsingLinksAndTest(consumer);
  }

  @Test
  public void insertEntityWithInlinedEntitiesAtom() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);
    insertEntityWithInlinedEntities(consumer);
  }

  @Test
  public void insertEntityWithInlinedEntitiesJson() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, FormatType.JSON);
    insertEntityWithInlinedEntities(consumer);
  }

  protected void insertEntityWithInlinedEntities(ODataConsumer consumer) throws Exception {

    final long now = System.currentTimeMillis();
    OEntity prod1 = consumer
        .createEntity("Products")
        .properties(OProperties.string("ProductName", "P1" + now))
        .properties(OProperties.boolean_("Discontinued", true))
        .get();
    OEntity prod2 = consumer
        .createEntity("Products")
        .properties(OProperties.string("ProductName", "P2" + now))
        .properties(OProperties.boolean_("Discontinued", false))
        .get();

    OEntity category = consumer
        .createEntity("Categories")
        .properties(OProperties.string("CategoryName", "C" + now))
        .inline("Products", prod1, prod2)
        .execute();

    Assert.assertNotNull(category);
    Assert.assertNotNull(category.getEntityKey());
    Assert.assertNotNull(category.getProperty("CategoryID").getValue());
    Assert.assertEquals("C" + now, category.getProperty("CategoryName").getValue());

    Enumerable<OEntity> products = consumer
        .getEntities("Categories")
        .nav(category.getEntityKey(), "Products")
        .execute();

    Assert.assertEquals(2, products.count());

    prod1 = products.where(OPredicates.entityPropertyValueEquals("ProductName", "P1" + now)).firstOrNull();
    Assert.assertNotNull(prod1);
    Assert.assertNotNull(prod1.getProperty("ProductID").getValue());
    Assert.assertEquals(true, prod1.getProperty("Discontinued").getValue());

    prod2 = products.where(OPredicates.entityPropertyValueEquals("ProductName", "P2" + now)).firstOrNull();
    Assert.assertNotNull(prod2);
    Assert.assertNotNull(prod2.getProperty("ProductID").getValue());
    Assert.assertEquals(false, prod2.getProperty("Discontinued").getValue());
  }

  @Test
  public void insertEntityWithInlinedEntityAtom() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);
    insertEntityWithInlinedEntity(consumer);
  }

  @Test
  public void insertEntityWithInlinedEntityJson() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, FormatType.JSON);
    insertEntityWithInlinedEntity(consumer);
  }

  @Test
  public void expandEntitiesWithNullReferenceAtom() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);
    expandEntitiesWithNullReference(consumer);
  }

  @Test
  public void expandEntitiesWithNullReferenceJson() throws Exception {
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, FormatType.JSON);
    expandEntitiesWithNullReference(consumer);
  }

  public void insertEntityWithInlinedEntity(ODataConsumer consumer) throws Exception {
    final long now = System.currentTimeMillis();
    OEntity category = consumer
        .createEntity("Categories")
        .properties(OProperties.string("CategoryName", "C" + now))
        .get();

    OEntity product = consumer.createEntity("Products")
        .properties(OProperties.string("ProductName", "P" + now))
        .properties(OProperties.boolean_("Discontinued", true))
        .inline("Category", category)
        .execute();

    Object id = product.getProperty("ProductID").getValue();
    Assert.assertNotNull(id);
    Assert.assertEquals("P" + now, product.getProperty("ProductName").getValue());
    Assert.assertEquals(true, product.getProperty("Discontinued").getValue());
    Object categoryId = product.getProperty("CategoryID").getValue();
    Assert.assertNotNull(categoryId);

    category = consumer.getEntity("Categories", categoryId).execute();
    Assert.assertEquals("C" + now, category.getProperty("CategoryName").getValue());
  }

  protected void insertEntityToExistingEntityRelationAndTest(ODataConsumer consumer) throws Exception {
    OEntity category = consumer.getEntity("Categories", 1).execute();

    final long now = System.currentTimeMillis();
    Assert.assertNotNull(category);
    OEntity product = consumer
        .createEntity("Products")
        .properties(OProperties.string("ProductName", "P" + now))
        .properties(OProperties.boolean_("Discontinued", false))
        .addToRelation(category, "Products").execute();

    Object id = product.getProperty("ProductID").getValue();
    Assert.assertNotNull(id);

    product = consumer.getEntity("Products", id).execute();
    Assert.assertEquals(id, product.getProperty("ProductID").getValue());
    Assert.assertEquals("P" + now, product.getProperty("ProductName").getValue());
  }

  protected void insertEntityUsingLinksAndTest(ODataConsumer consumer) throws Exception {

    OEntity category = consumer.getEntity("Categories", 1).execute();

    Assert.assertNotNull(category);

    final long now = System.currentTimeMillis();
    OEntity product = consumer
        .createEntity("Products")
        .properties(OProperties.string("ProductName", "P" + now))
        .properties(OProperties.boolean_("Discontinued", true))
        .link("Category", category)
        .execute();

    Assert.assertNotNull(product);
    Assert.assertNotNull(product.getProperty("ProductID").getValue());
    Assert.assertEquals(1, product.getProperty("CategoryID").getValue());
    Assert.assertEquals("P" + now, product.getProperty("ProductName").getValue());
    Assert.assertEquals(true, product.getProperty("Discontinued").getValue());

    Object keyValue = product.getProperty("ProductID").getValue();
    product = consumer
        .getEntity("Products", keyValue)
        .execute();

    Assert.assertNotNull(product);
    Assert.assertEquals(keyValue, product.getProperty("ProductID").getValue());
    Assert.assertEquals(1, product.getProperty("CategoryID").getValue());
    Assert.assertEquals("P" + now, product.getProperty("ProductName").getValue());
    Assert.assertEquals(true, product.getProperty("Discontinued").getValue());
  }

  protected void expandEntitiesWithNullReference(ODataConsumer consumer) throws Exception {

    final long now = System.currentTimeMillis();

    //Add product with null Category
    consumer.createEntity("Products")
        .properties(OProperties.string("ProductName", "P" + now))
        .execute();

    //Get all products expanding on Category
    Enumerable<OEntity> products = consumer.getEntities("Products")
        .expand("Category").execute();

    Assert.assertNotNull(products);
  }

}
