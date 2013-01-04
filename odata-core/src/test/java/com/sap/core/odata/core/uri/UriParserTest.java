package com.sap.core.odata.core.uri;

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

import com.sap.core.odata.api.commons.InlineCount;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.edm.EdmTypeKind;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.uri.PathSegment;
import com.sap.core.odata.api.uri.UriNotMatchingException;
import com.sap.core.odata.api.uri.UriInfo;
import com.sap.core.odata.api.uri.UriSyntaxException;
import com.sap.core.odata.core.ODataPathSegmentImpl;
import com.sap.core.odata.core.enums.ContentType;
import com.sap.core.odata.core.uri.UriParserImpl;
import com.sap.core.odata.core.uri.UriInfoImpl;
import com.sap.core.odata.core.uri.UriType;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class UriParserTest {

  static {
    DOMConfigurator.configureAndWatch("log4j.xml");
  }

  private static Edm edm;

  @BeforeClass
  public static void getEdm() throws ODataException, EdmException {
    edm = MockFacade.getMockEdm();
  }

  /**
    * Parse the URI part after an OData service root, given as string.
    * Query parameters can be included.
    * @param uri  the URI part
    * @return a {@link UriInfoImpl} instance containing the parsed information
    */
  private UriInfoImpl parse(final String uri) throws UriSyntaxException, UriNotMatchingException, EdmException {
    final String[] path = uri.split("\\?", -1);
    if (path.length > 2)
      throw new UriSyntaxException(UriSyntaxException.URISYNTAX);

    final List<PathSegment> pathSegments = getPathSegments(path[0]);
    Map<String, String> queryParameters;
    if (path.length == 2) {
      queryParameters = getQueryParameters(unescape(path[1]));
    } else {
      queryParameters = new HashMap<String, String>();
    }

    UriInfo result = new UriParserImpl(edm).parse(pathSegments, queryParameters);

    return (UriInfoImpl) result;
  }

  private List<PathSegment> getPathSegments(final String uri) throws UriSyntaxException {
    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    for (final String segment : uri.split("/", -1)) {
      final String unescapedSegment = unescape(segment);
      PathSegment oDataSegment = new ODataPathSegmentImpl(unescapedSegment, null);
      pathSegments.add(oDataSegment);
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

  private String unescape(final String s) throws UriSyntaxException {
    try {
      return new URI(s).getPath();
    } catch (URISyntaxException e) {
      throw new UriSyntaxException(UriSyntaxException.NOTEXT);
    }
  }

  private void parseWrongUri(final String uri, MessageReference exceptionContext) {
    try {
      parse(uri);
      fail("Expected UriParserException not thrown");
    } catch (ODataMessageException e) {
      assertNotNull(e);
      assertEquals(exceptionContext.getKey(), e.getMessageReference().getKey());
    }
  }

  @Test
  public void copyPathSegmentsTest() throws Exception {
    List<PathSegment> pathSegments = new ArrayList<PathSegment>();
    pathSegments.add(new ODataPathSegmentImpl("$metadata", null));
    UriInfo result = new UriParserImpl(edm).parse(pathSegments, Collections.<String, String> emptyMap());
    assertNotNull(result);
    assertEquals(1, pathSegments.size());
    assertEquals("$metadata", pathSegments.get(0).getPath());

  }

  @Test
  public void parseNonsens() throws Exception {
    parseWrongUri("/bla", UriNotMatchingException.NOTFOUND);
  }

  @Test
  public void parseServiceDocument() throws Exception {
    UriInfoImpl result = parse("/");
    assertEquals(UriType.URI0, result.getUriType());

    result = parse("");
    assertEquals(UriType.URI0, result.getUriType());

    result = (UriInfoImpl) new UriParserImpl(edm).parse(Collections.<PathSegment> emptyList(), Collections.<String, String> emptyMap());
    assertEquals(UriType.URI0, result.getUriType());
  }

  @Test
  public void parseMetadata() throws Exception {
    UriInfoImpl result = parse("/$metadata");
    assertEquals(UriType.URI8, result.getUriType());
  }

  @Test
  public void parseMetadataError() throws Exception {
    parseWrongUri("/$metadata/somethingwrong", UriSyntaxException.MUSTBELASTSEGMENT);
  }

  @Test
  public void parseBatch() throws Exception {
    UriInfoImpl result = parse("/$batch");
    assertEquals(UriType.URI9, result.getUriType());
  }

  @Test
  public void parseBatchError() throws Exception {
    parseWrongUri("/$batch/somethingwrong", UriSyntaxException.MUSTBELASTSEGMENT);
  }

  @Test
  public void parseSomethingEntitySet() throws Exception {
    parseWrongUri("/somethingwrong", UriNotMatchingException.NOTFOUND);
  }

  @Test
  public void parseContainerWithoutEntitySet() throws Exception {
    parseWrongUri("Container1.", UriNotMatchingException.MATCHPROBLEM);
  }

  @Test
  public void parseEmployeesEntitySet() throws Exception {
    UriInfoImpl result = parse("/Employees");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseEmployeesEntitySetParenthesesCount() throws Exception {
    UriInfoImpl result = parse("/Employees()/$count");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI15, result.getUriType());
    assertTrue(result.isCount());
  }

  @Test
  public void parseEmployeesEntitySetParenthesesCountNotLast() throws Exception {
    parseWrongUri("/Employees()/$count/somethingwrong", UriSyntaxException.MUSTBELASTSEGMENT);
  }

  @Test
  public void parseEmployeesEntitySetParentheses() throws Exception {
    UriInfoImpl result = parse("/Employees()");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseWrongEntities() throws Exception {
    parseWrongUri("//", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("/Employ ees('1')", UriSyntaxException.NOTEXT);
    parseWrongUri("/Employees()/somethingwrong", UriSyntaxException.ENTITYSETINSTEADOFENTITY);
    parseWrongUri("/Employees/somethingwrong", UriSyntaxException.ENTITYSETINSTEADOFENTITY);
    parseWrongUri("Employees/", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("//Employees", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Employees//", UriSyntaxException.EMPTYSEGMENT);
  }

  @Test
  public void parseEmployeesEntityWithKey() throws Exception {
    UriInfoImpl result = parse("/Employees('1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntity() throws Exception {
    UriInfoImpl result = parse("/Employees('1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithExplicitKey() throws Exception {
    UriInfoImpl result = parse("/Employees(EmployeeId='1')");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithKeyValue() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/$value");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.getStartEntitySet().getEntityType().hasStream());
    assertEquals(UriType.URI17, result.getUriType());
    assertTrue(result.isValue());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesEntityWithKeyCount() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI16, result.getUriType());
    assertTrue(result.isCount());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseEmployeesSimpleProperty() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/EmployeeName");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI5, result.getUriType());
    assertEquals("EmployeeName", result.getPropertyPath().get(0).getName());
  }

  @Test
  public void parseEmployeesSimplePropertyValue() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/EmployeeName/$value");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI5, result.getUriType());
    assertEquals("EmployeeName", result.getPropertyPath().get(0).getName());
    assertTrue(result.isValue());
  }

  @Test
  public void parseEmployeesComplexProperty() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/Location");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI3, result.getUriType());
    assertEquals("Location", result.getPropertyPath().get(0).getName());
  }

  @Test
  public void parseEmployeesComplexPropertyWithEntity() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/Location/Country");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI4, result.getUriType());
    assertEquals("Location", result.getPropertyPath().get(0).getName());
    assertEquals("Country", result.getPropertyPath().get(1).getName());
  }

  @Test
  public void parseEmployeesComplexPropertyWithEntityValue() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/Location/Country/$value");
    assertNull(result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI4, result.getUriType());
    assertEquals("Location", result.getPropertyPath().get(0).getName());
    assertEquals("Country", result.getPropertyPath().get(1).getName());
    assertTrue(result.isValue());
  }

  @Test
  public void simplePropertyWrong() throws Exception {
    parseWrongUri("/Employees('1')/EmployeeName(1)", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("/Employees('1')/EmployeeName()", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("/Employees('1')/EmployeeName/something", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("/Employees('1')/EmployeeName/$value/something", UriSyntaxException.MUSTBELASTSEGMENT);
  }

  @Test
  public void complexPropertyWrong() throws Exception {
    parseWrongUri("/Employees('1')/Location(1)", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("/Employees('1')/Location/somethingwrong", UriNotMatchingException.PROPERTYNOTFOUND);
  }

  @Test
  public void EmployeesNoProperty() throws Exception {
    parseWrongUri("/Employees('1')/somethingwrong", UriNotMatchingException.PROPERTYNOTFOUND);
  }

  @Test
  public void parseNavigationPropertyWithEntityResult() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/ne_Manager");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntitySetResult() throws Exception {
    UriInfoImpl result = parse("/Managers('1')/nm_Employees");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntitySetResultParenthesis() throws Exception {
    UriInfoImpl result = parse("/Managers('1')/nm_Employees()");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEntityResultWithKey() throws Exception {
    UriInfoImpl result = parse("/Managers('1')/nm_Employees('1')");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI6A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksOne() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/$links/ne_Manager");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertEquals(UriType.URI7A, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksMany() throws Exception {
    UriInfoImpl result = parse("/Managers('1')/$links/nm_Employees");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertEquals(UriType.URI7B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithManagersCount() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/ne_Manager/$count");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI16, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEmployeesCount() throws Exception {
    UriInfoImpl result = parse("/Managers('1')/nm_Employees/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI15, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithEmployeeCount() throws Exception {
    UriInfoImpl result = parse("Managers('1')/nm_Employees('1')/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isCount());
    assertEquals(UriType.URI16, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksCountMany() throws Exception {
    UriInfoImpl result = parse("/Managers('1')/$links/nm_Employees/$count");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertTrue(result.isLinks());
    assertTrue(result.isCount());
    assertEquals(UriType.URI50B, result.getUriType());
  }

  @Test
  public void parseNavigationPropertyWithLinksCountOne() throws Exception {
    UriInfoImpl result = parse("/Employees('1')/$links/ne_Manager/$count");
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
    parseWrongUri("Employees('1')//ne_Manager", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Employees('1')/ne_Manager()", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Employees('1')/ne_Manager('1')", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Employees('1')/$links", UriSyntaxException.MUSTNOTBELASTSEGMENT);
    parseWrongUri("Employees('1')/$links/ne_Manager('1')", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Employees('1')/$links/ne_Manager()", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Employees('1')/$links/ne_Manager/somethingwrong", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Employees('1')/ne_Manager/$count/somethingwrong", UriSyntaxException.MUSTBELASTSEGMENT);
    parseWrongUri("Employees('1')/$links/ne_Manager/$count/somethingwrong", UriSyntaxException.MUSTBELASTSEGMENT);
    parseWrongUri("Employees('1')/ne_Manager/$value", UriSyntaxException.NOMEDIARESOURCE);
    parseWrongUri("Managers('1')/nm_Employees('1')/$value/somethingwrong", UriSyntaxException.MUSTBELASTSEGMENT);
    parseWrongUri("Managers('1')/nm_Employees/$links", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Managers('1')/nm_Employees/$links/Manager", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Managers('1')/nm_Employees/somethingwrong", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Employees('1')/$links/somethingwrong", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Employees('1')/$links/EmployeeName", UriSyntaxException.NONAVIGATIONPROPERTY);
    parseWrongUri("Employees('1')/$links/$links/ne_Manager", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Managers('1')/nm_Employee/", UriSyntaxException.EMPTYSEGMENT);
  }

  @Test
  public void navigationPathWrongMatch() throws Exception {
    parseWrongUri("/Employees('1')/(somethingwrong(", UriNotMatchingException.MATCHPROBLEM);

  }

  @Test
  public void navigationSegmentWrongMatch() throws Exception {
    parseWrongUri("/Employees('1')/$links/(somethingwrong(", UriNotMatchingException.MATCHPROBLEM);

  }

  @Test
  public void parseTeamsEntityWithIntKeyValue() throws Exception {
    parseWrongUri("/Teams(1)/$value", UriSyntaxException.INCOMPATIBLELITERAL);
  }

  @Test
  public void parseWrongKey() throws Exception {
    parseWrongUri("Employees(,'1')", UriSyntaxException.INVALIDKEYPREDICATE);
    parseWrongUri("Employees('1',)", UriSyntaxException.INVALIDKEYPREDICATE);
    parseWrongUri("Employees(EmployeeName='1')", UriSyntaxException.INVALIDKEYPREDICATE);
    parseWrongUri("Employees(EmployeeId='1',EmployeeId='1')", UriSyntaxException.DUPLICATEKEYNAMES);
    parseWrongUri("/Employees(EmployeeId='1',somethingwrong=abc)", UriSyntaxException.INVALIDKEYPREDICATE);
    parseWrongUri("/Employees(somethingwrong=1)", UriSyntaxException.INVALIDKEYPREDICATE);
    parseWrongUri("/Container2.Photos(Id=1,,Type='abc')", UriSyntaxException.INVALIDKEYPREDICATE);
    parseWrongUri("/Container2.Photos(Id=1;Type='abc')", UriSyntaxException.INVALIDKEYPREDICATE);
    parseWrongUri("Container2.Photos(Id=1,'abc')", UriSyntaxException.MISSINGKEYPREDICATENAME);
    parseWrongUri("Container2.Photos(Id=1)", UriSyntaxException.INVALIDKEYPREDICATE);
  }

  @Test
  public void parsePhotoEntityWithExplicitKeySet() throws Exception {
    UriInfoImpl result = parse("/Container2.Photos(Id=1,Type='abc')");
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
    UriInfoImpl result = parse("/Container1.Employees");
    assertEquals("Container1", result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseContainerEmployeesEntitySetParenthese() throws Exception {
    UriInfoImpl result = parse("/Container1.Employees()");
    assertEquals("Container1", result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
  }

  @Test
  public void parseContainerEmployeesEntityWithKey() throws Exception {
    UriInfoImpl result = parse("/Container1.Employees('1')");
    assertEquals("Container1", result.getEntityContainer().getName());
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());

    assertEquals(1, result.getKeyPredicates().size());
    assertEquals("1", result.getKeyPredicates().get(0).getLiteral());
    assertEquals("EmployeeId", result.getKeyPredicates().get(0).getProperty().getName());
  }

  @Test
  public void parseNonexistentContainer() throws Exception {
    parseWrongUri("/somethingwrong.Employees()", UriNotMatchingException.CONTAINERNOTFOUND);
  }

  @Test
  public void parseInvalidSegment() throws Exception {
    parseWrongUri("/.somethingwrong", UriNotMatchingException.MATCHPROBLEM);
  }

  @Test
  public void parseFunctionImports() throws Exception {
    UriInfoImpl result = parse("EmployeeSearch");
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
    UriInfoImpl result = parse("EmployeeSearch?q='Hugo'&notaparameter=2");
    assertEquals("EmployeeSearch", result.getFunctionImport().getName());
    assertEquals(1, result.getFunctionImportParameters().size());
    assertEquals(EdmSimpleTypeKind.String.getEdmSimpleTypeInstance(), result.getFunctionImportParameters().get("q").getType());
    assertEquals("Hugo", result.getFunctionImportParameters().get("q").getLiteral());
  }

  @Test
  public void parseWrongFunctionImports() throws Exception {
    parseWrongUri("EmployeeSearch?q=42", UriSyntaxException.INCOMPATIBLELITERAL);
    parseWrongUri("AllLocations/$value", UriSyntaxException.MUSTBELASTSEGMENT);
    parseWrongUri("MaximalAge()", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("MaximalAge/somethingwrong", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("ManagerPhoto", UriSyntaxException.MISSINGPARAMETER);
    parseWrongUri("ManagerPhoto?Id='", UriSyntaxException.UNKNOWNLITERAL);
  }

  @Test
  public void parseSystemQueryOptions() throws Exception {
    UriInfoImpl result = parse("Employees?$format=json&$inlinecount=allpages&$skiptoken=abc&$skip=2&$top=1");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(ContentType.APPLICATION_JSON.toContentTypeString(), result.getContentType());
    assertEquals(InlineCount.ALLPAGES, result.getInlineCount());
    assertEquals("abc", result.getSkipToken());
    assertEquals(2, result.getSkip().intValue());
    assertEquals(1, result.getTop().intValue());

    result = parse("Employees?$format=atom&$inlinecount=none&$skip=0&$top=0");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(ContentType.APPLICATION_ATOM_XML.toContentTypeString(), result.getContentType());
    assertEquals(InlineCount.NONE, result.getInlineCount());
    assertEquals(0, result.getSkip().intValue());
    assertEquals(0, result.getTop().intValue());

    result = parse("Employees?$format=json&$inlinecount=none");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(ContentType.APPLICATION_JSON.toContentTypeString(), result.getContentType());
    assertEquals(InlineCount.NONE, result.getInlineCount());
    assertNull(result.getSkip());
    assertNull(result.getTop());

    result = parse("Employees?$format=atom");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(ContentType.APPLICATION_ATOM_XML.toContentTypeString(), result.getContentType());

    result = parse("Employees?$format=xml");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(ContentType.APPLICATION_XML.toContentTypeString(), result.getContentType());
    assertNull(result.getTop());

    result = parse("Employees?$format=xml&$format=json");
    assertEquals(ContentType.APPLICATION_JSON.toContentTypeString(), result.getContentType());

    result = parse("Employees?$format=custom/*");
    assertNotNull(result.getContentType());
    assertEquals("custom/*", result.getContentType().toString());

    result = parse("/Employees('1')/Location/Country?$format=json");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI4, result.getUriType());
    assertEquals(ContentType.APPLICATION_JSON.toContentTypeString(), result.getContentType());

    result = parse("/Employees('1')/EmployeeName?$format=json");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI5, result.getUriType());
    assertEquals(ContentType.APPLICATION_JSON.toContentTypeString(), result.getContentType());

    result = parse("Employees?$filter=Age%20gt%2020&$orderby=EmployeeName%20desc");
    assertEquals("Employees", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertNotNull(result.getFilter());
    assertNotNull(result.getOrderBy());
  }

  @Test
  public void parseWrongSystemQueryOptions() throws Exception {
    parseWrongUri("Employees??", UriSyntaxException.URISYNTAX);
    parseWrongUri("Employees?$inlinecount=no", UriSyntaxException.INVALIDVALUE);
    parseWrongUri("Employees?&$skiptoken==", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$skip=-1", UriSyntaxException.INVALIDNEGATIVEVALUE);
    parseWrongUri("Employees?$skip='a'", UriSyntaxException.INVALIDVALUE);
    parseWrongUri("Employees?$top=-1", UriSyntaxException.INVALIDNEGATIVEVALUE);
    parseWrongUri("Employees?$top=12345678901234567890", UriSyntaxException.INVALIDVALUE);
    parseWrongUri("Employees?$somethingwrong", UriSyntaxException.INVALIDSYSTEMQUERYOPTION);
    parseWrongUri("Employees?$somethingwrong=", UriSyntaxException.INVALIDSYSTEMQUERYOPTION);
    parseWrongUri("Employees?$somethingwrong=adjaodjai", UriSyntaxException.INVALIDSYSTEMQUERYOPTION);
    parseWrongUri("Employees?$formatformat=xml", UriSyntaxException.INVALIDSYSTEMQUERYOPTION);
    parseWrongUri("Employees?$Format=atom", UriSyntaxException.INVALIDSYSTEMQUERYOPTION);
  }

  @Test
  public void parseWrongSystemQueryOptionInitialValues() throws Exception {
    parseWrongUri("Employees?$expand=", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$filter=", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$orderby=", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$format=", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$skip=", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$top=", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$skiptoken=", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$inlinecount=", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$select=", UriSyntaxException.INVALIDNULLVALUE);

    parseWrongUri("Employees?$expand", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$filter", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$orderby", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$format", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$skip", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$top", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$skiptoken", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$inlinecount", UriSyntaxException.INVALIDNULLVALUE);
    parseWrongUri("Employees?$select", UriSyntaxException.INVALIDNULLVALUE);
  }

  @Test
  public void parseCompatibleSystemQueryOptions() throws Exception {
    UriInfoImpl result = parse("Employees?$format=json&$inlinecount=allpages&$skiptoken=abc&$skip=2&$top=1");
    assertEquals("Employees", result.getStartEntitySet().getName());
    assertEquals(UriType.URI1, result.getUriType());
    assertEquals(ContentType.APPLICATION_JSON.toContentTypeString(), result.getContentType());
    assertEquals(InlineCount.ALLPAGES, result.getInlineCount());
    assertEquals("abc", result.getSkipToken());
    assertEquals(2, result.getSkip().intValue());
    assertEquals(1, result.getTop().intValue());
  }

  @Test
  public void parseInCompatibleSystemQueryOptions() throws Exception {
    parseWrongUri("$metadata?$top=1", UriSyntaxException.INCOMPATIBLESYSTEMQUERYOPTION);
    parseWrongUri("Employees('1')?$format=json&$inlinecount=allpages&$skiptoken=abc&$skip=2&$top=1", UriSyntaxException.INCOMPATIBLESYSTEMQUERYOPTION);
    parseWrongUri("/Employees('1')/Location/Country/$value?$format=json", UriSyntaxException.INCOMPATIBLESYSTEMQUERYOPTION);
    parseWrongUri("/Employees('1')/Location/Country/$value?$skip=2", UriSyntaxException.INCOMPATIBLESYSTEMQUERYOPTION);
    parseWrongUri("/Employees('1')/EmployeeName/$value?$format=json", UriSyntaxException.INCOMPATIBLESYSTEMQUERYOPTION);
    parseWrongUri("/Employees('1')/EmployeeName/$value?$skip=2", UriSyntaxException.INCOMPATIBLESYSTEMQUERYOPTION);
  }

  @Test
  public void parsePossibleQueryOptions() throws Exception {
    UriInfoImpl result = parse("EmployeeSearch?q='a'&sap-client=100&sap-ds-debug=true");
    assertEquals(2, result.getCustomQueryOptions().size());
    assertEquals("100", result.getCustomQueryOptions().get("sap-client"));
    assertEquals("true", result.getCustomQueryOptions().get("sap-ds-debug"));
  }

  @Test
  public void parseSystemQueryOptionSelectSingle() throws Exception {
    UriInfoImpl result = parse("Employees?$select=EmployeeName");
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
    UriInfoImpl result = parse("Employees?$select=EmployeeName,Location");
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
    parseWrongUri("Employees?$select=somethingwrong", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Employees?$select=*/Somethingwrong", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Employees?$select=EmployeeName/*", UriSyntaxException.INVALIDSEGMENT);
    parseWrongUri("Employees?$select=,EmployeeName", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Employees?$select=EmployeeName,", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Employees?$select=EmployeeName,,Location", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Employees?$select=*EmployeeName", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Employees?$select=EmployeeName*", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Employees?$select=/EmployeeName", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Employees?$select=EmployeeName/", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Teams('1')?$select=nt_Employees/Id", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Teams('1')?$select=nt_Employees//*", UriSyntaxException.EMPTYSEGMENT);
  }

  @Test
  public void parseSystemQueryOptionExpand() throws Exception {
    UriInfoImpl result = parse("Managers('1')?$expand=nm_Employees");
    assertEquals("Managers", result.getTargetEntitySet().getName());
    assertEquals(UriType.URI2, result.getUriType());
    assertEquals(1, result.getExpand().size());
    assertEquals(1, result.getExpand().get(0).size());
    assertEquals("Employees", result.getExpand().get(0).get(0).getTargetEntitySet().getName());
    assertEquals(result.getTargetEntitySet().getEntityType().getProperty("nm_Employees"), result.getExpand().get(0).get(0).getNavigationProperty());
  }

  @Test
  public void parseSystemQueryOptionExpandWrong() throws Exception {
    parseWrongUri("Managers('1')?$expand=,nm_Employees", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Managers('1')?$expand=nm_Employees,", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Managers('1')?$expand=nm_Employees,,", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Managers('1')?$expand=nm_Employees,,nm_Employees", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Managers('1')?$expand=nm_Employees, somethingwrong", UriSyntaxException.NOTEXT);
    parseWrongUri("Managers('1')?$expand=/nm_Employees", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Managers('1')?$expand=nm_Employees/", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Managers('1')?$expand=nm_Employees//", UriSyntaxException.EMPTYSEGMENT);
    parseWrongUri("Managers('1')?$expand=somethingwrong", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Managers('1')?$expand=nm_Employees/EmployeeName", UriSyntaxException.NONAVIGATIONPROPERTY);
    parseWrongUri("Managers('1')?$expand=nm_Employees/somethingwrong", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Managers('1')?$expand=nm_Employees/*", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Managers('1')?$expand=nm_Employees/*,somethingwrong", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Managers('1')?$expand=nm_Employees/*,some()", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Managers('1')?$expand=nm_Employees/(...)", UriNotMatchingException.PROPERTYNOTFOUND);
    parseWrongUri("Teams('1')?$expand=nt_Employees//ne_Manager", UriSyntaxException.EMPTYSEGMENT);
  }

}