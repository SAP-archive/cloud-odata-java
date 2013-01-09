package com.sap.core.odata.core.uri.expression;

import java.util.List;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.MethodOperator;

/**
 * Describes a method expression which is allowed in OData expressions
 * @author SAP AG
 */
class InfoMethod
{

  public MethodOperator method;
  public String syntax;
  public int minParameter;
  public int maxParameter;
  ParameterSetCombination combination;

  public InfoMethod(MethodOperator method, String syntax, ParameterSetCombination combination)
  {
    this.method = method;
    this.syntax = syntax;
    this.minParameter = 1;
    this.maxParameter = 1;
    this.combination = combination;
  }

  public InfoMethod(MethodOperator method, String syntax, int minParameters, int maxParameters, ParameterSetCombination combination)
  {
    this.method = method;
    this.syntax = syntax;
    this.minParameter = minParameters;
    this.maxParameter = maxParameters;
    this.combination = combination;
  }

  public MethodOperator getMethod()
  {
    return method;
  }

  public String getSyntax()
  {
    return syntax;
  }

  public int getMinParameter()
  {
    return minParameter;
  }

  public int getMaxParameter()
  {
    return maxParameter;
  }

  public EdmType validateParameterSet(List<EdmType> actualParameterTypes) throws FilterParserInternalError
  {
    return combination.validate(actualParameterTypes);
  }

  /**
   * Returns the EdmType of the returned value of a Method
   * If a method may have different return types (depending on the input type) null will be returned. 
   */
  public EdmType getReturnType() {
    /*  int parameterCount = allowedParameterTypes.size();
      if (parameterCount == 0)
        return null;

      if (parameterCount == 1)
        return allowedParameterTypes.get(0).getReturnType();

      //There are more than 1 possible return type, check if they are equal, if not return null.
      EdmType returnType = allowedParameterTypes.get(0).getReturnType();
      for (int i = 1; i < parameterCount; i++)
        if (returnType != allowedParameterTypes.get(i))
          return null;

      return returnType;*/
    //TODO
    return null;
  }
}
