package com.sap.core.odata.core.serialization.test;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataUriInfo;
import com.sap.core.odata.api.serialization.ODataSerializationException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.testutils.helper.StringHelper;

public class AtomEntrySerializationTest extends AbstractSerializerTest {

  @Test
  public void serializeEntry() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = createAtomSerializer();

    InputStream xmlStream = ser.serializeEntry(this.createEdmEntitySetMock(false), this.data);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
  }

  @Test
  public void serializeEntryId() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    EdmEntitySet es = createEdmEntitySetMock(false);
    ODataSerializer ser = createAtomSerializer(es);

    InputStream xmlStream = ser.serializeEntry(this.createEdmEntitySetMock(false), this.data);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Container.Employees('1')", "/a:entry/a:id/text()", xmlString);
  }

  @Test
  public void serializeEntryTitle() throws Exception {
    EdmEntitySet es = createEdmEntitySetMock(false);
    ODataSerializer ser = createAtomSerializer(es);

    InputStream xmlStream = ser.serializeEntry(this.createEdmEntitySetMock(false), this.data);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry/a:title", xmlString);
    assertXpathEvaluatesTo("text", "/a:entry/a:title/@type", xmlString);
    assertXpathEvaluatesTo((String) data.get("employeeName"), "/a:entry/a:title/text()", xmlString);
  }

  @Test
  public void serializeEntryUpdated() throws Exception {
    ODataSerializer ser = createAtomSerializer();

    InputStream xmlStream = ser.serializeEntry(this.createEdmEntitySetMock(false), this.data);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry/a:updated", xmlString);
    assertXpathEvaluatesTo("1999-01-01T00:00:00Z", "/a:entry/a:updated/text()", xmlString);
  }



  @Test
  public void serializeIds() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    EdmEntitySet es = createEdmEntitySetMock(true);
    ODataSerializer ser = createAtomSerializer(es);

    InputStream xmlStream = ser.serializeEntry(this.createEdmEntitySetMock(true), this.data);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString(), "/a:entry/@xml:base", xmlString);
    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI.toASCIIString() + "Container.Employees(employeeId='1',age=52)", "/a:entry/a:id/text()", xmlString);
  }

  @Test
  public void serializeProperties() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    EdmEntitySet es = createEdmEntitySetMock(true);
    ODataSerializer ser = createAtomSerializer(es);

    InputStream xmlStream = ser.serializeEntry(this.createEdmEntitySetMock(false), this.data);
    String xmlString = StringHelper.inputStreamToString(xmlStream);

    LOG.debug(xmlString);

    assertXpathExists("/a:entry/m:properties", xmlString);
    assertXpathEvaluatesTo((String) data.get("roomId"), "/a:entry/m:properties/d:roomId/text()", xmlString);
    assertXpathEvaluatesTo((String) data.get("teamId"), "/a:entry/m:properties/d:teamId/text()", xmlString);
  }
}
