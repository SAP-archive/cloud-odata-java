package com.sap.core.odata.api.uri.expression;

/**
 * Represents a unary expression node in the expression tree returned by the methods 
 * <li>{@link FilterParser#ParseExpression(String)}</li>
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * A unary expression node is inserted in the expression tree for any valid
 * ODATA unary operator in {@link UnaryOperator} (e.g. for "not or "-" )
 * <br>
 * <br>
 * @author SAP AG
 * @see {@link FilterParser}
 * @see {@link OrderByParser}
 */
public interface UnaryExpression extends CommonExpression 
{

  /**
   * @return Operator object that represent the operator
   * @see UnaryOperator
   */
  public UnaryOperator getOperator();

  public CommonExpression getOperand();

}