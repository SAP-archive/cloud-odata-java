package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.exception.ODataApplicationException;
import com.sap.core.odata.api.uri.expression.BinaryExpression;
import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExceptionVisitExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.ExpressionVisitor;

public class BinaryExpressionImpl implements BinaryExpression
{
  protected InfoBinaryOperator operatorInfo;
  protected CommonExpression leftSide;
  protected CommonExpression rightSide;
  protected EdmType edmType;

  public BinaryExpressionImpl(InfoBinaryOperator operatorInfo, CommonExpression leftSide, CommonExpression rightSide) {
    this.operatorInfo = operatorInfo;
    this.leftSide = leftSide;
    this.rightSide = rightSide;
    edmType = null;
  }

  @Override
  public BinaryOperator getOperator() 
  {
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
  public CommonExpression setEdmType(EdmType edmType)
  {
    this.edmType = edmType;
    return this;
  }

  @Override
  public ExpressionKind getKind()
  {
    return ExpressionKind.BINARY;
  }

  @Override
  public String getUriLiteral() 
  {
    return operatorInfo.getSyntax();
  }

  @Override
  public Object accept(ExpressionVisitor visitor) throws ExceptionVisitExpression, ODataApplicationException
  {
    Object retLeftSide = leftSide.accept(visitor);
    Object retRightSide = rightSide.accept(visitor);

    Object ret = visitor.visitBinary(this, operatorInfo.getOperator(), retLeftSide, retRightSide);
    return ret;
  }

}
