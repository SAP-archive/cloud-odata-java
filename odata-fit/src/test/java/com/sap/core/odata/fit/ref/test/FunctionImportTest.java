package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.*;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.testutils.helper.StringHelper;

public class FunctionImportTest extends AbstractRefTest {

  @Test
  public void testFunctionImports() throws Exception {
    HttpResponse response;
    String payload;

    response = callUri("EmployeeSearch('1')/ne_Room/Id/$value?q='alter'", HttpStatusCodes.OK);
    //checkMediaType(response, MediaType.TEXT_PLAIN_TYPE);
    // checkEtag(response, true, "W/\"1\"");
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("1", payload);

    response = callUri("MaximalAge", HttpStatusCodes.OK);
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains(EMPLOYEE_3_AGE));

    response = callUri("ManagerPhoto?Id='1'", HttpStatusCodes.OK);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    response = callUri("ManagerPhoto/$value?Id='1'", HttpStatusCodes.OK);
    // checkMediaType(response, IMAGE_JPEG);
    //assertNull(response.getEntityTag());
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    response = callUri("ManagerPhoto?Id='2'", HttpStatusCodes.NOT_FOUND);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    // assertThat(callUrl("EmployeeSearch?q='-'").getEntity());  // contains no entity

    response = callUri("AllLocations", HttpStatusCodes.OK);
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains(CITY_2_NAME));

    response = callUri("AllUsedRoomIds", HttpStatusCodes.OK);
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("3"));

    response = callUri("MostCommonLocation", HttpStatusCodes.OK);
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains(CITY_2_NAME));

    response = callUri("OldestEmployee", HttpStatusCodes.OK);
    // checkMediaType(response, MediaType.APPLICATION_ATOM_XML_TYPE);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains(EMPLOYEE_3_NAME));

    response = callUri("OldestEmployee?$format=xml", HttpStatusCodes.OK);
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains(EMPLOYEE_3_NAME));

    // badRequest("AllLocations/$count");
    // badRequest("AllUsedRoomIds/$value");
    // badRequest("MaximalAge()");
    // badRequest("MostCommonLocation/City/CityName");
    //response = callUri("ManagerPhoto", HttpStatusCodes.NOT_FOUND);
    // badRequest("OldestEmployee()");

  }

}
