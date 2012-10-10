package org.odata4j.test.integration;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.core4j.Funcs;
import org.odata4j.producer.inmemory.InMemoryProducer;

public class TestInMemoryProducers {

  public static final String SIMPLE_PRODUCER = "Simple";
  public static final String SIMPLE_ENTITY_SET_NAME = "Alphabet";
  public static final String[] SIMPLE_ENTITIES = { "A", "B", "C" };

  public static InMemoryProducer simple() {
    InMemoryProducer producer = new InMemoryProducer(SIMPLE_PRODUCER);

    producer.register(String.class, String.class, SIMPLE_ENTITY_SET_NAME, new Func<Iterable<String>>() {
      public Iterable<String> apply() {
        return Enumerable.create(SIMPLE_ENTITIES);
      }
    }, Funcs.identity(String.class));

    return producer;
  }
}
