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
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntitySet;
import com.sap.core.odata.api.edm.EdmProperty;
import com.sap.core.odata.api.ep.EntityProviderException;
import com.sap.core.odata.api.ep.EntityProviderReadProperties;
import com.sap.core.odata.api.ep.callback.OnReadEntryContent;
import com.sap.core.odata.api.ep.callback.ReadCallbackResult;
import com.sap.core.odata.api.ep.callback.ReadEntryCallbackContext;
import com.sap.core.odata.api.ep.entry.EntryMetadata;
import com.sap.core.odata.api.ep.entry.MediaMetadata;
import com.sap.core.odata.api.ep.entry.ODataEntry;
import com.sap.core.odata.api.exception.MessageReference;
import com.sap.core.odata.api.exception.ODataException;
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
          "  <link href=\"Employees('1')/$value\" rel=\"edit-media\" type=\"application/octet-stream\" m:etag=\"mmEtag\"/>" +
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

  /**
   * http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata/Teams('1')?$expand=nt_Employees
   * 
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  @Test
  public void readWithInlineContent() throws Exception {
    // prepare
    String content = readFile("expanded_team.xml");
    assertNotNull(content);

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream reqContent = createContentAsStream(content);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    EntityProviderReadProperties consumerProperties = EntityProviderReadProperties.init()
        .mergeSemantic(false)
        .callback(new OnReadEntryContent() {
      @Override
      public ReadCallbackResult retrieveReadResult(final ReadEntryCallbackContext context) {
        try {
          String title = context.getTitle();
          if (title.contains("Employees")) {
            EdmEntitySet employeeEntitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
            return new ReadCallbackResult(context.getReadProperties(), employeeEntitySet);
          } else {
            throw new RuntimeException("Invalid title");
          }
        } catch (ODataException e) {
          throw new RuntimeException(e);
        }
      }
    }).build();

    ODataEntry entry = xec.readEntry(entitySet, reqContent, consumerProperties);
    // validate
    assertNotNull(entry);
    Map<String, Object> properties = entry.getProperties();
    assertEquals("1", properties.get("Id"));
    assertEquals("Team 1", properties.get("Name"));
    assertEquals(Boolean.FALSE, properties.get("isScrumTeam"));
    //
    List<Object> employees = (List<Object>) properties.get("nt_Employees");
    assertEquals(3, employees.size());
    //
    ODataEntry employeeNo2 = (ODataEntry) employees.get(1);
    Map<String, Object> employessNo2Props = employeeNo2.getProperties();
    assertEquals("Frederic Fall", employessNo2Props.get("EmployeeName"));
    assertEquals("2", employessNo2Props.get("RoomId"));
    assertEquals(32, employessNo2Props.get("Age"));
    Map<String, Object> emp2Location = (Map<String, Object>) employessNo2Props.get("Location");
    Map<String, Object> emp2City = (Map<String, Object>) emp2Location.get("City");
    assertEquals("69190", emp2City.get("PostalCode"));
    assertEquals("Walldorf", emp2City.get("CityName"));
  }

  /**
   * http://ldcigmd.wdf.sap.corp:50055/sap/bc/odata/Teams('1')?$expand=nt_Employees,nt_Employees/ne_Team
   * 
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  @Test
  public void readWithDoubleInlineContent() throws Exception {
    // prepare
    String content = readFile("double_expanded_team.xml");
    assertNotNull(content);

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream reqContent = createContentAsStream(content);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    EntityProviderReadProperties consumerProperties = EntityProviderReadProperties.init()
        .mergeSemantic(false)
        .callback(new OnReadEntryContent() {
      @Override
      public ReadCallbackResult retrieveReadResult(final ReadEntryCallbackContext context) {
        try {
          String title = context.getTitle();
          if (title.contains("Employees")) {
            EdmEntitySet employeeEntitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
            return new ReadCallbackResult(context.getReadProperties(), employeeEntitySet);
          } else if (title.contains("Team")) {
            EdmEntitySet teamsEntitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
            return new ReadCallbackResult(context.getReadProperties(), teamsEntitySet);
          } else {
            throw new RuntimeException("Invalid title");
          }
        } catch (Exception e) {
          throw new RuntimeException();
        }
      }
    }).build();

    ODataEntry entry = xec.readEntry(entitySet, reqContent, consumerProperties);
    // validate
    assertNotNull(entry);
    Map<String, Object> properties = entry.getProperties();
    assertEquals("1", properties.get("Id"));
    assertEquals("Team 1", properties.get("Name"));
    assertEquals(Boolean.FALSE, properties.get("isScrumTeam"));
    //
    List<Object> employees = (List<Object>) properties.get("nt_Employees");
    assertEquals(3, employees.size());
    //
    ODataEntry employeeNo2 = (ODataEntry) employees.get(1);
    Map<String, Object> employessNo2Props = employeeNo2.getProperties();
    assertEquals("Frederic Fall", employessNo2Props.get("EmployeeName"));
    assertEquals("2", employessNo2Props.get("RoomId"));
    assertEquals(32, employessNo2Props.get("Age"));
    Map<String, Object> emp2Location = (Map<String, Object>) employessNo2Props.get("Location");
    Map<String, Object> emp2City = (Map<String, Object>) emp2Location.get("City");
    assertEquals("69190", emp2City.get("PostalCode"));
    assertEquals("Walldorf", emp2City.get("CityName"));

    List<Object> inlinedTeamEmployeeNo2 = (List<Object>) employessNo2Props.get("ne_Team");
    assertEquals(1, inlinedTeamEmployeeNo2.size());
    //
    ODataEntry inlinedTeam = (ODataEntry) inlinedTeamEmployeeNo2.get(0);
    assertEquals("1", inlinedTeam.getProperties().get("Id"));
    assertEquals("Team 1", inlinedTeam.getProperties().get("Name"));
  }

  @Test
  public void readWithInlineContentIgnored() throws Exception {
    // prepare
    String content = readFile("expanded_team.xml");
    assertNotNull(content);

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Teams");
    InputStream reqContent = createContentAsStream(content);

    // execute
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry entry = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());

    // validate
    assertNotNull(entry);
    Map<String, Object> properties = entry.getProperties();
    assertEquals("1", properties.get("Id"));
    assertEquals("Team 1", properties.get("Name"));
    assertEquals(Boolean.FALSE, properties.get("isScrumTeam"));
  }

  /**
   * We only support <code>UTF-8</code> as character encoding.
   * 
   * @throws Exception
   */
  @Test(expected = EntityProviderException.class)
  public void validationOfWrongXmlEncodingUtf32() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-32'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    readAndExpectException(entitySet, reqContent, EntityProviderException.UNSUPPORTED_CHARACTER_ENCODING.addContent("UTF-32"));
  }

  /**
   * We only support <code>UTF-8</code> as character encoding.
   * 
   * @throws Exception
   */
  @Test(expected = EntityProviderException.class)
  public void validationOfWrongXmlEncodingIso8859_1() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='iso-8859-1'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    readAndExpectException(entitySet, reqContent, EntityProviderException.UNSUPPORTED_CHARACTER_ENCODING.addContent("iso-8859-1"));
  }

  /**
   * Character encodings are case insensitive.
   * Hence <code>uTf-8</code> should work as well as <code>UTF-8</code>.
   * 
   * @throws Exception
   */
  @Test
  public void validationCaseInsensitiveXmlEncodingUtf8() throws Exception {
    String room =
        "<?xml version='1.0' encoding='uTf-8'?>" +
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
    InputStream reqContent = createContentAsStream(room);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(true).build());

    assertNotNull(result);
    assertEquals("1", result.getProperties().get("Id"));
  }

  /**
   * For none media resource if <code>properties</code> tag is not within <code>content</code> tag it results in an exception.
   * 
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
   * For media resource if <code>properties</code> tag is within <code>content</code> tag it results in an exception.
   * 
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
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(true).build());
    assertNotNull(result);
  }

  /**
   * Use different namespace prefixes for <code>metadata (m)</code> and <code>data (d)</code>.
   * 
   * @throws Exception
   */
  @Test
  public void validationOfDifferentNamespacesPrefixSuccess() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" " +
            "    xmlns:meta=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
            "    xmlns:data=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" " +
            "    xml:base=\"http://localhost:19000/test/\" " +
            "    meta:etag=\"W/&quot;1&quot;\">" +
            "" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <meta:properties>" +
            "      <data:Id>1</data:Id>" +
            "      <data:Seats>11</data:Seats>" +
            "      <data:Name>Room 42</data:Name>" +
            "      <data:Version>4711</data:Version>" +
            "    </meta:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());
    assertNotNull(result);
  }

  /**
   * Add <code>unknown property</code> in own namespace which is defined in entry tag.
   *  
   * @throws Exception
   */
  @Test
  public void validationOfUnknownPropertyOwnNamespaceSuccess() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" " +
            "    xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
            "    xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" " +
            "    xmlns:more=\"http://sample.com/more\" " +
            "    xml:base=\"http://localhost:19000/test/\" " +
            "    m:etag=\"W/&quot;1&quot;\">" +
            "" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Id>1</d:Id>" +
            "      <more:somePropertyToBeIgnored>ignore me</more:somePropertyToBeIgnored>" +
            "      <d:Seats>11</d:Seats>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <d:Version>4711</d:Version>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());
    assertNotNull(result);
  }

  /**
   * Is allowed because <code>Id</code> is in default namespace (<code>xmlns=\"http://www.w3.org/2005/Atom\"</code>)
   * 
   * @throws Exception
   */
  @Test
  public void validationOfUnknownPropertyDefaultNamespaceSuccess() throws Exception {
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

    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(true).build());
    assertNotNull(result);
  }

  /**
   * Add <code>unknown property</code> in own namespace which is defined directly in unknown tag.
   *  
   * @throws Exception
   */
  @Test
  public void validationOfUnknownPropertyInlineNamespaceSuccess() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" " +
            "    xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
            "    xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" " +
            "    xml:base=\"http://localhost:19000/test/\" " +
            "    m:etag=\"W/&quot;1&quot;\">" +
            "" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Id>1</d:Id>" +
            "      <more:somePropertyToBeIgnored xmlns:more=\"http://sample.com/more\">ignore me</more:somePropertyToBeIgnored>" +
            "      <d:Seats>11</d:Seats>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <d:Version>4711</d:Version>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());
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
    readAndExpectException(entitySet, reqContent, EntityProviderException.COMMON);
  }

  /**
   * Double occurrence of <code>d:Name</code> tag must result in an exception.
   * 
   * @throws Exception
   */
  @Test(expected = EntityProviderException.class)
  public void validationOfDuplicatedPropertyException() throws Exception {
    String room =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" " +
            "    xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
            "    xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" " +
            "    xml:base=\"http://localhost:19000/test/\" " +
            "    m:etag=\"W/&quot;1&quot;\">" +
            "" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Id>1</d:Id>" +
            "      <d:Seats>11</d:Seats>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <d:Version>4711</d:Version>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(room);
    readAndExpectException(entitySet, reqContent, EntityProviderException.DOUBLE_PROPERTY.addContent("Name"));
  }

  /**
   * Double occurrence of <code>Name</code> tag within different namespace is allowed.
   * 
   * @throws Exception
   */
  @Test
  public void validationOfDoublePropertyDifferentNamespace() throws Exception {
    String room =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" " +
            "    xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
            "    xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" " +
            "    xml:base=\"http://localhost:19000/test/\" " +
            "    m:etag=\"W/&quot;1&quot;\">" +
            "" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Id>1</d:Id>" +
            "      <d:Seats>11</d:Seats>" +
            "      <o:Name xmlns:o=\"http://sample.org/own\">Room 42</o:Name>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <d:Version>4711</d:Version>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(room);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());
    assertNotNull(result);
  }

  /**
   * Double occurrence of <code>Name</code> tag within ignored/unknown property AND different namespace is allowed.
   * 
   * @throws Exception
   */
  @Test
  public void validationOfDoublePropertyDifferentTagHierachy() throws Exception {
    String room =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" " +
            "    xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
            "    xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" " +
            "    xml:base=\"http://localhost:19000/test/\" " +
            "    m:etag=\"W/&quot;1&quot;\">" +
            "" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Id>1</d:Id>" +
            "      <d:Seats>11</d:Seats>" +
            "      <SomeProp>" +
            "        <Name>Room 42</Name>" +
            "      </SomeProp>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <d:Version>4711</d:Version>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(room);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());
    assertNotNull(result);
  }

  /**
   * Double occurrence of <code>d:Name</code> tag within an unknown (and hence ignored) property is allowed.
   * 
   * @throws Exception
   */
  @Test
  public void validationOfDoublePropertyDifferentTagHierachyD_Namespace() throws Exception {
    String room =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" " +
            "    xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" " +
            "    xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" " +
            "    xml:base=\"http://localhost:19000/test/\" " +
            "    m:etag=\"W/&quot;1&quot;\">" +
            "" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Id>1</d:Id>" +
            "      <d:Seats>11</d:Seats>" +
            "      <SomeProp>" +
            "        <d:Name>Room 42</d:Name>" +
            "      </SomeProp>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <d:Version>4711</d:Version>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(room);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());
    assertNotNull(result);
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
    readAndExpectException(entitySet, reqContent, EntityProviderException.COMMON);
  }

  /**
   * Missing _d_ namespace at key property/tag (_id_) is allowed.
   * 
   * @throws Exception
   */
  @Test
  public void validationOfNamespacesMissingD_NamespaceAtKeyPropertyTag() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <Id>1</Id>" +
            "      <d:Seats>11</d:Seats>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <d:Version>4711</d:Version>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    XmlEntityConsumer xec = new XmlEntityConsumer();
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());
    assertNotNull(result);
  }

  /**
   * Missing _d_ namespace at mandatory property/tag (_Version_) results in an exception.
   * 
   * @throws Exception
   */
  @Test(expected = EntityProviderException.class)
  public void validationOfNamespacesMissingD_NamespaceAtRequiredTag() throws Exception {
    String roomWithValidNamespaces =
        "<?xml version='1.0' encoding='UTF-8'?>" +
            "<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:m=\"http://schemas.microsoft.com/ado/2007/08/dataservices/metadata\" xmlns:d=\"http://schemas.microsoft.com/ado/2007/08/dataservices\" xml:base=\"http://localhost:19000/test/\" m:etag=\"W/&quot;1&quot;\">" +
            "  <id>http://localhost:19000/test/Rooms('1')</id>" +
            "  <title type=\"text\">Room 1</title>" +
            "  <updated>2013-01-11T13:50:50.541+01:00</updated>" +
            "  <content type=\"application/xml\">" +
            "    <m:properties>" +
            "      <d:Seats>11</d:Seats>" +
            "      <d:Name>Room 42</d:Name>" +
            "      <Version>4711</Version>" +
            "    </m:properties>" +
            "  </content>" +
            "</entry>";

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Rooms");
    InputStream reqContent = createContentAsStream(roomWithValidNamespaces);
    readAndExpectException(entitySet, reqContent, false, EntityProviderException.MISSING_PROPERTY.addContent("Version"));
  }

  private void readAndExpectException(final EdmEntitySet entitySet, final InputStream reqContent, final MessageReference messageReference) throws ODataMessageException {
    readAndExpectException(entitySet, reqContent, true, messageReference);
  }

  private void readAndExpectException(final EdmEntitySet entitySet, final InputStream reqContent, final boolean merge, final MessageReference messageReference) throws ODataMessageException {
    try {
      XmlEntityConsumer xec = new XmlEntityConsumer();
      ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(merge).build());
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
    ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(true).build());

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
    ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(true).build());

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
    ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(false).build());

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
    ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(false).build());

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
      ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(false).build());

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
      ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(false).build());

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
    ODataEntry result = xec.readEntry(entitySet, contentBody, EntityProviderReadProperties.init().mergeSemantic(true).build());

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
    ODataEntry result = xec.readEntry(entitySet, contentBody, 
        EntityProviderReadProperties.init().mergeSemantic(true).addTypeMappings(
          createTypeMappings("Age", Long.class, // test unused type mapping 
              "EntryDate", Date.class))
          .build());

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
    ODataEntry result = xec.readEntry(entitySet, content, EntityProviderReadProperties.init().mergeSemantic(true).build());

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
    ODataEntry result = xec.readEntry(entitySet, content, EntityProviderReadProperties.init().mergeSemantic(true).build());

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
    ODataEntry result = xec.readEntry(entitySet, content, EntityProviderReadProperties.init().build());

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

  @Test(expected = EntityProviderException.class)
  public void testReadEntryRequestInvalidMapping() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = createContentAsStream(EMPLOYEE_1_XML);
    ODataEntry result = xec.readEntry(entitySet, content, 
        EntityProviderReadProperties.init().mergeSemantic(true).addTypeMappings(createTypeMappings("EmployeeName", Integer.class)).build());

    // verify
    Map<String, Object> properties = result.getProperties();
    assertEquals(9, properties.size());
  }

  @Test
  public void testReadEntryRequestObjectMapping() throws Exception {
    XmlEntityConsumer xec = new XmlEntityConsumer();

    EdmEntitySet entitySet = MockFacade.getMockEdm().getDefaultEntityContainer().getEntitySet("Employees");
    InputStream content = createContentAsStream(EMPLOYEE_1_XML);
    ODataEntry result = xec.readEntry(entitySet, content, 
        EntityProviderReadProperties.init().mergeSemantic(true).addTypeMappings(createTypeMappings("EmployeeName", Object.class)).build());

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
    ODataEntry result = xec.readEntry(entitySet, content, EntityProviderReadProperties.init().mergeSemantic(true).addTypeMappings(
        createTypeMappings("Age", Short.class,
            "Heidelberg", String.class,
            "EntryDate", Long.class)).build());

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
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(false).build());

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
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(true).build());

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
    ODataEntry result = xec.readEntry(entitySet, reqContent, EntityProviderReadProperties.init().mergeSemantic(true).build());

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
  public void readProperty() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">67</Age>";
    InputStream content = createContentAsStream(xml);
    Map<String, Object> value = new XmlEntityConsumer().readProperty(property, content, EntityProviderReadProperties.init().mergeSemantic(true).build());

    assertEquals(Integer.valueOf(67), value.get("Age"));
  }

  @Test
  public void readStringPropertyValue() throws Exception {
    String xml = "<EmployeeName xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">Max Mustermann</EmployeeName>";
    InputStream content = createContentAsStream(xml);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeName");

    Object result = new XmlEntityConsumer().readPropertyValue(property, content, String.class);

    assertEquals("Max Mustermann", result);
  }

  @Test
  public void testReadIntegerPropertyAsLong() throws Exception {
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    String xml = "<Age xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">42</Age>";
    InputStream content = createContentAsStream(xml);
    Map<String, Object> value = new XmlEntityConsumer().readProperty(property, content, 
        EntityProviderReadProperties.init().mergeSemantic(true).addTypeMappings(createTypeMappings("Age", Long.class)).build());

    assertEquals(Long.valueOf(42), value.get("Age"));
  }

  @Test(expected = EntityProviderException.class)
  public void readStringPropertyValueWithInvalidMapping() throws Exception {
    String xml = "<EmployeeName xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">Max Mustermann</EmployeeName>";
    InputStream content = createContentAsStream(xml);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("EmployeeName");

    new XmlEntityConsumer().readPropertyValue(property, content, Integer.class);
  }

  @Test(expected = EntityProviderException.class)
  public void readPropertyWrongNamespace() throws Exception {
    String xml = "<Age xmlns=\"" + Edm.NAMESPACE_M_2007_08 + "\">1</Age>";
    InputStream content = createContentAsStream(xml);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    new XmlEntityConsumer().readPropertyValue(property, content, Integer.class);
  }

  @Test(expected = EntityProviderException.class)
  public void readPropertyWrongClosingNamespace() throws Exception {
    String xml = "<d:Age xmlns:d=\"" + Edm.NAMESPACE_D_2007_08 + "\" xmlns:m=\"" + Edm.NAMESPACE_M_2007_08 + "\">1</m:Age>";
    InputStream content = createContentAsStream(xml);
    final EdmProperty property = (EdmProperty) MockFacade.getMockEdm().getEntityType("RefScenario", "Employee").getProperty("Age");

    new XmlEntityConsumer().readPropertyValue(property, content, Integer.class);
  }

  private InputStream createContentAsStream(final String xml) throws UnsupportedEncodingException {
    return new ByteArrayInputStream(xml.getBytes("UTF-8"));
  }
}
