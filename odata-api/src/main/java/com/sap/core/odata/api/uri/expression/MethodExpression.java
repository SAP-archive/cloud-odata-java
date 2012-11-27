package com.sap.core.odata.api.uri.expression;

import java.util.Vector;

public interface MethodExpression extends CommonExpression
{

  /**
   * @return Operator object that represent the operator
   * @see MethodOperator
   */
  public MethodOperator getMethod();

  public Vector<CommonExpression> getParameters();

  public CommonExpression getParameterCount();

  public CommonExpression appendParameter(CommonExpression ls_tmp_node);
}