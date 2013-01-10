package com.sap.core.odata.core.uri.expression;


public class ActualMethodOperator {
     protected InfoMethod method;
      protected Token token;


    public ActualMethodOperator(InfoMethod methodInfo, Token token)
    {
      if (methodInfo == null)
      {
        throw new IllegalArgumentException("operatorInfo parameter must not be null");
      }
      this.method = methodInfo;
      this.token = token;
    }
    
    public Token getToken()
    {
      return token;
    }
    
    public InfoMethod getOP()
    {
      return method;
    }
    
}
