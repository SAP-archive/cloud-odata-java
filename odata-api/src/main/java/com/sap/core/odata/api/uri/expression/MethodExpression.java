package com.sap.core.odata.api.uri.expression;

import java.util.List;

public interface MethodExpression extends CommonExpression
{

  
  /**
   * @return Operator object that represent the operator
   * @see BinaryExpression.Operator
   */
  public MethodOperator getMethod();

  public List<CommonExpression> getParameters();

  public CommonExpression getParameterCount();

  public CommonExpression appendParameter(CommonExpression ls_tmp_node);
}