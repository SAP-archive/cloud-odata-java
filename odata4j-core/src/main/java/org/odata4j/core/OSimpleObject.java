package org.odata4j.core;

import org.odata4j.edm.EdmSimpleType;

/**
 * An instance of an {@link EdmSimpleType}, a primitive value in the OData type system.
 *
 * <p>The {@link OSimpleObjects} static factory class can be used to create <code>OSimpleObject</code> instances.</p>
 *
 * @param <V>  the Java class used to represent the simple type
 * @see OSimpleObjects
 */
public interface OSimpleObject<V> extends OObject {

  /** Gets the value. */
  V getValue();

  /** Gets the edm type, which will be an edm simple type */
  @Override
  EdmSimpleType<V> getType();

}
