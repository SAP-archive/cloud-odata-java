package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.testutils.helper.StringHelper;

public class SimplePropertyTest extends AbstractRefTest {

  @Test
  public void simpleProperty() throws Exception {
    HttpResponse response = callUri("Employees('2')/Age/$value", HttpStatusCodes.OK);
    //checkMediaType(response, MediaType.TEXT_PLAIN_TYPE);
    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals(EMPLOYEE_2_AGE, payload);

    response = callUri("Employees('2')/Age", HttpStatusCodes.OK);
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertTrue(payload.contains(EMPLOYEE_2_AGE));

    response = callUri("Container2.Photos(Id=3,Type='image%2Fjpeg')/Image/$value", HttpStatusCodes.OK);
    // checkMediaType(response, IMAGE_JPEG);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    response = callUri("Container2.Photos(Id=3,Type='image%2Fjpeg')/Image", HttpStatusCodes.OK);
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    // assertTrue(response.getEntity().toString().contains("<d:Image m:type=\"Edm.Binary\" m:MimeType=\"image/jpeg\""));
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());

    response = callUri("Rooms('2')/Seats/$value", HttpStatusCodes.OK);
//    checkMediaType(response, MediaType.TEXT_PLAIN_TYPE);
    // checkEtag(response, true, "W/\"2\"");
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals("5", payload);

    response = callUri("Rooms('2')/Seats", HttpStatusCodes.OK);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    // checkEtag(response, true, "W/\"2\"");
    // assertTrue(response.getEntity().toString().contains("5</"));

    // response = callUri("Container2.Photos(Id=3,Type='image%2Fjpeg')/BinaryData/$value");
    // checkMediaType(response, IMAGE_JPEG);

    // response = callUri("Container2.Photos(Id=3,Type='image%2Fjpeg')/BinaryData");
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);

    // notFound("Employees('2')/Foo");
    // notFound("Employees('2')/Age()");
  }
  
}
