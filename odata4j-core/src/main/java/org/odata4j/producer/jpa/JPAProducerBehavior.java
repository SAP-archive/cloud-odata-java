package org.odata4j.producer.jpa;

import java.util.List;

import org.odata4j.producer.jpa.JPAProducer.CommandType;

public interface JPAProducerBehavior {
  public List<Command> modify(CommandType type, List<Command> commands);
}