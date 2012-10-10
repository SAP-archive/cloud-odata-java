package org.odata4j.producer.inmemory;

import java.util.HashMap;
import java.util.Map;

import org.odata4j.edm.EdmSimpleType;

public class InMemoryTypeMapping {

  private static final Map<Class<?>, EdmSimpleType<?>> ADDITIONAL_TYPES = new HashMap<Class<?>, EdmSimpleType<?>>();

  public static final InMemoryTypeMapping DEFAULT = new InMemoryTypeMapping();

  static {
    ADDITIONAL_TYPES.put(Object.class, EdmSimpleType.STRING);
  }

  public EdmSimpleType<?> findEdmType(Class<?> clazz) {
    EdmSimpleType<?> type = EdmSimpleType.forJavaType(clazz);
    if (type == null)
      type = ADDITIONAL_TYPES.get(clazz);

    return type;
  }

}
