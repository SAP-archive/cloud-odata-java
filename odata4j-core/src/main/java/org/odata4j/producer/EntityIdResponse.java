package org.odata4j.producer;

import java.util.Collection;

import org.odata4j.core.OEntityId;
import org.odata4j.edm.EdmMultiplicity;

/**
 * An <code>EntityIdResponse</code> is a response to a client request expecting one or more entity-ids.
 *
 * <p>The {@link Responses} static factory class can be used to create <code>EntityIdResponse</code> instances.</p>
 */
public interface EntityIdResponse {

  /** Gets the multiplicity of the response */
  EdmMultiplicity getMultiplicity();

  /** Gets the response payload as entity ids */
  Collection<OEntityId> getEntities();

}
