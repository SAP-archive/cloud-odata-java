package com.sap.core.odata.core.uri.expression;

import java.util.Vector;

import com.sap.core.odata.api.uri.expression.MethodOperator;

/**
 * Describes a method expression which is allowed in OData expressions
 */
class InfoMethod
{
  MethodOperator method;
  private String syntax;
  private int minParameter;
  private int maxParameter;
  private Vector<ParameterSet> allowedParameterTypes = null;

  public InfoMethod(MethodOperator method, String syntax, int minParameters, int maxParameters) {
    allowedParameterTypes = new Vector<ParameterSet>();
    
    this.method = method;
    this.syntax = syntax;
    this.minParameter = minParameters;
    this.maxParameter = maxParameters;
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
  
  public void addParameterSet(ParameterSet parameterSet)
  {
    allowedParameterTypes.add(parameterSet);
  }
 
}
