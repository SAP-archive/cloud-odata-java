package com.sap.core.odata.core.ep.test;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.enums.MediaType;
import com.sap.core.odata.api.ep.ODataSerializer;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.mocks.MockFacade;

public class AtomEntrySerializationTest extends AbstractSerializerTest {

  @Test
  public void serializeAtomMediaResource() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), this.employeeData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    log.debug(xmlString);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);

    assertXpathExists("/a:entry/a:content", xmlString);
    assertXpathEvaluatesTo(MediaType.APPLICATION_OCTET_STREAM.toString(), "/a:entry/a:content/@type", xmlString);
    assertXpathEvaluatesTo("Employees('1')/$value", "/a:entry/a:content/@src", xmlString);
    assertXpathExists("/a:entry/m:properties", xmlString);

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/$value\"]", xmlString);
    assertXpathExists("/a:entry/a:link[@rel='edit-media']", xmlString);
    assertXpathExists("/a:entry/a:link[@type='application/octet-stream']", xmlString);

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')\"]", xmlString);
    assertXpathExists("/a:entry/a:link[@rel='edit']", xmlString);
    assertXpathExists("/a:entry/a:link[@title='Employee']", xmlString);
  }

  @Test
  public void serializeAtomMediaResourceWithMimeType() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), this.employeeData, "abc");
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);

    assertXpathExists("/a:entry/a:content", xmlString);
    assertXpathEvaluatesTo("abc", "/a:entry/a:content/@type", xmlString);
    assertXpathEvaluatesTo("Employees('1')/$value", "/a:entry/a:content/@src", xmlString);
    assertXpathExists("/a:entry/m:properties", xmlString);

  }

  @Test
  public void serializeAtomEntry() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), this.roomData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);

    assertXpathExists("/a:entry/a:content", xmlString);
    assertXpathEvaluatesTo(MediaType.APPLICATION_XML.toString(), "/a:entry/a:content/@type", xmlString);

    assertXpathExists("/a:entry/a:content/m:properties", xmlString);
  }

  @Test
  public void serializeEntryId() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), this.employeeData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Employees('1')", "/a:entry/a:id/text()", xmlString);
  }

  @Test
  public void serializeEntryTitle() throws Exception {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), this.employeeData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry/a:title", xmlString);
    assertXpathEvaluatesTo("text", "/a:entry/a:title/@type", xmlString);
    assertXpathEvaluatesTo((String) employeeData.get("EmployeeName"), "/a:entry/a:title/text()", xmlString);
  }

  @Test
  public void serializeEntryUpdated() throws Exception {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), this.employeeData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry/a:updated", xmlString);
    assertXpathEvaluatesTo("1999-01-01T00:00:00Z", "/a:entry/a:updated/text()", xmlString);
  }

  @Test
  public void serializeIds() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos"), this.photoData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    log.debug(xmlString);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Container2.Photos(Id=1,Type='JPG')", "/a:entry/a:id/text()", xmlString);
  }

  @Test
  public void serializeProperties() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), this.employeeData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    log.debug(xmlString);

    assertXpathExists("/a:entry/m:properties", xmlString);
    assertXpathEvaluatesTo((String) employeeData.get("RoomId"), "/a:entry/m:properties/d:RoomId/text()", xmlString);
    assertXpathEvaluatesTo((String) employeeData.get("TeamId"), "/a:entry/m:properties/d:TeamId/text()", xmlString);
  }

  @Test
  public void serializeWithValueEncoding() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    this.photoData.put("Type", "< Ö >");

    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos"), this.photoData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    log.debug(xmlString);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Container2.Photos(Id=1,Type='%3C%20%C3%96%20%3E')", "/a:entry/a:id/text()", xmlString);
    assertXpathEvaluatesTo("Container2.Photos(Id=1,Type='%3C%20%C3%96%20%3E')", "/a:entry/a:link/@href", xmlString);
  }

  @Test
  public void serializeCategory() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), this.employeeData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry/a:category", xmlString);
    assertXpathExists("/a:entry/a:category/@term", xmlString);
    assertXpathExists("/a:entry/a:category/@scheme", xmlString);
    assertXpathEvaluatesTo("RefScenario.Employee", "/a:entry/a:category/@term", xmlString);
    assertXpathEvaluatesTo(Edm.NAMESPACE_SCHEME_2007_08, "/a:entry/a:category/@scheme", xmlString);
  }

  @Test
  public void serializeETag() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos"), this.photoData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    log.debug(xmlString);

    assertXpathExists("/a:entry", xmlString);
    assertXpathExists("/a:entry/@m:etag", xmlString);
    assertXpathEvaluatesTo("W/&quot;1&quot;", "/a:entry/@m:etag", xmlString);
  }

  @Test
  public void serializeETagEncoding() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    this.roomData.put("Id", "<\">");
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), this.roomData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    log.debug(xmlString);

    assertXpathExists("/a:entry", xmlString);
    assertXpathExists("/a:entry/@m:etag", xmlString);
    assertXpathEvaluatesTo("W/&quot;&lt;&quot;&gt;&quot;", "/a:entry/@m:etag", xmlString);
  }

  @Ignore("7 ms")
  @Test
  public void serializePerformance() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    long t = System.currentTimeMillis();
    for (int i = 0; i < 1000; i++) {
      ODataSerializer ser = createAtomSerializer();
      ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms"), this.roomData);
    }

    t = (System.currentTimeMillis() - t) / 1000;
    log.debug("ms: " + t);
  }

  @Test
  public void serializeAtomMediaResourceLinks() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();
    InputStream xmlStream = ser.serializeEntry(MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees"), this.employeeData);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    log.debug(xmlString);

    String rel = Edm.NAMESPACE_REL_2007_08 + "ne_Manager";

    assertXpathExists("/a:entry/a:link[@href=\"Employees('1')/ne_Manager\"]", xmlString);
    assertXpathExists("/a:entry/a:link[@rel='" + rel + "']", xmlString);
    assertXpathExists("/a:entry/a:link[@type='application/atom+xml;type=entry']", xmlString);
    assertXpathExists("/a:entry/a:link[@title='ne_Manager']", xmlString);
  }

}
