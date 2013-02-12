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
 * Wrapper for UriParser functionality.
 * @author SAP AG
 */
public abstract class UriParser {

  /**
   * Parses path segments and query parameters for the given EDM.
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
   * Parses path segments and query parameters.
   * @param pathSegments list of path segments
   * @param queryParameters query parameters
   * @return {@link UriInfo} information about the parsed URI
   * @throws UriSyntaxException
   * @throws UriNotMatchingException
   * @throws EdmException
   */
  public abstract UriInfo parse(List<PathSegment> pathSegments, Map<String, String> queryParameters) throws UriSyntaxException, UriNotMatchingException, EdmException;

  /**
   * Parses a $filter expression string and create an expression tree.
   * <p>The current expression parser supports expressions as defined in the
   * OData specification 2.0 with the following restrictions:
   * <ul>
   *   <li>the methods "cast", "isof" and "replace" are not supported</li>
   * </ul></p>
   *
   * <p>The expression parser can be used with providing an Entity Data Model (EDM)
   * and without providing it. When an EDM is provided the expression parser will be
   * as strict as possible. That means:
   * <ul>
   *   <li>All properties used in the expression must be defined inside the EDM,</li>
   *   <li>the types of EDM properties will be checked against the lists of allowed
   *       types per method and per binary or unary operator, respectively</li>
   * </ul>
   * If no EDM is provided the expression parser performs a lax validation:
   * <ul>
   *   <li>The properties used in the expression are not looked up inside the EDM
   *       and the type of the expression node representing the property will be "null",</li>
   *   <li>expression nodes with EDM type "null" are not considered during the parameter
   *       type validation, so the return type of the parent expression node will
   *       also become "null".</li>
   * </ul>
   * @param edm        entity data model of the accessed OData service 
   * @param edmType    EDM type of the OData entity/complex type/... addressed by the URL
   * @param expression $filter expression string to be parsed
   * @return           expression tree which can be traversed with help of the interfaces
   *                   {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExpressionParserException thrown due to errors while parsing the $filter expression string
   * @throws ODataMessageException     for extensibility
   */
  public static FilterExpression parseFilter(Edm edm, EdmEntityType edmType, String expression) throws ExpressionParserException, ODataMessageException
  {
    return RuntimeDelegate.getUriParser(edm).parseFilterString(edmType, expression);
  }

  /**
   * Parses a $filter expression string and create an expression tree.
   * <p>The current expression parser supports expressions as defined in the
   * OData specification 2.0 with the following restrictions:
   * <ul>
   *   <li>the methods "cast", "isof" and "replace" are not supported</li>
   * </ul></p>
   *
   * <p>The expression parser can be used with providing an Entity Data Model (EDM)
   * and without providing it. When an EDM is provided the expression parser will be
   * as strict as possible. That means:
   * <ul>
   *   <li>All properties used in the expression must be defined inside the EDM,</li>
   *   <li>the types of EDM properties will be checked against the lists of allowed
   *       types per method and per binary or unary operator, respectively</li>
   * </ul>
   * If no EDM is provided the expression parser performs a lax validation:
   * <ul>
   *   <li>The properties used in the expression are not looked up inside the EDM
   *       and the type of the expression node representing the property will be "null",</li>
   *   <li>expression nodes with EDM type "null" are not considered during the parameter
   *       type validation, so the return type of the parent expression node will
   *       also become "null".</li>
   * </ul>
   * @param edmType    EDM type of the OData entity/complex type/... addressed by the URL
   * @param expression $filter expression string to be parsed
   * @return           expression tree which can be traversed with help of the interfaces
   *                   {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExpressionParserException thrown due to errors while parsing the $filter expression string
   * @throws ODataMessageException     for extensibility
   */
  public abstract FilterExpression parseFilterString(EdmEntityType edmType, String expression) throws ExpressionParserException, ODataMessageException;

  /**
   * Parses a $orderby expression string and creates an expression tree.
   * @param edm        EDM model of the accessed OData service 
   * @param edmType    EDM type of the OData entity/complex type/... addressed by the URL
   * @param expression $orderby expression string to be parsed
   * @return           expression tree which can be traversed with help of the interfaces
   *                   {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExpressionParserException thrown due to errors while parsing the $orderby expression string
   * @throws ODataMessageException     used for extensibility
   */
  public static OrderByExpression parseOrderBy(Edm edm, EdmEntityType edmType, String expression) throws ExpressionParserException, ODataMessageException
  {
    return RuntimeDelegate.getUriParser(edm).parseOrderByString(edmType, expression);
  }

  /**
   * Parses a $orderby expression string and creates an expression tree.
   * @param edmType    EDM type of the OData entity/complex type/... addressed by the URL
   * @param expression $orderby expression string to be parsed
   * @return           expression tree which can be traversed with help of the interfaces
   *                   {@link ExpressionVisitor} and {@link Visitable}
   * @throws ExpressionParserException thrown due to errors while parsing the $orderby expression string
   * @throws ODataMessageException     used for extensibility
   */
  public abstract OrderByExpression parseOrderByString(EdmEntityType edmType, String expression) throws ExpressionParserException, ODataMessageException;
}
