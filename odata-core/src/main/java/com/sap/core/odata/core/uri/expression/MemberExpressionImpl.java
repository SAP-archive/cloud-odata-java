package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.CommonExpression;

import com.sap.core.odata.api.uri.expression.BinaryOperator;
import com.sap.core.odata.api.uri.expression.ExpressionKind;

import com.sap.core.odata.api.uri.expression.MemberExpression;

public class MemberExpressionImpl implements MemberExpression 
{

  public MemberExpressionImpl(CommonExpression lo_left_node, CommonExpression lo_right_node) {


  }

  @Override
  public String GetSource() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public CommonExpression GetPath() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EdmType getEdmType() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setEdmType(EdmType edmType) {
    // TODO Auto-generated method stub
    
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
    
    return "/"; //TODO change this
  }
 
}
