package org.odata4j.producer.edm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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
import org.odata4j.edm.EdmProperty.CollectionKind;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.format.xml.XmlFormatParser;

/**
 * Creates an EdmDataServices instance that models the EDM in terms of the EDM,
 * call it a meta-EDM.
 *
 * <p>This model then serves as the basis for queryable metadata.  This generator
 * can be parameterized with an optional {@link EdmDecorator}.  The decorator implements
 * aspects of the meta EDM that are application specific such as Documentation
 * and Annotations.
 *
 * <p>The current implementation is not 100% complete.
 *
 * <p>EntityTypes implemented include: Schema, Property, ComplexType and EntityType
 *                                  (note that NavigationProperty is *no* implemented yet)
 * <p>EntitySets implemented include: Schemas, Properties, EntityTypes, RootEntityTypes,
 *                                 ComplexTypes and RootComplexTypes
 *
 * <p>Documentation elements are supported
 * <p>AnnotationAttributes are supported
 * <p>AnnotationElements are partially supported (JSON only)
 */
public class MetadataEdmGenerator implements EdmGenerator {

  private EdmDecorator decorator = null;
  private EdmSchema.Builder schema = null;
  private EdmEntityContainer.Builder container = null;
  private EdmComplexType.Builder entityKeyType = null;
  private EdmComplexType.Builder documentationType = null;
  private List<EdmAssociation.Builder> assocs = new LinkedList<EdmAssociation.Builder>();
  private List<EdmComplexType.Builder> ctypes = new LinkedList<EdmComplexType.Builder>();
  private List<EdmEntityType.Builder> etypes = new LinkedList<EdmEntityType.Builder>();
  private List<EdmEntitySet.Builder> esets = new LinkedList<EdmEntitySet.Builder>();
  private List<EdmAssociationSet.Builder> asets = new LinkedList<EdmAssociationSet.Builder>();

  /** Generates the meta EDM data services  */
  public EdmDataServices.Builder generateEdm(EdmDecorator decorator) {

    createComplexTypes();
    createEntityTypes();

    container = EdmEntityContainer.newBuilder()
        .setName(Edm.ContainerName)
        .setIsDefault(true)
        .setLazyLoadingEnabled(Boolean.TRUE)
        .addEntitySets(esets)
        .addAssociationSets(asets);

    schema = EdmSchema.newBuilder()
        .setNamespace(Edm.namespace)
        .addEntityTypes(etypes)
        .addComplexTypes(ctypes)
        .addAssociations(assocs)
        .addEntityContainers(container);

    return EdmDataServices.newBuilder().addSchemas(schema);
  }

  private void createComplexTypes() {
    // ----------------------------- PropertyRef --------------------------
    List<EdmProperty.Builder> props = new ArrayList<EdmProperty.Builder>();

    EdmProperty.Builder ep = EdmProperty.newBuilder(Edm.PropertyRef.Name).setType(EdmSimpleType.STRING);
    props.add(ep);

    EdmComplexType.Builder propertyRef = EdmComplexType.newBuilder().setNamespace(Edm.namespace).setName(
        XmlFormatParser.EDM2008_9_PROPERTYREF.getLocalPart()).addProperties(props);
    ctypes.add(propertyRef);

    // ----------------------------- EntityKey --------------------------
    props = new ArrayList<EdmProperty.Builder>();

    ep = EdmProperty.newBuilder(Edm.EntityKey.Keys).setType(propertyRef).setCollectionKind(CollectionKind.List);
    props.add(ep);

    entityKeyType = EdmComplexType.newBuilder().setNamespace(Edm.namespace).setName(Edm.EntityKey.name()).addProperties(props);
    ctypes.add(entityKeyType);

    // ----------------------------- Documentation --------------------------

    props = new ArrayList<EdmProperty.Builder>();

    ep = EdmProperty.newBuilder(Edm.Documentation.Summary).setType(EdmSimpleType.STRING).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Documentation.LongDescription).setType(EdmSimpleType.STRING).setNullable(true);
    props.add(ep);

    documentationType = EdmComplexType.newBuilder().setNamespace(Edm.namespace).setName(Edm.Documentation.name()).addProperties(props);
    ctypes.add(documentationType);

  }

  private void createEntityTypes() {
    // --------------------------- Schema ------------------------------
    List<EdmProperty.Builder> props = new ArrayList<EdmProperty.Builder>();
    List<EdmNavigationProperty.Builder> navprops = new ArrayList<EdmNavigationProperty.Builder>();

    EdmProperty.Builder ep = null;

    ep = EdmProperty.newBuilder(Edm.Schema.Namespace).setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Schema.Alias).setType(EdmSimpleType.STRING).setNullable(true);
    props.add(ep);

    List<String> keys = new ArrayList<String>();
    keys.add(Edm.Schema.Namespace);
    EdmEntityType.Builder schemaType = EdmEntityType.newBuilder()
        .setNamespace(Edm.namespace)
        .setName(Edm.Schema.name())
        .addKeys(keys)
        .addProperties(props)
        .addNavigationProperties(navprops);
    if (decorator != null) {
      schemaType.setDocumentation(decorator.getDocumentationForEntityType(Edm.namespace, Edm.Schema.name()));
      schemaType.setAnnotations(decorator.getAnnotationsForEntityType(Edm.namespace, Edm.Schema.name()));
    }
    etypes.add(schemaType);

    EdmEntitySet.Builder schemaSet = EdmEntitySet.newBuilder().setName(Edm.EntitySets.Schemas).setEntityType(schemaType);
    esets.add(schemaSet);

    // --------------------------- StructuralType ------------------------------
    props = new ArrayList<EdmProperty.Builder>();
    navprops = new ArrayList<EdmNavigationProperty.Builder>();

    ep = EdmProperty.newBuilder(Edm.StructuralType.Namespace).setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.StructuralType.Name).setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.StructuralType.BaseType).setType(EdmSimpleType.STRING).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.StructuralType.Abstract).setType(EdmSimpleType.BOOLEAN).setNullable(true);
    props.add(ep);

    keys = new ArrayList<String>();
    keys.add(Edm.StructuralType.Namespace);
    keys.add(Edm.StructuralType.Name);
    EdmEntityType.Builder structuralType = EdmEntityType.newBuilder()
        .setNamespace(Edm.namespace)
        .setName(Edm.StructuralType.name())
        .addKeys(keys)
        .addProperties(props)
        .addNavigationProperties(navprops);
    if (decorator != null) {
      schemaType.setDocumentation(decorator.getDocumentationForEntityType(Edm.namespace, Edm.StructuralType.name()));
      schemaType.setAnnotations(decorator.getAnnotationsForEntityType(Edm.namespace, Edm.StructuralType.name()));
    }

    etypes.add(structuralType);

    // maybe?
    //EdmEntitySet structuralSet = new EdmEntitySet(Edm.EntitySets.EdmStructuralTypes, structuralType);
    //esets.add(structuralSet);

    // --------------------------- ComplexType ------------------------------

    props = Collections.<EdmProperty.Builder> emptyList();
    navprops = new ArrayList<EdmNavigationProperty.Builder>();

    EdmEntityType.Builder complexType = EdmEntityType.newBuilder()
        .setNamespace(Edm.namespace)
        .setName(Edm.ComplexType.name())
        .setBaseType(structuralType)
        .addProperties(props)
        .addNavigationProperties(navprops);
    if (decorator != null) {
      schemaType.setDocumentation(decorator.getDocumentationForEntityType(Edm.namespace, Edm.ComplexType.name()));
      schemaType.setAnnotations(decorator.getAnnotationsForEntityType(Edm.namespace, Edm.ComplexType.name()));
    }
    etypes.add(complexType);

    EdmEntitySet.Builder complexSet = EdmEntitySet.newBuilder().setName(Edm.EntitySets.ComplexTypes).setEntityType(complexType);
    esets.add(complexSet);

    EdmEntitySet.Builder rootComplexTypesSet = EdmEntitySet.newBuilder().setName(Edm.EntitySets.RootComplexTypes).setEntityType(complexType);
    esets.add(rootComplexTypesSet);

    // ---------------------------- Entity Type ----------------------------
    // adds the notion of Key
    // key is nullable because only base types specifiy the key

    props = new ArrayList<EdmProperty.Builder>();
    navprops = new ArrayList<EdmNavigationProperty.Builder>();

    ep = EdmProperty.newBuilder(Edm.EntityType.Key).setType(this.entityKeyType).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.EntityType.Documentation).setType(this.documentationType).setNullable(true);
    props.add(ep);

    EdmEntityType.Builder entityType = EdmEntityType.newBuilder()
        .setNamespace(Edm.namespace)
        .setName(Edm.EntityType.name())
        .setBaseType(structuralType)
        .addProperties(props)
        .addNavigationProperties(navprops);
    if (decorator != null) {
      entityType.setDocumentation(decorator.getDocumentationForEntityType(Edm.namespace, Edm.EntityType.name()));
      entityType.setAnnotations(decorator.getAnnotationsForEntityType(Edm.namespace, Edm.EntityType.name()));
    }

    etypes.add(entityType);

    EdmEntitySet.Builder entitySet = EdmEntitySet.newBuilder().setName(Edm.EntitySets.EntityTypes).setEntityType(entityType);
    esets.add(entitySet);

    EdmEntitySet.Builder rootEntitiesSet = EdmEntitySet.newBuilder().setName(Edm.EntitySets.RootEntityTypes).setEntityType(entityType);
    esets.add(rootEntitiesSet);

    // --------------------------- Property ------------------------------
    // model Property as an Entity so we can use the $expand mechanism to get
    // a lightweight view of the hierarchy or one that has all of the properties.
    props = new ArrayList<EdmProperty.Builder>();
    navprops = new ArrayList<EdmNavigationProperty.Builder>();

    ep = EdmProperty.newBuilder(Edm.Property.Namespace).setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.EntityTypeName).setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.Name).setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.Type).setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.Nullable).setType(EdmSimpleType.BOOLEAN).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.DefaultValue).setType(EdmSimpleType.STRING).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.MaxLength).setType(EdmSimpleType.INT32).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.FixedLength).setType(EdmSimpleType.BOOLEAN).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.Precision).setType(EdmSimpleType.INT16).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.Scale).setType(EdmSimpleType.INT16).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.Unicode).setType(EdmSimpleType.BOOLEAN).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.Collation).setType(EdmSimpleType.STRING).setNullable(true);
    props.add(ep);

    ep = EdmProperty.newBuilder(Edm.Property.ConcurrencyMode).setType(EdmSimpleType.STRING).setNullable(true);
    props.add(ep);

    keys = new ArrayList<String>();
    keys.add(Edm.Property.Namespace); // comes from the EntityType
    keys.add(Edm.Property.EntityTypeName);
    keys.add(Edm.Property.Name);
    EdmEntityType.Builder propertyType = EdmEntityType.newBuilder()
        .setNamespace(Edm.namespace)
        .setName(Edm.Property.name())
        .addKeys(keys)
        .addProperties(props)
        .addNavigationProperties(navprops);
    if (decorator != null) {
      propertyType.setDocumentation(decorator.getDocumentationForEntityType(Edm.namespace, Edm.Property.name()));
      propertyType.setAnnotations(decorator.getAnnotationsForEntityType(Edm.namespace, Edm.Property.name()));
    }

    etypes.add(propertyType);

    EdmEntitySet.Builder propertySet = EdmEntitySet.newBuilder().setName(Edm.EntitySets.Properties).setEntityType(propertyType);
    esets.add(propertySet);

    // Navigation Property
    // Schema ------------0..* (EntityTypes)----EntityType
    EdmAssociation.Builder assoc = defineAssociation(
        Edm.Schema.NavProps.EntityTypes,
        EdmMultiplicity.ONE,
        EdmMultiplicity.MANY,
        schemaType,
        schemaSet,
        structuralType,
        entitySet);

    EdmNavigationProperty.Builder navigationProperty = EdmNavigationProperty.newBuilder(assoc.getName()).setRelationship(assoc).setFromTo(assoc.getEnd1(),
        assoc.getEnd2());

    schemaType.addNavigationProperties(navigationProperty);

    // Schema ------------0..* (ComplexTypes)----ComplexTypes
    assoc = defineAssociation(
        Edm.Schema.NavProps.ComplexTypes,
        EdmMultiplicity.ONE,
        EdmMultiplicity.MANY,
        schemaType,
        schemaSet,
        complexType,
        complexSet);

    navigationProperty = EdmNavigationProperty.newBuilder(assoc.getName())
        .setRelationship(assoc).setFromTo(assoc.getEnd1(), assoc.getEnd2());

    schemaType.addNavigationProperties(navigationProperty);

    // EntityType ------------0..* (Properties)----Property
    assoc = defineAssociation(
        Edm.EntityType.NavProps.Properties,
        EdmMultiplicity.ONE,
        EdmMultiplicity.MANY,
        structuralType,
        entitySet,
        propertyType,
        propertySet);

    navigationProperty = EdmNavigationProperty.newBuilder(assoc.getName()).setRelationship(assoc).setFromTo(
        assoc.getEnd1(),
        assoc.getEnd2());

    structuralType.addNavigationProperties(navigationProperty);

    // Navigation Property
    // EntityType ------------0..* (SubTypes)----EntityType
    assoc = defineAssociation(
        Edm.EntityType.NavProps.SubTypes,
        EdmMultiplicity.ONE,
        EdmMultiplicity.MANY,
        structuralType,
        entitySet,
        structuralType,
        entitySet);

    navigationProperty = EdmNavigationProperty.newBuilder(assoc.getName()).setRelationship(assoc)
        .setFromTo(
            assoc.getEnd1(),
            assoc.getEnd2());

    structuralType.addNavigationProperties(navigationProperty);

    // Navigation Property
    // EntityType ------------0..1 (SuperType)----EntityType
    assoc = defineAssociation(
        Edm.EntityType.NavProps.SuperType,
        EdmMultiplicity.ONE,
        EdmMultiplicity.ZERO_TO_ONE,
        structuralType,
        entitySet,
        structuralType,
        entitySet);

    navigationProperty = EdmNavigationProperty.newBuilder(assoc.getName())
        .setRelationship(assoc)
        .setFromTo(assoc.getEnd1(), assoc.getEnd2());

    structuralType.addNavigationProperties(navigationProperty);
  }

  private EdmAssociation.Builder defineAssociation(
      String assocName,
      EdmMultiplicity fromMult,
      EdmMultiplicity toMult,
      EdmEntityType.Builder fromEntityType,
      EdmEntitySet.Builder fromEntitySet,
      EdmEntityType.Builder toEntityType,
      EdmEntitySet.Builder toEntitySet) {

    // add EdmAssociation
    EdmAssociationEnd.Builder fromAssociationEnd = EdmAssociationEnd.newBuilder().setRole(fromEntityType.getName()).setType(fromEntityType).setMultiplicity(fromMult);
    String toAssociationRole = toEntityType.getName();
    if (toAssociationRole.equals(fromEntityType.getName())) {
      toAssociationRole = toAssociationRole + "1";
    }
    EdmAssociationEnd.Builder toAssociationEnd = EdmAssociationEnd.newBuilder().setRole(toAssociationRole).setType(toEntityType).setMultiplicity(toMult);
    EdmAssociation.Builder association = EdmAssociation.newBuilder().setNamespace(Edm.namespace).setName(assocName).setEnds(fromAssociationEnd, toAssociationEnd);

    // add EdmAssociationSet
    EdmAssociationSet.Builder associationSet = EdmAssociationSet.newBuilder()
        .setName(assocName)
        .setAssociation(association).setEnds(
            EdmAssociationSetEnd.newBuilder().setRole(fromAssociationEnd).setEntitySet(fromEntitySet),
            EdmAssociationSetEnd.newBuilder().setRole(toAssociationEnd).setEntitySet(toEntitySet));
    asets.add(associationSet);
    assocs.add(association);
    return association;
  }

}
