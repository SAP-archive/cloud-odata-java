package com.sap.core.odata.core.uri.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleTypeFacade;
import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.uri.KeyPredicate;
import com.sap.core.odata.api.uri.UriParserException;
import com.sap.core.odata.api.uri.UriParserResult;
import com.sap.core.odata.api.uri.enums.Format;
import com.sap.core.odata.api.uri.enums.InlineCount;
import com.sap.core.odata.api.uri.enums.UriType;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.UriParserResultImpl;
import com.sap.core.testutils.mocks.MockFacade;

public class UriParserTest {

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  private static Edm edm;
  private static EdmSimpleTypeFacade facade = new EdmSimpleTypeFacade();
  
  @BeforeClass
  public static void getEdm() throws EdmException {
    edm = MockFacade.getMockEdm();
  }

  
 /**
   * Parse the URI part after an OData service root, given as string.
   * Query parameters can be included.
   * @param uri  the URI part
   * @return a {@link UriParserResultImpl} instance containing the parsed information
   * @throws UriParserException
   * @throws EdmException
   */
  private UriParserResultImpl parse(final String uri) throws UriParserException {
    final String[] path = uri.split("\\?", -1);
    if (path.length > 2)
      throw new UriParserException("Wrong URI Syntax");

    final List<String> pathSegments = getPathSegments(path[0]);
    Map<String, String> queryParameters;
    if (path.length == 2){
      queryParameters = getQueryParameters(unescape(path[1]));
    }else{
      queryParameters = new HashMap<String, String>();
    }
      
    UriParserResult result = new UriParserImpl(edm).parse(pathSegments, queryParameters);
    
    return (UriParserResultImpl) result;
  }

  private List<String> getPathSegments(final String uri) throws UriParserException {
    List<String> pathSegments = new ArrayList<String>();
    for (final String segment : uri.split("/", -1)) {
      final String unescapedSegment = unescape(segment);
      pathSegments.add(unescapedSegment);
    }
    return pathSegments;
  }

  private Map<String, String> getQueryParameters(final String uri) {
    Map<String, String> queryParameters = new HashMap<String, String>();
    for (final String option : uri.split("&")) {
      final String[] keyAndValue = option.split("=");
      if (keyAndValue.length == 2)
        queryParameters.put(keyAndValue[0], keyAndValue[1]);
      else
        queryParameters.put(keyAndValue[0], "");
    }
    return queryParameters;
  }

  private String unescape(final String s) throws UriParserException {
    try {
      return new URI(s).getPath();
    } catch (URISyntaxException e) {
      throw new UriParserException("Error while unescaping", e);
    }
  }

  private void parseWrongUri(final String uri) {
    try {
      parse(uri);
      fail("Expected UriParserException not thrown");
    } catch (UriParserException e) {
      assertNotNull(e);
    }
  }

  @Test
  public void parseServiceDocument() throws Exception {
    UriParserResult result = parse("/");
    assertEquals(UriType.URI0, result.getUriType());

    result = parse("");
    assertEquals(UriType.URI0, result.getUriType());

    result = new UriParserImpl(edm).parse(Collections.<String> emptyList(), Collections.<String, String> emptyMap());
    assertEquals(UriType.URI0, result.getUriType());
  }

  @Test
  public void parseMetadata() throws Exception {
    UriParserResultImpl result = parse("/$metadata");
    assertEquals(UriType.URI8, result.getUriType());
  }

  @Test
  public void parseMetadataError() throws Exception {
    parseWrongUri("/$metadata/somethingwrong");
  }

  @Test
  public void parseBatch() throws Exception {
    UriParserResultImpl result = parse("/$batch");
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
    parseWrongUri("Container1.");
  }

  @Test
  public void parseEmployeesEntitySet() throws Exception {
    UriParserResultImpl result = parse("/Employees");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseEmployeesEntitySetParenthesesCount() throws Exception {
    UriParserResultImpl result = parse("/Employees()/$count");
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
  public void parseEmployeesEntitySetParentheses() throws Exception {
    UriParserResultImpl result = parse("/Employees()");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseWrongEntities() throws Exception {
    parseWrongUri("//");
    parseWrongUri("/Employ ees('1')");
    parseWrongUri("/Employees()/somethingwrong");
    parseWrongUri("/Employees/somethingwrong");
    parseWrongUri("Employees/");
    parseWrongUri("//Employees");
    parseWrongUri("Employees//");
  }

  @Test
  public void parseEmployeesEntityWithKey() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntity() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithExplicitKey() throws Exception {
    UriParserResultImpl result = parse("/Employees(EmployeeId='1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithKeyValue() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/$value");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.getEntitySet().getEntityType().hasStream());
    assertEquals(UriType.URI17, result.getUriType());
    assertTrue(result.isValue());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithKeyCount() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI16, result.getUriType());
    assertTrue(result.isCount());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesSimpleProperty() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/EmployeeName");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI5, result.getUriType());
    assertEquals("EmployeeName", result.getPropertyPath().get(0).getName());
  }

  @Test
  public void parseEmployeesSimplePropertyValue() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/EmployeeName/$value");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI5, result.getUriType());
    assertEquals("EmployeeName", result.getPropertyPath().get(0).getName());
    assertTrue(result.isValue());
  }

  @Test
  public void parseEmployeesComplexProperty() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/Location");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI3, result.getUriType());
    assertEquals("Location", result.getPropertyPath().get(0).getName());
  }

  @Test
  public void parseEmployeesComplexPropertyWithEntity() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/Location/Country");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI4, result.getUriType());
    assertEquals("Location", result.getPropertyPath().get(0).getName());
    assertEquals("Country", result.getPropertyPath().get(1).getName());
  }

  @Test
  public void parseEmployeesComplexPropertyWithEntityValue() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/Location/Country/$value");
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
  public void parseNavigationPropertyWithEntityResult() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/ne_Manager");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntitySetResult() throws Exception {
    UriParserResultImpl result = parse("/Managers('1')/nm_Employees");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntitySetResultParenthesis() throws Exception {
    UriParserResultImpl result = parse("/Managers('1')/nm_Employees()");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntityResultWithKey() throws Exception {
    UriParserResultImpl result = parse("/Managers('1')/nm_Employees('1')");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksOne() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/$links/ne_Manager");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertEquals(UriType.URI7A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksMany() throws Exception {
    UriParserResultImpl result = parse("/Managers('1')/$links/nm_Employees");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertEquals(UriType.URI7B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithManagersCount() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/ne_Manager/$count");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI16, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEmployeesCount() throws Exception {
    UriParserResultImpl result = parse("/Managers('1')/nm_Employees/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI15, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEmployeeCount() throws Exception {
    UriParserResultImpl result = parse("Managers('1')/nm_Employees('1')/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI16, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksCountMany() throws Exception {
    UriParserResultImpl result = parse("/Managers('1')/$links/nm_Employees/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertTrue(result.isCount());
    assertEquals(UriType.URI50B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksCountOne() throws Exception {
    UriParserResultImpl result = parse("/Employees('1')/$links/ne_Manager/$count");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertTrue(result.isCount());
    assertEquals(UriType.URI50A, result.getUriType());

    result = parse("/Managers('1')/$links/nm_Employees('1')/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertTrue(result.isCount());
    assertEquals(UriType.URI50A, result.getUriType());
  }

  @Test
  public void navigationPropertyWrong() throws Exception {
    parseWrongUri("Employees('1')//ne_Manager");
    parseWrongUri("Employees('1')/ne_Manager()");
    parseWrongUri("Employees('1')/ne_Manager('1')");
    parseWrongUri("Employees('1')/$links");
    parseWrongUri("Employees('1')/$links/ne_Manager('1')");
    parseWrongUri("Employees('1')/$links/ne_Manager()");
    parseWrongUri("Employees('1')/$links/ne_Manager/somethingwrong");
    parseWrongUri("Employees('1')/ne_Manager/$count/somethingwrong");
    parseWrongUri("Employees('1')/$links/ne_Manager/$count/somethingwrong");
    parseWrongUri("Employees('1')/ne_Manager/$value");
    parseWrongUri("Managers('1')/nm_Employees('1')/$value/somethingwrong");
    parseWrongUri("Managers('1')/nm_Employees/$links");
    parseWrongUri("Managers('1')/nm_Employees/$links/Manager");
    parseWrongUri("Managers('1')/nm_Employees/somethingwrong");
    parseWrongUri("Employees('1')/$links/somethingwrong");
    parseWrongUri("Employees('1')/$links/EmployeeName");
    parseWrongUri("Employees('1')/$links/$links/ne_Manager");
    parseWrongUri("Managers('1')/nm_Employee/");
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
    parseWrongUri("Employees(,'1')");
    parseWrongUri("Employees('1',)");
    parseWrongUri("Employees(EmployeeName='1')");
    parseWrongUri("Employees(EmployeeId='1',EmployeeId='1')");
    parseWrongUri("/Employees(EmployeeId='1',somethingwrong=abc)");
    parseWrongUri("/Employees(somethingwrong=1)");
    parseWrongUri("/Container2.Photos(Id=1,,Type='abc')");
    parseWrongUri("/Container2.Photos(Id=1;Type='abc')");
    parseWrongUri("Container2.Photos(Id=1,'abc')");
    parseWrongUri("Container2.Photos(Id=1)");
  }

  @Test
  public void parsePhotoEntityWithExplicitKeySet() throws Exception {
    UriParserResultImpl result = parse("/Container2.Photos(Id=1,Type='abc')");
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
  public void parseContainerEmployeesEntitySet() throws Exception {
    UriParserResultImpl result = parse("/Container1.Employees");
    assertEquals("Container1", result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseContainerEmployeesEntitySetParenthese() throws Exception {
    UriParserResultImpl result = parse("/Container1.Employees()");
    assertEquals("Container1", result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseContainerEmployeesEntityWithKey() throws Exception {
    UriParserResultImpl result = parse("/Container1.Employees('1')");
    assertEquals("Container1", result.getEntityContainer().getName());
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
   * Parse a URI string with an entity set and a single-value key
   * and assert that it has the correct parsed type and value.
   * 
   * @param uri
   *          the URI to be parsed as string; it must refer to an entity with one key property
   * @param type
   *          the expected {@link } of the key property
   * @param expectedLiteral
   *          the expected literal value of the key
   * @throws UriParserException
   * @throws EdmException 
   */
  private void parseOneKey(final String uri, final EdmType type, final String expectedLiteral) throws UriParserException, EdmException {
    final UriParserResultImpl result = parse(uri);
    final KeyPredicate keyPredicate = result.getKeyPredicates().get(0);

    assertEquals(type, keyPredicate.getProperty().getType());
    assertEquals(expectedLiteral, keyPredicate.getLiteral());
  }

  @Test
  public void parseDecimalKey() throws Exception {
    parseOneKey("/Decimals(4.5m)", facade.decimalInstance(), "4.5");
    parseOneKey("/Decimals(4.5M)", facade.decimalInstance(), "4.5");
    parseOneKey("/Decimals(1)", facade.decimalInstance(), "1");
    parseOneKey("/Decimals(255)", facade.decimalInstance(), "255");
    parseOneKey("/Decimals(-32768)", facade.decimalInstance(), "-32768");
    parseOneKey("/Decimals(32768)", facade.decimalInstance(), "32768");
    parseOneKey("/Decimals(3000000)", facade.decimalInstance(), "3000000");
    parseOneKey("/Decimals(4.5d)", facade.decimalInstance(), "4.5");
    parseOneKey("/Decimals(4.2E9F)", facade.decimalInstance(), "4.2E9");
    parseOneKey("/Decimals(1234567890L)", facade.decimalInstance(), "1234567890");
  }

  @Test
  public void parseInt16Key() throws Exception {
    parseOneKey("/Int16s(16)", facade.int16Instance(), "16");
    parseOneKey("/Int16s(-16)", facade.int16Instance(), "-16");
    parseOneKey("/Int16s(255)", facade.int16Instance(), "255");
    parseOneKey("/Int16s(-32768)", facade.int16Instance(), "-32768");

  }

  @Test
  public void parseInt32Key() throws Exception {
    parseOneKey("/Int32s(32)", facade.int32Instance(), "32");
    parseOneKey("/Int32s(-127)", facade.int32Instance(), "-127");
    parseOneKey("/Int32s(255)", facade.int32Instance(), "255");
    parseOneKey("/Int32s(32767)", facade.int32Instance(), "32767");
    parseOneKey("/Int32s(-32769)", facade.int32Instance(), "-32769");
  }

  @Test
  public void parseInt64Key() throws Exception {
    parseOneKey("/Int64s(64)", facade.int64Instance(), "64");
    parseOneKey("/Int64s(255)", facade.int64Instance(), "255");
    parseOneKey("/Int64s(1000)", facade.int64Instance(), "1000");
    parseOneKey("/Int64s(100000)", facade.int64Instance(), "100000");
    parseOneKey("/Int64s(-64L)", facade.int64Instance(), "-64");
    parseOneKey("/Int64s(" + Long.MAX_VALUE + "L)", facade.int64Instance(), "" + Long.MAX_VALUE);
    parseOneKey("/Int64s(" + Long.MIN_VALUE + "l)", facade.int64Instance(), "" + Long.MIN_VALUE);
  }

  @Test
  public void parseStringKey() throws Exception {
    parseOneKey("/Strings('abc')", facade.stringInstance(), "abc");
    parseOneKey("/Strings('abc%20xyz')", facade.stringInstance(), "abc xyz");
    parseOneKey("/Strings('true')", facade.stringInstance(), "true");
    parseOneKey("/Strings('')", facade.stringInstance(), "");
  }

  @Test
  public void parseSingleKey() throws Exception {
    parseOneKey("/Singles(45)", facade.singleInstance(), "45");
    parseOneKey("/Singles(255)", facade.singleInstance(), "255");
    parseOneKey("/Singles(-32768)", facade.singleInstance(), "-32768");
    parseOneKey("/Singles(32768)", facade.singleInstance(), "32768");
    parseOneKey("/Singles(1L)", facade.singleInstance(), "1");
    parseOneKey("/Singles(4.5f)", facade.singleInstance(), "4.5");
    parseOneKey("/Singles(4.5F)", facade.singleInstance(), "4.5");
    parseOneKey("/Singles(4.5e9f)", facade.singleInstance(), "4.5e9");
  }

  @Test
  public void parseDoubleKey() throws Exception {
    parseOneKey("/Doubles(45)", facade.doubleInstance(), "45");
    parseOneKey("/Doubles(255)", facade.doubleInstance(), "255");
    parseOneKey("/Doubles(-32768)", facade.doubleInstance(), "-32768");
    parseOneKey("/Doubles(32768)", facade.doubleInstance(), "32768");
    parseOneKey("/Doubles(1l)", facade.doubleInstance(), "1");
    parseOneKey("/Doubles(4.5d)", facade.doubleInstance(), "4.5");
    parseOneKey("/Doubles(4.5D)", facade.doubleInstance(), "4.5");
    parseOneKey("/Doubles(4.5e21f)", facade.doubleInstance(), "4.5e21");
  }

  @Test
  public void parseByteKey() throws Exception {
    parseOneKey("/Bytes(255)", facade.byteInstance(), "255");
    parseOneKey("Bytes(123)", facade.byteInstance(), "123");
  }

  @Test
  public void parseGuidKey() throws Exception {
    parseOneKey("/Guids(guid'1225c695-cfb8-4ebb-aaaa-80da344efa6a')", facade.guidInstance(), "1225c695-cfb8-4ebb-aaaa-80da344efa6a");
  }

  @Test
  public void parseTimeKey() throws Exception {
    parseOneKey("/Times(time'P120D')", facade.timeInstance(), "P120D");
  }

  @Test
  public void parseDatetimeKey() throws Exception {
    parseOneKey("/DateTimes(datetime'2009-12-26T21%3A23%3A38')", facade.dateTimeInstance(), "2009-12-26T21:23:38");
    parseOneKey("/DateTimes(datetime'2009-12-26T21%3A23%3A38Z')", facade.dateTimeInstance(), "2009-12-26T21:23:38Z");
  }

  @Test
  public void parseDatetimeOffsetKey() throws Exception {
    parseOneKey("/DateTimeOffsets(datetimeoffset'2009-12-26T21%3A23%3A38Z')", facade.dateTimeOffsetInstance(), "2009-12-26T21:23:38Z");
    parseOneKey("/DateTimeOffsets(datetimeoffset'2002-10-10T12%3A00%3A00-05%3A00')", facade.dateTimeOffsetInstance(), "2002-10-10T12:00:00-05:00");
  }

  @Test
  public void parseBooleanKey() throws Exception {
    parseOneKey("/Booleans(true)", facade.booleanInstance(), "true");
    parseOneKey("/Booleans(false)", facade.booleanInstance(), "false");
    parseOneKey("/Booleans(1)", facade.booleanInstance(), "1");
    parseOneKey("/Booleans(0)", facade.booleanInstance(), "0");
  }

  @Test
  public void parseSByteKey() throws Exception {
    parseOneKey("/SBytes(-123)", facade.sByteInstance(), "-123");
    parseOneKey("/SBytes(12)", facade.sByteInstance(), "12");
  }

  @Test
  public void parseBinaryKey() throws Exception {
    parseOneKey("/Binaries(X'Fa12aAA1')", facade.binaryInstance(), "+hKqoQ==");
    parseOneKey("/Binaries(binary'FA12AAA1')", facade.binaryInstance(), "+hKqoQ==");
  }

  @Test
  public void parseKeyWithWrongContent() throws Exception {
    parseWrongUri("/Binaries(binary'abcde')");
    parseWrongUri("Strings(')");
    parseWrongUri("Strings('a)");
    parseWrongUri("Times(wrongprefix'PT1H2M3S')");
    parseWrongUri("/Int32s(32i)");
    parseWrongUri("Int32s(12345678901234567890)");
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
    UriParserResultImpl result = parse("EmployeeSearch");
    assertEquals("EmployeeSearch", result.getFunctionImport().getName());
    assertEquals(EdmTypeKind.ENTITY, result.getTargetType().getKind());
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

    result = parse("ManagerPhoto?Id='1'");
    assertEquals("ManagerPhoto", result.getFunctionImport().getName());
    assertEquals(UriType.URI14, result.getUriType());

    result = parse("OldestEmployee");
    assertEquals("OldestEmployee", result.getFunctionImport().getName());
    assertEquals(UriType.URI10, result.getUriType());
  }

  @Test
  public void parseFunctionImportParameters() throws Exception {
    UriParserResultImpl result = parse("EmployeeSearch?q='Hugo'&notaparameter=2");
    assertEquals("EmployeeSearch", result.getFunctionImport().getName());
    assertEquals(1, result.getFunctionImportParameters().size());
    assertEquals(facade.stringInstance(), result.getFunctionImportParameters().get("q").getType());
    assertEquals("Hugo", result.getFunctionImportParameters().get("q").getLiteral());
  }

  @Test
  public void parseWrongFunctionImports() throws Exception {
    parseWrongUri("EmployeeSearch?q=42");
    parseWrongUri("AllLocations/$value");
    parseWrongUri("MaximalAge()");
    parseWrongUri("MaximalAge/somethingwrong");
    parseWrongUri("ManagerPhoto");
    parseWrongUri("ManagerPhoto?Id='");
  }

  @Test
  public void parseSystemQueryOptions() throws Exception {
    UriParserResultImpl result = parse("Employees?$format=json&$inlinecount=allpages&$skiptoken=abc&$skip=2&$top=1");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.JSON, result.getFormat());
    assertEquals(InlineCount.ALLPAGES, result.getInlineCount());
    assertEquals("abc", result.getSkipToken());
    assertEquals(2, result.getSkip());
    assertEquals(1, result.getTop().intValue());

    result = parse("Employees?$format=atom&$inlinecount=none&$top=0");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.ATOM, result.getFormat());
    assertEquals(InlineCount.NONE, result.getInlineCount());
    assertEquals(0, result.getTop().intValue());

    result = parse("Employees?$format=json&$inlinecount=none");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.JSON, result.getFormat());
    assertEquals(InlineCount.NONE, result.getInlineCount());

    result = parse("Employees?$format=atom");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.ATOM, result.getFormat());

    result = parse("Employees?$format=xml");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.XML, result.getFormat());
    assertNull(result.getTop());

    result = parse("Employees?$format=xml&$format=json");
    assertEquals(Format.JSON, result.getFormat());

    result = parse("Employees?$format=custom");
    assertNull(result.getFormat());
    assertEquals("custom", result.getCustomFormat());

    result = parse("/Employees('1')/Location/Country?$format=json");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI4, result.getUriType());
    assertEquals(Format.JSON, result.getFormat());

    result = parse("/Employees('1')/EmployeeName?$format=json");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI5, result.getUriType());
    assertEquals(Format.JSON, result.getFormat());

    result = parse("Employees?$filter=Age%20gt%2020&$orderby=EmployeeName%20desc");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertNotNull(result.getFilter());
    assertNotNull(result.getOrderBy());
  }

  @Test
  public void parseWrongSystemQueryOptions() throws Exception {
    parseWrongUri("Employees??");
    parseWrongUri("Employees?$inlinecount=no");
    parseWrongUri("Employees?&$skiptoken==");
    parseWrongUri("Employees?$skip=-1");
    parseWrongUri("Employees?$skip='a'");
    parseWrongUri("Employees?$top=-1");
    parseWrongUri("Employees?$top=12345678901234567890");
    parseWrongUri("Employees?$somethingwrong");
    parseWrongUri("Employees?$somethingwrong=");
    parseWrongUri("Employees?$somethingwrong=adjaodjai");
    parseWrongUri("Employees?$formatformat=xml");
    parseWrongUri("Employees?$Format=atom");
  }

  @Test
  public void parseWrongSystemQueryOptionInitialValues() throws Exception {
    parseWrongUri("Employees?$expand=");
    parseWrongUri("Employees?$filter=");
    parseWrongUri("Employees?$orderby=");
    parseWrongUri("Employees?$format=");
    parseWrongUri("Employees?$skip=");
    parseWrongUri("Employees?$top=");
    parseWrongUri("Employees?$skiptoken=");
    parseWrongUri("Employees?$inlinecount=");
    parseWrongUri("Employees?$select=");

    parseWrongUri("Employees?$expand");
    parseWrongUri("Employees?$filter");
    parseWrongUri("Employees?$orderby");
    parseWrongUri("Employees?$format");
    parseWrongUri("Employees?$skip");
    parseWrongUri("Employees?$top");
    parseWrongUri("Employees?$skiptoken");
    parseWrongUri("Employees?$inlinecount");
    parseWrongUri("Employees?$select");
  }

  @Test
  public void parseCompatibleSystemQueryOptions() throws Exception {
    UriParserResultImpl result = parse("Employees?$format=json&$inlinecount=allpages&$skiptoken=abc&$skip=2&$top=1");
    assertEquals("Employees", result.getEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(Format.JSON, result.getFormat());
    assertEquals(InlineCount.ALLPAGES, result.getInlineCount());
    assertEquals("abc", result.getSkipToken());
    assertEquals(2, result.getSkip());
    assertEquals(1, result.getTop().intValue());
  }

  @Test
  public void parseInCompatibleSystemQueryOptions() throws Exception {
    parseWrongUri("$metadata?$top=1");
    parseWrongUri("Employees('1')?$format=json&$inlinecount=allpages&$skiptoken=abc&$skip=2&$top=1");
    parseWrongUri("/Employees('1')/Location/Country/$value?$format=json");
    parseWrongUri("/Employees('1')/Location/Country/$value?$skip=2");
    parseWrongUri("/Employees('1')/EmployeeName/$value?$format=json");
    parseWrongUri("/Employees('1')/EmployeeName/$value?$skip=2");
  }

  @Test
  public void parsePossibleQueryOptions() throws Exception {
    UriParserResultImpl result = parse("EmployeeSearch?q='a'&sap-client=100&sap-ds-debug=true");
    assertEquals(2, result.getCustomQueryOptions().size());
    assertEquals("100", result.getCustomQueryOptions().get("sap-client"));
    assertEquals("true", result.getCustomQueryOptions().get("sap-ds-debug"));
  }

  @Test
  public void parseSystemQueryOptionSelectSingle() throws Exception {
    UriParserResultImpl result = parse("Employees?$select=EmployeeName");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(1, result.getSelect().size());
    assertEquals("EmployeeName", result.getSelect().get(0).getProperty().getName());

    result = parse("Employees?$select=*");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(1, result.getSelect().size());
    assertTrue(result.getSelect().get(0).isStar());

    result = parse("Employees?$select=Location");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(1, result.getSelect().size());
    assertEquals("Location", result.getSelect().get(0).getProperty().getName());

    result = parse("Employees?$select=ne_Manager");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(1, result.getSelect().size());
    assertEquals(1, result.getSelect().get(0).getNavigationPropertySegments().size());
    assertEquals("Managers", result.getSelect().get(0).getNavigationPropertySegments().get(0).getTargetEntitySet().getName());

    result = parse("Teams?$select=nt_Employees/ne_Manager/*");
    assertEquals(1, result.getSelect().size());
    assertEquals(2, result.getSelect().get(0).getNavigationPropertySegments().size());
    assertTrue(result.getSelect().get(0).isStar());
  }

  @Test
  public void parseSystemQueryOptionSelectMultiple() throws Exception {
    UriParserResultImpl result = parse("Employees?$select=EmployeeName,Location");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(2, result.getSelect().size());
    assertEquals("EmployeeName", result.getSelect().get(0).getProperty().getName());
    assertEquals("Location", result.getSelect().get(1).getProperty().getName());

    result = parse("Employees?$select=%20ne_Manager,%20EmployeeName,%20Location");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(3, result.getSelect().size());
    assertEquals("EmployeeName", result.getSelect().get(1).getProperty().getName());
    assertEquals("Location", result.getSelect().get(2).getProperty().getName());
    assertEquals(1, result.getSelect().get(0).getNavigationPropertySegments().size());
    assertEquals("Managers", result.getSelect().get(0).getNavigationPropertySegments().get(0).getTargetEntitySet().getName());

    result = parse("Managers('1')?$select=nm_Employees/EmployeeName,nm_Employees/Location");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());
    assertEquals(2, result.getSelect().size());
    assertEquals("EmployeeName", result.getSelect().get(0).getProperty().getName());
    assertEquals("Location", result.getSelect().get(1).getProperty().getName());
    assertEquals(1, result.getSelect().get(0).getNavigationPropertySegments().size());
    assertEquals("Employees", result.getSelect().get(0).getNavigationPropertySegments().get(0).getTargetEntitySet().getName());

  }

  @Test
  public void parseSystemQueryOptionSelectNegative() throws Exception {
    parseWrongUri("Employees?$select=somethingwrong");
    parseWrongUri("Employees?$select=*/Somethingwrong");
    parseWrongUri("Employees?$select=EmployeeName/*");
    parseWrongUri("Employees?$select=,EmployeeName");
    parseWrongUri("Employees?$select=EmployeeName,");
    parseWrongUri("Employees?$select=EmployeeName,,Location");
    parseWrongUri("Employees?$select=*EmployeeName");
    parseWrongUri("Employees?$select=EmployeeName*");
    parseWrongUri("Employees?$select=/EmployeeName");
    parseWrongUri("Employees?$select=EmployeeName/");
    parseWrongUri("Teams('1')?$select=nt_Employees/Id");
    parseWrongUri("Teams('1')?$select=nt_Employees//*");
  }

  @Test
  public void parseSystemQueryOptionExpand() throws Exception {
    UriParserResultImpl result = parse("Managers('1')?$expand=nm_Employees");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());
    assertEquals(1, result.getExpand().size());
    assertEquals(1, result.getExpand().get(0).size());
    assertEquals("Employees", result.getExpand().get(0).get(0).getTargetEntitySet().getName());
    assertEquals(result.getTargetEntitySet().getEntityType().getProperty("nm_Employees"), result.getExpand().get(0).get(0).getNavigationProperty());
  }

  @Test
  public void parseSystemQueryOptionExpandWrong() throws Exception {
    parseWrongUri("Managers('1')?$expand=,nm_Employees");
    parseWrongUri("Managers('1')?$expand=nm_Employees,");
    parseWrongUri("Managers('1')?$expand=nm_Employees,,");
    parseWrongUri("Managers('1')?$expand=nm_Employees,,nm_Employees");
    parseWrongUri("Managers('1')?$expand=nm_Employees, somethingwrong");
    parseWrongUri("Managers('1')?$expand=/nm_Employees");
    parseWrongUri("Managers('1')?$expand=nm_Employees/");
    parseWrongUri("Managers('1')?$expand=nm_Employees//");
    parseWrongUri("Managers('1')?$expand=somethingwrong");
    parseWrongUri("Managers('1')?$expand=nm_Employees/EmployeeName");
    parseWrongUri("Managers('1')?$expand=nm_Employees/somethingwrong");
    parseWrongUri("Managers('1')?$expand=nm_Employees/*");
    parseWrongUri("Managers('1')?$expand=nm_Employees/*,somethingwrong");
    parseWrongUri("Managers('1')?$expand=nm_Employees/*,some()");
    parseWrongUri("Managers('1')?$expand=nm_Employees/(...)");
    parseWrongUri("Teams('1')?$expand=nt_Employees//ne_Manager");
  }

}