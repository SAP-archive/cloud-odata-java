package com.sap.core.odata.core.uri.expression.antlr;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;

public class MyBinaryExpressionImpl implements BinaryExpression {
  private final BinaryOperator operator;
  CommonExpression leftSide;
  CommonExpression rightSide;
  EdmType edmType;

  public MyBinaryExpressionImpl(BinaryOperator operator, CommonExpression leftSide, CommonExpression rightSide) {
    this.operator = operator;
    this.leftSide = leftSide;
    this.rightSide = rightSide;
    edmType = null;

  }

  @Override
  public BinaryOperator getOperator() {
    return operator;
  }

  @Override
  public CommonExpression getLeftOperand() {
    return leftSide;
  }

  @Override
  public CommonExpression getRightOperand() {
    return rightSide;
  }

  @Override
  public EdmType getEdmType() {
    return edmType;
  }

  @Override
  public CommonExpression setEdmType(EdmType edmType) {
    this.edmType = edmType;
    return this;
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.BINARY;
  }

  @Override
  public String getUriLiteral() {
    return null;
  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException {
    final Object retLeftSide = leftSide.accept(visitor);
    final Object retRightSide = rightSide.accept(visitor);

    final Object ret = visitor.visitBinary(this, operator, retLeftSide, retRightSide);
    return ret;
  }

}
