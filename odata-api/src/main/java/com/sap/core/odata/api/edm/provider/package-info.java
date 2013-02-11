/**
 * Entity Data Model Provider API
 * <p>Classes in this package are used to provide an EDM to the library as well as to the application. To do this the class {@link com.sap.core.odata.api.edm.provider.EdmProvider} has to be implemented.</p>
 * <p>Inside the OData library we are using a lazy loading concept which means the EdmProvider is only called for an element if it is needed. See some sample coding for an EdmProvider below</p>
 * <p>public class Provider extends EdmProvider {

  public static final String NAMESPACE_1 = "RefScenario";
  public static final String NAMESPACE_2 = "RefScenario2";

  private static final FullQualifiedName ENTITY_TYPE_1_1 = new FullQualifiedName(NAMESPACE_1, "Employee");
  private static final FullQualifiedName ENTITY_TYPE_1_BASE = new FullQualifiedName(NAMESPACE_1, "Base");
  private static final FullQualifiedName ENTITY_TYPE_1_4 = new FullQualifiedName(NAMESPACE_1, "Manager");
  private static final FullQualifiedName ENTITY_TYPE_2_1 = new FullQualifiedName(NAMESPACE_2, "Photo");

  private static final FullQualifiedName COMPLEX_TYPE_1 = new FullQualifiedName(NAMESPACE_1, "c_Location");
  private static final FullQualifiedName COMPLEX_TYPE_2 = new FullQualifiedName(NAMESPACE_1, "c_City");

  private static final FullQualifiedName ASSOCIATION_1_1 = new FullQualifiedName(NAMESPACE_1, "ManagerEmployees");

  private static final String ROLE_1_1 = "r_Employees";
  private static final String ROLE_1_4 = "r_Manager";

  private static final String ENTITY_CONTAINER_1 = "Container1";
  private static final String ENTITY_CONTAINER_2 = "Container2";

  private static final String ENTITY_SET_1_1 = "Employees";
  private static final String ENTITY_SET_1_4 = "Managers";
  private static final String ENTITY_SET_2_1 = "Photos";

  private static final String FUNCTION_IMPORT_1 = "EmployeeSearch";
  private static final String FUNCTION_IMPORT_2 = "AllLocations";

  @Override
  public List<Schema> getSchemas() throws ODataException {
    List<Schema> schemas = new ArrayList<Schema>();

    Schema schema = new Schema();
    schema.setNamespace(NAMESPACE_1);

    List<EntityType> entityTypes = new ArrayList<EntityType>();
    entityTypes.add(getEntityType(ENTITY_TYPE_1_1));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_4));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_BASE));
    schema.setEntityTypes(entityTypes);

    List<ComplexType> complexTypes = new ArrayList<ComplexType>();
    complexTypes.add(getComplexType(COMPLEX_TYPE_1));
    complexTypes.add(getComplexType(COMPLEX_TYPE_2));
    schema.setComplexTypes(complexTypes);

    List<Association> associations = new ArrayList<Association>();
    associations.add(getAssociation(ASSOCIATION_1_1));
    schema.setAssociations(associations);

    EntityContainer entityContainer = new EntityContainer();
    entityContainer.setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);

    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_1));
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_4));
    entityContainer.setEntitySets(entitySets);

    List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
    associationSets.add(getAssociationSet(ENTITY_CONTAINER_1, ASSOCIATION_1_1, ENTITY_SET_1_4, ROLE_1_4));
    entityContainer.setAssociationSets(associationSets);

    List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_1));
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_2));
    entityContainer.setFunctionImports(functionImports);

    schema.setEntityContainers(Arrays.asList(entityContainer));

    schemas.add(schema);

    schema = new Schema();
    schema.setNamespace(NAMESPACE_2);

    schema.setEntityTypes(Arrays.asList(getEntityType(ENTITY_TYPE_2_1)));

    entityContainer = new EntityContainer();
    entityContainer.setName(ENTITY_CONTAINER_2);
    entityContainer.setEntitySets(Arrays.asList(getEntitySet(ENTITY_CONTAINER_2, ENTITY_SET_2_1)));
    schema.setEntityContainers(Arrays.asList(entityContainer));

    schemas.add(schema);

    return schemas;
  }

  @Override
  public EntityType getEntityType(FullQualifiedName edmFQName) throws ODataException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace())) {
      if (ENTITY_TYPE_1_1.getName().equals(edmFQName.getName())) {
        List<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("EmployeeId").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false))
            .setMapping(new Mapping().setInternalName("getId")));
        properties.add(new SimpleProperty().setName("EmployeeName").setType(EdmSimpleTypeKind.String)
            .setCustomizableFeedMappings(new CustomizableFeedMappings()
                .setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
        properties.add(new SimpleProperty().setName("ManagerId").setType(EdmSimpleTypeKind.String)
            .setMapping(new Mapping().setInternalName("getManager.getId")));
        properties.add(new SimpleProperty().setName("RoomId").setType(EdmSimpleTypeKind.String)
            .setMapping(new Mapping().setInternalName("getRoom.getId")));
        properties.add(new SimpleProperty().setName("TeamId").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setMaxLength(2))
            .setMapping(new Mapping().setInternalName("getTeam.getId")));
        properties.add(new ComplexProperty().setName("Location").setType(COMPLEX_TYPE_1)
            .setFacets(new Facets().setNullable(false)));
        properties.add(new SimpleProperty().setName("Age").setType(EdmSimpleTypeKind.Int16));
        properties.add(new SimpleProperty().setName("EntryDate").setType(EdmSimpleTypeKind.DateTime)
            .setFacets(new Facets().setNullable(true))
            .setCustomizableFeedMappings(new CustomizableFeedMappings()
                .setFcTargetPath(EdmTargetPath.SYNDICATION_UPDATED)));
        properties.add(new SimpleProperty().setName("ImageUrl").setType(EdmSimpleTypeKind.String)
            .setMapping(new Mapping().setInternalName("getImageUri")));
        List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        navigationProperties.add(new NavigationProperty().setName("ne_Manager")
            .setRelationship(ASSOCIATION_1_1).setFromRole(ROLE_1_1).setToRole(ROLE_1_4));

        return new EntityType().setName(ENTITY_TYPE_1_1.getName())
            .setProperties(properties)
            .setHasStream(true)
            .setKey(getKey("EmployeeId"))
            .setNavigationProperties(navigationProperties)
            .setMapping(new Mapping().setMimeType("getImageType"));

      } else if (ENTITY_TYPE_1_BASE.getName().equals(edmFQName.getName())) {
        List<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(false).setDefaultValue("1")));
        properties.add(new SimpleProperty().setName("Name").setType(EdmSimpleTypeKind.String)
            .setCustomizableFeedMappings(new CustomizableFeedMappings()
                .setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));

        return new EntityType().setName(ENTITY_TYPE_1_BASE.getName())
            .setAbstract(true)
            .setProperties(properties)
            .setKey(getKey("Id"));

      } else if (ENTITY_TYPE_1_4.getName().equals(edmFQName.getName())) {
        List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        navigationProperties.add(new NavigationProperty().setName("nm_Employees")
            .setRelationship(ASSOCIATION_1_1).setFromRole(ROLE_1_4).setToRole(ROLE_1_1));

        return new EntityType().setName(ENTITY_TYPE_1_4.getName())
            .setBaseType(ENTITY_TYPE_1_1)
            .setHasStream(true)
            .setNavigationProperties(navigationProperties)
            .setMapping(new Mapping().setMimeType("getImageType"));

      } else if (NAMESPACE_2.equals(edmFQName.getNamespace())) {
        if (ENTITY_TYPE_2_1.getName().equals(edmFQName.getName())) {
          List<Property> properties = new ArrayList<Property>();
          properties.add(new SimpleProperty().setName("Id").setType(EdmSimpleTypeKind.Int32)
              .setFacets(new Facets().setNullable(false).setConcurrencyMode(EdmConcurrencyMode.Fixed)));
          properties.add(new SimpleProperty().setName("Name").setType(EdmSimpleTypeKind.String)
              .setCustomizableFeedMappings(new CustomizableFeedMappings()
                  .setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
          properties.add(new SimpleProperty().setName("Type").setType(EdmSimpleTypeKind.String)
              .setFacets(new Facets().setNullable(false)));
          properties.add(new SimpleProperty().setName("ImageUrl").setType(EdmSimpleTypeKind.String)
              .setCustomizableFeedMappings(new CustomizableFeedMappings()
                  .setFcTargetPath(EdmTargetPath.SYNDICATION_AUTHORURI))
              .setMapping(new Mapping().setInternalName("getImageUri")));
          properties.add(new SimpleProperty().setName("Image").setType(EdmSimpleTypeKind.Binary)
              .setMapping(new Mapping().setMimeType("getImageType")));
          properties.add(new SimpleProperty().setName("BinaryData").setType(EdmSimpleTypeKind.Binary)
              .setFacets(new Facets().setNullable(true))
              .setMimeType("image/jpeg"));
          properties.add(new SimpleProperty().setName("Содержание").setType(EdmSimpleTypeKind.String)
              .setFacets(new Facets().setNullable(true))
              .setCustomizableFeedMappings(new CustomizableFeedMappings()
                  .setFcKeepInContent(false)
                  .setFcNsPrefix("ру") // CYRILLIC SMALL LETTER ER + CYRILLIC SMALL LETTER U
                  .setFcNsUri("http://localhost")
                  .setFcTargetPath("Содержание"))
              .setMapping(new Mapping().setInternalName("getContent")));

          return new EntityType().setName(ENTITY_TYPE_2_1.getName())
              .setProperties(properties)
              .setHasStream(true)
              .setKey(getKey("Id", "Type"))
              .setMapping(new Mapping().setMimeType("getType"));
        }
      }
    }
    return null;
  }

  @Override
  public ComplexType getComplexType(FullQualifiedName edmFQName) throws ODataException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
      if (COMPLEX_TYPE_1.getName().equals(edmFQName.getName())) {
        List<Property> properties = new ArrayList<Property>();
        properties.add(new ComplexProperty().setName("City").setType(COMPLEX_TYPE_2));
        properties.add(new SimpleProperty().setName("Country").setType(EdmSimpleTypeKind.String));
        return new ComplexType().setName(COMPLEX_TYPE_1.getName()).setProperties(properties);

      } else if (COMPLEX_TYPE_2.getName().equals(edmFQName.getName())) {
        List<Property> properties = new ArrayList<Property>();
        properties.add(new SimpleProperty().setName("PostalCode").setType(EdmSimpleTypeKind.String));
        properties.add(new SimpleProperty().setName("CityName").setType(EdmSimpleTypeKind.String));
        return new ComplexType().setName(COMPLEX_TYPE_2.getName()).setProperties(properties);
      }

    return null;
  }

  @Override
  public Association getAssociation(FullQualifiedName edmFQName) throws ODataException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace())) {
      if (ASSOCIATION_1_1.getName().equals(edmFQName.getName())) {
        return new Association().setName(ASSOCIATION_1_1.getName())
            .setEnd1(new AssociationEnd().setType(ENTITY_TYPE_1_1).setRole(ROLE_1_1).setMultiplicity(EdmMultiplicity.MANY))
            .setEnd2(new AssociationEnd().setType(ENTITY_TYPE_1_4).setRole(ROLE_1_4).setMultiplicity(EdmMultiplicity.ONE));
      }
    }
    return null;
  }

  @Override
  public EntityContainerInfo getEntityContainerInfo(String name) throws ODataException {
    if (name == null || ENTITY_CONTAINER_1.equals(name)) {
      return new EntityContainerInfo().setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);
    } else if (ENTITY_CONTAINER_2.equals(name)) {
      return new EntityContainerInfo().setName(name).setDefaultEntityContainer(false);
    }
    return null;
  }

  @Override
  public EntitySet getEntitySet(String entityContainer, String name) throws ODataException {
    if (ENTITY_CONTAINER_1.equals(entityContainer)) {
      if (ENTITY_SET_1_1.equals(name)) {
        return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_1);
      }
    } else if (ENTITY_CONTAINER_2.equals(entityContainer)) {
      if (ENTITY_SET_2_1.equals(name)) {
        return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_2_1);
      }
    }
    return null;
  }

  @Override
  public FunctionImport getFunctionImport(String entityContainer, String name) throws ODataException {
    if (ENTITY_CONTAINER_1.equals(entityContainer)) {
      if (FUNCTION_IMPORT_1.equals(name)) {
        List<FunctionImportParameter> parameters = new ArrayList<FunctionImportParameter>();
        parameters.add(new FunctionImportParameter().setName("q").setType(EdmSimpleTypeKind.String)
            .setFacets(new Facets().setNullable(true)));
        return new FunctionImport().setName(name)
            .setReturnType(new ReturnType().setTypeName(ENTITY_TYPE_1_1).setMultiplicity(EdmMultiplicity.MANY))
            .setEntitySet(ENTITY_SET_1_1)
            .setHttpMethod("GET")
            .setParameters(parameters);

      } else if (FUNCTION_IMPORT_2.equals(name)) {
        return new FunctionImport().setName(name)
            .setReturnType(new ReturnType().setTypeName(COMPLEX_TYPE_1).setMultiplicity(EdmMultiplicity.MANY))
            .setHttpMethod("GET");

      }
    }

    return null;
  }

  @Override
  public AssociationSet getAssociationSet(String entityContainer, FullQualifiedName association, String sourceEntitySetName, String sourceEntitySetRole) throws ODataException {
    if (ENTITY_CONTAINER_1.equals(entityContainer))
      if (ASSOCIATION_1_1.equals(association))
        return new AssociationSet().setName(ASSOCIATION_1_1.getName())
            .setAssociation(ASSOCIATION_1_1)
            .setEnd1(new AssociationSetEnd().setRole(ROLE_1_4).setEntitySet(ENTITY_SET_1_4))
            .setEnd2(new AssociationSetEnd().setRole(ROLE_1_1).setEntitySet(ENTITY_SET_1_1));

    return null;
  }

  private Key getKey(String... keyNames) {
    List<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
    for (String keyName : keyNames)
      keyProperties.add(new PropertyRef().setName(keyName));
    return new Key().setKeys(keyProperties);
  }
}
</p>
 */
package com.sap.core.odata.api.edm.provider;

