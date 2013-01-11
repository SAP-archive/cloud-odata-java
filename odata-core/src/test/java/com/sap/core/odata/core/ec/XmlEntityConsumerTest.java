package com.sap.core.odata.core.ec;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.processor.ODataRequest;
import com.sap.core.odata.core.ODataRequestImpl;
import com.sap.core.odata.testutil.mock.MockFacade;

public class XmlEntityConsumerTest {

  public static final String EMPLOYEE_1_XML = 
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/\">" + 
      "<id>https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Employees('1')</id>" +
      "<title type=\"text\">Walter Winter</title>" + 
      "<updated>1999-01-01T00:00:00Z</updated>" + 
      "<category term=\"RefScenario.Employee\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" + 
      "<link href=\"Employees('1')\" rel=\"edit\" title=\"Employee\"/><link href=\"Employees('1')/$value\" rel=\"edit-media\" type=\"application/octet-stream\"/>" + 
      "<link href=\"Employees('1')/ne_Room\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Room\" type=\"application/atom+xml; type=entry\" title=\"ne_Room\"/>" + 
      "<link href=\"Employees('1')/ne_Manager\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Manager\" type=\"application/atom+xml; type=entry\" title=\"ne_Manager\"/>" + 
      "<link href=\"Employees('1')/ne_Team\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Team\" type=\"application/atom+xml; type=entry\" title=\"ne_Team\"/>" + 
      "<content type=\"application/octet-stream\" src=\"Employees('1')/$value\"/>" + 
      "<m:properties>" + 
        "<d:EmployeeId>1</d:EmployeeId>" + 
        "<d:EmployeeName>Walter Winter</d:EmployeeName>" + 
        "<d:ManagerId>1</d:ManagerId>" + 
        "<d:RoomId>1</d:RoomId>" + 
        "<d:TeamId>1</d:TeamId>" + 
        "<d:Location m:type=\"RefScenario.c_Location\">" + 
          "<d:Country>Germany</d:Country>" + 
          "<d:City m:type=\"RefScenario.c_City\">" + 
            "<d:PostalCode>69124</d:PostalCode>" + 
            "<d:CityName>Heidelberg</d:CityName>" + 
          "</d:City>" + 
        "</d:Location>" + 
        "<d:Age>52</d:Age>" + 
        "<d:EntryDate>1999-01-01T00:00:00</d:EntryDate>" + 
        "<d:ImageUrl>/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg</d:ImageUrl>" + 
      "</m:properties>" + 
    "</entry>";
  
  
  @Test
  public void testReadEntry() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream contentBody = new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("utf-8"));
    String contentType = "application/xml";
    ODataRequest request = ODataRequestImpl.create(contentBody, contentType).build();

    
    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    Map<String, Object> result = xec.readEntry(entitySet, request);

    
    // verify
    assertEquals(11, result.size());

    assertEquals("1", result.get("EmployeeId"));
    assertEquals("Walter Winter", result.get("EmployeeName"));
    assertEquals("1", result.get("ManagerId"));
    assertEquals("1", result.get("RoomId"));
    assertEquals("1", result.get("TeamId"));
    assertEquals("Germany", result.get("Country"));
    assertEquals("69124", result.get("PostalCode"));
    assertEquals("Heidelberg", result.get("CityName"));
    assertEquals("52", result.get("Age"));
    assertEquals("1999-01-01T00:00:00", result.get("EntryDate"));
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", result.get("ImageUrl"));
  }

  @Test
  public void testReadEntryRequest() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    
    EdmEntitySet edmEntitySet = null;
    String contentType = "application/xml";
    ODataRequest request = ODataRequestImpl.create(new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("utf-8")), contentType).build();
    Map<String, Object> result = xec.readEntry(edmEntitySet, request);

    assertEquals(11, result.size());

    assertEquals("1", result.get("EmployeeId"));
    assertEquals("Walter Winter", result.get("EmployeeName"));
    assertEquals("1", result.get("ManagerId"));
    assertEquals("1", result.get("RoomId"));
    assertEquals("1", result.get("TeamId"));
    assertEquals("Germany", result.get("Country"));
    assertEquals("69124", result.get("PostalCode"));
    assertEquals("Heidelberg", result.get("CityName"));
    assertEquals("52", result.get("Age"));
    assertEquals("1999-01-01T00:00:00", result.get("EntryDate"));
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", result.get("ImageUrl"));
  }
  
  @Test
  public void testReadProperty() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    
    String contentType = "application/xml";
    String xml = "<Age>67</Age>";
    ODataRequest request = ODataRequestImpl.create(new ByteArrayInputStream(xml.getBytes("utf-8")), contentType).build();
    
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");

    Map<String, Object> value = xec.readProperty(property, request);
    assertEquals(Integer.valueOf(67), value.get("Age"));

//    Object value = xec.readProperty(property, request);
//    assertEquals(Integer.valueOf(67), value);
  }
}
