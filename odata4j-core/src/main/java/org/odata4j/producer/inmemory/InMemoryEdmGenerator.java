package org.odata4j.producer.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.core4j.Enumerable;
import org.core4j.Predicate1;
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

public class InMemoryEdmGenerator implements EdmGenerator {
  private static final boolean DUMP = false;

  private static void dump(String msg) {
    if (DUMP) System.out.println(msg);
  }

  private final Logger log = Logger.getLogger(getClass().getName());

  private final String namespace;
  private final String containerName;
  protected final InMemoryTypeMapping typeMapping;
  protected final Map<String, InMemoryEntityInfo<?>> eis; // key: EntitySet name
  protected final Map<String, InMemoryComplexTypeInfo<?>> complexTypeInfo; // key complex type edm type name
  protected final List<EdmComplexType.Builder> edmComplexTypes = new ArrayList<EdmComplexType.Builder>();

  // Note, assumes each Java type will only have a single Entity Set defined for it.
  protected final Map<Class<?>, String> entitySetNameByClass = new HashMap<Class<?>, String>();

  // build these as we go now.
  protected Map<String, EdmEntityType.Builder> entityTypesByName = new HashMap<String, EdmEntityType.Builder>();
  protected Map<String, EdmEntitySet.Builder> entitySetsByName = new HashMap<String, EdmEntitySet.Builder>();
  protected final boolean flatten;

  public InMemoryEdmGenerator(String namespace, String containerName, InMemoryTypeMapping typeMapping,
      String idPropertyName, Map<String, InMemoryEntityInfo<?>> eis,
      Map<String, InMemoryComplexTypeInfo<?>> complexTypes) {
    this(namespace, containerName, typeMapping, idPropertyName, eis, complexTypes, true);
  }

  public InMemoryEdmGenerator(String namespace, String containerName, InMemoryTypeMapping typeMapping,
      String idPropertyName, Map<String, InMemoryEntityInfo<?>> eis,
      Map<String, InMemoryComplexTypeInfo<?>> complexTypes, boolean flatten) {
    this.namespace = namespace;
    this.containerName = containerName;
    this.typeMapping = typeMapping;
    this.eis = eis;
    this.complexTypeInfo = complexTypes;

    for (Entry<String, InMemoryEntityInfo<?>> e : eis.entrySet()) {
      entitySetNameByClass.put(e.getValue().entityClass, e.getKey());
    }
    this.flatten = flatten;
  }

  @Override
  public EdmDataServices.Builder generateEdm(EdmDecorator decorator) {

    List<EdmSchema.Builder> schemas = new ArrayList<EdmSchema.Builder>();
    List<EdmEntityContainer.Builder> containers = new ArrayList<EdmEntityContainer.Builder>();
    List<EdmAssociation.Builder> associations = new ArrayList<EdmAssociation.Builder>();
    List<EdmAssociationSet.Builder> associationSets = new ArrayList<EdmAssociationSet.Builder>();

    createComplexTypes(decorator, edmComplexTypes);

    // creates id other basic SUPPORTED_TYPE properties(structural) entities
    createStructuralEntities(decorator);

    // TODO handle back references too
    // create hashmaps from sets

    createNavigationProperties(associations, associationSets,
        entityTypesByName, entitySetsByName, entitySetNameByClass);

    EdmEntityContainer.Builder container = EdmEntityContainer.newBuilder().setName(containerName).setIsDefault(true)
        .addEntitySets(entitySetsByName.values()).addAssociationSets(associationSets);

    containers.add(container);

    EdmSchema.Builder schema = EdmSchema.newBuilder().setNamespace(namespace)
        .addEntityTypes(entityTypesByName.values())
        .addAssociations(associations)
        .addEntityContainers(containers)
        .addComplexTypes(edmComplexTypes);

    addFunctions(schema, container);

    if (decorator != null) {
      schema.setDocumentation(decorator.getDocumentationForSchema(namespace));
      schema.setAnnotations(decorator.getAnnotationsForSchema(namespace));
    }

    schemas.add(schema);
    EdmDataServices.Builder rt = EdmDataServices.newBuilder().addSchemas(schemas);
    if (decorator != null)
      rt.addNamespaces(decorator.getNamespaces());
    return rt;
  }

  private void createComplexTypes(EdmDecorator decorator, List<EdmComplexType.Builder> complexTypes) {
    for (String complexTypeName : complexTypeInfo.keySet()) {
      dump("edm complexType: " + complexTypeName);
      InMemoryComplexTypeInfo<?> typeInfo = complexTypeInfo.get(complexTypeName);

      List<EdmProperty.Builder> properties = new ArrayList<EdmProperty.Builder>();

      // no keys
      properties.addAll(toEdmProperties(decorator, typeInfo.propertyModel, new String[] {}, complexTypeName));

      EdmComplexType.Builder typeBuilder = EdmComplexType.newBuilder()
          .setNamespace(namespace)
          .setName(typeInfo.typeName)
          .addProperties(properties);

      if (decorator != null) {
        typeBuilder.setDocumentation(decorator.getDocumentationForEntityType(namespace, complexTypeName));
        typeBuilder.setAnnotations(decorator.getAnnotationsForEntityType(namespace, complexTypeName));
      }

      complexTypes.add(typeBuilder);
    }
  }

  private void createStructuralEntities(EdmDecorator decorator) {

    // eis contains all of the registered entity sets.
    for (String entitySetName : eis.keySet()) {
      InMemoryEntityInfo<?> entityInfo = eis.get(entitySetName);

      // do we have this type yet?
      EdmEntityType.Builder eet = entityTypesByName.get(entityInfo.entityTypeName);
      if (eet == null) {
        eet = createStructuralType(decorator, entityInfo);
      }

      EdmEntitySet.Builder ees = EdmEntitySet.newBuilder().setName(entitySetName).setEntityType(eet);
      entitySetsByName.put(ees.getName(), ees);

    }
  }

  protected InMemoryEntityInfo<?> findEntityInfoForClass(Class<?> clazz) {
    for (InMemoryEntityInfo<?> typeInfo : this.eis.values()) {
      if (typeInfo.entityClass.equals(clazz)) {
        return typeInfo;
      }
    }

    return null;
  }

  /*
   * contains all generated InMemoryEntityInfos that get created as we walk
   * up the inheritance hierarchy and find Java types that are not registered.
   */
  private Map<Class<?>, InMemoryEntityInfo<?>> unregisteredEntityInfo =
      new HashMap<Class<?>, InMemoryEntityInfo<?>>();

  protected InMemoryEntityInfo<?> getUnregisteredEntityInfo(Class<?> clazz, InMemoryEntityInfo<?> subclass) {
    InMemoryEntityInfo<?> ei = unregisteredEntityInfo.get(clazz);
    if (ei == null) {
      ei = new InMemoryEntityInfo();
      ei.entityTypeName = clazz.getSimpleName();
      ei.keys = subclass.keys;
      ei.entityClass = (Class) clazz;
      ei.properties = new EnumsAsStringsPropertyModelDelegate(
          new BeanBasedPropertyModel(ei.entityClass, this.flatten));
    }
    return ei;
  }

  protected EdmEntityType.Builder createStructuralType(EdmDecorator decorator, InMemoryEntityInfo<?> entityInfo) {
    List<EdmProperty.Builder> properties = new ArrayList<EdmProperty.Builder>();

    Class<?> superClass = flatten ? null : entityInfo.getSuperClass();

    properties.addAll(toEdmProperties(decorator, entityInfo.properties, entityInfo.keys, entityInfo.entityTypeName));

    EdmEntityType.Builder eet = EdmEntityType.newBuilder()
        .setNamespace(namespace)
        .setName(entityInfo.entityTypeName)
        .setHasStream(entityInfo.hasStream)
        .addProperties(properties);

    if (superClass == null) {
      eet.addKeys(entityInfo.keys);
    }

    if (decorator != null) {
      eet.setDocumentation(decorator.getDocumentationForEntityType(namespace, entityInfo.entityTypeName));
      eet.setAnnotations(decorator.getAnnotationsForEntityType(namespace, entityInfo.entityTypeName));
    }
    entityTypesByName.put(eet.getName(), eet);

    EdmEntityType.Builder superType = null;
    if (!this.flatten && entityInfo.entityClass.getSuperclass() != null && !entityInfo.entityClass.getSuperclass().equals(Object.class)) {
      InMemoryEntityInfo<?> entityInfoSuper = findEntityInfoForClass(entityInfo.entityClass.getSuperclass());
      // may have created it along another branch in the hierarchy
      if (entityInfoSuper == null) {
        // synthesize...
        entityInfoSuper = getUnregisteredEntityInfo(entityInfo.entityClass.getSuperclass(), entityInfo);
      }

      superType = entityTypesByName.get(entityInfoSuper.entityTypeName);
      if (superType == null) {
        superType = createStructuralType(decorator, entityInfoSuper);
      }
    }

    eet.setBaseType(superType);
    return eet;
  }

  protected void createNavigationProperties(List<EdmAssociation.Builder> associations,
      List<EdmAssociationSet.Builder> associationSets,
      Map<String, EdmEntityType.Builder> entityTypesByName,
      Map<String, EdmEntitySet.Builder> entitySetByName,
      Map<Class<?>, String> entityNameByClass) {

    for (String entitySetName : eis.keySet()) {
      InMemoryEntityInfo<?> ei = eis.get(entitySetName);
      Class<?> clazz1 = ei.entityClass;

      generateToOneNavProperties(associations, associationSets,
          entityTypesByName, entitySetByName, entityNameByClass,
          ei.entityTypeName, ei);

      generateToManyNavProperties(associations, associationSets,
          entityTypesByName, entitySetByName, entityNameByClass,
          ei.entityTypeName, ei, clazz1);
    }
  }

  protected void generateToOneNavProperties(
      List<EdmAssociation.Builder> associations,
      List<EdmAssociationSet.Builder> associationSets,
      Map<String, EdmEntityType.Builder> entityTypesByName,
      Map<String, EdmEntitySet.Builder> entitySetByName,
      Map<Class<?>, String> entityNameByClass,
      String entityTypeName,
      InMemoryEntityInfo<?> ei) {

    Iterable<String> propertyNames = this.flatten ? ei.properties.getPropertyNames() : ei.properties.getDeclaredPropertyNames();
    for (String assocProp : propertyNames) {

      EdmEntityType.Builder eet1 = entityTypesByName.get(entityTypeName);
      Class<?> clazz2 = ei.properties.getPropertyType(assocProp);
      String entitySetName2 = entityNameByClass.get(clazz2);
      InMemoryEntityInfo<?> ei2 = entitySetName2 == null ? null : eis.get(entitySetName2);

      if (log.isLoggable(Level.FINE)) {
        log.log(Level.FINE, "genToOnNavProp {0} - {1}({2}) eetName2: {3}", new Object[] { entityTypeName, assocProp, clazz2, entitySetName2 });
      }

      if (eet1.findProperty(assocProp) != null || ei2 == null)
        continue;

      EdmEntityType.Builder eet2 = entityTypesByName.get(ei2.entityTypeName);

      EdmMultiplicity m1 = EdmMultiplicity.MANY;
      EdmMultiplicity m2 = EdmMultiplicity.ONE;

      String assocName = String.format("FK_%s_%s", eet1.getName(), eet2.getName());
      EdmAssociationEnd.Builder assocEnd1 = EdmAssociationEnd.newBuilder().setRole(eet1.getName())
          .setType(eet1).setMultiplicity(m1);
      String assocEnd2Name = eet2.getName();
      if (assocEnd2Name.equals(eet1.getName()))
        assocEnd2Name = assocEnd2Name + "1";

      EdmAssociationEnd.Builder assocEnd2 = EdmAssociationEnd.newBuilder().setRole(assocEnd2Name).setType(eet2).setMultiplicity(m2);
      EdmAssociation.Builder assoc = EdmAssociation.newBuilder().setNamespace(namespace).setName(assocName).setEnds(assocEnd1, assocEnd2);

      associations.add(assoc);

      EdmEntitySet.Builder ees1 = entitySetByName.get(eet1.getName());
      EdmEntitySet.Builder ees2 = entitySetByName.get(eet2.getName());
      if (ees1 == null) {
        // entity set name different than entity type name.
        ees1 = getEntitySetForEntityTypeName(eet1.getName());
      }
      if (ees2 == null) {
        // entity set name different than entity type name.
        ees2 = getEntitySetForEntityTypeName(eet2.getName());
      }

      EdmAssociationSet.Builder eas = EdmAssociationSet.newBuilder().setName(assocName).setAssociation(assoc).setEnds(
          EdmAssociationSetEnd.newBuilder().setRole(assocEnd1).setEntitySet(ees1),
          EdmAssociationSetEnd.newBuilder().setRole(assocEnd2).setEntitySet(ees2));

      associationSets.add(eas);

      EdmNavigationProperty.Builder np = EdmNavigationProperty.newBuilder(assocProp)
          .setRelationship(assoc).setFromTo(assoc.getEnd1(), assoc.getEnd2());

      eet1.addNavigationProperties(np);
    }
  }

  protected EdmEntitySet.Builder getEntitySetForEntityTypeName(String entityTypeName) {

    for (InMemoryEntityInfo<?> ti : eis.values()) {
      if (ti.entityTypeName.equals(entityTypeName)) {
        return entitySetsByName.get(ti.entitySetName);
      }
    }
    return null;
  }

  protected void generateToManyNavProperties(List<EdmAssociation.Builder> associations,
      List<EdmAssociationSet.Builder> associationSets,
      Map<String, EdmEntityType.Builder> entityTypesByName,
      Map<String, EdmEntitySet.Builder> entitySetByName,
      Map<Class<?>, String> entityNameByClass,
      String entityTypeName,
      InMemoryEntityInfo<?> ei,
      Class<?> clazz1) {

    Iterable<String> collectionNames = this.flatten ? ei.properties.getCollectionNames() : ei.properties.getDeclaredCollectionNames();

    for (String assocProp : collectionNames) {

      final EdmEntityType.Builder eet1 = entityTypesByName.get(entityTypeName);

      Class<?> clazz2 = ei.properties.getCollectionElementType(assocProp);
      String entitySetName2 = entityNameByClass.get(clazz2);
      InMemoryEntityInfo<?> class2eiInfo = entitySetName2 == null ? null : eis.get(entitySetName2);

      if (class2eiInfo == null)
        continue;

      final EdmEntityType.Builder eet2 = entityTypesByName.get(class2eiInfo.entityTypeName);

      try {
        EdmAssociation.Builder assoc = Enumerable.create(associations).firstOrNull(new Predicate1<EdmAssociation.Builder>() {

          public boolean apply(EdmAssociation.Builder input) {
            return input.getEnd1().getType().equals(eet2) && input.getEnd2().getType().equals(eet1);
          }
        });

        EdmAssociationEnd.Builder fromRole, toRole;

        if (assoc == null) {
          //no association already exists
          EdmMultiplicity m1 = EdmMultiplicity.ZERO_TO_ONE;
          EdmMultiplicity m2 = EdmMultiplicity.MANY;

          //find ei info of class2
          for (String tmp : class2eiInfo.properties.getCollectionNames()) {
            //class2 has a ref to class1
            //Class<?> tmpc = class2eiInfo.properties.getCollectionElementType(tmp);
            if (clazz1 == class2eiInfo.properties.getCollectionElementType(tmp)) {
              m1 = EdmMultiplicity.MANY;
              m2 = EdmMultiplicity.MANY;
              break;
            }
          }

          String assocName = String.format("FK_%s_%s", eet1.getName(), eet2.getName());
          EdmAssociationEnd.Builder assocEnd1 = EdmAssociationEnd.newBuilder().setRole(eet1.getName()).setType(eet1).setMultiplicity(m1);
          String assocEnd2Name = eet2.getName();
          if (assocEnd2Name.equals(eet1.getName()))
            assocEnd2Name = assocEnd2Name + "1";
          EdmAssociationEnd.Builder assocEnd2 = EdmAssociationEnd.newBuilder().setRole(assocEnd2Name).setType(eet2).setMultiplicity(m2);
          assoc = EdmAssociation.newBuilder().setNamespace(namespace).setName(assocName).setEnds(assocEnd1, assocEnd2);

          associations.add(assoc);

          EdmEntitySet.Builder ees1 = entitySetByName.get(eet1.getName());
          EdmEntitySet.Builder ees2 = entitySetByName.get(eet2.getName());
          if (ees1 == null) {
            // entity set name different than entity type name.
            ees1 = getEntitySetForEntityTypeName(eet1.getName());
          }
          if (ees2 == null) {
            // entity set name different than entity type name.
            ees2 = getEntitySetForEntityTypeName(eet2.getName());
          }

          EdmAssociationSet.Builder eas = EdmAssociationSet.newBuilder().setName(assocName).setAssociation(assoc).setEnds(
              EdmAssociationSetEnd.newBuilder().setRole(assocEnd1).setEntitySet(ees1),
              EdmAssociationSetEnd.newBuilder().setRole(assocEnd2).setEntitySet(ees2));
          associationSets.add(eas);

          fromRole = assoc.getEnd1();
          toRole = assoc.getEnd2();
        } else {
          fromRole = assoc.getEnd2();
          toRole = assoc.getEnd1();
        }

        EdmNavigationProperty.Builder np = EdmNavigationProperty.newBuilder(assocProp).setRelationship(assoc).setFromTo(fromRole, toRole);

        eet1.addNavigationProperties(np);
      } catch (Exception e) {
        // hmmh...I guess the build() will fail later
        log.log(Level.WARNING, "Exception building Edm associations: " + entityTypeName + "," + clazz1 + " set: " + ei.entitySetName
            + " -> " + assocProp, e);
      }
    }
  }

  protected EdmComplexType.Builder findComplexTypeBuilder(String typeName) {
    String fqName = this.namespace + "." + typeName;
    for (EdmComplexType.Builder builder : this.edmComplexTypes) {
      if (builder.getFullyQualifiedTypeName().equals(fqName)) {
        return builder;
      }
    }
    return null;
  }

  protected EdmComplexType.Builder findComplexTypeForClass(Class<?> clazz) {
    for (InMemoryComplexTypeInfo<?> typeInfo : this.complexTypeInfo.values()) {
      if (typeInfo.entityClass.equals(clazz)) {
        // the typeName defines the edm type name
        return findComplexTypeBuilder(typeInfo.typeName);
      }
    }

    return null;
  }

  private Collection<EdmProperty.Builder> toEdmProperties(
      EdmDecorator decorator,
      PropertyModel model,
      String[] keys,
      String structuralTypename) {

    List<EdmProperty.Builder> rt = new ArrayList<EdmProperty.Builder>();
    Set<String> keySet = Enumerable.create(keys).toSet();

    Iterable<String> propertyNames = this.flatten ? model.getPropertyNames() : model.getDeclaredPropertyNames();
    for (String propName : propertyNames) {
      dump("edm property: " + propName);
      Class<?> propType = model.getPropertyType(propName);
      EdmType type = typeMapping.findEdmType(propType);
      EdmComplexType.Builder typeBuilder = null;
      if (type == null) {
        typeBuilder = findComplexTypeForClass(propType);
      }

      dump("edm property: " + propName + " type: " + type + " builder: " + typeBuilder);
      if (type == null && typeBuilder == null) {
        continue;
      }

      EdmProperty.Builder ep = EdmProperty
          .newBuilder(propName)
          .setNullable(!keySet.contains(propName));

      if (type != null) {
        ep.setType(type);
      }
      else {
        ep.setType(typeBuilder);
      }

      if (decorator != null) {
        ep.setDocumentation(decorator.getDocumentationForProperty(namespace, structuralTypename, propName));
        ep.setAnnotations(decorator.getAnnotationsForProperty(namespace, structuralTypename, propName));
      }
      rt.add(ep);
    }

    // collections of primitives and complex types
    propertyNames = this.flatten ? model.getCollectionNames() : model.getDeclaredCollectionNames();
    for (String collectionPropName : propertyNames) {
      Class<?> collectionElementType = model.getCollectionElementType(collectionPropName);
      if (entitySetNameByClass.get(collectionElementType) != null) {
        // this will be a nav prop
        continue;
      }

      EdmType type = typeMapping.findEdmType(collectionElementType);
      EdmType.Builder typeBuilder = null;
      if (type == null) {
        typeBuilder = findComplexTypeForClass(collectionElementType);
      } else {
        typeBuilder = EdmSimpleType.newBuilder(type);
      }

      if (typeBuilder == null) {
        continue;
      }

      // either a simple or complex type.
      EdmProperty.Builder ep = EdmProperty.newBuilder(collectionPropName)
          .setNullable(true)
          .setCollectionKind(EdmProperty.CollectionKind.Collection)
          .setType(typeBuilder);

      if (decorator != null) {
        ep.setDocumentation(decorator.getDocumentationForProperty(namespace, structuralTypename, collectionPropName));
        ep.setAnnotations(decorator.getAnnotationsForProperty(namespace, structuralTypename, collectionPropName));
      }
      rt.add(ep);
    }

    return rt;
  }

  /**
   * get the Edm namespace
   * @return the Edm namespace
   */
  public String getNamespace() {
    return namespace;
  }

  /**
   * provides an override point for applications to add application specific
   * EdmFunctions to their producer.
   * @param schema    the EdmSchema.Builder
   * @param container the EdmEntityContainer.Builder
   */
  protected void addFunctions(EdmSchema.Builder schema, EdmEntityContainer.Builder container) {
    // overridable :)
  }
}
