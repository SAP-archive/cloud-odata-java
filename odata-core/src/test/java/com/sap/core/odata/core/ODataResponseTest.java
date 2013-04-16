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
package com.sap.core.odata.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.sap.core.odata.api.commons.HttpStatusCodes;
import com.sap.core.odata.api.processor.ODataResponse;
import com.sap.core.odata.testutil.fit.BaseTest;

public class ODataResponseTest extends BaseTest {

  @Test
  public void buildStatusResponseTest() {
    ODataResponse response = ODataResponse.status(HttpStatusCodes.FOUND).build();
    assertEquals(HttpStatusCodes.FOUND, response.getStatus());
  }

  @Test
  public void buildEntityResponseTest() {
    ODataResponse response = ODataResponse.entity("abc").build();
    assertNull(response.getStatus());
    assertEquals("abc", response.getEntity());
  }

  @Test
  public void buildHeaderResponseTest() {
    ODataResponse response = ODataResponse
        .header("abc", "123")
        .header("def", "456")
        .build();
    assertNull(response.getStatus());
    assertEquals("123", response.getHeader("abc"));
    assertEquals("456", response.getHeader("def"));
  }

}
