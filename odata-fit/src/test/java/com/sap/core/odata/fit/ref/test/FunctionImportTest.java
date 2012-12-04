package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.junit.Test;

/**
 * Tests employing the reference scenario reading function-import output in XML format
 * @author SAP AG
 */
public class FunctionImportTest extends AbstractRefTest {

  @Test
  public void testFunctionImports() throws Exception {
    HttpResponse response;

    response = callUri("EmployeeSearch('1')/ne_Room/Id/$value?q='alter'");
    checkMediaType(response, TEXT_PLAIN);
    // checkEtag(response, "W/\"1\"");
    assertEquals("1", getBody(response));

    assertFalse(getBody(callUri("EmployeeSearch?q='-'")).contains("entry"));

    response = callUri("AllLocations");
    checkMediaType(response, APPLICATION_XML);
    assertTrue(getBody(response).contains(CITY_2_NAME));

    response = callUri("AllUsedRoomIds");
    checkMediaType(response, APPLICATION_XML);
    assertTrue(getBody(response).contains("3"));

    response = callUri("MaximalAge");
    checkMediaType(response, APPLICATION_XML);
    assertTrue(getBody(response).contains(EMPLOYEE_3_AGE));

    response = callUri("MostCommonLocation");
    checkMediaType(response, APPLICATION_XML);
    assertTrue(getBody(response).contains(CITY_2_NAME));

    checkUri("ManagerPhoto?Id='1'");

    response = callUri("ManagerPhoto/$value?Id='1'");
    // checkMediaType(response, IMAGE_JPEG);
    assertNull(response.getFirstHeader(HttpHeaders.ETAG));
    assertNotNull(getBody(response));

    response = callUri("OldestEmployee");
    checkMediaType(response, APPLICATION_ATOM_XML_ENTRY);
    assertTrue(getBody(response).contains(EMPLOYEE_3_NAME));

    response = callUri("OldestEmployee?$format=xml");
    checkMediaType(response, APPLICATION_XML);
    assertTrue(getBody(response).contains(EMPLOYEE_3_NAME));

    badRequest("AllLocations/$count");
    badRequest("AllUsedRoomIds/$value");
    badRequest("MaximalAge()");
    badRequest("MostCommonLocation/City/CityName");
    // notFound("ManagerPhoto");
    badRequest("OldestEmployee()");
    notFound("ManagerPhoto?Id='2'");
  }

}
