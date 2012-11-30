package com.sap.core.odata.core.serialization.test;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.edm.EdmTyped;
import com.sap.core.odata.api.enums.Format;
import com.sap.core.odata.api.serialization.ODataSerializer;
import com.sap.core.odata.testutils.helper.StringHelper;
import com.sap.core.odata.testutils.mocks.MockFacade;

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

    assertXpathExists("/d:imageUrl", xml);
    assertXpathExists("/d:imageUrl/@m:null", xml);
    assertXpathEvaluatesTo("true", "/d:imageUrl/@m:null", xml);
    
  }

  @Test
  public void serializeLocation() throws Exception {
    ODataSerializer s = ODataSerializer.create(Format.XML, this.createContextMock());
   
    EdmEntityType edmEntityType = MockFacade.getMockEdm().getEntityType("RefScenario", "Employee");
    EdmTyped edmTyped = edmEntityType.getProperty("Location");
    EdmProperty edmProperty = (EdmProperty) edmTyped; 
    
    Map<String, Object> locationData = new HashMap<String, Object>();
    Map<String, Object> cityData = new HashMap<String, Object>();
    cityData.put("PostalCode", "33470");
    cityData.put("CityName", "Duckburg");
    locationData.put("City", cityData);
    locationData.put("Country", "Calisota");
    
    InputStream in = s.serializeProperty(edmProperty, locationData);
    assertNotNull(in);
    String xml = StringHelper.inputStreamToString(in);
    assertNotNull(xml);

    this.log.debug(xml);

    assertXpathExists("/d:Location", xml);
    assertXpathExists("/d:Location/d:City", xml);
    assertXpathExists("/d:Location/d:City/d:PostalCode", xml);
    assertXpathExists("/d:Location/d:City/d:CityName", xml);
    assertXpathExists("/d:Location/d:Country", xml);

    assertXpathEvaluatesTo("33470", "/d:Location/d:City/d:PostalCode/text()", xml);
    assertXpathEvaluatesTo("Duckburg", "/d:Location/d:City/d:CityName/text()", xml);
    assertXpathEvaluatesTo("Calisota", "/d:Location/d:Country/text()", xml);
  }



}
