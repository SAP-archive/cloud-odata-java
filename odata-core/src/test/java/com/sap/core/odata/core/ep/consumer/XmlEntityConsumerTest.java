package com.sap.core.odata.core.ep.consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.entry.EntryMetadata;
import com.sap.core.odata.api.ep.entry.MediaMetadata;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.testutil.mock.MockFacade;

/**
 * @author SAP AG
 */
public class XmlEntityConsumerTest extends AbstractConsumerTest {

  public static final String EMPLOYEE_1_XML =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
          "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/\"  m:etag=\"W/&quot;1&quot;\">" +
          "  <id>http://localhost:19000/Employees('1')</id>" +
          "  <title type=\"text\">Walter Winter</title>" +
          "  <updated>1999-01-01T00:00:00Z</updated>" +
          "  <category term=\"RefScenario.Employee\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" +
          "  <link href=\"Employees('1')\" rel=\"edit\" title=\"Employee\"/>" +
          "  <link href=\"Employees('1')/$value\" rel=\"edit-media\" type=\"application/octet-stream\" etag=\"mmEtag\"/>" +
          "  <link href=\"Employees('1')/ne_Room\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Room\" type=\"application/atom+xml; type=entry\" title=\"ne_Room\"/>" +
          "  <link href=\"Employees('1')/ne_Manager\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Manager\" type=\"application/atom+xml; type=entry\" title=\"ne_Manager\"/>" +
          "  <link href=\"Employees('1')/ne_Team\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/ne_Team\" type=\"application/atom+xml; type=entry\" title=\"ne_Team\"/>" +
          "  <content type=\"application/octet-stream\" src=\"Employees('1')/$value\"/>" +
          "  <m:properties>" +
          "    <d:EmployeeId>1</d:EmployeeId>" +
          "    <d:EmployeeName>Walter Winter</d:EmployeeName>" +
          "    <d:ManagerId>1</d:ManagerId>" +
          "    <d:RoomId>1</d:RoomId>" +
          "    <d:TeamId>1</d:TeamId>" +
          "    <d:Location m:type=\"RefScenario.c_Location\">" +
          "      <d:Country>Germany</d:Country>" +
          "      <d:City m:type=\"RefScenario.c_City\">" +
          "        <d:PostalCode>69124</d:PostalCode>" +
          "        <d:CityName>Heidelberg</d:CityName>" +
          "      </d:City>" +
          "    </d:Location>" +
          "    <d:Age>52</d:Age>" +
          "    <d:EntryDate>1999-01-01T00:00:00</d:EntryDate>" +
          "    <d:ImageUrl>/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg</d:ImageUrl>" +
          "  </m:properties>" +
          "</entry>";

  private static final String ROOM_1_XML =
      "<?xml version='1.0' encoding='UTF-8'?>" +
          "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
          "  <id>http://localhost:19000/test/Rooms('1')</id>" +
          "  <title type=\"text\">Room 1</title>" +
          "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
          "  <category term=\"RefScenario.Room\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" +
          "  <link href=\"Rooms('1')\" rel=\"edit\" title=\"Room\"/>" +
          "  <link href=\"Rooms('1')/nr_Employees\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/nr_Employees\" type=\"application/atom+xml; type=feed\" title=\"nr_Employees\"/>" +
          "  <link href=\"Rooms('1')/nr_Building\" rel=\"http://schemas.microsoft.com/ado/2007/08/dataservices/related/nr_Building\" type=\"application/atom+xml; type=entry\" title=\"nr_Building\"/>" +
          "  <content type=\"application/xml\">" +
          "    <m:properties>" +
          "      <d:Id>1</d:Id>" +
          "    </m:properties>" +
          "  </content>" +
          "</entry>";

  private static final String PHOTO_XML = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
      "<entry m:etag=\"W/&quot;1&quot;\" xml:base=\"http://localhost:19000/test\" " +
      "xmlns=\"http://www.w3.org/2005/Atom\" " +
      "xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
      "xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">" +
      "  <id>http://localhost:19000/test/Container2.Photos(Id=1,Type='image%2Fpng')</id>" +
      "  <title type=\"text\">Photo1</title><updated>2013-01-16T12:57:43Z</updated>" +
      "  <category term=\"RefScenario2.Photo\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" +
      "  <link href=\"Container2.Photos(Id=1,Type='image%2Fpng')\" rel=\"edit\" title=\"Photo\"/>" +
      "  <link href=\"Container2.Photos(Id=1,Type='image%2Fpng')/$value\" rel=\"edit-media\" type=\"image/png\"/>" +
      "  <ру:Содержание xmlns:ру=\"http://localhost\">Образ</ру:Содержание>" +
      "  <content type=\"image/png\" src=\"Container2.Photos(Id=1,Type='image%2Fpng')/$value\"/>" +
      "  <m:properties>" +
      "    <d:Id>1</d:Id>" +
      "    <d:Name>Photo1</d:Name>" +
      "    <d:Type>image/png</d:Type>" +
      "  </m:properties>" +
      "</entry>";

  private static final String PHOTO_XML_INVALID_MAPPING = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
      "<entry m:etag=\"W/&quot;1&quot;\" xml:base=\"http://localhost:19000/test\" " +
      "xmlns=\"http://www.w3.org/2005/Atom\" " +
      "xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
      "xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\">" +
      "  <id>http://localhost:19000/test/Container2.Photos(Id=1,Type='image%2Fpng')</id>" +
      "  <title type=\"text\">Photo1</title><updated>2013-01-16T12:57:43Z</updated>" +
      "  <category term=\"RefScenario2.Photo\" scheme=\"http://schemas.microsoft.com/ado/2007/08/dataservices/scheme\"/>" +
      "  <link href=\"Container2.Photos(Id=1,Type='image%2Fpng')\" rel=\"edit\" title=\"Photo\"/>" +
      "  <link href=\"Container2.Photos(Id=1,Type='image%2Fpng')/$value\" rel=\"edit-media\" type=\"image/png\"/>" +
      "  <ру:Содержание xmlns:ру=\"http://localhost\">Образ</ру:Содержание>" +
      "  <ig:ignore xmlns:ig=\"http://localhost\">ignore</ig:ignore>" + // 406 Bad Request
      "  <content type=\"image/png\" src=\"Container2.Photos(Id=1,Type='image%2Fpng')/$value\"/>" +
      "  <m:properties>" +
      "    <d:Id>1</d:Id>" +
      "    <d:Name>Photo1</d:Name>" +
      "    <d:Type>image/png</d:Type>" +
      "  </m:properties>" +
      "</entry>";
  
  @Test
  public void readWithInlineContent() throws Exception {
    // prepare
    String content = readFile("expanded_team.xml");
    assertNotNull(content);
    
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream reqContent = createContentAsStream(content);
    
    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry entry = xec.readEntry(entitySet, reqContent, false);

    // validate
    assertNotNull(entry);
    Map<String, Object> properties = entry.getProperties();
    assertEquals("1", properties.get("Id"));
    assertEquals("Team 1", properties.get("Name"));
    assertEquals(Boolean.FALSE, properties.get("isScrumTeam"));
  }

  /**
   * OData specification v2: 2.2.6.2.2 Entity Type (as an Atom Entry Element)
   * 
   * @throws Exception
   */
  @Test(expected = EntityProviderException.class)
  public void validationOfWrongPropertiesTagPositionForNoneMediaLinkEntry() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\" />" +
            "  <m:properties>" +
            "    <d:Id>1</d:Id>" +
            "  </m:properties>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    readAndExpectException(entitySet, reqContent, EntityProviderException.INVALID_PARENT_TAG.addContent("content").addContent("properties"));
  }

  /**
   * OData specification v2: 2.2.6.2.2 Entity Type (as an Atom Entry Element)
   * And RFC5023 [section 4.2]
   * 
   * @throws Exception
   */
  @Test(expected = EntityProviderException.class)
  public void validationOfWrongPropertiesTagPositionForMediaLinkEntry() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "  <id>http://localhost:19000/test/Employees('1')</id>" +
            "  <title type=\"text\">Walter Winter</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:EmployeeId>1</d:EmployeeId>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    readAndExpectException(entitySet, reqContent, EntityProviderException.INVALID_PARENT_TAG.addContent("properties").addContent("content"));
  }

  @Test
  public void validationOfNamespacesSuccess() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Id>1</d:Id>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, true);
    assertNotNull(result);
  }

  @Test(expected = EntityProviderException.class)
  public void validationOfNamespacesMissingXmlns() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry etag=\"W/&quot;1&quot;\">" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Id>1</d:Id>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    readAndExpectException(entitySet, reqContent, EntityProviderException.INVALID_NAMESPACE.addContent(Edm.NAMESPACE_D_2007_08));
  }

  @Test(expected = EntityProviderException.class)
  public void validationOfNamespacesMissingM_NamespaceAtProperties() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <properties>" +
            "      <d:Id>1</d:Id>" +
            "    </properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    readAndExpectException(entitySet, reqContent, EntityProviderException.INVALID_NAMESPACE.addContent("properties"));
  }

  @Test(expected = EntityProviderException.class)
  public void validationOfNamespacesMissingD_NamespaceAtTag() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <Id>1</Id>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    readAndExpectException(entitySet, reqContent, EntityProviderException.INVALID_NAMESPACE.addContent("Id"));
  }

  private void readAndExpectException(EdmEntitySet entitySet, InputStream reqContent, MessageReference messageReference) throws ODataMessageException {
    readAndExpectException(entitySet, reqContent, true, messageReference);
  }
  
  private void readAndExpectException(EdmEntitySet entitySet, InputStream reqContent, boolean merge, MessageReference messageReference) throws ODataMessageException {
    try {
      XmlEntityConsumer xec = new XmlEntityConsumer();
      ODataEntry result = xec.readEntry(entitySet, reqContent, merge);
      assertNotNull(result);
      Assert.fail("Expected exception with MessageReference '" + messageReference.getKey() + "' was not thrown.");
    } catch (ODataMessageException e) {
      assertEquals(messageReference.getKey(), e.getMessageReference().getKey());
      assertEquals(messageReference.getContent(), e.getMessageReference().getContent());
      throw e;
    }
  }

  @Test
  public void readEntryAtomProperties() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream contentBody = createContentAsStream(EMPLOYEE_1_XML);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, contentBody, true);

    // verify
    EntryMetadata metadata = result.getMetadata();
    assertEquals("http://localhost:19000/Employees('1')", metadata.getId());
    assertEquals("W/\"1\"", metadata.getEtag());
    List<String> associationUris = metadata.getAssociationUris("ne_Room");
    assertEquals(1, associationUris.size());
    assertEquals("Employees('1')/ne_Room", associationUris.get(0));
    associationUris = metadata.getAssociationUris("ne_Manager");
    assertEquals(1, associationUris.size());
    assertEquals("Employees('1')/ne_Manager", associationUris.get(0));
    associationUris = metadata.getAssociationUris("ne_Team");
    assertEquals(1, associationUris.size());
    assertEquals("Employees('1')/ne_Team", associationUris.get(0));

    assertEquals(null, metadata.getUri());

    MediaMetadata mm = result.getMediaMetadata();
    assertEquals("Employees('1')/$value", mm.getSourceLink());
    assertEquals("mmEtag", mm.getEtag());
    assertEquals("application/octet-stream", mm.getContentType());
    assertEquals("Employees('1')/$value", mm.getEditLink());

    Map<String, Object> data = result.getProperties();
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
    InputStream contentBody = createContentAsStream(EMPLOYEE_1_XML);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, contentBody, true);

    // verify
    List<String> associationUris = result.getMetadata().getAssociationUris("ne_Room");
    assertEquals(1, associationUris.size());
    assertEquals("Employees('1')/ne_Room", associationUris.get(0));
    associationUris = result.getMetadata().getAssociationUris("ne_Manager");
    assertEquals(1, associationUris.size());
    assertEquals("Employees('1')/ne_Manager", associationUris.get(0));
    associationUris = result.getMetadata().getAssociationUris("ne_Team");
    assertEquals(1, associationUris.size());
    assertEquals("Employees('1')/ne_Team", associationUris.get(0));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadEntry() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream contentBody = createContentAsStream(EMPLOYEE_1_XML);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    boolean merge = false;
    ODataEntry result = xec.readEntry(entitySet, contentBody, merge);

    // verify
    Map<String, Object> properties = result.getProperties();
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

  /**
   * Missing 'key' properties are allowed for validation against Edm model.
   * @throws Exception
   */
  @Test
  @SuppressWarnings("unchecked")
  public void testReadEntryMissingKeyProperty() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream contentBody = createContentAsStream(EMPLOYEE_1_XML.replace("<d:EmployeeId>1</d:EmployeeId>", ""));

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    boolean merge = false;
    ODataEntry result = xec.readEntry(entitySet, contentBody, merge);

    // verify
    Map<String, Object> properties = result.getProperties();
    assertEquals(8, properties.size());

    assertNull(properties.get("EmployeeId"));
//    assertEquals("1", properties.get("EmployeeId"));
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
    Calendar entryDate = (Calendar) properties.get("EntryDate");
    assertEquals(Long.valueOf(915148800000l), Long.valueOf(entryDate.getTimeInMillis()));
    assertEquals(TimeZone.getTimeZone("GMT"), entryDate.getTimeZone());
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", properties.get("ImageUrl"));
  }

  @Test(expected = EntityProviderException.class)
  public void testReadEntryMissingProperty() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    String content = EMPLOYEE_1_XML.replace("<d:Age>52</d:Age>", "");
    InputStream contentBody = createContentAsStream(content);

    // execute
    try {
      XmlEntityConsumer xec = new XmlEntityConsumer();
      ODataEntry result = xec.readEntry(entitySet, contentBody, false);

      // verify - not necessary because of thrown exception - but kept to prevent eclipse warning about unused variables
      Map<String, Object> properties = result.getProperties();
      assertEquals(9, properties.size());
    } catch (EntityProviderException e) {
      // do some assertions...
      assertEquals(EntityProviderException.MISSING_PROPERTY.getKey(), e.getMessageReference().getKey());
      assertEquals("Age", e.getMessageReference().getContent().get(0));
      // ...and then re-throw
      throw e;
    }
  }

  @Test(expected = EntityProviderException.class)
  public void testReadEntryTooManyValues() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    String content = EMPLOYEE_1_XML.replace("<d:Age>52</d:Age>", "<d:Age>52</d:Age><d:SomeUnknownTag>SomeUnknownValue</d:SomeUnknownTag>");
    InputStream contentBody = createContentAsStream(content);

    // execute
    try {
      XmlEntityConsumer xec = new XmlEntityConsumer();
      ODataEntry result = xec.readEntry(entitySet, contentBody, false);

      // verify - not necessary because of thrown exception - but kept to prevent eclipse warning about unused variables
      Map<String, Object> properties = result.getProperties();
      assertEquals(9, properties.size());
    } catch (EntityProviderException e) {
      // do some assertions...
      assertEquals(EntityProviderException.INVALID_PROPERTY.getKey(), e.getMessageReference().getKey());
      assertEquals("SomeUnknownTag", e.getMessageReference().getContent().get(0));
      // ...and then re-throw
      throw e;
    }
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadEntryWithMerge() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    String content = EMPLOYEE_1_XML.replace("<d:Age>52</d:Age>", "");
    InputStream contentBody = createContentAsStream(content);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, contentBody, true);

    // verify
    Map<String, Object> properties = result.getProperties();
    assertEquals(8, properties.size());

    // removed property
    assertNull(properties.get("Age"));

    // available properties
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
    Calendar entryDate = (Calendar) properties.get("EntryDate");
    assertEquals(Long.valueOf(915148800000l), Long.valueOf(entryDate.getTimeInMillis()));
    assertEquals(TimeZone.getTimeZone("GMT"), entryDate.getTimeZone());
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", properties.get("ImageUrl"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadEntryWithMergeAndMappings() throws Exception {
    // prepare
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    String content = EMPLOYEE_1_XML.replace("<d:Age>52</d:Age>", "");
    InputStream contentBody = createContentAsStream(content);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, contentBody, true, 
        createTypeMappings("Age", Long.class, // test unused type mapping 
                            "EntryDate", Date.class));

    // verify
    Map<String, Object> properties = result.getProperties();
    assertEquals(8, properties.size());

    // removed property
    assertNull(properties.get("Age"));

    // available properties
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
    assertEquals(new Date(915148800000l), properties.get("EntryDate"));
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", properties.get("ImageUrl"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadEntryRequest() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = createContentAsStream(EMPLOYEE_1_XML);
    ODataEntry result = xec.readEntry(entitySet, content, true);

    // verify
    Map<String, Object> properties = result.getProperties();
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
  public void testReadEntryRequestNullMapping() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = createContentAsStream(EMPLOYEE_1_XML);
    ODataEntry result = xec.readEntry(entitySet, content, true, null);

    // verify
    Map<String, Object> properties = result.getProperties();
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
    Calendar entryDate = (Calendar) properties.get("EntryDate");
    assertEquals(Long.valueOf(915148800000l), Long.valueOf(entryDate.getTimeInMillis()));
    assertEquals(TimeZone.getTimeZone("GMT"), entryDate.getTimeZone());
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", properties.get("ImageUrl"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadEntryRequestEmptyMapping() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = createContentAsStream(EMPLOYEE_1_XML);
    ODataEntry result = xec.readEntry(entitySet, content, true, new HashMap<String, Object>());

    // verify
    Map<String, Object> properties = result.getProperties();
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
    Calendar entryDate = (Calendar) properties.get("EntryDate");
    assertEquals(Long.valueOf(915148800000l), Long.valueOf(entryDate.getTimeInMillis()));
    assertEquals(TimeZone.getTimeZone("GMT"), entryDate.getTimeZone());
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", properties.get("ImageUrl"));
  }

  @Test(expected=EntityProviderException.class)
  public void testReadEntryRequestInvalidMapping() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = createContentAsStream(EMPLOYEE_1_XML);
    ODataEntry result = xec.readEntry(entitySet, content, true, createTypeMappings("EmployeeName", Integer.class));

    // verify
    Map<String, Object> properties = result.getProperties();
    assertEquals(9, properties.size());
  }

  @Test
  public void testReadEntryRequestObjectMapping() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = createContentAsStream(EMPLOYEE_1_XML);
    ODataEntry result = xec.readEntry(entitySet, content, true, createTypeMappings("EmployeeName", Object.class));

    // verify
    Map<String, Object> properties = result.getProperties();
    assertEquals(9, properties.size());

    assertEquals("1", properties.get("EmployeeId"));
    Object o = properties.get("EmployeeName");
    assertTrue(o instanceof String);
    assertEquals("Walter Winter", properties.get("EmployeeName"));
    assertEquals("1", properties.get("ManagerId"));
    assertEquals("1", properties.get("RoomId"));
    assertEquals("1", properties.get("TeamId"));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void testReadEntryRequestWithMapping() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = createContentAsStream(EMPLOYEE_1_XML);
    ODataEntry result = xec.readEntry(entitySet, content, true, 
        createTypeMappings("Age", Short.class, 
                            "Heidelberg", String.class, 
                            "EntryDate", Long.class));

    // verify
    Map<String, Object> properties = result.getProperties();
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
    assertEquals(Short.valueOf("52"), properties.get("Age"));
    assertEquals(Long.valueOf(915148800000l), properties.get("EntryDate"));
    assertEquals("/SAP/PUBLIC/BC/NWDEMO_MODEL/IMAGES/male_1_WinterW.jpg", properties.get("ImageUrl"));
  }

  @Test
  public void readCustomizableFeedMappings() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos");
    InputStream reqContent = createContentAsStream(PHOTO_XML);
    ODataEntry result = xec.readEntry(entitySet, reqContent, false);

    // verify
    EntryMetadata entryMetadata = result.getMetadata();
    assertEquals("http://localhost:19000/test/Container2.Photos(Id=1,Type='image%2Fpng')", entryMetadata.getId());

    Map<String, Object> data = result.getProperties();
    assertEquals("Образ", data.get("Содержание"));
    assertEquals("Photo1", data.get("Name"));
    assertEquals("image/png", data.get("Type"));
    assertNull(data.get("ignore"));
  }

  @Test
  public void readCustomizableFeedMappingsWithMergeSemantic() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos");
    InputStream reqContent = createContentAsStream(PHOTO_XML);
    ODataEntry result = xec.readEntry(entitySet, reqContent, true);

    // verify
    EntryMetadata entryMetadata = result.getMetadata();
    assertEquals("http://localhost:19000/test/Container2.Photos(Id=1,Type='image%2Fpng')", entryMetadata.getId());

    Map<String, Object> data = result.getProperties();
    assertEquals("Photo1", data.get("Name"));
    assertEquals("image/png", data.get("Type"));
    // ignored customizable feed mapping
    assertNull(data.get("Содержание"));
    assertNull(data.get("ignore"));
  }

  @Test(expected = EntityProviderException.class)
  public void readCustomizableFeedMappingsBadRequest() throws Exception {
    EdmEntitySet entitySet = MockFacade.getMockEdm().getEntityContainer("Container2").getEntitySet("Photos");
    InputStream reqContent = createContentAsStream(PHOTO_XML_INVALID_MAPPING);

    readAndExpectException(entitySet, reqContent, false, EntityProviderException.INVALID_PROPERTY.addContent("ignore"));
  }

  @Test
  public void testReadEntryRooms() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(ROOM_1_XML);
    ODataEntry result = xec.readEntry(entitySet, reqContent, true);

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

    Map<String, Object> properties = result.getProperties();
    assertEquals(1, properties.size());
    assertEquals("1", properties.get("Id"));
  }

  @Test
  public void testReadProperty() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");

    String xml = "<Age>67</Age>";
    InputStream content = createContentAsStream(xml);
    Map<String, Object> value = xec.readProperty(property, content, true);

    assertEquals(Integer.valueOf(67), value.get("Age"));
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

  @Test
  public void testReadIntegerPropertyAsLong() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("Age");

    String xml = "<Age>42</Age>";
    InputStream content = createContentAsStream(xml);
    Map<String, Object> value = xec.readProperty(property, content, true, createTypeMappings("Age", Long.class));

    assertEquals(Long.valueOf(42), value.get("Age"));
  }

  @Test(expected=EntityProviderException.class)
  public void readStringPropertyValueWithInvalidMapping() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    String xml = "<EmployeeName>Max Mustermann</EmployeeName>";
    InputStream content = createContentAsStream(xml);
    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    EdmProperty property = (EdmProperty) entitySet.getEntityType().getProperty("EmployeeName");

    Object result = xec.readPropertyValue(property, content, Integer.class);

    assertEquals("Max Mustermann", result);
  }


  private InputStream createContentAsStream(final String xml) throws UnsupportedEncodingException {
    InputStream content = new ByteArrayInputStream(xml.getBytes("utf-8"));
    return content;
  }

}
