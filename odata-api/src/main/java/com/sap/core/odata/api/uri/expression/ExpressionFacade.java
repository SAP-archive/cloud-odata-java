package com.sap.core.odata.api.uri.expression;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.rt.RuntimeDelegate;

/**
 * Overs functionality for parsing a $filter Expression or a $orderby expression 
 * @author SAP AG
 */
public class ExpressionFacade
{
  /**
   * Parses a $filter expression string and create an expression tree
   * @param edm
   *   Edm model of the accessed OData service 
   * @param edmType
   *   Edm type of the OData entity/complex type/.. addressed by the URL.
   * @param expression
   *   $filter expression string to be parsed
   * @return
   *   Expression tree which can be traversed with help of the interfaces {@link ExpressionVisitor} and {@link Visitable}
   * @throws FilterParserException
   *   Exception thrown due to errors while parsing the $filter expression string 
   * @throws ODataMessageException
   *   Used for extensibility
   */
  public static FilterExpression parseFilter(Edm edm, EdmEntityType edmType, String expression) throws FilterParserException, ODataMessageException
  {
    return RuntimeDelegate.getFilterParser(edm, edmType).parseFilterString(expression);
  }

  /**
   * Parses a $orderby expression string and create an expression tree for easy consume
   * @param edm
   *   EDM model of the accessed OData service 
   * @param edmType
   *   EDM type of the OData entity/complex type/.. addressed by the URL.
   * @param expression
   *   $orderby expression to be parsed
   * @return
   *   Expression tree which can be traversed with help of the interfaces {@link ExpressionVisitor} and {@link Visitable}
   * @throws OrderByParserException
   *   Exception thrown due to errors while parsing the $orderby expression string 
   * @throws ODataMessageException
   *   Used for extensibility
   */
  public static OrderByExpression parseOrderBy(Edm edm, EdmEntityType edmType, String expression) throws OrderByParserException, ODataMessageException
  {
    return RuntimeDelegate.getOrderByParser(edm, edmType).parseOrderByString(expression);
  }
}
