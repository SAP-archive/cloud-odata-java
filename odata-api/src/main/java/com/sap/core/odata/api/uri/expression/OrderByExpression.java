package com.sap.core.odata.api.uri.expression;

import java.util.List;

public interface OrderByExpression 
{
  public List<CommonExpression> getOrders();

  public CommonExpression getOrdersCount();

}
