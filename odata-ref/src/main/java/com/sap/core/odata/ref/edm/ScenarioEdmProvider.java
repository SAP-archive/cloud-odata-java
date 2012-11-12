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
import com.sap.core.odata.api.exception.ODataRuntimeException;

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
        properties.put("EmployeeId", new Property("EmployeeId", EDM_STRING, getFacets(false, null, null, null), null, null, null, null, null));
        properties.put("EmployeeName", new Property("EmployeeName", EDM_STRING, null, new CustomizableFeedMappings(null, null, null, null, null, EdmTargetPath.SyndicationTitle, null), null, null, null, null));
        properties.put("ManagerId", new Property("ManagerId", EDM_STRING, null, null, null, null, null, null));
        properties.put("TeamId", new Property("TeamId", EDM_STRING, getFacets(null, 2, null, null), null, null, null, null, null));
        properties.put("RoomId", new Property("RoomId", EDM_STRING, null, null, null, null, null, null));
        properties.put("Location", new Property("Location", COMPLEX_TYPE_1, null, null, null, null, null, null));
        properties.put("Age", new Property("Age", EDM_INT16, null, null, null, null, null, null));
        properties.put("EntryDate", new Property("EntryDate", EDM_DATETIME, getFacets(true, null, null, null), new CustomizableFeedMappings(null, null, null, null, null, EdmTargetPath.SyndicationUpdated, null), null, null, null, null));
        properties.put("ImageUrl", new Property("ImageUrl", EDM_STRING, null, null, null, null, null, null));
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("ne_Manager", new NavigationProperty("ne_Manager", ASSOCIATION_1_1, ROLE_1_1, ROLE_1_4, null, null));
        navigationProperties.put("ne_Team", new NavigationProperty("ne_Team", ASSOCIATION_1_2, ROLE_1_1, ROLE_1_2, null, null));
        navigationProperties.put("ne_Room", new NavigationProperty("ne_Room", ASSOCIATION_1_3, ROLE_1_1, ROLE_1_3, null, null));
        return new EntityType(ENTITY_TYPE_1_1.getName(), null, false, properties, null, null, null, true, null, getKey("EmployeeId"), navigationProperties);

      } else if (ENTITY_TYPE_1_BASE.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("Id", new Property("Id", EDM_STRING, getFacets(false, null, "1", null), null, null, null, null, null));
        properties.put("Name", new Property("Name", EDM_STRING, null, new CustomizableFeedMappings(null, null, null, null, null, EdmTargetPath.SyndicationTitle, null), null, null, null, null));
        return new EntityType(ENTITY_TYPE_1_BASE.getName(), null, true, properties, null, null, null, false, null, getKey("Id"), null);

      } else if (ENTITY_TYPE_1_2.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("isScrumTeam", new Property("isScrumTeam", EDM_BOOLEAN, getFacets(true, null, null, null), null, null, null, null, null));
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("nt_Employees", new NavigationProperty("nt_Employees", ASSOCIATION_1_2, ROLE_1_2, ROLE_1_1, null, null));
        return new EntityType(ENTITY_TYPE_1_2.getName(), ENTITY_TYPE_1_BASE, false, properties, null, null, null, false, null, null, navigationProperties);

      } else if (ENTITY_TYPE_1_3.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("Seats", new Property("Seats", EDM_INT16, null, null, null, null, null, null));
        properties.put("Version", new Property("Version", EDM_INT16, getFacets(null, null, null, true), null, null, null, null, null));
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("nr_Employees", new NavigationProperty("nr_Employees", ASSOCIATION_1_3, ROLE_1_3, ROLE_1_1, null, null));
        navigationProperties.put("nr_Building", new NavigationProperty("nr_Building", ASSOCIATION_1_4, ROLE_1_3, ROLE_1_5, null, null));
        return new EntityType(ENTITY_TYPE_1_3.getName(), ENTITY_TYPE_1_BASE, false, properties, null, null, null, false, null, null, navigationProperties);

      } else if (ENTITY_TYPE_1_4.getName().equals(edmFQName.getName())) {
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("nm_Employees", new NavigationProperty("nm_Employees", ASSOCIATION_1_1, ROLE_1_4, ROLE_1_1, null, null));
        return new EntityType(ENTITY_TYPE_1_4.getName(), ENTITY_TYPE_1_1, false, null, null, null, null, true, null, null, navigationProperties);

      } else if (ENTITY_TYPE_1_5.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("Id", new Property("Id", EDM_STRING, getFacets(false, null, null, null), null, null, null, null, null));
        properties.put("Name", new Property("Name", EDM_STRING, null, null, null, null, null, null));
        properties.put("Image", new Property("Image", EDM_BINARY, null, null, null, null, null, null));
        Map<String, NavigationProperty> navigationProperties = new HashMap<String, NavigationProperty>();
        navigationProperties.put("nb_Rooms", new NavigationProperty("nb_Rooms", ASSOCIATION_1_4, ROLE_1_5, ROLE_1_3, null, null));
        return new EntityType(ENTITY_TYPE_1_5.getName(), null, false, properties, null, null, null, false, null, getKey("Id"), navigationProperties);

      } else {
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
      }

    else if (NAMESPACE_2.equals(edmFQName.getNamespace()))
      if (ENTITY_TYPE_2_1.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("Id", new Property("Id", EDM_INT32, getFacets(false, null, null, true), null, null, null, null, null));
        properties.put("Name", new Property("Name", EDM_STRING, null, new CustomizableFeedMappings(null, null, null, null, null, EdmTargetPath.SyndicationTitle, null), null, null, null, null));
        properties.put("Type", new Property("Type", EDM_STRING, getFacets(false, null, null, null), null, null, null, null, null));
        properties.put("ImageUrl", new Property("ImageUrl", EDM_STRING, null, new CustomizableFeedMappings(null, null, null, null, null, EdmTargetPath.SyndicationAuthorUri, null), null, null, null, null));
        properties.put("Image", new Property("Image", EDM_BINARY, null, null, null, null, null, null));
        properties.put("BinaryData", new Property("BinaryData", EDM_BINARY, getFacets(true, null, null, null), null, "image/jpeg", null, null, null));
        properties.put("Содержание", new Property("Содержание", EDM_STRING, getFacets(true, null, null, null), new CustomizableFeedMappings(false, null, "ру", "http://localhost", null, null, "Содержание"), null, null, null, null));
        return new EntityType(ENTITY_TYPE_2_1.getName(), null, false, properties, null, null, null, true, null, getKey("Id", "Type"), null);

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
        properties.put("City", new Property("City", COMPLEX_TYPE_2, null, null, null, null, null, null));
        properties.put("Country", new Property("Country", EDM_STRING, null, null, null, null, null, null));
        return new ComplexType(COMPLEX_TYPE_1.getName(), null, false, properties, null, null, null);

      } else if (COMPLEX_TYPE_2.getName().equals(edmFQName.getName())) {
        Map<String, Property> properties = new HashMap<String, Property>();
        properties.put("PostalCode", new Property("PostalCode", EDM_STRING, null, null, null, null, null, null));
        properties.put("CityName", new Property("CityName", EDM_STRING, null, null, null, null, null, null));
        return new ComplexType(COMPLEX_TYPE_2.getName(), null, false, properties, null, null, null);

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
        return new Association(ASSOCIATION_1_1.getName(), new AssociationEnd(ENTITY_TYPE_1_1, ROLE_1_1, EdmMultiplicity.MANY, null, null, null), new AssociationEnd(ENTITY_TYPE_1_4, ROLE_1_4, EdmMultiplicity.ONE, null, null, null), null, null, null);
      else if (ASSOCIATION_1_2.getName().equals(edmFQName.getName()))
        return new Association(ASSOCIATION_1_2.getName(), new AssociationEnd(ENTITY_TYPE_1_1, ROLE_1_1, EdmMultiplicity.MANY, null, null, null), new AssociationEnd(ENTITY_TYPE_1_2, ROLE_1_2, EdmMultiplicity.ONE, null, null, null), null, null, null);
      else if (ASSOCIATION_1_3.getName().equals(edmFQName.getName()))
        return new Association(ASSOCIATION_1_3.getName(), new AssociationEnd(ENTITY_TYPE_1_1, ROLE_1_1, EdmMultiplicity.MANY, null, null, null), new AssociationEnd(ENTITY_TYPE_1_3, ROLE_1_3, EdmMultiplicity.ONE, null, null, null), null, null, null);
      else if (ASSOCIATION_1_4.getName().equals(edmFQName.getName()))
        return new Association(ASSOCIATION_1_4.getName(), new AssociationEnd(ENTITY_TYPE_1_5, ROLE_1_5, EdmMultiplicity.ONE, null, null, null), new AssociationEnd(ENTITY_TYPE_1_3, ROLE_1_3, EdmMultiplicity.MANY, null, null, null), null, null, null);
      else
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public EntityContainer getEntityContainer(final String name) throws ODataRuntimeException, ODataMessageException {
    if (name == null || ENTITY_CONTAINER_1.equals(name))
      return new EntityContainer(ENTITY_CONTAINER_1, null, true);
    else if (ENTITY_CONTAINER_2.equals(name))
      return new EntityContainer(name, null, false);
    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  @Override
  public EntitySet getEntitySet(final String entityContainer, final String name) throws ODataRuntimeException, ODataMessageException {
    if (ENTITY_CONTAINER_1.equals(entityContainer))
      if (ENTITY_SET_1_1.equals(name))
        return new EntitySet(name, ENTITY_TYPE_1_1, null, null);
      else if (ENTITY_SET_1_2.equals(name))
        return new EntitySet(name, ENTITY_TYPE_1_2, null, null);
      else if (ENTITY_SET_1_3.equals(name))
        return new EntitySet(name, ENTITY_TYPE_1_3, null, null);
      else if (ENTITY_SET_1_4.equals(name))
        return new EntitySet(name, ENTITY_TYPE_1_4, null, null);
      else if (ENTITY_SET_1_5.equals(name))
        return new EntitySet(name, ENTITY_TYPE_1_5, null, null);
      else
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    else if (ENTITY_CONTAINER_2.equals(entityContainer))
      if (ENTITY_SET_2_1.equals(name))
        return new EntitySet(name, ENTITY_TYPE_2_1, null, null);
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
        parameters.put("q", new FunctionImportParameter("q", null, EDM_STRING, getFacets(true, null, null, null), null, null, null));
        return new FunctionImport(name, new ReturnType(new FullQualifiedName("Employee", NAMESPACE_1), EdmMultiplicity.MANY), ENTITY_SET_1_1, "GET", parameters, null, null);

      } else if (FUNCTION_IMPORT_2.equals(name)) {
        return new FunctionImport(name, new ReturnType(COMPLEX_TYPE_1, EdmMultiplicity.MANY), null, "GET", null, null, null);

      } else if (FUNCTION_IMPORT_3.equals(name)) {
        return new FunctionImport(name, new ReturnType(EDM_STRING, EdmMultiplicity.MANY), null, "GET", null, null, null);

      } else if (FUNCTION_IMPORT_4.equals(name)) {
        return new FunctionImport(name, new ReturnType(EDM_INT16, EdmMultiplicity.ONE), null, "GET", null, null, null);

      } else if (FUNCTION_IMPORT_5.equals(name)) {
        return new FunctionImport(name, new ReturnType(COMPLEX_TYPE_1, EdmMultiplicity.ONE), null, "GET", null, null, null);

      } else if (FUNCTION_IMPORT_6.equals(name)) {
        Map<String, FunctionImportParameter> parameters = new HashMap<String, FunctionImportParameter>();
        parameters.put("Id", new FunctionImportParameter("Id", null, EDM_STRING, getFacets(false, null, null, null), null, null, null));
        return new FunctionImport(name, new ReturnType(EDM_BINARY, EdmMultiplicity.ONE), null, "GET", parameters, null, null);

      } else if (FUNCTION_IMPORT_7.equals(name)) {
        return new FunctionImport(name, new ReturnType(new FullQualifiedName("Employee", NAMESPACE_1), EdmMultiplicity.ZERO_TO_ONE), ENTITY_SET_1_1, "GET", null, null, null);

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
        return new AssociationSet(ASSOCIATION_1_1.getName(), ASSOCIATION_1_1, new AssociationSetEnd(ROLE_1_4, ENTITY_SET_1_4, null, null), new AssociationSetEnd(ROLE_1_1, ENTITY_SET_1_1, null, null), null, null);
      else if (ASSOCIATION_1_2.equals(association))
        return new AssociationSet(ASSOCIATION_1_2.getName(), ASSOCIATION_1_2, new AssociationSetEnd(ROLE_1_2, ENTITY_SET_1_2, null, null), new AssociationSetEnd(ROLE_1_1, ENTITY_SET_1_1, null, null), null, null);
      else if (ASSOCIATION_1_3.equals(association))
        return new AssociationSet(ASSOCIATION_1_3.getName(), ASSOCIATION_1_3, new AssociationSetEnd(ROLE_1_3, ENTITY_SET_1_3, null, null), new AssociationSetEnd(ROLE_1_1, ENTITY_SET_1_1, null, null), null, null);
      else if (ASSOCIATION_1_4.equals(association))
        return new AssociationSet(ASSOCIATION_1_4.getName(), ASSOCIATION_1_4, new AssociationSetEnd(ROLE_1_5, ENTITY_SET_1_5, null, null), new AssociationSetEnd(ROLE_1_3, ENTITY_SET_1_3, null, null), null, null);
      else
        throw new ODataNotFoundException(ODataNotFoundException.ENTITY);

    else
      throw new ODataNotFoundException(ODataNotFoundException.ENTITY);
  }

  private Key getKey(final String... keyNames) {
    Map<String, PropertyRef> keyProperties = new HashMap<String, PropertyRef>();
    for (final String keyName : keyNames)
      keyProperties.put(keyName, new PropertyRef(keyName, null));
    return new Key(keyProperties, null);
  }

  private EdmFacets getFacets(final Boolean nullable, final Integer maxLength, final String defaultValue, final Boolean forEtag) {
    final EdmConcurrencyMode edmConcurrencyMode =
        forEtag == null ? null : forEtag ? EdmConcurrencyMode.Fixed : EdmConcurrencyMode.None;

    return new Facets(nullable, defaultValue, maxLength, null, null, null, null, null, edmConcurrencyMode);
  }
}