package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.exception.ODataMessageException;

public interface OrderByParser 
{
  abstract OrderByExpression parseExpression(String expression) throws FilterParserException, ODataMessageException;
}