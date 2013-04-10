package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpHeaders;

/**
 * Tests employing the reference scenario reading function-import output in JSON format.
 * @author SAP AG
 */
public class FunctionImportJsonTest extends AbstractRefTest {

  @Test
  public void functionImports() throws Exception {
    HttpResponse response = callUri("EmployeeSearch?q='nat'&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    final String body = getBody(response);
    assertEquals(getBody(callUri("Employees?$filter=substringof(EmployeeName,'nat')&$format=json")), body);

    response = callUri("AllLocations?$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"__metadata\":{\"type\":\"Collection(RefScenario.c_Location)\"},"
        + "\"results\":[{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},"
        + "\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},"
        + "\"PostalCode\":\"69124\",\"CityName\":\"Heidelberg\"},\"Country\":\"Germany\"},"
        + "{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},"
        + "\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},"
        + "\"PostalCode\":\"69190\",\"CityName\":\"Walldorf\"},\"Country\":\"Germany\"}]}}",
        getBody(response));

    response = callUri("AllUsedRoomIds?$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"__metadata\":{\"type\":\"Collection(Edm.String)\"},"
        + "\"results\":[\"1\",\"2\",\"3\"]}}",
        getBody(response));

    response = callUri("MaximalAge?$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"MaximalAge\":56}}", getBody(response));

    response = callUri("MostCommonLocation?$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"MostCommonLocation\":"
        + "{\"__metadata\":{\"type\":\"RefScenario.c_Location\"},"
        + "\"City\":{\"__metadata\":{\"type\":\"RefScenario.c_City\"},"
        + "\"PostalCode\":\"69190\",\"CityName\":\"" + CITY_2_NAME + "\"},"
        + "\"Country\":\"Germany\"}}}",
        getBody(response));

    response = callUri("ManagerPhoto?Id='1'&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertTrue(getBody(response).startsWith("{\"d\":{\"ManagerPhoto\":\"/9j/4"));

    final String expected = getBody(callUri("Employees('3')?$format=json"));
    response = callUri("OldestEmployee", HttpHeaders.ACCEPT, HttpContentType.APPLICATION_JSON);
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals(expected, getBody(response));
  }
}
