package com.sap.core.odata.core.uri.expression.test;

import org.junit.Test;

import com.sap.core.odata.api.uri.expression.FilterParserException;
import com.sap.core.odata.core.uri.expression.TokenizerExpectError;
import com.sap.core.odata.core.uri.expression.FilterParserInternalError;
import com.sap.core.odata.testutils.helper.ODataMessageTextVerifier;

public class TestExceptionTexts {
  
  
  @Test
  public void TestFilterParserExceptionMessages()
  {
    ODataMessageTextVerifier.TestClass(FilterParserException.class);
  }
  
  @Test
  public void TestFilterParserInternalErrorMessages()
  {
    ODataMessageTextVerifier.TestClass(FilterParserInternalError.class);
  }
  
  @Test
  public void TestExceptionTokenizerExpectMessages()
  {
    ODataMessageTextVerifier.TestClass(TokenizerExpectError.class);
  }
  
 
  

}
