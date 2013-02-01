package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;

/**
 * Tests employing the reference scenario reading complex properties in XML format
 * @author SAP AG
 */
public class ComplexPropertyTest extends AbstractRefTest {
  @Test
  public void complexProperty() throws Exception {
    HttpResponse response = callUri("Employees('2')/Location/City/CityName/$value");
    checkMediaType(response, HttpContentType.TEXT_PLAIN_UTF8);
    assertEquals(CITY_2_NAME, getBody(response));

    response = callUri("Employees('2')/Location");
    checkMediaType(response, HttpContentType.APPLICATION_XML_UTF8);
    assertTrue(getBody(response).contains("PostalCode"));

    badRequest("Employees('2')/Location()");
    notFound("Employees('2')/Location/City/$value");
  }
}
