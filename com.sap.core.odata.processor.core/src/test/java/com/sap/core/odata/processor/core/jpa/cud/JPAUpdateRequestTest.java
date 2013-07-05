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
package com.sap.core.odata.processor.core.jpa.cud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.uri.info.PutMergePatchUriInfo;
import com.sap.core.odata.processor.api.jpa.exception.ODataJPARuntimeException;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;

public class JPAUpdateRequestTest {

  @Test
  public void testProcess() {

    JPAUpdateRequest updateRequest = new JPAUpdateRequest();
    PutMergePatchUriInfo putUriInfo = JPATestUtil.getPutMergePatchUriInfo();
    try {
      updateRequest.process(JPATestUtil.getJPAEntity(), putUriInfo, null, "application/xml");
    } catch (ODataJPARuntimeException e) {
      if (e.isCausedByMessageException()) {
        assertTrue(true);
      } else {
        fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
            + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
      }
    }

  }

  @Test
  public void testParse2JPAEntityValueMap() {
    JPAUpdateRequest updateRequest = new JPAUpdateRequest();
    final EdmNavigationProperty navigationProperty = JPATestUtil.mockNavigationProperty();
    EdmEntityType edmEntityType = JPATestUtil.mockEdmEntityType(navigationProperty);
    Map<String, Object> propertyValueMap = JPATestUtil.getPropertyValueMap();
    propertyValueMap.put("description", "desktop");
    Object result = null;
    try {
      result = updateRequest.parse2JPAEntityValueMap(JPATestUtil.getJPAEntity(), edmEntityType, propertyValueMap);
      assertEquals("desktop", ((SalesOrderHeader) result).getDescription());
      assertEquals(1, ((SalesOrderHeader) result).getId());
    } catch (ODataJPARuntimeException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    assertNotNull(result);
    assertEquals(((SalesOrderHeader) result).getId(), 1);
  }

}
