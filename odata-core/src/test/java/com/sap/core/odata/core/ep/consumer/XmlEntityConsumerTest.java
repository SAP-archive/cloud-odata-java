package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntryMetadata;
import com.sap.core.odata.api.ep.MediaMetadata;
import com.sap.core.odata.api.ep.ReadEntryResult;
import com.sap.core.odata.testutil.fit.BaseTest;
import com.sap.core.odata.testutil.mock.MockFacade;

public class XmlEntityConsumerTest extends BaseTest {

  public static final String EMPLOYEE_1_XML = 
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/\"  m:etag=\"W/&quot;1&quot;\">" + 
      "<id>https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Employees('1')</id>" +
      "<title type=\"text\">Walter Winter</title>" + 
      "<updated>1999-01-01T00:00:00Z</updated>" + 
      "<category term=\"RefScenario.Employee\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" + 
      "<link href=\"Employees('1')\" rel=\"edit\" title=\"Employee\"/>" +
      "<link href=\"Employees('1')/$value\" rel=\"edit-media\" type=\"application/octet-stream\" etag=\"mmEtag\"/>" + 
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

//  private static final String ROOM_1_XML = "<?xml version='1.0' encoding='UTF-8'?><entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\"><id>http://localhost:19000/test/Rooms('1')</id><title type=\"text\">Room 1</title><updated>2013-01-11T13:50:50.541+01:00</updated><category term=\"RefScenario.Room\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/><link href=\"Rooms('1')\" rel=\"edit\" title=\"Room\"/><link href=\"Rooms('1')/nr_Employees\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/nr_Employees\" type=\"application/atom+xml; type=feed\" title=\"nr_Employees\"/><link href=\"Rooms('1')/nr_Building\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/nr_Building\" type=\"application/atom+xml; type=entry\" title=\"nr_Building\"/><content type=\"application/xml\"><m:properties><d:Id>1</d:Id><d:Name>Room 1</d:Name><d:Seats>1</d:Seats><d:Version>1</d:Version></m:properties></content></entry>";
  private static final String ROOM_1_XML = 
      "<?xml version='1.0' encoding='UTF-8'?>" +
      "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
        "<id>http://localhost:19000/test/Rooms('1')</id>" +
        "<title type=\"text\">Room 1</title>" +
        "<updated>2013-01-11T13:50:50.541+01:00</updated>" +
        "<category term=\"RefScenario.Room\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" +
        "<link href=\"Rooms('1')\" rel=\"edit\" title=\"Room\"/>" +
        "<link href=\"Rooms('1')/nr_Employees\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/nr_Employees\" type=\"application/atom+xml; type=feed\" title=\"nr_Employees\"/>" +
        "<link href=\"Rooms('1')/nr_Building\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/nr_Building\" type=\"application/atom+xml; type=entry\" title=\"nr_Building\"/>" +
        "<content type=\"application/xml\">" +
          "<m:properties>" +
            "<d:Id>1</d:Id>" +
          "</m:properties>" +
        "</content>" +
      "</entry>";
  
  @Test
  public void readEntryAtomProperties() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream contentBody = new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("utf-8"));
    
    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ReadEntryResult result = xec.readEntry(entitySet, contentBody);

    // verify
    EntryMetadata metadata = result.getMetadata();
    assertEquals("https://refodata.prod.jpaas.sapbydesign.com/com.sap.core.odata.ref.web/ReferenceScenario.svc/Employees('1')", metadata.getId());
    assertEquals("W/\"1\"", metadata.getEtag());
    Map<String, String> associationUris = metadata.getAssociationUris();
    assertEquals(3, associationUris.size());
    assertEquals("Employees('1')/ne_Room", associationUris.get("ne_Room"));
    assertEquals("Employees('1')/ne_Manager", associationUris.get("ne_Manager"));
    assertEquals("Employees('1')/ne_Team", associationUris.get("ne_Team"));

    assertEquals(null, metadata.getUri());

    MediaMetadata mm = result.getMediaMetadata();
    assertEquals("Employees('1')/$value", mm.getSourceLink());
    assertEquals("mmEtag", mm.getEtag());
    assertEquals("application/octet-stream", mm.getContentType());
    assertEquals("Employees('1')/$value", mm.getEditLink());
    
    Map<String, Object> data = result.getData();
    assertEquals(9, data.size());
    assertEquals("1", data.get("EmployeeId"));
    assertEquals("Walter Winter", data.get("EmployeeName"));
    assertEquals("1", data.get("ManagerId"));
    assertEquals("1", data.get("RoomId"));
    assertEquals("1", data.get("TeamId"));
  }

  
  
  @Test
  public void readEntryLinks() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream contentBody = new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("utf-8"));
    
    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ReadEntryResult result = xec.readEntry(entitySet, contentBody);

    // verify
    Map<String, String> associationUris = result.getMetadata().getAssociationUris();
    assertEquals(3, associationUris.size());
    assertEquals("Employees('1')/ne_Room", associationUris.get("ne_Room"));
    assertEquals("Employees('1')/ne_Manager", associationUris.get("ne_Manager"));
    assertEquals("Employees('1')/ne_Team", associationUris.get("ne_Team"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadEntry() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream contentBody = new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("utf-8"));
    
    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ReadEntryResult result = xec.readEntry(entitySet, contentBody);

    
    // verify
    Map<String, Object> properties = result.getData();
    assertEquals(9, properties.size());

    assertEquals("1", properties.get("EmployeeId"));
    assertEquals("Walter Winter", properties.get("EmployeeName"));
    assertEquals("1", properties.get("ManagerId"));
    assertEquals("1", properties.get("RoomId"));
    assertEquals("1", properties.get("TeamId"));
    Map<String, Object> location = (Map<String, Object>) properties.get("Location");
    assertEquals(2, location.size());
    assertEquals("Germany", location.get("Country"));
    Map<String, Object> city = (Map<String, Object>) location.get("City");
    assertEquals(2, city.size());
    assertEquals("69124", city.get("PostalCode"));
    assertEquals("Heidelberg", city.get("CityName"));
    assertEquals(Integer.valueOf(52), properties.get("Age"));
//    System.out.println(((Calendar)result.get("EntryDate")).getTimeInMillis());
//    //"1999-01-01T00:00:00"
//    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//    cal.set(1999, 0, 1, 0, 0, 0);
//    cal.setTimeInMillis(915148800000l);
//    System.out.println(cal);
//    System.out.println(result.get("EntryDate"));
    Calendar entryDate = (Calendar) properties.get("EntryDate");
    assertEquals(Long.valueOf(915148800000l), Long.valueOf(entryDate.getTimeInMillis()));
    assertEquals(TimeZone.getTimeZone("GMT"), entryDate.getTimeZone());
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", properties.get("ImageUrl"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadEntryRequest() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = new ByteArrayInputStream(EMPLOYEE_1_XML.getBytes("utf-8"));
    ReadEntryResult result = xec.readEntry(entitySet, content);

    // verify
    Map<String, Object> properties = result.getData();
    assertEquals(9, properties.size());

    assertEquals("1", properties.get("EmployeeId"));
    assertEquals("Walter Winter", properties.get("EmployeeName"));
    assertEquals("1", properties.get("ManagerId"));
    assertEquals("1", properties.get("RoomId"));
    assertEquals("1", properties.get("TeamId"));
    Map<String, Object> location = (Map<String, Object>) properties.get("Location");
    assertEquals(2, location.size());
    assertEquals("Germany", location.get("Country"));
    Map<String, Object> city = (Map<String, Object>) location.get("City");
    assertEquals(2, city.size());
    assertEquals("69124", city.get("PostalCode"));
    assertEquals("Heidelberg", city.get("CityName"));
    assertEquals(Integer.valueOf(52), properties.get("Age"));
//    System.out.println(((Calendar)result.get("EntryDate")).getTimeInMillis());
//    //"1999-01-01T00:00:00"
//    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
//    cal.set(1999, 0, 1, 0, 0, 0);
//    cal.setTimeInMillis(915148800000l);
//    System.out.println(cal);
//    System.out.println(result.get("EntryDate"));
    Calendar entryDate = (Calendar) properties.get("EntryDate");
    assertEquals(Long.valueOf(915148800000l), Long.valueOf(entryDate.getTimeInMillis()));
    assertEquals(TimeZone.getTimeZone("GMT"), entryDate.getTimeZone());
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", properties.get("ImageUrl"));
  }

  @Test
  public void testReadEntryRooms() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = new ByteArrayInputStream(ROOM_1_XML.getBytes("utf-8"));
    ReadEntryResult result = xec.readEntry(entitySet, reqContent);

    // verify
    EntryMetadata entryMetadata = result.getMetadata();
    assertEquals("http://localhost:19000/test/Rooms('1')", entryMetadata.getId());
    assertEquals("W/\"1\"", entryMetadata.getEtag());
    assertEquals(null, entryMetadata.getUri());

    MediaMetadata mediaMetadata = result.getMediaMetadata();
    assertEquals("application/xml", mediaMetadata.getContentType());
    assertEquals(null, mediaMetadata.getSourceLink());
    assertEquals(null, mediaMetadata.getEditLink());
    assertEquals(null, mediaMetadata.getEtag());
    
    Map<String, Object> properties = result.getData();
    assertEquals(1, properties.size());
    assertEquals("1", properties.get("Id"));
  }

  @Test
  public void testReadProperty() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");

    String xml = "<Age>67</Age>";
    InputStream content = new ByteArrayInputStream(xml.getBytes("utf-8"));
    Map<String, Object> value = xec.readProperty(property, content);

    assertEquals(Integer.valueOf(67), value.get("Age"));

//    Object value = xec.readProperty(property, request);
//    assertEquals(Integer.valueOf(67), value);
  }
  
  @Test
  public void readStringPropertyValue() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();
    
    String xml = "<EmployeeName>Max Mustermann</EmployeeName>";
    InputStream content = createContentAsStream(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("EmployeeName");
    
    Object result = xec.readPropertyValue(property, content);

    assertEquals("Max Mustermann", result);
  }
  
  private InputStream createContentAsStream(String xml) throws UnsupportedEncodingException {
    InputStream content = new ByteArrayInputStream(xml.getBytes("utf-8"));
    return content;
  }
}
