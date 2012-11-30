package com.sap.core.odata.core.serialization.test;

import static org.junit.Assert.assertNotNull;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

import java.io.InputStream;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.testutils.helper.StringHelper;

public class XmlPropertySerializationTest extends AbstractSerializerTest {

  @Test
  public void serializeEmployeeId() throws Exception {
    ODataSerializer s = ODataSerializer.create(Format.XML, this.createContextMock());
   
    EdmTyped edmTyped = this.createEdmEntitySetMock(false).getEntityType().getProperty("employeeId");
    EdmProperty edmProperty = (EdmProperty) edmTyped; 
    
    InputStream in = s.serializeProperty(edmProperty, this.data.get("employeeId"));
    assertNotNull(in);
    String xml = StringHelper.inputStreamToString(in);
    assertNotNull(xml);

    assertXpathExists("/d:employeeId", xml);
    assertXpathEvaluatesTo("1", "/d:employeeId/text()", xml);
  }

  @Test
  public void serializeAge() throws Exception {
    ODataSerializer s = ODataSerializer.create(Format.XML, this.createContextMock());
   
    EdmTyped edmTyped = this.createEdmEntitySetMock(false).getEntityType().getProperty("age");
    EdmProperty edmProperty = (EdmProperty) edmTyped; 
    
    InputStream in = s.serializeProperty(edmProperty, this.data.get("age"));
    assertNotNull(in);
    String xml = StringHelper.inputStreamToString(in);
    assertNotNull(xml);

    this.log.debug(xml);

    assertXpathExists("/d:age", xml);
    assertXpathEvaluatesTo("52", "/d:age/text()", xml);
    assertXpathExists("/d:age/@m:type", xml);
    assertXpathEvaluatesTo("Edm.Int32", "/d:age/@m:type", xml);
    
  }

  @Test
  public void serializeImageUrl() throws Exception {
    ODataSerializer s = ODataSerializer.create(Format.XML, this.createContextMock());
   
    EdmTyped edmTyped = this.createEdmEntitySetMock(false).getEntityType().getProperty("imageUrl");
    EdmProperty edmProperty = (EdmProperty) edmTyped; 
    
    InputStream in = s.serializeProperty(edmProperty, this.data.get("imageUrl"));
    assertNotNull(in);
    String xml = StringHelper.inputStreamToString(in);
    assertNotNull(xml);

    this.log.debug(xml);

    assertXpathExists("/d:imageUrl", xml);
    assertXpathExists("/d:imageUrl/@m:null", xml);
    assertXpathEvaluatesTo("true", "/d:imageUrl/@m:null", xml);
    
  }
}
