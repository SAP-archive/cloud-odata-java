package com.sap.core.odata.api.uri.expression;
/*1*/
import com.sap.core.odata.api.edm.EdmType;

/**
 * Parent class of all classes used to build the expression tree returned by 
 * <li>{@link FilterParser#ParseExpression(String)}</li>
 * <li>{@link OrderByParser#parseOrderExpression(String)}</li> 
 * <br>
 * <br>
 * This class defines the default methods for all expression tree nodes 
 * <br>
 * <br>
 * @author SAP AG
 * @see FilterParser
 * @see OrderByParser
 */
public interface CommonExpression extends Visitable
{
  
  void setEdmType(EdmType edmType);
  
  EdmType getEdmType();
  
  ExpressionKind getKind();
  
  String toUriLiteral();
}
