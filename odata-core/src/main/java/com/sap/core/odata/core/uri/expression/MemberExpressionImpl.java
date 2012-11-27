package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.CommonExpression;

import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.ExpressionKind;

import com.sap.core.odata.api.uri.expression.MemberExpression;

public class MemberExpressionImpl implements MemberExpression 
{
  CommonExpression source;
  CommonExpression path;
  EdmType edmType;
  
  public MemberExpressionImpl(CommonExpression source, CommonExpression path) 
  {
    this.source = source;
    this.path = path;
    this.edmType = null;
  }

  @Override
  public CommonExpression getSource() {
    return source;
  }

  @Override
  public CommonExpression getPath() {
    return path;
  }

  @Override
  public EdmType getEdmType() 
  {
    return edmType;
  }

  @Override
  public void setEdmType(EdmType edmType) {
    this.edmType = edmType;
  }
  
  public BinaryOperator getOperator()
  {
    return BinaryOperator.PROPERTY_ACCESS;
  }
  
  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.MEMBER;
  }

  @Override
  public String toUriLiteral() {
    
    return CharConst.MEMBER_OPERATOR; 
  }
 
}
