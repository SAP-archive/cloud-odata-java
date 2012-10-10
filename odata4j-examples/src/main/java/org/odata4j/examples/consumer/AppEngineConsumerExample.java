package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.examples.AbstractExample;

public class AppEngineConsumerExample extends AbstractExample {

  public static void main(String[] args) {
    AppEngineConsumerExample example = new AppEngineConsumerExample();
    example.run(args);
  }

  private void run(String[] args) {
    ODataConsumer c = ODataConsumers.create(ODataEndpoints.ODATA4JSAMPLE_APPSPOT);
    String newCategoryName = "NewCategory" + System.currentTimeMillis();

    report("Create a new category");
    OEntity newCategory = c.createEntity("Categories")
        .properties(OProperties.string("categoryName", newCategoryName))
        .properties(OProperties.int32("AdditionalProperty", 500)) // appengine datastore entities are open types, add a new property
        .execute();
    reportEntity(newCategoryName, newCategory);
    reportEntities(c, "Categories", 100);

    report("Update the new category");
    c.updateEntity(newCategory)
        .properties(OProperties.string("description", "Updated"))
        .execute();
    reportEntities(c, "Categories", 100);

    report("Merge the new category");
    c.mergeEntity("Categories", newCategory.getProperty("id"))
        .properties(OProperties.string("description", "Merged"))
        .execute();
    reportEntities(c, "Categories", 100);

    report("Delete the new category");
    c.deleteEntity("Categories", newCategory.getEntityKey()).execute();
    reportEntities(c, "Categories", 100);

    reportEntity("Last category by category name (excluding seafood): ",
        c.getEntities("Categories")
            .filter("categoryName ne 'Seafood'")
            .orderBy("categoryName desc")
            .top(1)
            .execute().first());

    reportEntity("\nNon-discontinued product with reorderLevel > 25 (two filter predicates): ",
        c.getEntities("Products")
            .filter("reorderLevel gt 25 and discontinued eq false")
            .top(1)
            .execute().first());
  }

}
