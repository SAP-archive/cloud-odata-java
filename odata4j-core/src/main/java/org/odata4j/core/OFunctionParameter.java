package org.odata4j.core;

import org.odata4j.edm.EdmType;

/**
 * An immutable service operation parameter, consisting of a name, a strongly-typed value, and an edm-type.
 * <p>The {@link OFunctionParameters} static factory class can be used to create <code>OFunctionParameter</code> instances.</p>
 *
 * @see OFunctionParameters
 */
public interface OFunctionParameter extends NamedValue<OObject> {

  /**
   * Gets the edm-type for this property.
   *
   * @return the edm-type
   */
  EdmType getType();

}
