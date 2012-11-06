package com.sap.core.odata.api.uri.expression;

public interface MemberExpression extends CommonExpression
{
  public String GetSource();

  public String GetPath();
}