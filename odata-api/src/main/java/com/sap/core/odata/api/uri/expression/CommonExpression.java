package com.sap.core.odata.api.uri.expression;
/*1*/
import com.sap.core.odata.api.edm.EdmType;

public interface CommonExpression 
{
  EdmType getEdmType();
  
  void setEdmType(EdmType edmType);
  
  ExpressionKind getKind();
  
  String toUriLiteral();

}
