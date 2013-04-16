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

  public InfoMethod(final MethodOperator method, final ParameterSetCombination combination)
  {
    this.method = method;
    syntax = method.toUriLiteral();
    minParameter = 1;
    maxParameter = 1;
    this.combination = combination;
  }

  public InfoMethod(final MethodOperator method, final int minParameters, final int maxParameters, final ParameterSetCombination combination)
  {
    this.method = method;
    syntax = method.toUriLiteral();
    minParameter = minParameters;
    maxParameter = maxParameters;
    this.combination = combination;
  }

  public InfoMethod(final MethodOperator method, final String string, final int minParameters, final int maxParameters, final ParameterSetCombination combination) {
    this.method = method;
    syntax = string;
    minParameter = minParameters;
    maxParameter = maxParameters;
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

  public ParameterSet validateParameterSet(final List<EdmType> actualParameterTypes) throws ExpressionParserInternalError
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
