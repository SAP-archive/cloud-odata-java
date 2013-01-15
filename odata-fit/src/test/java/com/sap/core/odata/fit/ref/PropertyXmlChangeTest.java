package com.sap.core.odata.fit.ref;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario changing properties in XML format
 * @author SAP AG
 */
public class PropertyXmlChangeTest extends AbstractRefTest {

  @Test
  public void simpleProperty() throws Exception {
    final String url1 = "Employees('2')/Age";
    String requestBody = getBody(callUri(url1)).replace(EMPLOYEE_2_AGE, "17");
    putUri(url1, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertTrue(getBody(callUri(url1)).contains("17"));

    final String url2 = "Buildings('3')/Name";
    requestBody = getBody(callUri(url2)).replace(BUILDING_3_NAME, "XXX");
    putUri(url2, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertTrue(getBody(callUri(url2)).contains("XXX"));

    final String url3 = "Employees('2')/Location/City/CityName";
    requestBody = getBody(callUri(url3)).replace(CITY_2_NAME, "XXX");
    putUri(url3, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertTrue(getBody(callUri(url3)).contains("XXX"));

    final String url4 = "Employees('2')/EmployeeId";
    requestBody = getBody(callUri(url4));
    putUri(url4, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }
}
