package com.sap.core.odata.core.uri.expression;

import java.util.Vector;

import com.sap.core.odata.api.edm.EdmType;
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
  
  public void addParameterSet(ParameterSet parameterSet)
  {
    allowedParameterTypes.add(parameterSet);
  }
  
  /**
   * Returns the EdmType of the returned value of a Method
   * If a method may have different return types (depending on the input type) null will be returned. 
   */
  public EdmType getReturnType()
  {
    int parameterCount = allowedParameterTypes.size();
    if (parameterCount == 0)
      return null;
    
    if (parameterCount == 1)
      return allowedParameterTypes.elementAt(0).getReturnType();
    
    //There are more than 1 possible return type, check if they are equal, if not return null.
    EdmType returnType =  allowedParameterTypes.elementAt(0).getReturnType();
    for ( int i = 1; i < parameterCount; i++)
      if (returnType != allowedParameterTypes.elementAt(i))
        return null;
   
    return returnType;
  }
 
}
