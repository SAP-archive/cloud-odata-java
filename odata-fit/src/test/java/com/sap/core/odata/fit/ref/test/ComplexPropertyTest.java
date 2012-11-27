package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.enums.HttpStatusCodes;
import com.sap.core.odata.testutils.helper.StringHelper;

public class ComplexPropertyTest extends AbstractRefTest {
  @Test
  public void complexProperty() throws Exception {
    HttpResponse response = callUri("Employees('2')/Location/City/CityName/$value", HttpStatusCodes.OK);
    //checkMediaType(response, MediaType.TEXT_PLAIN_TYPE);
    String payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    assertEquals(CITY_2_NAME, payload);

    response = callUri("Employees('2')/Location", HttpStatusCodes.OK);
    payload = StringHelper.inputStreamToString(response.getEntity().getContent());
    // checkMediaType(response, MediaType.APPLICATION_XML_TYPE);
    assertTrue(payload.contains("Walldorf"));

    // notFound("Employees('2')/Location()");
    // notFound("Employees('2')/Location/City/$value");
  }
}
