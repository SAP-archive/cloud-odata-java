package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

/**
 * Tests employing the reference scenario reading entity sets in XML format
 * @author SAP AG
 */
public class FeedTest extends AbstractRefTest {

  @Test
  public void feed() throws Exception {
    HttpResponse response = callUri("Employees()");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    final String payload = getBody(response);
    assertTrue(payload.contains("Employee"));
    assertTrue(payload.contains(EMPLOYEE_1_NAME));
    assertTrue(payload.contains(EMPLOYEE_2_NAME));
    assertTrue(payload.contains(EMPLOYEE_3_NAME));
    assertTrue(payload.contains(EMPLOYEE_4_NAME));
    assertTrue(payload.contains(EMPLOYEE_5_NAME));
    assertTrue(payload.contains(EMPLOYEE_6_NAME));

    response = callUri("Rooms()");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    assertTrue(getBody(response).contains("Room"));

    // notFound("$top");
  }

  @Test
  public void navigationFeed() throws Exception {
    HttpResponse response = callUri("Employees('3')/ne_Room/nr_Employees()");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    String body = getBody(response);
    // assertTrue(body.contains("feed"));
    // assertTrue(body.contains("entry"));
    // count <d:EmployeeName> = 4
    assertTrue(body.contains(EMPLOYEE_2_NAME));
    assertTrue(body.contains(EMPLOYEE_3_NAME));
    assertTrue(body.contains(EMPLOYEE_4_NAME));
    assertTrue(body.contains(EMPLOYEE_6_NAME));
    assertFalse(body.contains(EMPLOYEE_1_NAME));
    assertFalse(body.contains(EMPLOYEE_5_NAME));

    response = callUri("Rooms('2')/nr_Employees");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    assertFalse(getBody(response).isEmpty());
    // count <d:EmployeeName> = 4

    // response = callUri("Employees('2')/ne_Team/nt_Employees?$orderby=Age&$top=1");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    // count <d:EmployeeName> = 1
    // assertTrue(getBody(response).contains(EMPLOYEE_2_NAME));
  }

  @Test
  public void skipAndTop() throws Exception {
    HttpResponse response = callUri("Employees?$skip=1&$top=1");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    String body = getBody(response);
    // assertTrue(body.contains("feed"));
    // assertTrue(body.contains("entry"));
    // count <d:EmployeeName> = 1
    assertTrue(body.contains(EMPLOYEE_2_NAME));

    response = callUri("Teams()?$skip=4");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    assertFalse(getBody(response).contains("entry"));

    badRequest("Employees?$top=a");
    badRequest("Employees?$top=-1");
    badRequest("Teams('3')?$top=1");
  }

  @Test
  public void skiptoken() throws Exception {
    // HttpResponse response = callUri("Employees?$skiptoken=6");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    // String body = getBody(response);
    // count <d:EmployeeName> = 1
    // assertTrue(body.contains(EMPLOYEE_6_NAME));
    // assertFalse(body.contains(EMPLOYEE_1_NAME));

    HttpResponse response = callUri("Container2.Photos?$skiptoken=4foo");
    // checkMediaType(response, APPLICATION_ATOM_XML_FEED);
    String body = getBody(response);
    assertFalse(body.isEmpty());
    // count <d:Name> = 1
  }
}
