package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario changing properties in JSON format.
 * @author SAP AG
 */
public class PropertyJsonChangeTest extends AbstractRefTest {

  @Test
  public void simpleProperty() throws Exception {
    final String url = "Employees('2')/Age";
    putUri(url, "{\"Age\":17}", HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertEquals("{\"d\":{\"Age\":17}}", getBody(callUri(url + "?$format=json")));

    putUri(url, "{\"Age\":\"17\"}", HttpContentType.APPLICATION_JSON, HttpStatusCodes.BAD_REQUEST);
  }

  @Test
  public void complexProperty() throws Exception {
    final String url1 = "Employees('2')/Location";
    String requestBody = "{\"Location\":{\"City\":{\"PostalCode\":\"69190\","
        + "\"CityName\":\"" + CITY_2_NAME + "\"},\"Country\":\"Germany\"}}";
    putUri(url1, requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertTrue(getBody(callUri(url1 + "?$format=json")).contains(CITY_2_NAME));

    final String url2 = "Employees('2')/Location/City";
    requestBody = "{\"City\":{\"PostalCode\":\"69185\"}}";
    putUri(url2, requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertTrue(getBody(callUri(url2 + "?$format=json")).contains("69185"));
  }
}
