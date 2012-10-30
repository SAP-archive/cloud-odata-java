package com.sap.core.testutils.mocks;

import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmComplexType;
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
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;

class EdmMock {
  
  public static Edm createMockEdm() throws EdmException {
    final EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
    
    EdmServiceMetadata serviceMetadata = mock(EdmServiceMetadata.class);
    when(serviceMetadata.getDataServiceVersion()).thenReturn("MockEdm");

    EdmEntityContainer defaultContainer = mock(EdmEntityContainer.class);
    EdmEntitySet employeeEntitySet = createEntitySetMock(defaultContainer, "Employees", facade.stringInstance(), "EmployeeId");
    EdmEntitySet managerEntitySet = createEntitySetMock(defaultContainer, "Managers", facade.stringInstance(), "EmployeeId");

    EdmType navigationType = mock(EdmType.class);
    when(navigationType.getKind()).thenReturn(EdmTypeKind.NAVIGATION);

    EdmNavigationProperty employeeProperty = mock(EdmNavigationProperty.class);
    when(employeeProperty.getType()).thenReturn(navigationType);
    when(employeeProperty.getMultiplicity()).thenReturn(EdmMultiplicity.MANY);
    when(managerEntitySet.getRelatedEntitySet(employeeProperty)).thenReturn(employeeEntitySet);

    EdmEntityType managerType = managerEntitySet.getEntityType();
    when(managerType.getProperty("nm_Employees")).thenReturn(employeeProperty);

    EdmNavigationProperty managerProperty = mock(EdmNavigationProperty.class);
    when(managerProperty.getType()).thenReturn(navigationType);
    when(managerProperty.getMultiplicity()).thenReturn(EdmMultiplicity.ONE);
    when(employeeEntitySet.getRelatedEntitySet(managerProperty)).thenReturn(managerEntitySet);

    EdmEntityType employeeType = employeeEntitySet.getEntityType();
    when(employeeType.getKind()).thenReturn(EdmTypeKind.ENTITY);
    when(employeeType.hasStream()).thenReturn(true);
    when(employeeType.getProperty("ne_Manager")).thenReturn(managerProperty);
    when(employeeType.getProperty(matches(".+wrong|\\$links"))).thenThrow(new EdmException("Property not found"));

    EdmProperty employeeSimpleProperty = mock(EdmProperty.class);
    when(employeeSimpleProperty.getType()).thenReturn(facade.stringInstance());
    when(employeeSimpleProperty.getName()).thenReturn("EmployeeName");
    when(employeeType.getProperty("EmployeeName")).thenReturn(employeeSimpleProperty);

    EdmComplexType locationComplexType = mock(EdmComplexType.class);
    when(locationComplexType.getKind()).thenReturn(EdmTypeKind.COMPLEX);

    EdmProperty locationComplexProperty = mock(EdmProperty.class);
    when(locationComplexProperty.getType()).thenReturn(locationComplexType);
    when(locationComplexProperty.getName()).thenReturn("Location");
    when(employeeType.getProperty("Location")).thenReturn(locationComplexProperty);

    EdmProperty countryProperty = mock(EdmProperty.class);
    when(countryProperty.getType()).thenReturn(facade.stringInstance());
    when(countryProperty.getName()).thenReturn("Country");
    when(locationComplexType.getProperty("Country")).thenReturn(countryProperty);
    when(locationComplexType.getProperty("somethingwrong")).thenThrow(new EdmException("Property not found"));

    EdmEntitySet teamsEntitySet = createEntitySetMock(defaultContainer, "Teams", facade.stringInstance(), "Id");
    when(teamsEntitySet.getEntityType().getProperty("nt_Employees")).thenReturn(employeeProperty);
    when(teamsEntitySet.getRelatedEntitySet(employeeProperty)).thenReturn(employeeEntitySet);

    when(defaultContainer.getEntitySet(matches("some.+|.+Search|All.+|Max.+|Most.+|ManagerPhoto|Old.+"))).thenThrow(new EdmException("Entity set not found"));

    EdmFunctionImport employeeSearchFunctionImport = createFunctionImportMock(defaultContainer, "EmployeeSearch", employeeType, EdmMultiplicity.MANY);
    when(employeeSearchFunctionImport.getEntitySet()).thenReturn(employeeEntitySet);
    List<String> employeeSearchParameterNames = new ArrayList<String>();
    employeeSearchParameterNames.add("q");
    EdmParameter employeeSearchParameter = mock(EdmParameter.class);
    when(employeeSearchParameter.getType()).thenReturn(facade.stringInstance());
    when(employeeSearchFunctionImport.getParameterNames()).thenReturn(employeeSearchParameterNames);
    when(employeeSearchFunctionImport.getParameter("q")).thenReturn(employeeSearchParameter);
    createFunctionImportMock(defaultContainer, "AllLocations", locationComplexType, EdmMultiplicity.MANY);
    createFunctionImportMock(defaultContainer, "AllUsedRoomIds", facade.stringInstance(), EdmMultiplicity.MANY);
    createFunctionImportMock(defaultContainer, "MaximalAge", facade.int16Instance(), EdmMultiplicity.ONE);
    createFunctionImportMock(defaultContainer, "MostCommonLocation", locationComplexType, EdmMultiplicity.ONE);
    EdmFunctionImport managerPhotoFunctionImport = createFunctionImportMock(defaultContainer, "ManagerPhoto", facade.binaryInstance(), EdmMultiplicity.ONE);
    List<String> managerPhotoParameterNames = new ArrayList<String>();
    managerPhotoParameterNames.add("Id");
    EdmParameter managerPhotoParameter = mock(EdmParameter.class);
    when(managerPhotoParameter.getType()).thenReturn(facade.stringInstance());
    EdmFacets managerPhotoParameterFacets = mock(EdmFacets.class);
    when(managerPhotoParameterFacets.isNullable()).thenReturn(false);
    when(managerPhotoParameter.getFacets()).thenReturn(managerPhotoParameterFacets);
    when(managerPhotoFunctionImport.getParameterNames()).thenReturn(managerPhotoParameterNames);
    when(managerPhotoFunctionImport.getParameter("Id")).thenReturn(managerPhotoParameter);
    createFunctionImportMock(defaultContainer, "OldestEmployee", employeeType, EdmMultiplicity.ONE);
    when(defaultContainer.getFunctionImport("somethingwrong")).thenThrow(new EdmException("Function import not found"));

    EdmEntityContainer specificContainer = mock(EdmEntityContainer.class);
    when(specificContainer.getEntitySet("Employees")).thenReturn(employeeEntitySet);
    when(specificContainer.getName()).thenReturn("Container1");

    EdmProperty photoIdProperty = mock(EdmProperty.class);
    when(photoIdProperty.getType()).thenReturn(facade.int32Instance());
    when(photoIdProperty.getName()).thenReturn("Id");
    EdmProperty photoTypeProperty = mock(EdmProperty.class);
    when(photoTypeProperty.getType()).thenReturn(facade.stringInstance());
    when(photoTypeProperty.getName()).thenReturn("Type");
    List<EdmProperty> photoKeyProperties = new ArrayList<EdmProperty>();
    photoKeyProperties.add(photoIdProperty);
    photoKeyProperties.add(photoTypeProperty);
    EdmEntityType photoEntityType = mock(EdmEntityType.class);
    when(photoEntityType.getKeyProperties()).thenReturn(photoKeyProperties);
    when(photoEntityType.getProperty("Id")).thenReturn(photoIdProperty);
    when(photoEntityType.getProperty("Type")).thenReturn(photoTypeProperty);
    EdmEntitySet photoEntitySet = mock(EdmEntitySet.class);
    when(photoEntitySet.getName()).thenReturn("Photos");
    when(photoEntitySet.getEntityType()).thenReturn(photoEntityType);
    EdmEntityContainer photoContainer = mock(EdmEntityContainer.class);
    when(photoContainer.getEntitySet("Photos")).thenReturn(photoEntitySet);
    when(photoContainer.getName()).thenReturn("Container2");

    Edm edm = mock(Edm.class);
    when(edm.getServiceMetadata()).thenReturn(serviceMetadata);
    when(edm.getDefaultEntityContainer()).thenReturn(defaultContainer);
    when(edm.getEntityContainer("Container1")).thenReturn(specificContainer);
    when(edm.getEntityContainer("Container2")).thenReturn(photoContainer);
    when(edm.getEntityContainer("somethingwrong")).thenThrow(new EdmException("Container not found"));
    return edm;
  }

  private static EdmEntitySet createEntitySetMock(final EdmEntityContainer container, final String name, final EdmType type, final String keyPropertyId) throws EdmException {
    EdmEntityType entityType = createEntityTypeMock(type, keyPropertyId);

    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(name);
    when(entitySet.getEntityType()).thenReturn(entityType);

    when(container.getEntitySet(name)).thenReturn(entitySet);

    return entitySet;
  }

  private static EdmEntityType createEntityTypeMock(final EdmType type, final String keyPropertyId) throws EdmException {
    EdmProperty edmProperty = mock(EdmProperty.class);
    when(edmProperty.getName()).thenReturn(keyPropertyId);
    when(edmProperty.getType()).thenReturn(type);

    List<EdmProperty> keyProperties = new ArrayList<EdmProperty>();
    keyProperties.add(edmProperty);

    EdmEntityType entityType = mock(EdmEntityType.class);
    when(entityType.getKeyProperties()).thenReturn(keyProperties);
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
