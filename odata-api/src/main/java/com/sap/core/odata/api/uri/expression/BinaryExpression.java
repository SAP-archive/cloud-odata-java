package com.sap.core.odata.api.uri.expression;
/*1*/
/**
 * Represents a binary expression node in the expression tree returned by the methods 
 * <li>{@link FilterParser#ParseExpression(String)}</li>
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * A binary expression node is inserted in the expression tree for any valid
 * ODATA binary operator in {@link BinaryOperator} (e.g. for "and", "div", "eg", ... )
 * <br>
 * <br>
 * @author SAP AG
 * @see {@link FilterParser}
 * @see {@link OrderByParser}
 */
public interface BinaryExpression extends CommonExpression
{

  /**
    * @return Operator object that represent the operator
    * @see BinaryOperator
    */
  public BinaryOperator getOperator();

  public CommonExpression getLeftOperand();

  public CommonExpression getRightOperand();
}
