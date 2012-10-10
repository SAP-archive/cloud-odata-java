package org.odata4j.test.integration.function;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.parsers.ParserConfigurationException;

import org.core4j.Enumerable;
import org.core4j.Predicate1;
import org.custommonkey.xmlunit.NamespaceContext;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.core.OComplexObject;
import org.odata4j.core.OComplexObjects;
import org.odata4j.core.OEntity;
import org.odata4j.core.OObject;
import org.odata4j.core.OSimpleObject;
import org.odata4j.core.OSimpleObjects;
import org.odata4j.edm.EdmSimpleType;
import org.odata4j.edm.EdmType;
import org.odata4j.format.FormatType;
import org.odata4j.producer.resources.DefaultODataProducerProvider;
import org.odata4j.producer.server.ODataServer;
import org.odata4j.test.integration.AbstractRuntimeTest;
import org.odata4j.test.integration.ResponseData;
import org.xml.sax.SAXException;

@RunWith(JUnit4.class)
public class FunctionImportTest extends AbstractRuntimeTest {

  public FunctionImportTest() {
    super(RuntimeFacadeType.JERSEY);
  }

  /*
  public FunctionImportTest(RuntimeFacadeType type) {
    super(type);
  }*/

  private static ArrayList<FormatType> formats;
  static {
    FunctionImportTest.formats = new ArrayList<FormatType>();
    FunctionImportTest.formats.add(FormatType.JSON);
    FunctionImportTest.formats.add(FormatType.ATOM);
  }

  private ODataServer server;

  private final static String endpointUri = "http://localhost:8810/FunctionImportScenario.svc/";

  private FunctionImportProducerMock mockProducer;

  private static class TestCase {

    @Override
    public String toString() {
      return this.parameterName + "(" + this.type.getFullyQualifiedTypeName() + ")";
    }

    public TestCase(String parameterName, String valueLiteral, String valueString, EdmSimpleType<?> type) {
      this.parameterName = parameterName;
      this.valueLiteral = valueLiteral;
      this.valueString = valueString;
      this.type = type;
    }

    String parameterName;
    String valueLiteral;
    String valueString;
    EdmSimpleType<?> type;
  }

  public static ArrayList<TestCase> testCases;
  static {
    FunctionImportTest.testCases = new ArrayList<FunctionImportTest.TestCase>();
    FunctionImportTest.testCases.add(new TestCase("p1", "X'1F'", "0x1f", EdmSimpleType.BINARY));
    FunctionImportTest.testCases.add(new TestCase("p2", "true", "true", EdmSimpleType.BOOLEAN));
    FunctionImportTest.testCases.add(new TestCase("p3", "1", "1", EdmSimpleType.BYTE));
    FunctionImportTest.testCases.add(new TestCase("p4", "datetime'2010-12-12T23:44:57.123'", "2010-12-12T23:44:57.123", EdmSimpleType.DATETIME));
    FunctionImportTest.testCases.add(new TestCase("p5", "22.5m", "22.5", EdmSimpleType.DECIMAL));
    FunctionImportTest.testCases.add(new TestCase("p6", "1d", "1.0", EdmSimpleType.DOUBLE));
    FunctionImportTest.testCases.add(new TestCase("p7", "1f", "1.0", EdmSimpleType.SINGLE));
    FunctionImportTest.testCases.add(new TestCase("p8", "datetimeoffset'2012-12-12T22:07:44.123Z'", "2012-12-12T22:07:44.123Z", EdmSimpleType.DATETIMEOFFSET));
    FunctionImportTest.testCases.add(new TestCase("p9", "guid'11111111-1111-1111-1111-111111111111'", "11111111-1111-1111-1111-111111111111", EdmSimpleType.GUID));
    FunctionImportTest.testCases.add(new TestCase("p10", "1", "1", EdmSimpleType.INT16));
    FunctionImportTest.testCases.add(new TestCase("p11", "1", "1", EdmSimpleType.INT32));
    FunctionImportTest.testCases.add(new TestCase("p12", "1L", "1", EdmSimpleType.INT64));
    FunctionImportTest.testCases.add(new TestCase("p13", "1", "1", EdmSimpleType.SBYTE));
    FunctionImportTest.testCases.add(new TestCase("p14", "'hugo'", "hugo", EdmSimpleType.STRING));
    FunctionImportTest.testCases.add(new TestCase("p15", "time'PT10H30M'", "10:30:00.000", EdmSimpleType.TIME));
  }

  private String formatQuery(FormatType type) {
    String query;

    switch (type) {
    case ATOM:
      query = "$format=atom";
      break;
    case JSON:
      query = "$format=json";
      break;
    default:
      throw new RuntimeException("Unknown Format Type: " + type);
    }

    return query;
  }

  private void initializeXmlUnit() {
    HashMap<String, String> m = new HashMap<String, String>();
    m.put("m", "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata");
    m.put("d", "http://schemas.microsoft.com/ado/2007/08/dataservices");
    m.put("edmx", "http://schemas.microsoft.com/ado/2007/06/edmx");
    m.put("g", "http://www.w3.org/2005/Atom"); // 'g' is a dummy for global namespace

    NamespaceContext ctx = new SimpleNamespaceContext(m);
    XMLUnit.setXpathNamespaceContext(ctx);
    XpathEngine engine = XMLUnit.newXpathEngine();
    engine.setNamespaceContext(ctx);
  }

  @Before
  public void before() {

    this.initializeXmlUnit();

    this.mockProducer = new FunctionImportProducerMock();

    DefaultODataProducerProvider.setInstance(this.mockProducer);
    this.server = this.rtFacade.startODataServer(FunctionImportTest.endpointUri);
  }

  @After
  public void after() {
    if (this.server != null) {
      this.server.stop();
    }
  }

  @Test
  public void testMetaData() throws SAXException, IOException, ParserConfigurationException {
    ResponseData responseData = this.rtFacade.getWebResource(endpointUri + "$metadata/");
    String metadata = responseData.getEntity();
    assertEquals(200, responseData.getStatusCode());
    assertNotNull(metadata);
  }

  @Test
  public void testFunctionReturnStringWithAllParameter() throws XpathException, SAXException, IOException {
    String query = "?p1=X'1F'&p2=true&p3=1&p4=datetime'2010-12-12T23:44:57'&p5=22.5m&p6=1d&p7=1f&p8=datetimeoffset'2012-12-12T22:07:44Z'&p9=guid'11111111-1111-1111-1111-111111111111'&p10=1&p11=1&p12=1L&p13=1&p14='hugo'&p15=time'PT10H30M'";

    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING + query + "&" + this.formatQuery(format));
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnString", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnString/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }

    }
  }

  @Test
  public void testFunctionReturnBoolean() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_BOOLEAN + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnBoolean", resource);
        assertXpathEvaluatesTo(Boolean.toString(FunctionImportProducerMock.BOOLEAN_VALUE), "/d:TestFunctionReturnBoolean/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_BOOLEAN));
        assertTrue(format.toString(), resource.contains(Boolean.toString(FunctionImportProducerMock.BOOLEAN_VALUE)));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnBooleanConsumer() {
    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_BOOLEAN, EdmSimpleType.BOOLEAN, 1,
        new Predicate1<OObject>() {
          @Override
          public boolean apply(OObject t) {
            return ((OSimpleObject) t).getValue().equals(FunctionImportProducerMock.BOOLEAN_VALUE);
          }
        });
  }

  @Test
  public void testFunctionReturnString() throws XpathException, IOException, SAXException {
    for (TestCase testCase : FunctionImportTest.testCases) {

      String query = "?" + testCase.parameterName + "=" + testCase.valueLiteral;

      for (FormatType format : FunctionImportTest.formats) {
        ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING + query + "&" + this.formatQuery(format));
        String resource = responseData.getEntity();

        String msg = format + " | TestCase: " + testCase.toString();

        assertEquals(format.toString(), 200, responseData.getStatusCode());
        assertNotNull(msg, this.mockProducer.getQueryParameter());
        assertTrue(msg, this.mockProducer.getQueryParameter().containsKey(testCase.parameterName));

        assertEquals(msg, testCase.parameterName, this.mockProducer.getQueryParameter().get(testCase.parameterName).getName());
        assertEquals(msg, testCase.type, this.mockProducer.getQueryParameter().get(testCase.parameterName).getType());
        assertEquals(msg, testCase.valueString, OSimpleObjects.getValueDisplayString(this.mockProducer.getQueryParameter().get(testCase.parameterName).getValue()));

        switch (format) {
        case ATOM:
          assertXpathExists("/d:TestFunctionReturnString", resource);
          assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnString/text()", resource);
          break;
        case JSON:
          assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING));
          assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
          break;
        default:
          throw new RuntimeException("Unknown Format Type: " + format);
        }
      }
    }
  }

  protected void testFunctionConsumer(String functionName, EdmType expectedType, int nExpected, Predicate1<OObject> alsoTrue) {
    for (FormatType format : FunctionImportTest.formats) {
      if (format.equals(FormatType.ATOM)) {
        continue;
      } // maybe someday.
      ODataConsumer consumer = rtFacade.createODataConsumer(endpointUri, format, null);
      Enumerable<OObject> objects = consumer.callFunction(functionName).execute();

      assertEquals(nExpected, objects.count());
      for (OObject obj : objects) {
        assertEquals(obj.getType(), expectedType);
        if (null != alsoTrue) {
          assertTrue(alsoTrue.apply(obj));
        }
      }
    }
  }

  @Test
  public void testFunctionReturnStringConsumer() {
    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_STRING, EdmSimpleType.STRING, 1,
        new Predicate1<OObject>() {

          @Override
          public boolean apply(OObject t) {
            return ((OSimpleObject) t).getValue().equals(FunctionImportProducerMock.SOME_TEXT);
          }
        });
  }

  @Test
  public void testFunctionReturnInt16() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_INT16 + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnInt16", resource);
        assertXpathEvaluatesTo(Integer.toString(FunctionImportProducerMock.INT16_VALUE), "/d:TestFunctionReturnInt16/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_INT16));
        assertTrue(format.toString(), resource.contains(Integer.toString(FunctionImportProducerMock.INT16_VALUE)));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnInt16Consumer() {
    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_INT16, EdmSimpleType.INT16, 1,
        new Predicate1<OObject>() {

          @Override
          public boolean apply(OObject t) {
            return ((OSimpleObject) t).getValue().equals(FunctionImportProducerMock.INT16_VALUE);
          }
        });
  }

  @Test
  public void testFunctionReturnStringWithNoQueryParameter() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {

      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());

      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      assertFalse(format.toString(), this.mockProducer.getQueryParameter().containsKey("p0"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p1"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p2"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p3"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p4"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p5"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p6"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p7"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p8"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p9"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p10"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p11"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p12"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p13"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p14"));
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p15"));
      assertFalse(format.toString(), this.mockProducer.getQueryParameter().containsKey("p16"));

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnString", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnString/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }

    }
  }

  @Test
  public void testFunctionReturnEntity() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {

      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_ENTITY + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();
      this.logger.debug(resource);

      assertEquals(format.toString(), 200, responseData.getStatusCode());

      switch (format) {
      case ATOM:
        assertXpathEvaluatesTo("RefScenario.Employee", "/g:entry/g:category/@term", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.EMPLOYEE_NAME, "/g:entry/g:content/m:properties/d:EmployeeName/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.EMPLOYEE_ID, "/g:entry/g:content/m:properties/d:EmployeeId/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.EMPLOYEE_NAME));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.EMPLOYEE_ID));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnEntityConsumer() {

    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_ENTITY, mockProducer.getMetadata().findEdmEntitySet("Employees").getType(), 1,
        new Predicate1<OObject>() {
          @Override
          public boolean apply(OObject t) {
            OEntity e = (OEntity) t;
            return e.getProperty("EmployeeName", String.class).getValue().equals(FunctionImportProducerMock.EMPLOYEE_NAME) &&
                e.getProperty("EmployeeId", String.class).getValue().equals(FunctionImportProducerMock.EMPLOYEE_ID);
          }
        });
  }

  @Test
  public void testFunctionReturnComplexType() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {

      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_COMPLEX_TYPE + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();
      this.logger.debug(resource);

      assertEquals(format.toString(), 200, responseData.getStatusCode());

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnComplexType", resource);
        assertXpathEvaluatesTo("RefScenario.c_Location", "/d:TestFunctionReturnComplexType/@m:type", resource);
        assertXpathEvaluatesTo("RefScenario.c_City", "/d:TestFunctionReturnComplexType/d:City/@m:type", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.POSTAL_CODE, "/d:TestFunctionReturnComplexType/d:City/d:PostalCode/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.CITY, "/d:TestFunctionReturnComplexType/d:City/d:CityName/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.COUNTRY, "/d:TestFunctionReturnComplexType/d:Country/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_COMPLEX_TYPE));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.CITY));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.COUNTRY));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.POSTAL_CODE));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnComplexTypeConsumer() {

    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_COMPLEX_TYPE,
        mockProducer.getMetadata().findEdmComplexType(FunctionImportProducerMock.COMPLEY_TYPE_NAME_LOCATION),
        1,
        new Predicate1<OObject>() {

          @Override
          public boolean apply(OObject t) {
            OComplexObject e = (OComplexObject) t;
            // weird that e.getProperty("City") returns a property list and not a complex object.
            // and furthermore...why is List<OProperty<?>> used...there should be a PropertyBag abstraction no?
            OComplexObject city = OComplexObjects.create(mockProducer.getMetadata().findEdmComplexType(FunctionImportProducerMock.COMPLEY_TYPE_NAME_CITY), e.getProperty("City", List.class).getValue());
            return e.getProperty("Country", String.class).getValue().equals(FunctionImportProducerMock.COUNTRY)
                && city.getProperty("PostalCode", String.class).getValue().equals(FunctionImportProducerMock.POSTAL_CODE)
                && city.getProperty("CityName", String.class).getValue().equals(FunctionImportProducerMock.CITY);
          }
        });
  }

  @Test
  public void testFunctionReturnCollectionString() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_COLLECTION_STRING + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();
      this.logger.debug(resource);

      assertEquals(format.toString(), 200, responseData.getStatusCode());

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnCollectionString", resource);
        assertXpathNotExists("/d:TestFunctionReturnCollectionString/d:element/@m:type", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.COLLECTION_STRING1, "/d:TestFunctionReturnCollectionString/d:element[1]/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.COLLECTION_STRING2, "/d:TestFunctionReturnCollectionString/d:element[2]/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.COLLECTION_STRING1));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.COLLECTION_STRING2));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnCollectionStringConsumer() {

    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_COLLECTION_STRING,
        EdmSimpleType.STRING,
        2,
        new Predicate1<OObject>() {

          @Override
          public boolean apply(OObject t) {
            String s = ((OSimpleObject) t).getValue().toString();
            return FunctionImportProducerMock.COLLECTION_STRING1.equals(s) ||
                FunctionImportProducerMock.COLLECTION_STRING2.equals(s);
          }
        });
  }

  @Test
  public void testFunctionReturnCollectionDouble() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_COLLECTION_DOUBLE + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();
      this.logger.debug(resource);

      assertEquals(format.toString(), 200, responseData.getStatusCode());

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnCollectionDouble", resource);
        assertXpathNotExists("/d:TestFunctionReturnCollectionDouble/d:element/@m:type", resource);
        assertXpathEvaluatesTo(Double.toString(FunctionImportProducerMock.COLLECTION_DOUBLE1), "/d:TestFunctionReturnCollectionDouble/d:element[1]/text()", resource);
        assertXpathEvaluatesTo(Double.toString(FunctionImportProducerMock.COLLECTION_DOUBLE2), "/d:TestFunctionReturnCollectionDouble/d:element[2]/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(Double.toString(FunctionImportProducerMock.COLLECTION_DOUBLE1)));
        assertTrue(format.toString(), resource.contains(Double.toString(FunctionImportProducerMock.COLLECTION_DOUBLE2)));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnCollectionDoubleConsumer() {

    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_COLLECTION_DOUBLE,
        EdmSimpleType.DOUBLE,
        2,
        null);
  }

  @Test
  public void testFunctionReturnCollectionComplexType() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_COLLECTION_COMPLEX_TYPE + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();
      this.logger.debug(resource);

      assertEquals(format.toString(), 200, responseData.getStatusCode());

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnCollectionComplexType", resource);
        assertXpathEvaluatesTo("RefScenario.c_Location", "/d:TestFunctionReturnCollectionComplexType/d:element/@m:type", resource);
        assertXpathEvaluatesTo("RefScenario.c_City", "/d:TestFunctionReturnCollectionComplexType/d:element[1]/d:City/@m:type", resource);
        assertXpathEvaluatesTo("RefScenario.c_City", "/d:TestFunctionReturnCollectionComplexType/d:element[1]/d:City/@m:type", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.CITY, "/d:TestFunctionReturnCollectionComplexType/d:element[1]/d:City/d:CityName/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.CITY, "/d:TestFunctionReturnCollectionComplexType/d:element[2]/d:City/d:CityName/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.POSTAL_CODE, "/d:TestFunctionReturnCollectionComplexType/d:element[1]/d:City/d:PostalCode/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.POSTAL_CODE, "/d:TestFunctionReturnCollectionComplexType/d:element[2]/d:City/d:PostalCode/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.COUNTRY, "/d:TestFunctionReturnCollectionComplexType/d:element[1]/d:Country", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.COUNTRY, "/d:TestFunctionReturnCollectionComplexType/d:element[2]/d:Country", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.COUNTRY));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.POSTAL_CODE));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.CITY));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnCollectionComplexTypeConsumer() {

    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_COLLECTION_COMPLEX_TYPE,
        mockProducer.getMetadata().findEdmComplexType(FunctionImportProducerMock.COMPLEY_TYPE_NAME_LOCATION),
        2,
        null);
  }

  @Test
  public void testFunctionReturnCollectionEntityType() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData = this.rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_COLLECTION_ENTITY + "?" + this.formatQuery(format));
      String resource = responseData.getEntity();
      this.logger.debug(resource);

      assertEquals(format.toString(), 200, responseData.getStatusCode());

      switch (format) {
      case ATOM:
        assertXpathExists("/g:feed", resource);
        assertXpathEvaluatesTo("RefScenario.Employee", "/g:feed/g:entry/g:category/@term", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.EMPLOYEE_ID, "/g:feed/g:entry/g:content/m:properties/d:EmployeeId/text()", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.EMPLOYEE_NAME, "/g:feed/g:entry/g:content/m:properties/d:EmployeeName/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains("\"results\" : ["));
        assertTrue(format.toString(), resource.contains("\"__metadata\" : {"));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.EMPLOYEE_NAME));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.EMPLOYEE_ID));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnCollectionEntityTypeConsumer() {

    testFunctionConsumer(MetadataUtil.TEST_FUNCTION_RETURN_COLLECTION_ENTITY,
        mockProducer.getMetadata().findEdmEntitySet("Employees").getType(),
        1,
        new Predicate1<OObject>() {

          @Override
          public boolean apply(OObject t) {
            OEntity e = (OEntity) t;
            return e.getProperty("EmployeeName", String.class).getValue().equals(FunctionImportProducerMock.EMPLOYEE_NAME)
                && e.getProperty("EmployeeId", String.class).getValue().equals(FunctionImportProducerMock.EMPLOYEE_ID);
          }
        });
  }

  @Test
  public void testFunctionReturnEntitySet() throws Exception {
    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData = rtFacade.getWebResource(endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_ENTITYSET + "?" + formatQuery(format));
      logger.debug(responseData.getEntity());
      assertEquals(format.toString(), Status.OK.getStatusCode(), responseData.getStatusCode());
    }
  }

  @Test
  public void testFunctionReturnStringPost() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      String param = "p1='abc'";
      String uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_POST + "?" + param + "&" + this.formatQuery(format);

      ResponseData responseData = this.rtFacade.postWebResource(uri, null, null, null);
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      assertEquals(format.toString(), "p1", this.mockProducer.getQueryParameter().get("p1").getName());
      assertEquals(format.toString(), EdmSimpleType.STRING, this.mockProducer.getQueryParameter().get("p1").getType());
      assertEquals(format.toString(), "abc", OSimpleObjects.getValueDisplayString(this.mockProducer.getQueryParameter().get("p1").getValue()));

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnStringPost", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnStringPost/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING_POST));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnStringGet() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      String param = "p1='abc'";
      String uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_GET + "?" + param + "&" + this.formatQuery(format);

      ResponseData responseData = this.rtFacade.getWebResource(uri, null, null, null);
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());
      assertTrue(format.toString(), this.mockProducer.getQueryParameter().containsKey("p1"));

      assertEquals(format.toString(), "p1", this.mockProducer.getQueryParameter().get("p1").getName());
      assertEquals(format.toString(), EdmSimpleType.STRING, this.mockProducer.getQueryParameter().get("p1").getType());
      assertEquals(format.toString(), "abc", OSimpleObjects.getValueDisplayString(this.mockProducer.getQueryParameter().get("p1").getValue()));
      
      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnStringGet", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnStringGet/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING_GET));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnStringMerge() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      String param = "p1='abc'";
      String uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_MERGE + "?" + param + "&" + this.formatQuery(format);

      ResponseData responseData = this.rtFacade.mergeWebResource(uri, null, null, null);
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      assertEquals(format.toString(), "p1", this.mockProducer.getQueryParameter().get("p1").getName());
      assertEquals(format.toString(), EdmSimpleType.STRING, this.mockProducer.getQueryParameter().get("p1").getType());
      assertEquals(format.toString(), "abc", OSimpleObjects.getValueDisplayString(this.mockProducer.getQueryParameter().get("p1").getValue()));

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnStringMerge", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnStringMerge/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING_MERGE));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnStringPut() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      String param = "p1='abc'";
      String uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_PUT + "?" + param + "&" + this.formatQuery(format);

      ResponseData responseData = this.rtFacade.putWebResource(uri, null, null, null);
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      assertEquals(format.toString(), "p1", this.mockProducer.getQueryParameter().get("p1").getName());
      assertEquals(format.toString(), EdmSimpleType.STRING, this.mockProducer.getQueryParameter().get("p1").getType());
      assertEquals(format.toString(), "abc", OSimpleObjects.getValueDisplayString(this.mockProducer.getQueryParameter().get("p1").getValue()));

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnStringPut", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnStringPut/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING_PUT));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnStringDelete() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      String param = "p1='abc'";
      String uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_DELETE + "?" + param + "&" + this.formatQuery(format);

      ResponseData responseData = this.rtFacade.deleteWebResource(uri, null, null, null);
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      assertEquals(format.toString(), "p1", this.mockProducer.getQueryParameter().get("p1").getName());
      assertEquals(format.toString(), EdmSimpleType.STRING, this.mockProducer.getQueryParameter().get("p1").getType());
      assertEquals(format.toString(), "abc", OSimpleObjects.getValueDisplayString(this.mockProducer.getQueryParameter().get("p1").getValue()));

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnStringDelete", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnStringDelete/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING_DELETE));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testFunctionReturnStringPatch() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      String param = "p1='abc'";
      String uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_PATCH + "?" + param + "&" + this.formatQuery(format);

      ResponseData responseData = this.rtFacade.patchWebResource(uri, null, null, null);
      String resource = responseData.getEntity();

      assertEquals(format.toString(), 200, responseData.getStatusCode());
      assertNotNull(format.toString(), this.mockProducer.getQueryParameter());

      assertEquals(format.toString(), "p1", this.mockProducer.getQueryParameter().get("p1").getName());
      assertEquals(format.toString(), EdmSimpleType.STRING, this.mockProducer.getQueryParameter().get("p1").getType());
      assertEquals(format.toString(), "abc", OSimpleObjects.getValueDisplayString(this.mockProducer.getQueryParameter().get("p1").getValue()));

      switch (format) {
      case ATOM:
        assertXpathExists("/d:TestFunctionReturnStringPatch", resource);
        assertXpathEvaluatesTo(FunctionImportProducerMock.SOME_TEXT, "/d:TestFunctionReturnStringPatch/text()", resource);
        break;
      case JSON:
        assertTrue(format.toString(), resource.contains(MetadataUtil.TEST_FUNCTION_RETURN_STRING_PATCH));
        assertTrue(format.toString(), resource.contains(FunctionImportProducerMock.SOME_TEXT));
        break;
      default:
        throw new RuntimeException("Unknown Format Type: " + format);
      }
    }
  }

  @Test
  public void testMethodNotAllowed() {
    for (FormatType format : FunctionImportTest.formats) {
      ResponseData responseData;
      String uri;

      uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_GET + "?" + this.formatQuery(format);
      responseData = this.rtFacade.putWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.postWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.deleteWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.patchWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.mergeWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());

      uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_PUT + "?" + this.formatQuery(format);
      responseData = this.rtFacade.getWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.postWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.deleteWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.patchWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.mergeWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());

      uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_POST + "?" + this.formatQuery(format);
      responseData = this.rtFacade.getWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.putWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.deleteWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.patchWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.mergeWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());

      uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_DELETE + "?" + this.formatQuery(format);
      responseData = this.rtFacade.getWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.postWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.putWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.patchWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.mergeWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());

      uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_MERGE + "?" + this.formatQuery(format);
      responseData = this.rtFacade.getWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.postWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.deleteWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.patchWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.putWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());

      uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_PATCH + "?" + this.formatQuery(format);
      responseData = this.rtFacade.getWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.postWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.deleteWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.putWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
      responseData = this.rtFacade.mergeWebResource(uri, null, null, null);
      assertEquals(format.toString(), 405, responseData.getStatusCode());
    }
  }

  @Test
  public void testMethodNotFound() throws XpathException, IOException, SAXException {
    for (FormatType format : FunctionImportTest.formats) {
      String uri = endpointUri + "MethodNotAllowed" + "?" + this.formatQuery(format);

      ResponseData responseData;

      responseData = this.rtFacade.putWebResource(uri, null, null, null);
      assertEquals(format.toString(), 404, responseData.getStatusCode());

      responseData = this.rtFacade.deleteWebResource(uri, null, null, null);
      assertEquals(format.toString(), 404, responseData.getStatusCode());

      responseData = this.rtFacade.postWebResource(uri, null, null, null);
      assertEquals(format.toString(), 404, responseData.getStatusCode());

      responseData = this.rtFacade.getWebResource(uri, null, null, null);
      assertEquals(format.toString(), 404, responseData.getStatusCode());

      responseData = this.rtFacade.patchWebResource(uri, null, null, null);
      assertEquals(format.toString(), 404, responseData.getStatusCode());

      responseData = this.rtFacade.mergeWebResource(uri, null, null, null);
      assertEquals(format.toString(), 404, responseData.getStatusCode());
    }
  }

  @Test
  public void testBatch() throws XpathException, IOException, SAXException {

    /*
     * because of we don't have batch processing test we will to a sanity batch test here because of function call support
     * for all http methods did affect batch implementation (s. EntietiesRequestResource). 
     */

    for (FormatType format : FunctionImportTest.formats) {
      String uri = endpointUri + MetadataUtil.TEST_FUNCTION_RETURN_STRING_POST + "/$batch" + "?" + this.formatQuery(format);

      Hashtable<String, Object> header = new Hashtable<String, Object>();
      header.put("content-type", new MediaType("multipart", "mixed"));
      ResponseData responseData = this.rtFacade.postWebResource(uri, null, null, header);
      String resource = responseData.getEntity();

      assertNotNull(resource);
      assertTrue(resource.contains("batchresponse"));
      assertEquals(format.toString(), 202, responseData.getStatusCode());
    }
  }
}
