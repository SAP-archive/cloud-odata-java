package org.odata4j.core;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A static factory to create immutable {@link NamedValue} instances.
 */
public class NamedValues {

  private NamedValues() {}

  /**
   * Create an named-value from a name and a value.
   *
   * @param <T>  the value's java-type
   * @param name  the name
   * @param value  the value
   * @return a new named-value instance
   */
  public static <T> NamedValue<T> create(String name, T value) {
    return new NamedValueImpl<T>(name, value);
  }

  /**
   * Create multiple named-values from a string->object map.
   *
   * @param values  names and values
   * @return a set of new named-value instances
   */
  public static Set<NamedValue<Object>> fromMap(Map<String, Object> values) {
    Set<NamedValue<Object>> rt = new HashSet<NamedValue<Object>>();
    for (Map.Entry<String, Object> entry : values.entrySet())
      rt.add(new NamedValueImpl<Object>(entry.getKey(), entry.getValue()));
    return rt;
  }

  /**
   * Create a copy of a named-value.
   *
   * @param <T>  the value's java-type
   * @param value  the named-value to copy
   * @return a new named-value instance with the same name and value
   */
  public static <T> NamedValue<T> copy(NamedValue<T> value) {
    return new NamedValueImpl<T>(value);
  }

  private static class NamedValueImpl<T> implements NamedValue<T> {

    private final String name;
    private final T value;

    public NamedValueImpl(String name, T value) {
      this.name = name;
      this.value = value;
    }

    public NamedValueImpl(NamedValue<T> namedValue) {
      this.name = namedValue.getName();
      this.value = namedValue.getValue();
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public T getValue() {
      return value;
    }

  }

}
