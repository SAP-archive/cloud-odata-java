package org.odata4j.producer.jpa;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type.PersistenceType;

import org.core4j.Enumerable;
import org.core4j.Predicate1;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityId;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OExtension;
import org.odata4j.core.OFunctionParameter;
import org.odata4j.core.OLink;
import org.odata4j.core.OProperty;
import org.odata4j.core.ORelatedEntitiesLinkInline;
import org.odata4j.core.ORelatedEntityLink;
import org.odata4j.core.ORelatedEntityLinkInline;
import org.odata4j.core.Throwables;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmDecorator;
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.exceptions.BadRequestException;
import org.odata4j.exceptions.NotImplementedException;
import org.odata4j.expression.EntitySimpleProperty;
import org.odata4j.internal.TypeConverter;
import org.odata4j.producer.BaseResponse;
import org.odata4j.producer.CountResponse;
import org.odata4j.producer.EntitiesResponse;
import org.odata4j.producer.EntityIdResponse;
import org.odata4j.producer.EntityQueryInfo;
import org.odata4j.producer.EntityResponse;
import org.odata4j.producer.ODataContext;
import org.odata4j.producer.ODataProducer;
import org.odata4j.producer.QueryInfo;
import org.odata4j.producer.Responses;
import org.odata4j.producer.edm.MetadataProducer;

public class JPAProducer implements ODataProducer {

  public enum CommandType {
    CreateEntity,
    GetEntities,
    GetEntity,
    CreateAndLink,
    DeleteEntity,
    MergeEntity,
    UpdateEntity,
    GetLinks,
    GetCount
  };

  private final EntityManagerFactory emf;
  private final EdmDataServices metadata;
  private final int maxResults;
  private final MetadataProducer metadataProducer;
  private Command createEntityCommand;
  private Command createAndLinkCommand;
  private Command getEntitiesCommand;
  private Command getEntityCommand;
  private Command deleteEntityCommand;
  private Command mergeEntityCommand;
  private Command updateEntityCommand;
  private Command getLinksCommand;
  private Command getCountCommand;
  private JPAProducerBehavior producerBehavior;

  public JPAProducer(
      EntityManagerFactory emf,
      String namespace,
      int maxResults) {
    this(emf, new JPAEdmGenerator(emf, namespace).generateEdm(null).build(), maxResults, null, null);
  }

  public JPAProducer(
      EntityManagerFactory emf,
      EdmDataServices metadata,
      int maxResults) {
    this(emf, metadata, maxResults, null, null);
  }

  public JPAProducer(
      EntityManagerFactory emf,
      EdmDataServices metadata,
      int maxResults,
      EdmDecorator metadataDecorator) {
    this(emf, metadata, maxResults, metadataDecorator, null);
  }

  public JPAProducer(
      EntityManagerFactory emf,
      EdmDataServices metadata,
      int maxResults,
      EdmDecorator metadataDecorator,
      JPAProducerBehavior producerBehavior) {

    this.emf = emf;
    this.maxResults = maxResults;
    this.metadata = metadata;
    this.metadataProducer = new MetadataProducer(this, metadataDecorator);
    this.producerBehavior = producerBehavior;

    initCommandChains();
  }

  protected void initCommandChains() {
    List<Command> commands = new ArrayList<Command>();
    /* query processors */
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // parse generate the JPQL query
    commands.add(new GenerateJPQLCommand());
    // execute the JPQL query
    commands.add(new ExecuteJPQLQueryCommand(maxResults));
    // convert the query result to response
    commands.add(new SetResponseCommand());
    getEntitiesCommand = createChain(CommandType.GetEntities, commands);

    /* initialize the create processors */
    commands = new ArrayList<Command>();
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // begin a transaction
    commands.add(new BeginTransactionCommand());
    // convert the given OEntity to a JPAEntity
    commands.add(new OEntityToJPAEntityCommand(true));
    // persist the JPAEntity
    commands.add(new PersistJPAEntityCommand());
    // commit the transaction
    commands.add(new CommitTransactionCommand());
    // reread the JPAEntity if necessary
    commands.add(new ReReadJPAEntityCommand());
    // convert the JPAEntity to OEntity and set the response
    commands.add(new SetResponseCommand());
    createEntityCommand = createChain(CommandType.CreateEntity, commands);

    /* create and link processors */
    commands = new ArrayList<Command>();
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // begin a transaction
    commands.add(new BeginTransactionCommand());
    // get the entity we want the new entity add to (parent entity)
    commands.add(new GetEntityCommand());
    // convert the given new OEntity to a new JPAEntity
    commands.add(new OEntityToJPAEntityCommand(JPAContext.EntityAccessor.OTHER, true));
    // add the new JPAEntity to the parent entity
    commands.add(new CreateAndLinkCommand());
    // commit the transaction
    commands.add(new CommitTransactionCommand());
    // convert the JPAEntity to OEntity and set the response
    commands.add(new SetResponseCommand(JPAContext.EntityAccessor.OTHER));
    createAndLinkCommand = createChain(CommandType.CreateAndLink, commands);

    /* get entity processors */
    commands = new ArrayList<Command>();
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // get the requested JPAEntity
    commands.add(new GetEntityCommand());
    // convert the JPAEntity to OEntity and set the response
    commands.add(new SetResponseCommand());
    getEntityCommand = createChain(CommandType.GetEntity, commands);

    /* delete entity processors */
    commands = new ArrayList<Command>();
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // begin transaction
    commands.add(new BeginTransactionCommand());
    // get the JPAEntity to delete
    commands.add(new GetEntityCommand());
    // delete the JPAEntity
    commands.add(new DeleteEntityCommand());
    // commit the transaction
    commands.add(new CommitTransactionCommand());
    // the response stays empty
    deleteEntityCommand = createChain(CommandType.DeleteEntity, commands);

    /* merge entity processors */
    commands = new ArrayList<Command>();
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // begin transaction
    commands.add(new BeginTransactionCommand());
    // get the JPAEntity to delete
    commands.add(new GetEntityCommand());
    // delete the JPAEntity
    commands.add(new MergeEntityCommand());
    // commit the transaction
    commands.add(new CommitTransactionCommand());
    // the response stays empty
    mergeEntityCommand = createChain(CommandType.MergeEntity, commands);

    /* update entity processors */
    commands = new ArrayList<Command>();
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // begin transaction
    commands.add(new BeginTransactionCommand());
    // get the JPAEntity to delete
    commands.add(new OEntityToJPAEntityCommand(true));
    // delete the JPAEntity
    commands.add(new UpdateEntityCommand());
    // commit the transaction
    commands.add(new CommitTransactionCommand());
    // the response stays empty
    updateEntityCommand = createChain(CommandType.UpdateEntity, commands);

    /* get links command */
    commands = new ArrayList<Command>();
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // parse generate the JPQL query
    commands.add(new GenerateJPQLCommand());
    // execute the JPQL query
    commands.add(new ExecuteJPQLQueryCommand(maxResults));
    // convert the result to a response and set it
    commands.add(new SetResponseCommand());
    getLinksCommand = createChain(CommandType.GetLinks, commands);

    /* get entities count processors */
    commands = new ArrayList<Command>();
    // create an EntityManager
    commands.add(new ValidateCountRequestProcessor());
    // create an EntityManager
    commands.add(new EntityManagerCommand(emf));
    // parse generate the JPQL query
    commands.add(new GenerateJPQLCommand(true));
    // execute the JPQL query
    commands.add(new ExecuteCountQueryCommand());
    // set the count into the response
    commands.add(new SetResponseCommand());
    getCountCommand = createChain(CommandType.GetCount, commands);
  }

  private Command createChain(CommandType type, List<Command> commands) {
    if (producerBehavior != null) {
      return new Chain(producerBehavior.modify(type, commands));
    } else {
      return new Chain(commands);
    }
  }

  @Override
  public EdmDataServices getMetadata() {
    return metadata;
  }

  @Override
  public MetadataProducer getMetadataProducer() {
    return this.metadataProducer;
  }

  @Override
  public EntitiesResponse getEntities(ODataContext context, String entitySetName, QueryInfo queryInfo) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName, queryInfo);
    getEntitiesCommand.execute(jpaContext);
    return (EntitiesResponse) jpaContext.getResponse();
  }

  @Override
  public EntityResponse getEntity(ODataContext context, String entitySetName, OEntityKey entityKey, EntityQueryInfo queryInfo) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName, entityKey, null,
        queryInfo);
    getEntityCommand.execute(jpaContext);
    return (EntityResponse) jpaContext.getResponse();
  }

  @Override
  public BaseResponse getNavProperty(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName, entityKey,
        navProp, queryInfo);
    getEntitiesCommand.execute(jpaContext);
    return jpaContext.getResponse();
  }

  @Override
  public void close() {}

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntity entity) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName, null, entity);
    createEntityCommand.execute(jpaContext);
    return (EntityResponse) jpaContext.getResponse();
  }

  @Override
  public EntityResponse createEntity(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, OEntity entity) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName, entityKey,
        navProp, entity);
    createAndLinkCommand.execute(jpaContext);
    return (EntityResponse) jpaContext.getResponse();
  }

  @Override
  public void deleteEntity(ODataContext context, String entitySetName, OEntityKey entityKey) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName, entityKey, null);
    deleteEntityCommand.execute(jpaContext);
  }

  @Override
  public void mergeEntity(ODataContext context, String entitySetName, OEntity entity) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName,
        entity.getEntityKey(), entity);
    mergeEntityCommand.execute(jpaContext);
  }

  @Override
  public void updateEntity(ODataContext context, String entitySetName, OEntity entity) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName,
        entity.getEntityKey(), entity);
    updateEntityCommand.execute(jpaContext);
  }

  @Override
  public EntityIdResponse getLinks(ODataContext context, OEntityId sourceEntity, String targetNavProp) {
    JPAContext jpaContext = new JPAContext(metadata,
        sourceEntity.getEntitySetName(),
        sourceEntity.getEntityKey(), targetNavProp, (QueryInfo) null);
    getLinksCommand.execute(jpaContext);

    BaseResponse r = jpaContext.getResponse();
    if (r instanceof EntitiesResponse) {
      EntitiesResponse er = (EntitiesResponse) r;
      return Responses.multipleIds(er.getEntities());
    }
    if (r instanceof EntityResponse) {
      EntityResponse er = (EntityResponse) r;
      return Responses.singleId(er.getEntity());
    }
    if (r instanceof EntitiesResponse) {
      EntitiesResponse er = (EntitiesResponse) r;
      return Responses.multipleIds(er.getEntities());
    }
    if (r instanceof EntityResponse) {
      EntityResponse er = (EntityResponse) r;
      return Responses.singleId(er.getEntity());
    }
    throw new NotImplementedException(sourceEntity + " " + targetNavProp);
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
  public BaseResponse callFunction(ODataContext context, EdmFunctionImport name,
      Map<String, OFunctionParameter> params, QueryInfo queryInfo) {
    return null;
  }

  @Override
  public CountResponse getEntitiesCount(ODataContext context, String entitySetName, QueryInfo queryInfo) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName, queryInfo);
    getCountCommand.execute(jpaContext);
    return (CountResponse) jpaContext.getResponse();
  }

  @Override
  public CountResponse getNavPropertyCount(ODataContext context, String entitySetName, OEntityKey entityKey, String navProp, QueryInfo queryInfo) {
    JPAContext jpaContext = new JPAContext(metadata, entitySetName, entityKey,
        navProp, queryInfo);
    getCountCommand.execute(jpaContext);
    return (CountResponse) jpaContext.getResponse();
  }

  @Override
  public <TExtension extends OExtension<ODataProducer>> TExtension findExtension(Class<TExtension> clazz) {
    return null;
  }

  /**** utility functions ***/

  static void applyOProperties(EntityManager em,
      ManagedType<?> jpaManagedType, Collection<OProperty<?>> properties,
      Object jpaEntity) {

    for (OProperty<?> prop : properties) {
      boolean found = false;
      if (jpaManagedType instanceof EntityType) {
        EntityType<?> jpaEntityType = (EntityType<?>) jpaManagedType;
        if (jpaEntityType.getIdType().getPersistenceType() == PersistenceType.EMBEDDABLE) {
          EmbeddableType<?> et = (EmbeddableType<?>) jpaEntityType
              .getIdType();

          for (Attribute<?, ?> idAtt : et.getAttributes()) {

            if (idAtt.getName().equals(prop.getName())) {

              Object idValue = JPAMember.create(
                  jpaEntityType.getId(et.getJavaType()),
                  jpaEntity).get();

              setAttribute(idAtt, prop, idValue);
              found = true;
              break;
            }
          }
        }
      }
      if (found)
        continue;
      Attribute<?, ?> att = jpaManagedType.getAttribute(prop.getName());
      setAttribute(att, prop, jpaEntity);
    }
  }

  static Object coercePropertyValue(OProperty<?> prop, Class<?> javaType) {
    Object value = prop.getValue();
    try {
      return TypeConverter.convert(value, javaType);
    } catch (UnsupportedOperationException ex) {
      // let java complain
      return value;
    }
  }

  static EntityType<?> getJPAEntityType(EntityManager em,
      String jpaEntityTypeName) {

    for (EntityType<?> et : em.getMetamodel().getEntities()) {
      if (JPAEdmGenerator.getEntitySetName(et).equals(jpaEntityTypeName)) {
        return et;
      }
    }

    throw new RuntimeException("JPA Entity type " + jpaEntityTypeName
        + " not found");
  }

  @SuppressWarnings("unchecked")
  static <T> T newInstance(Class<?> javaType) {
    try {
      if (javaType.equals(Collection.class))
        javaType = HashSet.class;
      Constructor<?> ctor = javaType.getDeclaredConstructor();
      ctor.setAccessible(true);
      return (T) ctor.newInstance();
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  static void setAttribute(Attribute<?, ?> att, OProperty<?> prop,
      Object target) {
    JPAMember attMember = JPAMember.create(att, target);
    Object value = coercePropertyValue(prop, attMember.getJavaType());
    attMember.set(value);
  }

  static Object typeSafeEntityKey(
      EntityManager em,
      EntityType<?> jpaEntityType,
      OEntityKey entityKey) {

    if (entityKey != null
        && jpaEntityType.getIdType().getPersistenceType() == PersistenceType.EMBEDDABLE) {
      Object id = newInstance(jpaEntityType.getIdType().getJavaType());
      applyOProperties(
          em,
          em.getMetamodel().embeddable(
              jpaEntityType.getIdType().getJavaType()),
          entityKey.asComplexProperties(), id);
      return id;
    }

    Class<?> javaType = jpaEntityType.getIdType().getJavaType();

    try {
      return TypeConverter.convert(entityKey == null ? null : entityKey.asSingleValue(), javaType);
    } catch (UnsupportedOperationException e) {
      throw new BadRequestException("Invalid key type", e);
    } catch (IllegalArgumentException e) {
      throw new BadRequestException("Invalid key value", e);
    }
  }

  @SuppressWarnings("unchecked")
  static void applyOLinks(EntityManager em, EntityType<?> jpaEntityType,
      List<OLink> links, Object jpaEntity) {
    if (links == null)
      return;

    for (final OLink link : links) {
      String[] propNameSplit = link.getRelation().split("/");
      String propName = propNameSplit[propNameSplit.length - 1];

      if (link instanceof ORelatedEntitiesLinkInline) {
        PluralAttribute<?, ?, ?> att = (PluralAttribute<?, ?, ?>) jpaEntityType
            .getAttribute(propName);
        JPAMember member = JPAMember.create(att, jpaEntity);

        EntityType<?> collJpaEntityType = (EntityType<?>) att
            .getElementType();

        OneToMany oneToMany = member.getAnnotation(OneToMany.class);
        boolean hasSingularBackRef = oneToMany != null
            && oneToMany.mappedBy() != null
            && !oneToMany.mappedBy().isEmpty();
        boolean cascade = oneToMany != null && oneToMany.cascade() != null
            ? Enumerable.create(oneToMany.cascade()).any(new Predicate1<CascadeType>() {
              @Override
              public boolean apply(CascadeType input) {
                return input == CascadeType.ALL || input == CascadeType.PERSIST;
              }
            })
            : false;

        ManyToMany manyToMany = member.getAnnotation(ManyToMany.class);

        Collection<Object> coll = member.get();
        if (coll == null) {
          coll = (Collection<Object>) newInstance(member
              .getJavaType());
          member.set(coll);
        }
        for (OEntity oentity : ((ORelatedEntitiesLinkInline) link)
            .getRelatedEntities()) {
          Object collJpaEntity = createNewJPAEntity(em,
              collJpaEntityType, oentity, true);
          if (hasSingularBackRef) {
            JPAMember backRef = JPAMember.create(collJpaEntityType
                .getAttribute(oneToMany.mappedBy()),
                collJpaEntity);
            backRef.set(jpaEntity);
          }
          if (manyToMany != null) {
            Attribute<?, ?> other = null;
            if (manyToMany.mappedBy() != null
                && !manyToMany.mappedBy().isEmpty())
              other = collJpaEntityType.getAttribute(manyToMany
                  .mappedBy());
            else {
              for (Attribute<?, ?> att2 : collJpaEntityType
                  .getAttributes()) {
                if (att2.isCollection()
                    && JPAMember
                        .create(att2, null)
                        .getAnnotation(ManyToMany.class) != null) {
                  CollectionAttribute<?, ?> ca = (CollectionAttribute<?, ?>) att2;
                  if (ca.getElementType().equals(
                      jpaEntityType)) {
                    other = ca;
                    break;
                  }
                }
              }
            }

            if (other == null)
              throw new RuntimeException(
                  "Could not find other side of many-to-many relationship");

            JPAMember backRef = JPAMember.create(other,
                collJpaEntity);
            Collection<Object> coll2 = backRef.get();
            if (coll2 == null) {
              coll2 = newInstance(backRef.getJavaType());
              backRef.set(coll2);
            }
            coll2.add(jpaEntity);
          }

          if (!cascade) {
            em.persist(collJpaEntity);
          }
          coll.add(collJpaEntity);
        }

      } else if (link instanceof ORelatedEntityLinkInline) {
        SingularAttribute<?, ?> att = jpaEntityType
            .getSingularAttribute(propName);
        JPAMember member = JPAMember.create(att, jpaEntity);

        OneToOne oneToOne = member.getAnnotation(OneToOne.class);
        boolean cascade = oneToOne != null && oneToOne.cascade() != null
            ? Enumerable.create(oneToOne.cascade()).any(new Predicate1<CascadeType>() {
              @Override
              public boolean apply(CascadeType input) {
                return input == CascadeType.ALL || input == CascadeType.PERSIST;
              }
            })
            : false;

        EntityType<?> relJpaEntityType = (EntityType<?>) att.getType();
        Object relJpaEntity = createNewJPAEntity(em, relJpaEntityType,
            ((ORelatedEntityLinkInline) link).getRelatedEntity(),
            true);

        if (!cascade) {
          em.persist(relJpaEntity);
        }

        member.set(relJpaEntity);
      } else if (link instanceof ORelatedEntityLink) {

        // look up the linked entity, and set the member value
        SingularAttribute<?, ?> att = jpaEntityType
            .getSingularAttribute(propName);
        JPAMember member = JPAMember.create(att, jpaEntity);

        EntityType<?> relJpaEntityType = (EntityType<?>) att.getType();
        Object key = typeSafeEntityKey(
            em,
            relJpaEntityType,
            OEntityKey.parse(link.getHref().substring(
                link.getHref().indexOf('('))));
        Object relEntity = em.find(relJpaEntityType.getJavaType(), key);

        member.set(relEntity);

        // set corresponding property (if there is one)
        JoinColumn joinColumn = member.getAnnotation(JoinColumn.class);
        ManyToOne manyToOne = member.getAnnotation(ManyToOne.class);
        if (joinColumn != null && manyToOne != null) {
          String columnName = joinColumn.name();
          JPAMember m = JPAMember.findByColumn(jpaEntityType,
              columnName, jpaEntity);
          if (m != null)
            m.set(key);

        }

      } else {
        throw new UnsupportedOperationException(
            "binding the new entity to many entities is not supported");
      }
    }
  }

  static Object createNewJPAEntity(
      EntityManager em,
      EntityType<?> jpaEntityType,
      OEntity oEntity,
      boolean withLinks) {

    Object jpaEntity = newInstance(jpaEntityType.getJavaType());

    if (jpaEntityType.getIdType().getPersistenceType() == PersistenceType.EMBEDDABLE) {
      EmbeddableType<?> et = (EmbeddableType<?>) jpaEntityType
          .getIdType();

      JPAMember idMember = JPAMember.create(
          jpaEntityType.getId(et.getJavaType()), jpaEntity);
      Object idValue = newInstance(et.getJavaType());
      idMember.set(idValue);
    }

    applyOProperties(em, jpaEntityType, oEntity.getProperties(), jpaEntity);
    if (withLinks)
      applyOLinks(em, jpaEntityType, oEntity.getLinks(), jpaEntity);

    return jpaEntity;
  }

  static boolean isSelected(String name, List<EntitySimpleProperty> select) {

    if (select != null && !select.isEmpty()) {
      for (EntitySimpleProperty prop : select) {
        String sname = prop.getPropertyName();
        if (name.equals(sname)) {
          return true;
        }
      }

      return false;
    }

    return true;
  }

}
