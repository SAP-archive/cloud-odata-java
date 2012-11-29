package com.sap.core.odata.fit.ref.test;

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

}
