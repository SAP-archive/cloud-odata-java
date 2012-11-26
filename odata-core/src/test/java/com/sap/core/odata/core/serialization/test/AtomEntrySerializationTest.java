package com.sap.core.odata.core.serialization.test;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import com.sap.core.odata.api.edm.EdmEntityContainer;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmSimpleTypeKind;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.processor.ODataContext;
import com.sap.core.odata.api.processor.ODataUriInfo;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.core.serializer.AtomEntrySerializer;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.helper.XMLUnitHelper;


public class AtomEntrySerializationTest {

  private static final String BASE_URI = "http://host:port/service/";

  private static final Logger LOG = LoggerFactory.getLogger(AtomEntrySerializationTest.class);
  
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
    ns.put("a", AtomEntrySerializer.NS_ATOM); 
    XMLUnitHelper.registerXmlNs(ns);
  }

  @Test
  public void serializeEntry() throws IOException, XpathException, SAXException, XMLStreamException, FactoryConfigurationError, ODataException {
    ODataSerializer ser = ODataSerializer.create(Format.ATOM);
    assertNotNull(ser);


    ODataUriInfo uriInfo = mock(ODataUriInfo.class);
    when(uriInfo.getBaseUri()).thenReturn(BASE_URI);
    ODataContext ctx = mock(ODataContext.class);
    when(ctx.getUriInfo()).thenReturn(uriInfo);
    
    EdmEntityContainer ec = mock(EdmEntityContainer.class);
    when(ec.getName()).thenReturn("Container");
    when(ec.isDefaultEntityContainer()).thenReturn(false);
    
    List<EdmProperty> kpl = new ArrayList<EdmProperty>();
    EdmProperty idp = mock(EdmProperty.class);
    when(idp.getName()).thenReturn("employeeId");
    when(idp.getType()).thenReturn(EdmSimpleTypeKind.stringInstance());
    kpl.add(idp);
//    EdmProperty idp2 = mock(EdmProperty.class);
//    when(idp2.getName()).thenReturn("age");
//    when(idp2.getType()).thenReturn(EdmSimpleTypeFacade.int32Instance());
//    kpl.add(idp2);
    
    EdmEntityType et = mock(EdmEntityType.class);
    when(et.getKeyProperties()).thenReturn(kpl);
    
    EdmEntitySet es = mock(EdmEntitySet.class);
    when(es.getName()).thenReturn("Employees");
    when(es.getEntityContainer()).thenReturn(ec);
    when(es.getEntityType()).thenReturn(et);
    
    ser.setContext(ctx);
    ser.setData(this.data);
    ser.setEdmEntitySet(es);
    
    
    InputStream xmlStream = ser.serialize();

    String xmlString = StringHelper.inputStreamToString(xmlStream);

    LOG.debug(xmlString);

    assertXpathExists("/a:entry", xmlString);
    assertXpathEvaluatesTo(BASE_URI, "/a:entry/@xml:base", xmlString);

    assertXpathExists("/a:entry/a:id", xmlString);
    assertXpathEvaluatesTo(BASE_URI + "Container.Employees('1')", "/a:entry/a:id/text()", xmlString);
//
//    assertXpathExists("/a:entry/a:title", xmlString);
//    assertXpathEvaluatesTo("text", "/a:entry/a:title/@type", xmlString);
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
