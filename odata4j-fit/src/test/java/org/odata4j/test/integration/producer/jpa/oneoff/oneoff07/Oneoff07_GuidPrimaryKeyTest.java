package org.odata4j.test.integration.producer.jpa.oneoff.oneoff07;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.odata4j.test.integration.producer.jpa.oneoff.AbstractOneoffBaseTest;

public class Oneoff07_GuidPrimaryKeyTest extends AbstractOneoffBaseTest {

  public Oneoff07_GuidPrimaryKeyTest(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void guidPrimaryKey() throws Exception {
    ODataConsumer c = this.rtFacade.createODataConsumer(endpointUri, null);
    Assert.assertEquals(0, c.getEntities("CommunicationCellCarrier").execute().count());
    String id = UUID.randomUUID().toString();
    c.createEntity("CommunicationCellCarrier")
        .properties(
            OProperties.guid("id", id),
            OProperties.string("name", "TMobile"))
        .execute();
    Assert.assertEquals(1, c.getEntities("CommunicationCellCarrier").execute().count());
    OEntity firstEntity = c.getEntity("CommunicationCellCarrier", id).execute();
    Assert.assertEquals(id, firstEntity.getProperty("id").getValue().toString());
    Assert.assertEquals("TMobile", firstEntity.getProperty("name").getValue());
    c.deleteEntity(firstEntity).execute();
    Assert.assertEquals(0, c.getEntities("CommunicationCellCarrier").execute().count());
  }

}
