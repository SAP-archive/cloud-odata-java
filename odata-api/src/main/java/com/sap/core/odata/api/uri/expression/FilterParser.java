package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.exception.ODataMessageException;

/**
 * Interface which defines a method for parsing a $filter expression to allow different parser implementations
 * <p>
 * The current expression parser supports expressions as defined in the OData specification 2.0 with the following restrictions
 *   - the methods "cast", "isof" and "replace" are not supported
 *   
 * The expression parser can be used with providing an Entity Data Model (EDM) an without providing it.
 *  <p>When a EDM is provided the expression parser will be as strict as possible. That means:
 *  <li>All properties used in the expression must be defined inside the EDM</li>
 *  <li>The types of EDM properties will be checked against the lists of allowed type per method, binary- and unary operator</li>
 *  </p>
 *  <p>If no EDM is provided the expression parser performs a lax validation
 *  <li>The properties used in the expression are not looked up inside the EDM and the type of the expression node representing the 
 *      property will be "null"</li>
 *  <li>Expression node with EDM-types which are "null" are not considered during the parameter type validation, to the return type of the parent expression node will
 *  also become "null"</li>
 *  
 * @author SAP AG
 */
public interface FilterParser
{
  /**
   * Parses a $filter expression string and creates an $orderby expression tree.
   * @param expression
   *   The $filter expression string ( for example "city eq 'Sydney'" ) to be parsed
   * @return
   *   Expression tree which can be traversed with help of the interfaces {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExpressionParserException
   *   Exception thrown due to errors while parsing the $filter expression string 
   * @throws ODataMessageException
   *   Used for extensibility   
   **/
  FilterExpression parseFilterString(String expression) throws ExpressionParserException, ODataMessageException;
}
