package org.odata4j.producer.jpa;

public interface Command {
  public boolean execute(JPAContext context);
}