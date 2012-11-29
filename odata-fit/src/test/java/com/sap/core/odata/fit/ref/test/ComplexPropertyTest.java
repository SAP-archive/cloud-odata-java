package com.sap.core.odata.fit.ref.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.http.HttpResponse;
import org.junit.Test;

/**
 * Tests employing the reference scenario reading complex properties in XML format
 * @author SAP AG
 */
public class ComplexPropertyTest extends AbstractRefTest {
  @Test
  public void complexProperty() throws Exception {
    HttpResponse response = callUri("Employees('2')/Location/City/CityName/$value");
    checkMediaType(response, TEXT_PLAIN);
    assertEquals(CITY_2_NAME, getBody(response));

    response = callUri("Employees('2')/Location");
    // checkMediaType(response, APPLICATION_XML);
    assertNotNull(getBody(response));
    // assertTrue(getBody(response).contains("PostalCode"));

    // notFound("Employees('2')/Location()");
    // notFound("Employees('2')/Location/City/$value");
  }
}
