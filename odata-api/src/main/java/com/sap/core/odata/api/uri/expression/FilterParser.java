package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.exception.ODataMessageException;



public interface FilterParser 
{
  /**
   * Parses a filter expression and create an expression tree for easy consume 
   * @param expression
   *   Expression to be parsed
   * @return
   *   Expression tree which can be traversed with help of the interfaces {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExceptionParseExpression
   * @throws ODataMessageException
   */
  abstract FilterExpression ParseExpression(String expression) throws ExceptionParseExpression, ODataMessageException;
  
}
