package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.uri.expression.FilterParserException;

public class ErrorFactory {
  
  public static class Filter
  {
    public static FilterParserException errorInTokenizer(ExceptionTokenizer tokenizerException)
    {
      return new FilterParserException(FilterParserException.ERROR_IN_TOKENIZER, tokenizerException);
    }
  }
  
  

}
