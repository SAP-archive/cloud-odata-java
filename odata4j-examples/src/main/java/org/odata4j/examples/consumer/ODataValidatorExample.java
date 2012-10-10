package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.Guid;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.core.ORelatedEntitiesLink;
import org.odata4j.examples.AbstractExample;
import org.odata4j.internal.InternalUtil;

public class ODataValidatorExample extends AbstractExample {

  public static void main(String[] args) {
    ODataValidatorExample example = new ODataValidatorExample();
    example.run(args);
  }

  private void run(String[] args) {

    String uri = ODataEndpoints.NORTHWIND;

    ODataConsumer c = ODataConsumers.create("http://services.odata.org/validation/odatavalidator/");

    Guid validationJobId = Guid.fromString("f4aa9495-ef40-469e-818c-29c4ec5fb2ed");
    if (true) {
      OEntity newValidationJob = c.createEntity("ValidationJobs").properties(OProperties.string("Uri", uri), OProperties.string("Format", "atompub")).execute();
      reportEntity("new job", newValidationJob);
      validationJobId = (Guid) newValidationJob.getEntityKey().asSingleValue();
    }

    boolean complete = false;
    OEntity validationJob = null;
    while (!complete) {
      if (validationJob != null)
        InternalUtil.sleep(500);
      validationJob = c.getEntity("ValidationJobs", validationJobId).execute();
      complete = validationJob.getProperty("Complete", Boolean.class).getValue();
    }

    reportEntity("job", validationJob);
    for (OEntity testResult : c.getEntities(validationJob.getLink("TestResults", ORelatedEntitiesLink.class)).execute()) {
      reportEntity("result", testResult);
    }

    report("PayloadLines:");
    for (OEntity payloadLine : c.getEntities(validationJob.getLink("PayloadLines", ORelatedEntitiesLink.class)).orderBy("LineNumber").execute())
      report(payloadLine.getProperty("LineText", String.class).getValue());
  }

}
