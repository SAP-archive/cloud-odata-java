package com.sap.core.odata.core.uri.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sap.core.odata.core.edm.Edm;
import com.sap.core.odata.core.edm.EdmComplexType;
import com.sap.core.odata.core.edm.EdmEntityContainer;
import com.sap.core.odata.core.edm.EdmEntitySet;
import com.sap.core.odata.core.edm.EdmEntityType;
import com.sap.core.odata.core.edm.EdmFunctionImport;
import com.sap.core.odata.core.edm.EdmMultiplicity;
import com.sap.core.odata.core.edm.EdmNavigationProperty;
import com.sap.core.odata.core.edm.EdmProperty;
import com.sap.core.odata.core.edm.EdmServiceMetadata;
import com.sap.core.odata.core.edm.EdmSimpleType;
import com.sap.core.odata.core.edm.EdmType;
import com.sap.core.odata.core.edm.EdmTypeEnum;
import com.sap.core.odata.core.edm.EdmTyped;
import com.sap.core.odata.core.uri.KeyPredicate;
import com.sap.core.odata.core.uri.UriParser;
import com.sap.core.odata.core.uri.UriParserException;
import com.sap.core.odata.core.uri.UriParserResult;
import com.sap.core.odata.core.uri.enums.Format;
import com.sap.core.odata.core.uri.enums.InlineCount;
import com.sap.core.odata.core.uri.enums.UriType;
import org.apache.log4j.xml.DOMConfigurator;

public class UriParserTest {

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  public Edm getEdm() {
    return createMockEdm();
  }

  private Edm createMockEdm() {
    EdmServiceMetadata serviceMetadata = mock(EdmServiceMetadata.class);
    when(serviceMetadata.getDataServiceVersion()).thenReturn("MockEdm");

    EdmEntityContainer defaultContainer = mock(EdmEntityContainer.class);
    EdmEntitySet employeeEntitySet = createEntitySetMock("Employees", EdmSimpleType.STRING, "EmployeeId");
    EdmEntitySet managerEntitySet = createEntitySetMock("Managers", EdmSimpleType.STRING, "ManagerId");

    EdmType navigationType = mock(EdmType.class);
    when(navigationType.getKind()).thenReturn(EdmTypeEnum.NAVIGATION);

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
    when(employeeType.getKind()).thenReturn(EdmTypeEnum.ENTITY);
    when(employeeType.hasStream()).thenReturn(true);
    when(employeeType.getProperty("Manager")).thenReturn(managerProperty);

    EdmProperty employeeSimpleProperty = mock(EdmProperty.class);
    when(employeeSimpleProperty.getType()).thenReturn(EdmSimpleType.STRING);
    when(employeeSimpleProperty.getName()).thenReturn("EmployeeName");
    when(employeeType.getProperty("EmployeeName")).thenReturn(employeeSimpleProperty);

    EdmComplexType locationComplexType = mock(EdmComplexType.class);
    when(locationComplexType.getKind()).thenReturn(EdmTypeEnum.COMPLEX);

    EdmProperty locationComplexProperty = mock(EdmProperty.class);
    when(locationComplexProperty.getType()).thenReturn(locationComplexType);
    when(locationComplexProperty.getName()).thenReturn("Location");
    when(employeeType.getProperty("Location")).thenReturn(locationComplexProperty);

    EdmProperty countryProperty = mock(EdmProperty.class);
    when(countryProperty.getType()).thenReturn(EdmSimpleType.STRING);
    when(countryProperty.getName()).thenReturn("Country");
    when(locationComplexType.getProperty("Country")).thenReturn(countryProperty);

    EdmEntitySet decimalsEntitySet = createEntitySetMock("Decimals", EdmSimpleType.DECIMAL, "Id");
    EdmEntitySet int16EntitySet = createEntitySetMock("Int16s", EdmSimpleType.INT16, "Id");
    EdmEntitySet int32EntitySet = createEntitySetMock("Int32s", EdmSimpleType.INT32, "Id");
    EdmEntitySet int64EntitySet = createEntitySetMock("Int64s", EdmSimpleType.INT64, "Id");
    EdmEntitySet stringEntitySet = createEntitySetMock("Strings", EdmSimpleType.STRING, "Id");
    EdmEntitySet singleEntitySet = createEntitySetMock("Singles", EdmSimpleType.SINGLE, "Id");
    EdmEntitySet doubleEntitySet = createEntitySetMock("Doubles", EdmSimpleType.DOUBLE, "Id");
    EdmEntitySet dateTimeEntitySet = createEntitySetMock("DateTimes", EdmSimpleType.DATETIME, "Id");
    EdmEntitySet dateTimeOffsetEntitySet = createEntitySetMock("DateTimeOffsets", EdmSimpleType.DATETIMEOFFSET, "Id");
    EdmEntitySet booleanEntitySet = createEntitySetMock("Booleans", EdmSimpleType.BOOLEAN, "Id");
    EdmEntitySet sByteEntitySet = createEntitySetMock("SBytes", EdmSimpleType.SBYTE, "Id");
    EdmEntitySet binaryEntitySet = createEntitySetMock("Binaries", EdmSimpleType.BINARY, "Id");
    EdmEntitySet byteEntitySet = createEntitySetMock("Bytes", EdmSimpleType.BYTE, "Id");
    EdmEntitySet guidEntitySet = createEntitySetMock("Guids", EdmSimpleType.GUID, "Id");
    EdmEntitySet timeEntitySet = createEntitySetMock("Times", EdmSimpleType.TIME, "Id");

    EdmEntitySet teamsEntitySet = createEntitySetMock("Teams", EdmSimpleType.STRING, "TeamId");

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
    when(defaultContainer.getFunctionImport("EmployeeSearch")).thenReturn(employeeSearchFunctionImport);
    EdmFunctionImport allLocationsFunctionImport = createFunctionImportMock("AllLocations", locationComplexType, EdmMultiplicity.MANY, null);
    when(defaultContainer.getFunctionImport("AllLocations")).thenReturn(allLocationsFunctionImport);
    EdmFunctionImport allUsedRoomIdsFunctionImport = createFunctionImportMock("AllUsedRoomIds", EdmSimpleType.STRING, EdmMultiplicity.MANY, null);
    when(defaultContainer.getFunctionImport("AllUsedRoomIds")).thenReturn(allUsedRoomIdsFunctionImport);
    EdmFunctionImport maximalAgeFunctionImport = createFunctionImportMock("MaximalAge", EdmSimpleType.INT16, EdmMultiplicity.ONE, null);
    when(defaultContainer.getFunctionImport("MaximalAge")).thenReturn(maximalAgeFunctionImport);
    EdmFunctionImport mostCommonLocationFunctionImport = createFunctionImportMock("MostCommonLocation", locationComplexType, EdmMultiplicity.ONE, null);
    when(defaultContainer.getFunctionImport("MostCommonLocation")).thenReturn(mostCommonLocationFunctionImport);
    EdmFunctionImport managerPhotoFunctionImport = createFunctionImportMock("ManagerPhoto", EdmSimpleType.BINARY, EdmMultiplicity.ONE, null);
    when(defaultContainer.getFunctionImport("ManagerPhoto")).thenReturn(managerPhotoFunctionImport);
    EdmFunctionImport oldestEmployeeFunctionImport = createFunctionImportMock("OldestEmployee", employeeType, EdmMultiplicity.ONE, null);
    when(defaultContainer.getFunctionImport("OldestEmployee")).thenReturn(oldestEmployeeFunctionImport);

    EdmEntityContainer specificContainer = mock(EdmEntityContainer.class);
    when(specificContainer.getEntitySet("Employees")).thenReturn(employeeEntitySet);
    when(specificContainer.getName()).thenReturn("Container");

    EdmProperty photoIdProperty = mock(EdmProperty.class);
    when(photoIdProperty.getType()).thenReturn(EdmSimpleType.INT32);
    when(photoIdProperty.getName()).thenReturn("Id");
    EdmProperty photoTypeProperty = mock(EdmProperty.class);
    when(photoTypeProperty.getType()).thenReturn(EdmSimpleType.STRING);
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

  private EdmEntitySet createEntitySetMock(final String name, final EdmType type, final String keyPropertyId) {
    EdmEntityType entityType = createEntityTypeMock(type, keyPropertyId);

    EdmEntitySet entitySet = mock(EdmEntitySet.class);
    when(entitySet.getName()).thenReturn(name);
    when(entitySet.getEntityType()).thenReturn(entityType);

    return entitySet;
  }

  private EdmEntityType createEntityTypeMock(final EdmType type, final String keyPropertyId) {
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

  private EdmFunctionImport createFunctionImportMock(final String name, final EdmType type, final EdmMultiplicity multiplicity, final EdmEntitySet entitySet) {
    EdmTyped returnType = mock(EdmTyped.class);
    when(returnType.getType()).thenReturn(type);
    when(returnType.getMultiplicity()).thenReturn(multiplicity);
    EdmFunctionImport functionImport = mock(EdmFunctionImport.class);
    when(functionImport.getName()).thenReturn(name);
    when(functionImport.getReturnType()).thenReturn(returnType);
    when(functionImport.getEntitySet()).thenReturn(entitySet);
    return functionImport;
  }

  private UriParserResult parse(final String uri) throws UriParserException {
    return new UriParser(getEdm()).parse(uri);
  }

  public void parseWrongUri(final String uri) throws Exception {
    try {
      parse(uri);
      fail("Expected UriParserException not thrown");
    } catch (UriParserException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void parseServiceDocument() throws UriParserException {
    UriParserResult result = parse("/");
    assertEquals(UriType.URI0, result.getUriType());
  }

  @Test
  public void parseMetadata() throws UriParserException {
    UriParserResult result = parse("/$metadata");
    assertEquals(UriType.URI8, result.getUriType());
  }

  @Test
  public void parseMetadataError() throws Exception {
    parseWrongUri("/$metadata/somethingwrong");
  }

  @Test
  public void parseBatch() throws UriParserException {
    UriParserResult result = parse("/$batch");
    assertEquals(UriType.URI9, result.getUriType());
  }

  @Test
  public void parseBatchError() throws Exception {
    parseWrongUri("/$batch/somethingwrong");
  }

  @Test
  public void parseSomethingEntitySet() throws Exception {
    parseWrongUri("/somethingwrong");
  }

  @Test
  public void parseContainerWithoutEntitySet() throws Exception {
    parseWrongUri("Container");
  }

  @Test
  public void parseEmployeesEntitySet() throws UriParserException {
    UriParserResult result = parse("/Employees");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseEmployeesEntitySetParenthesesCount() throws UriParserException {
    UriParserResult result = parse("/Employees()/$count");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI15, result.getUriType());
    assertTrue(result.isCount());
  }

  @Test
  public void parseEmployeesEntitySetParenthesesCountNotLast() throws Exception {
    parseWrongUri("/Employees()/$count/somethingwrong");
  }

  @Test
  public void parseEmployeesEntitySetParentheses() throws UriParserException {
    UriParserResult result = parse("/Employees()");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseWrongEntities() throws Exception {
    parseWrongUri("/Employ ees('1')");
    parseWrongUri("/Employees()/somethingwrong");
    parseWrongUri("/Employees/somethingwrong");
  }

  @Test
  public void parseEmployeesEntityWithKey() throws UriParserException {
    UriParserResult result = parse("/Employees('1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntity() throws UriParserException {
    UriParserResult result = parse("/Employees('1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithExplicitKey() throws UriParserException {
    UriParserResult result = parse("/Employees(EmployeeId='1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithKeyValue() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/$value");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.getEntitySet().getEntityType().hasStream());
    assertEquals(UriType.URI17, result.getUriType());
    assertTrue(result.isValue());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithKeyCount() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI16, result.getUriType());
    assertTrue(result.isCount());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesSimpleProperty() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/EmployeeName");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI5, result.getUriType());
    assertEquals("EmployeeName", result.getPropertyPath().get(0).getName());
  }

  @Test
  public void parseEmployeesSimplePropertyValue() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/EmployeeName/$value");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI5, result.getUriType());
    assertEquals("EmployeeName", result.getPropertyPath().get(0).getName());
    assertTrue(result.isValue());
  }

  @Test
  public void parseEmployeesComplexProperty() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/Location");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI3, result.getUriType());
    assertEquals("Location", result.getPropertyPath().get(0).getName());
  }

  @Test
  public void parseEmployeesComplexPropertyWithEntity() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/Location/Country");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI4, result.getUriType());
    assertEquals("Location", result.getPropertyPath().get(0).getName());
    assertEquals("Country", result.getPropertyPath().get(1).getName());
  }

  @Test
  public void parseEmployeesComplexPropertyWithEntityValue() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/Location/Country/$value");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI4, result.getUriType());
    assertEquals("Location", result.getPropertyPath().get(0).getName());
    assertEquals("Country", result.getPropertyPath().get(1).getName());
    assertTrue(result.isValue());
  }

  @Test
  public void simplePropertyWrong() throws Exception {
    parseWrongUri("/Employees('1')/EmployeeName(1)");
    parseWrongUri("/Employees('1')/EmployeeName()");
    parseWrongUri("/Employees('1')/EmployeeName/something");
    parseWrongUri("/Employees('1')/EmployeeName/$value/something");
  }

  @Test
  public void complexPropertyWrong() throws Exception {
    parseWrongUri("/Employees('1')/Location(1)");
    parseWrongUri("/Employees('1')/Location/somethingwrong");
  }

  @Test
  public void EmployeesNoProperty() throws Exception {
    parseWrongUri("/Employees('1')/somethingwrong");
  }

  @Test
  public void parseNavigationPropertyWithEntityResult() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/Manager");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntitySetResult() throws UriParserException {
    UriParserResult result = parse("/Managers('1')/Employees");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntitySetResultParenthesis() throws UriParserException {
    UriParserResult result = parse("/Managers('1')/Employees()");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntityResultWithKey() throws UriParserException {
    UriParserResult result = parse("/Managers('1')/Employees('1')");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksOne() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/$links/Manager");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertEquals(UriType.URI7A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksMany() throws UriParserException {
    UriParserResult result = parse("/Managers('1')/$links/Employees");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertEquals(UriType.URI7B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithManagersCount() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/Manager/$count");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI16, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEmployeesCount() throws UriParserException {
    UriParserResult result = parse("/Managers('1')/Employees/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI15, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEmployeeCount() throws UriParserException {
    UriParserResult result = parse("Managers('1')/Employees('1')/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI16, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksCountMany() throws UriParserException {
    UriParserResult result = parse("/Managers('1')/$links/Employees/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertTrue(result.isCount());
    assertEquals(UriType.URI50B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksCountOne() throws UriParserException {
    UriParserResult result = parse("/Employees('1')/$links/Manager/$count");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertTrue(result.isCount());
    assertEquals(UriType.URI50A, result.getUriType());

    result = parse("/Managers('1')/$links/Employees('1')/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertTrue(result.isCount());
    assertEquals(UriType.URI50A, result.getUriType());
  }

  @Test
  public void navigationPropertyWrong() throws Exception {
    parseWrongUri("/Employees('1')/Manager()");
    parseWrongUri("/Employees('1')/Manager('1')");
    parseWrongUri("/Employees('1')/$links/Manager('1')");
    parseWrongUri("/Employees('1')/$links/Manager()");
    parseWrongUri("/Employees('1')/$links/Manager/somethingwrong");
    parseWrongUri("/Employees('1')/Manager/$count/somethingwrong");
    parseWrongUri("/Employees('1')/$links/Manager/$count/somethingwrong");
    parseWrongUri("/Employees('1')/Manager/$value");
    parseWrongUri("/Managers('1')/Employees('1')/$value/somethingwrong");
    parseWrongUri("/Managers('1')/Employees/$links");
    parseWrongUri("/Employees('1')/$links/somethingwrong");
    parseWrongUri("/Employees('1')/$links/EmployeeName");
  }
  
  @Test
  public void navigationPathWrongMatch() throws Exception {
    parseWrongUri("/Employees('1')/(somethingwrong(");
    
  }
  
  @Test
  public void navigationSegmentWrongMatch() throws Exception {
    parseWrongUri("/Employees('1')/$links/(somethingwrong(");
    
  }

  @Test
  public void parseTeamsEntityWithIntKeyValue() throws Exception {
    parseWrongUri("/Teams(1)/$value");
  }

  @Test
  public void parseWrongKey() throws Exception {
    parseWrongUri("/Employees(EmployeeId='1',somethingwrong=abc)");
    parseWrongUri("/Employees(somethingwrong=1)");
    parseWrongUri("/Container2.Photos(Id=1;Type='abc')");
    parseWrongUri("Container2.Photos(Id=1,'abc')");
    parseWrongUri("Container2.Photos(Id=1)");
  }

  @Test
  public void parsePhotoEntityWithExplicitKeySet() throws UriParserException {
    UriParserResult result = parse("/Container2.Photos(Id=1,Type='abc')");
    assertEquals("Container2", result.getEntityContainer().getName());
    assertEquals("Photos", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());
    assertEquals(2, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("Id", result.getKeyPredicates().get(0).getProperty().getName());
    assertEquals("abc", result.getKeyPredicates().get(1).getLiteral());
    assertEquals("Type", result.getKeyPredicates().get(1).getProperty().getName());

    result = parse("/Container2.Photos(Id=1,Type='abc%20xyz')");
    assertEquals("abc xyz", result.getKeyPredicates().get(1).getLiteral());

    result = parse("/Container2.Photos(Id=1,Type='image%2Fpng')");
    assertEquals("image/png", result.getKeyPredicates().get(1).getLiteral());
  }

  @Test
  public void parseContainerEmployeesEntitySet() throws UriParserException {
    UriParserResult result = parse("/Container.Employees");
    assertEquals("Container", result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseContainerEmployeesEntitySetParenthese() throws UriParserException {
    UriParserResult result = parse("/Container.Employees()");
    assertEquals("Container", result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseContainerEmployeesEntityWithKey() throws UriParserException {
    UriParserResult result = parse("/Container.Employees('1')");
    assertEquals("Container", result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseNonexistentContainer() throws Exception {
    parseWrongUri("/somethingwrong.Employees()");
  }

  @Test
  public void parseInvalidSegment() throws Exception {
    parseWrongUri("/.somethingwrong");
  }

  /**
   * Parse an URI string with an entity set and a single-value key
   * and assert that it has the correct parsed type and value.
   * 
   * @param uri
   *          the URI to be parsed as string; it must refer to an entity with one key property
   * @param type
   *          the expected {@link EdmSimpleType} of the key property
   * @param expectedLiteral
   *          the expected literal value of the key
   * @throws UriParserException
   */
  private void parseOneKey(final String uri, final EdmType type, final String expectedLiteral) throws UriParserException {
    final UriParserResult result = parse(uri);
    final KeyPredicate keyPredicate = result.getKeyPredicates().get(0);

    assertEquals(type, keyPredicate.getProperty().getType());
    assertEquals(expectedLiteral, keyPredicate.getLiteral());
  }

  @Test
  public void parseDecimalKey() throws UriParserException {
    parseOneKey("/Decimals(4.5m)", EdmSimpleType.DECIMAL, "4.5");
    parseOneKey("/Decimals(4.5M)", EdmSimpleType.DECIMAL, "4.5");
    parseOneKey("/Decimals(1)", EdmSimpleType.DECIMAL, "1");
    parseOneKey("/Decimals(255)", EdmSimpleType.DECIMAL, "255");
    parseOneKey("/Decimals(-32768)", EdmSimpleType.DECIMAL, "-32768");
    parseOneKey("/Decimals(32768)", EdmSimpleType.DECIMAL, "32768");
    parseOneKey("/Decimals(3000000)", EdmSimpleType.DECIMAL, "3000000");
    parseOneKey("/Decimals(4.5d)", EdmSimpleType.DECIMAL, "4.5");
    parseOneKey("/Decimals(4.2E9F)", EdmSimpleType.DECIMAL, "4.2E9");
    parseOneKey("/Decimals(1234567890L)", EdmSimpleType.DECIMAL, "1234567890");
  }

  @Test
  public void parseInt16Key() throws UriParserException {
    parseOneKey("/Int16s(16)", EdmSimpleType.INT16, "16");
    parseOneKey("/Int16s(-16)", EdmSimpleType.INT16, "-16");
    parseOneKey("/Int16s(255)", EdmSimpleType.INT16, "255");
    parseOneKey("/Int16s(-32768)", EdmSimpleType.INT16, "-32768");
  }

  @Test
  public void parseInt32Key() throws UriParserException {
    parseOneKey("/Int32s(32)", EdmSimpleType.INT32, "32");
    parseOneKey("/Int32s(-127)", EdmSimpleType.INT32, "-127");
    parseOneKey("/Int32s(255)", EdmSimpleType.INT32, "255");
    parseOneKey("/Int32s(32767)", EdmSimpleType.INT32, "32767");
    parseOneKey("/Int32s(-32769)", EdmSimpleType.INT32, "-32769");
  }

  @Test
  public void parseInt64Key() throws UriParserException {
    parseOneKey("/Int64s(64)", EdmSimpleType.INT64, "64");
    parseOneKey("/Int64s(255)", EdmSimpleType.INT64, "255");
    parseOneKey("/Int64s(1000)", EdmSimpleType.INT64, "1000");
    parseOneKey("/Int64s(100000)", EdmSimpleType.INT64, "100000");
    parseOneKey("/Int64s(-64L)", EdmSimpleType.INT64, "-64");
    parseOneKey("/Int64s(" + Long.MAX_VALUE + "L)", EdmSimpleType.INT64, "" + Long.MAX_VALUE);
    parseOneKey("/Int64s(" + Long.MIN_VALUE + "l)", EdmSimpleType.INT64, "" + Long.MIN_VALUE);
  }

  @Test
  public void parseStringKey() throws UriParserException {
    parseOneKey("/Strings('abc')", EdmSimpleType.STRING, "abc");
    parseOneKey("/Strings('abc%20xyz')", EdmSimpleType.STRING, "abc xyz");
    parseOneKey("/Strings('true')", EdmSimpleType.STRING, "true");
    parseOneKey("/Strings('')", EdmSimpleType.STRING, "");
  }

  @Test
  public void parseSingleKey() throws UriParserException {
    parseOneKey("/Singles(45)", EdmSimpleType.SINGLE, "45");
    parseOneKey("/Singles(255)", EdmSimpleType.SINGLE, "255");
    parseOneKey("/Singles(-32768)", EdmSimpleType.SINGLE, "-32768");
    parseOneKey("/Singles(32768)", EdmSimpleType.SINGLE, "32768");
    parseOneKey("/Singles(1L)", EdmSimpleType.SINGLE, "1");
    parseOneKey("/Singles(4.5f)", EdmSimpleType.SINGLE, "4.5");
    parseOneKey("/Singles(4.5F)", EdmSimpleType.SINGLE, "4.5");
    parseOneKey("/Singles(4.5e9f)", EdmSimpleType.SINGLE, "4.5e9");
  }

  @Test
  public void parseDoubleKey() throws UriParserException {
    parseOneKey("/Doubles(45)", EdmSimpleType.DOUBLE, "45");
    parseOneKey("/Doubles(255)", EdmSimpleType.DOUBLE, "255");
    parseOneKey("/Doubles(-32768)", EdmSimpleType.DOUBLE, "-32768");
    parseOneKey("/Doubles(32768)", EdmSimpleType.DOUBLE, "32768");
    parseOneKey("/Doubles(1l)", EdmSimpleType.DOUBLE, "1");
    parseOneKey("/Doubles(4.5d)", EdmSimpleType.DOUBLE, "4.5");
    parseOneKey("/Doubles(4.5D)", EdmSimpleType.DOUBLE, "4.5");
    parseOneKey("/Doubles(4.5e21f)", EdmSimpleType.DOUBLE, "4.5e21");
  }

  @Test
  public void parseByteKey() throws UriParserException {
    parseOneKey("/Bytes(255)", EdmSimpleType.BYTE, "255");
    parseOneKey("Bytes(123)", EdmSimpleType.BYTE, "123");
  }

  @Test
  public void parseGuidKey() throws UriParserException {
    parseOneKey("/Guids(guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a')", EdmSimpleType.GUID, "1225c695-cfb8-4ebb-aaaa-80da344efa6a");
  }

  @Test
  public void parseTimeKey() throws UriParserException {
    parseOneKey("/Times(time'P120D')", EdmSimpleType.TIME, "P120D");
  }

  @Test
  public void parseDatetimeKey() throws UriParserException {
    parseOneKey("/DateTimes(datetime'2009-12-26T21%3A23%3A38')", EdmSimpleType.DATETIME, "2009-12-26T21:23:38");
    parseOneKey("/DateTimes(datetime'2009-12-26T21%3A23%3A38Z')", EdmSimpleType.DATETIME, "2009-12-26T21:23:38Z");
  }

  @Test
  public void parseDatetimeOffsetKey() throws UriParserException {
    parseOneKey("/DateTimeOffsets(datetimeoffset'2009-12-26T21%3A23%3A38Z')", EdmSimpleType.DATETIMEOFFSET, "2009-12-26T21:23:38Z");
    parseOneKey("/DateTimeOffsets(datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00')", EdmSimpleType.DATETIMEOFFSET, "2002-10-10T12:00:00-05:00");
  }

  @Test
  public void parseBooleanKey() throws UriParserException {
    parseOneKey("/Booleans(true)", EdmSimpleType.BOOLEAN, "true");
    parseOneKey("/Booleans(false)", EdmSimpleType.BOOLEAN, "false");
    parseOneKey("/Booleans(1)", EdmSimpleType.BOOLEAN, "1");
    parseOneKey("/Booleans(0)", EdmSimpleType.BOOLEAN, "0");
  }

  @Test
  public void parseSByteKey() throws UriParserException {
    parseOneKey("/SBytes(-123)", EdmSimpleType.SBYTE, "-123");
    parseOneKey("/SBytes(12)", EdmSimpleType.SBYTE, "12");
  }

  @Test
  public void parseBinaryKey() throws UriParserException {
    parseOneKey("/Binaries(X'Fa12aAA1')", EdmSimpleType.BINARY, "+hKqoQ==\r\n");
    parseOneKey("/Binaries(binary'FA12AAA1')", EdmSimpleType.BINARY, "+hKqoQ==\r\n");
  }

  @Test(expected = UriParserException.class)
  public void parseBinaryKeyWrongContent() throws UriParserException {
    parse("/Binaries(binary'abcde')");
  }

  @Test(expected = UriParserException.class)
  public void parseStringKeyWrongContent() throws UriParserException {
    parse("Strings(')");
  }

  @Test(expected = UriParserException.class)
  public void parseStringKeyMissingQuote() throws UriParserException {
    parse("Strings('a)");
  }

  @Test(expected = UriParserException.class)
  public void parseTimeKeyWithWrongPrefix() throws UriParserException {
    parse("Times(wrongprefix'PT1H2M3S')");
  }

  @Test(expected = UriParserException.class)
  public void parseInt32KeyWithWrongSuffix() throws UriParserException {
    parse("/Int32s(32i)");
  }

  @Test(expected = UriParserException.class)
  public void parseTooLargeNumber() throws UriParserException {
    parse("Int32s(12345678901234567890)");
  }

  @Test
  public void parseIncompatibleKeys() throws Exception {
    parseWrongUri("Binaries(1D)");
    parseWrongUri("Booleans('0')");
    parseWrongUri("Booleans('1')");
    parseWrongUri("Booleans(2)");
    parseWrongUri("Bytes(-1)");
    parseWrongUri("Bytes(-129)");
    parseWrongUri("DateTimes(time'PT11H12M13S')");
    parseWrongUri("DateTimeOffsets(time'PT11H12M13S')");
    parseWrongUri("Decimals('1')");
    parseWrongUri("Doubles(1M)");
    parseWrongUri("Guids(1)");
    parseWrongUri("Int16s(32768)");
    parseWrongUri("Int32s(1L)");
    parseWrongUri("Int64s(1M)");
    parseWrongUri("SBytes(128)");
    parseWrongUri("Singles(1D)");
    parseWrongUri("Strings(1)");
    parseWrongUri("Times(datetime'2012-10-10T11:12:13')");
  }

  @Test
  public void parseFunctionImports() throws Exception {
    UriParserResult result = parse("EmployeeSearch");
    assertEquals("EmployeeSearch", result.getFunctionImport().getName());
    assertEquals(EdmTypeEnum.ENTITY, result.getTargetType().getKind());
    assertEquals(UriType.URI1, result.getUriType());

    result = parse("AllLocations");
    assertEquals("AllLocations", result.getFunctionImport().getName());
    assertEquals(UriType.URI11, result.getUriType());

    result = parse("AllUsedRoomIds");
    assertEquals("AllUsedRoomIds", result.getFunctionImport().getName());
    assertEquals(UriType.URI13, result.getUriType());

    result = parse("MaximalAge");
    assertEquals("MaximalAge", result.getFunctionImport().getName());
    assertEquals(UriType.URI14, result.getUriType());

    result = parse("MaximalAge/$value");
    assertEquals("MaximalAge", result.getFunctionImport().getName());
    assertTrue(result.isValue());
    assertEquals(UriType.URI14, result.getUriType());

    result = parse("MostCommonLocation");
    assertEquals("MostCommonLocation", result.getFunctionImport().getName());
    assertEquals(UriType.URI12, result.getUriType());

    result = parse("ManagerPhoto");
    assertEquals("ManagerPhoto", result.getFunctionImport().getName());
    assertEquals(UriType.URI14, result.getUriType());

    result = parse("OldestEmployee");
    assertEquals("OldestEmployee", result.getFunctionImport().getName());
    assertEquals(UriType.URI10, result.getUriType());
  }

  @Test
  public void parseWrongFunctionImports() throws Exception {
    parseWrongUri("AllLocations/$value");
    parseWrongUri("ManagerPhoto()");
    parseWrongUri("MaximalAge/somethingwrong");
  }

  @Test
  public void parseSystemQueryOptions() throws Exception {
    UriParserResult result = parse("Employees?$format=json&$inlinecount=allpages&$skiptoken=abc&$skip=2&$top=1");
    assertEquals("Employees", result.getEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.JSON, result.getFormat());
    assertEquals(InlineCount.ALLPAGES, result.getInlineCount());
    assertEquals("abc", result.getSkipToken());
    assertEquals(2, result.getSkip());
    assertEquals(1, result.getTop().intValue());

    result = parse("Employees?$format=atom&$inlinecount=none&$top=0");
    assertEquals(Format.ATOM, result.getFormat());
    assertEquals(InlineCount.NONE, result.getInlineCount());
    assertEquals(0, result.getTop().intValue());

    
    result = parse("Employees?$format=json&$inlinecount=none");
    assertEquals("Employees", result.getEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.JSON, result.getFormat());
    assertEquals(InlineCount.NONE, result.getInlineCount());
        
    result = parse("Employees?$format=atom");
    assertEquals("Employees", result.getEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.ATOM, result.getFormat());
    
    result = parse("Employees?$format=xml");
    assertEquals("Employees", result.getEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.XML, result.getFormat());
  }

  @Test
  public void parseWrongSystemQueryOptions() throws Exception {
    parseWrongUri("Employees?$format=");
    parseWrongUri("Employees?$inlinecount=no");
    parseWrongUri("Employees?&$skiptoken==");
    parseWrongUri("Employees?$skip=-1");
    parseWrongUri("Employees?$skip='a'");
    parseWrongUri("Employees?$top=-1");
    parseWrongUri("Employees?$top=12345678901234567890");
    
  }
}