package com.sap.core.odata.api.uri.expression;

import java.util.List;

/**
 * Represents a binary expression in the expression tree return by the {@link FilterParser#ParseExpression(String)} or 
 * {@link OrderByParser#parseOrderExpression(String)} 
 * 
 * @author SAP AG
 */
public interface OrderByExpression 
{
  public List<CommonExpression> getOrders();

  public CommonExpression getOrdersCount();

}
