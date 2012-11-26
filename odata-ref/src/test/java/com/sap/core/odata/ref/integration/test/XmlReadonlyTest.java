package com.sap.core.odata.ref.integration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Read-only tests employing the reference scenario that use the Atom/XML format
 * @author SAP AG
 */
@Ignore
public class XmlReadonlyTest extends AbstractTest {

  private static final String EMPLOYEE_1_NAME = "Walter Winter";
  private static final String EMPLOYEE_2_NAME = "Frederic Fall";
  private static final String EMPLOYEE_3_NAME = "Jonathan Smith";
  private static final String EMPLOYEE_4_NAME = "Peter Burke";
  private static final String EMPLOYEE_5_NAME = "John Field";
  private static final String EMPLOYEE_6_NAME = "Susan Bay";
  private static final String MANAGER_NAME = EMPLOYEE_1_NAME;

  private static final String EMPLOYEE_2_AGE = "32";
  private static final String EMPLOYEE_3_AGE = "56";
  private static final String EMPLOYEE_6_AGE = "29";

  private static final String CITY_2_NAME = "Walldorf";

  @Test
  public void serviceDocument() throws Exception {
    Response response = callUrl("/");
    // checkMediaType(response, new MediaType("application", "atomsvc+xml"));

    // notFound("invalid.svc/");
  }

  @Test
  public void metadataDocument() throws Exception {
    Response response = callUrl("$metadata");
    checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    // assertTrue(response.getEntity().toString().contains("c_Location"));
    // assertTrue(response.getEntity().toString().contains("c_City"));
    // assertTrue(response.getEntity().toString().contains("Container1"));

    // notFound("$invalid");
    // badRequest("$metadata?$format=json");
  }

  @Test
  public void feed() throws Exception {
    Response response = callUrl("Employees()");
    // checkMediaType(response, MediaType.APPLICATION_ATOM_XML_TYPE);
    // TODO: check content

    response = callUrl("Rooms()");
    // checkMediaType(response, MediaType.APPLICATION_ATOM_XML_TYPE);

    // notFound("$top");
  }

  @Test
  public void entry() throws Exception {
  }

  @Test
  public void mediaResourceLink() throws Exception {
  }

  @Test 
  public void functionImport() throws Exception {
    Response response = callUrl("EmployeeSearch('1')/ne_Room/Id/$value?q='alter'");
    checkMediaType(response, MediaType.TEXT_PLAIN_TYPE);
    // checkEtag(response, true, "W/\"1\"");
    assertEquals("1", response.getEntity());

    // assertThat(callUrl("EmployeeSearch?q='-'").getEntity());  // contains no entity

    response = callUrl("AllLocations");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    assertTrue(response.getEntity().toString().contains(CITY_2_NAME));

    response = callUrl("AllUsedRoomIds");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    assertTrue(response.getEntity().toString().contains("3"));

    response = callUrl("MaximalAge");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    assertTrue(response.getEntity().toString().contains(EMPLOYEE_3_AGE));

    response = callUrl("MostCommonLocation");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    assertTrue(response.getEntity().toString().contains(CITY_2_NAME));

    response = callUrl("ManagerPhoto?Id='1'");
 
    response = callUrl("ManagerPhoto/$value?Id='1'");
    // checkMediaType(response, IMAGE_JPEG);
    assertNull(response.getEntityTag());
 
    response = callUrl("OldestEmployee");
    // checkMediaType(response, MediaType.APPLICATION_ATOM_XML_TYPE);
    assertTrue(response.getEntity().toString().contains(EMPLOYEE_3_NAME));

    response = callUrl("OldestEmployee?$format=xml");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    assertTrue(response.getEntity().toString().contains(EMPLOYEE_3_NAME));

    // badRequest("AllLocations/$count");
    // badRequest("AllUsedRoomIds/$value");
    // badRequest("MaximalAge()");
    // badRequest("MostCommonLocation/City/CityName");
    notFound("ManagerPhoto");
    // badRequest("OldestEmployee()");
    notFound("ManagerPhoto?Id='2'");
  }

  @Test
  public void simpleProperty() throws Exception {
    Response response = callUrl("Employees('2')/Age/$value");
    checkMediaType(response, MediaType.TEXT_PLAIN_TYPE);
    assertEquals(EMPLOYEE_2_AGE, response.getEntity().toString());

    response = callUrl("Employees('2')/Age");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    assertTrue(response.getEntity().toString().contains(EMPLOYEE_2_AGE));

    response = callUrl("Container2.Photos(Id=3,Type='image%2Fjpeg')/Image/$value");
    // checkMediaType(response, IMAGE_JPEG);

    response = callUrl("Container2.Photos(Id=3,Type='image%2Fjpeg')/Image");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    // assertTrue(response.getEntity().toString().contains("<d:Image m:type=\"Edm.Binary\" m:MimeType=\"image/jpeg\""));

    response = callUrl("Rooms('2')/Seats/$value");
    checkMediaType(response, MediaType.TEXT_PLAIN_TYPE);
    // checkEtag(response, true, "W/\"2\"");
    assertEquals("5", response.getEntity().toString());

    response = callUrl("Rooms('2')/Seats");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    // checkEtag(response, true, "W/\"2\"");
    // assertTrue(response.getEntity().toString().contains("5</"));

    // response = callUrl("Container2.Photos(Id=3,Type='image%2Fjpeg')/BinaryData/$value");
    // checkMediaType(response, IMAGE_JPEG);

    // response = callUrl("Container2.Photos(Id=3,Type='image%2Fjpeg')/BinaryData");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);

    // notFound("Employees('2')/Foo");
    // notFound("Employees('2')/Age()");
  }

  @Test
  public void complexProperty() throws Exception {
    Response response = callUrl("Employees('2')/Location/City/CityName/$value");
    checkMediaType(response, MediaType.TEXT_PLAIN_TYPE);
    assertEquals(CITY_2_NAME, response.getEntity());

    response = callUrl("Employees('2')/Location");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    // assertTrue(response.getEntity().toString().contains("PostalCode"));

    // notFound("Employees('2')/Location()");
    // notFound("Employees('2')/Location/City/$value");
  }
}