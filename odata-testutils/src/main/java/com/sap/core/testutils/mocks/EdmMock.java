package com.sap.core.testutils.mocks;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmComplexType;
import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmFunctionImport;
import com.sap.core.odata.api.edm.EdmMultiplicity;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.edm.EdmParameter;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmServiceMetadata;
import com.sap.core.odata.api.edm.EdmSimpleType;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.exception.ODataException;

class EdmMock {
  
  private EdmSimpleType edmSimpleType = mock(EdmSimpleType.class);
  private Edm edmMock;
  
  public EdmMock(){
    try {
      edmMock = createMockEdm();  
    } catch (ODataException e) {
      // TODO: handle exception
    }
    
  }
  
  
  
  
  public Edm getEdmMock() {
    return edmMock;
  }




  private Edm createMockEdm() throws ODataException {
    EdmServiceMetadata serviceMetadata = mock(EdmServiceMetadata.class);
    when(serviceMetadata.getDataServiceVersion()).thenReturn("MockEdm");

    EdmEntityContainer defaultContainer = mock(EdmEntityContainer.class);
    EdmEntitySet employeeEntitySet = createEntitySetMock("Employees", edmSimpleType, "EmployeeId");
    EdmEntitySet managerEntitySet = createEntitySetMock("Managers", edmSimpleType, "ManagerId");

    EdmType navigationType = mock(EdmType.class);
    when(navigationType.getKind()).thenReturn(EdmTypeKind.NAVIGATION);

    EdmNavigationProperty employeeProperty = mock(EdmNavigationProperty.class);
    when(employeeProperty.getType()).thenReturn(navigationType);
    when(employeeProperty.getMultiplicity()).thenReturn(EdmMultiplicity.MANY);
    when(managerEntitySet.getRelatedEntitySet(employeeProperty)).thenReturn(employeeEntitySet);

    EdmEntityType managerType = managerEntitySet.getEntityType();
    when(managerType.getProperty("Employees")).thenReturn(employeeProperty);

    EdmNavigationProperty managerProperty = mock(EdmNavigationProperty.class);
    when(managerProperty.getType()).thenReturn(navigationType);
    when(managerProperty.getMultiplicity()).thenReturn(EdmMultiplicity.ONE);
    when(employeeEntitySet.getRelatedEntitySet(managerProperty)).thenReturn(managerEntitySet);

    EdmEntityType employeeType = employeeEntitySet.getEntityType();
    when(employeeType.getKind()).thenReturn(EdmTypeKind.ENTITY);
    when(employeeType.hasStream()).thenReturn(true);
    when(employeeType.getProperty("Manager")).thenReturn(managerProperty);

    EdmProperty employeeSimpleProperty = mock(EdmProperty.class);
    when(employeeSimpleProperty.getType()).thenReturn(edmSimpleType);
    when(employeeSimpleProperty.getName()).thenReturn("EmployeeName");
    when(employeeType.getProperty("EmployeeName")).thenReturn(employeeSimpleProperty);

    EdmComplexType locationComplexType = mock(EdmComplexType.class);
    when(locationComplexType.getKind()).thenReturn(EdmTypeKind.COMPLEX);

    EdmProperty locationComplexProperty = mock(EdmProperty.class);
    when(locationComplexProperty.getType()).thenReturn(locationComplexType);
    when(locationComplexProperty.getName()).thenReturn("Location");
    when(employeeType.getProperty("Location")).thenReturn(locationComplexProperty);

    EdmProperty countryProperty = mock(EdmProperty.class);
    when(countryProperty.getType()).thenReturn(edmSimpleType);
    when(countryProperty.getName()).thenReturn("Country");
    when(locationComplexType.getProperty("Country")).thenReturn(countryProperty);

    EdmEntitySet decimalsEntitySet = createEntitySetMock("Decimals", edmSimpleType, "Id");
    EdmEntitySet int16EntitySet = createEntitySetMock("Int16s", edmSimpleType, "Id");
    EdmEntitySet int32EntitySet = createEntitySetMock("Int32s", edmSimpleType, "Id");
    EdmEntitySet int64EntitySet = createEntitySetMock("Int64s", edmSimpleType, "Id");
    EdmEntitySet stringEntitySet = createEntitySetMock("Strings", edmSimpleType, "Id");
    EdmEntitySet singleEntitySet = createEntitySetMock("Singles", edmSimpleType, "Id");
    EdmEntitySet doubleEntitySet = createEntitySetMock("Doubles", edmSimpleType, "Id");
    EdmEntitySet dateTimeEntitySet = createEntitySetMock("DateTimes", edmSimpleType, "Id");
    EdmEntitySet dateTimeOffsetEntitySet = createEntitySetMock("DateTimeOffsets", edmSimpleType, "Id");
    EdmEntitySet booleanEntitySet = createEntitySetMock("Booleans", edmSimpleType, "Id");
    EdmEntitySet sByteEntitySet = createEntitySetMock("SBytes", edmSimpleType, "Id");
    EdmEntitySet binaryEntitySet = createEntitySetMock("Binaries", edmSimpleType, "Id");
    EdmEntitySet byteEntitySet = createEntitySetMock("Bytes", edmSimpleType, "Id");
    EdmEntitySet guidEntitySet = createEntitySetMock("Guids", edmSimpleType, "Id");
    EdmEntitySet timeEntitySet = createEntitySetMock("Times", edmSimpleType, "Id");

    EdmEntitySet teamsEntitySet = createEntitySetMock("Teams", edmSimpleType, "Id");
    when(teamsEntitySet.getEntityType().getProperty("nt_Employees")).thenReturn(employeeProperty);
    when(teamsEntitySet.getRelatedEntitySet(employeeProperty)).thenReturn(employeeEntitySet);

    when(defaultContainer.getEntitySet("Employees")).thenReturn(employeeEntitySet);
    when(defaultContainer.getEntitySet("Managers")).thenReturn(managerEntitySet);
    when(defaultContainer.getEntitySet("Teams")).thenReturn(teamsEntitySet);
    when(defaultContainer.getEntitySet("Decimals")).thenReturn(decimalsEntitySet);
    when(defaultContainer.getEntitySet("Int16s")).thenReturn(int16EntitySet);
    when(defaultContainer.getEntitySet("Int32s")).thenReturn(int32EntitySet);
    when(defaultContainer.getEntitySet("Int64s")).thenReturn(int64EntitySet);
    when(defaultContainer.getEntitySet("Strings")).thenReturn(stringEntitySet);
    when(defaultContainer.getEntitySet("Singles")).thenReturn(singleEntitySet);
    when(defaultContainer.getEntitySet("Doubles")).thenReturn(doubleEntitySet);
    when(defaultContainer.getEntitySet("DateTimes")).thenReturn(dateTimeEntitySet);
    when(defaultContainer.getEntitySet("DateTimeOffsets")).thenReturn(dateTimeOffsetEntitySet);
    when(defaultContainer.getEntitySet("Booleans")).thenReturn(booleanEntitySet);
    when(defaultContainer.getEntitySet("SBytes")).thenReturn(sByteEntitySet);
    when(defaultContainer.getEntitySet("Binaries")).thenReturn(binaryEntitySet);
    when(defaultContainer.getEntitySet("Bytes")).thenReturn(byteEntitySet);
    when(defaultContainer.getEntitySet("Guids")).thenReturn(guidEntitySet);
    when(defaultContainer.getEntitySet("Times")).thenReturn(timeEntitySet);

    EdmFunctionImport employeeSearchFunctionImport = createFunctionImportMock("EmployeeSearch", employeeType, EdmMultiplicity.MANY, employeeEntitySet);
    List<String> employeeSearchParameterNames = new ArrayList<String>();
    employeeSearchParameterNames.add("q");
    EdmParameter employeeSearchParameter = mock(EdmParameter.class);
    when(employeeSearchParameter.getType()).thenReturn(edmSimpleType);
    when(employeeSearchFunctionImport.getParameterNames()).thenReturn(employeeSearchParameterNames);
    when(employeeSearchFunctionImport.getParameter("q")).thenReturn(employeeSearchParameter);
    when(defaultContainer.getFunctionImport("EmployeeSearch")).thenReturn(employeeSearchFunctionImport);
    EdmFunctionImport allLocationsFunctionImport = createFunctionImportMock("AllLocations", locationComplexType, EdmMultiplicity.MANY, null);
    when(defaultContainer.getFunctionImport("AllLocations")).thenReturn(allLocationsFunctionImport);
    EdmFunctionImport allUsedRoomIdsFunctionImport = createFunctionImportMock("AllUsedRoomIds", edmSimpleType, EdmMultiplicity.MANY, null);
    when(defaultContainer.getFunctionImport("AllUsedRoomIds")).thenReturn(allUsedRoomIdsFunctionImport);
    EdmFunctionImport maximalAgeFunctionImport = createFunctionImportMock("MaximalAge", edmSimpleType, EdmMultiplicity.ONE, null);
    when(defaultContainer.getFunctionImport("MaximalAge")).thenReturn(maximalAgeFunctionImport);
    EdmFunctionImport mostCommonLocationFunctionImport = createFunctionImportMock("MostCommonLocation", locationComplexType, EdmMultiplicity.ONE, null);
    when(defaultContainer.getFunctionImport("MostCommonLocation")).thenReturn(mostCommonLocationFunctionImport);
    EdmFunctionImport managerPhotoFunctionImport = createFunctionImportMock("ManagerPhoto", edmSimpleType, EdmMultiplicity.ONE, null);
    List<String> managerPhotoParameterNames = new ArrayList<String>();
    managerPhotoParameterNames.add("Id");
    EdmParameter managerPhotoParameter = mock(EdmParameter.class);
    when(managerPhotoParameter.getType()).thenReturn(edmSimpleType);
    EdmFacets managerPhotoParameterFacets = mock(EdmFacets.class);
    when(managerPhotoParameterFacets.isNullable()).thenReturn(false);
    when(managerPhotoParameter.getFacets()).thenReturn(managerPhotoParameterFacets);
    when(managerPhotoFunctionImport.getParameterNames()).thenReturn(managerPhotoParameterNames);
    when(managerPhotoFunctionImport.getParameter("Id")).thenReturn(managerPhotoParameter);
    when(defaultContainer.getFunctionImport("ManagerPhoto")).thenReturn(managerPhotoFunctionImport);
    EdmFunctionImport oldestEmployeeFunctionImport = createFunctionImportMock("OldestEmployee", employeeType, EdmMultiplicity.ONE, null);
    when(defaultContainer.getFunctionImport("OldestEmployee")).thenReturn(oldestEmployeeFunctionImport);

    EdmEntityContainer specificContainer = mock(EdmEntityContainer.class);
    when(specificContainer.getEntitySet("Employees")).thenReturn(employeeEntitySet);
    when(specificContainer.getName()).thenReturn("Container");

    EdmProperty photoIdProperty = mock(EdmProperty.class);
    when(photoIdProperty.getType()).thenReturn(edmSimpleType);
    when(photoIdProperty.getName()).thenReturn("Id");
    EdmProperty photoTypeProperty = mock(EdmProperty.class);
    when(photoTypeProperty.getType()).thenReturn(edmSimpleType);
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
    when(edm.getEntityContainer("Container")).thenReturn(specificContainer);
    when(edm.getEntityContainer("Container2")).thenReturn(photoContainer);
    return edm;
  }

  private EdmEntitySet createEntitySetMock(final String name, final EdmType type, final String keyPropertyId) throws ODataException {
    EdmEntityType entityType = createEntityTypeMock(type, keyPropertyId);

    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(name);
    when(entitySet.getEntityType()).thenReturn(entityType);

    return entitySet;
  }

  private EdmEntityType createEntityTypeMock(final EdmType type, final String keyPropertyId) throws ODataException {
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

  private EdmFunctionImport createFunctionImportMock(final String name, final EdmType type, final EdmMultiplicity multiplicity, final EdmEntitySet entitySet) throws ODataException {
    EdmTyped returnType = mock(EdmTyped.class);
    when(returnType.getType()).thenReturn(type);
    when(returnType.getMultiplicity()).thenReturn(multiplicity);
    EdmFunctionImport functionImport = mock(EdmFunctionImport.class);
    when(functionImport.getName()).thenReturn(name);
    when(functionImport.getReturnType()).thenReturn(returnType);
    when(functionImport.getEntitySet()).thenReturn(entitySet);
    return functionImport;
  }
  
}
