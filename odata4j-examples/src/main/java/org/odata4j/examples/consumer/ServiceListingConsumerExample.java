package org.odata4j.examples.consumer;

import org.core4j.Enumerable;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.EntitySetInfo;
import org.odata4j.examples.AbstractExample;

public class ServiceListingConsumerExample extends AbstractExample {

  public static void main(String[] args) {
    ServiceListingConsumerExample example = new ServiceListingConsumerExample();
    example.run(args);
  }

  private void run(String[] args) {

    ODataConsumer.dump.requestHeaders(true);
    //ODataConsumer.dump.responseBody(true);
    //ODataConsumer.dump.responseHeaders(true);

    Enumerable<String> smallServices = Enumerable.create(
        ODataEndpoints.NORTHWIND,
        ODataEndpoints.ODATA4JSAMPLE_APPSPOT,
        ODataEndpoints.ODATA_WEBSITE_DATA,
        ODataEndpoints.ODATA_TEST_SERVICE_READONLY,
        ODataEndpoints.NERD_DINNER,
        ODataEndpoints.TECH_ED,
        ODataEndpoints.EU_TECH_ED,
        ODataEndpoints.TELERIK_TV,
        ODataEndpoints.PROAGORA_FR,
        ODataEndpoints.PROAGORA_EN
        );

    //Enumerable<String> brokenServices = Enumerable.create(
    //    ODataEndpoints.CITY_OF_EDMONTON,
    //    ODataEndpoints.DEVEXPRESS, // skiptoken not implemented
    //    ODataEndpoints.DEVTRANSIT, // returning 500 for access problems
    //    ODataEndpoints.LOGMYTIME, // returning proper 4xx, we should handle better in listing
    //    ODataEndpoints.PDC_2010, // four of the entity types return 404
    //    ODataEndpoints.MIX10 // down
    //    ODataEndpoints.AGILITRAIN,  // bad cert, redirect - down?
    //    ODataEndpoints.PLURALSIGHT, // bad datetime, see http://code.google.com/p/odata4j/issues/detail?id=198
    //    ODataEndpoints.INETA_LIVE,  // internal server error
    //    );

    Enumerable<String> largeServices = Enumerable.create(
        ODataEndpoints.BASEBALL_STATS,
        ODataEndpoints.NETFLIX,
        // ODataEndpoints.STACK_OVERFLOW, // Votes entity-sets return 500
        // ODataEndpoints.SUPER_USER,
        // ODataEndpoints.SERVER_FAULT,
        // ODataEndpoints.META_STACK_OVERFLOW,
        ODataEndpoints.WORLD_CUP,
        ODataEndpoints.NUGET
        );

    // stack overflow feeds 500 unless requests are rate-limited
    //behaviors = new OClientBehavior[] { OClientBehaviors.rateLimit(1000) };
    //printOutFirstEntities(Enumerable.create(ODataEndpoints.STACK_OVERFLOW));

    // print out each entity in every entity-set exposed by small services
    printOutAllEntities(smallServices);

    // print out the first record in each entity set exposed by large services
    printOutFirstEntities(largeServices);

  }

  private void printOutFirstEntities(Iterable<String> services) {
    for (String endpoint : services) {
      ODataConsumer c = ODataConsumers.create(endpoint);
      for (EntitySetInfo entitySet : c.getEntitySets())
        reportEntities(entitySet.getHref(), c.getEntities(entitySet.getHref()).top(1).execute());
    }
  }

  private void printOutAllEntities(Iterable<String> services) {
    for (String endpoint : services) {
      ODataConsumer c = ODataConsumers.create(endpoint);
      for (EntitySetInfo entitySet : c.getEntitySets())
        reportEntities(entitySet.getTitle(), c.getEntities(entitySet.getHref()).execute());
    }
  }

}
