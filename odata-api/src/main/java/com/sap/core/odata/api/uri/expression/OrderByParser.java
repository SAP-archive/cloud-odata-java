package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * Interface which defines a method for parsing a $orderby expression to allow different parser implementations   
 * @author SAP AG
 */
public interface OrderByParser
{
  /**
   * Parses a $orderby expression string and creates an $orderby expression tree
   * @param expression
   *   The $orderby expression string ( for example "name asc" ) to be parsed
   * @return
   *    The $orderby expression tree
   * @throws OrderByParserException
   *   Exception thrown due to errors while parsing the $orderby expression string 
   * @throws ODataMessageException
   *   Used for extensibility
   */
  abstract OrderByExpression parseOrderByString(String orderByExpression) throws OrderByParserException, ODataMessageException;
}