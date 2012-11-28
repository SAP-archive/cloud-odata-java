package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;

public class BinaryExpressionImpl implements BinaryExpression
{
  private InfoBinaryOperator operatorInfo;
  CommonExpression leftSide;
  CommonExpression rightSide;
  EdmType edmType;

  public BinaryExpressionImpl(InfoBinaryOperator operatorInfo, CommonExpression leftSide, CommonExpression rightSide) {
    this.operatorInfo = operatorInfo;
    this.leftSide = leftSide;
    this.rightSide = rightSide;
    edmType = null;

  }

  @Override
  public BinaryOperator getOperator() {
    return operatorInfo.getOperator();
  }

  @Override
  public CommonExpression getLeftOperand()
  {
    return leftSide;
  }

  @Override
  public CommonExpression getRightOperand()
  {
    return rightSide;
  }

  @Override
  public EdmType getEdmType()
  {
    return edmType;
  }

  @Override
  public void setEdmType(EdmType edmType)
  {
    this.edmType = edmType;
  }

  @Override
  public ExpressionKind getKind()
  {
    return ExpressionKind.BINARY;
  }

  @Override
  public String toUriLiteral() {
    return operatorInfo.syntax;
  }

  @Override
  public Object accept(ExpressionVisitor visitor)
  {
    Object retLeftSide = leftSide.accept(visitor);
    Object retRightSide = rightSide.accept(visitor);

    Object ret = visitor.visitBinary(this, operatorInfo.operator, retLeftSide, retRightSide);
    return ret;
  }

}
