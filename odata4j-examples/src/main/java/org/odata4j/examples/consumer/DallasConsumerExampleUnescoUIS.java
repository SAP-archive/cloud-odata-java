package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OFuncs;
import org.odata4j.examples.AbstractExample;

public class DallasConsumerExampleUnescoUIS extends AbstractExample {

  public static void main(String[] args) {
    DallasConsumerExampleUnescoUIS example = new DallasConsumerExampleUnescoUIS();
    example.run(args);
  }

  private void run(String[] args) {

    String[] dallasCreds = args.length > 0 ? args : System.getenv("DALLAS").split(":");

    ODataConsumer c = ODataConsumers.newBuilder(ODataEndpoints.DALLAS_CTP2_UNESCO_UIS)
        .setClientBehaviors(OClientBehaviors.basicAuth("accountKey", dallasCreds[0]))
        .build();

    // Public expenditure on education as % of GDP [XGDP_FSGOV]
    for (OEntity entity : c.getEntities("UNESCO/XGDP_FSGOV").execute()
        .orderBy(OFuncs.entityPropertyValue("observationValue", Double.class)))
      // client-side ordering, server-side ordering not supported on dallas
      report("Public expenditure on education as pct of GDP: %s %s, %.4f",
          entity.getProperty("referenceArea").getValue(),
          entity.getProperty("timePeriod").getValue(),
          entity.getProperty("observationValue").getValue());

    // Number of national feature films produced [C_F_220006]
    for (OEntity entity : c.getEntities("UNESCO/C_F_220006").execute()
        .orderBy(OFuncs.entityPropertyValue("observationValue", Double.class)))
      report("Number of national feature films produced: %s %s, %.0f",
          entity.getProperty("referenceArea").getValue(),
          entity.getProperty("timePeriod").getValue(),
          entity.getProperty("observationValue").getValue());
  }

}
