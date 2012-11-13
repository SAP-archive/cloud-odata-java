package com.sap.core.odata.ref.edm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.FullQualifiedName;
import com.sap.core.odata.api.edm.provider.Association;
import com.sap.core.odata.api.edm.provider.AssociationEnd;
import com.sap.core.odata.api.edm.provider.AssociationSet;
import com.sap.core.odata.api.edm.provider.AssociationSetEnd;
import com.sap.core.odata.api.edm.provider.ComplexType;
import com.sap.core.odata.api.edm.provider.CustomizableFeedMappings;
import com.sap.core.odata.api.edm.provider.EdmProvider;
import com.sap.core.odata.api.edm.provider.EntityContainer;
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
import com.sap.core.odata.core.exception.ODataRuntimeException;

/**
 * Provider for the entity data model used in the reference scenario
 * @author SAP AG
 */
public class ScenarioEdmProvider implements EdmProvider {

  private static final String NAMESPACE_EDM = EdmSimpleTypeFacade.edmNamespace;
  private static final String NAMESPACE_1 = "RefScenario";
  private static final String NAMESPACE_2 = "RefScenario2";

  private static final FullQualifiedName ENTITY_TYPE_1_1 = new FullQualifiedName("Employee", NAMESPACE_1);
  private static final FullQualifiedName ENTITY_TYPE_1_BASE = new FullQualifiedName("Base", NAMESPACE_1);
  private static final FullQualifiedName ENTITY_TYPE_1_2 = new FullQualifiedName("Team", NAMESPACE_1);
  private static final FullQualifiedName ENTITY_TYPE_1_3 = new FullQualifiedName("Room", NAMESPACE_1);
  private static final FullQualifiedName ENTITY_TYPE_1_4 = new FullQualifiedName("Manager", NAMESPACE_1);
  private static final FullQualifiedName ENTITY_TYPE_1_5 = new FullQualifiedName("Building", NAMESPACE_1);
  private static final FullQualifiedName ENTITY_TYPE_2_1 = new FullQualifiedName("Photo", NAMESPACE_2);

  private static final FullQualifiedName EDM_BINARY = new FullQualifiedName("Binary", NAMESPACE_EDM);
  private static final FullQualifiedName EDM_BOOLEAN = new FullQualifiedName("Boolean", NAMESPACE_EDM);
  private static final FullQualifiedName EDM_DATETIME = new FullQualifiedName("DateTime", NAMESPACE_EDM);
  private static final FullQualifiedName EDM_INT16 = new FullQualifiedName("Int16", NAMESPACE_EDM);
  private static final FullQualifiedName EDM_INT32 = new FullQualifiedName("Int32", NAMESPACE_EDM);
  private static final FullQualifiedName EDM_STRING = new FullQualifiedName("String", NAMESPACE_EDM);

  private static final FullQualifiedName COMPLEX_TYPE_1 = new FullQualifiedName("c_Location", NAMESPACE_1);
  private static final FullQualifiedName COMPLEX_TYPE_2 = new FullQualifiedName("c_City", NAMESPACE_1);

  private static final FullQualifiedName ASSOCIATION_1_1 = new FullQualifiedName("ManagerEmployees", NAMESPACE_1);
  private static final FullQualifiedName ASSOCIATION_1_2 = new FullQualifiedName("TeamEmployees", NAMESPACE_1);
  private static final FullQualifiedName ASSOCIATION_1_3 = new FullQualifiedName("RoomEmployees", NAMESPACE_1);
  private static final FullQualifiedName ASSOCIATION_1_4 = new FullQualifiedName("BuildingRooms", NAMESPACE_1);

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
  public Collection<Schema> getSchemas() throws ODataRuntimeException, ODataMessageException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EntityType getEntityType(final FullQualifiedName edmFQName) throws ODataRuntimeException, ODataMessageException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
      if (ENTITY_TYPE_1_1.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("EmployeeId", new Property().setName("EmployeeId").setType(EDM_STRING).setFacets(getFacets(false, null, null, null)));
        properties.put("EmployeeName", new Property().setName("EmployeeName").setType(EDM_STRING).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SyndicationTitle)));
        properties.put("ManagerId", new Property().setName("ManagerId").setType(EDM_STRING));
        properties.put("TeamId", new Property().setName("TeamId").setType(EDM_STRING).setFacets(getFacets(null, 2, null, null)));
        properties.put("RoomId", new Property().setName("RoomId").setType(EDM_STRING));
        properties.put("Location", new Property().setName("Location").setType(COMPLEX_TYPE_1));
        properties.put("Age", new Property().setName("Age").setType(EDM_INT16));
        properties.put("EntryDate", new Property().setName("EntryDate").setType(EDM_DATETIME).setFacets(getFacets(true, null, null, null)).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SyndicationUpdated)));
        properties.put("ImageUrl", new Property().setName("ImageUrl").setType(EDM_STRING));
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("ne_Manager", new NavigationProperty().setName("ne_Manager").setRelationship(ASSOCIATION_1_1).setFromRole(ROLE_1_1).setToRole(ROLE_1_4));
        navigationProperties.put("ne_Team", new NavigationProperty().setName("ne_Team").setRelationship(ASSOCIATION_1_2).setFromRole(ROLE_1_1).setToRole(ROLE_1_2));
        navigationProperties.put("ne_Room", new NavigationProperty().setName("ne_Room").setRelationship(ASSOCIATION_1_3).setFromRole(ROLE_1_1).setToRole(ROLE_1_3));
        return new EntityType().setName(ENTITY_TYPE_1_1.getName()).setAbstract(false).setProperties(properties).setHasStream(true).setKey(getKey("EmployeeId")).setNavigationProperties(navigationProperties);

      } else if (ENTITY_TYPE_1_BASE.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("Id", new Property().setName("Id").setType(EDM_STRING).setFacets(getFacets(false, null, "1", null)));
        properties.put("Name", new Property().setName("Name").setType(EDM_STRING).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SyndicationTitle)));
        return new EntityType().setName(ENTITY_TYPE_1_BASE.getName()).setAbstract(true).setProperties(properties).setHasStream(false).setKey(getKey("Id"));

      } else if (ENTITY_TYPE_1_2.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("isScrumTeam", new Property().setName("isScrumTeam").setType(EDM_BOOLEAN).setFacets(getFacets(true, null, null, null)));
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("nt_Employees", new NavigationProperty().setName("nt_Employees").setRelationship(ASSOCIATION_1_2).setFromRole(ROLE_1_2).setToRole(ROLE_1_1));
        return new EntityType().setName(ENTITY_TYPE_1_2.getName()).setBaseType(ENTITY_TYPE_1_BASE).setAbstract(false).setProperties(properties).setHasStream(false).setNavigationProperties(navigationProperties);

      } else if (ENTITY_TYPE_1_3.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("Seats", new Property().setName("Seats").setType(EDM_INT16));
        properties.put("Version", new Property().setName("Version").setType(EDM_INT16).setFacets(getFacets(null, null, null, true)));
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("nr_Employees", new NavigationProperty().setName("nr_Employees").setRelationship(ASSOCIATION_1_3).setFromRole(ROLE_1_3).setToRole(ROLE_1_1));
        navigationProperties.put("nr_Building", new NavigationProperty().setName("nr_Building").setRelationship(ASSOCIATION_1_4).setFromRole(ROLE_1_3).setToRole(ROLE_1_5));
        return new EntityType().setName(ENTITY_TYPE_1_3.getName()).setBaseType(ENTITY_TYPE_1_BASE).setAbstract(false).setProperties(properties).setHasStream(true).setNavigationProperties(navigationProperties);

      } else if (ENTITY_TYPE_1_4.getName().equals(edmFQName.getName())) {
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("nm_Employees", new NavigationProperty().setName("nm_Employees").setRelationship(ASSOCIATION_1_1).setFromRole(ROLE_1_4).setToRole(ROLE_1_1));
        return new EntityType().setName(ENTITY_TYPE_1_4.getName()).setBaseType(ENTITY_TYPE_1_1).setAbstract(false).setHasStream(true).setNavigationProperties(navigationProperties);

      } else if (ENTITY_TYPE_1_5.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("Id", new Property().setName("Id").setType(EDM_STRING).setFacets(getFacets(false, null, null, null)));
        properties.put("Name", new Property().setName("Name").setType(EDM_STRING));
        properties.put("Image", new Property().setName("Image").setType(EDM_BINARY));
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("nb_Rooms", new NavigationProperty().setName("nb_Rooms").setRelationship(ASSOCIATION_1_4).setFromRole(ROLE_1_5).setToRole(ROLE_1_3));
        return new EntityType().setName(ENTITY_TYPE_1_5.getName()).setAbstract(false).setProperties(properties).setHasStream(false).setKey(getKey("Id")).setNavigationProperties(navigationProperties);

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else if (NAMESPACE_2.equals(edmFQName.getNamespace()))
      if (ENTITY_TYPE_2_1.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("Id", new Property().setName("Id").setType(EDM_INT32).setFacets(getFacets(false, null, null, true)));
        properties.put("Name", new Property().setName("Name").setType(EDM_STRING).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SyndicationTitle)));
        properties.put("Type", new Property().setName("Type").setType(EDM_STRING).setFacets(getFacets(false, null, null, null)));
        properties.put("ImageUrl", new Property().setName("ImageUrl").setType(EDM_STRING).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcTargetPath(EdmTargetPath.SyndicationAuthorUri)));
        properties.put("Image", new Property().setName("Image").setType(EDM_BINARY));
        properties.put("BinaryData", new Property().setName("BinaryData").setType(EDM_BINARY).setFacets(getFacets(true, null, null, null)).setMimeType("image/jpeg"));
        properties.put("Содержание", new Property().setName("Содержание").setType(EDM_STRING).setFacets(getFacets(true, null, null, null)).setCustomizableFeedMappings(new CustomizableFeedMappings().setFcKeepInContent(false).setFcNsPrefix("py").setFcNsUri("http://localhost").setCustomTargetPath("Содержание")));
        return new EntityType().setName(ENTITY_TYPE_2_1.getName()).setAbstract(false).setProperties(properties).setHasStream(true).setKey(getKey("Id", "Type"));

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public ComplexType getComplexType(final FullQualifiedName edmFQName) throws ODataRuntimeException, ODataMessageException {
    if (NAMESPACE_1.equals(edmFQName.getNamespace()))
      if (COMPLEX_TYPE_1.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("City", new Property().setName("City").setType(COMPLEX_TYPE_2));
        properties.put("Country", new Property().setName("Country").setType(EDM_STRING));
        return new ComplexType().setName(COMPLEX_TYPE_1.getName()).setAbstract(false).setProperties(properties);

      } else if (COMPLEX_TYPE_2.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("PostalCode", new Property().setName("PostalCode").setType(EDM_STRING));
        properties.put("CityName", new Property().setName("CityName").setType(EDM_STRING));
        return new ComplexType().setName(COMPLEX_TYPE_2.getName()).setAbstract(false).setProperties(properties);

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public Association getAssociation(final FullQualifiedName edmFQName) throws ODataRuntimeException, ODataMessageException {
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
  public EntityContainer getEntityContainer(final String name) throws ODataRuntimeException, ODataMessageException {
    if (name == null || ENTITY_CONTAINER_1.equals(name))
      return new EntityContainer().setName(ENTITY_CONTAINER_1).setDefaultEntityContainer(true);
    else if (ENTITY_CONTAINER_2.equals(name))
      return new EntityContainer().setName(name).setDefaultEntityContainer(false);
    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataRuntimeException, ODataMessageException {
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
  public FunctionImport getFunctionImport(final String entityContainer, final String name) throws ODataRuntimeException, ODataMessageException {
    if (ENTITY_CONTAINER_1.equals(entityContainer))
      if (FUNCTION_IMPORT_1.equals(name)) {
        Map<String, FunctionImportParameter> parameters = new HashMap<String, FunctionImportParameter>();
        parameters.put("q", new FunctionImportParameter().setName("q").setQualifiedName(EDM_STRING).setFacets(getFacets(true, null, null, null)));
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(new FullQualifiedName("Employee", NAMESPACE_1)).setMultiplicity(EdmMultiplicity.MANY)).setEntitySet(ENTITY_SET_1_1).setHttpMethod("GET").setParameters(parameters);

      } else if (FUNCTION_IMPORT_2.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(COMPLEX_TYPE_1).setMultiplicity(EdmMultiplicity.MANY)).setHttpMethod("GET");

      } else if (FUNCTION_IMPORT_3.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(EDM_STRING).setMultiplicity(EdmMultiplicity.MANY)).setHttpMethod("GET");

      } else if (FUNCTION_IMPORT_4.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(EDM_INT16).setMultiplicity(EdmMultiplicity.ONE)).setHttpMethod("GET");

      } else if (FUNCTION_IMPORT_5.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(COMPLEX_TYPE_1).setMultiplicity(EdmMultiplicity.ONE)).setHttpMethod("GET");

      } else if (FUNCTION_IMPORT_6.equals(name)) {
        Map<String, FunctionImportParameter> parameters = new HashMap<String, FunctionImportParameter>();
        parameters.put("Id", new FunctionImportParameter().setName("Id").setQualifiedName(EDM_STRING).setFacets(getFacets(false, null, null, null)));
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(EDM_BINARY).setMultiplicity(EdmMultiplicity.ONE)).setHttpMethod("GET").setParameters(parameters);

      } else if (FUNCTION_IMPORT_7.equals(name)) {
        return new FunctionImport().setName(name).setReturnType(new ReturnType().setQualifiedName(new FullQualifiedName("Employee", NAMESPACE_1)).setMultiplicity(EdmMultiplicity.ZERO_TO_ONE)).setEntitySet(ENTITY_SET_1_1).setHttpMethod("GET");

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public AssociationSet getAssociationSet(final String entityContainer, final FullQualifiedName association, final String sourceEntitySetName, final String sourceEntitySetRole) throws ODataRuntimeException, ODataMessageException {
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
    Map<String, PropertyRef> keyProperties = new HashMap<String, PropertyRef>();
    for (final String keyName : keyNames)
      keyProperties.put(keyName, new PropertyRef().setName(keyName));
    return new Key().setKeys(keyProperties);
  }

  private EdmFacets getFacets(final Boolean nullable, final Integer maxLength, final String defaultValue, final Boolean forEtag) {
    final EdmConcurrencyMode edmConcurrencyMode =
        forEtag == null ? null : forEtag ? EdmConcurrencyMode.Fixed : EdmConcurrencyMode.None;

    return new Facets().setNullable(nullable).setDefaultValue(defaultValue).setMaxLength(maxLength).setConcurrencyMode(edmConcurrencyMode);
  }
}
