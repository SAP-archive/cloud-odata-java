package com.sap.core.odata.api.uri;

import java.util.List;
import java.util.Map;

import com.sap.core.odata.api.edm.Edm;
import com.sap.core.odata.api.edm.EdmEntityType;
import com.sap.core.odata.api.edm.EdmException;
import com.sap.core.odata.api.exception.ODataException;
import com.sap.core.odata.api.exception.ODataMessageException;
import com.sap.core.odata.api.rt.RuntimeDelegate;
import com.sap.core.odata.api.uri.expression.ExpressionParserException;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.FilterExpression;
import com.sap.core.odata.api.uri.expression.OrderByExpression;
import com.sap.core.odata.api.uri.expression.Visitable;

/**
 * Class to wrap UriParser functionality 
 * @author SAP AG
 */
public abstract class UriParser {

  /**
   * Parse path segments and query parameters for the given EDM
   * @param edm Entity Data Model
   * @param pathSegments list of path segments
   * @param queryParameters query parameters
   * @return {@link UriInfo} information about the parsed URI
   * @throws ODataException
   */
  public static UriInfo parse(Edm edm, List<PathSegment> pathSegments, Map<String, String> queryParameters) throws ODataException {
    return RuntimeDelegate.getUriParser(edm).parse(pathSegments, queryParameters);
  }

  /**
   * Parse path segments and query parameters.
   * @param pathSegments list of path segments
   * @param queryParameters query parameters
   * @return {@link UriInfo} information about the parsed URI
   * @throws UriSyntaxException
   * @throws UriNotMatchingException
   * @throws EdmException
   */
  public abstract UriInfo parse(List<PathSegment> pathSegments, Map<String, String> queryParameters) throws UriSyntaxException, UriNotMatchingException, EdmException;
  
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
   * @throws ExpressionParserException
   *   Exception thrown due to errors while parsing the $filter expression string 
   * @throws ODataMessageException
   *   Used for extensibility
   */
  public static FilterExpression parseFilter(Edm edm, EdmEntityType edmType, String expression) throws ExpressionParserException, ODataMessageException
  {
    return RuntimeDelegate.getUriParser(edm).parseFilterString(edmType,expression);
  }
  
  /**
   * Parses a $filter expression string and create an expression tree
   * @param edmType
   *   Edm type of the OData entity/complex type/.. addressed by the URL.
   * @param expression
   *   $filter expression string to be parsed
   * @return
   *   Expression tree which can be traversed with help of the interfaces {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExpressionParserException
   *   Exception thrown due to errors while parsing the $filter expression string 
   * @throws ODataMessageException
   *   Used for extensibility
   */
  public abstract FilterExpression parseFilterString(EdmEntityType edmType, String expression) throws ExpressionParserException, ODataMessageException;

  /**
   * Parses a $orderby expression string and create an expression tree for easy consume
   * @param edm
   *   EDM model of the accessed OData service 
   * @param edmType
   *   EDM type of the OData entity/complex type/.. addressed by the URL.
   * @param expression
   *   $orderby expression string to be parsed
   * @return
   *   Expression tree which can be traversed with help of the interfaces {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExpressionParserException
   *   Exception thrown due to errors while parsing the $orderby expression string 
   * @throws ODataMessageException
   *   Used for extensibility
   */
  public static OrderByExpression parseOrderBy(Edm edm, EdmEntityType edmType, String expression) throws ExpressionParserException, ODataMessageException
  {
    return RuntimeDelegate.getUriParser(edm).parseOrderByString(edmType,expression);
  }
  
  /**
   * Parses a $orderby expression string and create an expression tree for easy consume
   * @param edmType
   *   EDM type of the OData entity/complex type/.. addressed by the URL.
   * @param expression
   *   $orderby expression string to be parsed
   * @return
   *   Expression tree which can be traversed with help of the interfaces {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExpressionParserException
   *   Exception thrown due to errors while parsing the $orderby expression string 
   * @throws ODataMessageException
   *   Used for extensibility
   */
  public abstract OrderByExpression parseOrderByString( EdmEntityType edmType, String expression) throws ExpressionParserException, ODataMessageException;
}
