package org.odata4j.producer.jpa;

public interface Filter extends Command {
  public boolean postProcess(JPAContext context, Exception exception);
}