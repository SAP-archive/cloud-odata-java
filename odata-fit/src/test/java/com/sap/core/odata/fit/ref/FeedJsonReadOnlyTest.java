package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertEquals;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;

/**
 * Tests employing the reference scenario reading entity sets in JSON format.
 * @author SAP AG
 */
public class FeedJsonReadOnlyTest extends AbstractRefTest {

  @Test
  public void feed() throws Exception {
    final HttpResponse response = callUri("Buildings()?$top=2&$format=json");
    checkMediaType(response, HttpContentType.APPLICATION_JSON);
    assertEquals("{\"d\":{\"results\":[{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('1')\","
        + "\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"1\",\"Name\":\"Building 1\",\"Image\":null,"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('1')/nb_Rooms\"}}},"
        + "{\"__metadata\":{"
        + "\"id\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"uri\":\"" + getEndpoint() + "Buildings('2')\","
        + "\"type\":\"RefScenario.Building\"},"
        + "\"Id\":\"2\",\"Name\":\"Building 2\",\"Image\":null,"
        + "\"nb_Rooms\":{\"__deferred\":{\"uri\":\"" + getEndpoint() + "Buildings('2')/nb_Rooms\"}}}"
        + "]}}",
        getBody(response));
  }
}
