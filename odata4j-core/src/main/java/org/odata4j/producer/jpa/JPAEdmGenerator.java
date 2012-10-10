package org.odata4j.producer.jpa;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Column;
import javax.persistence.EntityManagerFactory;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.Attribute.PersistentAttributeType;
import javax.persistence.metamodel.EmbeddableType;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.ManagedType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.Type;
import javax.persistence.metamodel.Type.PersistenceType;

import org.core4j.Enumerable;
import org.core4j.Predicate1;
import org.odata4j.core.OFuncs;
import org.odata4j.core.OPredicates;
import org.odata4j.edm.EdmAssociation;
import org.odata4j.edm.EdmAssociationEnd;
import org.odata4j.edm.EdmAssociationSet;
import org.odata4j.edm.EdmAssociationSetEnd;
import org.odata4j.edm.EdmComplexType;
import org.odata4j.edm.EdmDataServices;
import org.odata4j.edm.EdmDecorator;
import org.odata4j.edm.EdmEntityContainer;
import org.odata4j.edm.EdmEntitySet;
import org.odata4j.edm.EdmEntityType;
import org.odata4j.edm.EdmGenerator;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;

public class JPAEdmGenerator implements EdmGenerator {

  private final Logger log = Logger.getLogger(getClass().getName());

  private final EntityManagerFactory emf;
  private final String namespace;

  public JPAEdmGenerator(EntityManagerFactory emf, String namespace) {
    this.emf = emf;
    this.namespace = namespace;
  }

  protected EntityManagerFactory getEntityManagerFactory() {
    return emf;
  }

  protected String getNamespace() {
    return namespace;
  }

  protected String getEntityContainerName() {
    return getNamespace() + "Entities";
  }

  protected String getModelSchemaNamespace() {
    return getNamespace() + "Model";
  }

  protected String getContainerSchemaNamespace() {
    return getNamespace() + "Container";
  }

  @Override
  public EdmDataServices.Builder generateEdm(EdmDecorator decorator) {

    String modelNamespace = getModelSchemaNamespace();

    List<EdmEntityType.Builder> edmEntityTypes = new ArrayList<EdmEntityType.Builder>();
    List<EdmComplexType.Builder> edmComplexTypes = new ArrayList<EdmComplexType.Builder>();
    List<EdmAssociation.Builder> associations = new ArrayList<EdmAssociation.Builder>();

    List<EdmEntitySet.Builder> entitySets = new ArrayList<EdmEntitySet.Builder>();
    List<EdmAssociationSet.Builder> associationSets = new ArrayList<EdmAssociationSet.Builder>();

    Metamodel mm = getEntityManagerFactory().getMetamodel();

    // complex types
    for (EmbeddableType<?> et : mm.getEmbeddables()) {

      String name = et.getJavaType().getSimpleName();
      List<EdmProperty.Builder> properties = getProperties(modelNamespace, et);

      EdmComplexType.Builder ect = EdmComplexType.newBuilder().setNamespace(modelNamespace).setName(name).addProperties(properties);
      edmComplexTypes.add(ect);
    }

    // entities
    for (EntityType<?> et : mm.getEntities()) {

      String name = getEntitySetName(et);

      List<String> keys = new ArrayList<String>();
      SingularAttribute<?, ?> idAttribute = null;
      if (et.hasSingleIdAttribute()) {
        idAttribute = getIdAttribute(et);
        // handle composite embedded keys (@EmbeddedId)
        if (idAttribute.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
          keys = Enumerable.create(getProperties(modelNamespace, (ManagedType<?>) idAttribute.getType()))
              .select(OFuncs.name(EdmProperty.Builder.class)).toList();
        } else {
          keys = Enumerable.create(idAttribute.getName()).toList();
        }
      } else {
        throw new IllegalArgumentException("IdClass not yet supported");
      }

      List<EdmProperty.Builder> properties = getProperties(modelNamespace, et);
      List<EdmNavigationProperty.Builder> navigationProperties = new ArrayList<EdmNavigationProperty.Builder>();

      EdmEntityType.Builder eet = EdmEntityType.newBuilder().setNamespace(modelNamespace).setName(name).addKeys(keys).addProperties(properties).addNavigationProperties(navigationProperties);
      edmEntityTypes.add(eet);

      EdmEntitySet.Builder ees = EdmEntitySet.newBuilder().setName(name).setEntityType(eet);
      entitySets.add(ees);
    }

    Map<String, EdmEntityType.Builder> eetsByName = Enumerable.create(edmEntityTypes).toMap(OFuncs.name(EdmEntityType.Builder.class));
    Map<String, EdmEntitySet.Builder> eesByName = Enumerable.create(entitySets).toMap(OFuncs.name(EdmEntitySet.Builder.class));

    // associations + navigationproperties on the non-collection side
    for (EntityType<?> et : mm.getEntities()) {

      for (Attribute<?, ?> att : et.getAttributes()) {

        if (!att.isCollection()) {

          SingularAttribute<?, ?> singularAtt = (SingularAttribute<?, ?>) att;

          Type<?> singularAttType = singularAtt.getType();
          if (singularAttType.getPersistenceType().equals(PersistenceType.ENTITY)) {

            // we found a single attribute to an entity
            // create an edm many-to-one relationship  (* 0..1)
            EntityType<?> singularAttEntityType = (EntityType<?>) singularAttType;

            EdmEntityType.Builder fromEntityType = eetsByName.get(getEntitySetName(et));
            EdmEntityType.Builder toEntityType = eetsByName.get(getEntitySetName(singularAttEntityType));

            // add EdmAssociation, EdmAssociationSet
            EdmAssociation.Builder association = defineManyTo(EdmMultiplicity.ZERO_TO_ONE, associations, fromEntityType, toEntityType, modelNamespace, eesByName, associationSets);

            // add EdmNavigationProperty
            EdmNavigationProperty.Builder navigationProperty = EdmNavigationProperty.newBuilder(singularAtt.getName())
                .setRelationship(association)
                .setFromTo(association.getEnd1(),
                    association.getEnd2());
            fromEntityType.addNavigationProperties(navigationProperty);
          }
        }
      }
    }

    // navigation properties for the collection side of associations
    for (EntityType<?> et : mm.getEntities()) {

      for (Attribute<?, ?> att : et.getAttributes()) {

        if (att.isCollection()) {

          // one-to-many
          PluralAttribute<?, ?, ?> pluralAtt = (PluralAttribute<?, ?, ?>) att;
          JPAMember m = JPAMember.create(pluralAtt, null);
          ManyToMany manyToMany = m.getAnnotation(ManyToMany.class);

          EntityType<?> pluralAttEntityType = (EntityType<?>) pluralAtt.getElementType();

          final EdmEntityType.Builder fromEntityType = eetsByName.get(getEntitySetName(et));
          final EdmEntityType.Builder toEntityType = eetsByName.get(getEntitySetName(pluralAttEntityType));

          try {
            EdmAssociation.Builder association = Enumerable.create(associations).firstOrNull(new Predicate1<EdmAssociation.Builder>() {
              public boolean apply(EdmAssociation.Builder input) {
                return input.getEnd1().getType().getFullyQualifiedTypeName().equals(toEntityType.getFullyQualifiedTypeName())
                    && input.getEnd2().getType().getFullyQualifiedTypeName().equals(fromEntityType.getFullyQualifiedTypeName());
              }
            });

            EdmAssociationEnd.Builder fromRole, toRole;

            if (association == null) {
              // no EdmAssociation, EdmAssociationSet defined, backfill!

              // add EdmAssociation, EdmAssociationSet
              if (manyToMany != null) {
                association = defineManyTo(EdmMultiplicity.MANY, associations, fromEntityType, toEntityType, modelNamespace, eesByName, associationSets);
                fromRole = association.getEnd1();
                toRole = association.getEnd2();
              } else {
                association = defineManyTo(EdmMultiplicity.ZERO_TO_ONE, associations, toEntityType, fromEntityType, modelNamespace, eesByName, associationSets);
                fromRole = association.getEnd2();
                toRole = association.getEnd1();
              }

            } else {
              fromRole = association.getEnd2();
              toRole = association.getEnd1();
            }

            // add EdmNavigationProperty
            EdmNavigationProperty.Builder navigationProperty = EdmNavigationProperty.newBuilder(pluralAtt.getName())
                .setRelationship(association)
                .setFromTo(fromRole,
                    toRole);
            fromEntityType.addNavigationProperties(navigationProperty);

          } catch (Exception e) {
            log.log(Level.WARNING, "Exception building Edm associations: " + e.getMessage(), e);
          }
        }

      }

    }

    EdmEntityContainer.Builder container = EdmEntityContainer.newBuilder().setName(getEntityContainerName()).setIsDefault(true).addEntitySets(entitySets).addAssociationSets(associationSets);

    EdmSchema.Builder modelSchema = EdmSchema.newBuilder().setNamespace(modelNamespace).addEntityTypes(edmEntityTypes).addComplexTypes(edmComplexTypes).addAssociations(associations);
    EdmSchema.Builder containerSchema = EdmSchema.newBuilder().setNamespace(getContainerSchemaNamespace()).addEntityContainers(container);

    return EdmDataServices.newBuilder().addSchemas(containerSchema, modelSchema);
  }

  protected EdmSimpleType<?> toEdmType(SingularAttribute<?, ?> sa) {
    Class<?> javaType = sa.getType().getJavaType();

    EdmSimpleType<?> type = EdmSimpleType.forJavaType(javaType);
    if (type == EdmSimpleType.DATETIME) {
      TemporalType temporal = getTemporalType(sa);
      if (temporal != null && temporal == TemporalType.TIME)
        type = EdmSimpleType.TIME;
    }

    if (type != null)
      return type;

    throw new UnsupportedOperationException(javaType.toString());
  }

  protected EdmProperty.Builder toEdmProperty(String modelNamespace, SingularAttribute<?, ?> sa) {
    String name = sa.getName();
    EdmType type;
    if (sa.getPersistentAttributeType() == PersistentAttributeType.EMBEDDED) {
      String simpleName = sa.getJavaType().getSimpleName();
      // this will map to an edm complex.  If anyone ever im;plements this you
      // should not create an EdmComplexType.  Instead, you should maintain
      // a cache of EdmComplexType.Builders and re-use instances with the same
      // namespace.name.  (multiple Entity classes may have properties of this
      // type and we don't wan't lots of instances of EdmComplex type floating
      // around that are the same conceptual type)
      type = EdmComplexType.newBuilder().setNamespace(modelNamespace).setName(simpleName).build();
    } else if (sa.getBindableJavaType().isEnum()) {
      // TODO assume string mapping for now, @Enumerated info not avail in metamodel?
      type = EdmSimpleType.STRING;
    } else {
      type = toEdmType(sa);
    }
    boolean nullable = sa.isOptional();

    Integer maxLength = null;
    if (sa.getJavaMember() instanceof AnnotatedElement) {
      Column col = ((AnnotatedElement) sa.getJavaMember()).getAnnotation(Column.class);
      if (col != null && Enumerable.<EdmType> create(EdmSimpleType.BINARY, EdmSimpleType.STRING).contains(type))
        maxLength = col.length();
    }

    return EdmProperty.newBuilder(name).setType(type).setNullable(nullable).setMaxLength(maxLength);
  }

  protected List<EdmProperty.Builder> getProperties(String modelNamespace, ManagedType<?> et) {
    List<EdmProperty.Builder> properties = new ArrayList<EdmProperty.Builder>();
    for (Attribute<?, ?> att : et.getAttributes()) {

      if (att.isCollection()) {} else {
        SingularAttribute<?, ?> sa = (SingularAttribute<?, ?>) att;

        Type<?> type = sa.getType();
        // Do we have an embedded composite key here? If so, we have to flatten the @EmbeddedId since
        // only any set of non-nullable, immutable, <EDMSimpleType> declared properties MAY serve as the key.
        if (sa.isId() && type.getPersistenceType() == PersistenceType.EMBEDDABLE) {
          properties.addAll(getProperties(modelNamespace, (ManagedType<?>) sa.getType()));
        } else if (type.getPersistenceType().equals(PersistenceType.BASIC) || type.getPersistenceType().equals(PersistenceType.EMBEDDABLE)) {
          EdmProperty.Builder prop = toEdmProperty(modelNamespace, sa);
          properties.add(prop);
        }
      }
    }
    return properties;
  }

  private static EdmAssociation.Builder defineManyTo(EdmMultiplicity toMult, List<EdmAssociation.Builder> associations, EdmEntityType.Builder fromEntityType, EdmEntityType.Builder toEntityType,
      String modelNamespace, Map<String, EdmEntitySet.Builder> eesByName, List<EdmAssociationSet.Builder> associationSets) {
    EdmMultiplicity fromMult = EdmMultiplicity.MANY;

    String assocName = getAssociationName(associations, fromEntityType, toEntityType);

    // add EdmAssociation
    EdmAssociationEnd.Builder fromAssociationEnd = EdmAssociationEnd.newBuilder().setRole(fromEntityType.getName()).setType(fromEntityType).setMultiplicity(fromMult);
    String toAssociationEndName = toEntityType.getName();
    if (toAssociationEndName.equals(fromEntityType.getName())) {
      toAssociationEndName = toAssociationEndName + "1";
    }
    EdmAssociationEnd.Builder toAssociationEnd = EdmAssociationEnd.newBuilder().setRole(toAssociationEndName).setType(toEntityType).setMultiplicity(toMult);
    EdmAssociation.Builder association = EdmAssociation.newBuilder().setNamespace(modelNamespace).setName(assocName).setEnds(fromAssociationEnd, toAssociationEnd);
    associations.add(association);

    // add EdmAssociationSet
    EdmEntitySet.Builder fromEntitySet = eesByName.get(fromEntityType.getName());
    EdmEntitySet.Builder toEntitySet = eesByName.get(toEntityType.getName());
    EdmAssociationSet.Builder associationSet = EdmAssociationSet.newBuilder()
        .setName(assocName)
        .setAssociation(association).setEnds(
            EdmAssociationSetEnd.newBuilder().setRole(fromAssociationEnd).setEntitySet(fromEntitySet),
            EdmAssociationSetEnd.newBuilder().setRole(toAssociationEnd).setEntitySet(toEntitySet));
    associationSets.add(associationSet);

    return association;

  }

  public static <X> SingularAttribute<? super X, ?> getIdAttribute(EntityType<X> et) {
    return Enumerable.create(et.getSingularAttributes()).firstOrNull(new Predicate1<SingularAttribute<? super X, ?>>() {
      public boolean apply(SingularAttribute<? super X, ?> input) {
        return input.isId();
      }
    });
  }

  public static <X> String getEntitySetName(EntityType<X> et) {
    String name = et.getName();
    int idx = name.lastIndexOf('.');
    return idx > 0
        ? name.substring(idx + 1)
        : name;
  }

  protected static String getAssociationName(List<EdmAssociation.Builder> associations, EdmEntityType.Builder fromEntityType, EdmEntityType.Builder toEntityType) {
    for (int i = 0;; i++) {
      String assocName = i == 0
          ? String.format("FK_%s_%s", fromEntityType.getName(), toEntityType.getName())
          : String.format("FK_%s_%s_%d", fromEntityType.getName(), toEntityType.getName(), i);

      boolean exists = Enumerable.create(associations).where(OPredicates.nameEquals(EdmAssociation.Builder.class, assocName)).count() > 0;

      if (!exists)
        return assocName;
    }
  }

  protected TemporalType getTemporalType(SingularAttribute<?, ?> sa) {
    Member member = sa.getJavaMember();

    Temporal temporal = null;
    if (member instanceof Field) {
      temporal = ((Field) member).getAnnotation(Temporal.class);
    } else if (member instanceof Method) {
      temporal = ((Method) member).getAnnotation(Temporal.class);
    }

    return temporal == null ? null : temporal.value();
  }

}
