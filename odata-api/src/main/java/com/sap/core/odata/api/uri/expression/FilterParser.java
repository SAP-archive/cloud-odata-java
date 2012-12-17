package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * Interface which defines a method for parsing a $filter expression to allow different parser implementations
 * @author SAP AG
 */
public interface FilterParser
{
  /**
   * Parses a $filter expression string and creates an $orderby expression tree 
   * @param expression
   *   The $filter expression string ( for example "city eq 'Sydney'" ) to be parsed
   * @return
   *   Expression tree which can be traversed with help of the interfaces {@link ExpressionVisitor} and {@link Visitable}
   * @throws FilterParserException
   *   Exception thrown due to errors while parsing the $filter expression string 
   * @throws ODataMessageException
   *   Used for extensibility   
   **/
  abstract FilterExpression parseFilterString(String expression) throws FilterParserException, ODataMessageException;
}
