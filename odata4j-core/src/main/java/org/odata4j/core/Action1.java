package org.odata4j.core;

/**
 * Generic function implementation taking a single argument and returning no value.
 */
public interface Action1<T> {

  /**
   * Applies this function, returning no result.
   *
   * @param input  Function argument
   */
  void apply(T input);

}
