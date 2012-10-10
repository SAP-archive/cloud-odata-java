package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.examples.AbstractExample;

public class DataMarketDataDotGovExample extends AbstractExample {

  public static void main(String[] args) {
    DataMarketDataDotGovExample example = new DataMarketDataDotGovExample();
    example.run(args);
  }

  private void run(String[] args) {
    String[] datamarketCreds = args.length > 0 ? args : System.getenv("DATAMARKET").split(":");

    // https://api.datamarket.azure.com/Data.ashx/data.gov/Crimes/CityCrime(115856)
    String url = "https://api.datamarket.azure.com/Data.ashx/data.gov/Crimes";
    ODataConsumer c = ODataConsumers.newBuilder(url)
        .setClientBehaviors(OClientBehaviors.basicAuth("accountKey", datamarketCreds[0]))
        .build();

    OEntity cityCrime115856 = c.getEntity("CityCrime", 115856).execute();
    reportEntity("cityCrime115856", cityCrime115856);
  }

}
