package com.sap.core.odata.processor.core.jpa.access.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Method;

import org.junit.Test;

import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;
import com.sap.core.odata.processor.core.jpa.mock.data.EdmMockUtil;
import com.sap.core.odata.processor.core.jpa.mock.data.JPATypeMock;

public class JPAEntityPropertyTest extends JPAEntity {

  public JPAEntityPropertyTest() {
    super(EdmMockUtil.mockTargetEdmEntityType(), EdmMockUtil.mockTargetEntitySet());
  }

  @Test
  public void testSetKeyPropertySimple() {

    final String EXPECTED = new String("ABC");

    Method method = null;
    JPATypeMock typeMock = new JPATypeMock();
    try {
      method = JPATypeMock.class.getMethod("setMString", new Class[] { String.class });
      setProperty(method, typeMock, EXPECTED);
    } catch (Exception e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    assertEquals(typeMock.getMString(), EXPECTED);
  }

  @Test
  public void testSetProperty() {

    final int EXPECTED = 20;

    Method method = null;
    JPATypeMock typeMock = new JPATypeMock();
    try {

      method = JPATypeMock.class.getMethod("setMInt", new Class[] { int.class });
      setProperty(method, typeMock, EXPECTED);
    } catch (Exception e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    assertEquals(typeMock.getMInt(), EXPECTED);

  }

  @Test
  public void testInstantiateJPAEntity() {
    Object jpaEntity = null;
    try {
      jpaEntity = instantiateJPAEntity();
    } catch (Exception e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }

    assertEquals(JPATypeMock.class, jpaEntity.getClass());
  }

}
