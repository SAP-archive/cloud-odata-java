package org.odata4j.test.integration.producer.custom;

import java.util.ArrayList;
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
import org.odata4j.edm.EdmFunctionImport;
import org.odata4j.edm.EdmFunctionParameter;
import org.odata4j.edm.EdmGenerator;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;
import org.odata4j.edm.EdmProperty;
import org.odata4j.edm.EdmProperty.CollectionKind;
import org.odata4j.edm.EdmSchema;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.producer.edm.Edm;

public class CustomEdm implements EdmGenerator {

  public static final String namespace = "myns";

  private EdmDecorator decorator = null;
  private EdmSchema.Builder schema = null;
  private EdmEntityContainer.Builder container = null;
  private List<EdmAssociation.Builder> associations = new LinkedList<EdmAssociation.Builder>();
  private List<EdmComplexType.Builder> complexTypes = new LinkedList<EdmComplexType.Builder>();
  private List<EdmEntityType.Builder> entityTypes = new LinkedList<EdmEntityType.Builder>();
  private List<EdmEntitySet.Builder> entitySets = new LinkedList<EdmEntitySet.Builder>();
  private List<EdmAssociationSet.Builder> associationSets = new LinkedList<EdmAssociationSet.Builder>();
  private EdmComplexType.Builder complexType = null;

  @Override
  public EdmDataServices.Builder generateEdm(EdmDecorator decorator) {
    createComplexTypes();
    createEntityTypes();

    container = EdmEntityContainer.newBuilder()
        .setName("Container1")
        .setIsDefault(true)
        .setLazyLoadingEnabled(Boolean.TRUE)
        .addEntitySets(entitySets)
        .addAssociationSets(associationSets);

    schema = EdmSchema.newBuilder()
        .setNamespace(namespace)
        .addEntityTypes(entityTypes)
        .addComplexTypes(complexTypes)
        .addAssociations(associations)
        .addEntityContainers(container);

    List<EdmFunctionParameter.Builder> params = new ArrayList<EdmFunctionParameter.Builder>(1);
    params.add(EdmFunctionParameter.newBuilder()
        .setName("p")
        .setType(EdmSimpleType.STRING)
        .setMode(EdmFunctionParameter.Mode.In));

    EdmFunctionImport.Builder f = EdmFunctionImport.newBuilder()
        .setName("f")
        .setHttpMethod("GET")
        .setReturnType(EdmSimpleType.BOOLEAN)
        .addParameters(params);

    container.addFunctionImports(f);

    return EdmDataServices.newBuilder().addSchemas(schema);
  }

  private void createComplexTypes() {
    // ----------------------------- ComplexType1 --------------------------
    List<EdmProperty.Builder> props = new ArrayList<EdmProperty.Builder>();

    EdmProperty.Builder ep = EdmProperty.newBuilder("Prop1").setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder("Prop2").setType(EdmSimpleType.STRING);
    props.add(ep);

    complexType = EdmComplexType.newBuilder().setNamespace(namespace).setName("ComplexType1").addProperties(props);
    complexTypes.add(complexType);
  }

  private void createEntityTypes() {
    // --------------------------- Type1 ------------------------------
    List<EdmProperty.Builder> props = new ArrayList<EdmProperty.Builder>();
    List<EdmNavigationProperty.Builder> navprops = new ArrayList<EdmNavigationProperty.Builder>();

    EdmProperty.Builder ep = null;

    ep = EdmProperty.newBuilder("EmptyStrings").setType(EdmSimpleType.STRING).setCollectionKind(CollectionKind.List);
    props.add(ep);

    ep = EdmProperty.newBuilder("ListOStrings").setType(EdmSimpleType.STRING).setCollectionKind(CollectionKind.List);
    props.add(ep);

    ep = EdmProperty.newBuilder("BagOStrings").setType(EdmSimpleType.STRING).setCollectionKind(CollectionKind.Bag);
    props.add(ep);

    ep = EdmProperty.newBuilder("BagOInts").setType(EdmSimpleType.INT32).setCollectionKind(CollectionKind.Bag);
    props.add(ep);

    ep = EdmProperty.newBuilder("Complex1").setType(complexType);
    props.add(ep);

    ep = EdmProperty.newBuilder("ListOComplex").setType(complexType).setCollectionKind(CollectionKind.List);
    props.add(ep);

    ep = EdmProperty.newBuilder("Id").setType(EdmSimpleType.STRING).setNullable(false);
    props.add(ep);

    List<String> keys = new ArrayList<String>();
    keys.add("Id");
    EdmEntityType.Builder type1Type = EdmEntityType.newBuilder()
        .setNamespace(namespace)
        .setName("Type1")
        .addKeys(keys)
        .addProperties(props)
        .addNavigationProperties(navprops);
    if (decorator != null) {
      type1Type.setDocumentation(decorator.getDocumentationForEntityType(Edm.namespace, Edm.EntityType.name()));
      type1Type.setAnnotations(decorator.getAnnotationsForEntityType(namespace, "Type1"));
    }

    entityTypes.add(type1Type);

    EdmEntitySet.Builder type1Set = EdmEntitySet.newBuilder().setName("Type1s").setEntityType(type1Type);
    entitySets.add(type1Set);

    // FileSystemItem
    //   - Directory
    //      nav Items: 0..* FileSystemItem
    //      nav NewestItem: 0..1 FileSystemItem
    //   - File

    // --------------------------- FileSystemItem ------------------------------
    props = new ArrayList<EdmProperty.Builder>();
    navprops = new ArrayList<EdmNavigationProperty.Builder>();

    ep = null;

    ep = EdmProperty.newBuilder("Name").setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder("Number").setType(EdmSimpleType.INT32);
    props.add(ep);

    keys = new ArrayList<String>();
    keys.add("Name");
    EdmEntityType.Builder fsiType = EdmEntityType.newBuilder()
        .setNamespace(namespace)
        .setName("FileSystemItem")
        .addKeys(keys)
        .addProperties(props)
        .addNavigationProperties(navprops);

    entityTypes.add(fsiType);

    EdmEntitySet.Builder fsiSet = EdmEntitySet.newBuilder().setName("FileSystemItems").setEntityType(fsiType);
    entitySets.add(fsiSet);

    // --------------------------- Directory ------------------------------
    props = new ArrayList<EdmProperty.Builder>();
    navprops = new ArrayList<EdmNavigationProperty.Builder>();

    ep = EdmProperty.newBuilder("DirProp1").setType(EdmSimpleType.STRING);
    props.add(ep);

    EdmEntityType.Builder dirType = EdmEntityType.newBuilder()
        .setNamespace(namespace)
        .setName("Directory")
        .setBaseType(fsiType)
        .addProperties(props);

    entityTypes.add(dirType);

    EdmEntitySet.Builder dirSet = EdmEntitySet.newBuilder().setName("Directories").setEntityType(dirType);
    entitySets.add(dirSet);

    EdmAssociation.Builder assoc = this.defineAssociation("Items", EdmMultiplicity.ONE, EdmMultiplicity.MANY, dirType, dirSet, fsiType, fsiSet);
    navprops.add(EdmNavigationProperty.newBuilder(assoc.getName())
        .setRelationship(assoc)
        .setFromTo(assoc.getEnd1(), assoc.getEnd2()));

    assoc = this.defineAssociation("NewestItem", EdmMultiplicity.ONE, EdmMultiplicity.ONE, dirType, dirSet, fsiType, fsiSet);
    navprops.add(EdmNavigationProperty.newBuilder(assoc.getName())
        .setRelationship(assoc)
        .setFromTo(assoc.getEnd1(), assoc.getEnd2()));

    dirType.addNavigationProperties(navprops);

    // --------------------------- File ------------------------------
    props = new ArrayList<EdmProperty.Builder>();
    navprops = new ArrayList<EdmNavigationProperty.Builder>();

    ep = EdmProperty.newBuilder("FileProp1").setType(EdmSimpleType.STRING);
    props.add(ep);

    EdmEntityType.Builder fileType = EdmEntityType.newBuilder()
        .setNamespace(namespace)
        .setName("File")
        .setBaseType(fsiType)
        .addProperties(props);

    entityTypes.add(fileType);

    EdmEntitySet.Builder fileSet = EdmEntitySet.newBuilder().setName("Files").setEntityType(fileType);
    entitySets.add(fileSet);

    // --------------------------- a Media Link Entry thing ------------------------------
    props = new ArrayList<EdmProperty.Builder>();
    navprops = new ArrayList<EdmNavigationProperty.Builder>();

    ep = EdmProperty.newBuilder("Id").setType(EdmSimpleType.STRING);
    props.add(ep);

    ep = EdmProperty.newBuilder("MLEProp1").setType(EdmSimpleType.STRING);
    props.add(ep);

    EdmEntityType.Builder mleType = EdmEntityType.newBuilder()
        .setNamespace(namespace)
        .setName("MLE")
        .addKeys("Id")
        .setHasStream(Boolean.TRUE)
        .addProperties(props);

    entityTypes.add(mleType);

    EdmEntitySet.Builder mleSet = EdmEntitySet.newBuilder().setName("MLEs").setEntityType(mleType);
    entitySets.add(mleSet);
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
    EdmAssociation.Builder association = EdmAssociation.newBuilder().setNamespace(namespace).setName(assocName).setEnds(fromAssociationEnd, toAssociationEnd);

    // add EdmAssociationSet
    EdmAssociationSet.Builder associationSet = EdmAssociationSet.newBuilder()
        .setName(assocName)
        .setAssociation(association).setEnds(
            EdmAssociationSetEnd.newBuilder().setRole(fromAssociationEnd).setEntitySet(fromEntitySet),
            EdmAssociationSetEnd.newBuilder().setRole(toAssociationEnd).setEntitySet(toEntitySet));
    associationSets.add(associationSet);
    associations.add(association);
    return association;
  }

}
