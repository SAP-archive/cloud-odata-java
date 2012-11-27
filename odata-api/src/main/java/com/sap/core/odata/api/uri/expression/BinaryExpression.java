package com.sap.core.odata.api.uri.expression;
/*1*/
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
