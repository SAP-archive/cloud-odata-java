package org.odata4j.core;

/**
 * A static factory to create immutable {@link OEntityId} instances.
 */
public class OEntityIds {

  private OEntityIds() {}

  /**
   * Creates an entity-id.
   *
   * @param entitySetName  the entity-set
   * @param entityKeyValues  the entity-key as one or more values
   * @return the entity-id
   */
  public static OEntityId create(String entitySetName, Object... entityKeyValues) {
    return create(entitySetName, OEntityKey.create(entityKeyValues));
  }

  /**
   * Creates an entity-id.
   *
   * @param entitySetName  the entity-set
   * @param entityKey  the entity-key
   * @return the entity-id
   */
  public static OEntityId create(String entitySetName, OEntityKey entityKey) {
    if (entitySetName == null)
      throw new NullPointerException("Must provide entity-set name");
    if (entityKey == null)
      throw new NullPointerException("Must provide entity-key");

    return new OEntityIdImpl(entitySetName, entityKey);
  }

  /**
   * Parses an entity-id from a "key string" representation.
   * <p>e.g. {@code Customers(15)}
   * @param entityId  the entity-id as a string representation
   * @return the parsed entity-id
   */
  public static OEntityId parse(String entityId) {
    if (entityId == null)
      throw new NullPointerException("Must provide entity-id");

    int indexOfParen = entityId.indexOf('(');
    if (indexOfParen == -1)
      throw new IllegalArgumentException("Invalid entity-id: " + entityId);

    String entitySetName = entityId.substring(0, indexOfParen);
    OEntityKey entityKey = OEntityKey.parse(entityId.substring(indexOfParen));
    return create(entitySetName, entityKey);
  }

  /**
   * Parses an entity-id of an entity at an OData uri given its service root uri.
   * @param serviceRootUri  the service root uri
   * @param uri  the entity uri
   * @return the parsed entity-id
   */
  public static OEntityId parse(String serviceRootUri, String uri) {
    if (serviceRootUri == null)
      throw new NullPointerException("Must provide service-root-uri");
    if (uri == null)
      throw new NullPointerException("Must provide uri");

    String entityId = uri;
    if (entityId.toLowerCase().startsWith(serviceRootUri.toLowerCase()))
      entityId = entityId.substring(serviceRootUri.length());
    if (entityId.startsWith("/"))
      entityId = entityId.substring(1);

    return parse(entityId);
  }

  /**
   * Computes the "key string" representation of an entity.
   *
   * @param entity  the entity
   * @return the "key string" representation e.g. {@code Customers(15)}
   */
  public static String toKeyString(OEntityId entity) {
    if (entity == null)
      return null;
    return entity.getEntitySetName() + entity.getEntityKey().toKeyString();
  }

  /**
   * Determines equality of two entity ids.  Ids are equal if their key strings are equal.
   *
   * @param lhs  the first entity id
   * @param rhs  the second entity id
   * @return whether or not the ids are equal
   */
  public static boolean equals(OEntityId lhs, OEntityId rhs) {
    if (lhs == null)
      return rhs == null;
    return toKeyString(lhs).equals(toKeyString(rhs));
  }

  private static class OEntityIdImpl implements OEntityId {

    private final String entitySetName;
    private final OEntityKey entityKey;

    public OEntityIdImpl(String entitySetName, OEntityKey entityKey) {
      this.entitySetName = entitySetName;
      this.entityKey = entityKey;
    }

    @Override
    public String getEntitySetName() {
      return entitySetName;
    }

    @Override
    public OEntityKey getEntityKey() {
      return entityKey;
    }

    @Override
    public String toString() {
      return String.format("OEntityId[%s%s]", entitySetName, entityKey.toKeyString());
    }

  }

}
