package org.odata4j.consumer;

import org.core4j.Enumerable;
import org.odata4j.consumer.behaviors.OClientBehavior;
import org.odata4j.core.EntitySetInfo;
import org.odata4j.core.OCountRequest;
import org.odata4j.core.OCreateRequest;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityDeleteRequest;
import org.odata4j.core.OEntityGetRequest;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OEntityRequest;
import org.odata4j.core.OFunctionRequest;
import org.odata4j.core.OModifyRequest;
import org.odata4j.core.OObject;
import org.odata4j.core.OQueryRequest;
import org.odata4j.core.ORelatedEntitiesLink;
import org.odata4j.core.ORelatedEntityLink;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.exceptions.ODataProducerException;
import org.odata4j.format.FormatType;

/**
 * <code>ODataConsumer</code> is the client-side interface to an OData service.
 *
 * <p>Use {@link ODataConsumers#create(String)} or {@link ODataConsumers#newBuilder(String)} to connect to an existing OData service.</p>
 *
 * @see ODataConsumers
 */
public interface ODataConsumer {

  /**
   * Sends http request and/or response information to standard out.  Useful for debugging.
   */
  public static enum Dump {
    /**
     * enum-as-singleton pattern
     */
    INSTANCE;
    private boolean requestHeaders;
    private boolean requestBody;
    private boolean responseHeaders;
    private boolean responseBody;

    /**
     * Sets whether or not to dump all http request and response information.
     */
    public void all(boolean dump) {
      requestHeaders(dump);
      requestBody(dump);
      responseHeaders(dump);
      responseBody(dump);
    }

    /**
     * Are http request headers currently dumped?
     */
    public boolean requestHeaders() {
      return requestHeaders;
    }

    /**
     * Sets whether or not to dump http request headers.
     */
    public void requestHeaders(boolean dump) {
      this.requestHeaders = dump;
    }

    /**
     * Are http request bodies currently dumped?
     */
    public boolean requestBody() {
      return requestBody;
    }

    /**
     * Sets whether or not to dump http request bodies.
     */
    public void requestBody(boolean dump) {
      this.requestBody = dump;
    }

    /**
     * Are http response headers currently dumped?
     */
    public boolean responseHeaders() {
      return responseHeaders;
    }

    /**
     * Sets whether or not to dump http response headers.
     */
    public void responseHeaders(boolean dump) {
      this.responseHeaders = dump;
    }

    /**
     * Are http response bodies currently dumped?
     */
    public boolean responseBody() {
      return responseBody;
    }

    /**
     * Sets whether or not to dump http response bodies.
     */
    public void responseBody(boolean dump) {
      this.responseBody = dump;
    }
  }

  /**
   * Sends http request and/or response information to standard out.  Useful for debugging.
   */
  public static final Dump dump = Dump.INSTANCE;

  /** Mutable builder for {@link ODataConsumer} instances. */
  public interface Builder {

    /**
     * Sets the desired format type for all requests.  e.g. ATOM or JSON.
     *
     * <p>Note: some services do not support all formats.</p>
     *
     * @param formatType  desired format
     * @return the consumer-builder
     */
    Builder setFormatType(FormatType formatType);

    /**
     * Sets the client behavior extensions associated with the consumer.
     *
     * @param clientBehaviors  extensions used for client request modification
     * @return the consumer-builder
     */
    Builder setClientBehaviors(OClientBehavior... clientBehaviors);

    /**
     * Returns the immutable consumer instance for interacting with an OData service.
     *
     * @return the newly-created consumer instance
     */
    ODataConsumer build();

  }

  /**
   * Gets the OData service uri.
   *
   * <p>e.g. <code>http://services.odata.org/Northwind/Northwind.svc/</code></p>
   *
   * @return the service uri
   */
  String getServiceRootUri();

  /**
   * Lists all top-level entity-sets for the OData service.
   *
   * @return the entity-set information
   * @throws ODataProducerException  error from the producer
   */
  Enumerable<EntitySetInfo> getEntitySets() throws ODataProducerException;

  /**
   * Gets the OData service metadata.
   *
   * @return the service metadata
   * @see <a href="http://msdn.microsoft.com/en-us/library/dd541087(v=prot.10).aspx">[msdn] 2.2 &lt;edmx:DataServices&gt;</a>
   */
  EdmDataServices getMetadata();

  /**
   * Gets entities referred to by the given related-entities link.
   *
   * <p>The query-request builder returned can be used for further server-side filtering.  Call {@link OQueryRequest#execute()} to issue request.</p>
   *
   * @param link  the link
   * @return a new query-request builder
   */
  OQueryRequest<OEntity> getEntities(ORelatedEntitiesLink link);

  /**
   * Gets entities from the given entity-set.
   *
   * <p>The query-request builder returned can be used for further server-side filtering.  Call {@link OQueryRequest#execute()} to issue request.</p>
   *
   * @param entitySetHref  the entity-set href
   * @return a new query-request builder
   */
  OQueryRequest<OEntity> getEntities(String entitySetHref);

  /**
   * Gets entities from the given entity-set.  The entities will be represented as the given java-type.
   *
   * <p>The query-request builder returned can be used for further server-side filtering.  Call {@link OQueryRequest#execute()} to issue request.</p>
   *
   * @param <T>  the entity representation as a java type
   * @param entityType  the entity representation as a java type
   * @param entitySetHref  the entity-set href
   * @return a new query-request builder
   */
  <T> OQueryRequest<T> getEntities(Class<T> entityType, String entitySetHref);

  /**
   * Gets the entity referred to by the given related entity link.
   *
   * <p>The entity-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param link  the link
   * @return a new entity-request builder
   */
  OEntityGetRequest<OEntity> getEntity(ORelatedEntityLink link);

  /**
   * Gets the entity by entity-set name and entity-key value.
   *
   * <p>The entity-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param entitySetName  the name of the entity-set
   * @param keyValue  the entity-key value
   * @return a new entity-request builder
   */
  OEntityGetRequest<OEntity> getEntity(String entitySetName, Object keyValue);

  /**
   * Gets the latest version of an entity using the given entity as a template.
   *
   * <p>The entity-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param entity  an existing entity to use as a template, using its entity-set and entity-key
   * @return a new entity-request builder
   */
  OEntityGetRequest<OEntity> getEntity(OEntity entity);

  /**
   * Gets the entity by entity-set name and entity-key.
   *
   * <p>The entity-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param entitySetName  the name of the entity-set
   * @param key  the entity-key
   * @return a new entity-request builder
   */
  OEntityGetRequest<OEntity> getEntity(String entitySetName, OEntityKey key);

  /**
   * Gets the entity by entity-set name and entity-key value.  The entity will be represented as the given java-type.
   *
   * <p>The entity-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param <T>  the entity representation as a java type
   * @param entityType  the entity representation as a java type
   * @param entitySetName  the name of the entity-set
   * @param keyValue  the entity-key value
   * @return a new entity-request builder
   */
  <T> OEntityGetRequest<T> getEntity(Class<T> entityType, String entitySetName, Object keyValue);

  /**
   * Gets the entity by entity-set name and entity-key.  The entity will be represented as the given java-type.
   *
   * <p>The entity-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param <T>  the entity representation as a java type
   * @param entityType   the entity representation as a java type
   * @param entitySetName  the name of the entity-set
   * @param key  the entity-key
   * @return a new entity-request builder
   */
  <T> OEntityGetRequest<T> getEntity(Class<T> entityType, String entitySetName, OEntityKey key);

  /**
   * Gets related entity links for a given source entity by navigation property.
   *
   * <p>The entityid-request builder returned can be used for further server-side filtering.  Call {@link OQueryRequest#execute()} to issue request.</p>
   *
   * @param sourceEntity  the entity to start from
   * @param targetNavProp  the relationship navigation property
   * @return a new entityid-request builder
   */
  OQueryRequest<OEntityId> getLinks(OEntityId sourceEntity, String targetNavProp);

  /**
   * Creates a new related entity link between two entities.
   *
   * <p>Call {@link OEntityRequest#execute()} on the returned request builder to issue request.</p>
   *
   * @param sourceEntity  the entity to start from
   * @param targetNavProp  the relationship navigation property
   * @param targetEntity  the entity to use as the target of the relationship
   * @return a request builder
   */
  OEntityRequest<Void> createLink(OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity);

  /**
   * Deletes related entity links between two entities by navigation property.
   *
   * <p>Call {@link OEntityRequest#execute()} on the returned request builder to issue request.</p>
   *
   * @param sourceEntity  the entity to start from
   * @param targetNavProp  the relationship navigation property
   * @param targetKeyValues  the target entity-key, applicable if the navigation property represents a collection
   * @return a request builder
   */
  OEntityRequest<Void> deleteLink(OEntityId sourceEntity, String targetNavProp, Object... targetKeyValues);

  /**
   * Updates related entity links between two entities by navigation property.
   *
   * <p>Call {@link OEntityRequest#execute()} on the returned request builder to issue request.</p>
   *
   * @param sourceEntity  the entity to start from
   * @param newTargetEntity  the entity to use as the new target of the relationship
   * @param targetNavProp  the relationship navigation property
   * @param oldTargetKeyValues  the target entity-key, applicable if the navigation property represents a collection
   * @return a request builder
   */
  OEntityRequest<Void> updateLink(OEntityId sourceEntity, OEntityId newTargetEntity, String targetNavProp, Object... oldTargetKeyValues);

  /**
   * Creates a new entity in the given entity-set.
   *
   * <p>The create-request builder returned can be used to construct the new entity.  Call {@link OCreateRequest#execute()} to issue request.</p>
   *
   * @param entitySetName  the name of the entity-set
   * @return a new create-request builder
   */
  OCreateRequest<OEntity> createEntity(String entitySetName);

  /**
   * Modifies an existing entity using update semantics.
   *
   * <p>The modification-request builder returned can be used to redefine the new entity.  Call {@link OModifyRequest#execute()} to issue request.</p>
   *
   * @param entity  the entity identity
   * @return a new modification-request builder
   */
  OModifyRequest<OEntity> updateEntity(OEntity entity);

  /**
   * Modifies an existing entity using merge semantics.
   *
   * <p>The modification-request builder returned can be used to modify the new entity.  Call {@link OModifyRequest#execute()} to issue request.</p>
   *
   * @param entity  the entity identity
   * @return a new modification-request builder
   */
  OModifyRequest<OEntity> mergeEntity(OEntity entity);

  /**
   * Modifies an existing entity using merge semantics.
   *
   * <p>The modification-request builder returned can be used to modify the new entity.  Call {@link OModifyRequest#execute()} to issue request.</p>
   *
   * @param entitySetName  the entity identity entity-set name
   * @param keyValue  the entity identity key value
   * @return a new modification-request builder
   */
  OModifyRequest<OEntity> mergeEntity(String entitySetName, Object keyValue);

  /**
   * Modifies an existing entity using merge semantics.
   *
   * <p>The modification-request builder returned can be used to modify the new entity.  Call {@link OModifyRequest#execute()} to issue request.</p>
   *
   * @param entitySetName  the entity identity entity-set name
   * @param key  the entity identity key
   * @return a new modification-request builder
   */
  OModifyRequest<OEntity> mergeEntity(String entitySetName, OEntityKey key);

  /**
   * Deletes an existing entity.
   *
   * <p>The delete-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param entity  the entity identity
   * @return a new delete-request builder
   */
  OEntityDeleteRequest deleteEntity(OEntity entity);

  /**
   * Deletes an existing entity.
   *
   * <p>The delete-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param entitySetName  the entity identity entity-set name
   * @param keyValue  the entity identity key value
   * @return a new entity-request builder
   */
  OEntityDeleteRequest deleteEntity(String entitySetName, Object keyValue);

  /**
   * Deletes an existing entity.
   *
   * <p>The delete-request builder returned can be used for further navigation.  Call {@link OEntityRequest#execute()} to issue request.</p>
   *
   * @param entitySetName  the entity identity entity-set name
   * @param key  the entity identity key
   * @return a new entity-request builder
   */
  OEntityDeleteRequest deleteEntity(String entitySetName, OEntityKey key);

  /**
   * Call a server-side function (also known as a service operation).
   *
   * <p>The functioncall-request builder returned can be used to add parameters.  Call {@link OFunctionRequest#execute()} to issue request.</p>
   *
   * @param functionName  the function name
   * @return a new functioncall-request builder
   */
  OFunctionRequest<OObject> callFunction(String functionName);

  /**
   * Returns a single value request which can be extended by query options. The execution of the request will return a single value for $count.
   *
   * @param entitySetName  the entity identity entity-set name
   * @return a new count-request builder
   */
  OCountRequest getEntitiesCount(String entitySetName);
}
