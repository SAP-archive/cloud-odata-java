package com.sap.core.odata.core.uri.expression;

import org.junit.Test;

import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.testutil.helper.ODataMessageTextVerifier;

public class TestExceptionTexts extends TestBase
{
  @Test
  public void TestFilterParserExceptionMessages()
  {
    ODataMessageTextVerifier.TestClass(ExpressionParserException.class);
  }

  @Test
  public void TestFilterParserInternalErrorMessages()
  {
    ODataMessageTextVerifier.TestClass(ExpressionParserInternalError.class);
  }

  @Test
  public void TestExceptionVisitExpressionMessages()
  {
    ODataMessageTextVerifier.TestClass(ExceptionVisitExpression.class);
  }

  @Test
  public void TestExceptionTokenizerExpectMessages()
  {
    ODataMessageTextVerifier.TestClass(TokenizerExpectError.class);
  }

  @Test
  public void TestExceptionTokenizerMessages()
  {
    ODataMessageTextVerifier.TestClass(TokenizerException.class);
  }
}
