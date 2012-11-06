package com.sap.core.odata.api.uri.expression;

import java.util.List;

public interface OrderByExpression 
{
  public List<CommonExpression> GetOrders();

  public CommonExpression GetOrdersCount();

  public CommonExpression AppendOrder();

}
