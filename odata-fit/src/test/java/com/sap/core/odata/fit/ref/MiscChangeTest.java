package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.core.commons.ContentType;
import com.sap.core.odata.core.commons.ODataHttpMethod;

/**
 * Tests employing the reference scenario that use neither XML nor JSON
 * and that change data in some way
 * @author SAP AG
 */
public class MiscChangeTest extends AbstractRefTest {

  @Test
  public void deleteEntry() throws Exception {
    deleteUriOk("Employees('2')");
    deleteUriOk("Managers('3')");
    deleteUriOk("Teams('2')");
    callUri(ODataHttpMethod.DELETE, "Rooms('1')", HttpHeaders.IF_MATCH, "W/\"1\"", null, null, HttpStatusCodes.NO_CONTENT);
    callUri(ODataHttpMethod.DELETE, "Container2.Photos(Id=1,Type='image%2Fpng')",
        HttpHeaders.IF_MATCH, "W/\"1\"", null, null, HttpStatusCodes.NO_CONTENT);

    // deleteUri("Rooms('1')", HttpStatusCodes.PRECONDITION_REQUIRED);
    deleteUri("Managers()", HttpStatusCodes.METHOD_NOT_ALLOWED);
    deleteUri("Managers('5')", HttpStatusCodes.NOT_FOUND);
    deleteUri("Employees('2')/ne_Manager", HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  @Test
  public void deletePropertyValue() throws Exception {
    deleteUriOk("Employees('2')/Age/$value");
    deleteUriOk("Employees('2')/Location/City/PostalCode/$value");

    deleteUri("Employees('2')/Age", HttpStatusCodes.METHOD_NOT_ALLOWED);
    deleteUri("Employees('2')/Foo/$value", HttpStatusCodes.NOT_FOUND);
    deleteUri("Employees('2')/EmployeeId/$value", HttpStatusCodes.METHOD_NOT_ALLOWED);
    deleteUri("Employees('2')/Location/City/$value", HttpStatusCodes.NOT_FOUND);
    deleteUri("Employees('2')/ne_Manager/Age/$value", HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  @Test
  public void deleteLink() throws Exception {
    deleteUriOk("Employees('6')/$links/ne_Room");
    deleteUriOk("Managers('3')/$links/nm_Employees('5')");

    deleteUri("Managers('3')/$links/nm_Employees()", HttpStatusCodes.METHOD_NOT_ALLOWED);
    deleteUri("Managers('3')/$links/nm_Employees('1')", HttpStatusCodes.NOT_FOUND);
    deleteUri("Employees('2')/ne_Team/$links/nt_Employees('1')", HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  @Test
  public void deleteMediaResource() throws Exception {
    deleteUriOk("Managers('1')/$value");

    deleteUri("Teams('2')/$value", HttpStatusCodes.BAD_REQUEST);
  }

  @Test
  public void updateMediaResource() throws Exception {
    final String url = "Managers('1')/$value";
    putUri(url, "00", HttpContentType.APPLICATION_OCTET_STREAM, HttpStatusCodes.NO_CONTENT);
    final HttpResponse response = callUri(url);
    checkMediaType(response, ContentType.APPLICATION_OCTET_STREAM, false);
    assertEquals("00", getBody(response));
  }

  @Test
  public void updatePropertyValue() throws Exception {
    putUri("Employees('2')/Age/$value", "42", HttpContentType.TEXT_PLAIN_UTF8, HttpStatusCodes.NO_CONTENT);

    String url = "Container2.Photos(Id=3,Type='image%2Fjpeg')/Image/$value";
    callUri(ODataHttpMethod.PUT, url, HttpHeaders.ETAG, "W/\"3\"", "4711", HttpContentType.APPLICATION_OCTET_STREAM, HttpStatusCodes.NO_CONTENT);
    assertEquals("4711", getBody(callUri(url)));

    url = "Container2.Photos(Id=4,Type='foo')/BinaryData/$value";
    callUri(ODataHttpMethod.PUT, url, HttpHeaders.ETAG, "W/\"4\"", "4711", IMAGE_JPEG, HttpStatusCodes.NO_CONTENT);
    assertEquals("4711", getBody(callUri(url)));

    final String content = "2012-02-29T00:00:00";
    url = "Employees('2')/EntryDate/$value";
    putUri(url, content, HttpContentType.TEXT_PLAIN_UTF8, HttpStatusCodes.NO_CONTENT);
    assertEquals(content, getBody(callUri(url)));

    putUri("Employees('2')/EmployeeId/$value", "42", HttpContentType.TEXT_PLAIN_UTF8, HttpStatusCodes.METHOD_NOT_ALLOWED);
    // putUri("Employees('2')/Age/$value", "42a", HttpContentType.TEXT_PLAIN_UTF8, HttpStatusCodes.BAD_REQUEST);
    // putUri(url, "2000-13-78T42:19:18z", HttpContentType.TEXT_PLAIN_UTF8, HttpStatusCodes.BAD_REQUEST);
  }
}
