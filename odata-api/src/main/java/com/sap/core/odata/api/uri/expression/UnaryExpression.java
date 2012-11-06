package com.sap.core.odata.api.uri.expression;

public interface UnaryExpression extends CommonExpression 
{
   public String GetOperator();
   public CommonExpression Getoperand();
}