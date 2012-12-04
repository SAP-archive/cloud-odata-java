package com.sap.core.odata.core.uri.expression.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Vector;

import org.junit.Test;

public class TestExceptionTextTool
{

  @Test
  public void TestExceptionText()
  {
    ExceptionTextsTool tool = new ExceptionTextsTool();
    tool.CheckMessagesOfClass(SampleClassForMessageReference.class);

    Vector<Throwable> ec = tool.getErrorCollector();

    assertNotNull("!!!Error in testtool", ec.elementAt(0));
    assertEquals("Error", "Error-->Messagetext for key:\"com.sap.core.odata.core.uri.expression.test.SampleClassForMessageReference.DOES_NOT_EXIST\" missing", ec.elementAt(0).getMessage());
    
    assertNotNull("!!!Error in testtool", ec.elementAt(1));
    assertEquals("Error", "Error-->Messagetext for key:\"com.sap.core.odata.core.uri.expression.test.SampleClassForMessageReference.EXITS_BUT_EMPTY\" empty", ec.elementAt(1).getMessage());
  }

}
