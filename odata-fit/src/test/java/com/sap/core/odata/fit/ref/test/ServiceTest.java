package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

/**
 * @author SAP AG
 */
public class ServiceTest extends AbstractRefTest {

  @Test
  public void serviceDocument() throws Exception {
    checkUri("/");
    // checkMediaType(response, new MediaType("application", "atomsvc+xml"));

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
