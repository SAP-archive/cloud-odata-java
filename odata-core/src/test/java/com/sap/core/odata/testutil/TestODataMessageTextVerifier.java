package com.sap.core.odata.testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.sap.core.odata.testutil.helper.ODataMessageTextVerifier;
import com.sap.core.odata.testutil.mock.SampleClassForInvalidMessageReferences;

/**
 * This class tests the {@link ODataMessageTextVerifier}
 * @Author SAP AG
 */
public class TestODataMessageTextVerifier
{

  @Test
  public void TestExceptionText()
  {
    ODataMessageTextVerifier tool = new ODataMessageTextVerifier();
    tool.CheckMessagesOfClass(SampleClassForInvalidMessageReferences.class);

    List<Throwable> ec = tool.getErrorCollector();

    assertEquals("!!!Error in testtool", 2, ec.size());

    assertNotNull("!!!Error in testtool", ec.get(0));
    assertEquals("Error", "Error-->Messagetext for key:\"com.sap.core.odata.testutil.mock.SampleClassForInvalidMessageReferences.DOES_NOT_EXIST\" missing", ec.get(0).getMessage());

    assertNotNull("!!!Error in testtool", ec.get(1));
    assertEquals("Error", "Error-->Messagetext for key:\"com.sap.core.odata.testutil.mock.SampleClassForInvalidMessageReferences.EXITS_BUT_EMPTY\" empty", ec.get(1).getMessage());

  }

}
