package org.odata4j.producer.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;

import org.core4j.Enumerable;
import org.core4j.Func1;
import org.core4j.Predicate1;
import org.odata4j.core.OEntities;
import org.odata4j.core.OEntity;
import org.odata4j.core.OEntityKey;
import org.odata4j.core.OLink;
import org.odata4j.core.OLinks;
import org.odata4j.core.OProperties;
import org.odata4j.core.OProperty;
import org.odata4j.core.Throwables;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.expression.EntitySimpleProperty;
import org.odata4j.expression.Expression;
import org.odata4j.producer.Responses;

public class SetResponseCommand implements Command {

  private JPAContext.EntityAccessor accessor;

  public SetResponseCommand() {
    this(JPAContext.EntityAccessor.ENTITY);
  }

  public SetResponseCommand(JPAContext.EntityAccessor accessor) {
    this.accessor = accessor;
  }

  @Override
  public boolean execute(final JPAContext context) {

    if (context.getResult() instanceof EntityResult) {
      EntityResult result = (EntityResult) context.getResult();

      OEntity oentity = makeEntity(context, result.getEntity());
      context.setResponse(Responses.entity(oentity));

    } else if (context.getResult() instanceof EntitiesResult) {

      EntitiesResult result = (EntitiesResult) context.getResult();
      List<OEntity> entities = Enumerable.create(result.getEntities())
          .select(new Func1<Object, OEntity>() {
            public OEntity apply(final Object jpaEntity) {
              return makeEntity(context, jpaEntity);
            }
          }).toList();

      //  TODO create the skip token based on the jpaEntity and
      //  move this back to ExecuteJPQLQueryCommand
      String skipToken = null;
      if (result.createSkipToken()) {
        skipToken = JPASkipToken.create(context.getQueryInfo() == null
            ? null
            : context.getQueryInfo().orderBy,
            Enumerable.create(entities).last());
      }

      context.setResponse(Responses.entities(entities, context.getEntity()
          .getEdmEntitySet(), result.getInlineCount(), skipToken));

    } else if (context.getResult() instanceof PropertyResult) {

      PropertyResult<?> result = (PropertyResult<?>) context.getResult();
      OProperty<?> op = OProperties.simple(result.getName(),
          result.getType(), result.getValue());
      context.setResponse(Responses.property(op));

    } else if (context.getResult() instanceof CountResult) {

      CountResult result = (CountResult) context.getResult();
      context.setResponse(Responses.count(result.getCount()));

    }

    return false;
  }

  private OEntity makeEntity(JPAContext context, Object jpaEntity) {
    return jpaEntityToOEntity(
        context.getMetadata(),
        accessor.getEntity(context).getEdmEntitySet(),
        accessor.getEntity(context).getJPAEntityType(),
        jpaEntity,
        context.getQueryInfo() == null
            ? null
            : context.getQueryInfo().expand,
        context.getQueryInfo() == null
            ? null
            : context.getQueryInfo().select);
  }

  private OEntity jpaEntityToOEntity(
      EdmDataServices metadata,
      EdmEntitySet ees,
      EntityType<?> entityType,
      Object jpaEntity,
      List<EntitySimpleProperty> expand,
      List<EntitySimpleProperty> select) {

    List<OProperty<?>> properties = new ArrayList<OProperty<?>>();
    List<OLink> links = new ArrayList<OLink>();

    try {
      SingularAttribute<?, ?> idAtt = JPAEdmGenerator.getIdAttribute(entityType);
      boolean hasEmbeddedCompositeKey =
          idAtt.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED;

      // get properties
      for (EdmProperty ep : ees.getType().getProperties()) {

        if (!JPAProducer.isSelected(ep.getName(), select)) {
          continue;
        }

        // we have a embedded composite key and we want a property from
        // that key
        if (hasEmbeddedCompositeKey && ees.getType().getKeys().contains(ep.getName())) {
          Object value = SetResponseCommand.getIdValue(jpaEntity, idAtt, ep.getName());

          properties.add(OProperties.simple(
              ep.getName(),
              (EdmSimpleType<?>) ep.getType(),
              value));

        } else {
          // get the simple attribute
          Attribute<?, ?> att = entityType.getAttribute(ep.getName());
          JPAMember member = JPAMember.create(att, jpaEntity);
          Object value = member.get();

          if (ep.getType().isSimple()) {
            properties.add(OProperties.simple(
                ep.getName(),
                (EdmSimpleType<?>) ep.getType(),
                value));
          } else {
            // TODO handle embedded entities
          }
        }
      }

      // get the collections if necessary
      if (expand != null && !expand.isEmpty()) {

        HashMap<String, List<EntitySimpleProperty>> expandedProps = new HashMap<String, List<EntitySimpleProperty>>();

        //process all the expanded properties and add them to map
        for (final EntitySimpleProperty propPath : expand) {
          // split the property path into the first and remaining
          // parts
          String[] props = propPath.getPropertyName().split("/", 2);
          String prop = props[0];
          String remainingPropPath = props.length > 1 ? props[1] : null;
          //if link is already set to be expanded, add other remaining prop path to the list
          if (expandedProps.containsKey(prop)) {
            if (remainingPropPath != null) {
              List<EntitySimpleProperty> remainingPropPaths = expandedProps.get(prop);
              remainingPropPaths.add(Expression.simpleProperty(remainingPropPath));
            }
          } else {
            List<EntitySimpleProperty> remainingPropPaths = new ArrayList<EntitySimpleProperty>();
            if (remainingPropPath != null)
              remainingPropPaths.add(Expression.simpleProperty(remainingPropPath));
            expandedProps.put(prop, remainingPropPaths);
          }
        }

        for (final String prop : expandedProps.keySet()) {
          List<EntitySimpleProperty> remainingPropPath = expandedProps.get(prop);

          Attribute<?, ?> att = entityType.getAttribute(prop);
          if (att.getPersistentAttributeType() == PersistentAttributeType.ONE_TO_MANY
              || att.getPersistentAttributeType() == PersistentAttributeType.MANY_TO_MANY) {

            Collection<?> value = JPAMember.create(att, jpaEntity).get();

            List<OEntity> relatedEntities = new ArrayList<OEntity>();
            for (Object relatedEntity : value) {
              EntityType<?> elementEntityType = (EntityType<?>) ((PluralAttribute<?, ?, ?>) att)
                  .getElementType();
              EdmEntitySet elementEntitySet = metadata
                  .getEdmEntitySet(JPAEdmGenerator.getEntitySetName(elementEntityType));

              relatedEntities.add(jpaEntityToOEntity(
                  metadata,
                  elementEntitySet,
                  elementEntityType,
                  relatedEntity,
                  remainingPropPath,
                  null));
            }

            links.add(OLinks.relatedEntitiesInline(
                null,
                prop,
                null,
                relatedEntities));

          } else if (att.getPersistentAttributeType() == PersistentAttributeType.ONE_TO_ONE
              || att.getPersistentAttributeType() == PersistentAttributeType.MANY_TO_ONE) {
            EntityType<?> relatedEntityType =
                (EntityType<?>) ((SingularAttribute<?, ?>) att)
                    .getType();

            EdmEntitySet relatedEntitySet =
                metadata.getEdmEntitySet(JPAEdmGenerator
                    .getEntitySetName(relatedEntityType));

            Object relatedEntity = JPAMember.create(att, jpaEntity).get();

            if (relatedEntity == null) {
              links.add(OLinks.relatedEntityInline(
                  null,
                  prop,
                  null,
                  null));

            } else {
              links.add(OLinks.relatedEntityInline(
                  null,
                  prop,
                  null,
                  jpaEntityToOEntity(
                      metadata,
                      relatedEntitySet,
                      relatedEntityType,
                      relatedEntity,
                      remainingPropPath,
                      null)));
            }

          }

        }
      }

      // for every navigation propety that we didn' expand we must place an deferred
      // OLink if the nav prop is selected
      for (final EdmNavigationProperty ep : ees.getType().getNavigationProperties()) {
        if (JPAProducer.isSelected(ep.getName(), select)) {
          boolean expanded = Enumerable.create(links).any(new Predicate1<OLink>() {
            @Override
            public boolean apply(OLink t) {
              return t.getTitle().equals(ep.getName());
            }
          });

          if (!expanded) {
            // defer
            if (ep.getToRole().getMultiplicity() == EdmMultiplicity.MANY) {
              links.add(OLinks.relatedEntities(null, ep.getName(), null));
            } else {
              links.add(OLinks.relatedEntity(null, ep.getName(), null));
            }
          }
        }
      }

      return OEntities.create(ees, SetResponseCommand.toOEntityKey(jpaEntity, idAtt), properties, links);

    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  static Object getIdValue(
      Object jpaEntity,
      SingularAttribute<?, ?> idAtt,
      String propName) {
    try {
      // get the composite id
      Object keyValue = JPAMember.create(idAtt, jpaEntity).get();

      if (propName == null)
        return keyValue;

      // get the property from the key
      ManagedType<?> keyType = (ManagedType<?>) idAtt.getType();
      Attribute<?, ?> att = keyType.getAttribute(propName);
      return JPAMember.create(att, keyValue).get();
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  static OEntityKey toOEntityKey(Object jpaEntity, SingularAttribute<?, ?> idAtt) {
    boolean hasEmbeddedCompositeKey =
        idAtt.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED;
    if (!hasEmbeddedCompositeKey) {
      Object id = SetResponseCommand.getIdValue(jpaEntity, idAtt, null);
      return OEntityKey.create(id);
    }
    ManagedType<?> keyType = (ManagedType<?>) idAtt.getType();

    Map<String, Object> nameValues = new HashMap<String, Object>();
    for (Attribute<?, ?> att : keyType.getAttributes())
      nameValues.put(att.getName(), SetResponseCommand.getIdValue(jpaEntity, idAtt, att.getName()));
    return OEntityKey.create(nameValues);
  }

}