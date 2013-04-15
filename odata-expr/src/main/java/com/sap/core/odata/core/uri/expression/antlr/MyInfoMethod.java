/**
 * (c) 2013 by SAP AG
 */
package com.sap.core.odata.core.uri.expression.antlr;

import com.sap.core.odata.api.edm.EdmType;
import com.sap.core.odata.api.uri.expression.MethodOperator;
import com.sap.core.odata.core.uri.expression.ParameterSetCombination;

/**
 * Describes a method expression which is allowed in OData expressions
 * @author SAP AG
 */
class MyInfoMethod {

  public MethodOperator method;
  public String syntax;
  public int minParameter;
  public int maxParameter;
  ParameterSetCombination combination;

  public MyInfoMethod(final MethodOperator method, final String syntax, final ParameterSetCombination combination) {
    this.method = method;
    this.syntax = syntax;
    minParameter = 1;
    maxParameter = 1;
    this.combination = combination;
  }

  public MyInfoMethod(final MethodOperator method, final String syntax, final int minParameters, final int maxParameters, final ParameterSetCombination combination) {
    this.method = method;
    this.syntax = syntax;
    minParameter = minParameters;
    maxParameter = maxParameters;
    this.combination = combination;
  }

  public MethodOperator getMethod() {
    return method;
  }

  public String getSyntax() {
    return syntax;
  }

  public int getMinParameter() {
    return minParameter;
  }

  public int getMaxParameter() {
    return maxParameter;
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
