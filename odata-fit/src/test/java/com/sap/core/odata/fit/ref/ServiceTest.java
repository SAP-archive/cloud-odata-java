package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.ContentType;

/**
 * Tests employing the reference scenario reading the service document in XML format
 * @author SAP AG
 */
public class ServiceTest extends AbstractRefTest {

  @Test
  public void serviceDocument() throws Exception {
    final HttpResponse response = callUri("/");
    checkMediaType(response, ContentType.APPLICATION_ATOM_SVC);
    assertTrue(getBody(response).contains("Employees"));

    // notFound("invalid.svc/");
  }

  @Test
  public void metadataDocument() throws Exception {
    final HttpResponse response = callUri("$metadata");
    checkMediaType(response, ContentType.APPLICATION_XML, false);
    final String payload = getBody(response);
    assertTrue(payload.contains("c_Location"));
    assertTrue(payload.contains("c_City"));
    assertTrue(payload.contains("Container1"));

    notFound("$invalid");
    badRequest("$metadata?$format=json");
  }
}
