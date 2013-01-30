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

  public InfoMethod(MethodOperator method, ParameterSetCombination combination)
  {
    this.method = method;
    this.syntax = method.toUriLiteral();
    this.minParameter = 1;
    this.maxParameter = 1;
    this.combination = combination;
  }

  public InfoMethod(MethodOperator method, int minParameters, int maxParameters, ParameterSetCombination combination)
  {
    this.method = method;
    this.syntax = method.toUriLiteral();
    this.minParameter = minParameters;
    this.maxParameter = maxParameters;
    this.combination = combination;
  }

  public InfoMethod(MethodOperator method, String string, int minParameters, int maxParameters, ParameterSetCombination combination) {
    this.method = method;
    this.syntax = string;
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

  public ParameterSet validateParameterSet(List<EdmType> actualParameterTypes) throws ExpressionParserInternalError
  {
    return combination.validate(actualParameterTypes);
  }

  /**
   * Returns the EdmType of the returned value of a Method
   * If a method may have different return types (depending on the input type) null will be returned. 
   */
  public EdmType getReturnType() {
     return combination.getReturnType();

  }
}
