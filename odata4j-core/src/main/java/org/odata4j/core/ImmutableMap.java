package org.odata4j.core;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * A Map implementation whose structure is immutable after construction.
 *
 * <p>Useful for apis that assume a returned map remains unmodified.</p>
 *
 * <p>All mutation methods throw <code>UnsupportedOperationException</code></p>
 *
 * @param <K>  the map key type
 * @param <V>  the map value type
 */
public class ImmutableMap<K, V> implements Map<K, V> {

  /** Mutable builder for {@link ImmutableMap} objects. */
  public static class Builder<K, V> {
    private final LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();

    public Builder<K, V> put(K key, V value) {
      map.put(key, value);
      return this;
    }

    public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
      this.map.putAll(map);
      return this;
    }

    public ImmutableMap<K, V> build() {
      return new ImmutableMap<K, V>(map);
    }
  }

  private final LinkedHashMap<K, V> map;

  private ImmutableMap(LinkedHashMap<K, V> map) {
    this.map = map;
  }

  public static <K, V> ImmutableMap<K, V> of() {
    return new Builder<K, V>()
        .build();
  }

  public static <K, V> ImmutableMap<K, V> of(K key1, V value1) {
    return new Builder<K, V>()
        .put(key1, value1)
        .build();
  }

  public static <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2) {
    return new Builder<K, V>()
        .put(key1, value1)
        .put(key2, value2)
        .build();
  }

  public static <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3) {
    return new Builder<K, V>()
        .put(key1, value1)
        .put(key2, value2)
        .put(key3, value3)
        .build();
  }

  public static <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
    return new Builder<K, V>()
        .put(key1, value1)
        .put(key2, value2)
        .put(key3, value3)
        .put(key4, value4)
        .build();
  }

  public static <K, V> ImmutableMap<K, V> of(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
    return new Builder<K, V>()
        .put(key1, value1)
        .put(key2, value2)
        .put(key3, value3)
        .put(key4, value4)
        .put(key5, value5)
        .build();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(Object key) {
    return map.get(key);
  }

  @Override
  public Set<K> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return map.entrySet();
  }

  private static UnsupportedOperationException newModificationUnsupported() {
    return new UnsupportedOperationException(ImmutableMap.class.getSimpleName() + " cannot be modified");
  }

  @Override
  public V put(K key, V value) {
    throw newModificationUnsupported();
  }

  @Override
  public V remove(Object key) {
    throw newModificationUnsupported();
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    throw newModificationUnsupported();
  }

  @Override
  public void clear() {
    throw newModificationUnsupported();
  }
}
