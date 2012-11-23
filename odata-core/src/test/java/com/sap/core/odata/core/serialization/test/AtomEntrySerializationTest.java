package com.sap.core.odata.core.serialization.test;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.core.serializer.AtomEntrySerializer;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.helper.XMLUnitHelper;

@Ignore
public class AtomEntrySerializationTest {

  private static final Logger LOG = LoggerFactory.getLogger(AtomEntrySerializationTest.class);

  private EdmEntityType edmEntityType;
  
  private Map<String, Object> data;

  {
    try {
      this.data = new HashMap<String, Object>();

      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm");
      Date date;
      date = formatter.parse("1999-01-01T00:00");

      this.data.put("employeeId", "1");
      this.data.put("immageUrl", null);
      this.data.put("managerId", "1");
      this.data.put("age", new Integer(52));
      this.data.put("roomId", "1");
      this.data.put("entryData", date);
      this.data.put("teamId", "1");
      this.data.put("employeeName", "Walter Winter");
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  @Before
  public void before() throws EdmException {
    Map<String, String> ns = new HashMap<String, String>();
    ns.put("d", AtomEntrySerializer.NS_DATASERVICES);
    ns.put("m", AtomEntrySerializer.NS_DATASERVICES_METADATA);
    ns.put("default", AtomEntrySerializer.NS_ATOM); 
    XMLUnitHelper.registerXmlNs(ns);

    this.edmEntityType = mock(EdmEntityType.class);
  }

  @Test
  public void serializeEntry() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer s = ODataSerializer.create(Format.ATOM);
    assertNotNull(s);

    InputStream xmlStream = s.serialize();

    String xmlString = StringHelper.inputStreamToString(xmlStream);

    LOG.debug(xmlString);

    assertXpathExists("/entry", xmlString);
    assertXpathEvaluatesTo(AtomEntrySerializer.BASE_URI, "/entry/@xml:base", xmlString);

//    assertXpathExists("/entry/id", xmlString);
//
//    assertXpathExists("/entry/title", xmlString);
//    assertXpathEvaluatesTo("text", "/entry/title/@type", xmlString);
//
//    assertXpathExists("/entry/updated", xmlString);
//    assertXpathEvaluatesTo("2012-11-21T12:53:30Z", "/entry/updated", xmlString);
//
//    assertXpathExists("/entry/author", xmlString);
//    assertXpathExists("/entry/author/name", xmlString);
//    assertXpathExists("/entry/category", xmlString);
//    assertXpathExists("/entry/content", xmlString);
//    assertXpathExists("/entry/content/m:properties", xmlString);
//    assertXpathExists("/entry/content/m:properties/d:employeeId", xmlString);
//    assertXpathExists("/entry/content/m:properties/d:imageUrl", xmlString);
//    assertXpathExists("/entry/content/m:properties/d:managerId", xmlString);
//    assertXpathExists("/entry/content/m:properties/d:age", xmlString);
//    assertXpathExists("/entry/content/m:properties/d:roomId", xmlString);
//    assertXpathExists("/entry/content/m:properties/d:entryDate", xmlString);
//    assertXpathExists("/entry/content/m:properties/d:teamId", xmlString);
//    assertXpathExists("/entry/content/m:properties/d:employeeName", xmlString);

  }
}
