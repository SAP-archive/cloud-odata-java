package org.odata4j.producer.inmemory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.core4j.Enumerable;
import org.core4j.Func;
import org.core4j.Func1;
import org.core4j.Predicate1;
import org.odata4j.core.OAtomStreamEntity;
import org.odata4j.core.OCollection;
import org.odata4j.core.OCollections;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OComplexObjects;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OExtension;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OLink;
import org.odata4j.core.OLinks;
import org.odata4j.core.OObject;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.core.OSimpleObject;
import org.odata4j.core.OSimpleObjects;
import org.odata4j.core.OStructuralObject;
import org.odata4j.edm.EdmCollectionType;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmDecorator;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmStructuralType;
import org.odata4j.edm.EdmType;
import org.odata4j.exceptions.NotFoundException;
import org.odata4j.exceptions.NotImplementedException;
import org.odata4j.expression.BoolCommonExpression;
import org.odata4j.expression.OrderByExpression;
import org.odata4j.expression.OrderByExpression.Direction;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.CountResponse;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.EntityIdResponse;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.InlineCount;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.PropertyPathHelper;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;
import org.odata4j.producer.edm.MetadataProducer;
import org.odata4j.producer.inmemory.InMemoryProducer.RequestContext.RequestType;

/**
 * An in-memory implementation of an ODATA Producer.  Uses the standard Java bean
 * and property model to access information within entities.
 */
public class InMemoryProducer implements ODataProducer {
  private static final boolean DUMP = false;

  private static void dump(String msg) {
    if (DUMP) System.out.println(msg);
  }

  public static final String ID_PROPNAME = "EntityId";

  private final String namespace;
  private final String containerName;
  private final int maxResults;
  // preserve the order of registration
  private final Map<String, InMemoryEntityInfo<?>> eis = new LinkedHashMap<String, InMemoryEntityInfo<?>>();
  private final Map<String, InMemoryComplexTypeInfo<?>> complexTypes = new LinkedHashMap<String, InMemoryComplexTypeInfo<?>>();
  private EdmDataServices metadata;
  private final EdmDecorator decorator;
  private final MetadataProducer metadataProducer;
  private final InMemoryTypeMapping typeMapping;

  private boolean includeNullPropertyValues = true;
  private final boolean flattenEdm;

  private static final int DEFAULT_MAX_RESULTS = 100;

  /**
   * Creates a new instance of an in-memory POJO producer.
   *
   * @param namespace  the namespace of the schema registrations
   */
  public InMemoryProducer(String namespace) {
    this(namespace, DEFAULT_MAX_RESULTS);
  }

  /**
   * Creates a new instance of an in-memory POJO producer.
   *
   * @param namespace  the namespace of the schema registrations
   * @param maxResults  the maximum number of entities to return in a single call
   */
  public InMemoryProducer(String namespace, int maxResults) {
    this(namespace, null, maxResults, null, null);
  }

  /**
   * Creates a new instance of an in-memory POJO producer.
   *
   * @param namespace  the namespace of the schema registrations
   * @param containerName  the container name for generated metadata
   * @param maxResults  the maximum number of entities to return in a single call
   * @param decorator  a decorator to use for edm customizations
   * @param typeMapping  optional mapping between java types and edm types, null for default
   */
  public InMemoryProducer(String namespace, String containerName, int maxResults, EdmDecorator decorator, InMemoryTypeMapping typeMapping) {
    this(namespace, containerName, maxResults, decorator, typeMapping,
        true); // legacy: flatten edm
  }

  public InMemoryProducer(String namespace, String containerName, int maxResults, EdmDecorator decorator, InMemoryTypeMapping typeMapping,
      boolean flattenEdm) {
    this.namespace = namespace;
    this.containerName = containerName != null && !containerName.isEmpty() ? containerName : "Container";
    this.maxResults = maxResults;
    this.decorator = decorator;
    this.metadataProducer = new MetadataProducer(this, decorator);
    this.typeMapping = typeMapping == null ? InMemoryTypeMapping.DEFAULT : typeMapping;
    this.flattenEdm = flattenEdm;
  }

  @Override
  public EdmDataServices getMetadata() {
    if (metadata == null) {
      metadata = newEdmGenerator(namespace, typeMapping, ID_PROPNAME, eis, complexTypes).generateEdm(decorator).build();
    }
    return metadata;
  }

  public String getContainerName() {
    return containerName;
  }

  protected InMemoryEdmGenerator newEdmGenerator(String namespace, InMemoryTypeMapping typeMapping, String idPropName, Map<String, InMemoryEntityInfo<?>> eis,
      Map<String, InMemoryComplexTypeInfo<?>> complexTypesInfo) {
    return new InMemoryEdmGenerator(namespace, containerName, typeMapping, ID_PROPNAME, eis, complexTypesInfo, this.flattenEdm);
  }

  @Override
  public MetadataProducer getMetadataProducer() {
    return metadataProducer;
  }

  @Override
  public void close() {

  }

  public void setIncludeNullPropertyValues(boolean value) {
    this.includeNullPropertyValues = value;
  }

  /**
   * Registers a POJO class as an EdmComplexType.
   *
   * @param complexTypeClass    The POJO Class
   * @param typeName            The name of the EdmComplexType
   */
  public <TEntity> void registerComplexType(Class<TEntity> complexTypeClass, String typeName) {
    registerComplexType(complexTypeClass, typeName,
        new EnumsAsStringsPropertyModelDelegate(new BeanBasedPropertyModel(complexTypeClass, this.flattenEdm)));
  }

  public <TEntity> void registerComplexType(Class<TEntity> complexTypeClass, String typeName, PropertyModel propertyModel) {
    InMemoryComplexTypeInfo<TEntity> i = new InMemoryComplexTypeInfo<TEntity>();
    i.typeName = (typeName == null) ? complexTypeClass.getSimpleName() : typeName;
    i.entityClass = complexTypeClass;
    i.propertyModel = propertyModel;

    complexTypes.put(i.typeName, i);
    metadata = null;
  }

  /**
   * Registers a new entity based on a POJO, with support for composite keys.
   *
   * @param entityClass  the class of the entities that are to be stored in the set
   * @param entitySetName  the alias the set will be known by; this is what is used in the OData url
   * @param get  a function to iterate over the elements in the set
   * @param keys  one or more keys for the entity
   */
  public <TEntity> void register(Class<TEntity> entityClass, String entitySetName, Func<Iterable<TEntity>> get, String... keys) {
    register(entityClass, entitySetName, entitySetName, get, keys);
  }

  /**
   * Registers a new entity based on a POJO, with support for composite keys.
   *
   * @param entityClass  the class of the entities that are to be stored in the set
   * @param entitySetName  the alias the set will be known by; this is what is used in the OData url
   * @param entityTypeName  type name of the entity
   * @param get  a function to iterate over the elements in the set
   * @param keys  one or more keys for the entity
   */
  public <TEntity> void register(Class<TEntity> entityClass, String entitySetName, String entityTypeName, Func<Iterable<TEntity>> get, String... keys) {
    PropertyModel model = new BeanBasedPropertyModel(entityClass, this.flattenEdm);
    model = new EnumsAsStringsPropertyModelDelegate(model);
    register(entityClass, model, entitySetName, entityTypeName, get, keys);
  }

  /**
   * Registers a new entity set based on a POJO type using the default property model.
   */
  public <TEntity, TKey> void register(Class<TEntity> entityClass, Class<TKey> keyClass, String entitySetName, Func<Iterable<TEntity>> get, Func1<TEntity, TKey> id) {
    PropertyModel model = new BeanBasedPropertyModel(entityClass, this.flattenEdm);
    model = new EnumsAsStringsPropertyModelDelegate(model);
    model = new EntityIdFunctionPropertyModelDelegate<TEntity, TKey>(model, ID_PROPNAME, keyClass, id);
    register(entityClass, model, entitySetName, get, ID_PROPNAME);
  }

  /**
   * Registers a new entity set based on a POJO type and a property model.
   *
   * @param entityClass  the class of the entities that are to be stored in the set
   * @param propertyModel a way to get/set properties on the POJO
   * @param entitySetName  the alias the set will be known by; this is what is used in the ODATA URL
   * @param get  a function to iterate over the elements in the set
   * @param keys  one or more keys for the entity
   */
  public <TEntity, TKey> void register(
      Class<TEntity> entityClass,
      PropertyModel propertyModel,
      String entitySetName,
      Func<Iterable<TEntity>> get,
      String... keys) {
    register(entityClass, propertyModel, entitySetName, entitySetName, get, keys);
  }

  public <TEntity> void register(
      final Class<TEntity> entityClass,
      final PropertyModel propertyModel,
      final String entitySetName,
      final String entityTypeName,
      final Func<Iterable<TEntity>> get,
      final String... keys) {
    register(entityClass, propertyModel, entitySetName, entityTypeName,
        get, null, keys);
  }

  public <TEntity> void register(
      final Class<TEntity> entityClass,
      final PropertyModel propertyModel,
      final String entitySetName,
      final String entityTypeName,
      final Func<Iterable<TEntity>> get,
      final Func1<RequestContext, Iterable<TEntity>> getWithContext,
      final String... keys) {

    InMemoryEntityInfo<TEntity> ei = new InMemoryEntityInfo<TEntity>();
    ei.entitySetName = entitySetName;
    ei.entityTypeName = entityTypeName;
    ei.properties = propertyModel;
    ei.get = get;
    ei.getWithContext = getWithContext;
    ei.keys = keys;
    ei.entityClass = entityClass;
    ei.hasStream = OAtomStreamEntity.class.isAssignableFrom(entityClass);

    ei.id = new Func1<Object, HashMap<String, Object>>() {
      @Override
      public HashMap<String, Object> apply(Object input) {
        HashMap<String, Object> values = new HashMap<String, Object>();
        for (String key : keys) {
          values.put(key, eis.get(entitySetName).properties.getPropertyValue(input, key));
        }
        return values;
      }
    };

    eis.put(entitySetName, ei);
    metadata = null;
  }

  protected InMemoryComplexTypeInfo<?> findComplexTypeInfoForClass(Class<?> clazz) {
    // drill down the hierarchy as far as we can go.
    InMemoryComplexTypeInfo<?> found = null;

    for (InMemoryComplexTypeInfo<?> typeInfo : this.complexTypes.values()) {
      if (typeInfo.entityClass.equals(clazz)) {
        return typeInfo; // as far down as we can go
      } else if (typeInfo.entityClass.isAssignableFrom(clazz)) {
        // somewhere in the ancestors of clazz
        if (null == found || found.entityClass.isAssignableFrom(typeInfo.entityClass)) {
          // we found a lower ancestor
          found = typeInfo;
        }
      }
    }

    return found;
  }

  protected InMemoryEntityInfo<?> findEntityInfoForClass(Class<?> clazz) {
    // drill down the hierarchy as far as we can go.
    InMemoryEntityInfo<?> found = null;

    for (InMemoryEntityInfo<?> typeInfo : this.eis.values()) {
      if (typeInfo.entityClass.equals(clazz)) {
        return typeInfo; // as far down as we can go
      } else if (typeInfo.entityClass.isAssignableFrom(clazz)) {
        // somewhere in the ancestors of clazz
        if (null == found || found.entityClass.isAssignableFrom(typeInfo.entityClass)) {
          // we found a lower ancestor
          found = typeInfo;
        }
      }
    }

    return found;
  }

  protected InMemoryEntityInfo<?> findEntityInfoForEntitySet(String entitySetName) {
    for (InMemoryEntityInfo<?> typeInfo : this.eis.values()) {
      if (typeInfo.entitySetName.equals(entitySetName)) {
        return typeInfo;
      }
    }

    return null;
  }

  /**
   * Transforms a POJO into a list of OProperties based on a given
   * EdmStructuralType.
   *
   * @param obj the POJO to transform
   * @param propertyModel the PropertyModel to use to access POJO class
   * structure and values.
   * @param structuralType the EdmStructuralType
   * @param properties put properties into this list.
   */
  protected void addPropertiesFromObject(Object obj, PropertyModel propertyModel, EdmStructuralType structuralType, List<OProperty<?>> properties, PropertyPathHelper pathHelper) {
    dump("addPropertiesFromObject: " + obj.getClass().getName());
    for (Iterator<EdmProperty> it = structuralType.getProperties().iterator(); it.hasNext();) {
      EdmProperty property = it.next();

      // $select projections not allowed for complex types....hmmh...why?
      if (structuralType instanceof EdmEntityType && !pathHelper.isSelected(property.getName())) {
        continue;
      }

      Object value = propertyModel.getPropertyValue(obj, property.getName());
      dump("  prop: " + property.getName() + " val: " + value);
      if (value == null && !this.includeNullPropertyValues) {
        // this is not permitted by the spec but makes debugging wide entity types
        // much easier.
        continue;
      }

      if (property.getCollectionKind() == EdmProperty.CollectionKind.NONE) {
        if (property.getType().isSimple()) {
          properties.add(OProperties.simple(property.getName(), (EdmSimpleType<? extends Object>) property.getType(), value));
        } else {
          // complex.
          if (value == null) {
            properties.add(OProperties.complex(property.getName(), (EdmComplexType) property.getType(), null));
          } else {
            Class<?> propType = propertyModel.getPropertyType(property.getName());
            InMemoryComplexTypeInfo<?> typeInfo = findComplexTypeInfoForClass(propType);
            if (typeInfo == null) {
              continue;
            }
            List<OProperty<?>> cprops = new ArrayList<OProperty<?>>();
            addPropertiesFromObject(value, typeInfo.getPropertyModel(), (EdmComplexType) property.getType(), cprops, pathHelper);
            properties.add(OProperties.complex(property.getName(), (EdmComplexType) property.getType(), cprops));
          }
        }
      } else {
        // collection.
        Iterable<?> values = propertyModel.getCollectionValue(obj, property.getName());
        OCollection.Builder<OObject> b = OCollections.newBuilder(property.getType());
        if (values != null) {
          Class<?> propType = propertyModel.getCollectionElementType(property.getName());
          InMemoryComplexTypeInfo<?> typeInfo = property.getType().isSimple() ? null : findComplexTypeInfoForClass(propType);
          if ((!property.getType().isSimple()) && typeInfo == null) {
            continue;
          }
          for (Object v : values) {
            if (property.getType().isSimple()) {
              b.add(OSimpleObjects.create((EdmSimpleType<?>) property.getType(), v));
            } else {
              List<OProperty<?>> cprops = new ArrayList<OProperty<?>>();
              addPropertiesFromObject(v, typeInfo.getPropertyModel(), (EdmComplexType) property.getType(), cprops, pathHelper);
              b.add(OComplexObjects.create((EdmComplexType) property.getType(), cprops));
            }
          }
        }
        properties.add(OProperties.collection(property.getName(),
            // hmmmh...is something is wrong here if I have to create a new EdmCollectionType?
            new EdmCollectionType(EdmProperty.CollectionKind.Collection,
                property.getType()), b.build()));
      }
    }
    dump("done addPropertiesFromObject: " + obj.getClass().getName());
  }

  protected OEntity toOEntity(EdmEntitySet ees, Object obj, PropertyPathHelper pathHelper) {

    InMemoryEntityInfo<?> ei = this.findEntityInfoForClass(obj.getClass()); //  eis.get(ees.getName());
    final List<OLink> links = new ArrayList<OLink>();
    final List<OProperty<?>> properties = new ArrayList<OProperty<?>>();

    Map<String, Object> keyKVPair = new HashMap<String, Object>();
    for (String key : ei.getKeys()) {
      Object keyValue = ei.getPropertyModel().getPropertyValue(obj, key);
      keyKVPair.put(key, keyValue);
    }

    // the entity set being queried may contain objects of subtypes of the entity set's type
    EdmEntityType edmEntityType = (EdmEntityType) this.getMetadata().findEdmEntityType(namespace + "." + ei.getEntityTypeName());

    // "regular" properties
    addPropertiesFromObject(obj, ei.getPropertyModel(), edmEntityType, properties, pathHelper);

    // navigation properties

    for (final EdmNavigationProperty navProp : edmEntityType.getNavigationProperties()) {

      if (!pathHelper.isSelected(navProp.getName())) {
        continue;
      }

      if (!pathHelper.isExpanded(navProp.getName())) {
        // defer
        if (navProp.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
          links.add(OLinks.relatedEntities(null, navProp.getName(), null));
        } else {
          links.add(OLinks.relatedEntity(null, navProp.getName(), null));
        }
      } else {
        // inline
        pathHelper.navigate(navProp.getName());
        if (navProp.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
          List<OEntity> relatedEntities = new ArrayList<OEntity>();

          EdmEntitySet relEntitySet = null;

          for (final Object entity : getRelatedPojos(navProp, obj, ei)) {
            if (relEntitySet == null) {
              InMemoryEntityInfo<?> oei = this.findEntityInfoForClass(entity.getClass());
              relEntitySet = getMetadata().getEdmEntitySet(oei.getEntitySetName());
            }

            relatedEntities.add(toOEntity(relEntitySet, entity, pathHelper));
          }

          // relation and href will be filled in later for atom or json
          links.add(OLinks.relatedEntitiesInline(null, navProp.getName(), null, relatedEntities));
        } else {
          final Object entity = ei.getPropertyModel().getPropertyValue(obj, navProp.getName());
          OEntity relatedEntity = null;

          if (entity != null) {
            InMemoryEntityInfo<?> oei = this.findEntityInfoForClass(entity.getClass());
            EdmEntitySet relEntitySet = getMetadata().getEdmEntitySet(oei.getEntitySetName());
            relatedEntity = toOEntity(relEntitySet, entity, pathHelper);
          }
          links.add(OLinks.relatedEntityInline(null, navProp.getName(), null, relatedEntity));
        }

        pathHelper.popPath();
      }
    }

    return OEntities.create(ees, edmEntityType, OEntityKey.create(keyKVPair), properties, links, obj);
  }

  protected Iterable<?> getRelatedPojos(EdmNavigationProperty navProp, Object srcObject, InMemoryEntityInfo<?> srcInfo) {
    if (navProp.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
      Iterable<?> i = srcInfo.getPropertyModel().getCollectionValue(srcObject, navProp.getName());
      return i == null ? Collections.EMPTY_LIST : i;
    } else {
      // can be null
      return Collections.singletonList(srcInfo.getPropertyModel().getPropertyValue(srcObject, navProp.getName()));
    }
  }

  private static Predicate1<Object> filterToPredicate(final BoolCommonExpression filter, final PropertyModel properties) {
    return new Predicate1<Object>() {
      public boolean apply(Object input) {
        return InMemoryEvaluation.evaluate(filter, input, properties);
      }
    };
  }

  @Override
  public EntitiesResponse getEntities(ODataContext context, String entitySetName, final QueryInfo queryInfo) {

    final RequestContext rc = RequestContext.newBuilder(RequestType.GetEntities)
        .entitySetName(entitySetName)
        .entitySet(getMetadata().getEdmEntitySet(entitySetName))
        .queryInfo(queryInfo)
        .odataContext(context)
        .pathHelper(new PropertyPathHelper(queryInfo)).build();

    final InMemoryEntityInfo<?> ei = eis.get(entitySetName);

    Enumerable<Object> objects = ei.getWithContext == null
        ? Enumerable.create(ei.get.apply()).cast(Object.class)
        : Enumerable.create(ei.getWithContext.apply(rc)).cast(Object.class);

    return getEntitiesResponse(rc, rc.getEntitySet(), objects, ei.getPropertyModel());
  }

  protected EntitiesResponse getEntitiesResponse(final RequestContext rc, final EdmEntitySet targetEntitySet, Enumerable<Object> objects, PropertyModel propertyModel) {
    // apply filter
    final QueryInfo queryInfo = rc.getQueryInfo();
    if (queryInfo != null && queryInfo.filter != null) {
      objects = objects.where(filterToPredicate(queryInfo.filter, propertyModel));
    }

    // compute inlineCount, must be done after applying filter
    Integer inlineCount = null;
    if (queryInfo != null && queryInfo.inlineCount == InlineCount.ALLPAGES) {
      objects = Enumerable.create(objects.toList()); // materialize up front, since we're about to count
      inlineCount = objects.count();
    }

    // apply ordering
    if (queryInfo != null && queryInfo.orderBy != null) {
      objects = orderBy(objects, queryInfo.orderBy, propertyModel);
    }

    // work with oentities
    Enumerable<OEntity> entities = objects.select(new Func1<Object, OEntity>() {
      @Override
      public OEntity apply(Object input) {
        return toOEntity(targetEntitySet, input, rc.getPathHelper());
      }
    });

    // skip records by $skipToken
    if (queryInfo != null && queryInfo.skipToken != null) {
      final Boolean[] skipping = new Boolean[] { true };
      entities = entities.skipWhile(new Predicate1<OEntity>() {
        @Override
        public boolean apply(OEntity input) {
          if (skipping[0]) {
            String inputKey = input.getEntityKey().toKeyString();
            if (queryInfo.skipToken.equals(inputKey)) skipping[0] = false;
            return true;
          }
          return false;
        }
      });
    }

    // skip records by $skip amount
    if (queryInfo != null && queryInfo.skip != null) {
      entities = entities.skip(queryInfo.skip);
    }

    // apply limit
    int limit = this.maxResults;
    if (queryInfo != null && queryInfo.top != null && queryInfo.top < limit) {
      limit = queryInfo.top;
    }
    entities = entities.take(limit + 1);

    // materialize OEntities
    List<OEntity> entitiesList = entities.toList();

    // determine skipToken if necessary
    String skipToken = null;
    if (entitiesList.size() > limit) {
      entitiesList = Enumerable.create(entitiesList).take(limit).toList();
      skipToken = entitiesList.size() == 0 ? null : Enumerable.create(entitiesList).last().getEntityKey().toKeyString();
    }

    return Responses.entities(entitiesList, targetEntitySet, inlineCount, skipToken);

  }

  @Override
  public CountResponse getEntitiesCount(ODataContext context, String entitySetName, final QueryInfo queryInfo) {

    final RequestContext rc = RequestContext.newBuilder(RequestType.GetEntitiesCount)
        .entitySetName(entitySetName)
        .entitySet(getMetadata().getEdmEntitySet(entitySetName))
        .queryInfo(queryInfo)
        .odataContext(context)
        .build();

    final InMemoryEntityInfo<?> ei = eis.get(entitySetName);

    final PropertyPathHelper pathHelper = new PropertyPathHelper(queryInfo);

    Enumerable<Object> objects = ei.getWithContext == null
        ? Enumerable.create(ei.get.apply()).cast(Object.class)
        : Enumerable.create(ei.getWithContext.apply(rc)).cast(Object.class);

    // apply filter
    if (queryInfo != null && queryInfo.filter != null) {
      objects = objects.where(filterToPredicate(queryInfo.filter, ei.properties));
    }

    // inlineCount is not applicable to $count queries
    if (queryInfo != null && queryInfo.inlineCount == InlineCount.ALLPAGES) {
      throw new UnsupportedOperationException("$inlinecount cannot be applied to the resource segment '$count'");
    }

    // ignore ordering for count

    // work with oentities.
    Enumerable<OEntity> entities = objects.select(new Func1<Object, OEntity>() {
      @Override
      public OEntity apply(Object input) {
        return toOEntity(rc.getEntitySet(), input, pathHelper);
      }
    });

    // skipToken is not applicable to $count queries
    if (queryInfo != null && queryInfo.skipToken != null) {
      throw new UnsupportedOperationException("Skip tokens can only be provided for requests that return collections of entities.");
    }

    // skip records by $skip amount
    // http://services.odata.org/Northwind/Northwind.svc/Customers/$count/?$skip=5
    if (queryInfo != null && queryInfo.skip != null) {
      entities = entities.skip(queryInfo.skip);
    }

    // apply $top.  maxResults is not applicable to $count but $top is.
    // http://services.odata.org/Northwind/Northwind.svc/Customers/$count/?$top=55
    int limit = Integer.MAX_VALUE;
    if (queryInfo != null && queryInfo.top != null && queryInfo.top < limit) {
      limit = queryInfo.top;
    }
    entities = entities.take(limit);

    return Responses.count(entities.count());
  }

  private Enumerable<Object> orderBy(Enumerable<Object> iter, List<OrderByExpression> orderBys, final PropertyModel properties) {
    for (final OrderByExpression orderBy : Enumerable.create(orderBys).reverse())
      iter = iter.orderBy(new Comparator<Object>() {
        @SuppressWarnings({ "unchecked", "rawtypes" })
        public int compare(Object o1, Object o2) {
          Comparable lhs = (Comparable) InMemoryEvaluation.evaluate(orderBy.getExpression(), o1, properties);
          Comparable rhs = (Comparable) InMemoryEvaluation.evaluate(orderBy.getExpression(), o2, properties);
          return (orderBy.getDirection() == Direction.ASCENDING ? 1 : -1) * lhs.compareTo(rhs);
        }
      });
    return iter;
  }

  @Override
  public EntityResponse getEntity(ODataContext context, final String entitySetName, final OEntityKey entityKey, final EntityQueryInfo queryInfo) {

    PropertyPathHelper pathHelper = new PropertyPathHelper(queryInfo);

    RequestContext rc = RequestContext.newBuilder(RequestType.GetEntity)
        .entitySetName(entitySetName)
        .entitySet(getMetadata()
            .getEdmEntitySet(entitySetName))
        .entityKey(entityKey)
        .queryInfo(queryInfo)
        .pathHelper(pathHelper)
        .odataContext(context).build();

    final Object rt = getEntityPojo(rc);
    if (rt == null)
      throw new NotFoundException("No entity found in entityset " + entitySetName
          + " for key " + entityKey.toKeyStringWithoutParentheses()
          + " and query info " + queryInfo);

    OEntity oe = toOEntity(rc.getEntitySet(), rt, rc.getPathHelper());

    return Responses.entity(oe);
  }

  @Override
  public void mergeEntity(ODataContext context, String entitySetName, OEntity entity) {
    throw new NotImplementedException();
  }

  @Override
  public void updateEntity(ODataContext context, String entitySetName, OEntity entity) {
    throw new NotImplementedException();
  }

  @Override
  public void deleteEntity(ODataContext context, String entitySetName, OEntityKey entityKey) {
    throw new NotImplementedException();
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntity entity) {
    throw new NotImplementedException();
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
    throw new NotImplementedException();
  }

  @Override
  public BaseResponse getNavProperty(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {

    RequestContext rc = RequestContext.newBuilder(RequestType.GetNavProperty)
        .entitySetName(entitySetName)
        .entitySet(getMetadata().getEdmEntitySet(entitySetName))
        .entityKey(entityKey)
        .navPropName(navProp)
        .queryInfo(queryInfo)
        .pathHelper(new PropertyPathHelper(queryInfo))
        .odataContext(context)
        .build();

    EdmNavigationProperty navProperty = rc.getEntitySet().getType().findNavigationProperty(navProp);
    if (navProperty != null) {
      return getNavProperty(navProperty, rc);
    }

    // not a NavigationProperty:

    EdmProperty edmProperty = rc.getEntitySet().getType().findProperty(navProp);
    if (edmProperty == null)
      throw new NotFoundException("Property " + navProp + " is not found");
    // currently only simple types are supported
    EdmType edmType = edmProperty.getType();

    if (!edmType.isSimple())
      throw new NotImplementedException("Only simple types are supported. Property type is '" + edmType.getFullyQualifiedTypeName() + "'");

    // get property value...
    InMemoryEntityInfo<?> entityInfo = eis.get(entitySetName);
    Object target = getEntityPojo(rc);
    Object propertyValue = entityInfo.properties.getPropertyValue(target, navProp);
    // ... and create OProperty
    OProperty<?> property = OProperties.simple(navProp, (EdmSimpleType<?>) edmType, propertyValue);

    return Responses.property(property);
  }

  protected EdmEntitySet findEntitySetForNavProperty(EdmNavigationProperty navProp) {
    EdmEntityType et = navProp.getToRole().getType();
    // assumes one set per type...
    for (EdmEntitySet set : this.getMetadata().getEntitySets()) {
      if (set.getType().equals(et)) {
        return set;
      }
    }
    return null;
  }

  /**
   * Gets the entity(s) on the target end of a NavigationProperty.
   *
   * @param navProp  the navigation property
   * @param rc  the request context
   * @return a BaseResponse with either a single Entity (can be null) or a set of entities.
   */
  protected BaseResponse getNavProperty(EdmNavigationProperty navProp, RequestContext rc) {

    // First, get the source POJO.
    Object obj = getEntityPojo(rc);
    Iterable relatedPojos = this.getRelatedPojos(navProp, obj, this.findEntityInfoForClass(obj.getClass()));

    EdmEntitySet targetEntitySet = findEntitySetForNavProperty(navProp);

    if (navProp.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
      // apply filter, orderby, etc.
      return getEntitiesResponse(rc, targetEntitySet, Enumerable.create(relatedPojos), findEntityInfoForEntitySet(targetEntitySet.getName()).getPropertyModel());
    } else {
      return Responses.entity(this.toOEntity(targetEntitySet, relatedPojos.iterator().next(), rc.getPathHelper()));
    }
  }

  @Override
  public CountResponse getNavPropertyCount(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    throw new NotImplementedException();
  }

  @Override
  public EntityIdResponse getLinks(ODataContext context, OEntityId sourceEntity, String targetNavProp) {
    throw new NotImplementedException();
  }

  @Override
  public void createLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityId targetEntity) {
    throw new NotImplementedException();
  }

  @Override
  public void updateLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey oldTargetEntityKey, OEntityId newTargetEntity) {
    throw new NotImplementedException();
  }

  @Override
  public void deleteLink(ODataContext context, OEntityId sourceEntity, String targetNavProp, OEntityKey targetEntityKey) {
    throw new NotImplementedException();
  }

  @Override
  public BaseResponse callFunction(ODataContext context, EdmFunctionImport name, java.util.Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
    throw new NotImplementedException();
  }

  @Override
  public <TExtension extends OExtension<ODataProducer>> TExtension findExtension(Class<TExtension> clazz) {
    return null;
  }

  public static class RequestContext {

    public enum RequestType {
      GetEntity, GetEntities, GetEntitiesCount, GetNavProperty
    };

    public final RequestType requestType;
    private final String entitySetName;
    private EdmEntitySet entitySet;
    private final String navPropName;
    private final OEntityKey entityKey;
    private final QueryInfo queryInfo;
    private final PropertyPathHelper pathHelper;
    private ODataContext odataContext;

    public RequestType getRequestType() {
      return requestType;
    }

    public String getEntitySetName() {
      return entitySetName;
    }

    public EdmEntitySet getEntitySet() {
      return entitySet;
    }

    public String getNavPropName() {
      return navPropName;
    }

    public OEntityKey getEntityKey() {
      return entityKey;
    }

    public QueryInfo getQueryInfo() {
      return queryInfo;
    }

    public PropertyPathHelper getPathHelper() {
      return pathHelper;
    }

    public ODataContext getODataContext() {
      return odataContext;
    }

    public static Builder newBuilder(RequestType requestType) {
      return new Builder().requestType(requestType);
    }

    public static class Builder {

      private RequestType requestType;
      private String entitySetName;
      private EdmEntitySet entitySet;
      private String navPropName;
      private OEntityKey entityKey;
      private QueryInfo queryInfo;
      private PropertyPathHelper pathHelper;
      private ODataContext odataContext;

      public Builder requestType(RequestType value) {
        this.requestType = value;
        return this;
      }

      public Builder entitySetName(String value) {
        this.entitySetName = value;
        return this;
      }

      public Builder entitySet(EdmEntitySet value) {
        this.entitySet = value;
        return this;
      }

      public Builder navPropName(String value) {
        this.navPropName = value;
        return this;
      }

      public Builder entityKey(OEntityKey value) {
        this.entityKey = value;
        return this;
      }

      public Builder queryInfo(QueryInfo value) {
        this.queryInfo = value;
        return this;
      }

      public Builder pathHelper(PropertyPathHelper value) {
        this.pathHelper = value;
        return this;
      }

      public Builder odataContext(ODataContext value) {
        this.odataContext = value;
        return this;
      }

      public RequestContext build() {
        return new RequestContext(requestType, entitySetName, entitySet, navPropName, entityKey, queryInfo, pathHelper, odataContext);
      }
    }

    private RequestContext(RequestType requestType, String entitySetName, EdmEntitySet entitySet,
        String navPropName, OEntityKey entityKey, QueryInfo queryInfo, PropertyPathHelper pathHelper,
        ODataContext odataContext) {
      this.requestType = requestType;
      this.entitySetName = entitySetName;
      this.entitySet = entitySet;
      this.navPropName = navPropName;
      this.entityKey = entityKey;
      this.queryInfo = queryInfo;
      this.pathHelper = pathHelper;
      this.odataContext = odataContext;
    }
  }

  /**
   * Given an entity set and an entity key, returns the pojo that is that entity instance.
   * The default implementation iterates over the entire set of pojos to find the
   * desired instance.
   *
   * @param rc  the current ReqeustContext, may be valuable to the ei.getWithContext impl
   * @return the pojo
   */
  @SuppressWarnings("unchecked")
  protected Object getEntityPojo(final RequestContext rc) {
    final InMemoryEntityInfo<?> ei = eis.get(rc.getEntitySetName());

    final String[] keyList = ei.keys;

    Iterable<Object> iter = ei.getWithContext == null ? ((Iterable<Object>) ei.get.apply())
        : ((Iterable<Object>) ei.getWithContext.apply(rc));

    final Object rt = Enumerable.create(iter).firstOrNull(new Predicate1<Object>() {
      public boolean apply(Object input) {
        HashMap<String, Object> idObjectMap = ei.id.apply(input);

        if (keyList.length == 1) {
          Object idValue = rc.getEntityKey().asSingleValue();
          return idObjectMap.get(keyList[0]).equals(idValue);
        } else if (keyList.length > 1) {
          for (String key : keyList) {
            Object curValue = null;
            Iterator<OProperty<?>> keyProps = rc.getEntityKey().asComplexProperties().iterator();
            while (keyProps.hasNext()) {
              OProperty<?> keyProp = keyProps.next();
              if (keyProp.getName().equalsIgnoreCase(key)) {
                curValue = keyProp.getValue();
              }
            }
            if (curValue == null) {
              return false;
            } else if (!idObjectMap.get(key).equals(curValue)) {
              return false;
            }
          }
          return true;
        } else {
          return false;
        }
      }
    });
    return rt;
  }

  private enum TriggerType {
    Before, After
  };

  protected void fireUnmarshalEvent(Object pojo, OStructuralObject sobj, TriggerType ttype)
      throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    try {
      Method m = pojo.getClass().getMethod(ttype == TriggerType.Before ? "beforeOEntityUnmarshal" : "afterOEntityUnmarshal", OStructuralObject.class);
      if (m != null) {
        m.invoke(pojo, sobj);
      }
    } catch (NoSuchMethodException ex) {}
  }

  /**
   * Transforms an OComplexObject into a POJO of the given class
   */
  public <T> T toPojo(OComplexObject entity, Class<T> pojoClass) throws InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    InMemoryComplexTypeInfo<?> e = this.findComplexTypeInfoForClass(pojoClass);

    T pojo = fillInPojo(entity, this.getMetadata().findEdmComplexType(
        this.namespace + "." + e.getTypeName()), e.getPropertyModel(), pojoClass);

    fireUnmarshalEvent(pojo, entity, TriggerType.After);
    return pojo;
  }

  /**
   * Populates a new POJO instance of type pojoClass using data from the given structural object.
   */
  protected <T> T fillInPojo(OStructuralObject sobj, EdmStructuralType stype, PropertyModel propertyModel,
      Class<T> pojoClass) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    T pojo = pojoClass.newInstance();
    fireUnmarshalEvent(pojo, sobj, TriggerType.Before);

    for (Iterator<EdmProperty> it = stype.getProperties().iterator(); it.hasNext();) {
      EdmProperty property = it.next();
      Object value = null;
      try {
        value = sobj.getProperty(property.getName()).getValue();
      } catch (Exception ex) {
        // property not define on object
        if (property.isNullable()) {
          continue;
        } else {
          throw new RuntimeException("missing required property " + property.getName());
        }
      }

      if (property.getCollectionKind() == EdmProperty.CollectionKind.NONE) {
        if (property.getType().isSimple()) {
          // call the setter.
          propertyModel.setPropertyValue(pojo, property.getName(), value);
        } else {
          // complex.
          // hmmh, value is a Collection<OProperty<?>>...why is it not an OComplexObject.

          propertyModel.setPropertyValue(
              pojo,
              property.getName(),
              value == null
                  ? null
                  : toPojo(
                      OComplexObjects.create((EdmComplexType) property.getType(), (List<OProperty<?>>) value),
                      propertyModel.getPropertyType(property.getName())));
        }
      } else {
        // collection.
        OCollection<? extends OObject> collection = (OCollection<? extends OObject>) value;
        List<Object> pojos = new ArrayList<Object>();
        for (OObject item : collection) {
          if (collection.getType().isSimple()) {
            pojos.add(((OSimpleObject<?>) item).getValue());
          } else {
            // turn OComplexObject into a pojo
            pojos.add(toPojo((OComplexObject) item, propertyModel.getCollectionElementType(property.getName())));
          }
        }
        propertyModel.setCollectionValue(pojo, property.getName(), pojos);
      }
    }

    return pojo;
  }

  /*
   * Design note:
   * toPojo is functionality that is useful on both the producer and consumer side.
   * I'm putting it in the producer class for now although I suspect there is a
   * more elegant design that factors out POJO Classes and PropertyModels into
   * some kind of "PojoModelDefinition" class.  The producer side would then
   * layer and extended definition that defined how the PojoModelDefinition maps
   * to entity sets and such.
   *
   * with all that said, hopefully this start is useful.  I'm going to use it on
   * our producer side for now to handle createEntity payloads.
   */

  /**
   * Transforms the given entity into a POJO of type pojoClass.
   */
  public <T> T toPojo(OEntity entity, Class<T> pojoClass) throws InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {

    InMemoryEntityInfo<?> e = this.findEntityInfoForClass(pojoClass);

    // so, how is this going to work?
    // we have the PropertyModel available.  We can lookup the EdmStructuredType if necessary.

    EdmEntitySet entitySet = this.getMetadata().findEdmEntitySet(e.getEntitySetName());

    T pojo = fillInPojo(entity, entitySet.getType(), e.getPropertyModel(), pojoClass);

    // nav props
    for (Iterator<EdmNavigationProperty> it = entitySet.getType().getNavigationProperties().iterator(); it.hasNext();) {
      EdmNavigationProperty np = it.next();
      OLink link = null;
      try {
        link = entity.getLink(np.getName(), OLink.class);
      } catch (IllegalArgumentException nolinkex) {
        continue;
      }

      if (link.isInline()) {
        if (link.isCollection()) {
          List<Object> pojos = new ArrayList<Object>();
          for (OEntity relatedEntity : link.getRelatedEntities()) {
            pojos.add(toPojo(relatedEntity, e.getPropertyModel().getCollectionElementType(np.getName())));
          }
          e.getPropertyModel().setCollectionValue(pojo, np.getName(), pojos);
        } else {
          e.getPropertyModel().setPropertyValue(pojo, np.getName(),
              toPojo(link.getRelatedEntity(), e.getPropertyModel().getPropertyType(np.getName())));
        }
      } // else ignore deferred links.
    }

    fireUnmarshalEvent(pojo, entity, TriggerType.After);
    return pojo;
  }
}
