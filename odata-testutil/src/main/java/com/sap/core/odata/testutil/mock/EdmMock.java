package com.sap.core.odata.testutil.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.mockito.Mockito;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmMapping;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmStructuralType;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.provider.CustomizableFeedMappings;
import com.sap.core.odata.api.exception.ODataException;

/**
 * Mocked Entity Data Model, more or less aligned to the Reference Scenario.
 * @author SAP AG
 */
class EdmMock {

  public static Edm createMockEdm() throws ODataException {
    EdmEntityContainer defaultContainer = mock(EdmEntityContainer.class);
    when(defaultContainer.isDefaultEntityContainer()).thenReturn(true);

    final EdmEntitySet employeeEntitySet = createEntitySetMock(defaultContainer, "Employees", EdmSimpleTypeKind.String, "EmployeeId");
    final EdmEntitySet teamEntitySet = createEntitySetMock(defaultContainer, "Teams", EdmSimpleTypeKind.String, "Id");
    final EdmEntitySet roomEntitySet = createEntitySetMock(defaultContainer, "Rooms", EdmSimpleTypeKind.String, "Id");
    final EdmEntitySet managerEntitySet = createEntitySetMock(defaultContainer, "Managers", EdmSimpleTypeKind.String, "EmployeeId");
    final EdmEntitySet buildingEntitySet = createEntitySetMock(defaultContainer, "Buildings", EdmSimpleTypeKind.String, "Id");

    EdmEntityType employeeType = employeeEntitySet.getEntityType();
    when(employeeType.hasStream()).thenReturn(true);
    EdmMapping employeeTypeMapping = Mockito.mock(EdmMapping.class);
    when(employeeTypeMapping.getMimeType()).thenReturn("getImageType");
    when(employeeType.getMapping()).thenReturn(employeeTypeMapping);
    when(employeeType.getPropertyNames()).thenReturn(Arrays.asList(
        "EmployeeId", "EmployeeName", "ManagerId", "RoomId", "TeamId",
        "Location", "Age", "EntryDate", "ImageUrl"));
    when(employeeType.getNavigationPropertyNames()).thenReturn(Arrays.asList("ne_Manager", "ne_Team", "ne_Room"));

    EdmProperty employeeNameProperty = createProperty("EmployeeName", EdmSimpleTypeKind.String, employeeType);
    EdmCustomizableFeedMappings employeeTitleMappings = mock(EdmCustomizableFeedMappings.class);
    when(employeeTitleMappings.getFcTargetPath()).thenReturn(EdmTargetPath.SYNDICATION_TITLE);
    when(employeeTitleMappings.isFcKeepInContent()).thenReturn(true);
    when(employeeNameProperty.getCustomizableFeedMappings()).thenReturn(employeeTitleMappings);

    createProperty("ManagerId", EdmSimpleTypeKind.String, employeeType);
    createProperty("RoomId", EdmSimpleTypeKind.String, employeeType);
    createProperty("TeamId", EdmSimpleTypeKind.String, employeeType);
    createProperty("Age", EdmSimpleTypeKind.Int32, employeeType);

    EdmProperty employeeEntryDateProperty = createProperty("EntryDate", EdmSimpleTypeKind.DateTime, employeeType);
    EdmCustomizableFeedMappings employeeUpdatedMappings = mock(EdmCustomizableFeedMappings.class);
    when(employeeUpdatedMappings.getFcTargetPath()).thenReturn(EdmTargetPath.SYNDICATION_UPDATED);
    when(employeeUpdatedMappings.isFcKeepInContent()).thenReturn(true);
    when(employeeEntryDateProperty.getCustomizableFeedMappings()).thenReturn(employeeUpdatedMappings);
    EdmFacets employeeEntryDateFacets = mock(EdmFacets.class);
    when(employeeEntryDateFacets.getMaxLength()).thenReturn(null);
    when(employeeEntryDateFacets.isNullable()).thenReturn(Boolean.TRUE);
    when(employeeEntryDateProperty.getFacets()).thenReturn(employeeEntryDateFacets);

    createProperty("ImageUrl", EdmSimpleTypeKind.String, employeeType);

    final EdmComplexType locationComplexType = mock(EdmComplexType.class);
    when(locationComplexType.getKind()).thenReturn(EdmTypeKind.COMPLEX);
    when(locationComplexType.getName()).thenReturn("c_Location");
    when(locationComplexType.getNamespace()).thenReturn("RefScenario");
    when(locationComplexType.getPropertyNames()).thenReturn(Arrays.asList("City", "Country"));

    final EdmProperty locationComplexProperty = mock(EdmProperty.class);
    when(locationComplexProperty.getType()).thenReturn(locationComplexType);
    when(locationComplexProperty.getName()).thenReturn("Location");
    when(employeeType.getProperty("Location")).thenReturn(locationComplexProperty);
    createProperty("Country", EdmSimpleTypeKind.String, locationComplexType);

    final EdmComplexType cityComplexType = mock(EdmComplexType.class);
    when(cityComplexType.getKind()).thenReturn(EdmTypeKind.COMPLEX);
    when(cityComplexType.getName()).thenReturn("c_City");
    when(cityComplexType.getNamespace()).thenReturn("RefScenario");
    when(cityComplexType.getPropertyNames()).thenReturn(Arrays.asList("PostalCode", "CityName"));

    EdmProperty cityProperty = mock(EdmProperty.class);
    when(cityProperty.getType()).thenReturn(cityComplexType);
    when(cityProperty.getName()).thenReturn("City");
    when(locationComplexType.getProperty("City")).thenReturn(cityProperty);

    createProperty("PostalCode", EdmSimpleTypeKind.String, cityComplexType);
    createProperty("CityName", EdmSimpleTypeKind.String, cityComplexType);

    createNavigationProperty("ne_Manager", EdmMultiplicity.ONE, employeeEntitySet, managerEntitySet);
    createNavigationProperty("ne_Team", EdmMultiplicity.ONE, employeeEntitySet, teamEntitySet);
    createNavigationProperty("ne_Room", EdmMultiplicity.ONE, employeeEntitySet, roomEntitySet);

    EdmEntityType teamType = teamEntitySet.getEntityType();
    when(teamType.getPropertyNames()).thenReturn(Arrays.asList("Id", "Name", "isScrumTeam"));
    createProperty("Name", EdmSimpleTypeKind.String, teamType);
    createProperty("isScrumTeam", EdmSimpleTypeKind.Boolean, teamType);
    when(teamType.getNavigationPropertyNames()).thenReturn(Arrays.asList("nt_Employees"));
    createNavigationProperty("nt_Employees", EdmMultiplicity.MANY, teamEntitySet, employeeEntitySet);

    EdmEntityType roomType = roomEntitySet.getEntityType();
    when(roomType.getPropertyNames()).thenReturn(Arrays.asList("Id", "Name", "Seats", "Version"));
    EdmProperty roomId = roomEntitySet.getEntityType().getKeyProperties().get(0);
    EdmFacets roomIdFacets = mock(EdmFacets.class);
    when(roomIdFacets.getMaxLength()).thenReturn(100);
    when(roomId.getFacets()).thenReturn(roomIdFacets);
    createProperty("Name", EdmSimpleTypeKind.String, roomType);
    createProperty("Seats", EdmSimpleTypeKind.Int16, roomType);
    EdmProperty roomVersion = createProperty("Version", EdmSimpleTypeKind.Int16, roomType);
    EdmFacets roomVersionFacets = mock(EdmFacets.class);
    when(roomVersionFacets.getConcurrencyMode()).thenReturn(EdmConcurrencyMode.Fixed);
    when(roomVersion.getFacets()).thenReturn(roomVersionFacets);
    when(roomType.getNavigationPropertyNames()).thenReturn(Arrays.asList("nr_Employees", "nr_Building"));
    createNavigationProperty("nr_Employees", EdmMultiplicity.MANY, roomEntitySet, employeeEntitySet);
    createNavigationProperty("nr_Building", EdmMultiplicity.ONE, roomEntitySet, buildingEntitySet);

    createNavigationProperty("ne_Manager", EdmMultiplicity.ONE, managerEntitySet, managerEntitySet);
    createNavigationProperty("ne_Team", EdmMultiplicity.ONE, managerEntitySet, teamEntitySet);
    createNavigationProperty("ne_Room", EdmMultiplicity.ONE, managerEntitySet, roomEntitySet);
    createNavigationProperty("nm_Employees", EdmMultiplicity.MANY, managerEntitySet, employeeEntitySet);

    EdmEntityType buildingType = buildingEntitySet.getEntityType();
    when(buildingType.getPropertyNames()).thenReturn(Arrays.asList("Id", "Name", "Image"));
    createProperty("Name", EdmSimpleTypeKind.String, buildingType);
    createProperty("Image", EdmSimpleTypeKind.Binary, buildingType);
    when(buildingType.getNavigationPropertyNames()).thenReturn(Arrays.asList("nb_Rooms"));
    createNavigationProperty("nb_Rooms", EdmMultiplicity.MANY, buildingEntitySet, roomEntitySet);

    EdmFunctionImport employeeSearchFunctionImport = createFunctionImportMock(defaultContainer, "EmployeeSearch", employeeType, EdmMultiplicity.MANY);
    when(employeeSearchFunctionImport.getEntitySet()).thenReturn(employeeEntitySet);
    EdmParameter employeeSearchParameter = mock(EdmParameter.class);
    when(employeeSearchParameter.getType()).thenReturn(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
    when(employeeSearchFunctionImport.getParameterNames()).thenReturn(Arrays.asList("q"));
    when(employeeSearchFunctionImport.getParameter("q")).thenReturn(employeeSearchParameter);
    createFunctionImportMock(defaultContainer, "AllLocations", locationComplexType, EdmMultiplicity.MANY);
    createFunctionImportMock(defaultContainer, "AllUsedRoomIds", EdmSimpleTypeKind.String.getEdmSimpleTypeInstance(), EdmMultiplicity.MANY);
    createFunctionImportMock(defaultContainer, "MaximalAge", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(), EdmMultiplicity.ONE);
    createFunctionImportMock(defaultContainer, "MostCommonLocation", locationComplexType, EdmMultiplicity.ONE);
    EdmFunctionImport managerPhotoFunctionImport = createFunctionImportMock(defaultContainer, "ManagerPhoto", EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance(), EdmMultiplicity.ONE);
    EdmParameter managerPhotoParameter = mock(EdmParameter.class);
    when(managerPhotoParameter.getType()).thenReturn(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
    EdmFacets managerPhotoParameterFacets = mock(EdmFacets.class);
    when(managerPhotoParameterFacets.isNullable()).thenReturn(false);
    when(managerPhotoParameter.getFacets()).thenReturn(managerPhotoParameterFacets);
    when(managerPhotoFunctionImport.getParameterNames()).thenReturn(Arrays.asList("Id"));
    when(managerPhotoFunctionImport.getParameter("Id")).thenReturn(managerPhotoParameter);
    EdmFunctionImport oldestEmployeeFunctionImport = createFunctionImportMock(defaultContainer, "OldestEmployee", employeeType, EdmMultiplicity.ONE);
    when(oldestEmployeeFunctionImport.getEntitySet()).thenReturn(employeeEntitySet);

    EdmEntityContainer specificContainer = mock(EdmEntityContainer.class);
    when(specificContainer.getEntitySet("Employees")).thenReturn(employeeEntitySet);
    when(specificContainer.getName()).thenReturn("Container1");

    EdmEntityType photoEntityType = mock(EdmEntityType.class);
    when(photoEntityType.getName()).thenReturn("Photo");
    when(photoEntityType.getNamespace()).thenReturn("RefScenario2");
    when(photoEntityType.getPropertyNames()).thenReturn(Arrays.asList(
        "Id", "Name", "Type", "Image", "BinaryData", "Содержание", "CustomProperty"));
    when(photoEntityType.getKeyPropertyNames()).thenReturn(Arrays.asList("Id", "Type"));
    when(photoEntityType.hasStream()).thenReturn(true);
    EdmMapping photoEntityTypeMapping = Mockito.mock(EdmMapping.class);
    when(photoEntityTypeMapping.getMimeType()).thenReturn("getType");
    when(photoEntityType.getMapping()).thenReturn(photoEntityTypeMapping);
    EdmProperty photoIdProperty = createProperty("Id", EdmSimpleTypeKind.Int32, photoEntityType);
    EdmFacets photoIdFacet = mock(EdmFacets.class);
    when(photoIdFacet.getConcurrencyMode()).thenReturn(EdmConcurrencyMode.Fixed);
    when(photoIdProperty.getFacets()).thenReturn(photoIdFacet);

    createProperty("Name", EdmSimpleTypeKind.String, photoEntityType);
    final EdmProperty photoTypeProperty = createProperty("Type", EdmSimpleTypeKind.String, photoEntityType);
    when(photoEntityType.getKeyProperties()).thenReturn(Arrays.asList(photoIdProperty, photoTypeProperty));

    EdmProperty photoImageProperty = createProperty("Image", EdmSimpleTypeKind.Binary, photoEntityType);
    EdmMapping imageMapping = mock(EdmMapping.class);
    when(imageMapping.getMimeType()).thenReturn("getImageType");
    when(photoImageProperty.getMapping()).thenReturn(imageMapping);

    EdmProperty binaryDataProperty = createProperty("BinaryData", EdmSimpleTypeKind.Binary, photoEntityType);
    when(binaryDataProperty.getMimeType()).thenReturn("image/jpeg");

    EdmProperty photoRussianProperty = createProperty("Содержание", EdmSimpleTypeKind.String, photoEntityType);
    EdmFacets photoRussianFacets = mock(EdmFacets.class);
    when(photoRussianFacets.isNullable()).thenReturn(true);
    when(photoRussianFacets.isUnicode()).thenReturn(true);
    when(photoRussianFacets.getMaxLength()).thenReturn(Integer.MAX_VALUE);
    when(photoRussianProperty.getFacets()).thenReturn(photoRussianFacets);
    CustomizableFeedMappings photoRussianMapping = mock(CustomizableFeedMappings.class);
    when(photoRussianMapping.getFcKeepInContent()).thenReturn(false);
    when(photoRussianMapping.getFcNsPrefix()).thenReturn("ру");
    when(photoRussianMapping.getFcNsUri()).thenReturn("http://localhost");
    when(photoRussianMapping.getFcTargetPath()).thenReturn("Содержание");
    when(photoRussianProperty.getCustomizableFeedMappings()).thenReturn(photoRussianMapping);

    EdmProperty customProperty = createProperty("CustomProperty", EdmSimpleTypeKind.String, photoEntityType);
    CustomizableFeedMappings customFeedMapping = mock(CustomizableFeedMappings.class);
    when(customFeedMapping.getFcKeepInContent()).thenReturn(false);
    when(customFeedMapping.getFcNsPrefix()).thenReturn("custom");
    when(customFeedMapping.getFcNsUri()).thenReturn("http://localhost");
    when(customFeedMapping.getFcTargetPath()).thenReturn("TarPath");
    when(customProperty.getCustomizableFeedMappings()).thenReturn(customFeedMapping);

    EdmEntitySet photoEntitySet = mock(EdmEntitySet.class);
    when(photoEntitySet.getName()).thenReturn("Photos");
    when(photoEntitySet.getEntityType()).thenReturn(photoEntityType);
    EdmEntityContainer photoContainer = mock(EdmEntityContainer.class);
    when(photoContainer.isDefaultEntityContainer()).thenReturn(false);
    when(photoContainer.getEntitySet("Photos")).thenReturn(photoEntitySet);
    when(photoContainer.getName()).thenReturn("Container2");

    when(photoEntitySet.getEntityContainer()).thenReturn(photoContainer);

    Edm edm = mock(Edm.class);
    EdmServiceMetadata serviceMetadata = mock(EdmServiceMetadata.class);
    when(serviceMetadata.getDataServiceVersion()).thenReturn("MockEdm");
    when(edm.getServiceMetadata()).thenReturn(serviceMetadata);
    when(edm.getDefaultEntityContainer()).thenReturn(defaultContainer);
    when(edm.getEntityContainer("Container1")).thenReturn(specificContainer);
    when(edm.getEntityContainer("Container2")).thenReturn(photoContainer);
    when(edm.getEntityType("RefScenario", "Employee")).thenReturn(employeeType);
    when(edm.getEntityType("RefScenario", "Room")).thenReturn(roomType);
    when(edm.getEntityType("RefScenario", "Building")).thenReturn(buildingType);
    when(edm.getComplexType("RefScenario", "c_Location")).thenReturn(locationComplexType);
    when(edm.getEntityType("RefScenario2", "Photo")).thenReturn(photoEntityType);

    return edm;
  }

  private static EdmNavigationProperty createNavigationProperty(final String name, final EdmMultiplicity multiplicity, final EdmEntitySet entitySet, final EdmEntitySet targetEntitySet) throws EdmException {
    EdmType navigationType = mock(EdmType.class);
    when(navigationType.getKind()).thenReturn(EdmTypeKind.ENTITY);

    EdmNavigationProperty navigationProperty = mock(EdmNavigationProperty.class);
    when(navigationProperty.getName()).thenReturn(name);
    when(navigationProperty.getType()).thenReturn(navigationType);
    when(navigationProperty.getMultiplicity()).thenReturn(multiplicity);

    when(entitySet.getEntityType().getProperty(name)).thenReturn(navigationProperty);
    when(entitySet.getRelatedEntitySet(navigationProperty)).thenReturn(targetEntitySet);

    return navigationProperty;
  }

  private static EdmProperty createProperty(final String name, final EdmSimpleTypeKind kind, final EdmStructuralType entityType) throws EdmException {
    EdmProperty property = mock(EdmProperty.class);
    when(property.getType()).thenReturn(kind.getEdmSimpleTypeInstance());
    when(property.getName()).thenReturn(name);
    when(entityType.getProperty(name)).thenReturn(property);
    return property;
  }

  private static EdmEntitySet createEntitySetMock(final EdmEntityContainer container, final String name, final EdmSimpleTypeKind kind, final String keyPropertyId) throws EdmException {
    final EdmEntityType entityType = createEntityTypeMock(name.substring(0, name.length() - 1), kind, keyPropertyId);

    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(name);
    when(entitySet.getEntityType()).thenReturn(entityType);

    when(entitySet.getEntityContainer()).thenReturn(container);

    when(container.getEntitySet(name)).thenReturn(entitySet);

    return entitySet;
  }

  private static EdmEntityType createEntityTypeMock(final String name, final EdmSimpleTypeKind kind, final String keyPropertyId) throws EdmException {
    EdmEntityType entityType = mock(EdmEntityType.class);
    when(entityType.getName()).thenReturn(name);
    when(entityType.getNamespace()).thenReturn("RefScenario");

    final EdmProperty keyProperty = createProperty(keyPropertyId, kind, entityType);
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.getMaxLength()).thenReturn(null);
    when(facets.isNullable()).thenReturn(false);
    when(keyProperty.getFacets()).thenReturn(facets);

    when(entityType.getKind()).thenReturn(EdmTypeKind.ENTITY);
    when(entityType.getPropertyNames()).thenReturn(Arrays.asList(keyPropertyId));
    when(entityType.getKeyPropertyNames()).thenReturn(Arrays.asList(keyPropertyId));
    when(entityType.getKeyProperties()).thenReturn(Arrays.asList(keyProperty));

    return entityType;
  }

  private static EdmFunctionImport createFunctionImportMock(final EdmEntityContainer container, final String name, final EdmType type, final EdmMultiplicity multiplicity) throws EdmException {
    EdmTyped returnType = mock(EdmTyped.class);
    when(returnType.getType()).thenReturn(type);
    when(returnType.getMultiplicity()).thenReturn(multiplicity);

    EdmFunctionImport functionImport = mock(EdmFunctionImport.class);
    when(functionImport.getName()).thenReturn(name);
    when(functionImport.getReturnType()).thenReturn(returnType);
    when(functionImport.getHttpMethod()).thenReturn("GET");

    when(container.getFunctionImport(name)).thenReturn(functionImport);

    return functionImport;
  }

}
