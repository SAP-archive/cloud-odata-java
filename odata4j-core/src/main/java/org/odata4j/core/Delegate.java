package org.odata4j.core;

/**
 * Base interface for delegates, also known as decorators.
 *
 * <p>Implementations are expected to implement all methods of type T, and forward to the delegate instance by default.
 */
public interface Delegate<T> {
  T getDelegate();
}
