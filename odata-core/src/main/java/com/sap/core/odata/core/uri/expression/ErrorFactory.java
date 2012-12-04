package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.uri.expression.ExceptionParseExpression;

public class ErrorFactory {
  
  public static class Filter
  {
    public static ExceptionParseExpression errorInTokenizer(ExceptionTokenizer tokenizerException)
    {
      return new ExceptionParseExpression(ExceptionParseExpression.ERROR_IN_TOKENIZER, tokenizerException);
    }
  }
  
  

}
