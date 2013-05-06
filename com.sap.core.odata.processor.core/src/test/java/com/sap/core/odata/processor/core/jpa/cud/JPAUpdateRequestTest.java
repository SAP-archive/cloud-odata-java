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
