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
    final EdmServiceMetadata serviceMetadata = mock(EdmServiceMetadata.class);
    when(serviceMetadata.getDataServiceVersion()).thenReturn("MockEdm");

    final EdmEntityContainer defaultContainer = mock(EdmEntityContainer.class);
    when(defaultContainer.isDefaultEntityContainer()).thenReturn(true);

    final EdmEntitySet employeeEntitySet = createEntitySetMock(defaultContainer, "Employees", EdmSimpleTypeKind.String, "EmployeeId");
    final EdmEntitySet teamEntitySet = createEntitySetMock(defaultContainer, "Teams", EdmSimpleTypeKind.String, "Id");
    final EdmEntitySet roomEntitySet = createEntitySetMock(defaultContainer, "Rooms", EdmSimpleTypeKind.String, "Id");
    final EdmEntitySet managerEntitySet = createEntitySetMock(defaultContainer, "Managers", EdmSimpleTypeKind.String, "EmployeeId");

    final EdmNavigationProperty employeeProperty = createNavigationProperty("nm_Employees", EdmMultiplicity.MANY);
    when(managerEntitySet.getRelatedEntitySet(employeeProperty)).thenReturn(employeeEntitySet);

    final EdmNavigationProperty managerProperty = createNavigationProperty("ne_Manager", EdmMultiplicity.ONE);
    when(employeeEntitySet.getRelatedEntitySet(managerProperty)).thenReturn(managerEntitySet);

    final EdmNavigationProperty teamNavigationProperty = createNavigationProperty("ne_Team", EdmMultiplicity.ONE);
    when(employeeEntitySet.getRelatedEntitySet(teamNavigationProperty)).thenReturn(teamEntitySet);

    final EdmNavigationProperty roomNavigationProperty = createNavigationProperty("ne_Room", EdmMultiplicity.ONE);
    when(employeeEntitySet.getRelatedEntitySet(roomNavigationProperty)).thenReturn(roomEntitySet);

    when(teamEntitySet.getEntityType().getProperty("nt_Employees")).thenReturn(employeeProperty);
    when(teamEntitySet.getRelatedEntitySet(employeeProperty)).thenReturn(employeeEntitySet);

    final EdmEntityType managerType = managerEntitySet.getEntityType();
    when(managerType.getProperty("nm_Employees")).thenReturn(employeeProperty);

    final EdmEntityType employeeType = employeeEntitySet.getEntityType();
    when(employeeType.hasStream()).thenReturn(true);
    when(employeeType.getPropertyNames()).thenReturn(Arrays.asList(
        "EmployeeId", "EmployeeName", "ManagerId", "RoomId", "TeamId",
        "Location", "Age", "EntryDate", "ImageUrl"));
    when(employeeType.getProperty("ne_Manager")).thenReturn(managerProperty);
    when(employeeType.getProperty("ne_Team")).thenReturn(teamNavigationProperty);
    when(employeeType.getProperty("ne_Room")).thenReturn(roomNavigationProperty);
    when(employeeType.getNavigationPropertyNames()).thenReturn(Arrays.asList("ne_Manager", "ne_Team", "ne_Room"));

    final EdmProperty employeeNameProperty = createProperty("EmployeeName", EdmSimpleTypeKind.String, employeeType);
    final EdmCustomizableFeedMappings employeeTitleMappings = mock(EdmCustomizableFeedMappings.class);
    when(employeeTitleMappings.getFcTargetPath()).thenReturn(EdmTargetPath.SYNDICATION_TITLE);
    when(employeeTitleMappings.isFcKeepInContent()).thenReturn(true);
    when(employeeNameProperty.getCustomizableFeedMappings()).thenReturn(employeeTitleMappings);

    createProperty("ManagerId", EdmSimpleTypeKind.String, employeeType);
    createProperty("RoomId", EdmSimpleTypeKind.String, employeeType);
    createProperty("TeamId", EdmSimpleTypeKind.String, employeeType);

    EdmProperty employeeAgeProperty = createProperty("Age", EdmSimpleTypeKind.Int32, employeeType);
    EdmFacets employeeAgeFacets = Mockito.mock(EdmFacets.class);
    when(employeeAgeFacets.getMaxLength()).thenReturn(null);
    when(employeeAgeFacets.isNullable()).thenReturn(Boolean.FALSE);
    when(employeeAgeProperty.getFacets()).thenReturn(employeeAgeFacets);

    final EdmProperty employeeEntryDateProperty = createProperty("EntryDate", EdmSimpleTypeKind.DateTime, employeeType);
    final EdmCustomizableFeedMappings employeeUpdatedMappings = mock(EdmCustomizableFeedMappings.class);
    when(employeeUpdatedMappings.getFcTargetPath()).thenReturn(EdmTargetPath.SYNDICATION_UPDATED);
    when(employeeUpdatedMappings.isFcKeepInContent()).thenReturn(true);
    when(employeeEntryDateProperty.getCustomizableFeedMappings()).thenReturn(employeeUpdatedMappings);

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
    when(cityComplexType.getName()).thenReturn("City");
    when(cityComplexType.getNamespace()).thenReturn("RefScenario");
    when(cityComplexType.getPropertyNames()).thenReturn(Arrays.asList("PostalCode", "CityName"));

    final EdmProperty cityProperty = mock(EdmProperty.class);
    when(cityProperty.getType()).thenReturn(cityComplexType);
    when(cityProperty.getName()).thenReturn("City");
    when(locationComplexType.getProperty("City")).thenReturn(cityProperty);

    createProperty("PostalCode", EdmSimpleTypeKind.String, cityComplexType);
    createProperty("CityName", EdmSimpleTypeKind.String, cityComplexType);

    final EdmEntityType roomType = roomEntitySet.getEntityType();
    final EdmProperty roomId = roomType.getKeyProperties().get(0);
    final EdmFacets roomIdFacet = mock(EdmFacets.class);
    when(roomIdFacet.getMaxLength()).thenReturn(100);
    when(roomIdFacet.getConcurrencyMode()).thenReturn(EdmConcurrencyMode.Fixed);
    when(roomId.getFacets()).thenReturn(roomIdFacet);

    final EdmFunctionImport employeeSearchFunctionImport = createFunctionImportMock(defaultContainer, "EmployeeSearch", employeeType, EdmMultiplicity.MANY);
    when(employeeSearchFunctionImport.getEntitySet()).thenReturn(employeeEntitySet);
    final EdmParameter employeeSearchParameter = mock(EdmParameter.class);
    when(employeeSearchParameter.getType()).thenReturn(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
    when(employeeSearchFunctionImport.getParameterNames()).thenReturn(Arrays.asList("q"));
    when(employeeSearchFunctionImport.getParameter("q")).thenReturn(employeeSearchParameter);
    createFunctionImportMock(defaultContainer, "AllLocations", locationComplexType, EdmMultiplicity.MANY);
    createFunctionImportMock(defaultContainer, "AllUsedRoomIds", EdmSimpleTypeKind.String.getEdmSimpleTypeInstance(), EdmMultiplicity.MANY);
    createFunctionImportMock(defaultContainer, "MaximalAge", EdmSimpleTypeKind.Int16.getEdmSimpleTypeInstance(), EdmMultiplicity.ONE);
    createFunctionImportMock(defaultContainer, "MostCommonLocation", locationComplexType, EdmMultiplicity.ONE);
    final EdmFunctionImport managerPhotoFunctionImport = createFunctionImportMock(defaultContainer, "ManagerPhoto", EdmSimpleTypeKind.Binary.getEdmSimpleTypeInstance(), EdmMultiplicity.ONE);
    final EdmParameter managerPhotoParameter = mock(EdmParameter.class);
    when(managerPhotoParameter.getType()).thenReturn(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance());
    final EdmFacets managerPhotoParameterFacets = mock(EdmFacets.class);
    when(managerPhotoParameterFacets.isNullable()).thenReturn(false);
    when(managerPhotoParameter.getFacets()).thenReturn(managerPhotoParameterFacets);
    when(managerPhotoFunctionImport.getParameterNames()).thenReturn(Arrays.asList("Id"));
    when(managerPhotoFunctionImport.getParameter("Id")).thenReturn(managerPhotoParameter);
    final EdmFunctionImport oldestEmployeeFunctionImport = createFunctionImportMock(defaultContainer, "OldestEmployee", employeeType, EdmMultiplicity.ONE);
    when(oldestEmployeeFunctionImport.getEntitySet()).thenReturn(employeeEntitySet);

    final EdmEntityContainer specificContainer = mock(EdmEntityContainer.class);
    when(specificContainer.getEntitySet("Employees")).thenReturn(employeeEntitySet);
    when(specificContainer.getName()).thenReturn("Container1");

    final EdmEntityType photoEntityType = mock(EdmEntityType.class);
    when(photoEntityType.getName()).thenReturn("Photo");
    when(photoEntityType.getNamespace()).thenReturn("RefScenario2");
    when(photoEntityType.getPropertyNames()).thenReturn(Arrays.asList(
        "Id", "Name", "Type", "Image", "BinaryData", "Содержание", "CustomProperty"));
    when(photoEntityType.getKeyPropertyNames()).thenReturn(Arrays.asList("Id", "Type"));

    final EdmProperty photoIdProperty = createProperty("Id", EdmSimpleTypeKind.Int32, photoEntityType);
    final EdmFacets photoIdFacet = mock(EdmFacets.class);
    when(photoIdFacet.getConcurrencyMode()).thenReturn(EdmConcurrencyMode.Fixed);
    when(photoIdProperty.getFacets()).thenReturn(photoIdFacet);

    createProperty("Name", EdmSimpleTypeKind.String, photoEntityType);
    final EdmProperty photoTypeProperty = createProperty("Type", EdmSimpleTypeKind.String, photoEntityType);

    when(photoEntityType.getKeyProperties()).thenReturn(Arrays.asList(photoIdProperty, photoTypeProperty));

    final EdmProperty photoImageProperty = createProperty("Image", EdmSimpleTypeKind.Binary, photoEntityType);
    final EdmMapping imageMapping = mock(EdmMapping.class);
    when(imageMapping.getMimeType()).thenReturn("getImageType");
    when(photoImageProperty.getMapping()).thenReturn(imageMapping);

    final EdmProperty binaryDataProperty = createProperty("BinaryData", EdmSimpleTypeKind.Binary, photoEntityType);
    when(binaryDataProperty.getMimeType()).thenReturn("image/jpeg");

    final EdmProperty photoRussianProperty = createProperty("Содержание", EdmSimpleTypeKind.String, photoEntityType);
    final EdmFacets photoRussianFacets = mock(EdmFacets.class);
    when(photoRussianFacets.isNullable()).thenReturn(true);
    when(photoRussianFacets.isUnicode()).thenReturn(true);
    when(photoRussianFacets.getMaxLength()).thenReturn(Integer.MAX_VALUE);
    when(photoRussianProperty.getFacets()).thenReturn(photoRussianFacets);
    final CustomizableFeedMappings photoRussianMapping = mock(CustomizableFeedMappings.class);
    when(photoRussianMapping.getFcKeepInContent()).thenReturn(false);
    when(photoRussianMapping.getFcNsPrefix()).thenReturn("ру");
    when(photoRussianMapping.getFcNsUri()).thenReturn("http://localhost");
    when(photoRussianMapping.getFcTargetPath()).thenReturn("Содержание");
    when(photoRussianProperty.getCustomizableFeedMappings()).thenReturn(photoRussianMapping);

    final EdmProperty customProperty = createProperty("CustomProperty", EdmSimpleTypeKind.String, photoEntityType);
    final CustomizableFeedMappings customFeedMapping = mock(CustomizableFeedMappings.class);
    when(customFeedMapping.getFcKeepInContent()).thenReturn(false);
    when(customFeedMapping.getFcNsPrefix()).thenReturn("custom");
    when(customFeedMapping.getFcNsUri()).thenReturn("http://localhost");
    when(customFeedMapping.getFcTargetPath()).thenReturn("TarPath");
    when(customProperty.getCustomizableFeedMappings()).thenReturn(customFeedMapping);

    final EdmEntitySet photoEntitySet = mock(EdmEntitySet.class);
    when(photoEntitySet.getName()).thenReturn("Photos");
    when(photoEntitySet.getEntityType()).thenReturn(photoEntityType);
    final EdmEntityContainer photoContainer = mock(EdmEntityContainer.class);
    when(photoContainer.isDefaultEntityContainer()).thenReturn(false);
    when(photoContainer.getEntitySet("Photos")).thenReturn(photoEntitySet);
    when(photoContainer.getName()).thenReturn("Container2");

    when(photoEntitySet.getEntityContainer()).thenReturn(photoContainer);

    final Edm edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(serviceMetadata);
    when(edm.getDefaultEntityContainer()).thenReturn(defaultContainer);
    when(edm.getEntityContainer("Container1")).thenReturn(specificContainer);
    when(edm.getEntityContainer("Container2")).thenReturn(photoContainer);
    when(edm.getEntityType("RefScenario", "Employee")).thenReturn(employeeType);
    when(edm.getComplexType("RefScenario", "c_Location")).thenReturn(locationComplexType);
    when(edm.getEntityType("RefScenario2", "Photo")).thenReturn(photoEntityType);

    return edm;
  }

  private static EdmNavigationProperty createNavigationProperty(final String name, final EdmMultiplicity multiplicity) throws EdmException {
    final EdmType navigationType = mock(EdmType.class);
    when(navigationType.getKind()).thenReturn(EdmTypeKind.ENTITY);

    final EdmNavigationProperty navigationProperty = mock(EdmNavigationProperty.class);
    when(navigationProperty.getName()).thenReturn(name);
    when(navigationProperty.getType()).thenReturn(navigationType);
    when(navigationProperty.getMultiplicity()).thenReturn(multiplicity);

    return navigationProperty;
  }

  private static EdmProperty createProperty(final String name, final EdmSimpleTypeKind kind, EdmStructuralType entityType) throws EdmException {
    final EdmProperty property = mock(EdmProperty.class);
    when(property.getType()).thenReturn(kind.getEdmSimpleTypeInstance());
    when(property.getName()).thenReturn(name);
    when(entityType.getProperty(name)).thenReturn(property);
    return property;
  }

  private static EdmEntitySet createEntitySetMock(final EdmEntityContainer container, final String name, final EdmSimpleTypeKind kind, final String keyPropertyId) throws EdmException {
    final EdmEntityType entityType = createEntityTypeMock(name.substring(0, name.length() - 1), kind, keyPropertyId);

    final EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(name);
    when(entitySet.getEntityType()).thenReturn(entityType);

    when(entitySet.getEntityContainer()).thenReturn(container);

    when(container.getEntitySet(name)).thenReturn(entitySet);

    return entitySet;
  }

  private static EdmEntityType createEntityTypeMock(final String name, final EdmSimpleTypeKind kind, final String keyPropertyId) throws EdmException {
    final EdmEntityType entityType = mock(EdmEntityType.class);
    when(entityType.getName()).thenReturn(name);
    when(entityType.getNamespace()).thenReturn("RefScenario");

    final EdmProperty keyProperty = createProperty(keyPropertyId, kind, entityType);

    when(entityType.getKind()).thenReturn(EdmTypeKind.ENTITY);
    when(entityType.getPropertyNames()).thenReturn(Arrays.asList(keyPropertyId));
    when(entityType.getKeyPropertyNames()).thenReturn(Arrays.asList(keyPropertyId));
    when(entityType.getKeyProperties()).thenReturn(Arrays.asList(keyProperty));

    return entityType;
  }

  private static EdmFunctionImport createFunctionImportMock(final EdmEntityContainer container, final String name, final EdmType type, final EdmMultiplicity multiplicity) throws EdmException {
    final EdmTyped returnType = mock(EdmTyped.class);
    when(returnType.getType()).thenReturn(type);
    when(returnType.getMultiplicity()).thenReturn(multiplicity);

    final EdmFunctionImport functionImport = mock(EdmFunctionImport.class);
    when(functionImport.getName()).thenReturn(name);
    when(functionImport.getReturnType()).thenReturn(returnType);
    when(functionImport.getHttpMethod()).thenReturn("GET");

    when(container.getFunctionImport(name)).thenReturn(functionImport);

    return functionImport;
  }

}
