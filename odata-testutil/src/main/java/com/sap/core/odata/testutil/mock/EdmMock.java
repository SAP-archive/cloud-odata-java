package com.sap.core.odata.testutil.mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

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
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.edm.provider.CustomizableFeedMappings;
import com.sap.core.odata.api.exception.ODataException;

/**
 * @author SAP AG
 */
class EdmMock {

  public static Edm createMockEdm() throws ODataException {
    EdmServiceMetadata serviceMetadata = mock(EdmServiceMetadata.class);
    when(serviceMetadata.getDataServiceVersion()).thenReturn("MockEdm");

    EdmEntityContainer defaultContainer = mock(EdmEntityContainer.class);
    EdmEntitySet employeeEntitySet = createEntitySetMock(defaultContainer, "Employees", EdmSimpleTypeKind.String, "EmployeeId");
    EdmEntitySet managerEntitySet = createEntitySetMock(defaultContainer, "Managers", EdmSimpleTypeKind.String, "EmployeeId");
    EdmEntitySet roomEntitySet = createEntitySetMock(defaultContainer, "Rooms", EdmSimpleTypeKind.String, "Id");
    EdmEntitySet teamEntitySet = createEntitySetMock(defaultContainer, "Teams", EdmSimpleTypeKind.String, "Id");

    when(defaultContainer.getEntitySet("Employees")).thenReturn(employeeEntitySet);
    when(defaultContainer.getEntitySet("Rooms")).thenReturn(roomEntitySet);
    when(defaultContainer.getEntitySet("Teams")).thenReturn(teamEntitySet);
    when(defaultContainer.isDefaultEntityContainer()).thenReturn(true);

    EdmNavigationProperty employeeProperty = createNavigationProperty("nm_Employees", EdmMultiplicity.MANY);
    when(managerEntitySet.getRelatedEntitySet(employeeProperty)).thenReturn(employeeEntitySet);

    EdmEntityType roomType = roomEntitySet.getEntityType();
    when(roomType.getName()).thenReturn("Room");
    when(roomType.getPropertyNames()).thenReturn(Arrays.asList("Id"));
    when(roomType.hasStream()).thenReturn(false);
    EdmProperty roomId = roomType.getKeyProperties().get(0);
    EdmFacets roomIdFacet = mock(EdmFacets.class);
    when(roomIdFacet.getMaxLength()).thenReturn(100);
    when(roomIdFacet.getConcurrencyMode()).thenReturn(EdmConcurrencyMode.Fixed);
    when(roomId.getFacets()).thenReturn(roomIdFacet);

    EdmEntityType managerType = managerEntitySet.getEntityType();
    when(managerType.getProperty("nm_Employees")).thenReturn(employeeProperty);
    //when(managerType.getProperty("somethingwrong")).thenThrow(new EdmException("Property not found"));

    EdmNavigationProperty managerProperty = createNavigationProperty("ne_Manager", EdmMultiplicity.ONE);
    when(employeeEntitySet.getRelatedEntitySet(managerProperty)).thenReturn(managerEntitySet);
    when(employeeEntitySet.getEntityContainer()).thenReturn(defaultContainer);

    EdmNavigationProperty teamNavigationProperty = createNavigationProperty("ne_Team", EdmMultiplicity.ONE);
    when(employeeEntitySet.getRelatedEntitySet(teamNavigationProperty)).thenReturn(teamEntitySet);
    when(employeeEntitySet.getEntityContainer()).thenReturn(defaultContainer);

    EdmNavigationProperty roomNavigationProperty = createNavigationProperty("ne_Room", EdmMultiplicity.ONE);
    when(employeeEntitySet.getRelatedEntitySet(roomNavigationProperty)).thenReturn(roomEntitySet);
    when(employeeEntitySet.getEntityContainer()).thenReturn(defaultContainer);

    EdmEntityType employeeType = employeeEntitySet.getEntityType();
    when(employeeType.hasStream()).thenReturn(true);
    when(employeeType.getPropertyNames()).thenReturn(Arrays.asList("EmployeeId", "ManagerId", "EmployeeName", "ImageUrl", "Age", "TeamId", "RoomId", "EntryDate", "Location"));
    when(employeeType.getProperty("ne_Manager")).thenReturn(managerProperty);
    when(employeeType.getProperty("ne_Team")).thenReturn(teamNavigationProperty);
    when(employeeType.getProperty("ne_Room")).thenReturn(roomNavigationProperty);
    when(employeeType.getName()).thenReturn("Employee");
    when(employeeType.getNamespace()).thenReturn("RefScenario");
    when(employeeType.getNavigationPropertyNames()).thenReturn(Arrays.asList("ne_Manager", "ne_Team", "ne_Room"));

    EdmProperty employeeIdProperty = createProperty("EmployeeId", EdmSimpleTypeKind.String);
    when(employeeType.getProperty("EmployeeId")).thenReturn(employeeIdProperty);
    when(employeeType.getKeyProperties()).thenReturn(Arrays.asList(employeeIdProperty));

    EdmProperty managerIdProperty = createProperty("ManagerId", EdmSimpleTypeKind.String);
    when(employeeType.getProperty("ManagerId")).thenReturn(managerIdProperty);

    EdmProperty employeeNameProperty = createProperty("EmployeeName", EdmSimpleTypeKind.String);
    when(employeeType.getProperty("EmployeeName")).thenReturn(employeeNameProperty);

    EdmProperty employeeImageUrlProperty = createProperty("ImageUrl", EdmSimpleTypeKind.String);
    when(employeeType.getProperty("ImageUrl")).thenReturn(employeeImageUrlProperty);

    EdmProperty employeeAgeProperty = createProperty("Age", EdmSimpleTypeKind.Int32);
    when(employeeType.getProperty("Age")).thenReturn(employeeAgeProperty);

    EdmProperty employeeRoomIdProperty = createProperty("RoomId", EdmSimpleTypeKind.String);
    when(employeeType.getProperty("RoomId")).thenReturn(employeeRoomIdProperty);

    EdmProperty employeeEntryDateProperty = createProperty("EntryDate", EdmSimpleTypeKind.DateTime);
    when(employeeType.getProperty("EntryDate")).thenReturn(employeeEntryDateProperty);

    EdmCustomizableFeedMappings employeeUpdatedMappings = mock(EdmCustomizableFeedMappings.class);
    when(employeeUpdatedMappings.getFcTargetPath()).thenReturn(EdmTargetPath.SYNDICATION_UPDATED);
    when(employeeUpdatedMappings.isFcKeepInContent()).thenReturn(true);
    when(employeeEntryDateProperty.getCustomizableFeedMappings()).thenReturn(employeeUpdatedMappings);

    EdmProperty employeeTeamIdProperty = createProperty("TeamId", EdmSimpleTypeKind.String);
    when(employeeType.getProperty("TeamId")).thenReturn(employeeTeamIdProperty);

    EdmCustomizableFeedMappings employeeTitleMappings = mock(EdmCustomizableFeedMappings.class);
    when(employeeTitleMappings.getFcTargetPath()).thenReturn(EdmTargetPath.SYNDICATION_TITLE);
    when(employeeTitleMappings.isFcKeepInContent()).thenReturn(true);
    when(employeeNameProperty.getCustomizableFeedMappings()).thenReturn(employeeTitleMappings);

    EdmComplexType locationComplexType = mock(EdmComplexType.class);
    when(locationComplexType.getKind()).thenReturn(EdmTypeKind.COMPLEX);
    when(locationComplexType.getName()).thenReturn("c_Location");
    when(locationComplexType.getNamespace()).thenReturn("RefScenario");
    when(locationComplexType.getPropertyNames()).thenReturn(Arrays.asList("City", "Country"));

    EdmProperty locationComplexProperty = mock(EdmProperty.class);
    when(locationComplexProperty.getType()).thenReturn(locationComplexType);
    when(locationComplexProperty.getName()).thenReturn("Location");
    when(employeeType.getProperty("Location")).thenReturn(locationComplexProperty);

    EdmProperty countryProperty = createProperty("Country", EdmSimpleTypeKind.String);
    when(locationComplexType.getProperty("Country")).thenReturn(countryProperty);

    EdmComplexType cityComplexType = mock(EdmComplexType.class);
    when(cityComplexType.getKind()).thenReturn(EdmTypeKind.COMPLEX);
    when(cityComplexType.getName()).thenReturn("City");
    when(cityComplexType.getNamespace()).thenReturn("RefScenario");
    when(cityComplexType.getPropertyNames()).thenReturn(Arrays.asList("PostalCode", "CityName"));

    EdmProperty cityProperty = mock(EdmProperty.class);
    when(cityProperty.getType()).thenReturn(cityComplexType);
    when(cityProperty.getName()).thenReturn("City");
    when(locationComplexType.getProperty("City")).thenReturn(cityProperty);

    EdmProperty postalCodeProperty = createProperty("PostalCode", EdmSimpleTypeKind.String);
    when(cityComplexType.getProperty("PostalCode")).thenReturn(postalCodeProperty);

    EdmProperty cityNameProperty = createProperty("CityName", EdmSimpleTypeKind.String);
    when(cityComplexType.getProperty("CityName")).thenReturn(cityNameProperty);

    EdmEntitySet teamsEntitySet = createEntitySetMock(defaultContainer, "Teams", EdmSimpleTypeKind.String, "Id");
    when(teamsEntitySet.getEntityType().getProperty("nt_Employees")).thenReturn(employeeProperty);
    when(teamsEntitySet.getRelatedEntitySet(employeeProperty)).thenReturn(employeeEntitySet);

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

    EdmProperty photoIdProperty = createProperty("Id", EdmSimpleTypeKind.Int32);
    EdmFacets photoIdFacet = mock(EdmFacets.class);
    when(photoIdFacet.getConcurrencyMode()).thenReturn(EdmConcurrencyMode.Fixed);
    when(photoIdProperty.getFacets()).thenReturn(photoIdFacet);

    EdmProperty photoNameProperty = createProperty("Name", EdmSimpleTypeKind.String);
    EdmProperty photoTypeProperty = createProperty("Type", EdmSimpleTypeKind.String);
    final EdmProperty photoImageProperty = createProperty("Image", EdmSimpleTypeKind.Binary);

    EdmProperty photoRussianProperty = createProperty("Содержание", EdmSimpleTypeKind.String);
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

    EdmProperty customProperty = createProperty("CustomProperty", EdmSimpleTypeKind.String);
    CustomizableFeedMappings customFeedMapping = mock(CustomizableFeedMappings.class);
    when(customFeedMapping.getFcKeepInContent()).thenReturn(false);
    when(customFeedMapping.getFcNsPrefix()).thenReturn("custom");
    when(customFeedMapping.getFcNsUri()).thenReturn("http://localhost");
    when(customFeedMapping.getFcTargetPath()).thenReturn("TarPath");
    when(customProperty.getCustomizableFeedMappings()).thenReturn(customFeedMapping);

    EdmEntityType photoEntityType = mock(EdmEntityType.class);
    when(photoEntityType.getName()).thenReturn("Photo");
    when(photoEntityType.getNamespace()).thenReturn("RefScenario2");
    when(photoEntityType.getPropertyNames()).thenReturn(Arrays.asList("Id", "Name", "Type", "Image", "Содержание", "CustomProperty"));
    when(photoEntityType.getKeyPropertyNames()).thenReturn(Arrays.asList("Id", "Type"));
    when(photoEntityType.getKeyProperties()).thenReturn(Arrays.asList(photoIdProperty, photoTypeProperty));
    when(photoEntityType.getProperty("Id")).thenReturn(photoIdProperty);
    when(photoEntityType.getProperty("Name")).thenReturn(photoNameProperty);
    when(photoEntityType.getProperty("Type")).thenReturn(photoTypeProperty);
    when(photoEntityType.getProperty("Image")).thenReturn(photoImageProperty);
    when(photoEntityType.getProperty(photoRussianProperty.getName())).thenReturn(photoRussianProperty);
    when(photoEntityType.getProperty(customProperty.getName())).thenReturn(customProperty);
    EdmEntitySet photoEntitySet = mock(EdmEntitySet.class);
    when(photoEntitySet.getName()).thenReturn("Photos");
    when(photoEntitySet.getEntityType()).thenReturn(photoEntityType);
    EdmEntityContainer photoContainer = mock(EdmEntityContainer.class);
    when(photoContainer.isDefaultEntityContainer()).thenReturn(false);
    when(photoContainer.getEntitySet("Photos")).thenReturn(photoEntitySet);
    when(photoContainer.getName()).thenReturn("Container2");

    when(photoEntitySet.getEntityContainer()).thenReturn(photoContainer);

    Edm edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(serviceMetadata);
    when(edm.getDefaultEntityContainer()).thenReturn(defaultContainer);
    when(edm.getEntityContainer("Container1")).thenReturn(specificContainer);
    when(edm.getEntityContainer("Container2")).thenReturn(photoContainer);
    when(edm.getEntityType("RefScenario", "Employee")).thenReturn(employeeType);
    when(edm.getEntityType("RefScenario2", "Photo")).thenReturn(photoEntityType);

    return edm;
  }

  private static EdmNavigationProperty createNavigationProperty(final String name, final EdmMultiplicity multiplicity) throws EdmException {
    EdmType navigationType = mock(EdmType.class);
    when(navigationType.getKind()).thenReturn(EdmTypeKind.ENTITY);

    EdmNavigationProperty navigationProperty = mock(EdmNavigationProperty.class);
    when(navigationProperty.getName()).thenReturn(name);
    when(navigationProperty.getType()).thenReturn(navigationType);
    when(navigationProperty.getMultiplicity()).thenReturn(multiplicity);
    return navigationProperty;
  }

  private static EdmProperty createProperty(final String name, final EdmSimpleTypeKind kind) throws EdmException {
    EdmProperty property = mock(EdmProperty.class);
    when(property.getType()).thenReturn(kind.getEdmSimpleTypeInstance());
    when(property.getName()).thenReturn(name);
    return property;
  }

  private static EdmEntitySet createEntitySetMock(final EdmEntityContainer container, final String name, final EdmSimpleTypeKind kind, final String keyPropertyId) throws EdmException {
    EdmEntityType entityType = createEntityTypeMock(kind, keyPropertyId);

    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(name);
    when(entitySet.getEntityType()).thenReturn(entityType);

    when(entitySet.getEntityContainer()).thenReturn(container);

    when(container.getEntitySet(name)).thenReturn(entitySet);

    return entitySet;
  }

  private static EdmEntityType createEntityTypeMock(final EdmSimpleTypeKind kind, final String keyPropertyId) throws EdmException {
    EdmProperty edmProperty = createProperty(keyPropertyId, kind);

    EdmEntityType entityType = mock(EdmEntityType.class);
    when(entityType.getKind()).thenReturn(EdmTypeKind.ENTITY);
    when(entityType.getKeyPropertyNames()).thenReturn(Arrays.asList(keyPropertyId));
    when(entityType.getKeyProperties()).thenReturn(Arrays.asList(edmProperty));
    when(entityType.getProperty(keyPropertyId)).thenReturn(edmProperty);
    return entityType;
  }

  private static EdmFunctionImport createFunctionImportMock(final EdmEntityContainer container, final String name, final EdmType type, final EdmMultiplicity multiplicity) throws EdmException {
    EdmTyped returnType = mock(EdmTyped.class);
    when(returnType.getType()).thenReturn(type);
    when(returnType.getMultiplicity()).thenReturn(multiplicity);
    EdmFunctionImport functionImport = mock(EdmFunctionImport.class);
    when(functionImport.getName()).thenReturn(name);
    when(functionImport.getReturnType()).thenReturn(returnType);
    when(container.getFunctionImport(name)).thenReturn(functionImport);
    return functionImport;
  }

}
