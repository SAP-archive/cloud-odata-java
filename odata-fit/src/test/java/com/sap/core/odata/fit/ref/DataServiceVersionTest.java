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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Test;

import com.sap.core.odata.api.ODataServiceVersion;
import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.commons.ODataHttpHeaders;

/**
 * @author SAP AG
 */
public class DataServiceVersionTest extends AbstractRefTest {

  private static void checkVersion(final HttpResponse response, final String expectedValue) throws AssertionError {
    final Header header = response.getFirstHeader(ODataHttpHeaders.DATASERVICEVERSION);
    assertNotNull(header);
    assertEquals(expectedValue, header.getValue());
  }

  @Test
  public void testDataServiceVersionCase() throws Exception {
    HttpResponse response = callUri("Employees");
    Header header = response.getFirstHeader("dataserviceversion");
    assertNotNull(header);
    assertEquals(ODataServiceVersion.V20, header.getValue());

    checkVersion(response, ODataServiceVersion.V20);
  }

  @Test
  public void testDataServiceVersionWithSemicolon() throws Exception {
    HttpResponse response = callUri("Employees", ODataHttpHeaders.DATASERVICEVERSION, "2.0;hallo", HttpStatusCodes.OK);
    checkVersion(response, ODataServiceVersion.V20);
  }

  @Test
  public void testDataServiceVersionNotSetOnEntitySet() throws Exception {
    checkVersion(callUri("Employees"), ODataServiceVersion.V20);
  }

  @Test
  public void testDataServiceVersionSetOnEntitySet() throws Exception {
    HttpResponse response = callUri("Employees", ODataHttpHeaders.DATASERVICEVERSION, "2.0");
    checkVersion(response, ODataServiceVersion.V20);
    getBody(response);

    response = callUri("Employees", ODataHttpHeaders.DATASERVICEVERSION, "1.0");
    checkVersion(response, ODataServiceVersion.V20);
  }

  @Test
  public void testDataServiceVersionSetOnEntitySetFail() throws Exception {
    HttpResponse response = callUri("Employees", ODataHttpHeaders.DATASERVICEVERSION, "3.0", HttpStatusCodes.BAD_REQUEST);
    checkVersion(response, ODataServiceVersion.V10);
    getBody(response);
    response = callUri("$metadata", ODataHttpHeaders.DATASERVICEVERSION, "3.0", HttpStatusCodes.BAD_REQUEST);
    checkVersion(response, ODataServiceVersion.V10);
    getBody(response);
    response = callUri("Employees", ODataHttpHeaders.DATASERVICEVERSION, "4.0", HttpStatusCodes.BAD_REQUEST);
    checkVersion(response, ODataServiceVersion.V10);
    getBody(response);
    response = callUri("$metadata", ODataHttpHeaders.DATASERVICEVERSION, "somethingwrong", HttpStatusCodes.BAD_REQUEST);
    checkVersion(response, ODataServiceVersion.V10);
    getBody(response);
    response = callUri("$metadata", ODataHttpHeaders.DATASERVICEVERSION, "3.2", HttpStatusCodes.BAD_REQUEST);
    checkVersion(response, ODataServiceVersion.V10);
  }

  @Test
  public void testDataServiceVersionNotSetOnMetadata() throws Exception {
    checkVersion(callUri("$metadata"), ODataServiceVersion.V20);
  }

  @Test
  public void testDataServiceVersionSetOnMetadata() throws Exception {
    HttpResponse response = callUri("$metadata", ODataHttpHeaders.DATASERVICEVERSION, "1.0");
    checkVersion(response, ODataServiceVersion.V20);
    getBody(response);

    response = callUri("$metadata", ODataHttpHeaders.DATASERVICEVERSION, "2.0");
    checkVersion(response, ODataServiceVersion.V20);
    getBody(response);
  }

  @Test
  public void testDataServiceVersionNotSetOnServiceDocument() throws Exception {
    checkVersion(callUri(""), ODataServiceVersion.V10);
  }

  @Test
  public void testDataServiceVersionSetOnServiceDocument() throws Exception {
    HttpResponse response = callUri("", ODataHttpHeaders.DATASERVICEVERSION, "1.0");
    checkVersion(response, ODataServiceVersion.V10);
    getBody(response);

    response = callUri("", ODataHttpHeaders.DATASERVICEVERSION, "2.0");
    checkVersion(response, ODataServiceVersion.V10);
  }
}
