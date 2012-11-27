package com.sap.core.odata.api.uri.expression;


public interface FilterExpression extends Visitable
{
  String getFilterExpression();

  CommonExpression getExpression();
 
}
