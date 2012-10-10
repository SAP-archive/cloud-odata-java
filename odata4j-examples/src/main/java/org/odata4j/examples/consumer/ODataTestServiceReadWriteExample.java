package org.odata4j.examples.consumer;

import org.joda.time.LocalDateTime;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;
import org.odata4j.core.OFuncs;
import org.odata4j.core.OProperties;
import org.odata4j.examples.AbstractExample;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.exceptions.ODataProducerException;

public class ODataTestServiceReadWriteExample extends AbstractExample {

  public static void main(String[] args) {
    ODataTestServiceReadWriteExample example = new ODataTestServiceReadWriteExample();
    example.run(args);
  }

  private void run(String[] args) {
    ODataConsumer c = ODataConsumers.create(ODataEndpoints.ODATA_TEST_SERVICE_READWRITE2);

    // take a look at the service edm
    reportMetadata(c.getMetadata());

    // retrieve a product entity with a known id
    OEntity havinaCola = c.getEntity("Products", 3).execute();
    reportEntity("Havina Cola", havinaCola);

    // list all products
    for (OEntity product : c.getEntities("Products").execute()) {
      reportEntity("Product: " + product.getProperty("Name").getValue(), product);
    }

    // query for the dvd player product by description
    OEntity dvdPlayer = c.getEntities("Products").filter("Description eq '1080P Upconversion DVD Player'").top(1).execute().first();
    reportEntity("DVD Player", dvdPlayer);

    // we are about to add a new product, first make sure it does not exist
    try {
      c.deleteEntity("Products", 10).execute();
    } catch (NotFoundException e) {}

    // create the new product
    OEntity newProduct = c.createEntity("Products")
        .properties(OProperties.int32("ID", 10))
        .properties(OProperties.string("Name", "Josta"))
        .properties(OProperties.string("Description", "With guaraná"))
        .properties(OProperties.datetime("ReleaseDate", new LocalDateTime()))
        .properties(OProperties.int32("Rating", 1))
        .properties(OProperties.decimal("Price", 1.23))
        .execute();

    report("newProduct: " + newProduct);

    // update the newly created product
    c.updateEntity(newProduct)
        .properties(OProperties.int32("Rating", 5))
        .execute();

    report("newProduct rating after update: " + c.getEntity("Products", 10).execute().getProperty("Rating").getValue());

    // update the newly create product using merge
    c.mergeEntity("Products", 10)
        .properties(OProperties.int32("Rating", 500))
        .execute();

    report("newProduct rating after merge: " + c.getEntity("Products", 10).execute().getProperty("Rating").getValue());

    // now that we've inflated the rating on our new product, query for the highest-rated product
    report("highest rated product (compute on server): " + c.getEntities("Products").orderBy("Rating desc").top(1).execute().first());
    report("highest rated product (compute on client): " + c.getEntities("Products").execute().orderBy(OFuncs.entityPropertyValue("Rating", Integer.class)).last());

    // clean up, delete the new product
    c.deleteEntity("Products", 10).execute();
    boolean exists = true;
    try {
      c.getEntity("Products", 10).execute();
    } catch (ODataProducerException e) {
      if (e.getHttpStatus().getStatusCode() == 404) exists = false;
    }
    report("newProduct " + (exists ? "exists" : "does not exist"));
  }

}
