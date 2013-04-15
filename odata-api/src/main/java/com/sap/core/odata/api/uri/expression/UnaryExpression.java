/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.api.uri.expression;

/**
 * Represents a unary expression node in the expression tree returned by the methods:
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseFilterString(com.sap.core.odata.api.edm.EdmEntityType, String)}</li>
 * <li>{@link com.sap.core.odata.api.uri.UriParser#parseOrderByString(com.sap.core.odata.api.edm.EdmEntityType, String)}</li> 
 * <br>
 * <br>
 * <p>A unary expression node is inserted in the expression tree for any valid
 * ODATA unary operator in {@link UnaryOperator} (e.g. for "not or "-" )
 * <br>
 * <br>
 * @author SAP AG
 */
public interface UnaryExpression extends CommonExpression {

  /**
   * @return Returns the operator object that represents the used operator
   * @see UnaryOperator
   */
  public UnaryOperator getOperator();

  /**
   * @return Returns the expression node of the operand of the unary operator
   * @see CommonExpression
   */
  public CommonExpression getOperand();

}