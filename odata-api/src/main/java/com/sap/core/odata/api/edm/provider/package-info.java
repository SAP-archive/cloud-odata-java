/**
 * (c) 2013 by SAP AG
 */
/**
 * Entity Data Model Provider API
 * <p>Classes in this package are used to provide an EDM to the library as well as to the application. To do this the class {@link com.sap.core.odata.api.edm.provider.EdmProvider} has to be implemented.</p>
 * <p>Inside the OData library we are using a lazy loading concept which means the EdmProvider is only called for an element if it is needed. See some sample coding for an EdmProvider below</p>
 * <p>public class Provider extends EdmProvider {
  <p>public static final String NAMESPACE_1 = "RefScenario";
  <br/>public static final String NAMESPACE_2 = "RefScenario2";
  <br/>private static final FullQualifiedName ENTITY_TYPE_1_1 = new FullQualifiedName(NAMESPACE_1, "Employee");
  <br/>private static final FullQualifiedName ENTITY_TYPE_1_BASE = new FullQualifiedName(NAMESPACE_1, "Base");
  <br/>private static final FullQualifiedName ENTITY_TYPE_1_4 = new FullQualifiedName(NAMESPACE_1, "Manager");
  <br/>private static final FullQualifiedName ENTITY_TYPE_2_1 = new FullQualifiedName(NAMESPACE_2, "Photo");
  <br/>private static final FullQualifiedName COMPLEX_TYPE_1 = new FullQualifiedName(NAMESPACE_1, "c_Location");
  <br/>private static final FullQualifiedName COMPLEX_TYPE_2 = new FullQualifiedName(NAMESPACE_1, "c_City");
  <br/>private static final FullQualifiedName ASSOCIATION_1_1 = new FullQualifiedName(NAMESPACE_1, "ManagerEmployees");
  <br/>private static final String ROLE_1_1 = "r_Employees";
  <br/>private static final String ROLE_1_4 = "r_Manager";
  <br/>private static final String ENTITY_CONTAINER_1 = "Container1";
  <br/>private static final String ENTITY_CONTAINER_2 = "Container2";
  <br/>private static final String ENTITY_SET_1_1 = "Employees";
  <br/>private static final String ENTITY_SET_1_4 = "Managers";
  <br/>private static final String ENTITY_SET_2_1 = "Photos";
  <br/>private static final String FUNCTION_IMPORT_1 = "EmployeeSearch";
  <br/>private static final String FUNCTION_IMPORT_2 = "AllLocations";
  </p>
  <p>public List<Schema> getSchemas() throws ODataException {
    <p>List<Schema> schemas = new ArrayList<Schema>();
    <br/>Schema schema = new Schema();
    <br/>schema.setNamespace(NAMESPACE_1);

    <br/>List<EntityType> entityTypes = new ArrayList<EntityType>();
    <br/>entityTypes.add(getEntityType(ENTITY_TYPE_1_1));
    <br/>entityTypes.add(getEntityType(ENTITY_TYPE_1_4));
    <br/>entityTypes.add(getEntityType(ENTITY_TYPE_1_BASE));
    <br/>schema.setEntityTypes(entityTypes);

    <br/>List<ComplexType> complexTypes = new ArrayList<ComplexType>();
    <br/>complexTypes.add(getComplexType(COMPLEX_TYPE_1));
    <br/>complexTypes.add(getComplexType(COMPLEX_TYPE_2));
    <br/>schema.setComplexTypes(complexTypes);

    <br/>List<Association> associations = new ArrayList<Association>();
    <br/>associations.add(getAssociation(ASSOCIATION_1_1));
    <br/>schema.setAssociations(associations);

    <br/>EntityContainer entityContainer = new EntityContainer();
    <br/>entityContainer.setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);

    <br/>List<EntitySet> entitySets = new ArrayList<EntitySet>();
    <br/>entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_1));
    <br/>entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_4));
    <br/>entityContainer.setEntitySets(entitySets);

    <br/>List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
    <br/>associationSets.add(getAssociationSet(ENTITY_CONTAINER_1, ASSOCIATION_1_1, ENTITY_SET_1_4, ROLE_1_4));
    <br/>entityContainer.setAssociationSets(associationSets);

    <br/>List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
    <br/>functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_1));
    <br/>functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_2));
    <br/>entityContainer.setFunctionImports(functionImports);

    <br/>schema.setEntityContainers(Arrays.asList(entityContainer));

    <br/>schemas.add(schema);
    </p>
    <p>schema = new Schema();
    <br/>schema.setNamespace(NAMESPACE_2);

    <br/>schema.setEntityTypes(Arrays.asList(getEntityType(ENTITY_TYPE_2_1)));

    <br/>entityContainer = new EntityContainer();
    <br/>entityContainer.setName(ENTITY_CONTAINER_2);
    <br/>entityContainer.setEntitySets(Arrays.asList(getEntitySet(ENTITY_CONTAINER_2, ENTITY_SET_2_1)));
    <br/>schema.setEntityContainers(Arrays.asList(entityContainer));

    <br/>schemas.add(schema);
</p>
    <p>return schemas;</p>
  }

  <p>public EntityType getEntityType(FullQualifiedName edmFQName) throws ODataException {
    <p>if (NAMESPACE_1.equals(edmFQName.getNamespace())) {
      <br/>if (ENTITY_TYPE_1_1.getName().equals(edmFQName.getName())) {
        <br/>List<Property> properties = new ArrayList<Property>();
        <br/>properties.add(new SimpleProperty().setName("EmployeeId").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false))
            .setMapping(new Mapping().setInternalName("getId")));
        <br/>properties.add(new SimpleProperty().setName("EmployeeName").setType(EdmSimpleTypeKind.String)
            .setCustomizableFeedMappings(new CustomizableFeedMappings()
                .setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
        <br/>properties.add(new SimpleProperty().setName("ManagerId").setType(EdmSimpleTypeKind.String)
            .setMapping(new Mapping().setInternalName("getManager.getId")));
        <br/>properties.add(new SimpleProperty().setName("RoomId").setType(EdmSimpleTypeKind.String)
            .setMapping(new Mapping().setInternalName("getRoom.getId")));
        <br/>properties.add(new SimpleProperty().setName("TeamId").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setMaxLength(2))
            .setMapping(new Mapping().setInternalName("getTeam.getId")));
        <br/>properties.add(new ComplexProperty().setName("Location").setType(COMPLEX_TYPE_1)
            .setFacets(new Facets().setNullable(false)));
        <br/>properties.add(new SimpleProperty().setName("Age").setType(EdmSimpleTypeKind.Int16));
        <br/>properties.add(new SimpleProperty().setName("EntryDate").setType(EdmSimpleTypeKind.DateTime)
            .setFacets(new Facets().setNullable(true))
            .setCustomizableFeedMappings(new CustomizableFeedMappings()
                .setFcTargetPath(EdmTargetPath.SYNDICATION_UPDATED)));
        <br/>properties.add(new SimpleProperty().setName("ImageUrl").setType(EdmSimpleTypeKind.String)
            .setMapping(new Mapping().setInternalName("getImageUri")));
        <br/>List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        <br/>navigationProperties.add(new NavigationProperty().setName("ne_Manager")
            .setRelationship(ASSOCIATION_1_1).setFromRole(ROLE_1_1).setToRole(ROLE_1_4));

        <br/>return new EntityType().setName(ENTITY_TYPE_1_1.getName())
            .setProperties(properties)
            .setHasStream(true)
            .setKey(getKey("EmployeeId"))
            .setNavigationProperties(navigationProperties)
            .setMapping(new Mapping().setMimeType("getImageType"));

      <p>} else if (ENTITY_TYPE_1_BASE.getName().equals(edmFQName.getName())) {
        <br/>List<Property> properties = new ArrayList<Property>();
        <br/>properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false).setDefaultValue("1")));
        <br/>properties.add(new SimpleProperty().setName("Name").setType(EdmSimpleTypeKind.String)
            .setCustomizableFeedMappings(new CustomizableFeedMappings()
                .setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));

        <br/>return new EntityType().setName(ENTITY_TYPE_1_BASE.getName())
            .setAbstract(true)
            .setProperties(properties)
            .setKey(getKey("Id"));

      <p>} else if (ENTITY_TYPE_1_4.getName().equals(edmFQName.getName())) {
        <br/>List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        <br/>navigationProperties.add(new NavigationProperty().setName("nm_Employees")
            .setRelationship(ASSOCIATION_1_1).setFromRole(ROLE_1_4).setToRole(ROLE_1_1));

        <br/>return new EntityType().setName(ENTITY_TYPE_1_4.getName())
            .setBaseType(ENTITY_TYPE_1_1)
            .setHasStream(true)
            .setNavigationProperties(navigationProperties)
            .setMapping(new Mapping().setMimeType("getImageType"));

      <p>} else if (NAMESPACE_2.equals(edmFQName.getNamespace())) {
        <br/>if (ENTITY_TYPE_2_1.getName().equals(edmFQName.getName())) {
          <br/>List<Property> properties = new ArrayList<Property>();
          <br/>properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32)
              .setFacets(new Facets().setNullable(false).setConcurrencyMode(EdmConcurrencyMode.Fixed)));
          <br/>properties.add(new SimpleProperty().setName("Name").setType(EdmSimpleTypeKind.String)
              .setCustomizableFeedMappings(new CustomizableFeedMappings()
                  .setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
          <br/>properties.add(new SimpleProperty().setName("Type").setType(EdmSimpleTypeKind.String)
              .setFacets(new Facets().setNullable(false)));
          <br/>properties.add(new SimpleProperty().setName("ImageUrl").setType(EdmSimpleTypeKind.String)
              .setCustomizableFeedMappings(new CustomizableFeedMappings()
                  .setFcTargetPath(EdmTargetPath.SYNDICATION_AUTHORURI))
              .setMapping(new Mapping().setInternalName("getImageUri")));
          <br/>properties.add(new SimpleProperty().setName("Image").setType(EdmSimpleTypeKind.Binary)
              .setMapping(new Mapping().setMimeType("getImageType")));
          <br/>properties.add(new SimpleProperty().setName("BinaryData").setType(EdmSimpleTypeKind.Binary)
              .setFacets(new Facets().setNullable(true))
              .setMimeType("image/jpeg"));
          <br/>properties.add(new SimpleProperty().setName("Содержание").setType(EdmSimpleTypeKind.String)
              .setFacets(new Facets().setNullable(true))
              .setCustomizableFeedMappings(new CustomizableFeedMappings()
                  .setFcKeepInContent(false)
                  .setFcNsPrefix("ру") // CYRILLIC SMALL LETTER ER + CYRILLIC SMALL LETTER U
                  .setFcNsUri("http://localhost")
                  .setFcTargetPath("Содержание"))
              .setMapping(new Mapping().setInternalName("getContent")));

          <br/>return new EntityType().setName(ENTITY_TYPE_2_1.getName())
              .setProperties(properties)
              .setHasStream(true)
              .setKey(getKey("Id", "Type"))
              .setMapping(new Mapping().setMimeType("getType"));
        }
      }
    }
    <p>return null;
  }

  <p>public ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataException {
    <br/>if (NAMESPACE_1.equals(edmFQName.getNamespace()))
      <br/>if (COMPLEX_TYPE_1.getName().equals(edmFQName.getName())) {
        <br/>List<Property> properties = new ArrayList<Property>();
        <br/>properties.add(new ComplexProperty().setName("City").setType(COMPLEX_TYPE_2));
        <br/>properties.add(new SimpleProperty().setName("Country").setType(EdmSimpleTypeKind.String));
        <br/>return new ComplexType().setName(COMPLEX_TYPE_1.getName()).setProperties(properties);

      } <br/>else if (COMPLEX_TYPE_2.getName().equals(edmFQName.getName())) {
        <br/>List<Property> properties = new ArrayList<Property>();
        <br/>properties.add(new SimpleProperty().setName("PostalCode").setType(EdmSimpleTypeKind.String));
        <br/>properties.add(new SimpleProperty().setName("CityName").setType(EdmSimpleTypeKind.String));
        <br/>return new ComplexType().setName(COMPLEX_TYPE_2.getName()).setProperties(properties);
      }

    <br/>return null;
  }

  <p>public Association getAssociation(FullQualifiedName edmFQName) throws ODataException {
    <br/>if (NAMESPACE_1.equals(edmFQName.getNamespace())) {
      <br/>if (ASSOCIATION_1_1.getName().equals(edmFQName.getName())) {
        <br/>return new Association().setName(ASSOCIATION_1_1.getName())
            .setEnd1(new AssociationEnd().setType(ENTITY_TYPE_1_1).setRole(ROLE_1_1).setMultiplicity(EdmMultiplicity.MANY))
            .setEnd2(new AssociationEnd().setType(ENTITY_TYPE_1_4).setRole(ROLE_1_4).setMultiplicity(EdmMultiplicity.ONE));
      }
    }
    <br/>return null;
  }

  <p>public EntityContainerInfo getEntityContainerInfo(String name) throws ODataException {
    <br/>if (name == null || ENTITY_CONTAINER_1.equals(name)) {
      <br/>return new EntityContainerInfo().setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);
    } <br/>else if (ENTITY_CONTAINER_2.equals(name)) {
      <br/>return new EntityContainerInfo().setName(name).setDefaultEntityContainer(false);
    }
    <br/>return null;
  }

  <p>public EntitySet getEntitySet(String entityContainer, String name) throws ODataException {
    <br/>if (ENTITY_CONTAINER_1.equals(entityContainer)) {
      <br/>if (ENTITY_SET_1_1.equals(name)) {
        <br/>return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_1);
      }
    } <br/>else if (ENTITY_CONTAINER_2.equals(entityContainer)) {
      <br/>if (ENTITY_SET_2_1.equals(name)) {
        <br/>return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_2_1);
      }
    }
    <br/>return null;
  }

  <p>public FunctionImport getFunctionImport(String entityContainer, String name) throws ODataException {
    <br/>if (ENTITY_CONTAINER_1.equals(entityContainer)) {
      <br/>if (FUNCTION_IMPORT_1.equals(name)) {
        <br/>List<FunctionImportParameter> parameters = new ArrayList<FunctionImportParameter>();
        <br/>parameters.add(new FunctionImportParameter().setName("q").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(true)));
        <br/>return new FunctionImport().setName(name)
            .setReturnType(new ReturnType().setTypeName(ENTITY_TYPE_1_1).setMultiplicity(EdmMultiplicity.MANY))
            .setEntitySet(ENTITY_SET_1_1)
            .setHttpMethod("GET")
            .setParameters(parameters);

      } <br/>else if (FUNCTION_IMPORT_2.equals(name)) {
        <br/>return new FunctionImport().setName(name)
            .setReturnType(new ReturnType().setTypeName(COMPLEX_TYPE_1).setMultiplicity(EdmMultiplicity.MANY))
            .setHttpMethod("GET");

      }
    }

    <br/>return null;
  }

  <p>public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataException {
    <br/>if (ENTITY_CONTAINER_1.equals(entityContainer))
      <br/>if (ASSOCIATION_1_1.equals(association))
        <br/>return new AssociationSet().setName(ASSOCIATION_1_1.getName())
            .setAssociation(ASSOCIATION_1_1)
            .setEnd1(new AssociationSetEnd().setRole(ROLE_1_4).setEntitySet(ENTITY_SET_1_4))
            .setEnd2(new AssociationSetEnd().setRole(ROLE_1_1).setEntitySet(ENTITY_SET_1_1));

    <br/>return null;
  }
}
</p>
 */
package com.sap.core.odata.api.edm.provider;

