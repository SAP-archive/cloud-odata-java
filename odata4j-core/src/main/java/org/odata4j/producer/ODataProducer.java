package org.odata4j.producer;

import java.util.Map;

import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OExtensible;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.producer.edm.MetadataProducer;

/**
 * Implement <code>ODataProducer</code> on the server-side to create a new java-based OData producer.
 *
 * <p>The interface contains methods for clients to retrieve/query entities and to introspect service metadata (for read-only services)
 * as well as methods to create/modify/delete entities (for read-write services).</p>
 *
 * <p>Note that all client requests/responses are normalized - all details involving the OData http protocol, query expression model, EDM structure are handled by odata4j at a higher level.</p>
 */
public interface ODataProducer extends OExtensible<ODataProducer> {

  /**
   * Obtains the service metadata for this producer.
   *
   * @return a fully-constructed metadata object
   */
  EdmDataServices getMetadata();

  /**
   * Obtains the ODataProducer implementation that serves the metadata as
   * OData EDM constructs.
   *
   * @return the MetadataProducer object
   */
  MetadataProducer getMetadataProducer();

  /**
   * Gets all the entities for a given set matching the query information.
   *
   * @param entitySetName  the entity-set name for entities to return
   * @param queryInfo  the additional constraints to apply to the entities
   * @return a packaged collection of entities to pass back to the client
   */
  EntitiesResponse getEntities(ODataContext context, String entitySetName, QueryInfo queryInfo);

  /**
   * Gets the count of all the entities for a given set matching the query information.
   *
   * @param entitySetName  the entity-set name for entities whose count is returned
   * @param queryInfo  the additional constraints to apply to the entities
   * @return count of the entities
   */
  CountResponse getEntitiesCount(ODataContext context, String entitySetName, QueryInfo queryInfo);

  /**
   * Obtains a single entity based on its type and key.
   *
   * @param entitySetName  the entity-set name for entities to return
   * @param entityKey  the unique entity-key of the entity to start with
   * @param queryInfo  the additional constraints applicable to single-entity queries
   * @return the resulting entity
   */
  EntityResponse getEntity(ODataContext context, String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo);

  /**
   * Given a specific entity, follow one of its navigation properties, applying constraints as appropriate.
   * Return the resulting entity, entities, or property value.
   *
   * @param entitySetName  the entity-set of the entity to start with
   * @param entityKey  the unique entity-key of the entity to start with
   * @param navProp  the navigation property to follow
   * @param queryInfo  additional constraints to apply to the result
   * @return the resulting entity, entities, or property value
   */
  BaseResponse getNavProperty(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo);

  /**
   * Given a specific entity, follow one of its navigation properties, applying constraints as appropriate.
   * Return the count of the resulting entities.
   *
   * @param entitySetName  the entity-set of the entity to start with
   * @param entityKey  the unique entity-key of the entity to start with
   * @param navProp  the navigation property to follow
   * @param queryInfo  additional constraints to apply to the result
   * @return the count of the resulting entities
   */
  CountResponse getNavPropertyCount(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo);

  /**
   * Releases any resources managed by this producer.
   */
  void close();

  /**
   * Creates a new OData entity.
   *
   * @param entitySetName  the entity-set name
   * @param entity  the request entity sent from the client
   * @return the newly-created entity, fully populated with the key and default properties
   *
   * @see <a href="http://www.odata.org/developers/protocols/operations#CreatingnewEntries">[odata.org] Creating new Entries</a>
   */
  EntityResponse createEntity(ODataContext context, String entitySetName, OEntity entity);

  /**
   * Creates a new OData entity as a reference of an existing entity, implicitly linked to the existing entity by a navigation property.
   *
   * @param entitySetName  the entity-set name of the existing entity
   * @param entityKey  the entity-key of the existing entity
   * @param navProp  the navigation property off of the existing entity
   * @param entity  the request entity sent from the client
   * @return the newly-created entity, fully populated with the key and default properties, and linked to the existing entity
   *
   * @see <a href="http://www.odata.org/developers/protocols/operations#CreatingnewEntries">[odata.org] Creating new Entries</a>
   */
  EntityResponse createEntity(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, OEntity entity);

  /**
   * Deletes an existing entity.
   *
   * @param entitySetName  the entity-set name of the entity
   * @param entityKey  the entity-key of the entity
   *
   * @see <a href="http://www.odata.org/developers/protocols/operations#DeletingEntries">[odata.org] Deleting Entries</a>
   */
  void deleteEntity(ODataContext context, String entitySetName, OEntityKey entityKey);

  /**
   * Modifies an existing entity using merge semantics.
   *
   * @param entitySetName  the entity-set name
   * @param entity  the entity modifications sent from the client
   *
   * @see <a href="http://www.odata.org/developers/protocols/operations#UpdatingEntries">[odata.org] Updating Entries</a>
   */
  void mergeEntity(ODataContext context, String entitySetName, OEntity entity);

  /**
   * Modifies an existing entity using update semantics.
   *
   * @param entitySetName  the entity-set name
   * @param entity  the entity modifications sent from the client
   *
   * @see <a href="http://www.odata.org/developers/protocols/operations#UpdatingEntries">[odata.org] Updating Entries</a>
   */
  void updateEntity(ODataContext context, String entitySetName, OEntity entity);

  /**
   * Returns the value of an entity's navigation property as a collection of entity links (or a single link if the association cardinality is 1).
   *
   * @param sourceEntity  an entity with at least one navigation property
   * @param targetNavProp  the navigation property
   * @return a collection of entity links (or a single link if the association cardinality is 1)
   */
  EntityIdResponse getLinks(ODataContext context, OEntityId sourceEntity, String targetNavProp);

  /**
   * Creates a link between two entities.
   *
   * @param sourceEntity  an entity with at least one navigation property
   * @param targetNavProp  the navigation property
   * @param targetEntity  the link target entity
   *
   * @see <a href="http://www.odata.org/developers/protocols/operations#CreatingLinksbetweenEntries">[odata.org] Creating Links between Entries</a>
   */
  void createLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity);

  /**
   * Replaces an existing link between two entities.
   *
   * @param sourceEntity  an entity with at least one navigation property
   * @param targetNavProp  the navigation property
   * @param oldTargetEntityKey  if the navigation property represents a set, the key identifying the old target entity within the set, else n/a
   * @param newTargetEntity  the new link target entity
   *
   * @see <a href="http://www.odata.org/developers/protocols/operations#ReplacingLinksbetweenEntries">[odata.org] Replacing Links between Entries</a>
   */
  void updateLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity);

  /**
   * Deletes an existing link between two entities.
   *
   * @param sourceEntity  an entity with at least one navigation property
   * @param targetNavProp  the navigation property
   * @param targetEntityKey  if the navigation property represents a set, the key identifying the target entity within the set, else n/a
   */
  void deleteLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey);

  /**
   * Calls a function (aka Service Operation).
   *
   * @param name  the name of the function
   * @param params  the parameters to the function
   * @param queryInfo  additional query parameters to apply to collection-valued results
   * @return a BaseResponse appropriately typed to hold the function results
   *    From the spec:<pre>
   *    The return type of &lt;Function&gt; MUST be one of the following:
   *        An EDMSimpleType or collection of EDMSimpleTypes.
   *        An entity type or collection of entity types.
   *        A complex type or collection of complex types.
   *        A row type or collection of row types.
   *        &lt;ReturnType&gt; can contain a maximum of one &lt;CollectionType&gt; element.
   *        &lt;ReturnType&gt; can contain a maximum of one &lt;ReferenceType&gt; element.
   *        &lt;ReturnType&gt; can contain a maximum of one &lt;RowType&gt; element.
   *        A ref type or collection of ref types.</pre>
   */
  BaseResponse callFunction(ODataContext context, EdmFunctionImport name, Map<String, OFunctionParameter> params, QueryInfo queryInfo);
}
