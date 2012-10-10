package org.odata4j.producer.jdbc;

public interface JdbcProducerCommandContext {

  Jdbc getJdbc();

  JdbcProducerBackend getBackend();

  <T> T get(Class<T> instanceType);

}
