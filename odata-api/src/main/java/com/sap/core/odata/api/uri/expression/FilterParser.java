package com.sap.core.odata.api.uri.expression;

public interface FilterParser 
{
  abstract FilterExpression ParseExpression(String expression) throws ExpressionException;
}
