package com.sap.core.odata.api.uri.expression;

import java.util.List;

public interface FunctionExpression extends CommonExpression
{
  public String GetFunctionName();

  public List<CommonExpression> GetParameters();

  public CommonExpression GetParameterCount();

  public CommonExpression AppendParameter(CommonExpression ls_tmp_node);
}