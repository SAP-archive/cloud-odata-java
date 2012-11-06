package com.sap.core.odata.api.uri.expression;

public interface BinaryExpression extends CommonExpression
{
  public String GetOperator();

  public String GetLeftOperand();

  public String GetRightOperand();
}