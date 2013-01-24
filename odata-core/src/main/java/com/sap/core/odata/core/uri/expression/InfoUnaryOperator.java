package com.sap.core.odata.core.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.UnaryOperator;

/**
 * Describes a unary operator which is allowed in OData expressions
 * @author SAP AG
 */
class InfoUnaryOperator
{
  UnaryOperator operator;
  private String category;
  private String syntax;
  ParameterSetCombination combination;

  public InfoUnaryOperator(UnaryOperator operator, String category, ParameterSetCombination combination)
  {
    this.operator = operator;
    this.category = category;
    this.syntax = operator.toUriLiteral();
    this.combination = combination;
  }

  public String getCategory()
  {
    return category;
  }

  public String getSyntax() 
  {
    return syntax;
  }

  public UnaryOperator getOperator() 
  {
    return operator;
  }

  public EdmType validateParameterSet(List<EdmType> actualParameterTypes) throws ExpressionParserInternalError 
  {
    return combination.validate(actualParameterTypes);
  }

  /**
   * Returns the EdmType of the returned value of a Method
   * If a method may have different return types (depending on the input type) null will be returned. 
   */
  /*
  public EdmType getReturnType()
  {
  int parameterCount = allowedParameterTypes.size();
  if (parameterCount == 0)
   return null;

  if (parameterCount == 1)
   return allowedParameterTypes.get(0).getReturnType();

  //There are more than 1 possible return type, check if they are equal, if not return null.
  EdmType returnType = allowedParameterTypes.get(0).getReturnType();
  for (int i = 1; i < parameterCount; i++)
   if (returnType != allowedParameterTypes.get(i))
     return null;

  return returnType;
  }*/

}
