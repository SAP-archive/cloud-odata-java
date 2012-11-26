package com.sap.core.odata.core.uri.expression;
/*1*/

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.CommonExpression;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.UnaryOperator;
import com.sap.core.odata.api.uri.expression.UnaryExpression;

public class UnaryExpressionImpl implements UnaryExpression
{
  private InfoUnaryOperator operatorInfo = null;
  private CommonExpression operand = null;
  private EdmType edmType = null;

  public UnaryExpressionImpl(InfoUnaryOperator operatorInfo, CommonExpression operand) {
    this.operatorInfo = operatorInfo;
    this.operand = operand;
    this.edmType = null;
  }
  
  @Override
  public ExpressionKind getKind() 
  {
   return ExpressionKind.UNARY;
  }

  @Override
  public UnaryOperator getOperator() 
  {
    return operatorInfo.operator;
  }

  @Override
  public CommonExpression getoperand() 
  {
    return operand;
  }
  

  @Override
  public EdmType getEdmType() 
  {
    return this.edmType;
  }

  @Override
  public void setEdmType(EdmType edmType) 
  {
    this.edmType = edmType;    
  }

  @Override
  public String toUriLiteral() {
    return operatorInfo.getSyntax();

  }

}
