package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

/**
 * Tests employing the reference scenario reading the service document in XML format
 * @author SAP AG
 */
public class ServiceTest extends AbstractRefTest {

  @Test
  public void serviceDocument() throws Exception {
    final HttpResponse response = callUri("/");
    // checkMediaType(response, APPLICATION_ATOMSVC_XML);
    assertFalse(getBody(response).isEmpty());
    // assertTrue(getBody(response).contains("Employees"));

    // notFound("invalid.svc/");
  }

  @Test
  public void metadataDocument() throws Exception {
    final HttpResponse response = callUri("$metadata");
    checkMediaType(response, APPLICATION_XML);
    final String payload = getBody(response);
    assertTrue(payload.contains("c_Location"));
    assertTrue(payload.contains("c_City"));
    assertTrue(payload.contains("Container1"));

    // notFound("$invalid");
    badRequest("$metadata?$format=json");
  }
}
