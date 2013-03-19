package com.sap.core.odata.core.ep.producer;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmConcurrencyMode;
import com.sap.core.odata.api.edm.EdmCustomizableFeedMappings;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmFacets;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTargetPath;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.ep.EntityProviderProperties;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.ep.AbstractProviderTest;
import com.sap.core.odata.core.ep.AtomEntityProvider;
import com.sap.core.odata.testutil.helper.StringHelper;
import com.sap.core.odata.testutil.helper.XMLUnitHelper;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class AtomEntryProducerTest extends AbstractProviderTest {

  public AtomEntryProducerTest(StreamWriterImplType type) {
    super(type);
  }

  @Test
  public void serializeAtomMediaResource() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);

    assertXpathExists("/a:entry/a:content", xmlString);
    assertXpathEvaluatesTo(ContentType.APPLICATION_OCTET_STREAM.toString(), "/a:entry/a:content/@type", xmlString);
    assertXpathEvaluatesTo("Employees('1')/$value", "/a:entry/a:content/@src", xmlString);
    assertXpathExists("/a:entry/m:properties", xmlString);

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/$value\"]", xmlString);
    assertXpathExists("/a:entry/a:link[@rel='edit-media']", xmlString);
    assertXpathExists("/a:entry/a:link[@type='application/octet-stream']", xmlString);

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')\"]", xmlString);
    assertXpathExists("/a:entry/a:link[@rel='edit']", xmlString);
    assertXpathExists("/a:entry/a:link[@title='Employee']", xmlString);

    // assert navigation link order
    verifyTagOrdering(xmlString,
        "link((?:(?!link).)*?)edit",
        "link((?:(?!link).)*?)edit-media",
        "link((?:(?!link).)*?)ne_Manager",
        "link((?:(?!link).)*?)ne_Team",
        "link((?:(?!link).)*?)ne_Room");
  }

  private String verifyResponse(final ODataResponse response) throws IOException {
    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_ATOM_XML_ENTRY_CS_UTF_8.toContentTypeString(), response.getContentHeader());
    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());
    return xmlString;
  }

  @Test
  public void serializeAtomMediaResourceWithMimeType() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).mediaResourceMimeType("abc").build();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);

    assertXpathExists("/a:entry/a:content", xmlString);
    assertXpathEvaluatesTo("abc", "/a:entry/a:content/@type", xmlString);
    assertXpathEvaluatesTo("Employees('1')/$value", "/a:entry/a:content/@src", xmlString);
    assertXpathExists("/a:entry/m:properties", xmlString);

  }

  @Test
  public void serializeEmployeeAndCheckOrderOfTags() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).mediaResourceMimeType("abc").build();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathExists("/a:entry/a:content", xmlString);
    // verify self link
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')\"]", xmlString);
    // verify content media link
    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/$value\"]", xmlString);
    // verify one navigation link
    assertXpathExists("/a:entry/a:link[@title='ne_Manager']", xmlString);

    // verify content
    assertXpathExists("/a:entry/a:content[@type='abc']", xmlString);
    // verify properties
    assertXpathExists("/a:entry/m:properties", xmlString);
    assertXpathEvaluatesTo("9", "count(/a:entry/m:properties/*)", xmlString);

    // verify order of tags
    verifyTagOrdering(xmlString, "id", "title", "updated", "category",
        "link((?:(?!link).)*?)edit",
        "link((?:(?!link).)*?)edit-media",
        "link((?:(?!link).)*?)ne_Manager",
        "content", "properties");
  }

  @Test
  public void serializeEmployeeAndCheckOrderOfPropertyTags() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).mediaResourceMimeType("abc").build();
    EdmEntitySet employeeEntitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    ODataResponse response = ser.writeEntry(employeeEntitySet, employeeData, properties);
    String xmlString = verifyResponse(response);

    //        log.debug(xmlString);

    assertXpathExists("/a:entry", xmlString);
    assertXpathExists("/a:entry/a:content", xmlString);
    // verify properties
    assertXpathExists("/a:entry/m:properties", xmlString);
    assertXpathEvaluatesTo("9", "count(/a:entry/m:properties/*)", xmlString);

    // verify order of tags
    List<String> expectedPropertyNamesFromEdm = employeeEntitySet.getEntityType().getPropertyNames();
    verifyTagOrdering(xmlString, expectedPropertyNamesFromEdm.toArray(new String[0]));
  }

  @Test
  public void serializeEmployeeAndCheckKeepInContentFalse() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).mediaResourceMimeType("abc").build();
    EdmEntitySet employeeEntitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");

    // set "keepInContent" to false for EntryDate
    EdmCustomizableFeedMappings employeeUpdatedMappings = mock(EdmCustomizableFeedMappings.class);
    when(employeeUpdatedMappings.getFcTargetPath()).thenReturn(EdmTargetPath.SYNDICATION_UPDATED);
    when(employeeUpdatedMappings.isFcKeepInContent()).thenReturn(Boolean.FALSE);
    EdmTyped employeeEntryDateProperty = employeeEntitySet.getEntityType().getProperty("EntryDate");
    when(((EdmProperty) employeeEntryDateProperty).getCustomizableFeedMappings()).thenReturn(employeeUpdatedMappings);

    ODataResponse response = ser.writeEntry(employeeEntitySet, employeeData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathExists("/a:entry/a:content", xmlString);
    // verify properties
    assertXpathExists("/a:entry/m:properties", xmlString);
    assertXpathEvaluatesTo("8", "count(/a:entry/m:properties/*)", xmlString);
    //
    assertXpathNotExists("/a:entry/m:properties/d:EntryDate", xmlString);

    // verify order of tags
    List<String> expectedPropertyNamesFromEdm = new ArrayList<String>(employeeEntitySet.getEntityType().getPropertyNames());
    expectedPropertyNamesFromEdm.remove(String.valueOf("EntryDate"));
    verifyTagOrdering(xmlString, expectedPropertyNamesFromEdm.toArray(new String[0]));
  }

  @Test
  public void serializeAtomEntry() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    final EntityProviderProperties properties = EntityProviderProperties.serviceRoot(BASE_URI).build();
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), roomData, properties);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);

    assertXpathExists("/a:entry/a:content", xmlString);
    assertXpathEvaluatesTo(ContentType.APPLICATION_XML.toString(), "/a:entry/a:content/@type", xmlString);

    assertXpathExists("/a:entry/a:content/m:properties", xmlString);
  }

  @Test
  public void serializeEntryId() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Employees('1')", "/a:entry/a:id/text()", xmlString);
  }

  @Test
  public void serializeEntryTitle() throws Exception {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry/a:title", xmlString);
    assertXpathEvaluatesTo("text", "/a:entry/a:title/@type", xmlString);
    assertXpathEvaluatesTo((String) employeeData.get("EmployeeName"), "/a:entry/a:title/text()", xmlString);
  }

  @Test
  public void serializeEntryUpdated() throws Exception {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry/a:updated", xmlString);
    assertXpathEvaluatesTo("1999-01-01T00:00:00Z", "/a:entry/a:updated/text()", xmlString);
  }

  @Test
  public void serializeIds() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos"), photoData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Container2.Photos(Id=1,Type='image%2fpng')", "/a:entry/a:id/text()", xmlString);
  }

  @Test
  public void serializeProperties() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry/m:properties", xmlString);
    assertXpathEvaluatesTo((String) employeeData.get("RoomId"), "/a:entry/m:properties/d:RoomId/text()", xmlString);
    assertXpathEvaluatesTo((String) employeeData.get("TeamId"), "/a:entry/m:properties/d:TeamId/text()", xmlString);
  }

  @Test
  public void serializeWithValueEncoding() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    photoData.put("Type", "< Ö >");

    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos"), photoData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Container2.Photos(Id=1,Type='%3c%20%c3%96%20%3e')", "/a:entry/a:id/text()", xmlString);
    assertXpathEvaluatesTo("Container2.Photos(Id=1,Type='%3c%20%c3%96%20%3e')", "/a:entry/a:link/@href", xmlString);
  }

  @Test
  public void serializeCategory() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry/a:category", xmlString);
    assertXpathExists("/a:entry/a:category/@term", xmlString);
    assertXpathExists("/a:entry/a:category/@scheme", xmlString);
    assertXpathEvaluatesTo("RefScenario.Employee", "/a:entry/a:category/@term", xmlString);
    assertXpathEvaluatesTo(Edm.NAMESPACE_SCHEME_2007_08, "/a:entry/a:category/@scheme", xmlString);
  }

  @Test
  public void serializeETag() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos"), photoData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertXpathExists("/a:entry/@m:etag", xmlString);
    assertXpathEvaluatesTo("W/\"1\"", "/a:entry/@m:etag", xmlString);
    assertEquals("W/\"1\"", response.getETag());
  }

  @Test
  public void serializeETagEncoding() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    Edm edm = MockFacade.getMockEdm();
    EdmTyped roomIdProperty = edm.getEntityType("RefScenario", "Room").getProperty("Id");
    EdmFacets facets = mock(EdmFacets.class);
    when(facets.getConcurrencyMode()).thenReturn(EdmConcurrencyMode.Fixed);
    when(facets.getMaxLength()).thenReturn(3);
    when(((EdmProperty) roomIdProperty).getFacets()).thenReturn(facets);

    roomData.put("Id", "<\">");
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(edm.getDefaultEntityContainer().getEntitySet("Rooms"), roomData, DEFAULT_PROPERTIES);

    assertNotNull(response);
    assertNotNull(response.getEntity());
    assertEquals(ContentType.APPLICATION_ATOM_XML_ENTRY_CS_UTF_8.toContentTypeString(), response.getContentHeader());
    assertEquals("W/\"<\">.3\"", response.getETag());

    String xmlString = StringHelper.inputStreamToString((InputStream) response.getEntity());

    assertXpathExists("/a:entry", xmlString);
    assertXpathExists("/a:entry/@m:etag", xmlString);
    assertXpathEvaluatesTo("W/\"<\">.3\"", "/a:entry/@m:etag", xmlString);
  }

  @Test
  public void serializeCustomMapping() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos"), photoData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    //    assertXpathExists("/a:entry/ру:Содержание", xmlString);
    //    assertXpathEvaluatesTo("", "/a:entry/ру:Содержание/text()", xmlString);
    // ugly, but problems with XMLUnit and 'namespace:ру'
    final String tagcontent = (String) photoData.get("Содержание");
    assertTrue("Expected result was not found in input [\n\n" + xmlString + "\n\n].",
        //        Pattern.matches(".*<ру:Содержание xmlns:ру=\"http://localhost\">" + tagcontent + "</ру:Содержание>.*", xmlString));
        Pattern.matches(".*:Содержание xmlns:..=\"http://localhost\">" + tagcontent + "</..:Содержание>.*", xmlString));
    verifyTagOrdering(xmlString, "category", "Содержание", "content", "properties");
  }

  @Test
  public void testCustomProperties() throws Exception {
    AtomEntityProvider ser = createAtomEntityProvider();
    EdmEntitySet entitySet = MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos");

    ODataResponse response = ser.writeEntry(entitySet, photoData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);
    assertXpathExists("/a:entry", xmlString);
    assertTrue("Expected result was not found in input [\n\n" + xmlString + "\n\n].",
        Pattern.matches(".*<custom:CustomProperty xmlns:custom=\"http://localhost\" m:null=\"true\".*", xmlString));
    verifyTagOrdering(xmlString, "category", "Содержание", "CustomProperty", "content", "properties");
  }

  @Test
  public void testKeepInContentNull() throws Exception {
    AtomEntityProvider ser = createAtomEntityProvider();
    EdmEntitySet entitySet = MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos");

    EdmProperty customProperty = (EdmProperty) entitySet.getEntityType().getProperty("CustomProperty");
    when(customProperty.getCustomizableFeedMappings().isFcKeepInContent()).thenReturn(null);

    ODataResponse response = ser.writeEntry(entitySet, photoData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    assertXpathExists("/a:entry", xmlString);
    assertTrue("Expected result was not found in input [\n\n" + xmlString + "\n\n].",
        Pattern.matches(".*<custom:CustomProperty xmlns:custom=\"http://localhost\" m:null=\"true\".*", xmlString));
    verifyTagOrdering(xmlString, "category", "Содержание", "CustomProperty", "content", "properties");
  }

  @Test
  public void serializeAtomMediaResourceLinks() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    AtomEntityProvider ser = createAtomEntityProvider();
    ODataResponse response = ser.writeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), employeeData, DEFAULT_PROPERTIES);
    String xmlString = verifyResponse(response);

    String rel = Edm.NAMESPACE_REL_2007_08 + "ne_Manager";

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Manager\"]", xmlString);
    assertXpathExists("/a:entry/a:link[@rel='" + rel + "']", xmlString);
    assertXpathExists("/a:entry/a:link[@type='application/atom+xml; type=entry']", xmlString);
    assertXpathExists("/a:entry/a:link[@title='ne_Manager']", xmlString);
  }

  private void verifyTagOrdering(final String xmlString, final String... toCheckTags) {
    XMLUnitHelper.verifyTagOrdering(xmlString, toCheckTags);
  }
}