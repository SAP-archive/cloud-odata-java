package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.*;
import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.testutils.helper.StringHelper;

public class ServiceTest extends AbstractRefTest {

  @Test
  public void serviceDocument() throws Exception {
    callUri("/", HttpStatusCodes.OK);
    // checkMediaType(response, new MediaType("application", "atomsvc+xml"));
    //    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    // notFound("invalid.svc/");
  }

  @Test
  public void metadataDocument() throws Exception {
    HttpResponse response = callUri("$metadata", HttpStatusCodes.OK);
    //    checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("c_Location"));
    assertTrue(payload.contains("c_City"));
    assertTrue(payload.contains("Container1"));

    // notFound("$invalid");
    callUri("$metadata?$format=json", HttpStatusCodes.BAD_REQUEST);
  }
}
