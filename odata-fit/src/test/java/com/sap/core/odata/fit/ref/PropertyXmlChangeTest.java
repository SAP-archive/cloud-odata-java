/*******************************************************************************
 * Copyright 2013 SAP AG
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.sap.core.odata.fit.ref;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.core.commons.ODataHttpMethod;

/**
 * Tests employing the reference scenario changing properties in XML format
 * @author SAP AG
 */
public class PropertyXmlChangeTest extends AbstractRefXmlTest {

  @Test
  public void simpleProperty() throws Exception {
    final String url1 = "Employees('2')/Age";
    String requestBody = getBody(callUri(url1)).replace(EMPLOYEE_2_AGE, "17");
    putUri(url1, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo("17", "/d:Age", getBody(callUri(url1)));

    final String url2 = "Buildings('3')/Name";
    requestBody = getBody(callUri(url2)).replace(BUILDING_3_NAME, "XXX");
    putUri(url2, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo("XXX", "/d:Name", getBody(callUri(url2)));

    final String url3 = "Employees('2')/Location/City/CityName";
    requestBody = getBody(callUri(url3)).replace(CITY_2_NAME, "XXX");
    putUri(url3, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo("XXX", "/d:CityName", getBody(callUri(url3)));

    final String url4 = "Rooms('42')/Seats";
    requestBody = "<Seats xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\">42</Seats>";
    HttpResponse response = callUri(ODataHttpMethod.PUT, url4, null, null, requestBody, HttpContentType.APPLICATION_XML_UTF8, HttpStatusCodes.NO_CONTENT);
    checkEtag(response, "W/\"1\"");

    final String url5 = "Employees('2')/EmployeeId";
    requestBody = getBody(callUri(url5));
    putUri(url5, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.METHOD_NOT_ALLOWED);
  }

  @Test
  public void complexProperty() throws Exception {
    final String url1 = "Employees('2')/Location";
    String requestBody = getBody(callUri(url1)).replace(CITY_2_NAME, "XXX");
    putUri(url1, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo("XXX", "/d:Location/d:City/d:CityName", getBody(callUri(url1)));

    final String url2 = "Employees('2')/Location/City";
    requestBody = getBody(callUri(url2)).replace("XXX", "YYY");
    putUri(url2, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo("YYY", "/d:City/d:CityName", getBody(callUri(url2)));

    requestBody = "<City xmlns=\"" + Edm.NAMESPACE_D_2007_08 + "\"><PostalCode>00000</PostalCode></City>";
    callUri(ODataHttpMethod.PATCH, url2, null, null, requestBody, HttpContentType.APPLICATION_XML, HttpStatusCodes.NO_CONTENT);
    assertXpathEvaluatesTo("YYY", "/d:City/d:CityName", getBody(callUri(url2)));
  }
}
