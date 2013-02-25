package com.sap.core.odata.core.uri.expression.antlr;

/*1*/

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;
import com.sap.core.odata.api.uri.expression.UnaryExpression;
import com.sap.core.odata.api.uri.expression.UnaryOperator;

public class MyUnaryExpressionImpl implements UnaryExpression {
  private UnaryOperator operator = null;
  private CommonExpression operand = null;
  private EdmType edmType = null;

  public MyUnaryExpressionImpl(UnaryOperator operator, CommonExpression operand) {
    this.operator = operator;
    this.operand = operand;
    //   this.edmType = operatorInfo.getReturnType();
  }

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.UNARY;
  }

  @Override
  public UnaryOperator getOperator() {
    return operator;
  }

  @Override
  public CommonExpression getOperand() {
    return operand;
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
  public String getUriLiteral() {
    return null;

  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException {
    final Object retOperand = operand.accept(visitor);

    final Object ret = visitor.visitUnary(this, operator, retOperand);
    return ret;
  }

}
