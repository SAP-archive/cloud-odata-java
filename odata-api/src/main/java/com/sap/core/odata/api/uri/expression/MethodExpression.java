package com.sap.core.odata.api.uri.expression;

import java.util.Vector;

public interface MethodExpression extends CommonExpression
{

  /**
   * @return Method
   * @see MethodOperator
   */
  public MethodOperator getMethod();
  
  public int getParameterCount();

  public Vector<CommonExpression> getParameters();

  
}