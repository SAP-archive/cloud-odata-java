package org.odata4j.edm;

/**
 * An object that knows how to produce an {@link EdmDataServices} model in the
 * context of an {@link EdmDecorator}.
 */
public interface EdmGenerator {

  /** Produces a new mutable {@link EdmDataServices} model given an optional decorator. */
  EdmDataServices.Builder generateEdm(EdmDecorator decorator);

}
