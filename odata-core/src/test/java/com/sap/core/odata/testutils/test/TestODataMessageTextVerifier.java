package com.sap.core.odata.testutils.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Vector;

import org.junit.Test;

import com.sap.core.odata.testutils.helper.ODataMessageTextVerifier;
import com.sap.core.odata.testutils.mocks.SampleClassForInvalidMessageReferences;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;

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

    //TODO change to List
    Vector<Throwable> ec = tool.getErrorCollector();
   
    assertEquals("!!!Error in testtool",2,ec.size());

    assertNotNull("!!!Error in testtool", ec.elementAt(0));
    assertEquals("Error", "Error-->Messagetext for key:\"com.sap.core.odata.testutils.mocks.SampleClassForInvalidMessageReferences.DOES_NOT_EXIST\" missing", ec.elementAt(0).getMessage());
    
    assertNotNull("!!!Error in testtool", ec.elementAt(1));
    assertEquals("Error", "Error-->Messagetext for key:\"com.sap.core.odata.testutils.mocks.SampleClassForInvalidMessageReferences.EXITS_BUT_EMPTY\" empty", ec.elementAt(1).getMessage());
    
    
  }

}
