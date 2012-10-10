package org.odata4j.examples.consumer;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.core.OEntity;
import org.odata4j.core.OObject;
import org.odata4j.core.OProperties;
import org.odata4j.examples.AbstractExample;

public class LightSwitchExample extends AbstractExample {

  public static void main(String[] args) {

    String serviceUrl = "http://localhost:5076/ApplicationData.svc/";
    String entitySet = "Customers";
    String propertyName = "Name";

    // LightSwitch services are normal odata services, they don't support json, and require concurrency control
    ODataConsumer c = ODataConsumers.newBuilder(serviceUrl).build();

    reportMetadata(c.getMetadata());

    reportEntities(c, "Customers", 50);

    OEntity newCustomer = c.createEntity(entitySet).properties(OProperties.string(propertyName, "New name")).execute();
    reportEntity("created", newCustomer);

    c.mergeEntity(newCustomer).properties(OProperties.string(propertyName, "Merged Name")).execute();
    newCustomer = c.getEntity(entitySet, newCustomer.getEntityKey()).execute();
    reportEntity("merged and got", newCustomer);

    c.updateEntity(newCustomer).properties(OProperties.string(propertyName, "Updated Name")).execute();
    newCustomer = c.getEntity(entitySet, newCustomer.getEntityKey()).execute();
    reportEntity("updated and got", newCustomer);

    c.deleteEntity(newCustomer).execute();
    report("deleted");

    // ApplicationData.svc/Microsoft_LightSwitch_GetCanInformation?dataServiceMembers='Customers'
    OObject rt = c.callFunction("Microsoft_LightSwitch_GetCanInformation").pString("dataServiceMembers", entitySet).execute().iterator().next();
    report("GetCanInformation for %s: %s", entitySet, rt);
  }

}
