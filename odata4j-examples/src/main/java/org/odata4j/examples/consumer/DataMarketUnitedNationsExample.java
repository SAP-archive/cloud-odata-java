package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.examples.AbstractExample;

public class DataMarketUnitedNationsExample extends AbstractExample {

  public static void main(String[] args) {
    DataMarketUnitedNationsExample example = new DataMarketUnitedNationsExample();
    example.run(args);
  }

  private void run(String[] args) {

    String[] datamarketCreds = args.length > 0 ? args : System.getenv("DATAMARKET").split(":");

    String url = "https://api.datamarket.azure.com/Data.ashx/UnitedNations/MDG/";

    ODataConsumer c = ODataConsumers.newBuilder(url)
        .setClientBehaviors(OClientBehaviors.basicAuth("accountKey", datamarketCreds[0]))
        .build();

    OEntity firstDataSeries = c.getEntities("DataSeries").top(1).execute().first();
    String filter = String.format("DataSeriesId eq '%s'", firstDataSeries.getProperty("Id").getValue());
    reportEntities(firstDataSeries.getProperty("Name", String.class).getValue(), c.getEntities("Values").filter(filter).top(10).execute());
  }

}
