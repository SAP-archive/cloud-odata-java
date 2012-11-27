package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.testutils.helper.StringHelper;

public class FeedTest extends AbstractRefTest {

  @Test
  public void feed() throws Exception {
    HttpResponse response = callUri("Employees()", HttpStatusCodes.OK);
    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
//    // checkMediaType(response, MediaType.APPLICATION_ATOM_XML_TYPE);
    assertTrue(payload.contains("Employee"));
//    // TODO: check content

    response = callUri("Rooms()", HttpStatusCodes.OK);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains("Room"));
    // checkMediaType(response, MediaType.APPLICATION_ATOM_XML_TYPE);

    // notFound("$top");
  }
  
  
}
