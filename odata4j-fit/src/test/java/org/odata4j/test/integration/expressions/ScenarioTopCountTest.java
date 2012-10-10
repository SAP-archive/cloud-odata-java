package org.odata4j.test.integration.expressions;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.core4j.Funcs;
import org.junit.Test;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.test.integration.AbstractRuntimeTest;

public class ScenarioTopCountTest extends AbstractRuntimeTest {

  public ScenarioTopCountTest(RuntimeFacadeType type) {
    super(type);
  }

  @Test
  public void testScenario() throws Exception {
    String uri = "http://localhost:18888/TestService.svc/";
    InMemoryProducer producer = new InMemoryProducer("ScenarioTest");
    DefaultODataProducerProvider.setInstance(producer);
    ODataServer server = this.rtFacade.startODataServer(uri);
    try {
      ODataConsumer c = this.rtFacade.createODataConsumer(uri, null);

      List<Foo> foos = new ArrayList<Foo>();
      producer.register(Foo.class, "Foos1", Funcs.constant((Iterable<Foo>) foos), "Id");
      foos.add(new Foo("1", 3, 3, "Alpha", true));
      foos.add(new Foo("2", 2, 2, null, false));
      foos.add(new Foo("3", 1, 1, "Gamma", true));

      int count;

      count = c.getEntitiesCount("Foos1").execute();
      assertEquals(3, count);
      count = c.getEntitiesCount("Foos1").top(1).execute();
      assertEquals(1, count);
      count = c.getEntitiesCount("Foos1").top(2).execute();
      assertEquals(2, count);

      server.stop();
    } finally {
      server.stop();
    }
  }

  public static class Foo {

    private final String id;
    private final int int32;
    private final int int64;
    private final String name;
    private final boolean bool;

    public Foo(String id, int int32, int int64, String name, boolean bool) {
      this.id = id;
      this.int32 = int32;
      this.int64 = int64;
      this.name = name;
      this.bool = bool;
    }

    public String getId() {
      return id;
    }

    public int getInt32() {
      return int32;
    }

    public int getInt64() {
      return int64;
    }

    public String getName() {
      return name;
    }

    public boolean getBoolean() {
      return bool;
    }
  }
}
