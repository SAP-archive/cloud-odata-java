package com.sap.core.odata.core.uri.expression;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.EdmLiteral;
import com.sap.core.odata.api.uri.expression.ExpressionKind;
import com.sap.core.odata.api.uri.expression.LiteralExpression;

public class LiteralExpressionImpl implements LiteralExpression {

  EdmType edmType;         
  EdmLiteral javaLiteral;
  String uriLiteral;
  
  public LiteralExpressionImpl(String uriLiteral, EdmLiteral javaLiteral) {
    this.uriLiteral = uriLiteral;
    this.javaLiteral = javaLiteral;
    this.edmType = this.javaLiteral.getType();
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

  @Override
  public ExpressionKind getKind() {
    return ExpressionKind.LITERAL;
  }

  @Override
  public String toUriLiteral() {
    return uriLiteral;
  }

  

}
