package com.sap.core.odata.ref.edm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.CustomizableFeedMappings;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EdmProviderDefault;
import com.sap.core.odata.api.edm.provider.EntityContainer;
import com.sap.core.odata.api.edm.provider.EntityContainerInfo;
import com.sap.core.odata.api.edm.provider.EntitySet;
import com.sap.core.odata.api.edm.provider.EntityType;
import com.sap.core.odata.api.edm.provider.Facets;
import com.sap.core.odata.api.edm.provider.FunctionImport;
import com.sap.core.odata.api.edm.provider.FunctionImportParameter;
import com.sap.core.odata.api.edm.provider.Key;
import com.sap.core.odata.api.edm.provider.NavigationProperty;
import com.sap.core.odata.api.edm.provider.Property;
import com.sap.core.odata.api.edm.provider.PropertyRef;
import com.sap.core.odata.api.edm.provider.ReturnType;
import com.sap.core.odata.api.edm.provider.Schema;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.exception.ODataNotFoundException;

/**
 * Provider for the entity data model used in the reference scenario
 * @author SAP AG
 */
public class ScenarioEdmProvider extends EdmProviderDefault {

  public static final String NAMESPACE_1 = "RefScenario";
  public static final String NAMESPACE_2 = "RefScenario2";

  private static final FullQualifiedName ENTITY_TYPE_1_1 = new FullQualifiedName(NAMESPACE_1, "Employee");
  private static final FullQualifiedName ENTITY_TYPE_1_BASE = new FullQualifiedName(NAMESPACE_1, "Base");
  private static final FullQualifiedName ENTITY_TYPE_1_2 = new FullQualifiedName(NAMESPACE_1, "Team");
  private static final FullQualifiedName ENTITY_TYPE_1_3 = new FullQualifiedName(NAMESPACE_1, "Room");
  private static final FullQualifiedName ENTITY_TYPE_1_4 = new FullQualifiedName(NAMESPACE_1, "Manager");
  private static final FullQualifiedName ENTITY_TYPE_1_5 = new FullQualifiedName(NAMESPACE_1, "Building");
  private static final FullQualifiedName ENTITY_TYPE_2_1 = new FullQualifiedName(NAMESPACE_2, "Photo");

  private static final FullQualifiedName COMPLEX_TYPE_1 = new FullQualifiedName(NAMESPACE_1, "c_Location");
  private static final FullQualifiedName COMPLEX_TYPE_2 = new FullQualifiedName(NAMESPACE_1, "c_City");

  private static final FullQualifiedName ASSOCIATION_1_1 = new FullQualifiedName(NAMESPACE_1, "ManagerEmployees");
  private static final FullQualifiedName ASSOCIATION_1_2 = new FullQualifiedName(NAMESPACE_1, "TeamEmployees");
  private static final FullQualifiedName ASSOCIATION_1_3 = new FullQualifiedName(NAMESPACE_1, "RoomEmployees");
  private static final FullQualifiedName ASSOCIATION_1_4 = new FullQualifiedName(NAMESPACE_1, "BuildingRooms");

  private static final String ROLE_1_1 = "r_Employees";
  private static final String ROLE_1_2 = "r_Team";
  private static final String ROLE_1_3 = "r_Room";
  private static final String ROLE_1_4 = "r_Manager";
  private static final String ROLE_1_5 = "r_Building";

  private static final String ENTITY_CONTAINER_1 = "Container1";
  private static final String ENTITY_CONTAINER_2 = "Container2";

  private static final String ENTITY_SET_1_1 = "Employees";
  private static final String ENTITY_SET_1_2 = "Teams";
  private static final String ENTITY_SET_1_3 = "Rooms";
  private static final String ENTITY_SET_1_4 = "Managers";
  private static final String ENTITY_SET_1_5 = "Buildings";
  private static final String ENTITY_SET_2_1 = "Photos";

  private static final String FUNCTION_IMPORT_1 = "EmployeeSearch";
  private static final String FUNCTION_IMPORT_2 = "AllLocations";
  private static final String FUNCTION_IMPORT_3 = "AllUsedRoomIds";
  private static final String FUNCTION_IMPORT_4 = "MaximalAge";
  private static final String FUNCTION_IMPORT_5 = "MostCommonLocation";
  private static final String FUNCTION_IMPORT_6 = "ManagerPhoto";
  private static final String FUNCTION_IMPORT_7 = "OldestEmployee";

  @Override
  public Collection<Schema> getSchemas() throws ODataMessageException {
    Collection<Schema> schemas = new ArrayList<Schema>();

    Schema schema = new Schema();
    schema.setNamespace(NAMESPACE_1);

    List<EntityType> entityTypes = new ArrayList<EntityType>();
    entityTypes.add(getEntityType(ENTITY_TYPE_1_1));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_BASE));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_2));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_3));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_4));
    entityTypes.add(getEntityType(ENTITY_TYPE_1_5));
    schema.setEntityTypes(entityTypes);

    List<ComplexType> complexTypes = new ArrayList<ComplexType>();
    complexTypes.add(getComplexType(COMPLEX_TYPE_1));
    complexTypes.add(getComplexType(COMPLEX_TYPE_2));
    schema.setComplexTypes(complexTypes);

    List<Association> associations = new ArrayList<Association>();
    associations.add(getAssociation(ASSOCIATION_1_1));
    associations.add(getAssociation(ASSOCIATION_1_2));
    associations.add(getAssociation(ASSOCIATION_1_3));
    associations.add(getAssociation(ASSOCIATION_1_4));
    schema.setAssociations(associations);

    List<EntityContainer> entityContainers = new ArrayList<EntityContainer>();
    EntityContainer entityContainer = new EntityContainer();
    entityContainer.setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);

    List<EntitySet> entitySets = new ArrayList<EntitySet>();
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_1));
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_2));
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_3));
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_4));
    entitySets.add(getEntitySet(ENTITY_CONTAINER_1, ENTITY_SET_1_5));
    entityContainer.setEntitySets(entitySets);

    List<AssociationSet> associationSets = new ArrayList<AssociationSet>();
    associationSets.add(getAssociationSet(ENTITY_CONTAINER_1, ASSOCIATION_1_1, ENTITY_SET_1_4, ROLE_1_4));
    associationSets.add(getAssociationSet(ENTITY_CONTAINER_1, ASSOCIATION_1_2, ENTITY_SET_1_2, ROLE_1_2));
    associationSets.add(getAssociationSet(ENTITY_CONTAINER_1, ASSOCIATION_1_3, ENTITY_SET_1_3, ROLE_1_3));
    associationSets.add(getAssociationSet(ENTITY_CONTAINER_1, ASSOCIATION_1_4, ENTITY_SET_1_5, ROLE_1_5));
    entityContainer.setAssociationSets(associationSets);

    List<FunctionImport> functionImports = new ArrayList<FunctionImport>();
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_1));
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_2));
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_3));
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_4));
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_5));
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_6));
    functionImports.add(getFunctionImport(ENTITY_CONTAINER_1, FUNCTION_IMPORT_7));
    entityContainer.setFunctionImports(functionImports);

    entityContainers.add(entityContainer);
    schema.setEntityContainers(entityContainers);

    schemas.add(schema);

    schema = new Schema();
    schema.setNamespace(NAMESPACE_2);

    entityTypes = new ArrayList<EntityType>();
    entityTypes.add(getEntityType(ENTITY_TYPE_2_1));
    schema.setEntityTypes(entityTypes);

    entityContainers = new ArrayList<EntityContainer>();
    entityContainer = new EntityContainer();
    entityContainer.setName(ENTITY_CONTAINER_2);

    entitySets = new ArrayList<EntitySet>();
    entitySets.add(getEntitySet(ENTITY_CONTAINER_2, ENTITY_SET_2_1));
    entityContainer.setEntitySets(entitySets);

    entitySets = new ArrayList<EntitySet>();
    entitySets.add(getEntitySet(ENTITY_CONTAINER_2, ENTITY_SET_2_1));

    entityContainers.add(entityContainer);
    schema.setEntityContainers(entityContainers);

    schemas.add(schema);

    return schemas;
  }

  @Override
  public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataMessageException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
      if (ENTITY_TYPE_1_1.getName().equals(edmFQName.getName())) {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new Property().setName("EmployeeId").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setFacets(new Facets().setNullable(false)));
        properties.add(new Property().setName("EmployeeName").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
        properties.add(new Property().setName("ManagerId").setType(EdmSimpleTypeKind.String.getFullqualifiedName()));
        properties.add(new Property().setName("TeamId").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setFacets(new Facets().setMaxLength(2)));
        properties.add(new Property().setName("RoomId").setType(EdmSimpleTypeKind.String.getFullqualifiedName()));
        properties.add(new Property().setName("Location").setType(COMPLEX_TYPE_1));
        properties.add(new Property().setName("Age").setType(EdmSimpleTypeKind.Int16.getFullqualifiedName()));
        properties.add(new Property().setName("EntryDate").setType(EdmSimpleTypeKind.DateTime.getFullqualifiedName()).setFacets(new Facets().setNullable(true)).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SYNDICATION_UPDATED)));
        properties.add(new Property().setName("ImageUrl").setType(EdmSimpleTypeKind.String.getFullqualifiedName()));
        Collection<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        navigationProperties.add(new NavigationProperty().setName("ne_Manager").setRelationship(ASSOCIATION_1_1).setFromRole(ROLE_1_1).setToRole(ROLE_1_4));
        navigationProperties.add(new NavigationProperty().setName("ne_Team").setRelationship(ASSOCIATION_1_2).setFromRole(ROLE_1_1).setToRole(ROLE_1_2));
        navigationProperties.add(new NavigationProperty().setName("ne_Room").setRelationship(ASSOCIATION_1_3).setFromRole(ROLE_1_1).setToRole(ROLE_1_3));
        return new EntityType().setName(ENTITY_TYPE_1_1.getName()).setAbstract(false).setProperties(properties).setHasStream(true).setKey(getKey("EmployeeId")).setNavigationProperties(navigationProperties);

      } else if (ENTITY_TYPE_1_BASE.getName().equals(edmFQName.getName())) {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new Property().setName("Id").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setFacets(new Facets().setNullable(false).setDefaultValue("1")));
        properties.add(new Property().setName("Name").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
        return new EntityType().setName(ENTITY_TYPE_1_BASE.getName()).setAbstract(true).setProperties(properties).setHasStream(false).setKey(getKey("Id"));

      } else if (ENTITY_TYPE_1_2.getName().equals(edmFQName.getName())) {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new Property().setName("isScrumTeam").setType(EdmSimpleTypeKind.Boolean.getFullqualifiedName()).setFacets(new Facets().setNullable(true)));
        Collection<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        navigationProperties.add(new NavigationProperty().setName("nt_Employees").setRelationship(ASSOCIATION_1_2).setFromRole(ROLE_1_2).setToRole(ROLE_1_1));
        return new EntityType().setName(ENTITY_TYPE_1_2.getName()).setBaseType(ENTITY_TYPE_1_BASE).setAbstract(false).setProperties(properties).setHasStream(false).setNavigationProperties(navigationProperties);

      } else if (ENTITY_TYPE_1_3.getName().equals(edmFQName.getName())) {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new Property().setName("Seats").setType(EdmSimpleTypeKind.Int16.getFullqualifiedName()));
        properties.add(new Property().setName("Version").setType(EdmSimpleTypeKind.Int16.getFullqualifiedName()).setFacets(new Facets().setConcurrencyMode(EdmConcurrencyMode.Fixed)));
        Collection<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        navigationProperties.add(new NavigationProperty().setName("nr_Employees").setRelationship(ASSOCIATION_1_3).setFromRole(ROLE_1_3).setToRole(ROLE_1_1));
        navigationProperties.add(new NavigationProperty().setName("nr_Building").setRelationship(ASSOCIATION_1_4).setFromRole(ROLE_1_3).setToRole(ROLE_1_5));
        return new EntityType().setName(ENTITY_TYPE_1_3.getName()).setBaseType(ENTITY_TYPE_1_BASE).setAbstract(false).setProperties(properties).setHasStream(true).setNavigationProperties(navigationProperties);

      } else if (ENTITY_TYPE_1_4.getName().equals(edmFQName.getName())) {
        Collection<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        navigationProperties.add(new NavigationProperty().setName("nm_Employees").setRelationship(ASSOCIATION_1_1).setFromRole(ROLE_1_4).setToRole(ROLE_1_1));
        return new EntityType().setName(ENTITY_TYPE_1_4.getName()).setBaseType(ENTITY_TYPE_1_1).setAbstract(false).setHasStream(true).setNavigationProperties(navigationProperties);

      } else if (ENTITY_TYPE_1_5.getName().equals(edmFQName.getName())) {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new Property().setName("Id").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setFacets(new Facets().setNullable(false)));
        properties.add(new Property().setName("Name").setType(EdmSimpleTypeKind.String.getFullqualifiedName()));
        properties.add(new Property().setName("Image").setType(EdmSimpleTypeKind.Binary.getFullqualifiedName()));
        Collection<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();
        navigationProperties.add(new NavigationProperty().setName("nb_Rooms").setRelationship(ASSOCIATION_1_4).setFromRole(ROLE_1_5).setToRole(ROLE_1_3));
        return new EntityType().setName(ENTITY_TYPE_1_5.getName()).setAbstract(false).setProperties(properties).setHasStream(false).setKey(getKey("Id")).setNavigationProperties(navigationProperties);

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else if (NAMESPACE_2.equals(edmFQName.getNamespace()))
      if (ENTITY_TYPE_2_1.getName().equals(edmFQName.getName())) {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new Property().setName("Id").setType(EdmSimpleTypeKind.Int32.getFullqualifiedName()).setFacets(new Facets().setNullable(false).setConcurrencyMode(EdmConcurrencyMode.Fixed)));
        properties.add(new Property().setName("Name").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SYNDICATION_TITLE)));
        properties.add(new Property().setName("Type").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setFacets(new Facets().setNullable(false)));
        properties.add(new Property().setName("ImageUrl").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SYNDICATION_AUTHORURI)));
        properties.add(new Property().setName("Image").setType(EdmSimpleTypeKind.Binary.getFullqualifiedName()));
        properties.add(new Property().setName("BinaryData").setType(EdmSimpleTypeKind.Binary.getFullqualifiedName()).setFacets(new Facets().setNullable(true)).setMimeType("image/jpeg"));
        properties.add(new Property().setName("Содержание").setType(EdmSimpleTypeKind.String.getFullqualifiedName()).setFacets(new Facets().setNullable(true)).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcKeepInContent(false).setFcNsPrefix("py").setFcNsUri("http://localhost").setFcTargetPath("Содержание")));
        return new EntityType().setName(ENTITY_TYPE_2_1.getName()).setAbstract(false).setProperties(properties).setHasStream(true).setKey(getKey("Id", "Type"));

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataMessageException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
      if (COMPLEX_TYPE_1.getName().equals(edmFQName.getName())) {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new Property().setName("City").setType(COMPLEX_TYPE_2));
        properties.add(new Property().setName("Country").setType(EdmSimpleTypeKind.String.getFullqualifiedName()));
        return new ComplexType().setName(COMPLEX_TYPE_1.getName()).setAbstract(false).setProperties(properties);

      } else if (COMPLEX_TYPE_2.getName().equals(edmFQName.getName())) {
        Collection<Property> properties = new ArrayList<Property>();
        properties.add(new Property().setName("PostalCode").setType(EdmSimpleTypeKind.String.getFullqualifiedName()));
        properties.add(new Property().setName("CityName").setType(EdmSimpleTypeKind.String.getFullqualifiedName()));
        return new ComplexType().setName(COMPLEX_TYPE_2.getName()).setAbstract(false).setProperties(properties);

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public Association getAssociation(final FullQualifiedName edmFQName) throws ODataMessageException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
      if (ASSOCIATION_1_1.getName().equals(edmFQName.getName()))
        return new Association().setName(ASSOCIATION_1_1.getName()).setEnd1(new AssociationEnd().setType(ENTITY_TYPE_1_1).setRole(ROLE_1_1).setMultiplicity(EdmMultiplicity.MANY)).setEnd2(new AssociationEnd().setType(ENTITY_TYPE_1_4).setRole(ROLE_1_4).setMultiplicity(EdmMultiplicity.ONE));
      else if (ASSOCIATION_1_2.getName().equals(edmFQName.getName()))
        return new Association().setName(ASSOCIATION_1_2.getName()).setEnd1(new AssociationEnd().setType(ENTITY_TYPE_1_1).setRole(ROLE_1_1).setMultiplicity(EdmMultiplicity.MANY)).setEnd2(new AssociationEnd().setType(ENTITY_TYPE_1_2).setRole(ROLE_1_2).setMultiplicity(EdmMultiplicity.ONE));
      else if (ASSOCIATION_1_3.getName().equals(edmFQName.getName()))
        return new Association().setName(ASSOCIATION_1_3.getName()).setEnd1(new AssociationEnd().setType(ENTITY_TYPE_1_1).setRole(ROLE_1_1).setMultiplicity(EdmMultiplicity.MANY)).setEnd2(new AssociationEnd().setType(ENTITY_TYPE_1_3).setRole(ROLE_1_3).setMultiplicity(EdmMultiplicity.ONE));
      else if (ASSOCIATION_1_4.getName().equals(edmFQName.getName()))
        return new Association().setName(ASSOCIATION_1_4.getName()).setEnd1(new AssociationEnd().setType(ENTITY_TYPE_1_5).setRole(ROLE_1_5).setMultiplicity(EdmMultiplicity.ONE)).setEnd2(new AssociationEnd().setType(ENTITY_TYPE_1_3).setRole(ROLE_1_3).setMultiplicity(EdmMultiplicity.MANY));
      else
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public EntityContainerInfo getEntityContainer(final String name) throws ODataMessageException {
    if (name == null || ENTITY_CONTAINER_1.equals(name))
      return new EntityContainerInfo().setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);
    else if (ENTITY_CONTAINER_2.equals(name))
      return new EntityContainerInfo().setName(name).setDefaultEntityContainer(false);
    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataMessageException {
    if (ENTITY_CONTAINER_1.equals(entityContainer))
      if (ENTITY_SET_1_1.equals(name))
        return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_1);
      else if (ENTITY_SET_1_2.equals(name))
        return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_2);
      else if (ENTITY_SET_1_3.equals(name))
        return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_3);
      else if (ENTITY_SET_1_4.equals(name))
        return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_4);
      else if (ENTITY_SET_1_5.equals(name))
        return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_1_5);
      else
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    else if (ENTITY_CONTAINER_2.equals(entityContainer))
      if (ENTITY_SET_2_1.equals(name))
        return new EntitySet().setName(name).setEntityType(ENTITY_TYPE_2_1);
      else
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataMessageException {
    if (ENTITY_CONTAINER_1.equals(entityContainer))
      if (FUNCTION_IMPORT_1.equals(name)) {
        Collection<FunctionImportParameter> parameters = new ArrayList<FunctionImportParameter>();
        parameters.add(new FunctionImportParameter().setName("q").setQualifiedName(EdmSimpleTypeKind.String.getFullqualifiedName()).setFacets(new Facets().setNullable(true)));
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(ENTITY_TYPE_1_1).setMultiplicity(EdmMultiplicity.MANY)).setEntitySet(ENTITY_SET_1_1).setHttpMethod("GET").setParameters(parameters);

      } else if (FUNCTION_IMPORT_2.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(COMPLEX_TYPE_1).setMultiplicity(EdmMultiplicity.MANY)).setHttpMethod("GET");

      } else if (FUNCTION_IMPORT_3.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(EdmSimpleTypeKind.String.getFullqualifiedName()).setMultiplicity(EdmMultiplicity.MANY)).setHttpMethod("GET");

      } else if (FUNCTION_IMPORT_4.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(EdmSimpleTypeKind.Int16.getFullqualifiedName()).setMultiplicity(EdmMultiplicity.ONE)).setHttpMethod("GET");

      } else if (FUNCTION_IMPORT_5.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(COMPLEX_TYPE_1).setMultiplicity(EdmMultiplicity.ONE)).setHttpMethod("GET");

      } else if (FUNCTION_IMPORT_6.equals(name)) {
        Collection<FunctionImportParameter> parameters = new ArrayList<FunctionImportParameter>();
        parameters.add(new FunctionImportParameter().setName("Id").setQualifiedName(EdmSimpleTypeKind.String.getFullqualifiedName()).setFacets(new Facets().setNullable(false)));
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(EdmSimpleTypeKind.Binary.getFullqualifiedName()).setMultiplicity(EdmMultiplicity.ONE)).setHttpMethod("GET").setParameters(parameters);

      } else if (FUNCTION_IMPORT_7.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(new FullQualifiedName(NAMESPACE_1, "Employee")).setMultiplicity(EdmMultiplicity.ZERO_TO_ONE)).setEntitySet(ENTITY_SET_1_1).setHttpMethod("GET");

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association, final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataMessageException {
    if (ENTITY_CONTAINER_1.equals(entityContainer))
      if (ASSOCIATION_1_1.equals(association))
        return new AssociationSet().setName(ASSOCIATION_1_1.getName()).setAssociation(ASSOCIATION_1_1).setEnd1(new AssociationSetEnd().setRole(ROLE_1_4).setEntitySet(ENTITY_SET_1_4)).setEnd2(new AssociationSetEnd().setRole(ROLE_1_1).setEntitySet(ENTITY_SET_1_1));
      else if (ASSOCIATION_1_2.equals(association))
        return new AssociationSet().setName(ASSOCIATION_1_2.getName()).setAssociation(ASSOCIATION_1_2).setEnd1(new AssociationSetEnd().setRole(ROLE_1_2).setEntitySet(ENTITY_SET_1_2)).setEnd2(new AssociationSetEnd().setRole(ROLE_1_1).setEntitySet(ENTITY_SET_1_1));
      else if (ASSOCIATION_1_3.equals(association))
        return new AssociationSet().setName(ASSOCIATION_1_3.getName()).setAssociation(ASSOCIATION_1_3).setEnd1(new AssociationSetEnd().setRole(ROLE_1_3).setEntitySet(ENTITY_SET_1_3)).setEnd2(new AssociationSetEnd().setRole(ROLE_1_1).setEntitySet(ENTITY_SET_1_1));
      else if (ASSOCIATION_1_4.equals(association))
        return new AssociationSet().setName(ASSOCIATION_1_4.getName()).setAssociation(ASSOCIATION_1_4).setEnd1(new AssociationSetEnd().setRole(ROLE_1_5).setEntitySet(ENTITY_SET_1_5)).setEnd2(new AssociationSetEnd().setRole(ROLE_1_3).setEntitySet(ENTITY_SET_1_3));
      else
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  private Key getKey(final String... keyNames) {
    Collection<PropertyRef> keyProperties = new ArrayList<PropertyRef>();
    for (final String keyName : keyNames)
      keyProperties.add(new PropertyRef().setName(keyName));
    return new Key().setKeys(keyProperties);
  }
}
