package org.odata4j.producer;

import org.odata4j.core.OCollection;
import org.odata4j.core.OObject;
import org.odata4j.edm.EdmEntitySet;

/**
 * An <code>CollectionResponse</code> is a response to a client request expecting a collection of OData objects.
 *
 * <p>The {@link Responses} static factory class can be used to create <code>CollectionResponse</code> instances.</p>
 */
public interface CollectionResponse<T extends OObject> extends BaseResponse {

  /**
  * Gets the entity-set for the entities if the collection is a collection of entities.
  *
  * @return the entity-set for the entities
  */
  EdmEntitySet getEntitySet();

  OCollection<T> getCollection();

  String getCollectionName();

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
