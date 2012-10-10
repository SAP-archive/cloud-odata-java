package org.odata4j.core;

import org.odata4j.edm.EdmType;

/**
 * A homogeneous collection of OData objects of a given {@link EdmType}.
 * <p>The {@link OCollections} static factory class can be used to create <code>OCollection</code> instances.</p>
 *
 * @param <T>  type of instances in the collection.
 * @see OCollections
 */
public interface OCollection<T extends OObject> extends OObject, Iterable<T> {

  /** Builds an {@link OCollection} instance. */
  public interface Builder<T extends OObject> {
    Builder<T> add(T value);

    OCollection<T> build();
  }

  /** Gets the size of this collection */
  int size();

}
