package org.odata4j.producer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.odata4j.core.OCollection;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OError;
import org.odata4j.core.OObject;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmSimpleType;

/**
 * A static factory to create immutable {@link EntitiesResponse}, {@link EntityResponse}, {@link PropertyResponse}, {@link EntityIdResponse},
 * {@link ComplexObjectResponse}, {@link CollectionResponse}, or {@link ErrorResponse} instances.
 */
public class Responses {

  private Responses() {}

  /**
   * Creates a new <code>EntitiesResponse</code> instance.
   *
   * @param entities  the OData entities, if any
   * @param entitySet  the entity-set
   * @param inlineCount  the inline-count value, if necessary
   * @param skipToken  the continuation-token, if necessary
   * @return a new <code>EntitiesResponse</code> instance
   */
  public static EntitiesResponse entities(
      final List<OEntity> entities,
      final EdmEntitySet entitySet,
      final Integer inlineCount,
      final String skipToken) {
    return new EntitiesResponse() {

      @Override
      public List<OEntity> getEntities() {
        return entities;
      }

      @Override
      public EdmEntitySet getEntitySet() {
        return entitySet;
      }

      @Override
      public Integer getInlineCount() {
        return inlineCount;
      }

      @Override
      public String getSkipToken() {
        return skipToken;
      }
    };
  }

  public static CountResponse count(final long count) {
    return new CountResponse() {
      @Override
      public long getCount() {
        return count;
      }
    };
  }

  /**
   * Creates a new <code>EntityResponse</code> instance.
   *
   * @param entity  the OData entity
   * @return a new <code>EntityResponse</code> instance
   */
  public static EntityResponse entity(final OEntity entity) {
    return new EntityResponse() {
      @Override
      public OEntity getEntity() {
        return entity;
      }
    };
  }

  /**
   * Creates a new <code>PropertyResponse</code> instance.
   *
   * @param property  the property value
   * @return a new <code>PropertyResponse</code> instance
   */
  public static PropertyResponse property(final OProperty<?> property) {
    return new PropertyResponse() {
      @Override
      public OProperty<?> getProperty() {
        return property;
      }
    };
  }

  public static SimpleResponse simple(final EdmSimpleType<?> type, final Object value) {
    return new SimpleResponse() {

      @Override
      public EdmSimpleType<?> getType() {
        return type;
      }

      @Override
      public Object getValue() {
        return value;
      }

      @Override
      public String getName() {
        return null;
      }

    };
  }

  public static SimpleResponse simple(final EdmSimpleType<?> type, final String name, final Object value) {
    return new SimpleResponse() {

      @Override
      public EdmSimpleType<?> getType() {
        return type;
      }

      @Override
      public Object getValue() {
        return value;
      }

      @Override
      public String getName() {
        return name;
      }

    };
  }

  /**
   * Creates a new <code>EntityIdResponse</code> instance for payloads with a cardinality of {@link EdmMultiplicity#ONE}.
   *
   * @param entityId  the payload entity
   * @return a new <code>EntityIdResponse</code> instance
   */
  public static <T extends OEntityId> EntityIdResponse singleId(T entityId) {
    final List<OEntityId> entities = new ArrayList<OEntityId>();
    entities.add(entityId);

    return new EntityIdResponse() {
      @Override
      public EdmMultiplicity getMultiplicity() {
        return EdmMultiplicity.ONE;
      }

      @Override
      public Collection<OEntityId> getEntities() {
        return entities;
      }
    };
  }

  /**
   * Creates a new <code>EntityIdResponse</code> instance for payloads with a cardinality of {@link EdmMultiplicity#MANY}.
   *
   * @param entityIds  the payload entities
   * @return a new <code>EntityIdResponse</code> instance
   */
  public static <T extends OEntityId> EntityIdResponse multipleIds(Iterable<T> entityIds) {
    final List<OEntityId> entities = new ArrayList<OEntityId>();
    for (T entityId : entityIds)
      entities.add(entityId);

    return new EntityIdResponse() {
      @Override
      public EdmMultiplicity getMultiplicity() {
        return EdmMultiplicity.MANY;
      }

      @Override
      public Collection<OEntityId> getEntities() {
        return entities;
      }
    };
  }

  /**
   * Creates a new <code>ComplexObjectResponse</code> instance.
   *
   * @param complexObject  the complex object
   * @return a new <code>ComplexObjectResponse</code> instance
   */
  public static ComplexObjectResponse complexObject(final OComplexObject complexObject, final String complexObjectName) {
    return new ComplexObjectResponse() {
      @Override
      public OComplexObject getObject() {
        return complexObject;
      }

      @Override
      public String getComplexObjectName() {
        return complexObjectName;
      }
    };
  }

  /**
   * Creates a new <code>CollectionResponse</code> instance.
   *
   * @param collection  the collection
   * @return a new <code>ComplexObjectResponse</code> instance
   */
  public static <T extends OObject> CollectionResponse<?> collection(final OCollection<T> collection) {
    return collection(collection, null, null, null, null);
  }

  /**
   * Creates a new <code>CollectionResponse</code> instance.
   *
   * @param collection  the collection
   * @return a new <code>ComplexObjectResponse</code> instance
   */
  public static <T extends OObject> CollectionResponse<?> collection(
      final OCollection<T> collection,
      final EdmEntitySet entitySet,
      final Integer inlineCount,
      final String skipToken,
      final String collectionName) {

    return new CollectionResponse<T>() {

      @Override
      public OCollection<T> getCollection() {
        return collection;
      }

      @Override
      public String getCollectionName() {
        return collectionName;
      }

      public Integer getInlineCount() {
        return inlineCount;
      }

      @Override
      public String getSkipToken() {
        return skipToken;
      }

      @Override
      public EdmEntitySet getEntitySet() {
        return entitySet;
      }
    };
  }

  /**
   * Creates a new <code>ErrorResponse</code> instance.
   *
   * @param error  the OData error
   * @return a new <code>ErrorResponse</code> instance
   */
  public static ErrorResponse error(final OError error) {
    return new ErrorResponse() {

      @Override
      public OError getError() {
        return error;
      }
    };
  }
}
