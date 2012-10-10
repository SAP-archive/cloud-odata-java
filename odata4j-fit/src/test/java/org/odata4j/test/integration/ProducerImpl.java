package org.odata4j.test.integration;

import org.odata4j.producer.inmemory.InMemoryProducer;
import org.odata4j.producer.jpa.JPAProducer;

public enum ProducerImpl {

  IN_MEMORY(InMemoryProducer.class.getSimpleName()),
  JPA(JPAProducer.class.getSimpleName());

  private final String className;

  private ProducerImpl(String className) {
    this.className = className;
  }

  @Override
  public String toString() {
    return className;
  }
}
