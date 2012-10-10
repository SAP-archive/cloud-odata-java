package org.odata4j.producer.jdbc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.odata4j.producer.command.ProducerCommandContext;

public class JdbcProducerBackendInvocationHandler implements InvocationHandler {

  private final JdbcProducerBackend backend;
  private final Class<?> contextType;
  private final Map<String, Object> fields;

  private Object result;

  public JdbcProducerBackendInvocationHandler(JdbcProducerBackend backend, Class<?> contextType, Object[] args) {
    this.backend = backend;
    this.contextType = contextType;

    if (args != null && args.length > 0) {
      fields = new HashMap<String, Object>();
      for (int i = 0; i < args.length; i += 2) {
        fields.put((String) args[i], args[i + 1]);
      }
    } else {
      fields = null;
    }
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    Method getResult = ProducerCommandContext.class.getMethod("getResult");
    if (method.equals(getResult)) {
      return result;
    }
    Method setResult = ProducerCommandContext.class.getMethod("setResult", Object.class);
    if (method.equals(setResult)) {
      result = args[0];
      return null;
    }
    Method toString = Object.class.getMethod("toString");
    if (method.equals(toString)) {
      return "JdbcProducer proxy for " + contextType.getSimpleName();
    }

    if (method.getDeclaringClass().equals(JdbcProducerCommandContext.class)) {
      return method.invoke(backend.newJdbcCommandContext(), args);
    }

    if (method.getName().startsWith("get")) {
      String fieldName = method.getName().substring("get".length());
      fieldName = Character.toLowerCase(fieldName.charAt(0)) + fieldName.substring(1);
      return fields.get(fieldName);
    }

    throw new UnsupportedOperationException("TODO implement: " + method.toString());
  }

}