package com.sap.core.odata.api.uri.expression;
/*1*/
import com.sap.core.odata.api.edm.EdmType;


public interface CommonExpression extends Visitable
{
  
  void setEdmType(EdmType edmType);
  
  EdmType getEdmType();
  
  ExpressionKind getKind();
  
  String toUriLiteral();
}
