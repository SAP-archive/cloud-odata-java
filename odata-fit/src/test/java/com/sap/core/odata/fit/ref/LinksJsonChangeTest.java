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

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpContentType;
import com.sap.core.odata.api.commons.HttpStatusCodes;

/**
 * Tests employing the reference scenario changing links in JSON format.
 * @author SAP AG
 */
public final class LinksJsonChangeTest extends AbstractRefTest {

  @Test
  public void createLink() throws Exception {
    final String uriString = "Rooms('3')/$links/nr_Employees";
    final String requestBody = "{\"uri\":\"" + getEndpoint() + "Employees('6')\"}";
    postUri(uriString, requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertEquals("{\"d\":" + requestBody + "}", getBody(callUri(uriString + "('6')?$format=json")));
  }

  @Test
  public void updateLink() throws Exception {
    final String uriString = "Employees('2')/$links/ne_Room";
    final String requestBody = "{\"uri\":\"" + getEndpoint() + "Rooms('3')\"}";
    putUri(uriString, requestBody, HttpContentType.APPLICATION_JSON, HttpStatusCodes.NO_CONTENT);
    assertEquals("{\"d\":" + requestBody + "}", getBody(callUri(uriString + "?$format=json")));
  }
}
