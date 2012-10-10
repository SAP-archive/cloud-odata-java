package org.odata4j.test.integration.producer.jpa.oneoff.oneoff05;

import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OProperties;
import org.odata4j.test.integration.producer.jpa.oneoff.AbstractOneoffBaseTest;

public class Oneoff05_createEntityElementCollectionTest extends AbstractOneoffBaseTest {

  public Oneoff05_createEntityElementCollectionTest(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void createEntityElementCollection() throws Exception {
    final long now = System.currentTimeMillis();
    ODataConsumer consumer = this.rtFacade.createODataConsumer(endpointUri, null);

    consumer
        .createEntity("Student")
        .properties(OProperties.string("StudentName", "Student1" + now))
        //todo add courses as well to the student
        .execute();
  }
}
