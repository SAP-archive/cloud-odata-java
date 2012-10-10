package org.odata4j.producer;

import java.util.List;

import org.odata4j.core.OEntity;
import org.odata4j.edm.EdmEntitySet;

/**
 * An <code>EntitiesResponse</code> is a response to a client request expecting multiple OData entities.
 *
 * <p>The {@link Responses} static factory class can be used to create <code>EntitiesResponse</code> instances.</p>
 */
public interface EntitiesResponse extends BaseResponse {

  /**
   * Gets the entity-set for the entities.
   *
   * @return the entity-set for the entities
   */
  EdmEntitySet getEntitySet();

  /**
   * Gets the OData entities, if any.
   *
   * @return the entities, if any
   */
  List<OEntity> getEntities();

  /**
   * Gets the inline-count value, if applicable.
   *
   * @return the inline-count value, if applicable
   */
  Integer getInlineCount();

  /**
   * Gets the continuation token to use on a subsequent request, if applicable.
   *
   * @return a continuation token, if applicable
   */
  String getSkipToken();

}
