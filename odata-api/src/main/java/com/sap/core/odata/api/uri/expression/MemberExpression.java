package com.sap.core.odata.api.uri.expression;

public interface MemberExpression extends CommonExpression
{
  public CommonExpression getSource();

  public CommonExpression getPath();
}