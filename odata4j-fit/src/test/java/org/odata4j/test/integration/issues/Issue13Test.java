package org.odata4j.test.integration.issues;

import junit.framework.Assert;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.core4j.Funcs;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OEntityKey;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.test.integration.AbstractRuntimeTest;

public class Issue13Test extends AbstractRuntimeTest {

  public Issue13Test(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void issue13() throws Exception {

    String endpointUri = "http://localhost:8813/Issue13.svc/";

    final OEntityKey[] lastEntityKey = new OEntityKey[1];
    InMemoryProducer producer = new InMemoryProducer("Issue13") {
      @Override
      public EntityResponse getEntity(ODataContext context, String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo) {
        lastEntityKey[0] = entityKey;
        return super.getEntity(context, entitySetName, entityKey, queryInfo);
      }
    };
    producer.register(Long.class, Long.class, "Entity", new Func<Iterable<Long>>() {
      public Iterable<Long> apply() {
        return Enumerable.create(1L, 2L, 3L);
      }
    }, Funcs.identity(Long.class));

    DefaultODataProducerProvider.setInstance(producer);
    ODataServer server = this.rtFacade.startODataServer(endpointUri);

    ODataConsumer c = this.rtFacade.createODataConsumer(endpointUri, null);

    lastEntityKey[0] = null;
    Assert.assertNotNull(c.getEntity("Entity", 2L).execute());
    Assert.assertNotNull(lastEntityKey[0]);
    Assert.assertEquals(OEntityKey.create(2L), lastEntityKey[0]);

    server.stop();

  }

}
