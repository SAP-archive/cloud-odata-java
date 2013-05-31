package com.sap.core.odata.processor.core.jpa.access.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sap.core.odata.api.ODataCallback;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.edm.EdmNavigationProperty;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteEntryCallbackResult;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackContext;
import com.sap.core.odata.api.ep.callback.WriteFeedCallbackResult;
import com.sap.core.odata.api.uri.ExpandSelectTreeNode;
import com.sap.core.odata.api.uri.NavigationPropertySegment;
import com.sap.core.odata.processor.core.jpa.common.ODataJPATestConstants;

public class JPAExpandCallBackTest {

  @Test
  public void testRetrieveEntryResult() {
    JPAExpandCallBack callBack = getJPAExpandCallBackObject();
    WriteEntryCallbackContext writeFeedContext = TestUtil.getWriteEntryCallBackContext();
    try {
      Field field = callBack.getClass().getDeclaredField("nextEntitySet");
      field.setAccessible(true);
      field.set(callBack, TestUtil.mockTargetEntitySet());
      WriteEntryCallbackResult result = callBack.retrieveEntryResult(writeFeedContext);
      assertEquals(1, result.getEntryData().size());
    } catch (SecurityException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (NoSuchFieldException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (IllegalArgumentException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (IllegalAccessException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testRetrieveFeedResult() {
    JPAExpandCallBack callBack = getJPAExpandCallBackObject();
    WriteFeedCallbackContext writeFeedContext = TestUtil.getWriteFeedCallBackContext();
    try {
      Field field = callBack.getClass().getDeclaredField("nextEntitySet");
      field.setAccessible(true);
      field.set(callBack, TestUtil.mockTargetEntitySet());
      WriteFeedCallbackResult result = callBack.retrieveFeedResult(writeFeedContext);
      assertEquals(2, result.getFeedData().size());
    } catch (SecurityException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (NoSuchFieldException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (IllegalArgumentException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (IllegalAccessException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  @Test
  public void testGetCallbacks() {
    Map<String, ODataCallback> callBacks = null;
    try {
      URI baseUri = new URI("http://localhost:8080/com.sap.core.odata.processor.ref.web/SalesOrderProcessing.svc/");
      ExpandSelectTreeNode expandSelectTreeNode = TestUtil.mockExpandSelectTreeNode();
      List<ArrayList<NavigationPropertySegment>> expandList = TestUtil.getExpandList();
      callBacks = JPAExpandCallBack.getCallbacks(baseUri, expandSelectTreeNode, expandList);
    } catch (URISyntaxException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    assertEquals(1, callBacks.size());

  }

  @Test
  public void testGetNextNavigationProperty()
  {
    JPAExpandCallBack callBack = getJPAExpandCallBackObject();
    List<ArrayList<NavigationPropertySegment>> expandList = TestUtil.getExpandList();
    ArrayList<NavigationPropertySegment> expands = expandList.get(0);
    expands.add(TestUtil.mockThirdNavigationPropertySegment());
    EdmNavigationProperty result = null;
    try {
      Field field = callBack.getClass().getDeclaredField("expandList");
      field.setAccessible(true);
      field.set(callBack, expandList);
      Class<?>[] formalParams = { EdmEntityType.class, EdmNavigationProperty.class };
      Object[] actualParams = { TestUtil.mockSourceEdmEntityType(), TestUtil.mockNavigationProperty() };
      Method method = callBack.getClass().getDeclaredMethod("getNextNavigationProperty", formalParams);
      method.setAccessible(true);
      result = (EdmNavigationProperty) method.invoke(callBack, actualParams);
      assertEquals("MaterialDetails", result.getName());

    } catch (SecurityException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (NoSuchFieldException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (IllegalArgumentException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (IllegalAccessException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (NoSuchMethodException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (InvocationTargetException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
  }

  private JPAExpandCallBack getJPAExpandCallBackObject()
  {
    Map<String, ODataCallback> callBacks = null;
    try {
      URI baseUri = new URI("http://localhost:8080/com.sap.core.odata.processor.ref.web/SalesOrderProcessing.svc/");
      ExpandSelectTreeNode expandSelectTreeNode = TestUtil.mockExpandSelectTreeNode();
      List<ArrayList<NavigationPropertySegment>> expandList = TestUtil.getExpandList();
      callBacks = JPAExpandCallBack.getCallbacks(baseUri, expandSelectTreeNode, expandList);
    } catch (URISyntaxException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    } catch (EdmException e) {
      fail(ODataJPATestConstants.EXCEPTION_MSG_PART_1 + e.getMessage()
          + ODataJPATestConstants.EXCEPTION_MSG_PART_2);
    }
    return (JPAExpandCallBack) callBacks.get("SalesOrderLineItemDetails");
  }

}
